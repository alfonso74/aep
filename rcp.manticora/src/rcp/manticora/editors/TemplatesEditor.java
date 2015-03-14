package rcp.manticora.editors;

import java.util.Set;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.TemplatesController;
import rcp.manticora.dialogs.AgregarActividadTemplate;
import rcp.manticora.model.LineaTemplate;
import rcp.manticora.model.Template;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.DaySeqComparator;
import rcp.manticora.services.Productos;
import rcp.manticora.views.TemplatesView;

public class TemplatesEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.templates";
	private final String pluginId = Application.PLUGIN_ID;
	
	private Table tabla;
	private TableViewer viewer;
	private Productos productos;
	
	private Combo comboTipo;
	private Combo comboGira;
	private Text txtNombre;
	private Text txtCodigo;
	
	private Button bNuevo;
	private Button bEditar;
	private Button bBorrar;
	private Button bTemplate;
	private Button bArriba;
	private Button bAbajo;
		
	private ComboData comboData;
	private Template registro;
	private TemplatesController editorController;
	private ComboDataController cdController;
	
	private ImageDescriptor image;

	public TemplatesEditor() {
		editorController = new TemplatesController(ID);
		cdController = new ComboDataController();
		productos = new Productos();
	}

	public void doSave(IProgressMonitor monitor) {
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		// obtenemos el código de producto
		int pos = comboGira.getSelectionIndex();
		Long codigoProducto = productos.getIdProductoByIndex(pos);
		
		reenumerarLineasTemplate(viewer);

		registro.setIdProducto(codigoProducto);
		registro.setNombre(txtNombre.getText());
		
		editorController.doSave(registro);
		if (isNewDoc) {
			txtCodigo.setText(valor2Txt(registro.getIdTemplate()));
			isNewDoc = false;
		}
		
		// reflejamos el nombre en el tab
		this.setPartName(registro.getTituloDocumento());
		// actualizamos el nombre del editorInput (para que deje de parecer "Nuevo")
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		actualizarVista(TemplatesView.ID);
		removeDirtyFlag();
	}
	
	private boolean validarSave() {
		String pNombre = txtNombre.getText();
		
		if (pNombre.length() > 30) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre del template no puede superar los 30 caracteres (" + pNombre.length() + ").");
			return false;
		}
		if (registro.getListaActividades().size() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"Debe definir actividades antes de guardar el template.");
			return false;
		}
		return true;
	}
	
	/**
	 * Asigna una nueva secuencia a las líneas pertenecientes a un mismo día
	 * 
	 */
	private void reenumerarLineasTemplate(TableViewer viewer, int posInicial) {
		int secuencia = 1;
		LineaTemplate linea = (LineaTemplate) viewer.getElementAt(posInicial++);
		int diaBase = linea.getDia().intValue();
		while (linea != null && linea.getDia().intValue() == diaBase) {
			linea.setSecuencia(secuencia++);
			linea = (LineaTemplate) viewer.getElementAt(posInicial++);
		}
		viewer.refresh();
	}
	
	/**
	 * Reordena todas las líneas del template en base al día y secuencia
	 *
	 */
	private void reenumerarLineasTemplate(TableViewer viewer) {
//	para base 0 se pone diaBase, secuenciaBase, diaLinea y secuencia en 0.  Para base 1 en 1.
		LineaTemplate linea;
		int diaBase = 1;
		int secuenciaBase = 1;
		int diaLinea = 1;
		int secuencia = secuenciaBase;
		for (int n = 0; n < viewer.getTable().getItemCount(); n++) {
			linea = (LineaTemplate) viewer.getElementAt(n);
			//System.out.println(linea.getDspProducto());
			diaLinea = linea.getDia().intValue();
			if (diaLinea == diaBase) {
				System.out.println("Base igual a línea (" + linea.getDspProducto() + "): " + diaBase + ", " + diaLinea);
				linea.setSecuencia(secuencia++);
			} else {
				System.out.println("Base diferente a línea (" + linea.getDspProducto() + "): " + diaBase + ", " + diaLinea);
				diaBase = diaLinea;
				secuencia = secuenciaBase;
				linea.setSecuencia(secuencia++);
			}
		};
		viewer.refresh();
	}
	
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(4, false);
		layout.marginTop = 15;
		layout.marginLeft = 10;
		parent.setLayout(layout);
		agregarControles(parent);
		llenarControles();
	}

	protected void agregarControles(Composite parent) {
		Label l;
		l = new Label(parent, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(parent, SWT.READ_ONLY);
		comboData = cdController.getComboDataTipoProductosTour(true);
		comboTipo.setItems(comboData.getTexto());
		comboTipo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
// Si se cambia la selección, se determina el tp seleccionado y se filtra el combo de productos
				int indice = comboTipo.getSelectionIndex();
				if (indice != -1) {
					long seleccionado = comboData.getKeyAsLongByIndex(indice);
					productos.filtrarByTipo(seleccionado, true);
				};
				comboGira.setItems(productos.getTexto());
			}
		});
		comboTipo.addModifyListener(this.createModifyListener());
		
		// el combo de gira sólo contiene productos de tipo "TOUR"
		l = new Label(parent, SWT.NONE);
		l.setText("Gira:");
		comboGira = new Combo(parent, SWT.READ_ONLY);
		comboGira.setLayoutData(new GridData(120,15));
		comboGira.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Nombre:");
		txtNombre = new Text(parent, SWT.BORDER);
		txtNombre.setLayoutData(new GridData(220,15));
		txtNombre.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Código:");
		txtCodigo = new Text(parent, SWT.BORDER);
		txtCodigo.setLayoutData(new GridData(40,15));
		txtCodigo.setEditable(false);
		
		crearTabla(parent);
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setComparator(new DaySeqComparator());
		crearBotones(parent);
	}

	
	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo template...");
			registro = new Template();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
			viewer.setInput(registro.getListaActividades());
		} else {
			System.out.println("Ejecutando código para cargar datos...");
			registro = (Template) ((CommonEditorInput) this.getEditorInput()).getElemento();
			
			// nos aseguramos de cargar el registro desde la base de datos para
			// evitar problemas de caché
			//registro = editorController.getTemplateById(registro.getIdTemplate());
			// el objeto registro está detached de hibernate, así que debemos abrir una sesión
			// y asignársela (con refresh).
			//Session session = HibernateUtil.getSessionFactory().openSession();
			//session.refresh(registro);
			editorController.getSession().refresh(registro);
			inicializarCamposProductos();
			txtNombre.setText(registro.getNombre());
			txtCodigo.setText(Long.toString(registro.getIdTemplate()));
			viewer.setInput(registro.getListaActividades());
			//session.close();
			viewer.refresh();
		};
		addFilledFlag();
		setFocoInicial(txtNombre);
	}
	
	/**
	 * Invocado durante el llenarControles y encargado de inicializar los campos de
	 * comboTipoProd y comboProd para un template que se está editando. 
	 */
	private void inicializarCamposProductos() {
		comboTipo.setText(registro.getDspTipoProducto());
		int indice = comboTipo.getSelectionIndex();
		if (indice != -1) {
			Long seleccionado = comboData.getKeyAsLongByIndex(indice);
			productos.filtrarTours(true);
			productos.filtrarByTipo(seleccionado, false);
		};
		comboGira.setItems(productos.getTexto());
		comboGira.setText(registro.getDspProducto());
	}
	
	
	public void crearTabla(Composite parent) {
		//int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		//GridData gridData = new GridData();
		gridData.horizontalSpan = 4;
		tabla.setLayoutData(gridData);
		
		TableColumn column;
		column = new TableColumn(tabla, SWT.CENTER, 0);
		column.setText("Día");
		column.setToolTipText("Día en que es realizada la actividad (el primer día es 0)");
		column.setWidth(33);
		
		column = new TableColumn(tabla, SWT.CENTER, 1);
		column.setText("Sec");
		column.setToolTipText("Secuencia de actividad en el día (iniciando en 0)");
		column.setWidth(33);
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Actividad");
		column.setWidth(190);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Tipo reserva");
		column.setWidth(80);
		
		column = new TableColumn(tabla, SWT.LEFT, 4);
		column.setText("Comentarios");
		column.setWidth(200);
		
		column = new TableColumn(tabla, SWT.LEFT, 5);
		column.setText("BLD");
		column.setWidth(40);
	}
	
	
	public void crearBotones(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(5, false);
		layout.marginHeight = 5;
		composite.setLayout(layout);
		//GridData gridData = new GridData();
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.horizontalSpan = 4;
		//gridData.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gridData);
		
		bNuevo = new Button(composite, SWT.NONE);
		bNuevo.setText("Agregar");
		bNuevo.setLayoutData(new GridData(70,20));
		bNuevo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				LineaTemplate linea = null;
				AgregarActividadTemplate dialogo = new AgregarActividadTemplate(getSite().getShell(), linea);
				// si se presionó OK entonces refrescamos y marcamos para grabar
				if (dialogo.open() == IDialogConstants.OK_ID) {
					linea = dialogo.getLinea();
					editorController.agregarActividad(registro, linea);
					viewer.add(linea);
					reenumerarLineasTemplate(viewer);
					addDirtyFlag();
				}
			}
		});
		
		bEditar = new Button(composite, SWT.PUSH);
		bEditar.setText("Editar");
		bEditar.setLayoutData(new GridData(70,20));
		bEditar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Object seleccion = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
				LineaTemplate linea = (LineaTemplate) seleccion;
				AgregarActividadTemplate dialogo = new AgregarActividadTemplate(getSite().getShell(), linea);
				if (dialogo.open() == IDialogConstants.OK_ID) {
					viewer.refresh();  // refresh total (líneas se reordenan)
					reenumerarLineasTemplate(viewer);
					addDirtyFlag();
				}
			}
		});
		
		bTemplate = new Button(composite, SWT.PUSH);
		bTemplate.setText("Template");
		bTemplate.setLayoutData(new GridData(70,20));
		bTemplate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openWarning(getSite().getShell(), "Información", "En construcción...");
			}
		});
		
		bBorrar = new Button(composite, SWT.NONE);
		bBorrar.setText("Borrar");
		bBorrar.setLayoutData(new GridData(70,20));
		bBorrar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				LineaTemplate linea = getActividadSeleccionada();
				editorController.eliminarActividad(registro, linea);
				// debemos remover el registro del viewer porque sino tendríamos
				// que hacer un viewer.refresh() antes de reenumerar
				viewer.remove(linea);
				reenumerarLineasTemplate(viewer);
				addDirtyFlag();     // como borramos, indicamos que se ha modificado el template
			}
		});
		
		Composite derecha = new Composite(composite, SWT.NONE);
		layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		derecha.setLayout(layout);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, true);
		derecha.setLayoutData(gridData);
		
		bArriba = new Button(derecha, SWT.NONE);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.MOVEUP);
		gridData = new GridData(20, 20);
		gridData.horizontalAlignment = SWT.RIGHT;
		gridData.grabExcessHorizontalSpace = true;
		bArriba.setLayoutData(gridData);
		bArriba.setToolTipText("Mueve el elemento seleccionado una línea hacia arriba");
		bArriba.setImage(image.createImage());
		bArriba.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				LineaTemplate lineaSeleccionada = getActividadSeleccionada();
				LineaTemplate lineaTmp = lineaSeleccionada;
				int posInicial = viewer.getTable().getSelectionIndex();
				int posDestino = posInicial - 1;
				System.out.println("Posición seleccionada: " + posInicial);
				if (posInicial > 0) {
					lineaTmp = (LineaTemplate) viewer.getElementAt(posDestino);
					// si pertenecen al mismo día, intercambiamos la secuencia
					if (lineaTmp.getDia().intValue() == lineaSeleccionada.getDia().intValue()) {
						Integer tmpSeq = lineaTmp.getSecuencia();
						lineaTmp.setSecuencia(lineaSeleccionada.getSecuencia());
						lineaSeleccionada.setSecuencia(tmpSeq);
						viewer.refresh();
					} else {
						// asignamos el nuevo dia/seq
						lineaSeleccionada.setDia(lineaTmp.getDia());
						lineaSeleccionada.setSecuencia(lineaTmp.getSecuencia() + 1);
						// y reenumeramos la secuencia de las líneas que vienen
						// luego (hacia abajo) de la línea que se seleccionó para mover
						reenumerarLineasTemplate(viewer, posInicial + 1);
					}
					addDirtyFlag();
				} else {
					System.out.println("Omitiendo reenumeración de líneas");
				}
			}
		});
		
		bAbajo = new Button(derecha, SWT.NONE);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.MOVEDOWN);
		gridData = new GridData(20, 20);
		//gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.RIGHT;
		bAbajo.setLayoutData(gridData);
		bAbajo.setToolTipText("Mueve el elemento seleccionado una línea hacia abajo");
		bAbajo.setImage(image.createImage());
		bAbajo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				LineaTemplate lineaSeleccionada = getActividadSeleccionada();
				LineaTemplate lineaTmp = lineaSeleccionada;
				int posInicial = viewer.getTable().getSelectionIndex();
				int posDestino = posInicial + 1;
				System.out.println("Posición seleccionada: " + posInicial);
				if (posInicial < (viewer.getTable().getItemCount() - 1)) {
					lineaTmp = (LineaTemplate) viewer.getElementAt(posDestino);
					// si pertenecen al mismo día, intercambiamos la secuencia
					if (lineaTmp.getDia().intValue() == lineaSeleccionada.getDia().intValue()) {
						Integer tmpSeq = lineaTmp.getSecuencia();
						lineaTmp.setSecuencia(lineaSeleccionada.getSecuencia());
						lineaSeleccionada.setSecuencia(tmpSeq);
						viewer.refresh();
					} else {
						// asignamos el nuevo dia/seq (lineaSel será la primera del día)
						lineaSeleccionada.setDia(lineaTmp.getDia());
						lineaSeleccionada.setSecuencia(0);
						// y reenumeramos la secuencia de las líneas correspondientes
						// al nuevo día de la linea seleccionada
						reenumerarLineasTemplate(viewer, posInicial);
					}
					addDirtyFlag();
				} else {
					System.out.println("Omitiendo reenumeración de líneas");
				}
			}
		});
	}
	
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			//LineaTemplate[] lin = new LineaTemplate[vLineas.size()];
			//Object[] resultados = vLineas.toArray(new LineaTemplate[vLineas.size()]);
			//Object[] resultados = ((Set) registro.getListaActividades()).toArray();
			//Object[] resultados = lineas.toArray();
			Object[] resultados = ((Set) parent).toArray();
			return resultados;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			LineaTemplate linea = (LineaTemplate) obj;
			switch (index) {
			case 0:
				resultado = valor2Txt(linea.getDia());
				break;
			case 1:
				resultado = valor2Txt(linea.getSecuencia());
				break;
			case 2:
				resultado = linea.getDspProducto();
				break;
			case 3:
				resultado = linea.getTipoReserva();
				break;
			case 4:
				resultado = linea.getComentario();
				break;
			case 5:
				resultado = linea.getComidas();
				break;
			}
			return resultado;
			
		}
		public Image getColumnImage(Object obj, int index) {
			/*
			String imageKey = IImageKeys.CHECK;
			ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
			LineaTemplate linea = (LineaTemplate) obj;
			String tr = "";
			switch(index) {
			case 0:
				tr = valor2Txt(linea.getTipoReserva());
				if (!tr.equals("No aplica")) {
					return image.createImage();
				} else {
					System.out.println("No");
				}
			}
			*/
			return null;
		}
	}
	
	public LineaTemplate getActividadSeleccionada() {
		Object seleccion = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
		LineaTemplate actividad = (LineaTemplate) seleccion;
		return actividad;
	}
	
	/*
	class DaySeqComparer implements Comparator<LineaTemplate> {
		public int compare(LineaTemplate arg0, LineaTemplate arg1) {
			int dia0 = ((LineaTemplate) arg0).getDia().intValue();
			int dia1 = ((LineaTemplate) arg1).getDia().intValue();
			int seq0 = ((LineaTemplate) arg0).getSecuencia().intValue();
			int seq1 = ((LineaTemplate) arg1).getSecuencia().intValue();
			if (dia0 > dia1) {
				return 1;
			} else if (dia0 < dia1) {
				return -1;
			} else {
				// dia1 es igual a dia0, así que comparamos en base a la secuencia
				if (seq0 > seq1) {
					return 1;
				} else if (seq0 < seq1) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}
	*/

	@Override
	public void dispose() {
		if (this.isDirty()){
			actualizarVista(TemplatesView.ID);
		}
		editorController.finalizar(ID);
		super.dispose();
	}
}

