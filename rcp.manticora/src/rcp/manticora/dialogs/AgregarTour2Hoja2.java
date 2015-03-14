package rcp.manticora.dialogs;

import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.BuscarResToursDTO;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.Productos;

public class AgregarTour2Hoja2 extends AbstractAEPTitleAreaDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private ComboDataController cdController;
	private Combo comboTipo;
	private Combo comboProducto;
	private Text txtFechaDesde;
	private Text txtFechaHasta;
	private Button bFecha;
	private Button bFechaHasta;
	
	private TableViewer viewer;
	private List<DisponibilidadTour> listadoTours;
	private DisponibilidadTour seleccion;
	
	private ImageDescriptor image;
	
	private Productos productos;
	private ComboData cdTipo;
	private Shell shell;

	public AgregarTour2Hoja2(Shell parentShell) {
		super(parentShell);
		this.shell = parentShell;
		cdController = new ComboDataController();
		productos = new Productos();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);

		GridLayout layout = new GridLayout(6, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);

		Label l;
		GridData gridData;
		
// ***************** sección de filtro (tipo, producto y fechas) ****************
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 2;
		comboTipo.setLayoutData(gridData);
		cdTipo = cdController.getComboDataTipoProductosTour(true);
		comboTipo.setItems(cdTipo.getTexto());
		comboTipo.add("Todos", 0);
		comboTipo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int indice = comboTipo.getSelectionIndex();
				if (indice != -1 && indice != 0) {
					Long seleccionado = cdTipo.getKeyAsLongByIndex(indice-1);
					productos.filtrarByTipo(seleccionado, true);   // filtramos si hay algún tipo de prod. seleccionado
				} else {
					productos.filtrarTours(true);  // si no hay selección de tipo, mostramos todos los productos tipo tour
				}
				comboProducto.setItems(productos.getTexto());
				comboProducto.add("Todos", 0);
				comboProducto.select(0);
				procesarDisponibilidad();
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tour:");
		comboProducto = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(200,15);
		gridData.horizontalSpan = 2;
		comboProducto.setLayoutData(gridData);
		comboProducto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				procesarDisponibilidad();
			}
		});
		productos.filtrarTours(true);
		comboProducto.setItems(productos.getTexto());
		comboProducto.add("Todos", 0);
		comboProducto.select(0);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Desde:");
		txtFechaDesde = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFechaDesde.setLayoutData(gridData);
		txtFechaDesde.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				procesarDisponibilidad();
			}
		});
		
		bFecha = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFechaDesde));
		bFecha.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				procesarDisponibilidad();
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Hasta:");
		txtFechaHasta = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFechaHasta.setLayoutData(gridData);
		txtFechaHasta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				procesarDisponibilidad();
			}
		});
		
		bFechaHasta = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
		bFechaHasta.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaHasta.setImage(image.createImage());
		bFechaHasta.addSelectionListener(this.crearCalendario(shell, txtFechaHasta, txtFechaDesde));
		bFechaHasta.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				procesarDisponibilidad();
			}
		});
		
// ************************ sección de lista de tours **************************
		
		Composite compLista = new Composite(composite, SWT.NULL);
		layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		compLista.setLayout(layout);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 6, 1);
		gridData.heightHint = 130;
		compLista.setLayoutData(gridData);
		
		viewer = new TableViewer(crearTablaDisponibilidad(compLista));
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(listadoTours);
		viewer.addSelectionChangedListener(this.createSelectionChangedListener());
		llenarCampos();
		
		return composite;
	}

	
	@Override
	protected void llenarCampos() {
		setTitle("Agregar tour a hoja de servicios");
		setMessage("Por favor, seleccione el tour a incluir en la hoja de servicios", IMessageProvider.INFORMATION);
		
		// por default, están seleccionados todos los tipos de tours disponibles para el siguiente día
		Date fechaSiguiente = FechaUtil.ajustarFecha(new Date(), 1);
		comboTipo.select(0);
		comboProducto.select(0);
		txtFechaDesde.setText(FechaUtil.toString(fechaSiguiente));
		txtFechaHasta.setText(FechaUtil.toString(fechaSiguiente));
		procesarDisponibilidad();
	}
	
	
	private ISelectionChangedListener createSelectionChangedListener() {
		return new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				seleccion = (DisponibilidadTour) ((IStructuredSelection) event.getSelection()).getFirstElement();
				System.out.println("Nueva selección: " + seleccion);
			}
		};
	}
	
	
	private Table crearTablaDisponibilidad(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Table tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tabla.setLayoutData(gridData);

		TableColumn column = new TableColumn(tabla, SWT.LEFT, 0);
		column.setText("Fecha");
		column.setWidth(75);
		
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Tour");
		column.setWidth(150);
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Ocup/Cap");
		column.setWidth(65);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Tipo");
		column.setWidth(65);
		
		column = new TableColumn(tabla, SWT.LEFT, 4);
		column.setText("Comentarios");
		column.setWidth(115);
		
		return tabla;
	}
	
	
	private List<DisponibilidadTour> buscarDisponibilidadTours() {
		BuscarResToursDTO buscar = new BuscarResToursDTO();
		
		if (comboTipo.getSelectionIndex() != -1 && comboTipo.getSelectionIndex() != 0) {
			String pTipo = comboTipo.getText();
			buscar.setTipo(pTipo);
		}
		if (comboProducto.getSelectionIndex() != -1 && comboProducto.getSelectionIndex() != 0) {
			String pProducto = comboProducto.getText();
			buscar.setProducto(pProducto);
		}
		if (!txtFechaDesde.getText().equals("")) {
			Date pFechaDesde = FechaUtil.toDate(txtFechaDesde.getText());
			buscar.setFechaDesde(pFechaDesde);
		}
		if (!txtFechaHasta.getText().equals("")) {
			Date pFechaHasta = FechaUtil.toDate(txtFechaHasta.getText());
			buscar.setFechaHasta(pFechaHasta);
		}
		
		return buscar.buscarReservasToursAsList();
	}
	
	
	public DisponibilidadTour getDisponibilidadSeleccionada() {
		return seleccion;
	}
	
	
	class ViewContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			List<DisponibilidadTour> elementos = (List<DisponibilidadTour>) inputElement;
			Object[] resultados = elementos.toArray();
			return resultados;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
	}
	
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			DisponibilidadTour linea = (DisponibilidadTour) element;
			String resultado = "";
			switch (columnIndex) {
			case 0:
				resultado = FechaUtil.toString(linea.getFecha());
				break;
			case 1:
				resultado = linea.getTour().getNombre();
				break;
			case 2:
				resultado = linea.getResumenDisp();
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
	}
	
	
	private void procesarDisponibilidad() {
		listadoTours = buscarDisponibilidadTours();
		viewer.setInput(listadoTours);
		viewer.refresh();
	}
	
}

