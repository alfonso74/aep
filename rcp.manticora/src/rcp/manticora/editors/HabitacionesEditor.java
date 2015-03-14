package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.HabitacionesController;
import rcp.manticora.model.Habitacion;
import rcp.manticora.model.Producto;
import rcp.manticora.model.TipoHabitacion;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.HabitacionesView;

public class HabitacionesEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.habitaciones";
	private Combo comboHotel;
	private Text txtNombre;
	private Text txtNumero;
	private Text txtPiso;
	private Combo comboStatus;
	private Combo comboTipoHab;
	private CheckboxTableViewer viewerCond;
	
	private ComboData cdHotel;
	private ComboData cdStatus;
	private ComboData cdTipoHab;
	
	private Habitacion registro;
	private HabitacionesController editorController;
	private ComboDataController cdController;
	
	public HabitacionesEditor() {
		editorController = new HabitacionesController(ID);
		cdController = new ComboDataController();
		cdHotel = cdController.getComboDataHotelesAEP();
		cdStatus = cdController.getComboDataKeyword("Status general");
	}
	
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		gridLayout.marginTop = 12;
		gridLayout.marginLeft = 10;
		parent.setLayout(gridLayout);
		
		agregarControles(parent);
		llenarControles();
	}

	@Override
	protected void agregarControles(Composite parent) {
		Label l;
		GridData gridData;
		
		l = new Label(parent, SWT.NONE);
		l.setText("Hotel (AEP):");
		comboHotel = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		comboHotel.setLayoutData(gridData);
		comboHotel.setItems(cdHotel.getTexto());
		comboHotel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int indice = comboHotel.getSelectionIndex();
				if (indice != -1) {
					System.out.println("Actualizando tipo de habitaciones...");
					Long id = cdHotel.getKeyAsLongByIndex(indice);
					Producto hotel = editorController.getProductoById(id);
					cdTipoHab = cdController.getComboDataTipoHabitacionesByHotel(hotel);
					comboTipoHab.setItems(cdTipoHab.getTexto());
				}
			}
		});
		comboHotel.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Nombre");
		txtNombre = new Text(parent, SWT.BORDER);
		gridData = new GridData(120, 15);
		gridData.horizontalSpan = 5;
		txtNombre.setLayoutData(gridData);
		txtNombre.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Número:");
		txtNumero = new Text(parent, SWT.BORDER);
		gridData = new GridData(30, 15);
		gridData.horizontalSpan = 1;
		txtNumero.setLayoutData(gridData);
		txtNumero.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("   Piso:");
		txtPiso = new Text(parent, SWT.BORDER);
		gridData = new GridData(30, 15);
		gridData.horizontalSpan = 1;
		txtPiso.setLayoutData(gridData);
		txtPiso.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("   Status:");
		comboStatus = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		comboStatus.setLayoutData(gridData);
		comboStatus.setItems(cdStatus.getTexto());
		comboStatus.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Tipo hab.:");
		comboTipoHab = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		comboTipoHab.setLayoutData(gridData);
		comboTipoHab.addModifyListener(this.createModifyListener());
		comboTipoHab.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int indice = comboTipoHab.getSelectionIndex();
				if (indice != -1) {
					System.out.println("Actualizando condiciones disponibles...");
					Long id = cdTipoHab.getKeyAsLongByIndex(indice);
					TipoHabitacion tipoHab = editorController.getTipoHabitacionById(id);
					String[] condiciones = tipoHab.getListaCondiciones();
					viewerCond.setInput(condiciones);
				}
			}
		});
		
		l = new Label(parent, SWT.NONE);
		l.setText("Condiciones:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);

		viewerCond = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 1);
		gridData.widthHint = 250;
		gridData.heightHint = 60;
		viewerCond.getTable().setLayoutData(gridData);
		viewerCond.setContentProvider(new ViewContentProvider());
		viewerCond.setLabelProvider(new ViewLabelProvider());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		Long id = cdHotel.getKeyAsLongByIndex(comboHotel.getSelectionIndex());
		Producto pHotel = editorController.getProductoById(id);
		id = cdTipoHab.getKeyAsLongByIndex(comboTipoHab.getSelectionIndex());
		TipoHabitacion pTipoHab = editorController.getTipoHabitacionById(id);
		String pNombre = txtNombre.getText();
		Integer pNumero = txt2Integer(txtNumero.getText());
		Integer pPiso = txt2Integer(txtPiso.getText());
		Object[] pChecked = viewerCond.getCheckedElements();
		String[] pCondiciones = new String[pChecked.length];
		for (int n = 0; n < pChecked.length; n++) {
			pCondiciones[n] = (String) pChecked[n];
		}
		String pEstado = cdStatus.getCodeByIndex(comboStatus.getSelectionIndex());
		
		registro.setHotel(pHotel);
		registro.setTipo(pTipoHab);
		registro.setNombre(pNombre);
		registro.setNumero(pNumero);
		registro.setPiso(pPiso);
		registro.setListaCondiciones((String[]) pCondiciones);
		registro.setEstado(pEstado);
		editorController.doSave(registro);
		if (isNewDoc) {
			isNewDoc = false;
		}
		
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
		this.setPartName(registro.getTituloDocumento());
		actualizarVista(HabitacionesView.ID);
		removeDirtyFlag();
	}
	
	
	private boolean validarSave() {
		String pNombre = txtNombre.getText();
		String pNumero = txtNumero.getText();
		String pPiso = txtPiso.getText();
		
		if (comboHotel.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Hotel\" no puede quedar en blanco");
			return false;
		}
		if (comboStatus.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Status\" no puede quedar en blanco");
			return false;
		}
		if (comboTipoHab.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Tipo hab.\" no puede quedar en blanco");
			return false;
		}
		if (pNombre.equals("")) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Nombre\" no puede quedar en blanco");
			return false;
		}
		if (pNumero.equals("")) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Número\" no puede quedar en blanco");
			return false;
		}
		if (pPiso.equals("")) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
				"El campo de \"Piso\" no puede quedar en blanco");
			return false;
		}
		return true;
	}
	

	@Override
	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nueva habitación (Hospedaje AEP)...");
			registro = new Habitacion();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (Habitacion) ((CommonEditorInput) this.getEditorInput()).getElemento();
			editorController.getSession().refresh(registro);
			comboHotel.setText(registro.getHotel().getDescripcionHotel());
			txtNombre.setText(registro.getNombre());
			txtNumero.setText(valor2Txt(registro.getNumero()));
			txtPiso.setText(valor2Txt(registro.getPiso()));
			comboStatus.setText(registro.getDspEstado());
			refreshTipoHabitacion();
			comboTipoHab.setText(registro.getTipo().getDescripcion());
			viewerCond.setInput(registro.getTipo().getListaCondiciones());
			viewerCond.setCheckedElements(registro.getListaCondiciones());
		}
		addFilledFlag();
		setFocoInicial(txtNombre);
	}
	
	@Override
	public void dispose() {
		editorController.finalizar(ID);
		super.dispose();
	}
	
	private void refreshTipoHabitacion() {
		int indice = comboHotel.getSelectionIndex();
		if (indice != -1) {
			Long id = cdHotel.getKeyAsLongByIndex(indice);
			Producto hotel = editorController.getProductoById(id);
			cdTipoHab = cdController.getComboDataTipoHabitacionesByHotel(hotel);
			comboTipoHab.setItems(cdTipoHab.getTexto());
		}
	}
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = (String[]) inputElement;
			return resultados;
		}
	}
	
	
	class ViewLabelProvider extends LabelProvider implements ILabelProvider {

		@Override
		public String getText(Object element) {
			return (String) element;
		}
	}
}
