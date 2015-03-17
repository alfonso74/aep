package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ClientesController;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.Cliente;
import rcp.manticora.model.ClienteJuridico;
import rcp.manticora.model.Condicional;
import rcp.manticora.model.TipoCliente;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.ClientesComView;

public class ClientesComEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.clientesCom";
	private Text txtCodigo;
	private Combo comboTipoCliente;
	private Text txtNombre;
	private Combo comboComision;
	private Text txtIdentificacion;
	private Text txtReferencia;
	private Text txtContacto;
	private Text txtTelefono;
	private Text txtEmail;
	private Combo comboPais;
	private Text txtDireccion1;
	private Text txtDireccion2;
	private Text txtDireccion3;
	
	private ComboData cdTipoCliente;
	private ComboData cdComision;
	private ComboData cdPais;
	
	private Cliente registro;
	private ClientesController editorController;
	private ComboDataController cdController;
	private Label label;
	private GridData gridData_1;
	private Label l_1;
	private Label l_2;
	private Label l_3;
	private GridData gridData_2;

	public ClientesComEditor() {
		super();
		editorController = new ClientesController(ID);
		cdController = new ComboDataController();
		cdTipoCliente = cdController.getComboDataTipoClientes();
		cdComision = cdController.getComboDataCondicional();
		cdPais = cdController.getComboDataPaises();
	}
	
	public void doSave(IProgressMonitor monitor) {
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		String pClase = "C";
		TipoCliente pTipo = (TipoCliente) cdTipoCliente.getObjectByIndex(comboTipoCliente.getSelectionIndex());
		System.out.println("Tipo de cliente: " + pTipo);
		String pNombre = txtNombre.getText().trim();
		String pComision = cdComision.getCodeByIndex(comboComision.getSelectionIndex());
		String pIdentificacion = txtIdentificacion.getText().trim();
		String pReferencia = txtReferencia.getText().trim();
		String pContacto = txtContacto.getText().trim();
		String pTelefono = txtTelefono.getText().trim();
		String pTelefono2 = "";
		String pEmail = txtEmail.getText().trim();
		Long pIdPais = cdPais.getKeyAsLongByIndex(comboPais.getSelectionIndex());
		System.out.println("Valor de país: " + pIdPais);
		String pDireccion1 = txtDireccion1.getText().trim();
		String pDireccion2 = txtDireccion2.getText().trim();
		String pDireccion3 = txtDireccion3.getText().trim();
		String pApartado = "";
		String pCiudad = "";
		String pComentario = "";
		String pEstado = "A";              // no hay campo de status en el editor
		
		registro.setClase(pClase);
		registro.setTipo(pTipo);
		registro.setIdentificacion(pIdentificacion);
		registro.setTelefono(pTelefono);
		registro.setTelefono2(pTelefono2);
		registro.setEmail(pEmail);
		registro.setIdPais(pIdPais);
		registro.setDireccion1(pDireccion1);
		registro.setDireccion2(pDireccion2);
		registro.setDireccion3(pDireccion3);
		registro.setApartado(pApartado);
		registro.setCiudad(pCiudad);
		registro.setComentario(pComentario);
		registro.setComision(Boolean.parseBoolean(pComision));
		registro.setEstado(pEstado);
		if (registro instanceof ClienteJuridico) {
			((ClienteJuridico) registro).setNombreCia(pNombre);
			((ClienteJuridico) registro).setReferencia(pReferencia);
			((ClienteJuridico) registro).setContacto(pContacto);
		}
		
		editorController.doSave(registro);
		if (isNewDoc) {
			System.out.println("Nuevo cliente comercial creado");
			txtCodigo.setText(valor2Txt(registro.getIdCliente()));
			isNewDoc = false;
		} else {
			System.out.println("Cliente comercial actualizado");
		}
		
		this.setPartName(registro.getTituloDocumento());
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		// unificar para clientes
		actualizarVista(ClientesComView.ID);
		removeDirtyFlag();
	}
	
	public boolean validarSave() {
		String pNombre = txtNombre.getText();
		String pContacto = txtContacto.getText();
		
		if (comboTipoCliente.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Tipo cliente\" no puede quedar en blanco");
			return false;
		}
		if (pNombre.length() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El campo de \"Nombre\" no puede quedar en blanco.");
			return false;
		}
		if (pNombre.length() > 50) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre del cliente no puede superar los 50 caracteres (" + pNombre.length() + ").");
			return false;
		}
		if (pContacto.length() > 50) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre del contacto no puede superar los 50 caracteres (" + pContacto.length() + ").");
			return false;
		}
		if (comboPais.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"País\" no puede quedar en blanco");
			return false;
		}
		return true;
	}

	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.marginLeft = 10;
		gridLayout.numColumns = 5;
		parent.setLayout(gridLayout);
		
		agregarControles(parent);
		llenarControles();
	}
	
	protected void agregarControles(Composite parent) {
		Label l;
		GridData gridData;
		GridLayout gridLayout;
		
		l = new Label(parent, SWT.NONE);
		l.setText("Código:");
		txtCodigo = new Text(parent, SWT.BORDER);
		gridData = new GridData(40,15);
		gridData.horizontalSpan = 1;
		txtCodigo.setLayoutData(gridData);
		txtCodigo.setEditable(false);
		
		l_2 = new Label(parent, SWT.NONE);
		GridData gd_l_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_l_2.horizontalIndent = 20;
		l_2.setLayoutData(gd_l_2);
		l_2.setText("Tipo cliente:");
		comboTipoCliente = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		comboTipoCliente.setLayoutData(gridData);
		comboTipoCliente.setItems(cdTipoCliente.getTexto());
		comboTipoCliente.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Nombre:");
		txtNombre = new Text(parent, SWT.BORDER);
		gridData_1 = new GridData(180,15);
		gridData_1.horizontalAlignment = SWT.LEFT;
		gridData_1.minimumWidth = 200;
		gridData_1.horizontalSpan = 1;
		txtNombre.setLayoutData(gridData_1);
		txtNombre.addModifyListener(this.createModifyListener());
		
		Label lblComision = new Label(parent, SWT.NONE);
		GridData gd_lblComision = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblComision.horizontalIndent = 20;
		lblComision.setLayoutData(gd_lblComision);
		lblComision.setText("Comisi\u00F3n:");
		comboComision = new Combo(parent, SWT.READ_ONLY);
		comboComision.setItems(cdComision.getTexto());
		comboComision.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		comboComision.setText(Condicional.NO.getDescripcion());
		comboComision.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Referencia:");
		txtReferencia = new Text(parent, SWT.BORDER);
		gridData_2 = new GridData(100,15);
		gridData_2.horizontalSpan = 1;
		txtReferencia.setLayoutData(gridData_2);
		txtReferencia.setTextLimit(20);
		txtReferencia.addModifyListener(this.createModifyListener());
		
		l_1 = new Label(parent, SWT.NONE);
		GridData gd_l_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_l_1.horizontalIndent = 20;
		l_1.setLayoutData(gd_l_1);
		l_1.setText("No. de registro:");
		txtIdentificacion = new Text(parent, SWT.BORDER);
		gridData = new GridData(80,15);
		gridData.horizontalSpan = 2;
		txtIdentificacion.setLayoutData(gridData);
		txtIdentificacion.setTextLimit(20);
		txtIdentificacion.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Contacto:");
		txtContacto = new Text(parent, SWT.BORDER);
		gridData = new GridData(180,15);
		gridData.horizontalSpan = 4;
		txtContacto.setLayoutData(gridData);
		txtContacto.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Teléfono:");
		txtTelefono = new Text(parent, SWT.BORDER);
		txtTelefono.setLayoutData(new GridData(90,15));
		txtTelefono.setTextLimit(15);
		txtTelefono.addModifyListener(this.createModifyListener());
		
		l_3 = new Label(parent, SWT.NONE);
		GridData gd_l_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_l_3.horizontalIndent = 20;
		l_3.setLayoutData(gd_l_3);
		l_3.setText("Email:");
		txtEmail = new Text(parent, SWT.BORDER);
		gridData = new GridData(150,15);
		gridData.horizontalSpan = 2;
		txtEmail.setLayoutData(gridData);
		txtEmail.setTextLimit(35);
		txtEmail.addModifyListener(this.createModifyListener());
		
		label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gd_label.heightHint = 15;
		label.setLayoutData(gd_label);
		
		l = new Label(parent, SWT.NONE);
		l.setText("País:");
		comboPais = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 1;
		comboPais.setLayoutData(gridData);
		comboPais.setItems(cdPais.getTexto());
		comboPais.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("");
		gridData = new GridData(100,15);
		gridData.horizontalSpan = 3;
		l.setLayoutData(gridData);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Dirección:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		gridData.verticalIndent = 7;
		l.setLayoutData(gridData);
		Composite direcciones = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		direcciones.setLayout(gridLayout);
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		direcciones.setLayoutData(gridData);
		txtDireccion1 = new Text(direcciones, SWT.BORDER);
		gridData = new GridData(250,15);
		gridData.horizontalSpan = 4;
		txtDireccion1.setLayoutData(gridData);
		txtDireccion1.setTextLimit(40);
		txtDireccion1.addModifyListener(this.createModifyListener());
		txtDireccion2 = new Text(direcciones, SWT.BORDER);
		gridData = new GridData(250,15);
		gridData.horizontalSpan = 4;
		txtDireccion2.setLayoutData(gridData);
		txtDireccion2.setTextLimit(40);
		txtDireccion2.addModifyListener(this.createModifyListener());
		txtDireccion3 = new Text(direcciones, SWT.BORDER);
		gridData = new GridData(250,15);
		gridData.horizontalSpan = 4;
		txtDireccion3.setLayoutData(gridData);
		txtDireccion3.setTextLimit(40);
		txtDireccion3.addModifyListener(this.createModifyListener());
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo cliente");
			registro = new ClienteJuridico();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			System.out.println("Ejecutando código para cargar datos...");
			registro = (Cliente) ((CommonEditorInput) this.getEditorInput()).getElemento();
			System.out.println("Pais: " + registro.getDspPais());
			txtCodigo.setText(valor2Txt(registro.getIdCliente()));
			
			System.out.println("Tipo: " + registro.getTipo());
			if (registro.getTipo() != null) {
				comboTipoCliente.setText(registro.getTipo().getDescripcion());
				System.out.println("Selection index: " + comboTipoCliente.getSelectionIndex());
				if (comboTipoCliente.getSelectionIndex() == -1) {
					String dspTipoCliente = registro.getTipo().getDescripcion() + " (Inactivo)";
					cdTipoCliente.agregarItem(dspTipoCliente, registro.getTipo().getIdTipo(), registro.getTipo());
					comboTipoCliente.add(dspTipoCliente);
					comboTipoCliente.select(comboTipoCliente.indexOf(dspTipoCliente));
				}
			}
			
			comboComision.setText(cdComision.getTextoByKey(Boolean.toString(registro.getComision())));
			
			txtIdentificacion.setText((valor2Txt(registro.getIdentificacion())));
			txtTelefono.setText(valor2Txt(registro.getTelefono()));
			txtEmail.setText(valor2Txt(registro.getEmail()));
			comboPais.select(comboPais.indexOf(registro.getDspPais()));
			txtDireccion1.setText(valor2Txt(registro.getDireccion1()));
			txtDireccion2.setText(valor2Txt(registro.getDireccion2()));
			txtDireccion3.setText(valor2Txt(registro.getDireccion3()));
			if (registro instanceof ClienteJuridico) {
				String nombre = valor2Txt(((ClienteJuridico) registro).getNombreCia());
				String referencia = valor2Txt(((ClienteJuridico) registro).getReferencia());
				String contacto = valor2Txt(((ClienteJuridico) registro).getContacto());
				txtNombre.setText(nombre);
				txtReferencia.setText(referencia);
				txtContacto.setText(contacto);
			}
		}
		addFilledFlag();
		setFocoInicial(txtNombre);
	}

	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}
}

