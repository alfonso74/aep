package rcp.manticora.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.RedesController;
import rcp.manticora.model.Red;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.RedesView;


public class RedesEditor extends AbstractEditorH {

	public static final String ID = "rcp.manticora.editors.RedesEditor"; //$NON-NLS-1$
	private Text txtCodigo;
	private Text txtDescripcion;
	
	private Combo comboStatus;
	private ComboData cdStatus;
	
	private Red registro;
	private RedesController editorController;
	private ComboDataController cdController;
	

	public RedesEditor() {
		System.out.println("Inicializando RedesEditor...");
		editorController = new RedesController(ID);
		cdController = new ComboDataController();
		cdStatus = cdController.getComboDataKeyword("Status general");
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginLeft = 5;
		gl_parent.marginTop = 10;
		parent.setLayout(gl_parent);
		
		Label lblCodigo = new Label(parent, SWT.NONE);
		lblCodigo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCodigo.setText("C\u00F3digo:");
		
		txtCodigo = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txtCodigo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtCodigo.widthHint = 40;
		txtCodigo.setLayoutData(gd_txtCodigo);
		txtCodigo.addModifyListener(this.createModifyListener());
		
		Label lblDescripcion = new Label(parent, SWT.NONE);
		lblDescripcion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescripcion.setText("Descripci\u00F3n:");
		
		txtDescripcion = new Text(parent, SWT.BORDER);
		GridData gd_txtDescripcion = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtDescripcion.widthHint = 120;
		txtDescripcion.setLayoutData(gd_txtDescripcion);
		txtDescripcion.addModifyListener(this.createModifyListener());
		
		Label lblEstado = new Label(parent, SWT.NONE);
		lblEstado.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEstado.setText("Estado:");
		
		comboStatus = new Combo(parent, SWT.READ_ONLY);
		comboStatus.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboStatus.setItems(cdStatus.getTexto());
		comboStatus.addModifyListener(this.createModifyListener());

		// acciones necesarias luego de crear los controles
		llenarControles();
		addFilledFlag();
		setFocoInicial(txtDescripcion);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		System.out.println("doSave de RedesEditor");
		String pDescripcion = txtDescripcion.getText();
		String pEstado = cdStatus.getCodeByIndex(comboStatus.getSelectionIndex());
		
		registro.setDescripcion(pDescripcion);
		registro.setEstado(pEstado);
		editorController.doSave(registro);
		if (isNewDoc) {
			txtCodigo.setText(valor2Txt(registro.getIdRed()));
			isNewDoc = false;
		}
		
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		this.setPartName(registro.getTituloDocumento());
		actualizarVista(RedesView.ID);
		removeDirtyFlag();
	}

	@Override
	protected void agregarControles(Composite parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void llenarControles() {
		if (getEditorInput().isNewDoc) {
			registro = new Red();
			comboStatus.setText("Activo");
			this.setPartName("Nueva red de viajes");
		} else {
			registro = (Red) ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtCodigo.setText(valor2Txt(registro.getIdRed()));
			txtDescripcion.setText(valor2Txt(registro.getDescripcion()));
			comboStatus.setText(cdStatus.getTextoByKey(valor2Txt(registro.getEstado())));
		}
	}
	
	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}
}
