package rcp.manticora.editors;

import java.util.HashSet;
import java.util.Set;



import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.UsuariosController;
import rcp.manticora.model.Rol;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.model.Usuario;
import rcp.manticora.services.AutenticacionUtil;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.UsuariosView;

public class UsuariosEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.usuarios";
	private Text txtCodigo;
	private Text txtNombre;
	private Text txtApellido;
	private Text txtUserName;
	private Text txtPassword;
	private Combo comboStatus;
	private CheckboxTableViewer viewerRoles;
	
	private ComboData cdStatus;
	
	private Usuario registro;
	private UsuariosController editorController;
	private ComboDataController cdController;
	private boolean passwordFlag = true;
	
	
	public UsuariosEditor() {
		editorController = new UsuariosController(ID);
		cdController = new ComboDataController();
		cdStatus = cdController.getComboDataKeyword(TipoKeyword.STATUS_GENERAL);
	}
	

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.marginTop = 12;
		gridLayout.marginLeft = 10;
		parent.setLayout(gridLayout);
		
		agregarControles(parent);
		llenarControles();
	}


	@Override
	protected void agregarControles(Composite parent) {
		Label l;
		GridData gridData;
		
		l = new Label(parent, SWT.NONE);
		l.setText("Código:");
		txtCodigo = new Text(parent, SWT.BORDER);
		gridData = new GridData(40, 15);
		gridData.horizontalSpan = 2;
		txtCodigo.setLayoutData(gridData);
		txtCodigo.setEditable(false);
		
		Button bFechaIni = new Button(parent, SWT.NONE);
		bFechaIni.setLayoutData(new GridData(16,16));
		//image = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.CALENDARIO);
		//bFechaIni.setImage(image.createImage());
		//bFechaIni.addSelectionListener(this.crearCalendario(txtInicio));
		bFechaIni.setText("...");
		bFechaIni.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				txtPassword.setEnabled(true);
				passwordFlag = true;
			}	
		});
		
		l = new Label(parent, SWT.NONE);
		l.setText("Nombre:");
		txtNombre = new Text(parent, SWT.BORDER);
		gridData = new GridData(120, 15);
		gridData.horizontalSpan = 1;
		txtNombre.setLayoutData(gridData);
		txtNombre.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Apellido:");
		txtApellido = new Text(parent, SWT.BORDER);
		gridData = new GridData(120, 15);
		gridData.horizontalSpan = 1;
		txtApellido.setLayoutData(gridData);
		txtApellido.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Username:");
		txtUserName = new Text(parent, SWT.BORDER);
		txtUserName.setLayoutData(new GridData(100,15));
		txtUserName.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Password:");
		txtPassword = new Text(parent, SWT.PASSWORD | SWT.BORDER);
		txtPassword.setLayoutData(new GridData(100,15));
		txtPassword.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Estado:");
		comboStatus = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		comboStatus.setLayoutData(gridData);
		comboStatus.setItems(cdStatus.getTexto());
		comboStatus.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Roles:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);

		viewerRoles = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		gridData.widthHint = 250;
		gridData.heightHint = 60;
		viewerRoles.getTable().setLayoutData(gridData);
		viewerRoles.setContentProvider(new ViewContentProvider());
		viewerRoles.setLabelProvider(new ViewLabelProvider());
		viewerRoles.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				addDirtyFlag();
			}
		});
	}

	
	@Override
	public void doSave(IProgressMonitor monitor) {
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		String pNombre = txtNombre.getText();
		String pApellido = txtApellido.getText();
		String pUserName = txtUserName.getText();
		String pPassword = AutenticacionUtil.encodePasswordMD5(txtPassword.getText());
		String pStatusCode = cdStatus.getCodeByIndex(comboStatus.getSelectionIndex());
		String pStatusText = cdStatus.getTextoByIndex(comboStatus.getSelectionIndex());
		Object[] pChecked = viewerRoles.getCheckedElements();
		Set<Rol> pRolesAsignados = new HashSet<Rol>();
		for (int n = 0; n < pChecked.length; n++) {
			pRolesAsignados.add((Rol) pChecked[n]);
		}
		
		registro.setNombre(pNombre);
		registro.setApellido(pApellido);
		registro.setUserName(pUserName);
		//if (passwordFlag) registro.setPassword(pPassword);
		if (passwordFlag) {
			System.out.println("Set de password habilitado");
			registro.setPassword(pPassword);
		} else {
			System.out.println("Set de password deshabilitado");
		}
		registro.setEstado(pStatusCode);
		registro.setDspEstado(pStatusText);
		registro.asignarRoles(pRolesAsignados);
		
		editorController.doSave(registro);
		if (isNewDoc) {
			txtCodigo.setText(registro.getIdUsuario().toString());
			isNewDoc = false;
		}
		this.setPartName(registro.getTituloDocumento());
		// actualizamos el nombre del editorInput (para que deje de parecer "Nuevo")
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		actualizarVista(UsuariosView.ID);
		removeDirtyFlag();
	}
	
	
	private boolean validarSave() {
		String pNombre = txtNombre.getText();
		String pApellido = txtApellido.getText();
		String pUserName = txtUserName.getText();
		if (pNombre.equals("")) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo 'Nombre' no puede quedar en blanco.");
			return false;
		}
		if (pNombre.length() > 25) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre del usuario no puede superar los 25 caracteres (" + pNombre.length() + ").");
			return false;
		}
		if (pApellido.equals("")) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo 'Apellido' no puede quedar en blanco.");
			return false;
		}
		if (pApellido.length() > 25) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El apellido del usuario no puede superar los 25 caracteres (" + pApellido.length() + ").");
			return false;
		}
		if (pUserName.equals("")) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo 'Username' no puede quedar en blanco.");
			return false;
		}
		if (pUserName.length() > 20) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El campo de 'Username' no puede superar los 20 caracteres (" + pUserName.length() + ").");
			return false;
		}
		return true;
	}
	

	@Override
	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo usuario...");
			registro = new Usuario();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
			//viewerRoles.setInput(editorController.getListadoRoles());      // retorna array de roles
			viewerRoles.setInput(editorController.getListaRoles());
		} else {
			System.out.println("Ejecutando código para cargar datos...");
			registro = (Usuario) ((CommonEditorInput) this.getEditorInput()).getElemento();
			editorController.getSession().refresh(registro);
			// carga de datos del registro existente
			txtCodigo.setText(valor2Txt(registro.getIdUsuario()));
			txtNombre.setText(valor2Txt(registro.getNombre()));
			txtApellido.setText(valor2Txt(registro.getApellido()));
			txtUserName.setText(valor2Txt(registro.getUserName()));
			txtPassword.setEnabled(false);
			passwordFlag = false;
			comboStatus.setText(valor2Txt(registro.getDspEstado()));
			//viewerRoles.setInput(editorController.getListadoRoles());  // retorna array de roles
			viewerRoles.setInput(editorController.getListaRoles());   // retorna un Set ordenado
			Set<Rol> listaRoles = registro.getListaRoles();
			Object[] arrayRoles = listaRoles.toArray(new Object[listaRoles.size()]);
			viewerRoles.setCheckedElements(arrayRoles);
		}
		addFilledFlag();
		setFocoInicial(txtNombre);
	}


	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}
	
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			//Object[] resultados = (Rol[]) inputElement;
			Object[] resultados = ((Set<Rol>) inputElement).toArray();
			return resultados;
		}
	}
	
	
	class ViewLabelProvider extends LabelProvider implements ILabelProvider {

		@Override
		public String getText(Object element) {
			return ((Rol) element).getDescripcion();
		}
	}

}
