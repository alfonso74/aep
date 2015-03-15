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
import rcp.manticora.dialogs.AgregarGuiaHoja;
import rcp.manticora.dialogs.AgregarPaxCotizacion;
import rcp.manticora.dialogs.AgregarTour2Hoja2;
import rcp.manticora.dialogs.ReservaAlimentacionDialog;
import rcp.manticora.dialogs.ReservaBoletosDialog;
import rcp.manticora.dialogs.ReservaHospedajeDialog;
import rcp.manticora.dialogs.ReservaTourDialog;
import rcp.manticora.dialogs.ReservaTransporteDialog;
import rcp.manticora.dialogs.ReservaVueloDialog;
import rcp.manticora.model.HojaServicioTour;
import rcp.manticora.model.IReserva;
import rcp.manticora.model.LineaActividad;
import rcp.manticora.model.Pax;
import rcp.manticora.model.ReservaAlimento;
import rcp.manticora.model.ReservaBoleto;
import rcp.manticora.model.ReservaGuia;
import rcp.manticora.model.ReservaHospedaje;
import rcp.manticora.model.ReservaTour;
import rcp.manticora.model.ReservaTransporte;
import rcp.manticora.model.ReservaVuelo;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.GenericSorter;
import rcp.manticora.views.HojasView;


public class HojaOpsEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.hojaOperaciones";
	private final String pluginId = Application.PLUGIN_ID;
	private String idSession = ID + FechaUtil.getMilisegundos();
	
	private Text txtNombre;
	private Text txtInicio;
	private Text txtFin;
	private Text txtCapacidad;
	private Text txtOcupacion;
	
	private Text txtEstado;
	private Text txtNumero;
	private Text txtNumeroTour;
	
	private Text txtComentario;
	private Text txtHojas;
	
	private TreeViewer viewerAct;
	private TableViewer viewerPaxs;
	private TableViewer viewerReservas;
	private TableViewer viewerGuias;
	
	private Button bFechaIni;
	private Button bFechaFin;
	
	private HojaServicioTour registro;
	
	private ComboData cdStatus;
	private ImageDescriptor image;
	private HojaServicioController hsController;
	
	public HojaOpsEditor() {
		super();
		hsController = new HojaServicioController(idSession);
		cdStatus = hsController.getComboDataKeyword(TipoKeyword.STATUS_HOJA_SERV);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		// campos comunes en HojaServicio
		Date pInicio = FechaUtil.toDate(txtInicio.getText());
		Date pFin = FechaUtil.toDate(txtFin.getText());
		String pNombre = txtNombre.getText();
		String pComentario = txtComentario.getText();
		String pEstado = cdStatus.getCodeByName(txtEstado.getText());
		// campos propios de HojaServicioTour
		Integer pCapacidad = txt2Integer(txtCapacidad.getText());
		
		// seteamos los campos comunes de HojaServicio
		registro.setIdProducto(0L);
		registro.setFechaInicio(pInicio);
		registro.setFechaFin(pFin);
		registro.setNombre(pNombre);
		registro.setComentario(pComentario);
		registro.setEstado(pEstado);
		// y los particulares de HojaServicioTour
		registro.setCapacidad(pCapacidad);
		registro.setNumero("");
		registro.setTipo("");
		
		hsController.doSave(registro);
		if (isNewDoc) {
			txtNumero.setText(Long.toString(registro.getIdHoja()));
			isNewDoc = false;
		}

//		 reflejamos el nombre en el tab
		this.setPartName(registro.getTituloDocumento());
// actualizamos el nombre del editorInput (para que deje de parecer "Nuevo")
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		llenarControles();
		// refresh de todas las vistas abiertas
		actualizarVistas();
		//actualizarVista(HojasView.ID);
		viewerAct.refresh();
		viewerPaxs.refresh();
		viewerReservas.refresh();
		viewerGuias.refresh();
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
	
	
	@Override
	protected void agregarControles(Composite parent) {
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// tab de datos generales
		TabItem tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("Información general");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/infoGeneral2.gif");
		tabGeneral.setImage(image.createImage());
		tabGeneral.setControl(crearControlesTabGeneral(tabFolder));
		
		// tab de actividades
		TabItem tabActividades2 = new TabItem(tabFolder, SWT.NONE);
		tabActividades2.setText("Actividades");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/actividades.gif");
		tabActividades2.setImage(image.createImage());
		tabActividades2.setControl(crearControlesTabActividades(tabFolder));
		
		// tab de clientes (paxs)
		TabItem tabClientes = new TabItem(tabFolder, SWT.NONE);
		tabClientes.setText("Detalle de PAXs");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/detallePAX2.gif");
		tabClientes.setImage(image.createImage());
		tabClientes.setControl(crearControlesTabPaxs(tabFolder));
		
		// tab de reservas
		TabItem tabReservas = new TabItem(tabFolder, SWT.NONE);
		tabReservas.setText("Reservas");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/informacion.gif");
		tabReservas.setImage(image.createImage());
		tabReservas.setControl(crearControlesTabReservas(tabFolder));
		
		// tab de asignación de guías
		TabItem tabGuias = new TabItem(tabFolder, SWT.NONE);
		tabGuias.setText("Guías");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/cliente.gif");
		tabGuias.setImage(image.createImage());
		tabGuias.setControl(crearControlesTabGuias(tabFolder));
	}

	@Override
	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nueva hoja de servicios (tour)");
			txtEstado.setText("Nueva");
			registro = new HojaServicioTour();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
			viewerAct.setInput(registro.getListaActividades());
			viewerPaxs.setInput(registro.getListaPaxs());
			viewerReservas.setInput(registro.getListaReservas());
			viewerGuias.setInput(registro.getListaResGuias());
		} else{
			System.out.println("Ejecutando código para cargar datos...");
			registro = (HojaServicioTour) ((CommonEditorInput) this.getEditorInput()).getElemento();

			hsController.getSession().refresh(registro, LockMode.READ);
			hsController.verSesiones();
			txtNombre.setText(valor2Txt(registro.getNombre()));
			txtInicio.setText(FechaUtil.toString(registro.getFechaInicio()));
			txtFin.setText(FechaUtil.toString(registro.getFechaFin()));
			txtCapacidad.setText(valor2Txt(registro.getCapacidad()));
			txtOcupacion.setText(registro.getDisponibilidad().getResumenDisp());
			txtEstado.setText(cdStatus.getTextoByKey(registro.getEstado()));
			txtNumero.setText(valor2Txt(registro.getIdHoja()));
			txtNumeroTour.setText(valor2Txt(registro.getNumero()));
			//txtFechaEntrada.setText(FechaUtil.toString(registro.getFechaOperaciones(), FechaUtil.formatoFecha));
			txtComentario.setText(valor2Txt(registro.getComentario()));
			txtHojas.setText(hsController.getHojasAsociadas(registro));

			viewerAct.setInput(registro.getListaActividades());
			viewerPaxs.setInput(registro.getListaPaxs());
			viewerReservas.setInput(registro.getListaReservas());
			viewerGuias.setInput(registro.getListaResGuias());
			viewerAct.refresh();
			viewerPaxs.refresh();
			viewerReservas.refresh();
			viewerGuias.refresh();
			hsController.generarNumeroTour();
			
		}
		addFilledFlag();
		setFocoInicial(txtNombre);
		
	}
	
	
	private Control crearControlesTabGeneral(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);
		
		Label l;
		GridData gridData;

