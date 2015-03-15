package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.PaisesController;
import rcp.manticora.model.Pais;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.PaisesView;


public class PaisesEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.paises";
	private Text txtCodigo;
	private Text txtNombre;
	private Combo comboStatus;
	
	private ComboData cdStatus;
	
	private Pais registro;
	private PaisesController editorController;
	private ComboDataController cdController;

	public PaisesEditor() {
		editorController = new PaisesController(ID);
		cdController = new ComboDataController();
		cdStatus = cdController.getComboDataKeyword(TipoKeyword.STATUS_GENERAL);
	}

	public void doSave(IProgressMonitor monitor) {
		System.out.println("doSave de PaisesEditor");
		String pDescripcion = txtNombre.getText();
		String pEstado = cdStatus.getCodeByIndex(comboStatus.getSelectionIndex());
		
		registro.setDescripcion(pDescripcion);
		registro.setEstado(pEstado);
		editorController.doSave(registro);
		if (isNewDoc) {
			txtCodigo.setText(valor2Txt(registro.getIdPais()));
			isNewDoc = false;
		}
		
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		this.setPartName(registro.getTituloDocumento());
		actualizarVista(PaisesView.ID);
		removeDirtyFlag();
	}

	protected void agregarControles(Composite parent) {
		Label l = new Label(parent, SWT.NONE);
		l.setText("Código:");
		txtCodigo = new Text(parent, SWT.BORDER);
		txtCodigo.setLayoutData(new GridData(40,15));
		txtCodigo.setEditable(false);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Nombre:");
		txtNombre = new Text(parent, SWT.BORDER);
		txtNombre.setLayoutData(new GridData(140,15));
		txtNombre.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Estado:");
		comboStatus = new Combo(parent, SWT.READ_ONLY);		
		comboStatus.setItems(cdStatus.getTexto());
		comboStatus.addModifyListener(this.createModifyListener());
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo país");
			registro = new Pais();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
			comboStatus.setText("Activo");
		} else {
			registro = (Pais)  ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtCodigo.setText(Long.toString(registro.getIdPais()));
			txtNombre.setText(valor2Txt(registro.getDescripcion()));
			String pEstado = cdStatus.getTextoByKey(valor2Txt(registro.getEstado()));
			comboStatus.setText(pEstado);
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
