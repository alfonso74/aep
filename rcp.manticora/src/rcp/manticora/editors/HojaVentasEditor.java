package rcp.manticora.editors;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.LockMode;
import org.hibernate.proxy.HibernateProxy;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.HojaServicioController;
import rcp.manticora.dialogs.AbstractAEPTitleAreaDialog;
import rcp.manticora.dialogs.AgregarActividadHoja;
import rcp.manticora.dialogs.AgregarPaxCotizacion;
import rcp.manticora.dialogs.AgregarTour2Hoja2;
import rcp.manticora.dialogs.IReservaDialog;
import rcp.manticora.dialogs.ReservaAlimentacionDialog;
import rcp.manticora.dialogs.ReservaAsignacionDialog;
import rcp.manticora.dialogs.ReservaBoletosDialog;
import rcp.manticora.dialogs.ReservaGestionDialog;
import rcp.manticora.dialogs.ReservaHospedajeDialog;
import rcp.manticora.dialogs.ReservaTourDialog;
import rcp.manticora.dialogs.ReservaTransporteDialog;
import rcp.manticora.dialogs.ReservaVueloDialog;
import rcp.manticora.model.AsignacionReserva;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.model.HojaServicioVentas;
import rcp.manticora.model.IReserva;
import rcp.manticora.model.LineaActividad;
import rcp.manticora.model.Pax;
import rcp.manticora.model.ReservaAlimento;
import rcp.manticora.model.ReservaBoleto;
import rcp.manticora.model.ReservaHospedaje;
import rcp.manticora.model.ReservaTour;
import rcp.manticora.model.ReservaTransporte;
import rcp.manticora.model.ReservaVuelo;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.GenericSorter;
import rcp.manticora.views.HojasView;

