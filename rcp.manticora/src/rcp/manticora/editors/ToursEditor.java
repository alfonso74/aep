package rcp.manticora.editors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.ToursController;
import rcp.manticora.dialogs.AgregarLineaTour;
import rcp.manticora.dialogs.DefinirDisponibilidad;
import rcp.manticora.dialogs.EditarDisponibilidad;
import rcp.manticora.dialogs.ImportarTemplate2Tour;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.model.LineaTour;
import rcp.manticora.model.Tour;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.DaySeqComparator;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.GenericSorter;
import rcp.manticora.services.OcupacionGantt;
import rcp.manticora.services.Productos;
import rcp.manticora.views.ToursView;

public class ToursEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.tours";
	private final String pluginId = Application.PLUGIN_ID;
	
	private Tour registro;
	// campos del formulario (usualmente asociados al tour)
	private Text txtNombre;
	private Text txtCodigo;
	private Text txtEstado;
	private Text txtCapacidad;
	private Combo comboTipoProd;
	private Combo comboProd;
	private Text txtComentario;
	
	private Set<String> vDiasAct;
	// objetos de apoyo (contenido de comboboxes y relacion tipoProducto-Producto
	private ComboData cdTipoProd;
	private ComboData cdEstado;
	private Productos productos;
	private ComboDataController cdController;
	private ToursController editorController;
	
	private TableViewer viewerAct;
	private TableViewer viewerDisp;
	private TableTreeViewer viewerAct2;
	
	private ImageDescriptor image;

	public ToursEditor() {
		editorController = new ToursController(ID);
		cdController = new ComboDataController();
		vDiasAct = new HashSet<String>();
	}
	

	public void doSave(IProgressMonitor monitor) {
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		// preparamos la variables para crear/actualizar el registro de tour
		String pNombre = txtNombre.getText();
		Integer pCapacidad = txt2Integer(txtCapacidad.getText());
		Long pIdProducto = productos.getIdProductoByName(comboProd.getText());
		//productos.get
		String pComentario = txtComentario.getText();
		String pEstado = cdEstado.getCodeByName(txtEstado.getText());
		
		registro.setNombre(pNombre);
		registro.setIdProducto(pIdProducto);
		registro.setCapacidad(pCapacidad);
		registro.setComentario(pComentario);
		registro.setEstado(pEstado);
		registro.setDspEstado(txtEstado.getText());
		registro.setDspProducto(comboProd.getText());
		registro.setDspTipoProducto(comboTipoProd.getText());
		
		editorController.doSave(registro);
		if (isNewDoc) {
			txtCodigo.setText(valor2Txt(registro.getIdTour()));
			isNewDoc = false;
		}
		
		this.setPartName(registro.getTituloDocumento());
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		actualizarVista(ToursView.ID);
		removeDirtyFlag();
	}
	
	private boolean validarSave() {
		String pNombre = txtNombre.getText();
		
		if (pNombre.length() > 40) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre del tour no puede superar los 40 caracteres (" + pNombre.length() + ").");
			return false;
		}
		if (registro.getListaActividades().size() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"Debe definir actividades antes de guardar el tour.");
			return false;
		}
		if (registro.getListaDisponibilidad().size() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"Debe definir la disponibilidad antes de guardar el tour.");
			return false;
		}
		return true;
	}

	protected void agregarControles(Composite parent) {
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		// llena el espacio vertical y horizontal
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// tab de datos generales
		TabItem tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("Información general");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/infoGeneral2.gif");
		tabGeneral.setImage(image.createImage());
		tabGeneral.setControl(crearTabGeneral(tabFolder));
		// tab de actividades
		
		TabItem tabActividades = new TabItem(tabFolder, SWT.NONE);
		tabActividades.setText("Actividades");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/actividades.gif");
		tabActividades.setImage(image.createImage());
		tabActividades.setControl(crearTabActividades(tabFolder));
		
		// tab de disponibilidad
		TabItem tabDisponibilidad = new TabItem(tabFolder, SWT.NONE);
		tabDisponibilidad.setText("Calendario");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/calendar.gif");
		tabDisponibilidad.setImage(image.createImage());
		tabDisponibilidad.setControl(crearTabDisponibilidad(tabFolder));
		
		// tab de prueba de categorías (tableTreeViewer)
		TabItem tabTreeViewer = new TabItem(tabFolder, SWT.NONE);
		tabTreeViewer.setText("Categorías");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/aviso.gif");
		tabTreeViewer.setImage(image.createImage());
		tabTreeViewer.setControl(crearTabCategorias(tabFolder));
		
		// tab de gantt chart
		TabItem tabGantt = new TabItem(tabFolder, SWT.NONE);
		tabGantt.setText("Gantt");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/aviso.gif");
		tabGantt.setImage(image.createImage());
		tabGantt.setControl(crearTabGantt(tabFolder));
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo tour...");
			registro = new Tour();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
			txtEstado.setText("Activo");
			viewerAct.setInput(registro.getListaActividades());
			viewerDisp.setInput(registro.getListaDisponibilidad());
			viewerAct2.setInput(registro.getListaActividades());
		} else {
			System.out.println("Ejecutando código para cargar datos...");
			registro = (Tour) ((CommonEditorInput) this.getEditorInput()).getElemento();
			editorController.getSession().refresh(registro);
			
			txtNombre.setText(registro.getNombre());
			txtCodigo.setText(valor2Txt(registro.getIdTour()));
			txtCapacidad.setText(valor2Txt(registro.getCapacidad()));
			txtEstado.setText(valor2Txt(registro.getDspEstado()));
			comboTipoProd.setText(registro.getDspTipoProducto());
			comboProd.setText(registro.getDspProducto());
			txtComentario.setText(registro.getComentario());
			viewerAct.setInput(registro.getListaActividades());
			viewerDisp.setInput(registro.getListaDisponibilidad());
			viewerAct2.setInput(registro.getListaActividades());

			inicializarDiasAct(registro.getListaActividades());
			viewerAct.refresh();
			viewerDisp.refresh();
			viewerAct2.refresh();
			//viewerAct2.expandAll();
		}
		addFilledFlag();
		setFocoInicial(txtNombre);
		viewerAct2.expandAll();
	}
	
	private void inicializarDiasAct(Set<LineaTour> listado) {
		LineaTour linea = null;
		Iterator<LineaTour> it = listado.iterator();
		while (it.hasNext()) {
			linea = it.next();
			vDiasAct.add("Día " + linea.getDia().toString());
		}
		Iterator<String> it2 = vDiasAct.iterator();
		while (it2.hasNext()) {
			System.out.println("Dia: " + it2.next());
		}
	}
	
	private Control crearTabGeneral(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 8;
		gridLayout.marginWidth = 15;
		gridLayout.marginHeight = 10;
		composite.setLayout(gridLayout);
		
		Label l;
		//GridLayout gridLayout;
		GridData gridData;
		
		cdTipoProd = cdController.getComboDataTipoProductosTour(true);
		cdEstado = cdController.getComboDataKeyword("Status general");
		productos = new Productos();
		
		l = new Label(composite, SWT.NONE);
		l.setText("Nombre:");
		txtNombre = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.TOP, false, false, 4, 1);
		txtNombre.setLayoutData(gridData);
		txtNombre.addModifyListener(this.createModifyListener());
		
		l = new Label(composite, SWT.NONE);
		l.setText("Código:");
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
		l.setLayoutData(gridData);
		txtCodigo = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gridData.widthHint = 50;
		txtCodigo.setLayoutData(gridData);
		txtCodigo.setEnabled(false);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Capacidad:");
		txtCapacidad = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(SWT.LEFT, SWT.FILL, false, false, 5, 1);
		gridData.widthHint = 30;
		txtCapacidad.setLayoutData(gridData);
		txtCapacidad.addModifyListener(this.createModifyListener());
		
		l = new Label(composite, SWT.NONE);
		l.setText("Estado:");
		l.setLayoutData(new GridData(45,15));
		l.setAlignment(SWT.RIGHT);
		txtEstado = new Text(composite, SWT.SINGLE | SWT.BORDER);
		txtEstado.setLayoutData(new GridData(50,15));
		txtEstado.setEnabled(false);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo prod:");
		comboTipoProd = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gridData.widthHint = 120;
		comboTipoProd.setLayoutData(gridData);
		comboTipoProd.setItems(cdTipoProd.getTexto());
