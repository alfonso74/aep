package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.TipoHabitacionesController;
import rcp.manticora.model.Producto;
import rcp.manticora.model.TipoHabitacion;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.TipoHabitacionesView;

public class TipoHabitacionesEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.tipoHabitaciones";
	private Combo comboHotel;
	private Combo comboTipo;
	private Text txtComentario;
	private Text txtCondiciones;
	
	private ComboData cdHotel;
	
	private TipoHabitacion registro;
	private TipoHabitacionesController editorController;
	private ComboDataController cdController;

	public TipoHabitacionesEditor() {
		editorController = new TipoHabitacionesController(ID);
		cdController = new ComboDataController();
		cdHotel = cdController.getComboDataHoteles();
	}

	@Override
	protected void agregarControles(Composite parent) {
		Label l;
		GridData gridData;
		
		l = new Label(parent, SWT.NONE);
		l.setText("Hotel:");
		comboHotel = new Combo(parent, SWT.READ_ONLY);
		comboHotel.setItems(cdHotel.getTexto());
		comboHotel.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Tipo habitación:");
		comboTipo = new Combo(parent, SWT.NONE);
		comboTipo.setItems(new String[] {"Single", "Doble", "Triple"});
		gridData = new GridData(75,15);
		comboTipo.setLayoutData(gridData);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Condiciones:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);
		txtCondiciones = new Text(parent, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		gridData.widthHint = 250;
		gridData.heightHint = 55;
		txtCondiciones.setLayoutData(gridData);
		txtCondiciones.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Texto de ayuda:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);
		txtComentario = new Text(parent, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData.widthHint = 250;
		gridData.heightHint = 55;
		txtComentario.setLayoutData(gridData);
		txtComentario.addModifyListener(this.createModifyListener());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		String newline = System.getProperty("line.separator");
		System.out.println("doSave de TipoHabitacionesEditor");
		
		Long id = cdHotel.getKeyAsLongByIndex(comboHotel.getSelectionIndex());
		Producto pHotel = editorController.getProductoById(id);
		String pDescripcion = comboTipo.getText();
		String[] pListaCondiciones = txtCondiciones.getText().split(newline);
		String pComentario = txtComentario.getText();
		
		registro.setHotel(pHotel);
		registro.setDescripcion(pDescripcion);
		registro.setListaCondiciones(pListaCondiciones);
		registro.setComentario(pComentario);
		editorController.doSave(registro);
		if (isNewDoc) {
			isNewDoc = false;
		}
		
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		this.setPartName(registro.getTituloDocumento());
		actualizarVista(TipoHabitacionesView.ID);
		removeDirtyFlag();
	}

	@Override
	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo tipo de habitación...");
			registro = new TipoHabitacion();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (TipoHabitacion) ((CommonEditorInput) this.getEditorInput()).getElemento();
			comboHotel.setText(registro.getHotel().getDescripcion());
			comboTipo.setText(registro.getDescripcion());
			llenarCampoCondiciones();
			txtComentario.setText(registro.getComentario());
		}
		addFilledFlag();
		setFocoInicial(comboTipo);
	}
	
	private void llenarCampoCondiciones() {
		String newline = System.getProperty("line.separator");
		String[] condiciones = registro.getListaCondiciones();
		String cadena = "";
		for (int n=0; n < condiciones.length; n++) {
			if (cadena.equals("")) {
				cadena = condiciones[n];
			} else {
				cadena += newline + condiciones[n];
			}
		}
		txtCondiciones.setText(cadena);
	}

	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}
}
