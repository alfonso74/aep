package rcp.manticora.editors;

import java.util.Date;



import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.controllers.ClientesController;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.Cliente;
import rcp.manticora.model.ClienteNatural;
import rcp.manticora.model.Condicional;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.views.ClientesView;

public class ClientesEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.clientes";
	private final String pluginId = Application.PLUGIN_ID;
	
	private Text txtCodigo;
	private Combo comboTipoCliente;
	private Combo comboFuente;
	private Combo comboComision;
	private Text txtNombre;
	private Text txtApellido;
	private Text txtIdentificacion;
	private Text txtNacimiento;
	private Text txtPeso;
	private Combo comboSexo;
	private Text txtTelefono;
	private Text txtEmail;
	private Combo comboPago;
	private Text txtComentario;
	
	private Combo comboPais;
	private Text txtCiudad;
	private Text txtApartado;
	private Text txtDireccion1;
	private Text txtDireccion2;
	private Text txtDireccion3;
	
	private Button bFecha;
	private ImageDescriptor image;
	
	private ComboData cdComision;
	private ComboData cdPais;
	private ComboData cdTipoCliente;
	private ComboData cdSexo;
	private ComboData cdFormaPago;
	
	private Cliente registro;
	private ClientesController editorController;
	private ComboDataController cdController;

	public ClientesEditor() {
		super();
		editorController = new ClientesController(ID);
		cdController = new ComboDataController();
		cdComision = cdController.getComboDataCondicional();
		cdPais = cdController.getComboDataPaises();
		cdTipoCliente = cdController.getComboDataTipoClientes();
		cdSexo = cdController.getComboDataKeyword(TipoKeyword.SEXO);
		cdFormaPago = cdController.getComboDataKeyword(TipoKeyword.FORMA_PAGO);
	}
	
	public void doSave(IProgressMonitor monitor) {
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		
		String pClase = "N";
		Long pIdTipo = cdTipoCliente.getKeyAsLongByIndex(comboTipoCliente.getSelectionIndex());
		System.out.println("Tipo de cliente: " + pIdTipo);
		String pFuente = comboFuente.getText();
		String pComision = cdComision.getCodeByIndex(comboComision.getSelectionIndex());
		String pNombre = txtNombre.getText().trim();
		String pApellido = txtApellido.getText().trim();
		String pIdentificacion = txtIdentificacion.getText().trim();
		Date pNacimiento = FechaUtil.toDate(txtNacimiento.getText());
		//int pPeso = Integer.parseInt(txtPeso.getText());
		Integer pPeso = txt2Integer(txtPeso.getText());
		//String pSexo = comboSexo.getText().substring(1);
		String pSexo = cdSexo.getCodeByIndex(comboSexo.getSelectionIndex());
		System.out.println("Sexo: " + pSexo);
		String pTelefono = txtTelefono.getText().trim();
		String pTelefono2 = "2222222";
		String pEmail = txtEmail.getText().trim();
		//String pFormaPago = comboPago.getText();
		String pFormaPago = cdFormaPago.getCodeByIndex(comboPago.getSelectionIndex());
		String pComentario = txtComentario.getText();
		Long pIdPais = cdPais.getKeyAsLongByIndex(comboPais.getSelectionIndex());
		System.out.println("Valor de país: " + pIdPais);
		String pCiudad = txtCiudad.getText().trim();
		String pApartado = txtApartado.getText().trim();
		String pDireccion1 = txtDireccion1.getText().trim();
		String pDireccion2 = txtDireccion2.getText().trim();
		String pDireccion3 = txtDireccion3.getText().trim();
		registro.setComision(Boolean.parseBoolean(pComision));
		String pEstado = "A";            // no hay campo de status en el editor
		
		/*
		if (isNewDoc) {
			System.out.println("Acción de salvar cliente");
			System.out.println("Pars: 0" + pNombre + pApellido + pEmail + pTelefono + pTelefono2);
			registro = controller.addCliente(-1, pNombre, pApellido, pIdentificacion,
				pTelefono, pTelefono2, pEmail, pIdPais, pDireccion1, pDireccion2,
				pDireccion3, pApartado, pCiudad, pComentario, pEstado);
			registro.setFuente(pFuente);
			registro.setFormaPago(pFormaPago);
			registro.setClase(pClase);
			registro.setIdTipo(pIdTipo);
			if (registro instanceof ClienteNatural) {
				((ClienteNatural) registro).setFechaNacimiento(pNacimiento);
				((ClienteNatural) registro).setSexo(pSexo);
				((ClienteNatural) registro).setPeso(pPeso);
			};
			controller.update(registro);
			int idCliente = registro.getIdCliente();
			txtCodigo.setText(Integer.toString(idCliente));
			isNewDoc = false;
		} else {
			System.out.println("Actualizando cliente...");
			registro.setClase(pClase);
			registro.setIdTipo(pIdTipo);
			registro.setFuente(pFuente);
			registro.setIdentificacion(pIdentificacion);
			registro.setTelefono(pTelefono);
			registro.setTelefono2(pTelefono2);
			registro.setEmail(pEmail);
			registro.setFormaPago(pFormaPago);
			System.out.println("Peso a grabar: " + pPeso);
			registro.setIdPais(pIdPais);
			registro.setCiudad(pCiudad);
			registro.setApartado(pApartado);
			registro.setDireccion1(pDireccion1);
			registro.setDireccion2(pDireccion2);
			registro.setDireccion3(pDireccion3);
			registro.setComentario(pComentario);
			if (registro instanceof ClienteNatural) {
				((ClienteNatural) registro).setNombre(pNombre);
				((ClienteNatural) registro).setApellido(pApellido);
				((ClienteNatural) registro).setFechaNacimiento(pNacimiento);
				((ClienteNatural) registro).setSexo(pSexo);
				((ClienteNatural) registro).setPeso(pPeso);
			}
			controller.update(registro);
		}
		*/
		
		registro.setClase(pClase);
		registro.setIdTipo(pIdTipo);
		registro.setFuente(pFuente);
		registro.setIdentificacion(pIdentificacion);
		registro.setTelefono(pTelefono);
		registro.setTelefono2(pTelefono2);
		registro.setEmail(pEmail);
		registro.setFormaPago(pFormaPago);
		System.out.println("Peso a grabar: " + pPeso);
		registro.setIdPais(pIdPais);
		registro.setCiudad(pCiudad);
		registro.setApartado(pApartado);
		registro.setDireccion1(pDireccion1);
		registro.setDireccion2(pDireccion2);
		registro.setDireccion3(pDireccion3);
		registro.setComentario(pComentario);
		registro.setEstado(pEstado);
		if (registro instanceof ClienteNatural) {
			((ClienteNatural) registro).setNombre(pNombre);
			((ClienteNatural) registro).setApellido(pApellido);
			((ClienteNatural) registro).setFechaNacimiento(pNacimiento);
			((ClienteNatural) registro).setSexo(pSexo);
			((ClienteNatural) registro).setPeso(pPeso);
		}
		
		//controller.update(registro);
		editorController.doSave(registro);
		if (isNewDoc) {
			System.out.println("Nuevo cliente natural creado");
			txtCodigo.setText(valor2Txt(registro.getIdCliente()));
			isNewDoc = false;
		} else {
			System.out.println("Cliente natural actualizado");
		}
		
		this.setPartName(registro.getTituloDocumento());
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		actualizarVista(ClientesView.ID);
		removeDirtyFlag();
	}
	
	public boolean validarSave() {
		String pNombre = txtNombre.getText();
		String pApellido = txtApellido.getText();
		String pIdentificacion = txtIdentificacion.getText();
		String pEmail = txtEmail.getText();
		
		if (isNewDoc) {
			int x = editorController.verificarClientePax(pNombre, pApellido);
			if (x > 0) {
				boolean resp = MessageDialog.openQuestion(getSite().getShell(), "Verificación de paxs", 
						"Ya existe una cotización con el pax \"" + pNombre + " " + pApellido + "\".  Está seguro de que\n" +
				"desea crear un nuevo cliente con este nombre de pax?");
				if (!resp) {
					return false;
				}
			}
		}
		if (comboTipoCliente.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Tipo cliente\" no puede quedar en blanco");
			return false;
		}
		if (comboFuente.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Fuente\" no puede quedar en blanco");
			return false;
		}
		if (pNombre.length() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El campo de \"Nombre\" no puede quedar en blanco.");
			return false;
		}
		if (pNombre.length() > 25) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre del cliente no puede superar los 25 caracteres (" + pNombre.length() + ").");
			return false;
		}
		if (pApellido.length() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El campo de \"Apellido\" no puede quedar en blanco.");
			return false;
		}
		if (pApellido.length() > 25) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El apellido del cliente no puede superar los 25 caracteres (" + pApellido.length() + ").");
			return false;
		}
		if (pIdentificacion.length() > 20) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El campo de identificación no puede superar los 20 caracteres (" + pIdentificacion.length() + ").");
			return false;
		}
		if (comboSexo.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Sexo\" no puede quedar en blanco");
			return false;
		}
		if (pEmail.length() > 35) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El campo de email no puede superar los 35 caracteres (" + pEmail.length() + ").");
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
		GridLayout gridLayout;
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		parent.setLayout(gridLayout);
		/*
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL);
		Composite cBase = new Composite(sc, SWT.NONE);
		sc.setContent(cBase);
		sc.setLayout(new GridLayout());
		parent.layout(true);
		cBase.setLayout(new GridLayout());
		cBase.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		cBase.setSize(cBase.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		*/
		/*
		Group grupoTop = new Group(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		grupoTop.setLayout(gridLayout);
		grupoTop.setLayoutData(gridData);
		*/
		agregarControles(parent);
		new Label(parent, SWT.NONE);
		llenarControles();
	}
	
	protected void agregarControles(Composite parent) {
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		TabItem tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("Generales");
		tabGeneral.setControl(createTabGeneralControl(tabFolder));
		
		TabItem tabDireccion = new TabItem(tabFolder, SWT.NONE);
		tabDireccion.setText("Ubicación");
		tabDireccion.setControl(createTabUbicacionControl(tabFolder));
		
	}
	
	private Control createTabGeneralControl(TabFolder tabFolder) {
		Label l;
		GridData gridData;
		GridLayout gridLayout;
		
		Composite parent = new Composite(tabFolder, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		parent.setLayout(gridLayout);

		l = new Label(parent, SWT.NONE);
		l.setText("Código:");
		txtCodigo = new Text(parent, SWT.BORDER);
		gridData = new GridData(40,15);
		gridData.horizontalSpan = 1;
		txtCodigo.setLayoutData(gridData);
		txtCodigo.setEditable(false);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Tipo cliente:");
		comboTipoCliente = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		comboTipoCliente.setLayoutData(gridData);
		comboTipoCliente.setItems(cdTipoCliente.getTexto());
		comboTipoCliente.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Fuente:");
		comboFuente = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		comboFuente.setLayoutData(gridData);
		comboFuente.setItems(new String[] {"Correo electrónico", "Página web", "Teléfono/fax", "Walk-in"});
		comboFuente.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Comisión:");
//		comboComision = new Combo(parent, SWT.READ_ONLY);
//		gridData = new GridData();
//		gridData.horizontalSpan = 2;
//		comboComision.setLayoutData(gridData);
//		comboComision.setItems(new String[] {"Correo electrónico", "Página web", "Teléfono/fax", "Walk-in"});
//		comboComision.addModifyListener(this.createModifyListener());
		
		comboComision = new Combo(parent, SWT.READ_ONLY);
		comboComision.setItems(cdComision.getTexto());
		comboComision.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		comboComision.setText(Condicional.NO.getDescripcion());
		comboComision.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Nombre:");
		txtNombre = new Text(parent, SWT.BORDER);
		txtNombre.setLayoutData(new GridData(125,15));
		txtNombre.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Apellido:");
		txtApellido = new Text(parent, SWT.BORDER);
		gridData = new GridData(125,15);
		gridData.horizontalSpan = 2;
		txtApellido.setLayoutData(gridData);
		txtApellido.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Identificación:");
		txtIdentificacion = new Text(parent, SWT.BORDER);
		txtIdentificacion.setLayoutData(new GridData(80,15));
		txtIdentificacion.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
	    l.setText("Fecha nac.:");
	    txtNacimiento = new Text(parent, SWT.BORDER);
	    txtNacimiento.setLayoutData(new GridData(60,15));
	    txtNacimiento.setTextLimit(10);
	    txtNacimiento.addModifyListener(this.createModifyListener());
	    
	    bFecha = new Button(parent, SWT.NONE);
	    gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
	    bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/dateChooser.gif");
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(txtNacimiento));
		
		l = new Label(parent, SWT.NONE);
		l.setText("Peso (lbs):");
		txtPeso = new Text(parent, SWT.BORDER);
		gridData = new GridData(35,15);
		gridData.horizontalSpan = 1;
		txtPeso.setLayoutData(gridData);
		txtPeso.setTextLimit(3);
		txtPeso.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Sexo");
		comboSexo = new Combo(parent, SWT.READ_ONLY);
		comboSexo.setItems(cdSexo.getTexto());
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		comboSexo.setLayoutData(gridData);
		comboSexo.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Teléfono:");
		txtTelefono = new Text(parent, SWT.BORDER);
		txtTelefono.setLayoutData(new GridData(75,15));
		txtTelefono.setTextLimit(15);
		txtTelefono.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Email:");
		txtEmail = new Text(parent, SWT.BORDER);
		gridData = new GridData(150,15);
		gridData.horizontalSpan = 2;
		txtEmail.setLayoutData(gridData);
		txtEmail.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Forma pago:");
		comboPago = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		comboPago.setLayoutData(gridData);
		comboPago.setItems(cdFormaPago.getTexto());
		comboPago.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setText("Comentarios:");
		l.setLayoutData(gridData);
		txtComentario = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1);
		gridData.heightHint = 55;
		txtComentario.setLayoutData(gridData);
		txtComentario.addModifyListener(this.createModifyListener());
		
		return parent;
	}
	
	private Control createTabUbicacionControl(TabFolder tabFolder) {
		Label l;
		GridData gridData;
		GridLayout gridLayout;

		Group parent = new Group(tabFolder, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayout(gridLayout);
		parent.setLayoutData(gridData);
		
		l = new Label(parent, SWT.NONE);
		l.setText("País:");
		comboPais = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 3;
		comboPais.setLayoutData(gridData);
		comboPais.setItems(cdPais.getTexto());
		comboPais.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Ciudad:");
		txtCiudad = new Text(parent, SWT.BORDER);
		txtCiudad.setLayoutData(new GridData(100,15));
		txtCiudad.setTextLimit(30);
		txtCiudad.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Apartado:");
		txtApartado = new Text(parent, SWT.BORDER);
		txtApartado.setLayoutData(new GridData(120,15));
		txtApartado.setTextLimit(20);
		txtApartado.addModifyListener(this.createModifyListener());
		
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
		gridData.horizontalSpan = 3;
		direcciones.setLayoutData(gridData);
		txtDireccion1 = new Text(direcciones, SWT.BORDER);
		gridData = new GridData(250,15);
		gridData.horizontalSpan = 3;
		txtDireccion1.setLayoutData(gridData);
		txtDireccion1.setTextLimit(40);
		txtDireccion1.addModifyListener(this.createModifyListener());
		txtDireccion2 = new Text(direcciones, SWT.BORDER);
		gridData = new GridData(250,15);
		gridData.horizontalSpan = 3;
		txtDireccion2.setLayoutData(gridData);
		txtDireccion2.setTextLimit(40);
		txtDireccion2.addModifyListener(this.createModifyListener());
		txtDireccion3 = new Text(direcciones, SWT.BORDER);
		gridData = new GridData(250,15);
		gridData.horizontalSpan = 3;
		txtDireccion3.setLayoutData(gridData);
		txtDireccion3.setTextLimit(40);
		txtDireccion3.addModifyListener(this.createModifyListener());
		
		return parent;
	}
	
	

	protected void llenarControles() {
		System.out.println("Llenar controles");
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo cliente");
			registro = new ClienteNatural();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (Cliente) ((CommonEditorInput) this.getEditorInput()).getElemento();
			System.out.println("Pais: " + registro.getDspPais());
			txtCodigo.setText("" + registro.getIdCliente());
			System.out.println("Tipo: " + registro.getDspTipo());
			//comboTipoCliente.select(comboTipoCliente.indexOf(registro.getDspTipo()));
			comboTipoCliente.setText(valor2Txt(registro.getDspTipo()));
			comboFuente.setText(valor2Txt(registro.getFuente()));
			comboComision.setText(cdComision.getTextoByKey(Boolean.toString(registro.getComision())));
			txtIdentificacion.setText(valor2Txt(registro.getIdentificacion()));
			txtTelefono.setText(valor2Txt(registro.getTelefono()));
			txtEmail.setText(valor2Txt(registro.getEmail()));
			comboPago.setText(checkNull(cdFormaPago.getTextoByKey(registro.getFormaPago())));
			txtComentario.setText(valor2Txt(registro.getComentario()));
			comboPais.select(comboPais.indexOf(registro.getDspPais()));
			txtCiudad.setText(valor2Txt(registro.getCiudad()));
			txtApartado.setText(valor2Txt(registro.getApartado()));
			txtDireccion1.setText(valor2Txt(registro.getDireccion1()));
			txtDireccion2.setText(valor2Txt(registro.getDireccion2()));
			txtDireccion3.setText(valor2Txt(registro.getDireccion3()));
			if (registro instanceof ClienteNatural) {
				String nombre = valor2Txt(((ClienteNatural) registro).getNombre());
				String apellido = valor2Txt(((ClienteNatural) registro).getApellido());
				String fechaNac = FechaUtil.toString(((ClienteNatural) registro).getFechaNacimiento());
				String sexo = cdSexo.getTextoByKey(((ClienteNatural) registro).getSexo());
				String peso = valor2Txt(((ClienteNatural) registro).getPeso());
				txtNombre.setText(nombre);
				txtApellido.setText(apellido);
				txtNacimiento.setText(fechaNac);
				comboSexo.select(comboSexo.indexOf(sexo));
				txtPeso.setText(peso);
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