public class HojaVentasEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.hojaVentas";
	private final String pluginId = Application.PLUGIN_ID;
	private String idSession = ID + FechaUtil.getMilisegundos();
	
	private Text txtNombre;
	private Text txtInicio;
	private Text txtFin;
	private Text txtPaxs;
	private Combo comboVendedor;
	private Text txtNombreCliente;
	private Text txtEstado;
	private Text txtNumero;
	private Text txtCotizacion;
	private Text txtFechaEntrada;
	private Text txtComentario;
	
	private Table tablaAct;
	private TableViewer viewerAct;
	private TreeViewer viewerAct2;
	private Table tablaPaxs;
	private TableViewer viewerPaxs;
	private Table tablaReservas;
	private TableViewer viewerReservas;
	/*
	private Vector<LineaActividad> vLineasAct;
	private Vector<Pax> vLineasPax;
	*/
	private HojaServicioVentas registro;
	private ComboData cdVendedor;
	private ComboData cdStatus;
	
	private Button bFechaIni;
	private Button bFechaFin;
	
	private ImageDescriptor image;
	private HojaServicioController hsController;
	
	public HojaVentasEditor() {
		super();
		hsController = new HojaServicioController(idSession);
		//vLineasAct = new Vector<LineaActividad>();
		//vLineasPax = new Vector<Pax>();
		cdStatus = hsController.getComboDataKeyword(TipoKeyword.STATUS_HOJA_SERV);
		cdVendedor = hsController.getComboDataVendedores();
	}

	public void doSave(IProgressMonitor monitor) {
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		Date pInicio = FechaUtil.toDate(txtInicio.getText());
		Date pFin = FechaUtil.toDate(txtFin.getText());
		String pNombre = txtNombre.getText();
		Integer pPaxs = txt2Integer(txtPaxs.getText());
		String pComentario = txtComentario.getText();
		String pEstado = cdStatus.getCodeByName(txtEstado.getText());
		
		registro.setFechaInicio(pInicio);
		registro.setFechaFin(pFin);
		registro.setNombre(pNombre);
		registro.setPaxs(pPaxs);
		registro.setComentario(pComentario);
		registro.setEstado(pEstado);
		/*
		registro.resetListaActividades();
		for (int n = 0; n < vLineasAct.size(); n++) {
			registro.agregarActividad((LineaActividad) vLineasAct.elementAt(n));
		};
		*/
		hsController.doSave(registro);
		if (isNewDoc) {
			txtNumero.setText(Long.toString(registro.getIdHoja()));
			isNewDoc = false;
		}

//		 reflejamos el nombre en el tab
		this.setPartName(registro.getTituloDocumento());
// actualizamos el nombre del editorInput (para que deje de parecer "Nuevo")
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		//hsController.finalizar(idSession);
		//hsController = new HojaServicioController(idSession);
		llenarControles();
		// refresh de todas las vistas abiertas
		actualizarVistas();
		//actualizarVista(HojasView.ID);
		viewerAct.refresh();
		viewerPaxs.refresh();
		viewerReservas.refresh();
		removeDirtyFlag();
	}
	
	private boolean validarSave() {
		String pNombre = txtNombre.getText();
		
		if (pNombre.length() > 50) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre de la hoja no puede superar los 50 caracteres (" + pNombre.length() + ").");
			return false;
		}
		if (registro.getListaActividades().size() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"Debe definir actividades antes de guardar la hoja de servicios.");
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
		tabGeneral.setControl(getTabGeneralControl(tabFolder));
		// tab de actividades
		TabItem tabActividades = new TabItem(tabFolder, SWT.NONE);
		tabActividades.setText("Actividades");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/actividades.gif");
		tabActividades.setImage(image.createImage());
		tabActividades.setControl(getTabActividadesControl(tabFolder));
		// tab de clientes (paxs)
		TabItem tabClientes = new TabItem(tabFolder, SWT.NONE);
		tabClientes.setText("Detalle de PAXs");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/detallePAX2.gif");
		tabClientes.setImage(image.createImage());
		tabClientes.setControl(getTabClientesControl(tabFolder));
		// tab de reservas
		TabItem tabReservas = new TabItem(tabFolder, SWT.NONE);
		tabReservas.setText("Reservas");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/informacion.gif");
		tabReservas.setImage(image.createImage());
		tabReservas.setControl(getTabReservasControl(tabFolder));
		TabItem tabActividades2 = new TabItem(tabFolder, SWT.NONE);
		tabActividades2.setText("ActTree");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/actividades.gif");
		tabActividades2.setImage(image.createImage());
		tabActividades2.setControl(getTabActividades2Control(tabFolder));
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nueva hoja de servicios (ventas)");
			txtEstado.setText("Nueva");
			registro = new HojaServicioVentas();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else{
			System.out.println("Ejecutando código para cargar datos...");
			registro = (HojaServicioVentas) ((CommonEditorInput) this.getEditorInput()).getElemento();
			
			/*
			Session session = HibernateUtil.currentSession();
			HojaServicioDAO dao = new HojaServicioDAO();
			dao.setSession(session);
			registro = dao.findById(registro.getIdHoja(), true);
			*/
			// obtenemos una sesión de hibernate para cargar las actividades y refrescar
			// todos los objetos asociados
			//Session session = HibernateUtil.getSessionFactory().openSession();
			//registro = (HojaServicio) session.get(HojaServicio.class, registro.getIdHoja());
			//session.lock(registro, LockMode.NONE);            // reattachment del objeto
			//registro = hsController.getHojaById(registro.getIdHoja());
			/*
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.refresh(registro);
			*/
			//session.get(HojaServicio.class, registro.getIdHoja(), LockMode.UPGRADE);
			//session.merge(registro);
			//session.lock(registro, LockMode.READ);
			
			//registro = hsController.inicializarHojaServicio(registro.getIdHoja());
			hsController.getSession().refresh(registro, LockMode.READ);
			hsController.verSesiones();
			txtNombre.setText(valor2Txt(registro.getNombre()));
			txtInicio.setText(FechaUtil.toString(registro.getFechaInicio()));
			txtFin.setText(FechaUtil.toString(registro.getFechaFin()));
			txtPaxs.setText(valor2Txt(registro.getPaxs()));
			comboVendedor.setText(registro.getDspVendedor());
			if (registro.getCliente() != null) {
				txtNombreCliente.setText(registro.getCliente().getNombreCliente());
			} else {
				txtNombreCliente.setText("No asignado");
			}
			txtEstado.setText(cdStatus.getTextoByKey(registro.getEstado()));
			txtNumero.setText(valor2Txt(registro.getIdHoja()));
			txtCotizacion.setText(valor2Txt(registro.getIdCotizacion()));
			txtFechaEntrada.setText(FechaUtil.toString(registro.getFechaOperaciones(), FechaUtil.formatoFecha));
			txtComentario.setText(valor2Txt(registro.getComentario()));
			//vLineasAct.addAll(registro.getListaActividades());
			//vLineasPax.addAll(registro.getListaPaxs());
			viewerAct.setInput(registro.getListaActividades());
			viewerPaxs.setInput(registro.getListaPaxs());
			viewerReservas.setInput(registro.getListaReservas());
			viewerAct2.setInput(registro.getListaActividades());
			//vLineasRes.addAll(registro.getListaReservas());
			//viewerReservas.setInput(vLineasRes);
			//session.close();
			viewerAct.refresh();
			viewerPaxs.refresh();
			viewerReservas.refresh();
			viewerAct2.refresh();
			//session.close();
			//HibernateUtil.closeSession();
			
		}
		addFilledFlag();
		setFocoInicial(txtNombre);
	}
	
	private Control getTabGeneralControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);
		
		crearControlesTabGeneral(composite);
		return composite;
	}
	
	private Control getTabActividadesControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		crearControlesTabActividades(composite);
		return composite;
	}
	
	private Control getTabActividades2Control(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		crearControlesTabActividades2(composite);
		return composite;
	}
	
	private Control getTabClientesControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		crearControlesTabClientes(composite);
		return composite;
	}
	
	private Control getTabReservasControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		crearControlesTabReservas(composite);
		return composite;
	}
	
	private void crearControlesTabGeneral(Composite parent) {
		Label l;
		GridLayout gridLayout;
		GridData gridData;

		Group grupoTop = new Group(parent, SWT.NONE);
		//grupoTop.setText("Información General");
		//Composite grupoTop = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		grupoTop.setLayout(gridLayout);
		grupoTop.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		l = new Label(grupoTop, SWT.None);
		l.setText("Nombre:");
		txtNombre = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(250,15);
		gridData.horizontalSpan = 5;
		txtNombre.setLayoutData(gridData);
		txtNombre.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoTop, SWT.None);
		l.setText("Fecha inicio:");
		txtInicio = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		txtInicio.setLayoutData(new GridData(60,15));
		txtInicio.addModifyListener(this.createModifyListener());

		bFechaIni = new Button(grupoTop, SWT.NONE);
		bFechaIni.setLayoutData(new GridData(16,16));
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaIni.setImage(image.createImage());
		bFechaIni.addSelectionListener(this.crearCalendario(txtInicio));
		
		l = new Label(grupoTop, SWT.None);
		l.setText("Fecha fin:");
		txtFin = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(60,15);
		txtFin.setLayoutData(gridData);
		txtFin.addModifyListener(this.createModifyListener());

		bFechaFin = new Button(grupoTop, SWT.NONE);
		gridData = new GridData(16,16);
		bFechaFin.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaFin.setImage(image.createImage());
		bFechaFin.addSelectionListener(this.crearCalendario(txtFin, txtInicio));
		
		l = new Label(grupoTop, SWT.NONE);
		l.setText("Paxs:");
		txtPaxs = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(40,15);
		gridData.horizontalSpan = 2;
		txtPaxs.setLayoutData(gridData);
		//txtPaxs.addModifyListener(this.createModifyListener());
		txtPaxs.setEnabled(false);
		
		l = new Label(grupoTop, SWT.NONE);
		l.setText("Vendedor:");
		comboVendedor = new Combo(grupoTop, SWT.READ_ONLY);
		comboVendedor.setItems(cdVendedor.getTexto());
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		comboVendedor.setLayoutData(gridData);
		comboVendedor.setEnabled(false);
		
		l = new Label(grupoTop, SWT.NONE);
		l.setText("Vendido a:");
		txtNombreCliente = new Text(grupoTop, SWT.BORDER);
		gridData = new GridData(250, 15);
		gridData.horizontalSpan = 5;
		txtNombreCliente.setLayoutData(gridData);
		txtNombreCliente.setEnabled(false);
		
