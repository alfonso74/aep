package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.TipoClientesController;
import rcp.manticora.model.TipoCliente;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.views.TipoClientesView;

public class TipoClientesEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.tipoClientes";
	private String idSession = ID + FechaUtil.getMilisegundos();
	private Text txtTipo;
	private Text txtDescripcion;
	
	private Combo comboStatus;
	private ComboData cdStatus;
	
	private TipoCliente registro;
	private TipoClientesController editorController;
	private ComboDataController cdController;

	public TipoClientesEditor() {
		super();
		editorController = new TipoClientesController(idSession);
		cdController = new ComboDataController();
		cdStatus = cdController.getComboDataKeyword(TipoKeyword.STATUS_GENERAL);
	}

	public void doSave(IProgressMonitor monitor) {
		String pDescripcion = txtDescripcion.getText();
		String pEstado = cdStatus.getCodeByIndex(comboStatus.getSelectionIndex());
		
		registro.setDescripcion(pDescripcion);
		registro.setEstado(pEstado);
		editorController.doSave(registro);
		if (isNewDoc) {
			txtTipo.setText(valor2Txt(registro.getIdTipo()));
			isNewDoc = false;
		};
		
		// actualizamos el nombre del editorInput (para que deje de parecer "Nuevo")
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		this.setPartName(registro.getTituloDocumento());
		actualizarVista(TipoClientesView.ID);
		removeDirtyFlag();
	}

	protected void agregarControles(Composite parent) {
		Label l;
		l = new Label(parent, SWT.NONE);
		l.setText("Tipo cliente:");
		txtTipo = new Text(parent, SWT.SINGLE | SWT.BORDER);
		txtTipo.setLayoutData(new GridData(40,15));
		txtTipo.setEditable(false);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Descripción:");
		txtDescripcion = new Text(parent, SWT.BORDER);
		txtDescripcion.setLayoutData(new GridData(180,15));
		txtDescripcion.addModifyListener(this.createModifyListener());
		
		Label lblEstado = new Label(parent, SWT.NONE);
		lblEstado.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEstado.setText("Estado:");
		
		comboStatus = new Combo(parent, SWT.READ_ONLY);
		comboStatus.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboStatus.setItems(cdStatus.getTexto());
		comboStatus.addModifyListener(this.createModifyListener());
	}

	protected void llenarControles() {
		if (getEditorInput().isNewDoc) {
			System.out.println("Creando nuevo tipo de cliente...");
			registro = new TipoCliente();
			comboStatus.setText("Activo");
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (TipoCliente) ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtTipo.setText(valor2Txt(registro.getIdTipo()));
			txtDescripcion.setText(registro.getDescripcion());
			comboStatus.setText(cdStatus.getTextoByKey(valor2Txt(registro.getEstado())));
		};
		setFocoInicial(txtDescripcion);
		addFilledFlag();
	}

	@Override
	public void dispose() {
		editorController.finalizar(idSession);
		super.dispose();
	}
}
