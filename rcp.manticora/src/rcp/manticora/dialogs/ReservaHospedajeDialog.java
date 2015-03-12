package rcp.manticora.dialogs;

import java.util.Date;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.HojaServicioController;
import rcp.manticora.controllers.ReservaController;
import rcp.manticora.model.ILineaHospedaje;
import rcp.manticora.model.LineaActividad;
import rcp.manticora.model.LineaHospedajeExt;
import rcp.manticora.model.LineaHospedajeInt;
import rcp.manticora.model.Producto;
import rcp.manticora.model.ReservaHospedaje;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.Productos;

public class ReservaHospedajeDialog extends AbstractAEPTitleAreaDialog
		implements IReservaDialog {
	
	private final String pluginId = Application.PLUGIN_ID;
	
	private ReservaController resController;
	private Productos productos;
	private Producto hospedaje;
	private boolean isNuevaReserva;
	
	private Text txtHotel;
	private Text txtNoReserva;
	private Text txtFechaDesde;
	private Button bFechaDesde;
	private Text txtFechaHasta;
	private Button bFechaHasta;
	private Text txtConfirmadoPor;
	private Text txtFechaConf;
	private Button bFechaConf;
	
	private TableViewer viewer;
	
	private ImageDescriptor image;
	private LineaActividad linea;
	private ReservaHospedaje reserva;
	private Shell shell;

	public ReservaHospedajeDialog(Shell parentShell, HojaServicioController editorController,
			LineaActividad linea, ReservaHospedaje reserva) {
		super(parentShell);
		this.shell = parentShell;
		this.linea = linea;
		if (reserva == null) {
			isNuevaReserva = true;
			this.reserva = new ReservaHospedaje();
		} else {
			this.reserva = reserva;
		}
		this.resController = editorController.getReservaController();
		productos = new Productos();
		System.out.println("Linea: " + linea);
		hospedaje = productos.getProductoByIdProducto(linea.getIdProducto());
	}
	
	
	@Override
	protected Control createDialogArea(Composite base) {
		Composite parent = new Composite(base, SWT.NULL);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 10;
		layout.marginHeight = 15;
		// espacio entre los dos grupos (Datos generales y Rooming list)
		layout.verticalSpacing = 10;
		parent.setLayout(layout);
		
		Group composite = new Group(parent, SWT.NULL);
		composite.setText(" Datos generales ");
		layout = new GridLayout(6, false);
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;   // espaciado entre las filas
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Hotel:");
		txtHotel = new Text(composite, SWT.BORDER);
		gridData = new GridData(120,15);
		gridData.grabExcessHorizontalSpace = true;
		gridData.minimumWidth = 200;
		gridData.horizontalSpan = 2;
		txtHotel.setLayoutData(gridData);
		txtHotel.setEnabled(false);
		
		l = new Label(composite, SWT.NONE);
		l.setText("No. de conf.:");
		gridData = new GridData();
		gridData.horizontalIndent = 20;
		l.setLayoutData(gridData);
		txtNoReserva = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		gridData.horizontalSpan = 2;
		txtNoReserva.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Desde:");
		txtFechaDesde = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFechaDesde.setLayoutData(gridData);
		
		bFechaDesde = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		bFechaDesde.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaDesde.setImage(image.createImage());
		bFechaDesde.addSelectionListener(this.crearCalendario(shell, txtFechaDesde));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Hasta:");
		gridData = new GridData();
		gridData.horizontalIndent = 20;
		l.setLayoutData(gridData);
		txtFechaHasta = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFechaHasta.setLayoutData(gridData);
		
		bFechaHasta = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		bFechaHasta.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaHasta.setImage(image.createImage());
		bFechaHasta.addSelectionListener(this.crearCalendario(shell, txtFechaHasta, txtFechaDesde));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Conf. por:");
		txtConfirmadoPor = new Text(composite, SWT.BORDER);
		gridData = new GridData(150,15);
		gridData.horizontalSpan = 2;
		txtConfirmadoPor.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Fecha conf.:");
		gridData = new GridData();
		gridData.horizontalIndent = 20;
		l.setLayoutData(gridData);
		txtFechaConf = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFechaConf.setLayoutData(gridData);
		
		bFechaConf = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		bFechaConf.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaConf.setImage(image.createImage());
		bFechaConf.addSelectionListener(this.crearCalendario(shell, txtFechaConf));
		
		
// ****************** Listado de habitaciones reservadas *******************
		
		Group habitaciones = new Group(parent, SWT.NONE);
		habitaciones.setText(" Rooming list ");
		layout = new GridLayout();
		layout.marginWidth = 5;
		layout.marginHeight = 10;
		habitaciones.setLayout(layout);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gridData.heightHint = 175;
		habitaciones.setLayoutData(gridData);
		/*
		Composite habitaciones = new Composite(composite, SWT.NULL);
		layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		habitaciones.setLayout(layout);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 6, 1);
		gridData.heightHint = 100;
		habitaciones.setLayoutData(gridData);
		*/
		
		viewer = new TableViewer(crearTablaHabitaciones(habitaciones));
		viewer.setContentProvider(new ViewContentProviderHab());
		viewer.setLabelProvider(new ViewLabelProviderHab());
		
		
// ************************* Botones de acción ****************************
		
		Composite botones = new Composite(habitaciones, SWT.NONE);
		layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		botones.setLayout(layout);
		crearBotones(botones);
		
// ************** inicializamos los campos del formulario *****************
		llenarCampos();
		
		return composite;
	}
	
	
	private Table crearTablaHabitaciones(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Table tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tabla.setLayoutData(gridData);

		TableColumn column = new TableColumn(tabla, SWT.LEFT, 0);
		column.setText("Desde");
		column.setWidth(70);
		
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Hasta");
		column.setWidth(70);
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		if (linea.getProducto().isHotelAEP()) {
			column.setText("Habit.");
		} else {
			column.setText("Tipo");
		}
		column.setWidth(60);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Paxs");
		column.setWidth(40);
		
		column = new TableColumn(tabla, SWT.LEFT, 4);
		column.setText("Nombre de los paxs");
		column.setWidth(175);
		
		return tabla;
	}
	
	
	private void crearBotones(Composite botones) {
		GridData gridData;
		
		Button bAgregar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		bAgregar.setLayoutData(gridData);
		bAgregar.setText("Agregar");
		bAgregar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				guardarReservaHospedaje();    // para que las fechas de la reserva pasen al diálogo que se está abriendo
				if (hospedaje.isHotelAEP()) {
					System.out.println("Hospedaje AEP");
					ReservaHospIntDialog dialog = new ReservaHospIntDialog(shell,
							"Reserva de hospedaje AEP", reserva, null);
					if (dialog.open() == IDialogConstants.OK_ID) {
						resController.agregarLinea(reserva, dialog.getLineaHospedaje());
						System.out.println("ACTUALIZANDO VIEWER!!!");
						viewer.refresh();
					}
				} else {
					System.out.println("Hospedaje externo");
					ReservaHospExtDialog dialog = new ReservaHospExtDialog(shell,
							"Reserva de hospedaje externo", reserva, null);
					if (dialog.open() == IDialogConstants.OK_ID) {
						resController.agregarLinea(reserva, dialog.getLineaHospedaje());
						System.out.println("ACTUALIZANDO VIEWER!!!");
						viewer.refresh();
					}
				}
			}
		});
		
		Button bEditar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		bEditar.setLayoutData(gridData);
		bEditar.setText("Editar");
		bEditar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ILineaHospedaje lineaHosp = getHospedajeSeleccionado();
				if (lineaHosp != null) {
					if (hospedaje.isHotelAEP()) {
						System.out.println("No se ha implementado hospedaje AEP");
					} else {
						System.out.println("Hospedaje externo");
						ReservaHospExtDialog dialog = new ReservaHospExtDialog(shell,
								"Reserva de hospedaje externo", reserva, (LineaHospedajeExt) lineaHosp);
						if (dialog.open() == IDialogConstants.OK_ID) {
							resController.agregarLinea(reserva, dialog.getLineaHospedaje());
							System.out.println("ACTUALIZANDO VIEWER!!!");
							viewer.refresh();
						}
					}
				} else {
					MessageDialog.openError(shell, "Editar detalle",
						"No se ha seleccionado ninguna línea de detalle.");
				}
			}
		});
		
		Button bBorrar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		bBorrar.setLayoutData(gridData);
		bBorrar.setText("Borrar");
		bBorrar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (getHospedajeSeleccionado() != null) {
					resController.eliminarLinea(reserva, getHospedajeSeleccionado());
					System.out.println("ACTUALIZANDO VIEWER!!!");
					viewer.refresh();
				} else {
					MessageDialog.openError(shell, "Borrar detalle",
							"No se ha seleccionado ninguna línea de detalle.");
				}
			}
		});
	}
	

	@Override
	protected void llenarCampos() {
		if (isNuevaReserva) {
			setTitle("Nueva reserva de hospedaje");
			setMessage("Por favor, introduzca los detalles de la nueva reserva", IMessageProvider.INFORMATION);
			
			reserva.setHoja(linea.getHoja());
			reserva.setProducto(linea.getProducto());
			txtHotel.setText(valor2Txt(linea.getDspProducto()));
			txtFechaDesde.setText(FechaUtil.toString(linea.getFecha(), FechaUtil.formatoFecha));
			viewer.setInput(reserva.getListaHabitaciones());
			viewer.refresh();
		} else {
			setTitle("Editando reserva de hospedaje");
			setMessage("Por favor, especifique los detalles de la reserva", IMessageProvider.INFORMATION);
			
			System.out.println("Cargando información de campos...");
			txtHotel.setText(reserva.getProducto().getDescripcion());
			txtNoReserva.setText(reserva.getNoReserva());
			txtFechaDesde.setText(FechaUtil.toString(reserva.getFechaDesde()));
			txtFechaHasta.setText(FechaUtil.toString(reserva.getFechaHasta()));
			txtConfirmadoPor.setText(valor2Txt(reserva.getConfirmadoPor()));
			txtFechaConf.setText(FechaUtil.toString(reserva.getFechaConfirmado()));
			
			viewer.setInput(reserva.getListaHabitaciones());
			viewer.refresh();
		}
	}
	
	
	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			guardarReservaHospedaje();
		}
		return super.close();
	}
	
	
	private void guardarReservaHospedaje() {
		String pNoReserva = txtNoReserva.getText();
		Date pFechaDesde = FechaUtil.toDate(txtFechaDesde.getText());
		Date pFechaHasta = FechaUtil.toDate(txtFechaHasta.getText());
		String pConfirmado = txtConfirmadoPor.getText();
		Date pFechaConf = FechaUtil.toDate(txtFechaConf.getText());
		reserva.setNoReserva(pNoReserva);
		reserva.setFechaDesde(pFechaDesde);
		reserva.setFechaHasta(pFechaHasta);
		reserva.setConfirmadoPor(pConfirmado);
		reserva.setFechaConfirmado(pFechaConf);
		reserva.setEstado("A");
	}
	

	public ReservaHospedaje getReserva() {
		return reserva;
	}
	
	
	class ViewContentProviderHab implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set<ILineaHospedaje>) inputElement).toArray();
			return resultados;
		}
	}
	
	
	class ViewLabelProviderHab extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			ILineaHospedaje linea = (ILineaHospedaje) obj;
			switch (index) {
			case 0:
				resultado = FechaUtil.toString(linea.getFechaDesde());
				break;
			case 1:
				resultado = FechaUtil.toString(linea.getFechaHasta());
				break;
			case 2:
				if (linea instanceof LineaHospedajeExt) {
					resultado = ((LineaHospedajeExt) linea).getTipoHabitacion().getDescripcion();
				} else {
					resultado = ((LineaHospedajeInt) linea).getHabitacion().getNombre();
				}
				break;
			case 3:
				resultado = String.valueOf(linea.getNoPaxAsignados());
				break;
			case 4:
				resultado = linea.getNombresPaxAsignados();
				break;
			}
			return resultado;
			
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}
	
	
	public Object getElementoSeleccionado(TableViewer viewer) {
		Object seleccion = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
		return seleccion;
	}
	
	/**
	 * Retorna la línea de detalle que ha sido seleccionada. Si no hay selección
	 * retorna null.
	 * @return ILineaHospedaje que ha sido seleccionada por el usuario.
	 */
	public ILineaHospedaje getHospedajeSeleccionado() {
		Object seleccion = getElementoSeleccionado(viewer);
		ILineaHospedaje hospedaje = (ILineaHospedaje) seleccion;
		System.out.println("Hospedaje: " + hospedaje);
		return hospedaje;
	}

}