// *************** Grupo de status de HS ***************************
		Group grupoStatus = new Group(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		grupoStatus.setLayout(gridLayout);
		grupoStatus.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		
		l = new Label(grupoStatus, SWT.None);
		l.setText("Estado HS:");
		txtEstado = new Text(grupoStatus, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(55,15);
		txtEstado.setLayoutData(gridData);
		txtEstado.setEditable(false);
		
		l = new Label(grupoStatus, SWT.None);
		l.setText("Número HS:");
		txtNumero = new Text(grupoStatus, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(35,15);
		txtNumero.setLayoutData(gridData);
		txtNumero.setEditable(false);
		
		l = new Label(grupoStatus, SWT.NONE);
		l.setText("Cotización:");
		txtCotizacion = new Text(grupoStatus, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(35,15);
		txtCotizacion.setLayoutData(gridData);
		txtCotizacion.setEditable(false);
		
		l = new Label(grupoStatus, SWT.NONE);
		l.setText("Entrada Ops:");
		txtFechaEntrada = new Text(grupoStatus, SWT.BORDER);
		txtFechaEntrada.setLayoutData(new GridData(60,15));
		txtFechaEntrada.setEditable(false);
		
// *************************** Grupo de Comentarios *************************
		
		Group grupoBottom = new Group(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		grupoBottom.setLayout(gridLayout);
		gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.horizontalSpan = 1;
		grupoBottom.setLayoutData(gridData);
		
		l = new Label(grupoBottom, SWT.NONE);
		l.setText("Comentarios:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);
		txtComentario = new Text(grupoBottom, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData.heightHint = 55;
		txtComentario.setLayoutData(gridData);
		txtComentario.addModifyListener(this.createModifyListener());
	}
	
	
	private void crearControlesTabActividades2(Composite parent) {
		Group grupoTabla = new Group(parent, SWT.None);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grupoTabla.setLayout(gridLayout);
		grupoTabla.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		viewerAct2 = new TreeViewer(grupoTabla, style);
		crearColumnasActividades(viewerAct2);
		viewerAct2.setContentProvider(new ViewContentProviderAct2());
		viewerAct2.setLabelProvider(new ViewLabelProviderAct2());
		//viewerAct.setInput(vLineasAct);
		//viewerAct2.setSorter(new GenericSorter());
		//((GenericSorter) viewerAct2.getSorter()).doSort(2, GenericSorter.TEXTO);
		viewerAct2.setSorter(new GenericSorter(viewerAct2, 0, GenericSorter.FECHAHORA));
		
		//registramos al viewer como un selection provider para determinar
		//qué elemento está seleccionado
		getSite().setSelectionProvider(viewerAct2);
	}
	
	private void crearControlesTabActividades(Composite parent) {
		Group grupoTabla = new Group(parent, SWT.None);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grupoTabla.setLayout(gridLayout);
		grupoTabla.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		tablaAct = crearTablaActividades(grupoTabla);
		viewerAct = new TableViewer(tablaAct);
		viewerAct.setContentProvider(new ViewContentProviderAct());
		viewerAct.setLabelProvider(new ViewLabelProviderAct());
		viewerAct.setSorter(new GenericSorter(viewerAct, 0, GenericSorter.FECHAHORA));
		
		//registramos al viewer como un selection provider para determinar
		//qué elemento está seleccionado
		getSite().setSelectionProvider(viewerAct);

		Composite botones = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(5, false);
		botones.setLayout(gridLayout);
		
		Button bAgregar = new Button(botones, SWT.PUSH);
		GridData gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bAgregar.setLayoutData(gridData);
		bAgregar.setText("Agregar");
		bAgregar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de agregar...");
				//AgregarLineaHS dialogo =new AgregarLineaHS(getSite().getShell(), "Agregar detalles");
				//dialogo.open();
				LineaActividad la = getActividadDefault(viewerAct);
				if (la != null) {
					String fechaDefault = FechaUtil.toString(la.getFecha());
					agregarActividad(getSite().getShell(), fechaDefault);
				} else {
					agregarActividad(getSite().getShell(), null);
				}
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
				editarActividad(getSite().getShell(), getActividadSeleccionada(viewerAct));
			}
		});
		viewerAct.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("View double cick!!:  editar actividad...");
				editarActividad(getSite().getShell(), getActividadSeleccionada(viewerAct));
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
				borrarActividades();
			}
		});	
		
		Button bReserva = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bReserva.setLayoutData(gridData);
		bReserva.setText("Reserva");
		bReserva.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de realizar reservas...");
				//gestionarReserva(getSite().getShell(), getActividadSeleccionada());
				if (gestionarReserva(getSite().getShell(), getActividadesSeleccionadas(viewerAct))) {
					viewerAct.refresh();
					viewerReservas.refresh();
					addDirtyFlag();
				}
			}
		});
		
		Button bTour = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		bTour.setLayoutData(gridData);
		bTour.setText("Tour");
		bTour.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("TOUR");
				agregarTour(getSite().getShell());
			}
		});
	}
	
	
	private void crearControlesTabClientes(Composite parent) {
		Group grupoTabla = new Group(parent, SWT.None);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grupoTabla.setLayout(gridLayout);
		grupoTabla.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		tablaPaxs = crearTablaPaxs(grupoTabla);
		viewerPaxs = new TableViewer(tablaPaxs);
		viewerPaxs.setContentProvider(new ViewContentProviderPax());
		viewerPaxs.setLabelProvider(new ViewLabelProviderPax());
		//viewerPaxs.setInput(vLineasPax);
		
		Composite botones = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(3, false);
		botones.setLayout(gridLayout);
		
		Button bAgregar = new Button(botones, SWT.PUSH);
		GridData gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bAgregar.setLayoutData(gridData);
		bAgregar.setText("Agregar");
		bAgregar.setEnabled(false);
		// acción deshabilitada, no hacemos nada
		bAgregar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de agregar...");
				agregarPax(getSite().getShell());
			}
		});
		
		Button bEditar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bEditar.setLayoutData(gridData);
		bEditar.setText("Editar");
		bEditar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de editar..." + getPaxSeleccionado().getNombre());
				editarPax(getSite().getShell(), getPaxSeleccionado());
			}
		});
		viewerPaxs.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("Editar pax (doble clic): " + getPaxSeleccionado().getNombre());
				editarPax(getSite().getShell(), getPaxSeleccionado());
			}
		});
		
		Button bBorrar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bBorrar.setLayoutData(gridData);
		bBorrar.setText("Excluir");
		bBorrar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de excluir...");
				hsController.excluirPax(registro, getPaxSeleccionado());
				viewerPaxs.refresh();
				addDirtyFlag();
			}
		});
	}
	
	private void crearControlesTabReservas(Composite parent) {
		Group grupoTabla = new Group(parent, SWT.None);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grupoTabla.setLayout(gridLayout);
		grupoTabla.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		tablaReservas = crearTablaReservas(grupoTabla);
		viewerReservas = new TableViewer(tablaReservas);
		viewerReservas.setContentProvider(new ViewContentProviderRes());
		viewerReservas.setLabelProvider(new ViewLabelProviderRes());
		//viewerReservas.setInput(registro.getListaReservas());
		viewerReservas.setSorter(new GenericSorter(viewerReservas));
		((GenericSorter) viewerReservas.getSorter()).doSort(2, GenericSorter.NUMERO, 1);
		
		//registramos al viewer como un selection provider para determinar
		//qué elemento está seleccionado
		getSite().setSelectionProvider(viewerAct);

		Composite botones = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		botones.setLayout(gridLayout);
		
		GridData gridData;
		
		Button bEditar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bEditar.setLayoutData(gridData);
		bEditar.setText("Editar");
		bEditar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de editar reserva...");
				IReserva reserva = (IReserva) getElementoSeleccionado(viewerReservas);
				editarReserva(getSite().getShell(), reserva);
			}
		});	
		viewerReservas.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("Editar reserva (doble clic)...");
				IReserva reserva = (IReserva) getElementoSeleccionado(viewerReservas);
				editarReserva(getSite().getShell(), reserva);
			}
		});
		
		Button bBorrar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bBorrar.setLayoutData(gridData);
		bBorrar.setText("Borrar");
		bBorrar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de borrar reserva...");
				IReserva reserva = (IReserva) getElementoSeleccionado(viewerReservas);
				System.out.println("Selección: " + reserva);
				hsController.eliminarReserva(reserva);
				viewerReservas.refresh();
				addDirtyFlag();   // como borramos, indicamos que se ha modificado la cotización
			}
		});	
	}
	
	private void crearColumnasActividades(TreeViewer v) {
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		v.getTree().setLayoutData(gridData);
		v.getTree().setHeaderVisible(true);
		v.getTree().setLinesVisible(true);
		
		TreeColumn column = new TreeColumn(v.getTree(),SWT.NONE);
		column.setText("Fecha");
		column.setWidth(130);
		
		column = new TreeColumn(v.getTree(),SWT.NONE);
		column.setText("R");
		column.setWidth(22);
		
		column = new TreeColumn(v.getTree(),SWT.NONE);
		column.setText("Descripción");
		column.setWidth(170);
		
		column = new TreeColumn(v.getTree(),SWT.NONE);
		column.setText("BLD");
		column.setWidth(35);
		
		column = new TreeColumn(v.getTree(),SWT.NONE);
		column.setText("Comentarios");
		column.setWidth(180);
	}
	
	private Table crearTablaActividades(Composite parent) {
		//int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;
		tablaAct = new Table(parent, style);
		tablaAct.setLinesVisible(true);
		tablaAct.setHeaderVisible(true);
		GridData gridData3 = new GridData(SWT.FILL, SWT.FILL, true, true);
		tablaAct.setLayoutData(gridData3);
		
		TableColumn column = new TableColumn(tablaAct, SWT.LEFT, 0);
		column.setText("Fecha");
		column.setWidth(130);
		
		column = new TableColumn(tablaAct, SWT.CENTER, 1);
		column.setText("R");
		column.setWidth(22);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 2);
		column.setText("Descripción");
		column.setWidth(170);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 3);
		column.setText("BLD");
		column.setWidth(35);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 4);
		column.setText("Comentarios");
		column.setWidth(180);
		
		return tablaAct;
	}
	
	private Table crearTablaPaxs(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tablaPaxs = new Table(parent, style);
		tablaPaxs.setLinesVisible(true);
		tablaPaxs.setHeaderVisible(true);
		GridData gridData3 = new GridData(SWT.FILL, SWT.FILL, true, true);
		tablaPaxs.setLayoutData(gridData3);
		
		TableColumn column = new TableColumn(tablaPaxs, SWT.CENTER, 0);
		column.setText("Nombre");
		column.setWidth(120);
		column.setAlignment(SWT.LEFT);
		
		column = new TableColumn(tablaPaxs, SWT.CENTER, 1);
		column.setText("Apellido");
		column.setWidth(120);
		column.setAlignment(SWT.LEFT);
		
		column = new TableColumn(tablaPaxs, SWT.CENTER, 2);
		column.setText("Pasaporte");
		column.setWidth(90);
		column.setAlignment(SWT.LEFT);
		
		column = new TableColumn(tablaPaxs, SWT.CENTER, 3);
		column.setText("País");
		column.setWidth(100);
		column.setAlignment(SWT.LEFT);
		
		return tablaPaxs;
	}
	
	private Table crearTablaReservas(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tablaAct = new Table(parent, style);
		tablaAct.setLinesVisible(true);
		tablaAct.setHeaderVisible(true);
		GridData gridData3 = new GridData(SWT.FILL, SWT.FILL, true, true);
		tablaAct.setLayoutData(gridData3);
		
		TableColumn column = new TableColumn(tablaAct, SWT.LEFT, 0);
		column.setText("Clase");
		column.setWidth(130);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 1);
		column.setText("Fecha");
		column.setWidth(150);
		
		column = new TableColumn(tablaAct, SWT.CENTER, 2);
		column.setText("");
		column.setWidth(30);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 3);
		column.setText("Comentarios");
		column.setWidth(200);
		
		return tablaAct;
	}
	
	
	class ViewContentProviderAct2 implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			//System.out.println("Elementos: " + ((LineaActividad) parentElement).getChildren().size());
			return ((LineaActividad) parentElement).getHojaTour().getListaActividades().toArray();
			//return ((LineaActividad) parentElement).getChildren().toArray();
		}

		public Object getParent(Object element) {
			System.out.println("Ejecutando getParent()");
			/*
			if (element == null) {
				return null;
			}
			return ((LineaActividad) element).getParent();
			*/
			return null;
		}

		public boolean hasChildren(Object element) {
			return ((LineaActividad) element).getHojaTour() != null;
			//return ((LineaActividad) element).getChildren().size() > 0;
		}

		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set) inputElement).toArray();
			System.out.println("Resultados: " + resultados.length);
			return resultados;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
	}
	
	class ViewLabelProviderAct2 extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			String imageKey = IImageKeys.CHECK;
			ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
			LineaActividad linea = (LineaActividad) element;
			switch (columnIndex) {
			case 1:
				imageKey = IImageKeys.INFO;
				imageKey = IImageKeys.QUESTION;
				image = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
				System.out.println(linea + ", tipo: " + linea.getTipoReserva());
				if (linea.getTipoReserva() != null && !linea.getTipoReserva().equals("No aplica")) {
					if (linea.getListaAsignaciones() == null || linea.getListaAsignaciones().size() == 0) {
						return image.createImage();
					} else {
						imageKey = IImageKeys.CHECK;
						image = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
						return image.createImage();
					}
				}
				break;
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			String resultado = "";
			LineaActividad linea = (LineaActividad) element;
			switch (columnIndex) {
			case 0:
				if (linea.getHora() == null) {
					resultado = FechaUtil.toString(linea.getFecha(), FechaUtil.formatoFecha);
				} else {
					resultado = FechaUtil.toString(linea.getFecha(), FechaUtil.formatoFechaHora);
				}
				break;
			case 2:
				//resultado = linea.getDspProducto();
				resultado = linea.getProducto().getDescripcionHotel();
				break;
			case 3:
				resultado = linea.getComidas();
				break;
			case 4:
				//resultado = new DecimalFormat(".00").format(linea.getPrecio());
				resultado = linea.getComentario();
				/*
				if (linea.getParent() != null) {
					resultado = linea.getParent().getDspProducto();
				} else if (linea.getChildren().size() > 0) {
					resultado = String.valueOf(linea.getChildren().size());
				} else {
					resultado = linea.getComentario();
				}
				*/
				break;
			}
			return resultado;	
		}
		
	}
	
	class ViewContentProviderAct implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			//Object[] resultados = vLineasAct.toArray(new LineaActividad[vLineasAct.size()]);
			Object[] resultados = ((Set) inputElement).toArray();
			return resultados;
		}
	}
	
	
	class ViewLabelProviderAct extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			LineaActividad linea = (LineaActividad) obj;
			switch (index) {
			case 0:
				if (linea.getHora() == null) {
					resultado = FechaUtil.toString(linea.getFecha(), FechaUtil.formatoFecha);
				} else {
					resultado = FechaUtil.toString(linea.getFecha(), FechaUtil.formatoFechaHora);
				}
				break;
			case 2:
				//resultado = linea.getDspProducto();
				resultado = linea.getProducto().getDescripcionHotel();
				break;
			case 3:
				resultado = linea.getComidas();
				break;
			case 4:
				//resultado = new DecimalFormat(".00").format(linea.getPrecio());
				resultado = linea.getComentario();
				/*
				if (linea.getParent() != null) {
					resultado = linea.getParent().getDspProducto();
				} else if (linea.getChildren().size() > 0) {
					resultado = String.valueOf(linea.getChildren().size());
				} else {
					resultado = linea.getComentario();
				}
				*/
				break;
			}
			return resultado;	
		}
		
		public Image getColumnImage(Object obj, int index) {
			String imageKey = IImageKeys.CHECK;
			ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
			LineaActividad linea = (LineaActividad) obj;
			switch (index) {
			case 1:
				imageKey = IImageKeys.INFO;
				imageKey = IImageKeys.QUESTION;
				image = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
				System.out.println(linea + ", tipo: " + linea.getTipoReserva());
				if (linea.getTipoReserva() != null && !linea.getTipoReserva().equals("No aplica")) {
					if (linea.getListaAsignaciones() == null || linea.getListaAsignaciones().size() == 0) {
						return image.createImage();
					} else {
						imageKey = IImageKeys.CHECK;
						image = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
						return image.createImage();
					}
				}
				break;
				/*
			case 3:
				if (linea.isBreakfast()) return image.createImage();
				break;
			case 4:
				if (linea.isLunch()) return image.createImage();
				break;
			case 5:
				if (linea.isDinner()) return image.createImage();
				break;
				*/
			}
			return null;
		}
	}
	
	
	class ViewContentProviderPax implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set) inputElement).toArray();
			return resultados;
		}
	}
	
	
	class ViewLabelProviderPax extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			Pax linea = (Pax) obj;
			switch (index) {
			case 0:
				resultado = linea.getNombre();
				break;
			case 1:
				resultado = linea.getApellido();
				break;
			case 2:
				resultado = linea.getIdentificacion();
				break;
			case 3:
				resultado = linea.getDspPais();
				break;
			}
			return resultado;
			
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}
	
	
	class ViewContentProviderRes implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set) inputElement).toArray();
			return resultados;
		}
	}
	
	
	class ViewLabelProviderRes extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			IReserva reserva = (IReserva) obj;
			/*
			// este código presenta correctamente la columna clase de la reserva
			if (reserva instanceof HibernateProxy) {
				reserva = (IReserva) ((HibernateProxy) reserva).getHibernateLazyInitializer().getImplementation();
			}
			*/
			switch (index) {
			case 0:
				resultado = reserva.getTipoReserva();
				if (reserva instanceof ReservaHospedaje) {
					resultado = ((ReservaHospedaje) reserva).getProducto().getDescripcion();
				}
				if (reserva instanceof ReservaTour) {
					resultado = ((ReservaTour) reserva).getDisponibilidad().getTour().getNombre();
				}
				break;
			case 1:
				resultado = reserva.getFechaDspReserva();
				break;
			case 2:
				resultado = Long.toString(reserva.getIdReserva());
				break;
			case 3:
				resultado = reserva.getComentario();
				break;
			}
			return resultado;	
		}
		
		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}
	
	
	
