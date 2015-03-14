package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.TipoProductoController;
import rcp.manticora.model.TipoProducto;
import rcp.manticora.views.TipoProductosView;

public class TipoProductosEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.tipoProductos";
	private TipoProducto registro;
	private Text txtTipo;
	private Text txtDescripcion;
	private Combo comboTour;
	
	private TipoProductoController editorController;

	public TipoProductosEditor() {
		editorController = new TipoProductoController(ID);
	}

	public void doSave(IProgressMonitor monitor) {
		String pDescripcion = txtDescripcion.getText();
		boolean pTour = comboTour.getText().equals("Sí") ? true : false;
		registro.setDescripcion(pDescripcion);
		registro.setTour(pTour);
		editorController.doSave(registro);
		if (isNewDoc) {
			txtTipo.setText(valor2Txt(registro.getIdTipo()));
			isNewDoc = false;
		}
		
		this.setPartName(registro.getTituloDocumento());
		// actualizamos el nombre del editorInput (para que deje de parecer "Nuevo")
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		actualizarVista(TipoProductosView.ID);
		removeDirtyFlag();
	}

	protected void agregarControles(Composite parent) {
		Label l;
		l = new Label(parent, SWT.NONE);
		l.setText("Tipo prod.:");
		txtTipo = new Text(parent, SWT.SINGLE | SWT.BORDER);
		txtTipo.setLayoutData(new GridData(40,15));
		txtTipo.setEditable(false);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Descripción:");
		txtDescripcion = new Text(parent, SWT.BORDER);
		txtDescripcion.setLayoutData(new GridData(180,15));
		txtDescripcion.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Es tour?:");
		comboTour = new Combo(parent, SWT.READ_ONLY);
		comboTour.setItems(new String[] {"Sí", "No"});
		comboTour.select(1);
		comboTour.addModifyListener(this.createModifyListener());
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo tipo de producto...");
			registro = new TipoProducto();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (TipoProducto) ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtTipo.setText(valor2Txt(registro.getIdTipo()));
			txtDescripcion.setText(registro.getDescripcion());
			String pTour = valor2Txt(registro.isTour()).equals("true") ? "Sí" : "No";
			comboTour.select(comboTour.indexOf(pTour));
		};
		addFilledFlag();
		setFocoInicial(txtDescripcion);
	}

	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}
}
