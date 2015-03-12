package rcp.manticora.editors;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;


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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.Session;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.CotizacionesController;
import rcp.manticora.dialogs.AgregarActividadCotizacion;
import rcp.manticora.dialogs.AgregarPaxCotizacion;
import rcp.manticora.dialogs.BuscarClientesDialog;
import rcp.manticora.dialogs.ImportarTemplate;
import rcp.manticora.dialogs.MyInputDialogData;
import rcp.manticora.model.Cliente;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.model.ICliente;
import rcp.manticora.model.LineaCotizacion;
import rcp.manticora.model.Pax;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.GenericSorter;
import rcp.manticora.services.HibernateUtil;

public class CotizacionesEditorV0 extends AbstractEditorH {
	public static final String ID = "manticora.editors.cotizaciones";
	private Text txtNumero;
	private Text txtANombreDe;
	private Text txtInicio;
	private Text txtFin;
	private Text txtEstado;
	private Text txtPaxs;
	private Combo comboVendedor;
	private Text txtPago;
	private Float porcPago;
	
	private Text txtNoCliente;
	private Text txtNombreCliente;
	
	private Label lSubtotal;
	private Text txtPorcHospedaje;
	private Label lHospedaje;
	private Text txtPorcImpuesto;
	private Label lImpuesto;
	private Label lTotal;
	
	private Table tablaAct;
	private TableViewer viewerAct;
	private Table tablaPaxs;
	private TableViewer viewerPaxs;
	
	private Vector<LineaCotizacion> vLineasAct;
	private Vector<Pax> vLineasPax;
	private ICliente cliente;
	
	private Cotizacion registro;
	private ComboData comboDataVendedor;
	private ComboData comboDataStatus;
	private ComboDataController cdController;
	private CotizacionesController editorController;
	
	private Date fechaFinalizacion = null;
	
	private Button bFechaIni;
	private Button bFechaFin;
	
	private ImageDescriptor image;
	
	public CotizacionesEditorV0() {
		super();
		editorController = new CotizacionesController(ID);
		vLineasAct = new Vector<LineaCotizacion>();
		vLineasPax = new Vector<Pax>();
		cdController = new ComboDataController();
	}
	
	public Cotizacion getCotizacion() {
		return registro;
	}
	