// ******************************* manejo de actividades *****************************
	
	private void agregarActividad(Shell shell, String fechaDefault) {
		LineaActividad linea = new LineaActividad();
		AgregarActividadHoja dialogo = new AgregarActividadHoja(shell, linea);
		dialogo.setFechaDefault(fechaDefault);
		
		if (dialogo.open() == IDialogConstants.OK_ID) {
			hsController.agregarActividad(registro, linea);
			viewerAct.refresh();
			addDirtyFlag();
		}
	}
	
	private void editarActividad(Shell shell, LineaActividad linea) {
		AgregarActividadHoja dialogo = new AgregarActividadHoja(shell, linea);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			viewerAct.refresh();
			addDirtyFlag();
		}
	}
	
	private void borrarActividades() {
		Set<LineaActividad> lineas = getActividadesSeleccionadas(viewerAct);
		if (!lineas.isEmpty()) {
			for (LineaActividad linea : lineas) {
				hsController.eliminarActividad(registro, linea);
			}
			viewerAct.refresh();
		} else {
			System.out.println("No hay actividades seleccionadas para borrar");
		}
		addDirtyFlag();   // como borramos, indicamos que se ha modificado la cotización
	}
	
	
	/**
	 * Retorna el elemento seleccionado por el usuario en un viewer.  Si
	 * el usuario no ha seleccionado ningún elemento, se retorna el
	 * último.
	 * @param viewer Objeto tipo TableViewer o TreeViewer
	 * @return Objeto de tipo LineaActividad
	 */
	private LineaActividad getActividadDefault(StructuredViewer viewer) {
		LineaActividad linea = getActividadSeleccionada(viewer);
		if (linea == null) {
			if (viewer instanceof TableViewer) {
				int n = ((TableViewer) viewer).getTable().getItemCount();
				if (n > 0) {
					linea = (LineaActividad) ((TableViewer) viewer).getElementAt(n - 1);
				}
			} else if (viewer instanceof TreeViewer) {
				int n = ((TreeViewer) viewer).getTree().getItemCount();
				System.out.println("Cantidad de actividades: " + n);
				if (n > 0) {
					TreeItem ti = ((TreeViewer) viewer).getTree().getItem(n - 1);
					// seleccionamos el último elemento para poder extraerlo del viewer
					((TreeViewer) viewer).getTree().setSelection(ti);
					linea = getActividadSeleccionada(viewer);
					// el usuario no ha seleccionado ningún elemento, así que regresamos al viewer a ese estado
					((TreeViewer) viewer).getTree().deselectAll();
				}
			}
		}
		return linea;
	}
	
	
	private LineaActividad getActividadSeleccionada(StructuredViewer viewer) {
		Object seleccion = getElementoSeleccionado(viewer);
		LineaActividad actividad = (LineaActividad) seleccion;
		System.out.println("Actividad: " + actividad);
		return actividad;
	}

	
	@SuppressWarnings("unchecked")
	private Set<LineaActividad> getActividadesSeleccionadas(StructuredViewer viewer) {
		List<LineaActividad> listado = getElementosSeleccionados(viewer).toList();
		HashSet<LineaActividad> seleccion = new HashSet<LineaActividad>();
		seleccion.addAll(listado);
		return seleccion;
	}
	
	

