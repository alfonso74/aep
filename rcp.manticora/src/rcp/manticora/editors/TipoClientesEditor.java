package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.TipoClientesController;
import rcp.manticora.model.TipoCliente;
import rcp.manticora.views.TipoClientesView;

public class TipoClientesEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.tipoClientes";
	private Text txtTipo;
	private Text txtDescripcion;
	
	private TipoCliente registro;
	private TipoClientesController editorController;

	public TipoClientesEditor() {
		super();
		editorController = new TipoClientesController(ID);
	}

	public void doSave(IProgressMonitor monitor) {
		String pDescripcion = txtDescripcion.getText();
		
		registro.setDescripcion(pDescripcion);
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
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo tipo de cliente...");
			registro = new TipoCliente();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (TipoCliente) ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtTipo.setText(valor2Txt(registro.getIdTipo()));
			txtDescripcion.setText(registro.getDescripcion());
		};
		setFocoInicial(txtDescripcion);
		addFilledFlag();
	}

	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}
}