	public void doSave(IProgressMonitor monitor) {
		calcularTotales();
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		Date pInicio = FechaUtil.toDate(txtInicio.getText());
		Date pFin = FechaUtil.toDate(txtFin.getText());
		String pNombre = txtANombreDe.getText();
		String pProspecto = txtNombreCliente.getText();
		Float pSubTotal = txt2Float(lSubtotal.getText());
		Float pPorcHospedaje = txt2Float(txtPorcHospedaje.getText());
		Float pHospedaje = txt2Float(lHospedaje.getText());
		Float pPorcImpuesto = txt2Float(txtPorcImpuesto.getText());
		Float pImpuesto = txt2Float(lImpuesto.getText());
		Float pTotal = txt2Float(lTotal.getText());
		Float pPago = txt2Float(txtPago.getText());
		porcPago = (pPago / pTotal) * 100;
		Integer pPaxs = txt2Integer(txtPaxs.getText());
		Long pIdVendedor = comboDataVendedor.getKeyAsLongByIndex(comboVendedor.getSelectionIndex());
		String pVendedor = comboVendedor.getText();
		String pEstado = comboDataStatus.getCodeByName(txtEstado.getText());
		Date pFechaFinalizacion = fechaFinalizacion;
		
		/*
//		si es nuevo creamos la actividad base y los detalles de las actividades
		if (isNewDoc) {
			System.out.println("Creando nueva cotización...");
			// creamos la cotización
			registro = controller.addCotizacion(-1L, pInicio, pFin, pNombre,
					pSubTotal, pPorcHospedaje, pHospedaje, pPorcImpuesto, pImpuesto,
					pTotal, pPago, pPaxs, pEstado);
			registro.setIdVendedor(pIdVendedor);
			registro.setDspVendedor(pVendedor);
			registro.setPorcPago(porcPago);
			registro.setCliente(cliente);
			registro.setProspecto(pProspecto);
			// agregamos las líneas de detalle
			for (int n = 0; n < vLineasAct.size(); n++) {
				registro.agregarActividad((LineaCotizacion) vLineasAct.elementAt(n));
			}
			// agregamos los integrantes del tour
			for (int n = 0; n < vLineasPax.size(); n++) {
				System.out.println("id pax: " + ((Pax) vLineasPax.elementAt(n)).getIdPax());
				registro.agregarPax((Pax) vLineasPax.elementAt(n));
			}
			controller.update(registro);
			txtNumero.setText(Long.toString(registro.getIdCotizacion()));
			// el documento deja de ser nuevo al guardarlo
			isNewDoc = false;
			
		} else {
			System.out.println("Actualizando cotización no. " + registro.getIdCotizacion() + "...");
			System.out.println("Pago: " + pPago);
			registro.resetListaActividades();
			registro.resetListaPaxs();
			
			for (int n = 0; n < vLineasAct.size(); n++) {
				registro.agregarActividad((LineaCotizacion) vLineasAct.elementAt(n));
			};
			for (int n = 0; n < vLineasPax.size(); n++) {
				System.out.println("id pax: " + ((Pax) vLineasPax.elementAt(n)).getIdPax());
				registro.agregarPax((Pax) vLineasPax.elementAt(n));
			}
			
			registro.setCliente(cliente);
			registro.setProspecto(pProspecto);
			registro.setNombre(pNombre);
			registro.setFechaInicio(pInicio);
			registro.setFechaFin(pFin);
			registro.setSubtotal(pSubTotal);
			registro.setPorcHospedaje(pPorcHospedaje);
			registro.setHospedaje(pHospedaje);
			registro.setPorcImpuesto(pPorcImpuesto);
			registro.setImpuesto(pImpuesto);
			registro.setTotal(pTotal);
			registro.setPago(pPago);
			registro.setPorcPago(porcPago);
			registro.setPaxs(pPaxs);
			registro.setIdVendedor(pIdVendedor);
			registro.setDspVendedor(pVendedor);
			registro.setEstado(pEstado);
			registro.setFechaFinalizacion(pFechaFinalizacion);
			controller.update(registro);
		}
		*/
		
		registro.setCliente(cliente);
		registro.setProspecto(pProspecto);
		registro.setNombre(pNombre);
		registro.setFechaInicio(pInicio);
		registro.setFechaFin(pFin);
		registro.setSubtotal(pSubTotal);
		registro.setPorcHospedaje(pPorcHospedaje);
		registro.setHospedaje(pHospedaje);
		registro.setPorcImpuesto(pPorcImpuesto);
		registro.setImpuesto(pImpuesto);
		registro.setTotal(pTotal);
		registro.setPago(pPago);
		registro.setPorcPago(porcPago);
		registro.setPaxs(pPaxs);
		registro.setIdVendedor(pIdVendedor);
		registro.setDspVendedor(pVendedor);
		registro.setEstado(pEstado);
		registro.setFechaFinalizacion(pFechaFinalizacion);
		// actualizamos los collections
		registro.resetListaActividades();
		registro.resetListaPaxs();
		for (int n = 0; n < vLineasAct.size(); n++) {
			registro.agregarActividad((LineaCotizacion) vLineasAct.elementAt(n));
		};
		for (int n = 0; n < vLineasPax.size(); n++) {
			System.out.println("id pax: " + ((Pax) vLineasPax.elementAt(n)).getIdPax());
			registro.agregarPax((Pax) vLineasPax.elementAt(n));
		}
		// y mandamos a grabar el registro
		//controller.update(registro);
		editorController.doSave(registro);
		if (isNewDoc) {
			System.out.println("Creando nueva cotización...");
			txtNumero.setText(valor2Txt(registro.getIdCotizacion()));
			isNewDoc = false;
		} else {
			System.out.println("Actualizando cotización no. " + registro.getIdCotizacion());
		}
		
// reflejamos el nombre en el tab
		this.setPartName(registro.getTituloDocumento());
// actualizamos el nombre del editorInput (para que deje de parecer "Nuevo")
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
// hacemos un refresh de todas las vistas que estén abiertas
		actualizarVistas();
		viewerAct.refresh();
		removeDirtyFlag();
	}
	