// ------------------------------ Tours ???  no no no ------------------------------------
	
	private void agregarTour(Shell shell) {
		AgregarTour2Hoja2 dialogo = new AgregarTour2Hoja2(shell);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			DisponibilidadTour disp = dialogo.getDisponibilidadSeleccionada();
			//importarTour(disp.getTour(), disp);
			hsController.agregarLineasFromTour(registro, disp.getTour(), disp);
			viewerAct.refresh();
			addDirtyFlag();
		}
	}
	
	/*
	private void importarTour(Tour tour, DisponibilidadTour disp) {
		Iterator<LineaTour> it = tour.getListaActividades().iterator();
		LineaTour linea = null;
		LineaActividad lineaAct = null;
		while (it.hasNext()) {
			linea = it.next();
			lineaAct = new LineaActividad();
			lineaAct.setHoja(registro);
			lineaAct.setIdProducto(linea.getIdProducto());
			lineaAct.setDspProducto(linea.getDspProducto());
			lineaAct.setFecha(FechaUtil.ajustarFecha(disp.getFecha(), linea.getDia() - 1));
			lineaAct.setTipoReserva(linea.getTipoReserva());
			lineaAct.setComidas(linea.getComidas());
			lineaAct.setComentario(linea.getComentario());
			//vLineasAct.add(lineaAct);
		}
	}
	*/
	
	
