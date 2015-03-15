package rcp.manticora.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.RolesController;
import rcp.manticora.model.Keyword;
import rcp.manticora.model.Rol;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.RolesView;


public class RolesEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.roles";
	private Text txtCodigo;
	private Text txtDescripcion;
	
	private Combo comboStatus;
	private ComboData cdStatus;
	
	private Rol registro;
	private RolesController editorController;
	private ComboDataController cdController;
	
	
	public RolesEditor() {
		System.out.println("Inicializando RolesEditor...");
		editorController = new RolesController(ID);
		cdController = new ComboDataController();
		cdStatus = cdController.getComboDataKeyword(TipoKeyword.STATUS_GENERAL);
	}
	
	
	@Override
	protected void agregarControles(Composite parent) {
		Label l;
		
		l = new Label(parent, SWT.NONE);
		l.setText("Código:");
		txtCodigo = new Text(parent, SWT.SINGLE | SWT.BORDER);
		txtCodigo.setLayoutData(new GridData(40,15));
		txtCodigo.setEditable(false);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Descripción:");
		txtDescripcion = new Text(parent, SWT.SINGLE | SWT.BORDER);
		txtDescripcion.setLayoutData(new GridData(120,15));
		txtDescripcion.addModifyListener(this.createModifyListener());
		
		l = new Label (parent, SWT.NONE);
		l.setText("Estado:");
		comboStatus = new Combo(parent, SWT.READ_ONLY);
		comboStatus.setItems(cdStatus.getTexto());
		comboStatus.select(0);
		comboStatus.addModifyListener(this.createModifyListener());
	}

	
	@Override
	public void doSave(IProgressMonitor monitor) {
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		String pDescripcion = txtDescripcion.getText();
		//Keyword pEstado = (Keyword) cdStatus.getObjectByIndex(comboStatus.getSelectionIndex());
		String pStatusCode = cdStatus.getCodeByIndex(comboStatus.getSelectionIndex());
		String pStatusText = cdStatus.getTextoByIndex(comboStatus.getSelectionIndex());
		
		registro.setDescripcion(pDescripcion);
		//registro.setEstado(pEstado.getCodigo());
		//registro.setDspEstado(pEstado.getDescripcion());
		registro.setEstado(pStatusCode);
		registro.setDspEstado(pStatusText);
		
		editorController.doSave(registro);
		if (isNewDoc) {
			txtCodigo.setText(registro.getIdRol().toString());
			isNewDoc = false;
		}
		this.setPartName(registro.getTituloDocumento());
		// actualizamos el nombre del editorInput (para que deje de parecer "Nuevo")
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		actualizarVista(RolesView.ID);
		removeDirtyFlag();
	}
	
	
	private boolean validarSave() {
		String pDescripcion = txtDescripcion.getText();
		if (pDescripcion.equals("")) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo 'Descripción' no puede quedar en blanco.");
			return false;
		}
		if (pDescripcion.length() > 15) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre del rol no puede superar los 15 caracteres (" + pDescripcion.length() + ").");
			return false;
		}
		return true;
	}

	
	@Override
	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo rol...");
			registro = new Rol();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			System.out.println("Ejecutando código para cargar datos...");
			registro = (Rol) ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtCodigo.setText(valor2Txt(registro.getIdRol()));
			txtDescripcion.setText(valor2Txt(registro.getDescripcion()));
			comboStatus.setText(registro.getDspEstado());
		}
		addFilledFlag();
		setFocoInicial(txtDescripcion);
	}
	
	
	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}

}