	private boolean validarSave() {
		String pNombre = txtANombreDe.getText();
		String pNombreCliente = txtNombreCliente.getText();
		
		if (pNombre.length() > 50) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre de la cotización no puede superar los 50 caracteres (" + pNombre.length() + ").");
			// TODO ninguno funciona para el foco del campo
			//setFocoInicial(txtANombreDe);
			//txtANombreDe.forceFocus();
			//txtANombreDe.setFocus();
			return false;
		}
		if (pNombreCliente.length() > 50) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre del cliente no puede superar los 50 caracteres (" + pNombreCliente.length() + ").");
			return false;
		}
		if (comboVendedor.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"Debe seleccionar un vendedor para la cotización.");
			return false;
		}
		return true;
	}
	
	public boolean validarGeneracionPdf() {
		String pNombreCliente = txtNombreCliente.getText();
		Integer pPAXs = txt2Integer(txtPaxs.getText());
		if (pNombreCliente.equals("")) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"Debe especificar el nombre del cliente.");
			return false;
		}
		if (pPAXs.intValue() < 1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El número de paxs no puede ser menor de 1.");
			return false;
		}
		return true;
	}
	
	public boolean validarGeneracionHS() {
		String pEstado = registro.getEstado();
		Float pPago = txt2Float(txtPago.getText());
		Float pMonto = txt2Float(lTotal.getText());
		Integer pPAXs = txt2Integer(txtPaxs.getText());
		int lineasPAXs = viewerPaxs.getTable().getItemCount();
		if (pEstado.equals("P")) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"No puede generar una hoja de servicio para cotizaciones en status " + txtEstado.getText());
			return true;
		}
		if (pPago < 0 || pPago > pMonto) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El valor del campo Pago es inválido");
			return false;
		}
		if (pPAXs.intValue() != lineasPAXs) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
			"El número de PAXs no coincide con los detalles especificados.");
			return false;
		}
		if (registro.getCliente() == null) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
			"Debe asignar un cliente a la cotización.");
			return false;
		}
		return true;
	}
	
	protected void agregarControles(Composite parent) {
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		// llena el espacio vertical y horizontal
		tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		// tab de datos generales
		TabItem tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("Información general");
		image = AbstractUIPlugin.imageDescriptorFromPlugin("manticora", "icons/infoGeneral2.gif");
		tabGeneral.setImage(image.createImage());
		tabGeneral.setControl(getTabGeneralControl(tabFolder));
		// tab de actividades
		TabItem tabActividades = new TabItem(tabFolder, SWT.NONE);
		tabActividades.setText("Actividades");
		image = AbstractUIPlugin.imageDescriptorFromPlugin("manticora", "icons/actividades.gif");
		tabActividades.setImage(image.createImage());
		tabActividades.setControl(getTabActividadesControl(tabFolder));
		// tab de clientes (paxs)
		TabItem tabClientes = new TabItem(tabFolder, SWT.NONE);
		tabClientes.setText("Detalle de PAXs");
		image = AbstractUIPlugin.imageDescriptorFromPlugin("manticora", "icons/detallePAX.gif");
		tabClientes.setImage(image.createImage());
		tabClientes.setControl(getTabClientesControl(tabFolder));
	}
	
	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nueva cotización");
			registro = new Cotizacion();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
			txtEstado.setText("Activa");
			txtPago.setText("0.00");
			txtPorcImpuesto.setText("5");
			comboDataStatus = cdController.getComboDataKeyword("Status de cotizaciones");
		} else {
			System.out.println("Ejecutando código para cargar datos...");
			registro = (Cotizacion) ((CommonEditorInput) this.getEditorInput()).getElemento();
			
			// obtenemos una sesión de hibernate para cargar las actividades y refrescar
			// todos los objetos asociados
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.refresh(registro);
			
			txtNumero.setText(valor2Txt(registro.getIdCotizacion()));
			txtANombreDe.setText(valor2Txt(registro.getNombre()));
			txtInicio.setText(FechaUtil.toString(registro.getFechaInicio()));
			txtFin.setText(FechaUtil.toString(registro.getFechaFin()));
			comboDataStatus = cdController.getComboDataKeyword("Status de cotizaciones");
			txtEstado.setText(comboDataStatus.getTextoByKey(registro.getEstado()));
			txtPaxs.setText(valor2Txt(registro.getPaxs()));
			System.out.println(registro.getIdVendedor() + "ja: " + registro.getDspVendedor());
			comboVendedor.select(comboVendedor.indexOf(valor2Txt(registro.getDspVendedor())));
			txtPago.setText(valor2Txt(registro.getPago(), "0.00"));
			// cargamos datos del cliente
			if (registro.getCliente() != null) {
				cliente = registro.getCliente();
				txtNoCliente.setText(valor2Txt(cliente.getIdCliente()));
				txtNombreCliente.setText(valor2Txt(cliente.getNombreCliente()));
				txtNombreCliente.setEnabled(false);
			} else {
				txtNombreCliente.setText(valor2Txt(registro.getProspecto()));
				txtNombreCliente.setEnabled(true);
			}
			// cargamos los totales de la cotización
			lSubtotal.setText(valor2Txt(registro.getSubtotal(), "0.00"));
			//txtPorcHospedaje.setText(valor2Txt(registro.getPorcDescuento(), "0"));
			lHospedaje.setText(valor2Txt(registro.getHospedaje(), "0.00"));
			txtPorcImpuesto.setText(valor2Txt(registro.getPorcImpuesto(), "0"));
			lImpuesto.setText(valor2Txt(registro.getImpuesto(), "0.00"));
			lTotal.setText(valor2Txt(registro.getTotal(), "0.00"));
			// cargamos las líneas y los pasajeros
			vLineasAct.addAll(registro.getListaActividades());
			vLineasPax.addAll(registro.getListaPaxs());
			System.out.println("Subtotal: " + registro.getSubtotal());
			session.close();
			viewerAct.refresh();
			viewerPaxs.refresh();
			System.out.println("Subtotal: " + registro.getSubtotal());
		}
		addFilledFlag();
		setFocoInicial(txtANombreDe);
		calcularTotales();
		//System.out.println("Subtotal: " + registro.getSubtotal());
	}
	
	private Control getTabGeneralControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		//gridLayout.verticalSpacing = 10;
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
	
	private Control getTabClientesControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		crearControlesTabClientes(composite);
		return composite;
	}

	private void crearControlesTabGeneral(Composite parent) {
		Label l;
		GridLayout gridLayout;
		GridData gridData;

		Group grupoTop = new Group(parent, SWT.NONE);
		//grupoTop.setText("Información General");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 8;
		grupoTop.setLayout(gridLayout);
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		grupoTop.setLayoutData(gridData);
		
		l = new Label(grupoTop, SWT.None);
		l.setText("Nombre:");
		txtANombreDe = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(300,15);
		gridData.horizontalSpan = 5;
		txtANombreDe.setLayoutData(gridData);
		txtANombreDe.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoTop, SWT.None);
		l.setText("Cotización:");
		txtNumero = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(40,15);
		txtNumero.setLayoutData(gridData);
		txtNumero.setEnabled(false);
		//txtNumero.setEditable(false);
		
		l = new Label(grupoTop, SWT.None);
		l.setText("Fecha inicio:");
		txtInicio = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		txtInicio.setLayoutData(new GridData(60,15));
		txtInicio.setTextLimit(10);
		txtInicio.addModifyListener(this.createModifyListener());

		bFechaIni = new Button(grupoTop, SWT.NONE);
		bFechaIni.setLayoutData(new GridData(16,16));
		image = AbstractUIPlugin.imageDescriptorFromPlugin("manticora", "icons/dateChooser.gif");
		bFechaIni.setImage(image.createImage());
		bFechaIni.addSelectionListener(this.crearCalendario(txtInicio));
		
		l = new Label(grupoTop, SWT.None);
		l.setText("Fecha fin:");
		txtFin = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(60,15);
		txtFin.setLayoutData(gridData);
		txtFin.setTextLimit(10);
		txtFin.addModifyListener(this.createModifyListener());

		bFechaFin = new Button(grupoTop, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 3;
		bFechaFin.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin("manticora", "icons/dateChooser.gif");
		bFechaFin.setImage(image.createImage());
		//bFechaFin.addSelectionListener(this.crearCalendario(txtFin));
		bFechaFin.addSelectionListener(this.crearCalendario(txtFin, txtInicio));
		
		l = new Label(grupoTop, SWT.None);
		l.setText("Estado:");
		txtEstado = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(50,15);
		txtEstado.setLayoutData(gridData);
		txtEstado.setEditable(false);
		
		l = new Label(grupoTop, SWT.NONE);
		l.setText("Paxs:");
		txtPaxs = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(40,15);
		gridData.horizontalSpan = 2;
		txtPaxs.setLayoutData(gridData);
		txtPaxs.setText("0");
		txtPaxs.setTextLimit(3);
		txtPaxs.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoTop, SWT.NONE);
		l.setText("Vendedor:");
		//comboDataVendedor = controller.getComboDataVendedores();
		comboDataVendedor = cdController.getComboDataVendedoresActivos();
		comboVendedor = new Combo(grupoTop, SWT.READ_ONLY);
		comboVendedor.setItems(comboDataVendedor.getTexto());
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		comboVendedor.setLayoutData(gridData);
		comboVendedor.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoTop, SWT.NONE);
		l.setText("Pago:");
		txtPago = new Text(grupoTop, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(45,15);
		txtPago.setLayoutData(gridData);
		txtPago.addModifyListener(this.createModifyListener());
		
// ******************* Grupo de cliente asignado ***************************
		
		Group grupoCliente = new Group(parent, SWT.NONE);
		grupoCliente.setText(" Vendido a ");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		grupoCliente.setLayout(gridLayout);
		gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		grupoCliente.setLayoutData(gridData);
		
		l = new Label(grupoCliente, SWT.NONE);
		l.setText("Código:");
		txtNoCliente = new Text(grupoCliente, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(40, 15);
		gridData.horizontalSpan = 1;
		txtNoCliente.setLayoutData(gridData);
		txtNoCliente.setEnabled(false);
		
		Button bBuscar = new Button(grupoCliente, SWT.PUSH);
		gridData = new GridData(18,18);
		gridData.horizontalSpan = 3;
		bBuscar.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin("manticora", "icons/lupa3.gif");
		bBuscar.setImage(image.createImage());
		bBuscar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de buscar cliente...");
				asignarCliente(getSite().getShell());
			}
		});
		
		l = new Label(grupoCliente, SWT.None);
		l.setText("Nombre:");
		txtNombreCliente = new Text(grupoCliente, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(200,15);
		gridData.horizontalSpan = 2;
		txtNombreCliente.setLayoutData(gridData);
		txtNombreCliente.addModifyListener(this.createModifyListener());
		
// ****************** Grupo que presenta los totales ***********************
		Group grupoTotal = new Group(parent, SWT.NONE);
		grupoTotal.setText(" Totales ");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		grupoTotal.setLayout(gridLayout);
		gridData = new GridData(GridData.END, GridData.FILL, false, true);
		grupoTotal.setLayoutData(gridData);
		
		l = new Label(grupoTotal, SWT.NONE);
		l.setText("SubTotal:");
		l = new Label(grupoTotal, SWT.NONE);
		lSubtotal = new Label(grupoTotal, SWT.NONE);
		lSubtotal.setText("11244.34");
		gridData = new GridData(45,13);
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = SWT.END;
		lSubtotal.setLayoutData(gridData);
		lSubtotal.setAlignment(SWT.RIGHT);
		lSubtotal.pack();     // se computa el tamaño para usarlo abajo (lDescuento)
		
		l = new Label(grupoTotal, SWT.NONE);
		l.setText("Hospedaje (%):");
		txtPorcHospedaje = new Text(grupoTotal, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		txtPorcHospedaje.setText("10");
		gridData = new GridData(13,12);
		txtPorcHospedaje.setLayoutData(gridData);
		txtPorcHospedaje.setEnabled(false);
		txtPorcHospedaje.addModifyListener(this.createModifyListener());
		gridData = new GridData(lSubtotal.getSize().x, lSubtotal.getSize().y);
		gridData.horizontalAlignment = SWT.END;
		lHospedaje = new Label(grupoTotal, SWT.NONE);
		lHospedaje.setLayoutData(gridData);
		lHospedaje.setAlignment(SWT.RIGHT);
		
		l = new Label(grupoTotal, SWT.NONE);
		l.setText("Impuesto (%):");
		txtPorcImpuesto = new Text(grupoTotal, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		txtPorcImpuesto.setText("0");
		txtPorcImpuesto.setTextLimit(2);
		gridData = new GridData(13,12);
		txtPorcImpuesto.setLayoutData(gridData);
		txtPorcImpuesto.addModifyListener(this.createModifyListener());
		gridData = new GridData(lSubtotal.getSize().x, lSubtotal.getSize().y);
		gridData.horizontalAlignment = SWT.END;
		lImpuesto = new Label(grupoTotal, SWT.NONE);
		lImpuesto.setLayoutData(gridData);
		lImpuesto.setAlignment(SWT.RIGHT);
		
		l = new Label(grupoTotal, SWT.NONE);
		l.setText("TOTAL:");
// ponemos el texto en negrita
		FontData fd = l.getFont().getFontData()[0];
		fd.setStyle(SWT.BOLD);
		l.setFont(new Font(getSite().getShell().getDisplay(), fd));
		l = new Label(grupoTotal, SWT.NONE);
		lTotal = new Label(grupoTotal, SWT.NONE);
		lTotal.setText("11402.98");
		gridData = new GridData(lSubtotal.getSize().x, lSubtotal.getSize().y);
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = SWT.END;
		lTotal.setLayoutData(gridData);
		lTotal.setAlignment(SWT.RIGHT);
	}
	
	
	public void calcularTotales() {
		Double subTotal;
		Double hospedaje;
		Double impuesto;
		Double total;
		Double porcHospedaje;
		Double porcImpuesto;
		double tmpMontoLinea;
		double tmpTotal;
		double tmpHospedaje;
		double tmpImpuesto;
		LineaCotizacion tmpLinea;
		
		tmpMontoLinea = 0;
		tmpTotal = 0;
		tmpHospedaje = 0;
		tmpImpuesto = 0;
		for (int n = 0; n < vLineasAct.size(); n++) {
			tmpLinea = (LineaCotizacion) vLineasAct.elementAt(n);
			try {
				tmpMontoLinea = tmpLinea.getPrecio() * tmpLinea.getCantidad() * tmpLinea.getEspacios();
			} catch (Exception e) {
				tmpMontoLinea = -1;
			}
			if (tmpLinea.isHotelAEP() != null && tmpLinea.isHotelAEP()) {
				tmpHospedaje += tmpMontoLinea; 
			} else {
				tmpImpuesto += tmpMontoLinea;
			}
			tmpTotal += tmpMontoLinea;
			//tmpTotal = tmpTotal + ((LineaCotizacion) vLineasAct.elementAt(n)).getPrecio();
		}
		
		subTotal = Double.valueOf(tmpTotal);
		porcHospedaje = Double.parseDouble(txtPorcHospedaje.getText());
		porcImpuesto = Double.parseDouble(txtPorcImpuesto.getText());
		
		hospedaje = tmpHospedaje * porcHospedaje / 100;
		impuesto = tmpImpuesto * porcImpuesto / 100;
		total = subTotal + hospedaje + impuesto;
		
		lSubtotal.setText(new DecimalFormat("0.00").format(subTotal));
		lHospedaje.setText(new DecimalFormat("0.00").format(hospedaje));
		lImpuesto.setText(new DecimalFormat("0.00").format(impuesto));
		lTotal.setText(new DecimalFormat("0.00").format(total));
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
		viewerAct.setInput(vLineasAct);
		viewerAct.setSorter(new GenericSorter(viewerAct));
		
		//registramos al viewer como un selection provider para determinar
		//qué elemento está seleccionado
		getSite().setSelectionProvider(viewerAct);

		Composite botones = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(4, false);
		botones.setLayout(gridLayout);
		
		Button bAgregar = new Button(botones, SWT.PUSH);
		GridData gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bAgregar.setLayoutData(gridData);
		bAgregar.setText("Agregar");
		bAgregar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de agregar...");
				LineaCotizacion lc = getActividadDefault();
				if (lc != null) {
					String fechaDefault = FechaUtil.toString(lc.getFecha());
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
				editarActividad(getSite().getShell(), getActividadSeleccionada());
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
				vLineasAct.remove(getActividadSeleccionada());
				viewerAct.refresh();
				addDirtyFlag();   // como borramos, indicamos que se ha modificado la cotización
			}
		});	
		
		Button bTemplate = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bTemplate.setLayoutData(gridData);
		bTemplate.setText("Templates");
		bTemplate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de importar templates...");
				ImportarTemplate dialogo = new ImportarTemplate(getSite().getShell(), txtInicio.getText(), txtPaxs.getText());
				dialogo.setText("Importar template");
				MyInputDialogData data = dialogo.open();
				if (data.isButtonResponse()) {
					System.out.println("Template seleccionado: " + data.getCodigoTemplate());
					// importar actividades del template seleccionado
					Date fechaBase = FechaUtil.toDate(data.getFechaBase());
					Integer noPaxs = txt2Integer(data.getNoPaxs());
					importarActividadesFromTemplate(Long.valueOf(data.getCodigoTemplate()), fechaBase, noPaxs, data.getTipoPrecio());
					addDirtyFlag();
				} else {
					MessageDialog.openInformation(getSite().getShell(), "Información", "La acción ha sido cancelada.");
				}
				viewerAct.refresh();
			}
		});
	}
	
	
	private void importarActividadesFromTemplate(Long idTemplate, Date fechaBase,
			Integer noPaxs, String tipoPrecio) {
		Set<LineaCotizacion> listaActividades = editorController.generarLineasFromTemplate(registro, idTemplate, fechaBase, noPaxs, tipoPrecio);
		vLineasAct.addAll(listaActividades);
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
		viewerPaxs.setInput(vLineasPax);
		
		Composite botones = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(3, false);
		botones.setLayout(gridLayout);
		
		Button bAgregar = new Button(botones, SWT.PUSH);
		GridData gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bAgregar.setLayoutData(gridData);
		bAgregar.setText("Agregar");
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
		
		Button bBorrar = new Button(botones, SWT.PUSH);
		gridData = new GridData(80,20);
		gridData.horizontalAlignment = SWT.END;
		bBorrar.setLayoutData(gridData);
		bBorrar.setText("Borrar");
		bBorrar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de borrar...");
				//viewerPaxs.remove(getPaxSeleccionado());  //Solo remueve la línea del viewer, pero en el vector sigue, así que reaparece luego de agregar otro elemento
				vLineasPax.remove(getPaxSeleccionado());
				viewerPaxs.refresh();
				addDirtyFlag();
			}
		});
	}
	
	private Table crearTablaActividades(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tablaAct = new Table(parent, style);
		tablaAct.setLinesVisible(true);
		tablaAct.setHeaderVisible(true);
		GridData gridData3 = new GridData(SWT.FILL, SWT.FILL, true, true);
		tablaAct.setLayoutData(gridData3);
		
		TableColumn column = new TableColumn(tablaAct, SWT.LEFT, 0);
		column.setText("Fecha");
		column.setWidth(70);
		
		column = new TableColumn(tablaAct, SWT.RIGHT, 1);
		column.setText("Qty");
		column.setWidth(35);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 2);
		column.setText("Descripción");
		column.setWidth(195);
		
		column = new TableColumn(tablaAct, SWT.LEFT, 3);
		column.setText("Comentarios");
		column.setWidth(195);
		
		column = new TableColumn(tablaAct, SWT.RIGHT, 4);
		column.setText("Venta");
		column.setWidth(65);
		
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
		column.setText("");
		column.setWidth(20);
		column.setAlignment(SWT.CENTER);
		
		column = new TableColumn(tablaPaxs, SWT.CENTER, 3);
		column.setText("Pasaporte");
		column.setWidth(80);
		column.setAlignment(SWT.LEFT);
		
		column = new TableColumn(tablaPaxs, SWT.CENTER, 4);
		column.setText("País");
		column.setWidth(90);
		column.setAlignment(SWT.LEFT);
		
		return tablaPaxs;
	}
	
	class ViewContentProviderAct implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = vLineasAct.toArray(new LineaCotizacion[vLineasAct.size()]);
			return resultados;
		}
	}
	
	
	class ViewLabelProviderAct extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			LineaCotizacion linea = (LineaCotizacion) obj;
			switch (index) {
			case 0:
				resultado = FechaUtil.toString(linea.getFecha());
				break;
			case 1:
				resultado = valor2Txt(linea.getCantidad());
				break;
			case 2:				
				resultado = valor2Txt(linea.getDspProducto());
				break;
			case 3:
				resultado = valor2Txt(linea.getComentario());
				break;
			case 4:
				try {
					Float t = linea.getPrecio() * linea.getCantidad() * linea.getEspacios();
					resultado = valor2Txt(t, "#,##0.00");
				} catch (Exception e) {
					resultado = "Error";
				}
				break;
			}
			return resultado;
			
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}
	
	
	class ViewContentProviderPax implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = vLineasPax.toArray(new Pax[vLineasPax.size()]);
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
			case 3:
				resultado = linea.getIdentificacion();
				break;
			case 4:
				resultado = linea.getDspPais();
				break;
			}
			return resultado;
			
		}
		public Image getColumnImage(Object obj, int index) {
			String imageKey = IImageKeys.CHECK;
			ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
			Pax linea = (Pax) obj;
			switch(index) {
			case 2:
				if (!linea.getCondiciones().equals("")) return image.createImage();
			}
			return null;		
		}
	}
	
	
	private void agregarActividad(Shell shell, String fechaDefault) {
		// cuando agregamos actividades, lineas es usada para guardar la nueva actividad
		// (inicialmente está null)
		Set<LineaCotizacion> lineas = new HashSet<LineaCotizacion>();
		AgregarActividadCotizacion dialogo = new AgregarActividadCotizacion(shell, lineas);
		dialogo.setFechaDefault(fechaDefault);
		dialogo.setEspaciosDefault(txtPaxs.getText());
		
		int respuesta = dialogo.open();
		System.out.println("Respuesta: " + respuesta + ", cantidad: " + lineas.size());
		if (respuesta == IDialogConstants.OK_ID) {
			vLineasAct.addAll(lineas);
			viewerAct.refresh();
			addDirtyFlag();
		}
	}
	
	private void editarActividad(Shell shell, LineaCotizacion linea) {
		Set<LineaCotizacion> lineas = new HashSet<LineaCotizacion>();
		lineas.add(linea);
		
		AgregarActividadCotizacion dialogo = new AgregarActividadCotizacion(shell, lineas);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			viewerAct.refresh();
			addDirtyFlag();
		}
	}
	
	private void agregarPax(Shell shell) {
		Pax linea = new Pax();
		AgregarPaxCotizacion dialogo = new AgregarPaxCotizacion(shell, linea);
		if (dialogo.open() == 0) {
			vLineasPax.add(linea);
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
	
	public LineaCotizacion getActividadSeleccionada() {
		Object seleccion = ((IStructuredSelection) viewerAct.getSelection()).getFirstElement();
		LineaCotizacion actividad = (LineaCotizacion) seleccion;
		return actividad;
	}
	
	public LineaCotizacion getActividadDefault() {
		LineaCotizacion linea = getActividadSeleccionada();
		if (linea == null) {
			int n = viewerAct.getTable().getItemCount();
			if (n > 0) {
				linea = (LineaCotizacion) viewerAct.getElementAt(n - 1);
			}
		}
		return linea;
	}
	
	public Pax getPaxSeleccionado() {
		Object seleccion = ((IStructuredSelection) viewerPaxs.getSelection()).getFirstElement();
		Pax linea = (Pax) seleccion;
		return linea;
	}
	
	private void asignarCliente(Shell shell) {
		//ICliente cliente = null;
		// para recuperar el cliente debo utilizar una lista, ya que si paso
		// un objeto cliente, se pierde en BuscarClientesDialog
		List<Cliente> seleccion = new ArrayList<Cliente>();
		BuscarClientesDialog dialogo = new BuscarClientesDialog(shell, seleccion);
		if (dialogo.open() == IDialogConstants.OK_ID) {
			//MessageDialog.openInformation(getSite().getShell(), "Información", "Se importan las actividades del template seleccionado.");
			cliente = seleccion.get(0);
			System.out.println("Nombre de cliente3: " + cliente.getNombreCliente());
			//registro.setCliente(cliente);
			txtNoCliente.setText(valor2Txt(cliente.getIdCliente()));
			txtNombreCliente.setText((cliente.getNombreCliente()));
			txtNombreCliente.setEnabled(false);
			addDirtyFlag();
		}
	}
	
	public void setTxtEstado(String status) {
		txtEstado.setText(status);
	}
	
	public void setFechaFinalizacion(Date fecha) {
		this.fechaFinalizacion = fecha;
	}

	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}
}