// *************** Grupo de información general ***************************
		Group grupoTop = new Group(composite, SWT.NONE);
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
		l.setText("Capacidad:");
		txtCapacidad = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(40,15);
		gridData.horizontalSpan = 2;
		txtCapacidad.setLayoutData(gridData);
		txtCapacidad.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoTop, SWT.NONE);
		l.setText("Ocupación:");
		txtOcupacion = new Text(grupoTop, SWT.BORDER);
		gridData = new GridData(75,15);
		gridData.horizontalSpan = 2;
		txtOcupacion.setLayoutData(gridData);
		txtOcupacion.setEditable(false);
		
		
// *************** Grupo de status de HS ***************************
		Group grupoStatus = new Group(composite, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		grupoStatus.setLayout(gridLayout);
		grupoStatus.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		
		l = new Label(grupoStatus, SWT.None);
		l.setText("Estado HS:");
		txtEstado = new Text(grupoStatus, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(50,15);
		txtEstado.setLayoutData(gridData);
		txtEstado.setEditable(false);
		
		l = new Label(grupoStatus, SWT.None);
		l.setText("Número HS:");
		txtNumero = new Text(grupoStatus, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(35,15);
		txtNumero.setLayoutData(gridData);
		txtNumero.setEditable(false);
		
		l = new Label(grupoStatus, SWT.None);
		l.setText("Número tour:");
		txtNumeroTour = new Text(grupoStatus, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(60,15);
		txtNumeroTour.setLayoutData(gridData);
		txtNumeroTour.setEditable(false);
		
		
// *************************** Grupo de Comentarios *************************
		
		Group grupoBottom = new Group(composite, SWT.NONE);
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
		
		
// *************************** Grupo de hojas suscritas *************************
		Group grupoHojas = new Group(composite, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grupoHojas.setLayout(gridLayout);
		gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData.horizontalSpan = 1;
		grupoHojas.setLayoutData(gridData);
		grupoHojas.setText(" Hojas con reserva ");
		
		//l = new Label(grupoHojas, SWT.NONE);
		//l.setText("Hojas suscritas");
		
		txtHojas = new Text(grupoHojas, SWT.BORDER | SWT.V_SCROLL);
		gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData.heightHint = 55;
		txtHojas.setLayoutData(gridData);
		txtHojas.setEditable(false);
		
		
		return composite;
	}
	
	
	
	private Control crearControlesTabActividades(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		//crearControlesTabActividades2(composite);
		
		Group grupoTabla = new Group(composite, SWT.None);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grupoTabla.setLayout(gridLayout);
		grupoTabla.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		viewerAct = new TreeViewer(grupoTabla, style);
		crearColumnasActividades(viewerAct);
		viewerAct.setContentProvider(new ViewContentProviderAct2());
		viewerAct.setLabelProvider(new ViewLabelProviderAct2());
		//viewerAct.setSorter(new GenericSorter(viewerAct));
		viewerAct.setSorter(new GenericSorter(viewerAct, 0, GenericSorter.FECHAHORA));
		
		//registramos al viewer como un selection provider para determinar
		//qué elemento está seleccionado
		getSite().setSelectionProvider(viewerAct);
		
		
		Composite botones = new Composite(composite, SWT.NONE);
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
				LineaActividad la = getActividadDefault(viewerAct);
				if (la != null) {
					String fechaDefault = FechaUtil.toString(la.getFecha());
					agregarActividad(getSite().getShell(), fechaDefault);
				} else {
					agregarActividad(getSite().getShell(), txtInicio.getText());
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
				//LineaActividad la = getActividadDefault(viewerAct);
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
		/*
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
		*/
		
		Button bTour = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		bTour.setLayoutData(gridData);
		bTour.setText("Ver tours");
		bTour.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de 'Ver tours'");
				AgregarTour2Hoja2 dialogo = new AgregarTour2Hoja2(getSite().getShell());
				dialogo.open();
			}
		});
		
		return composite;
	}
	
	
	private Control crearControlesTabPaxs(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		Group grupoTabla = new Group(composite, SWT.None);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grupoTabla.setLayout(gridLayout);
		grupoTabla.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		Table tablaPaxs = crearTablaPaxs(grupoTabla);
		viewerPaxs = new TableViewer(tablaPaxs);
		viewerPaxs.setContentProvider(new ViewContentProviderPax());
		viewerPaxs.setLabelProvider(new ViewLabelProviderPax());
		
// ******************** adición de botones para paxs *********************
		Composite botones = new Composite(composite, SWT.NONE);
		gridLayout = new GridLayout(3, false);
		botones.setLayout(gridLayout);
		
		Button bAgregar = new Button(botones, SWT.PUSH);
		GridData gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bAgregar.setLayoutData(gridData);
		bAgregar.setText("Agregar");
		//bAgregar.setEnabled(false);
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
		
		return composite;
	}
	
	
	private Control crearControlesTabReservas(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		Group grupoTabla = new Group(composite, SWT.None);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grupoTabla.setLayout(gridLayout);
		grupoTabla.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		Table tablaReservas = crearTablaReservas(grupoTabla);
		viewerReservas = new TableViewer(tablaReservas);
		viewerReservas.setContentProvider(new ViewContentProviderRes());
		viewerReservas.setLabelProvider(new ViewLabelProviderRes());
		//viewerReservas.setInput(registro.getListaReservas());
		viewerReservas.setSorter(new GenericSorter(viewerReservas));
		((GenericSorter) viewerReservas.getSorter()).doSort(2, GenericSorter.NUMERO, 1);
		
		//registramos al viewer como un selection provider para determinar
		//qué elemento está seleccionado
		getSite().setSelectionProvider(viewerAct);
		
// ****************** adición de botones de reservas *********************
		Composite botones = new Composite(composite, SWT.NONE);
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
		
		return composite;
	}
	
	
	private Control crearControlesTabGuias(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		Group grupoTabla = new Group(composite, SWT.None);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grupoTabla.setLayout(gridLayout);
		grupoTabla.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		Table tablaGuias = crearTablaGuias(grupoTabla);
		viewerGuias = new TableViewer(tablaGuias);
		viewerGuias.setContentProvider(new ViewContentProviderGuias());
		viewerGuias.setLabelProvider(new ViewLabelProviderGuias());
		//viewerReservas.setInput(registro.getListaReservas());
		viewerGuias.setSorter(new GenericSorter(viewerGuias, 1));
		
		//registramos al viewer como un selection provider para determinar
		//qué elemento está seleccionado
		getSite().setSelectionProvider(viewerAct);
		
// ****************** adición de botones de reservas *********************
		Composite botones = new Composite(composite, SWT.NONE);
		gridLayout = new GridLayout(3, false);
		botones.setLayout(gridLayout);
		GridData gridData;
		
		Button bAgregar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bAgregar.setLayoutData(gridData);
		bAgregar.setText("Agregar");
		bAgregar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de agregar asignación de guía...");
				agregarReservaGuia(getSite().getShell());
			}
		});
		
		Button bEditar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bEditar.setLayoutData(gridData);
		bEditar.setText("Editar");
		bEditar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de editar asignación de guía...");
				ReservaGuia reserva = (ReservaGuia) getElementoSeleccionado(viewerGuias);
				editarReservaGuia(getSite().getShell(), reserva);
			}
		});
		viewerGuias.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("Editar asignación de guía (doble clic)...");
				ReservaGuia reserva = (ReservaGuia) getElementoSeleccionado(viewerGuias);
				editarReservaGuia(getSite().getShell(), reserva);
			}
		});
		
		Button bBorrar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bBorrar.setLayoutData(gridData);
		bBorrar.setText("Borrar");
		bBorrar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de borrar asignación de guía...");
				ReservaGuia reserva = (ReservaGuia) getElementoSeleccionado(viewerGuias);
				System.out.println("Selección: " + reserva);
				hsController.eliminarGuia(registro, reserva);
				viewerGuias.refresh();
				addDirtyFlag();   // como borramos, indicamos que se ha modificado la cotización
			}
		});	
		
		return composite;
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
	
	
	private Table crearTablaPaxs(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Table tablaPaxs = new Table(parent, style);
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
		Table tablaAct = new Table(parent, style);
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
	
	
	private Table crearTablaGuias(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Table tablaGuias = new Table(parent, style);
		tablaGuias.setLinesVisible(true);
		tablaGuias.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tablaGuias.setLayoutData(gridData);
		
		TableColumn column = new TableColumn(tablaGuias, SWT.LEFT, 0);
		column.setText("Nombre");
		column.setWidth(100);
		
		column = new TableColumn(tablaGuias, SWT.LEFT, 1);
		column.setText("Apellido");
		column.setWidth(100);
		
		column = new TableColumn(tablaGuias, SWT.CENTER, 2);
		column.setText("Fecha");
		column.setWidth(140);
		
		column = new TableColumn(tablaGuias, SWT.LEFT, 3);
		column.setText("Comentarios");
		column.setWidth(200);
		
		return tablaGuias;
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
			System.out.println("Ejecutando hasChildren()");
			return ((LineaActividad) element).getHojaTour() != null;
			//return ((LineaActividad) element).getChildren().size() > 0;
		}

		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set<LineaActividad>) inputElement).toArray();
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
	
	
	class ViewContentProviderPax implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set<Pax>) inputElement).toArray();
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
			switch (index) {
			case 0:
				resultado = reserva.getTipoReserva();
				if (reserva instanceof ReservaHospedaje) {
					if (((ReservaHospedaje) reserva).getProducto().isHotelAEP()) {
						resultado += " (AEP)";
					} else {
						resultado += " (Externo)";
					}
				}
				break;
			case 1:
				resultado = reserva instanceof ReservaTour ? "ReservaTour" : "Otro";
//				resultado = ((ReservaTour) reserva).getDisponibilidad().getTour().getNombre();
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
	
	
	
	class ViewContentProviderGuias implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set<ReservaGuia>) inputElement).toArray();
			return resultados;
		}
	}
	
	
	class ViewLabelProviderGuias extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			ReservaGuia reserva = (ReservaGuia) obj;
			switch (index) {
			case 0:
				resultado = reserva.getGuia().getNombre();
				break;
			case 1:
				resultado = reserva.getGuia().getApellido();
				break;
			case 2:
				resultado = reserva.getFechaDspReserva();
				break;
			case 3:
				resultado = "Comentario";
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
		Object seleccion = getElementoSeleccionado(viewerPaxs);
		Pax pax = (Pax) seleccion;
		return pax;
	}
	

	
// ******************************* manejo de reservas *****************************
	
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
	

	
// ******************************* manejo de guías *****************************
	
	private void agregarReservaGuia(Shell shell) {
		ReservaGuia reserva = new ReservaGuia();
		AgregarGuiaHoja dialogo = new AgregarGuiaHoja(shell, reserva);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			hsController.agregarGuia(registro, reserva);
			for (ReservaGuia r : registro.getListaResGuias()) {
				System.out.println("RG: " + r);
			}
			viewerGuias.refresh();
			addDirtyFlag();
		}
	}
	
	
	private void editarReservaGuia(Shell shell, ReservaGuia reserva) {
		AgregarGuiaHoja dialogo = new AgregarGuiaHoja(shell, reserva);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			viewerGuias.refresh();
			addDirtyFlag();
		}
	}
	
	
	
// --------------------------------------------------------------------------------------------
	
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
}
