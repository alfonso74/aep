package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.VendedoresController;
import rcp.manticora.model.Vendedor;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.VendedoresView;

public class VendedoresEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.vendedores";
	private Text txtCodigo;
	private Text txtNombre;
	private Text txtApellido;
	private Combo comboStatus;
	
	private ComboData cdStatus;
	
	private Vendedor registro;
	private VendedoresController editorController;
	private ComboDataController cdController;

	public VendedoresEditor() {
		editorController = new VendedoresController(ID);
		cdController = new ComboDataController();
	}

	public void doSave(IProgressMonitor monitor) {		
		System.out.println("doSave de VendedoresEditor");
		String pNombre = txtNombre.getText();
		String pApellido = txtApellido.getText();
		String pStatus = cdStatus.getCodeByName(comboStatus.getItem(comboStatus.getSelectionIndex()));
		String pDspStatus = comboStatus.getText();

		registro.setNombre(pNombre);
		registro.setApellido(pApellido);
		registro.setEstado(pStatus);
		registro.setDspEstado(pDspStatus);
		editorController.doSave(registro);
		if (isNewDoc) {
			txtCodigo.setText(String.valueOf(registro.getIdVendedor()));
			isNewDoc = false;
		}

		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		this.setPartName(registro.getTituloDocumento());
		this.actualizarVista(VendedoresView.ID);
		removeDirtyFlag();
	}

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
		cdStatus = cdController.getComboDataKeyword("Status general");
		comboStatus = new Combo(parent, SWT.READ_ONLY);
		comboStatus.setItems(cdStatus.getTexto());
		comboStatus.select(0);
		comboStatus.addModifyListener(this.createModifyListener());
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo vendedor");
			registro = new Vendedor();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (Vendedor)  ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtCodigo.setText(Long.toString(registro.getIdVendedor()));
			txtNombre.setText(registro.getNombre());
			txtApellido.setText(registro.getApellido());
			comboStatus.select(comboStatus.indexOf(registro.getDspEstado()));
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
