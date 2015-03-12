package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.KeywordsController;
import rcp.manticora.model.Keyword;
import rcp.manticora.views.KeywordsView;

public class KeywordsEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.keywords";
	private Text txtId;
	private Text txtCodigo;
	private Text txtDescripcion;
	private Combo comboTipo;
	
	private Keyword registro;
	private KeywordsController editorController;

	public KeywordsEditor() {
		editorController = new KeywordsController(ID);
	}

	public void doSave(IProgressMonitor monitor) {
		System.out.println("doSave de KeywordsEditor");
		registro.setCodigo(txtCodigo.getText());
		registro.setDescripcion(txtDescripcion.getText());
		registro.setTipo(comboTipo.getItem(comboTipo.getSelectionIndex()));
		
		editorController.doSave(registro);
		if (isNewDoc) {
			txtId.setText(valor2Txt(registro.getIdKeyword()));
			isNewDoc = false;
		}
		
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		this.setPartName(registro.getTituloDocumento());
		actualizarVista(KeywordsView.ID);
		removeDirtyFlag();
	}

	protected void agregarControles(Composite parent) {
		Label l;
		//GridLayout gridLayout;
		//GridData gridData;
		
		l = new Label(parent, SWT.NONE);
		l.setText("ID:");
		txtId = new Text(parent, SWT.SINGLE | SWT.BORDER);
		txtId.setLayoutData(new GridData(40,15));
		txtId.setEditable(false);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Código:");
		txtCodigo = new Text(parent, SWT.SINGLE | SWT.BORDER);
		txtCodigo.setLayoutData(new GridData(120,15));
		txtCodigo.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Descripción:");
		txtDescripcion = new Text(parent, SWT.SINGLE | SWT.BORDER);
		txtDescripcion.setLayoutData(new GridData(120,15));
		txtDescripcion.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(parent, SWT.READ_ONLY);
		comboTipo.setItems(new String[] {"Forma de pago", "Sexo", "Status de cotizaciones", "Status general", "Status hoja de servicios", "Status de solicitudes", "Tipo de avión", "Tipo de impuesto", "Tipo de vehículo"});
		comboTipo.addModifyListener(this.createModifyListener());
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo keyword");
			registro = new Keyword();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (Keyword) ((CommonEditorInput) this.getEditorInput()).getElemento();
			txtId.setText(valor2Txt(registro.getIdKeyword()));
			txtCodigo.setText(registro.getCodigo());
			txtDescripcion.setText(registro.getDescripcion());
			comboTipo.select(comboTipo.indexOf(registro.getTipo()));
		}
		addFilledFlag();
		setFocoInicial(txtCodigo);
	}

	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}
}