// ******************************* manejo de paxs *****************************
	
	private void agregarPax(Shell shell) {
		Pax linea = new Pax();
		AgregarPaxCotizacion dialogo = new AgregarPaxCotizacion(shell, linea);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			hsController.agregarPax(registro, linea);
			viewerPaxs.refresh();
			addDirtyFlag();
		}
	}
	
	private void editarPax(Shell shell, Pax linea) {
		AgregarPaxCotizacion dialogo = new AgregarPaxCotizacion(shell, linea);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			viewerPaxs.refresh();
			addDirtyFlag();
		}
	}
	
	public Pax getPaxSeleccionado() {
		//Object seleccion = ((IStructuredSelection) viewerPaxs.getSelection()).getFirstElement();
		Object seleccion = getElementoSeleccionado(viewerPaxs);
		Pax pax = (Pax) seleccion;
		return pax;
	}
	
	
	
	
// ******************************* manejo de reservas *****************************
	
	private boolean gestionarReserva(Shell shell, Set<LineaActividad> lineas) {
		// verificamos que esté seleccionada una actividad
		if (lineas.isEmpty() || lineas.size() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Información", "Debe seleccionar una actividad para gestionar las reservas.");
			return false;
		}
		
		// si se ha seleccionado más de una actividad, verificamos que no tengan reservas asignadas y que
		// todas sean del mismo tipo.
		LineaActividad linea = null;
		if (lineas.size() > 1) {
			boolean grupoValido = true;
			String tipoReserva = null;
			String mensajeTxt = "";
			for (LineaActividad actividad : lineas) {
				if (actividad.hasReservas()) {
					mensajeTxt = "Las actividades seleccionadas en grupo no pueden tener reservas asociadas.\n\nPor favor, verifique e intente nuevamente.";
						grupoValido = false;
				}
				if (tipoReserva == null) {
					tipoReserva = actividad.getTipoReserva();
					linea = actividad;
				} else {
					if (!actividad.getTipoReserva().equals(tipoReserva)) {
						mensajeTxt = "La asignación de reservas a múltiples actividades requiere que todas las actividades sean del mismo tipo.\n\nPor favor, verifique e intente nuevamente.";
						grupoValido = false;
					}
				}
			}
			if (!grupoValido) {
				MessageDialog.openInformation(getSite().getShell(), "Información", mensajeTxt);
				return false;
			}
		} else {
			System.out.println("Una sola línea seleccionada");
			linea = lineas.iterator().next();
			if (linea.getTipoReserva().equals("") || linea.getTipoReserva().equals("No aplica")) {
				String mensajeTxt = "La actividad seleccionada no requiere asignación de reservas.";
				MessageDialog.openInformation(getSite().getShell(), "Información", mensajeTxt);
				return false;
			}
		}
		
		String accion = null;
		AbstractAEPTitleAreaDialog dialogo = new ReservaGestionDialog(shell, linea);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			accion = ((ReservaGestionDialog) dialogo).getAccion();
		}
		
		if (accion.equals("crear")) {
			// llamamos al diálogo adecuado dependiendo de tipo de reserva
			agregarReserva(shell, lineas);
		} else if (accion.equals("asignar")) {
			// diálogo que presenta el listado de reservas y permite asignar una de las mismas a la actividad
			asignarReservaExistente(shell, lineas);
		} else if (accion.equals("consultar")) {
			// permite ver y editar la reserva asignada a la actividad
			// por ahora asumimos que una actividad solamente tiene una reserva, aunque
			// el diseño permite tener varias reservas.
			IReserva reserva = linea.getListaAsignaciones().iterator().next().getReserva();
			editarReserva(shell, reserva);
		} else if (accion.equals("borrar")) {
			// presenta el listado de reservas asociadas para poder borrar
			// dialog.open();  // diálogo de asignaciones que se pueden borrar
			// AsignacionReserva asignacion = dialog.getAsignacion();
			// hsController.eliminarAsignacionReserva(asignacion);
			// inicialmente podemos borrar simplemente todo lo que encontremos asociado  :)
			hsController.eliminarAsignacionesReserva(linea);
		}
		return true;
	}
	
	
	private void asignarReservaExistente(Shell shell, Set<LineaActividad> lineas) {
		AbstractAEPTitleAreaDialog dialogo = new ReservaAsignacionDialog(shell, registro.getListaReservas());
		if (dialogo.open() == IDialogConstants.OK_ID) {
			System.out.println("SISISI");
			IReserva reserva = ((IReservaDialog) dialogo).getReserva();
			// Verificamos que al menos una actividad sea del mismo tipo que la reserva seleccionada
			for (LineaActividad actividad : lineas) {
				if (!actividad.getTipoReserva().equals(reserva.getTipoReserva())) {
					MessageDialog.openError(shell, "Asignar reserva existente",
							"El tipo de la reserva seleccionada (tipo: " + reserva.getTipoReserva() + ") no coincide con el\ntipo de la actividad (tipo: " + actividad.getTipoReserva() + ").");
					return;
				}
			}
			// Creamos las nuevas asignaciones entre las líneas de actividad y la reserva seleccionada
			for (LineaActividad actividad : lineas) {
				AsignacionReserva a = hsController.agregarAsignacionReserva(actividad, reserva);
				System.out.println("Asignación: " + a);
			}
			addDirtyFlag();
		}
	}
	
	
	private void agregarReserva(Shell shell, Set<LineaActividad> lineas) {
		LineaActividad linea = lineas.iterator().next();
		String tipo = linea.getTipoReserva();
		try {
			AbstractAEPTitleAreaDialog dialogo = null;
			if (tipo.equals("Transporte")) {
				dialogo = new ReservaTransporteDialog(shell, linea, null);
			} else if (tipo.equals("Vuelo")) {
				dialogo = new ReservaVueloDialog(shell, linea, null);
			} else if (tipo.equals("Boletos")) {
				dialogo = new ReservaBoletosDialog(shell, linea, null); 
			} else if (tipo.equals("Alimentación")) {
				dialogo = new ReservaAlimentacionDialog(shell, linea, null);
			} else if (tipo.equals("Tour")) {
				dialogo = new ReservaTourDialog(shell, hsController, linea, null);
			} else if (tipo.equals("Hospedaje")) {
				dialogo = new ReservaHospedajeDialog(shell, hsController, linea, null);
			} else {
				MessageDialog.openInformation(shell, "Agregar reserva",
						"No se han definido pantallas para las reservas de tipo: " + tipo + ".");
			}
			
			if (dialogo.open() == IDialogConstants.OK_ID) {
				IReserva reserva = ((IReservaDialog) dialogo).getReserva();
				// Creamos las asignaciones y establecemos la relación entre cada línea de actividad
				// seleccionada y la nueva reserva.
				for (LineaActividad actividad : lineas) {
					AsignacionReserva a = hsController.agregarAsignacionReserva(actividad, reserva);
					System.out.println(a);
				}
				addDirtyFlag();
			}
			
		} catch (Exception e) {
			mensajeError(shell, e);
			return;
		}
	}
	
	
	
	private void editarReserva(Shell shell, IReserva reserva) {
		try {
			if (reserva == null) {
				MessageDialog.openInformation(shell, "Editar reserva",
						"Debe seleccionar una reserva para ejecutar esta acción.");
				return;
			}
			AbstractAEPTitleAreaDialog dialogo = null;
			System.out.println("Tipo de reserva: " + reserva.getTipoReserva());
// Martillo chévere para cargar el objeto correcto en memoria, y evitar el problema con los proxys
// de hibernate.
// TODO: buscar forma de evitar este problema con los proxys
			if (reserva instanceof HibernateProxy) {
				reserva = (IReserva) ((HibernateProxy) reserva).getHibernateLazyInitializer().getImplementation();
				System.out.println("Hoja: " + reserva.getHoja());
			}
			
			System.out.println("Hoja: " + reserva.getHoja());
			if (reserva instanceof ReservaTransporte) {
				dialogo = new ReservaTransporteDialog(shell, (ReservaTransporte) reserva);
			} else if (reserva instanceof ReservaVuelo) {
				LineaActividad lineaAct = reserva.getListaActividades().iterator().next();
				dialogo = new ReservaVueloDialog(shell, lineaAct, (ReservaVuelo) reserva);
			} else if (reserva instanceof ReservaBoleto) {
				LineaActividad lineaAct = reserva.getListaActividades().iterator().next();
				dialogo = new ReservaBoletosDialog(shell, lineaAct, (ReservaBoleto) reserva);
			} else if (reserva instanceof ReservaAlimento) {
				LineaActividad lineaAct = reserva.getListaActividades().iterator().next();
				dialogo = new ReservaAlimentacionDialog(shell, lineaAct, (ReservaAlimento) reserva);
			} else if (reserva instanceof ReservaTour) {
				LineaActividad lineaAct = reserva.getListaActividades().iterator().next();
				dialogo = new ReservaTourDialog(shell, hsController, lineaAct, (ReservaTour) reserva);
			} else if (reserva instanceof ReservaHospedaje) {
				LineaActividad lineaAct = reserva.getListaActividades().iterator().next();
				dialogo = new ReservaHospedajeDialog(shell, hsController, lineaAct, (ReservaHospedaje) reserva);
			} else {
				MessageDialog.openInformation(shell, "Editar reserva",
						"No se han definido pantallas para las reservas de tipo: " + reserva.getTipoReserva() + ", " + reserva.getClass());
			}
			if (dialogo != null && dialogo.open() == IDialogConstants.OK_ID) {
				System.out.println("Comentario: " + reserva.getComentario());
				viewerReservas.refresh();
				addDirtyFlag();
			}
		
		} catch (Exception e) {
			mensajeError(shell, e);
			return;
		}
		
	}
	
	
	
// ----------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	public IStructuredSelection getElementosSeleccionados(StructuredViewer viewer) {
		return ((IStructuredSelection) viewer.getSelection());
	}
	
	
	public Object getElementoSeleccionado(StructuredViewer viewer) {
		Object seleccion = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
		return seleccion;
	}
	
	
	@Override
	public void dispose() {
		if (this.isDirty()) {
			actualizarVista(HojasView.ID);
		}
		hsController.finalizar(idSession);
		super.dispose();
	}
	
	/**
	 * Retorna el id de sesión generado para Hibernate.  Utilizado en FormalizarHojaAction.java
	 * @return id de la sesión de Hibernate.
	 */
	public String getIdSession() {
		return idSession;
	}
}

