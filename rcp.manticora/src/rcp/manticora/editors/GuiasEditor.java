package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.GuiasController;
import rcp.manticora.model.Guia;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.GuiasView;

public class GuiasEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.guias";
	private Text txtCodigo;
	private Text txtNombre;
	private Text txtApellido;
	
	private Combo comboStatus;
	private ComboData cdStatus;
	
	private Guia registro;
	private GuiasController editorController;
	private ComboDataController cdController;
	
	public GuiasEditor() {
		System.out.println("Inicializando GuiasEditor");
		editorController = new GuiasController(ID);
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
		l.setText("Nombre:");
		txtNombre = new Text(parent, SWT.SINGLE | SWT.BORDER);
		txtNombre.setLayoutData(new GridData(120,15));
		txtNombre.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Apellido:");
		txtApellido = new Text(parent, SWT.SINGLE | SWT.BORDER);
		txtApellido.setLayoutData(new GridData(120,15));
		txtApellido.addModifyListener(this.createModifyListener());
		
		l = new Label (parent, SWT.NONE);
		l.setText("Estado:");
		comboStatus = new Combo(parent, SWT.READ_ONLY);
		comboStatus.setItems(cdStatus.getTexto());
		comboStatus.select(0);
		comboStatus.addModifyListener(this.createModifyListener());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		String pNombre = txtNombre.getText();
		String pApellido = txtApellido.getText();
		String pStatus = cdStatus.getCodeByIndex(comboStatus.getSelectionIndex());
		String pDspStatus = comboStatus.getText();
		
		registro.setNombre(pNombre);
		registro.setApellido(pApellido);
		registro.setEstado(pStatus);
		registro.setDspEstado(pDspStatus);
		
		editorController.doSave(registro);
		if (isNewDoc) {
			txtCodigo.setText(registro.getIdGuia().toString());
			isNewDoc = false;
		}
		
		this.setPartName(registro.getTituloDocumento());
		// actualizamos el nombre del editorInput (para que deje de parecer "Nuevo")
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		actualizarVista(GuiasView.ID);
		removeDirtyFlag();
	}
	
	
	@Override
	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo guía...");
			registro = new Guia();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (Guia) ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtCodigo.setText(valor2Txt(registro.getIdGuia()));
			txtNombre.setText(valor2Txt(registro.getNombre()));
			txtApellido.setText(valor2Txt(registro.getApellido()));
			comboStatus.setText(registro.getDspEstado());
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