// Invocado durante el llenarControles y encargado de inicializar los campos de
// comboTipoProd y comboProd.  Luego es redundante con el selectionListener
		comboTipoProd.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				System.out.println("Cambio en comboTipoProd");
				if (comboTipoProd.getSelectionIndex() != -1){
					long seleccionado = cdTipoProd.getKeyAsLongByIndex(comboTipoProd.getSelectionIndex());
					productos.filtrarByTipo(seleccionado, true);
				}
				comboProd.setItems(productos.getTexto());
				if (filled) {
					addDirtyFlag();
				}
			}
		});
// Filtra el comboProd de acuerdo al tipo de producto seleccionado
		comboTipoProd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("comboTipoProd: selección");
				if (comboTipoProd.getSelectionIndex() != -1){
					long seleccionado = cdTipoProd.getKeyAsLongByIndex(comboTipoProd.getSelectionIndex());
					productos.filtrarByTipo(seleccionado, true);
				}
				comboProd.setItems(productos.getTexto());
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Producto:");
		comboProd = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(SWT.LEFT, SWT.FILL, false, false, 4, 1);
		gridData.widthHint = 120;
		comboProd.setLayoutData(gridData);
		comboProd.addModifyListener(this.createModifyListener());
		
		l = new Label(composite, SWT.NONE);
		l.setText("Comentario:");
		l.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		txtComentario = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 7, 1);
		gridData.heightHint = 55;
		txtComentario.setLayoutData(gridData);
		txtComentario.addModifyListener(this.createModifyListener());
		
		
		return composite;
	}
	
	
	private Control crearTabActividades(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 15;
		gridLayout.marginHeight = 10;
		composite.setLayout(gridLayout);
		
		Table tablaAct = crearTablaActividades(composite);
		viewerAct = new TableViewer(tablaAct);
		viewerAct.setContentProvider(new ViewContentProviderAct());
		viewerAct.setLabelProvider(new ViewLabelProviderAct());
		// aquí utilizamos ordenamiento del array y de la db para la relación
		// de día y secuencia (usando la clase DaySeqComparer)
		// en crearTabDisponibilidad utilizamos el GenericSorter() porque es una sola columna
		viewerAct.setComparator(new DaySeqComparator());
		
		//registramos al viewer como un selection provider para determinar
		//qué elemento está seleccionado
		getSite().setSelectionProvider(viewerAct);

		Composite botones = new Composite(composite, SWT.NONE);
		gridLayout = new GridLayout(5, false);
		botones.setLayout(gridLayout);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		botones.setLayoutData(gridData);
		
		Button bAgregar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bAgregar.setLayoutData(gridData);
		bAgregar.setText("Agregar");
		bAgregar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de agregar...");
				agregarActividad(getSite().getShell());
			}
		});
		
		Button bEditar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bEditar.setLayoutData(gridData);
		bEditar.setText("Editar");
		bEditar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de editar...");
				editarActividad(getSite().getShell());
			}
		});
		
		Button bBorrar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bBorrar.setLayoutData(gridData);
		bBorrar.setText("Borrar");
		bBorrar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de borrar actividad...");
				LineaTour linea = getActividadSeleccionada();
				editorController.eliminarActividad(registro, linea);
				viewerAct.remove(linea);
				reenumerarLineasTour(viewerAct);
				addDirtyFlag();   // como borramos, indicamos que se ha modificado la cotización
			}
		});	
		
		Button bTours = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bTours.setLayoutData(gridData);
		bTours.setText("Template");
		bTours.setToolTipText("Agrega un grupo de actividades predefinidas");
		bTours.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de agregar template...");
				ImportarTemplate2Tour dialogo = new ImportarTemplate2Tour(getSite().getShell(), "Importar template (tour)");
				if (dialogo.open() == IDialogConstants.OK_ID) {
					Long idTemplate = dialogo.getIdTemplate();
					System.out.println("Código template: " + idTemplate);
					editorController.importarLineasFromTemplate(registro, idTemplate);
					viewerAct.refresh();
					addDirtyFlag();
				} else {
					MessageDialog.openInformation(getSite().getShell(), "Información", "La acción ha sido cancelada.");
				}
			}
		});
		
		Composite derecha = new Composite(botones, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		derecha.setLayout(gridLayout);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, true);
		derecha.setLayoutData(gridData);
		
		Button bArriba = new Button(derecha, SWT.NONE);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.MOVEUP);
		gridData = new GridData(20, 20);
		gridData.horizontalAlignment = SWT.RIGHT;
		gridData.grabExcessHorizontalSpace = true;
		bArriba.setLayoutData(gridData);
		bArriba.setToolTipText("Mueve el elemento seleccionado una línea hacia arriba");
		bArriba.setImage(image.createImage());
		bArriba.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				LineaTour lineaSeleccionada = getActividadSeleccionada();
				LineaTour lineaTmp = lineaSeleccionada;
				int posInicial = viewerAct.getTable().getSelectionIndex();
				int posDestino = posInicial - 1;
				System.out.println("Posición seleccionada: " + posInicial);
				if (posInicial > 0) {
					lineaTmp = (LineaTour) viewerAct.getElementAt(posDestino);
					// si pertenecen al mismo día, intercambiamos la secuencia
					if (lineaTmp.getDia().intValue() == lineaSeleccionada.getDia().intValue()) {
						Integer tmpSeq = lineaTmp.getSecuencia();
						lineaTmp.setSecuencia(lineaSeleccionada.getSecuencia());
						lineaSeleccionada.setSecuencia(tmpSeq);
						viewerAct.refresh();
					} else {
						// asignamos el nuevo dia/seq
						lineaSeleccionada.setDia(lineaTmp.getDia());
						lineaSeleccionada.setSecuencia(lineaTmp.getSecuencia() + 1);
						// y reenumeramos la secuencia de las líneas que vienen
						// luego (hacia abajo) de la línea que se seleccionó para mover
						reenumerarLineasTour(viewerAct, posInicial + 1);
					}
					addDirtyFlag();
				} else {
					System.out.println("Omitiendo reenumeración de líneas");
				}
			}
		});
		
		Button bAbajo = new Button(derecha, SWT.NONE);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.MOVEDOWN);
		gridData = new GridData(20, 20);
		//gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.RIGHT;
		bAbajo.setLayoutData(gridData);
		bAbajo.setToolTipText("Mueve el elemento seleccionado una línea hacia abajo");
		bAbajo.setImage(image.createImage());
		bAbajo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				LineaTour lineaSeleccionada = getActividadSeleccionada();
				LineaTour lineaTmp = lineaSeleccionada;
				int posInicial = viewerAct.getTable().getSelectionIndex();
				int posDestino = posInicial + 1;
				System.out.println("Posición seleccionada: " + posInicial);
				if (posInicial < (viewerAct.getTable().getItemCount() - 1)) {
					lineaTmp = (LineaTour) viewerAct.getElementAt(posDestino);
					// si pertenecen al mismo día, intercambiamos la secuencia
					if (lineaTmp.getDia().intValue() == lineaSeleccionada.getDia().intValue()) {
						Integer tmpSeq = lineaTmp.getSecuencia();
						lineaTmp.setSecuencia(lineaSeleccionada.getSecuencia());
						lineaSeleccionada.setSecuencia(tmpSeq);
						viewerAct.refresh();
					} else {
						// asignamos el nuevo dia/seq (lineaSel será la primera del día)
						lineaSeleccionada.setDia(lineaTmp.getDia());
						lineaSeleccionada.setSecuencia(0);
						// y reenumeramos la secuencia de las líneas correspondientes
						// al nuevo día de la linea seleccionada
						reenumerarLineasTour(viewerAct, posInicial);
					}
					addDirtyFlag();
				} else {
					System.out.println("Omitiendo reenumeración de líneas");
				}
			}
		});
		
		return composite;
	}
	
	private Table crearTablaActividades(Composite parent) {
		//int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;
		Table tablaAct = new Table(parent, style);
		tablaAct.setLinesVisible(true);
		tablaAct.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tablaAct.setLayoutData(gridData);
		
		TableColumn column = new TableColumn(tablaAct, SWT.CENTER, 0);
		column.setText("Día");
		column.setWidth(33);
		column.setAlignment(SWT.LEFT);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 1);
		column.setText("Sec");
		column.setWidth(33);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 2);
		column.setText("Actividad");
		column.setWidth(190);
		
		column = new TableColumn(tablaAct, SWT.CENTER, 3);
		column.setText("BLD");
		column.setWidth(40);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 4);
		column.setText("Comentarios");
		column.setWidth(250);
		
		return tablaAct;
	}
	
	class ViewContentProviderAct implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set<LineaTour>) registro.getListaActividades()).toArray();
			return resultados;
		}
	}
	
	
	class ViewLabelProviderAct extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			LineaTour linea = (LineaTour) obj;
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
				resultado = linea.getComidas();
				break;
			case 4:
				resultado = linea.getComentario();
				break;
			}
			return resultado;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}

	
	private Control crearTabDisponibilidad(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 15;
		gridLayout.marginHeight = 10;
		composite.setLayout(gridLayout);
		
		Table tablaDisp = crearTablaDisponibilidad(composite);
		viewerDisp = new TableViewer(tablaDisp);
		viewerDisp.setContentProvider(new ViewContentProviderDisp());
		viewerDisp.setLabelProvider(new ViewLabelProviderDisp());
		viewerDisp.setSorter(new GenericSorter(viewerDisp));
		
// registramos al viewer como un selection provider para determinar
// qué elemento está seleccionado
		getSite().setSelectionProvider(viewerDisp);

		Composite botones = new Composite(composite, SWT.NONE);
		gridLayout = new GridLayout(4, false);
		botones.setLayout(gridLayout);
		
		Button bFechas = new Button(botones, SWT.PUSH);
		bFechas.setLayoutData(new GridData(80,20));
		bFechas.setText("Definir rango");
		bFechas.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de definir fechas...");
				definirDisponibilidad(getSite().getShell());
			}
		});
		
		Button bEditar = new Button(botones, SWT.PUSH);
		bEditar.setLayoutData(new GridData(80,20));
		bEditar.setText("Editar");
		bEditar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de editar...");
				DisponibilidadTour linea = getDisponibilidadSeleccionada();
				if (linea != null) {
					EditarDisponibilidad dialogo = new EditarDisponibilidad(getSite().getShell(), linea);
					if (dialogo.open() == IDialogConstants.OK_ID) {
						viewerDisp.refresh();
						addDirtyFlag();
					}
				}
			}
		});
		
		Button bBorrar = new Button(botones, SWT.PUSH);
		bBorrar.setLayoutData(new GridData(80,20));
		bBorrar.setText("Borrar");
		bBorrar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de borrar...");
				DisponibilidadTour linea = getDisponibilidadSeleccionada();
				if (linea != null) {
					System.out.println("Borrando línea de disponibilidad...");
					editorController.eliminarDisponibilidad(registro, linea);
					viewerDisp.refresh();
					addDirtyFlag();
				}
			}
		});
		
		Button bGantt = new Button(botones, SWT.PUSH);
		bGantt.setLayoutData(new GridData(80,20));
		bGantt.setText("Gantt");
		bGantt.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				OcupacionGantt gantt = new OcupacionGantt(getSite().getShell().getDisplay());
				gantt.open();
			}
		});
		
		return composite;
	}
	
	
	private Table crearTablaDisponibilidad(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Table tablaAct = new Table(parent, style);
		tablaAct.setLinesVisible(true);
		tablaAct.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tablaAct.setLayoutData(gridData);
		
		TableColumn column = new TableColumn(tablaAct, SWT.CENTER, 0);
		column.setText("Fecha");
		column.setWidth(80);
		column.setAlignment(SWT.LEFT);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewerDisp.getSorter()).doSort(0, GenericSorter.FECHA, 1, true);
				viewerDisp.refresh();
			}
		});
		
		column = new TableColumn(tablaAct, SWT.CENTER, 1);
		column.setText("Capac.");
		column.setWidth(65);
		column.setAlignment(SWT.LEFT);

		column = new TableColumn(tablaAct, SWT.CENTER, 2);
		column.setText("Número de tour");
		column.setWidth(90);
		column.setAlignment(SWT.LEFT);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 3);
		column.setText("Tipo");
		column.setWidth(70);
		
		column = new TableColumn(tablaAct, SWT.CENTER, 4);
		column.setText("Comentarios");
		column.setWidth(230);
		column.setAlignment(SWT.LEFT);
		
		return tablaAct;
	}
	
	
	class ViewContentProviderDisp implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set<DisponibilidadTour>) inputElement).toArray();
			return resultados;
		}
	}
	
	
	
	
	class ViewLabelProviderDisp extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			DisponibilidadTour linea = (DisponibilidadTour) obj;
			switch (index) {
			case 0:
				resultado = FechaUtil.toString(linea.getFecha());
				break;
			case 1:
				resultado = valor2Txt(linea.getCapacidad());
				break;
			case 2:
				resultado = linea.getNumero();
				if (resultado == null || resultado.equals("")) {
					resultado = "Por asignar";
				}
				break;
			case 3:
				resultado = linea.getTipo();
				break;
			case 4:
				resultado = linea.getComentario();
				break;
			}
			return resultado;
			
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}
	
	
	private Control crearTabCategorias(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 15;
		gridLayout.marginHeight = 10;
		composite.setLayout(gridLayout);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gridData);
		
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		viewerAct2 = new TableTreeViewer(composite, style);
		@SuppressWarnings("deprecation")
		Table tablaAct = viewerAct2.getTableTree().getTable();
		crearTablaCatActividades(tablaAct);
		viewerAct2.getTableTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewerAct2.setContentProvider(new ActivityTreeContentProvider());
		viewerAct2.setLabelProvider(new ActivityTreeLabelProvider());
		//viewerAct2.setInput(registro.getListaActividades());
		//viewerAct2.setSorter(new GenericSorter());
		
		return composite;
	}
	
	
	private Control crearTabGantt(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 15;
		gridLayout.marginHeight = 10;
		composite.setLayout(gridLayout);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gridData);
		OcupacionGantt gantt = new OcupacionGantt(composite);
		return composite;
	}
	
	
	private void crearTablaCatActividades(Table tablaAct) {
		//int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;
		tablaAct.setLinesVisible(true);
		tablaAct.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tablaAct.setLayoutData(gridData);
		
		TableColumn column = new TableColumn(tablaAct, SWT.LEFT, 0);
		column.setText("Día");
		column.setWidth(50);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewerAct2.getSorter()).doSort(0, GenericSorter.TEXTO);
				viewerAct2.refresh();
			}
		});
		
		column = new TableColumn(tablaAct, SWT.LEFT, 1);
		column.setText("Sec");
		column.setWidth(33);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 2);
		column.setText("Actividad2");
		column.setWidth(180);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 3);
		column.setText("BLD");
		column.setWidth(40);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 4);
		column.setText("Comentarios");
		column.setWidth(230);
	}
	
	
	class ActivityTreeContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			System.out.println("JAAA!!!");
			if (parentElement instanceof String) {
				String diaBase = (String) parentElement;
				String dia;
				Vector<LineaTour> v = new Vector<LineaTour>();
				LineaTour linea = null;
				Iterator<LineaTour> it = registro.getListaActividades().iterator();
				while (it.hasNext()) {
					linea = it.next();
					dia = "Día " + linea.getDia();
					System.out.println("Dia base: " + diaBase + ", dia: " + dia);
					if (dia.equals(diaBase)) {
						System.out.println("Igual!!");
						v.add(linea);
					}
				}
				Object[] resultados = v.toArray(new LineaTour[v.size()]);
				return resultados;
			} else {
				return null;
			}
		}

		public Object getParent(Object element) {
			String dia = "Día " + ((LineaTour) element).getDia(); 
			return dia;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof String) {
				return true;
			} else {
				return false;
			}
		}

		public Object[] getElements(Object inputElement) {
			// return getChildren(inputElement);
			Object[] resultados = vDiasAct.toArray(new String[vDiasAct.size()]);
			return resultados;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
	}
	
	
	class ActivityTreeLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			if (obj instanceof LineaTour) {
				String resultado = "";
				LineaTour linea = (LineaTour) obj;
				switch (index) {
				case 1:
					resultado = valor2Txt(linea.getSecuencia());
					break;
				case 2:
					resultado = linea.getDspProducto();
					break;
				case 3:
					resultado = linea.getComidas();
					break;
				case 4:
					resultado = linea.getComentario();
					break;
				}
				return resultado;
			}
			if (obj instanceof String) {
				String resultado = "";
				switch (index) {
				case 0:
					resultado = (String) obj;
				}
				return resultado;
			}
			return "NADA";
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}
	
	
	private void agregarActividad(Shell shell) {
		LineaTour linea = new LineaTour();
		LineaTour base = getActividadBase();
		AgregarLineaTour dialogo = new AgregarLineaTour(shell, linea, base);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			linea = dialogo.getLinea();
			System.out.println("LineaTour: " + linea.getDspProducto());
			editorController.agregarActividad(registro, linea);
			viewerAct.add(linea);
			reenumerarLineasTour(viewerAct);
			viewerAct.refresh();
			addDirtyFlag();
		}
	}
	
	private void definirDisponibilidad(Shell shell) {
		//DefinirDisponibilidad dialogo = new DefinirDisponibilidad(shell, registro, vLineasDisp);
		DefinirDisponibilidad dialogo = new DefinirDisponibilidad(shell, registro, registro.getListaDisponibilidad());
		dialogo.open();
		viewerDisp.refresh();
		addDirtyFlag();
	}
	
	private void editarActividad(Shell shell) {
		LineaTour linea = getActividadSeleccionada();
		LineaTour base = getActividadBase();
		AgregarLineaTour dialogo = new AgregarLineaTour(shell, linea, base);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			viewerAct.refresh(linea);
			reenumerarLineasTour(viewerAct);
			viewerAct.refresh();
			addDirtyFlag();
		}
	}
	
	/**
	 * Obtiene la actividad que ha sido seleccionada por el usuario final
	 * @return LineaTour seleccionada o null si no hay selección
	 */
	private LineaTour getActividadSeleccionada() {
		Object seleccion = ((IStructuredSelection) viewerAct.getSelection()).getFirstElement();
		LineaTour actividad = (LineaTour) seleccion;
		return actividad;
	}
	
	/**
	 * Obtiene la actividad seleccionada por el usuario final o la última
	 * actividad de la lista
	 * @return LineaTour seleccionada o la última línea del listado
	 */
	private LineaTour getActividadBase() {
		LineaTour l = getActividadSeleccionada();
		if (l == null) {
			int n = viewerAct.getTable().getItemCount();
			if (n > 0) {
				l = (LineaTour) viewerAct.getElementAt(n - 1);
			}
		}
		return l;
	}
	
	
	/**
	 * Asigna una nueva secuencia a las líneas pertenecientes a un mismo día
	 * 
	 */
	private void reenumerarLineasTour(TableViewer viewer, int posInicial) {
		int secuencia = 1;
		LineaTour linea = (LineaTour) viewer.getElementAt(posInicial++);
		int diaBase = linea.getDia().intValue();
		while (linea != null && linea.getDia().intValue() == diaBase) {
			linea.setSecuencia(secuencia++);
			linea = (LineaTour) viewer.getElementAt(posInicial++);
		}
		viewer.refresh();
		inicializarDiasAct(registro.getListaActividades());
		viewerAct2.refresh();
	}
	
	/**
	 * Reordena todas las líneas del template en base al día y secuencia
	 *
	 */
	private void reenumerarLineasTour(TableViewer viewer) {
//	para tener los días en base a 0 se pone diaBase, secuenciaBase, diaLinea y secuencia en 0.  Para base 1 en 1.
		LineaTour linea;
		int diaBase = 1;
		int secuenciaBase = 1;
		int diaLinea = 1;
		int secuencia = secuenciaBase;
		for (int n = 0; n < viewer.getTable().getItemCount(); n++) {
			linea = (LineaTour) viewer.getElementAt(n);
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
		// líneas para actualizar el viewer categorizado
		inicializarDiasAct(registro.getListaActividades());
		viewerAct2.refresh();
	}
	
	
	private DisponibilidadTour getDisponibilidadSeleccionada() {
		Object seleccion = ((IStructuredSelection) viewerDisp.getSelection()).getFirstElement();
		DisponibilidadTour disponibilidad = (DisponibilidadTour) seleccion;
		return disponibilidad;
	}
	
	
	/*
	class DaySeqComparer implements Comparator<LineaTour> {
		public int compare(LineaTour arg0, LineaTour arg1) {
			int dia0 = ((LineaTour) arg0).getDia().intValue();
			int dia1 = ((LineaTour) arg1).getDia().intValue();
			int seq0 = ((LineaTour) arg0).getSecuencia().intValue();
			int seq1 = ((LineaTour) arg1).getSecuencia().intValue();
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
	
	
	/*
	class DisponibilidadComparer implements Comparator<DisponibilidadTour> {
		public int compare(DisponibilidadTour arg0, DisponibilidadTour arg1) {
			long fecha0 = arg0.getFecha().getTime();
			long fecha1 = arg1.getFecha().getTime();
			if (fecha0 > fecha1) {
				return 1;
			} else if (fecha0 < fecha1) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	*/


	@Override
	public void dispose() {
		if (this.isDirty()) {
			actualizarVista(ToursView.ID);
		}
		editorController.finalizar(ID);
		super.dispose();
	}
}
