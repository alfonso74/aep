package rcp.manticora.editors;

import java.util.Date;



import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.SolicitudController;
import rcp.manticora.model.Solicitud;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.views.SolicitudesView;

public class SolicitudesEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.solicitudes";
	private final String pluginId = Application.PLUGIN_ID;
	
	private Text txtNumero;
	private Text txtEstado;
	private Text txtVendedor;
	
	private Text txtNombre;
	private Text txtApellido;
	private Text txtTelefono;
	private Text txtCelular;
	private Text txtEmail;
	private Combo comboPais;
	
	private Text txtPrograma;
	private Text txtFechaIni;
	private Text txtFechaFin;
	private Text txtComentario;
	
	private Date fechaAsignacion = null;
	private Date fechaFinalizacion = null;
	
	private Solicitud registro;
	private ComboDataController cdController;
	private SolicitudController editorController;
	private ComboData comboKeyword;
	private ComboData cdPaises;
	private ComboData cdVendedores;
	
	private ImageDescriptor image;   // imagen de calendario
	
	public String getComentario() {
		return txtComentario.getText();
	}

	public SolicitudesEditor() {
		super();
		editorController = new SolicitudController(ID);
		cdController = new ComboDataController();
		comboKeyword = cdController.getComboDataKeyword(TipoKeyword.STATUS_SOLICITUD);
		cdPaises = cdController.getComboDataPaises();
		cdVendedores = cdController.getComboDataVendedores();
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
	}

	public void doSave(IProgressMonitor monitor) {
		String pVendedor = txtVendedor.getText();
		String pNombre = txtNombre.getText();
		String pApellido = txtApellido.getText();
		String pTelefono = txtTelefono.getText();
		String pCelular = txtCelular.getText();
		String pPrograma = txtPrograma.getText();
		String pEmail = txtEmail.getText();
		Long pIdPais = cdPaises.getKeyAsLongByIndex(comboPais.getSelectionIndex());
		Date pFechaInicio = FechaUtil.toDate(txtFechaIni.getText());
		Date pFechaFin = FechaUtil.toDate(txtFechaFin.getText());
		String pComentario = txtComentario.getText();
		String pEstado = comboKeyword.getCodeByName(txtEstado.getText());
		Date pFechaAsignacion = fechaAsignacion;
		Date pFechaFinalizacion = fechaFinalizacion;
		
		if (!pVendedor.equals("No asignado")) {
			registro.setIdVendedor(cdVendedores.getKeyAsLongByTexto(pVendedor));
		}
		registro.setNombre(pNombre);
		registro.setApellido(pApellido);
		registro.setTelefono(pTelefono);
		registro.setCelular(pCelular);
		registro.setEmail(pEmail);
		registro.setIdPais(pIdPais);
		registro.setFechaInicio(pFechaInicio);
		registro.setFechaFin(pFechaFin);
		registro.setPrograma(pPrograma);
		registro.setComentario(pComentario);
		registro.setEstado(pEstado);
		registro.setFechaAsignacion(pFechaAsignacion);
		registro.setFechaFinalizacion(pFechaFinalizacion);
		editorController.doSave(registro);
		
		if (isNewDoc) {
			txtNumero.setText(valor2Txt(registro.getIdSolicitud()));
			isNewDoc = false;
		};
		this.setPartName(registro.getTituloDocumento());
		getEditorInput().setName(registro.getTituloDocumento());
		actualizarVista(SolicitudesView.ID);
		removeDirtyFlag();
	}

	protected void agregarControles(Composite parent) {
		Label l;
		GridLayout gridLayout;
		GridData gridData;
		
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginTop = 0;
		gridLayout.marginLeft = 5;
		parent.setLayout(gridLayout);
		
		Composite header = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		gridData = new GridData();
		//gridData.horizontalSpan = 1;
		header.setLayout(gridLayout);
		header.setLayoutData(gridData);
		
		Composite headerRight = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		gridData = new GridData();
		//gridData.horizontalSpan = 1;
		//gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.RIGHT;
		gridData.verticalAlignment = SWT.TOP;
		headerRight.setLayout(gridLayout);
		headerRight.setLayoutData(gridData);
		
		l = new Label(header, SWT.NONE);
		l.setText("No. de solicitud:");
		txtNumero = new Text(header, SWT.SINGLE | SWT.BORDER);
		txtNumero.setLayoutData(new GridData(40,15));
		txtNumero.setEditable(false);
		
		l = new Label(headerRight, SWT.NONE);
		l.setText("Estado:");
		txtEstado = new Text(headerRight, SWT.SINGLE | SWT.BORDER);
		txtEstado.setLayoutData(new GridData(80,15));
		txtEstado.setEditable(false);
		
		l = new Label(header, SWT.NONE);
		l.setText("Vendedor:");
		txtVendedor = new Text(header, SWT.SINGLE | SWT.BORDER);
		txtVendedor.setLayoutData(new GridData(120,15));
		txtVendedor.setEditable(false);
		txtVendedor.addModifyListener(this.createModifyListener());
		
		crearGrupoCliente(parent);
		
		crearGrupoTour(parent);
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nueva solicitud...");
			registro = new Solicitud();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
			txtEstado.setText("Nueva");
		} else {
			System.out.println("Ejecutando código para cargar datos...");
			registro = (Solicitud) ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtNumero.setText(valor2Txt(registro.getIdSolicitud()));
			txtEstado.setText(comboKeyword.getTextoByKey(registro.getEstado()));
			txtVendedor.setText(valor2Txt(registro.getDspVendedor()));
			txtNombre.setText(valor2Txt(registro.getNombre()));
			txtApellido.setText(valor2Txt(registro.getApellido()));
			txtTelefono.setText(valor2Txt(registro.getTelefono()));
			txtCelular.setText(valor2Txt(registro.getCelular()));
			txtEmail.setText(valor2Txt(registro.getEmail()));
			comboPais.select(comboPais.indexOf(registro.getDspPais()));
			txtFechaIni.setText(FechaUtil.toString(registro.getFechaInicio()));
			txtFechaFin.setText(FechaUtil.toString(registro.getFechaFin()));
			txtPrograma.setText(valor2Txt(registro.getPrograma()));
			txtComentario.setText(valor2Txt(registro.getComentario()));
			fechaAsignacion = registro.getFechaAsignacion();
			fechaFinalizacion = registro.getFechaFinalizacion();
		}
		addFilledFlag();
		setFocoInicial(txtNombre);
	}
	
	private void crearGrupoCliente(Composite parent) {
		GridLayout gridLayout;
		GridData gridData;
		Label l;
		
		Group grupoCliente = new Group(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.horizontalSpacing = 10;
		//gridData = new GridData(425,85);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.horizontalSpan = 2;
		grupoCliente.setLayout(gridLayout);
		//grupoCliente.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		grupoCliente.setLayoutData(gridData);
		//grupoCliente.setText("Generales del cliente");
		
		l = new Label(grupoCliente, SWT.NONE);
		l.setText("Nombre:");
		txtNombre = new Text(grupoCliente, SWT.SINGLE | SWT.BORDER);
		txtNombre.setLayoutData(new GridData(130, 15));
		txtNombre.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoCliente, SWT.NONE);
		l.setText("Apellido:");
		txtApellido = new Text(grupoCliente, SWT.SINGLE | SWT.BORDER);
		txtApellido.setLayoutData(new GridData(130, 15));
		txtApellido.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoCliente, SWT.NONE);
		l.setText("Teléfono:");
		txtTelefono = new Text(grupoCliente, SWT.SINGLE | SWT.BORDER);
		txtTelefono.setLayoutData(new GridData(80, 15));
		txtTelefono.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoCliente, SWT.NONE);
		l.setText("Teléfono2:");
		txtCelular = new Text(grupoCliente, SWT.SINGLE | SWT.BORDER);
		txtCelular.setLayoutData(new GridData(80, 15));
		txtCelular.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoCliente, SWT.NONE);
		l.setText("Email:");
		txtEmail = new Text(grupoCliente, SWT.SINGLE | SWT.BORDER);
		txtEmail.setLayoutData(new GridData(150, 15));
		txtEmail.addModifyListener(this.createModifyListener());
		
		l = new Label(grupoCliente, SWT.NONE);
		l.setText("País:");
		comboPais = new Combo(grupoCliente, SWT.READ_ONLY);
		comboPais.setItems(cdPaises.getTexto());
		comboPais.setLayoutData(new GridData(80,15));
		comboPais.addModifyListener(this.createModifyListener());
	}
	
	private void crearGrupoTour(Composite parent) {
		GridLayout gridLayout;
		GridData gridData;
		Label l;
		
		Group grupoTour = new Group(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		gridLayout.horizontalSpacing = 10;
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.horizontalSpan = 2;
		grupoTour.setLayout(gridLayout);
		grupoTour.setLayoutData(gridData);
		//grupoTour.setText("Información de la gira");
		
		l = new Label(grupoTour, SWT.NONE);
		l.setText("Interesado en:");
		txtPrograma = new Text(grupoTour, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(220,15);
		gridData.horizontalSpan = 5;
		txtPrograma.setLayoutData(gridData);
		txtPrograma.addModifyListener(this.createModifyListener());

		l = new Label(grupoTour, SWT.NONE);
		l.setText("Fecha inicio:");
		txtFechaIni = new Text(grupoTour, SWT.SINGLE | SWT.BORDER);
		txtFechaIni.setLayoutData(new GridData(60, 15));
		txtFechaIni.addModifyListener(this.createModifyListener());
		
		Button bFechaIni = new Button(grupoTour, SWT.NONE);
		bFechaIni.setLayoutData(new GridData(16,16));
		bFechaIni.setImage(image.createImage());
		bFechaIni.addSelectionListener(this.crearCalendario(txtFechaIni));
		
		l = new Label(grupoTour, SWT.NONE);
		l.setText("Fecha fin:");
		txtFechaFin = new Text(grupoTour, SWT.SINGLE | SWT.BORDER);
		txtFechaFin.setLayoutData(new GridData(60, 15));
		txtFechaFin.addModifyListener(this.createModifyListener());
		
		Button bFechaFin = new Button(grupoTour, SWT.NONE);
		bFechaFin.setLayoutData(new GridData(16,16));
		bFechaFin.setImage(image.createImage());
		bFechaFin.addSelectionListener(this.crearCalendario(txtFechaFin));
		
		l = new Label(grupoTour, SWT.NONE);
		l.setText("Comentario:");
		txtComentario = new Text(grupoTour, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 5;
		// sin el verticalSpan el label de comentarios queda en el medio
		gridData.verticalSpan = 2;
		gridData.heightHint = 40;
		txtComentario.setLayoutData(gridData);
		txtComentario.addModifyListener(this.createModifyListener());
	}
	
	public void setTxtVendedor(String vendedor) {
		txtVendedor.setText(vendedor);
	}
	
	public void setTxtEstado(String status) {
		txtEstado.setText(status);
	}
	
	public void setFechaAsignacion(Date fecha) {
		this.fechaAsignacion = fecha;
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

