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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.HibernateUtil;
import rcp.manticora.services.Productos;

public class AgregarTour2Hoja extends AbstractAEPTitleAreaDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private Session session;
	private ComboDataController cdController;
	private Combo comboTipo;
	private Combo comboProducto;
	private Text txtFecha;
	private Button bFecha;
	
	//private Set listadoTours = new LinkedHashSet<DisponibilidadTour>();
	private TableViewer viewer;
	private List<DisponibilidadTour> listadoTours;
	private DisponibilidadTour seleccion;
	
	private ImageDescriptor image;
	
	private Productos productos;
	private ComboData cdTipo;
	private Shell shell;

	public AgregarTour2Hoja(Shell parentShell) {
		super(parentShell);
		this.shell = parentShell;
		session = HibernateUtil.getSessionFactory().openSession();
		cdController = new ComboDataController();
		productos = new Productos();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);

		GridLayout layout = new GridLayout(7, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);

		Label l;
		GridData gridData;
		
// ***************** sección de filtro (tipo, producto y fecha) ****************
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 1;
		comboTipo.setLayoutData(gridData);
		cdTipo = cdController.getComboDataTipoProductosTour(true);
		comboTipo.setItems(cdTipo.getTexto());
		comboTipo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int indice = comboTipo.getSelectionIndex();
				if (indice != -1) {
					Long seleccionado = cdTipo.getKeyAsLongByIndex(indice);
					productos.filtrarByTipo(seleccionado, true);       // eliminamos cualquier filtro previo
				}
				comboProducto.setItems(productos.getTexto());
				txtFecha.setText("");
				listadoTours = buscarDisponibilidadTours();
				viewer.refresh();
				
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tour:");
		comboProducto = new Combo(composite, SWT.READ_ONLY);
		comboProducto.setLayoutData(new GridData(120,15));
		comboProducto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				listadoTours = buscarDisponibilidadTours();
				viewer.refresh();
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Fecha:");
		txtFecha = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFecha.setLayoutData(gridData);
		
		bFecha = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFecha));
		bFecha.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				listadoTours = buscarDisponibilidadTours();
				viewer.refresh();
			}
		});
		
// ************************ sección de lista de tours **************************
		
		Composite compLista = new Composite(composite, SWT.NULL);
		layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		compLista.setLayout(layout);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 7, 1);
		gridData.heightHint = 100;
		compLista.setLayoutData(gridData);
		
		viewer = new TableViewer(crearTablaDisponibilidad(compLista));
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		listadoTours = buscarDisponibilidadTours();
		viewer.setInput(listadoTours);
		viewer.addSelectionChangedListener(this.createSelectionChangedListener());
		llenarCampos();
		
		return composite;
	}

	@Override
	protected void llenarCampos() {
		setTitle("Agregar tour a hoja de servicios");
		setMessage("Por favor, seleccione el tour a incluir en la hoja de servicios", IMessageProvider.INFORMATION);
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
		column.setText("Ocup/Disp");
		column.setWidth(65);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Tipo");
		column.setWidth(65);
		
		column = new TableColumn(tabla, SWT.LEFT, 4);
		column.setText("Comentarios");
		column.setWidth(115);
		
		return tabla;
	}
	
	@SuppressWarnings("unchecked")
	private List<DisponibilidadTour> buscarDisponibilidadTours() {
		Criteria criteria = session.createCriteria(DisponibilidadTour.class);
		if (comboTipo.getSelectionIndex() != -1) {
			String pTipo = comboTipo.getText();
			Criteria critTour = criteria.createCriteria("tour");
			critTour.add(Restrictions.eq("dspTipoProducto", pTipo));
			if (comboProducto.getSelectionIndex() != -1) {
				String pProducto = comboProducto.getText();
				critTour.add(Restrictions.eq("dspProducto", pProducto));
			}
		} else {
			if (comboProducto.getSelectionIndex() != -1) {
				String pProducto = comboProducto.getText();
				criteria.createCriteria("tour")
					.add(Restrictions.eq("dspProducto", pProducto));
			}
		}
		if (!txtFecha.getText().equals("")) {
			Date pFecha = FechaUtil.toDate(txtFecha.getText());
			criteria.add(Restrictions.eq("fecha", pFecha));
		}
		criteria.addOrder(Order.asc("fecha"));
		//Set s = new LinkedHashSet<DisponibilidadTour>();
		//s.addAll(criteria.list());
		//return s;
		return criteria.list();
	}
	
	/*
	@SuppressWarnings("unchecked")
	private List<DisponibilidadTour> buscarDisponibilidadTours() {
		Criteria criteria = session.createCriteria(DisponibilidadTour.class);
		if (comboProducto.getSelectionIndex() != -1) {
			String pProducto = comboProducto.getText();
			criteria.add(Restrictions.eq("dspProducto", pProducto));
		}
		if (!txtFecha.getText().equals("")) {
			Date pFecha = FechaUtil.toDate(txtFecha.getText());
			criteria.add(Restrictions.eq("fecha", pFecha));
		}
		criteria.addOrder(Order.asc("fecha"));
		//Set s = new LinkedHashSet<DisponibilidadTour>();
		//s.addAll(criteria.list());
		//return s;
		return criteria.list();
	}
	*/
	
	public DisponibilidadTour getDisponibilidadSeleccionada() {
		return seleccion;
	}
	
	
	class ViewContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			Object[] resultados = listadoTours.toArray(new DisponibilidadTour[listadoTours.size()]);
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
				resultado = "? / " + linea.getCapacidad().toString();
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
	
	
}
