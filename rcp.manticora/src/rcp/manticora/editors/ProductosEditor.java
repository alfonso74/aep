package rcp.manticora.editors;

import java.util.Date;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.controllers.ProductosController;
import rcp.manticora.model.Producto;
import rcp.manticora.services.ComboData;
import rcp.manticora.views.ProductosView;

public class ProductosEditor extends AbstractEditorH {
	public static final String ID = "manticora.editors.productos";
	private Combo comboCategoria;
	private Text txtProducto;
	private Combo comboStatus;
	private Text txtDescripcion;
	private Text txtCosto;
	private Text txtVenta0;     // precio mínimo de venta
	private Text txtVenta1;     // publico
	private Text txtVenta2;		// operador
	private Text txtVenta3;		// comisionable
	private Combo comboTour;
	private Combo comboReserva;
	private Combo comboModificable;
	private Button bHotelAEP;
	private Combo comboImpuesto;
	private Text txtComentario;
	private Label labelModificable;
	
	private ComboData cdTipoProducto;
	private ComboData cdKeyword;
	
	private Producto registro;
	private ProductosController editorController;
	private ComboDataController cdController;

	public ProductosEditor() {
		editorController = new ProductosController(ID);
		cdController = new ComboDataController();
	}

	public void doSave(IProgressMonitor monitor) {
		editorController.verSesiones();
		if (!validarSave()) {
			monitor.setCanceled(true);
			return;
		}
		// preparamos los valores del formulario
		String tProd = comboCategoria.getItem(comboCategoria.getSelectionIndex());
		Long pIdTipo = cdTipoProducto.getKeyAsLongByTexto(tProd);
		String pDescripcion = txtDescripcion.getText();
		if (pDescripcion.startsWith("Night(s) at ")) {
			pDescripcion = pDescripcion.substring(12);
		} else if (pDescripcion.startsWith("Night at ")){
			pDescripcion = pDescripcion.substring(9);
		} else if (pDescripcion.startsWith("Night(s) ")) {
			pDescripcion = pDescripcion.substring(9);
		} else if (pDescripcion.startsWith("Night(s)")) {
			pDescripcion = pDescripcion.substring(8);
		} else if (pDescripcion.startsWith("Night ")) {
			pDescripcion = pDescripcion.substring(6);
		}
		Float pCosto = txt2Float(txtCosto.getText());
		Float pVenta0 = txt2Float(txtVenta0.getText());
		Float pVenta1 = txt2Float(txtVenta1.getText());
		Float pVenta2 = txt2Float(txtVenta2.getText());
		Float pVenta3 = txt2Float(txtVenta3.getText());
		String pReserva = comboReserva.getText();
		Boolean pHotelAEP = bHotelAEP.getSelection();
		Boolean pModificable = comboModificable.getText().equals("Si") ? true : false;
		String pTour = comboTour.getText();
		String pEstado = cdKeyword.getCodeByName(comboStatus.getItem(comboStatus.getSelectionIndex()));
		String pComentario = txtComentario.getText();
		Date pFechaMod = new Date();
		// pasamos los valores al registro a enviar al persistence layer
		registro.setIdTipo(pIdTipo);
		registro.setDescripcion(pDescripcion.trim());
		registro.setCosto(pCosto);
		registro.setVenta0(pVenta0);
		registro.setVenta1(pVenta1);
		registro.setVenta2(pVenta2);
		registro.setVenta3(pVenta3);
		registro.setTipoReserva(pReserva);
		registro.setHotelAEP(pHotelAEP);
		registro.setModificable(pModificable);
		registro.setTour(pTour.equals("Si") ? true : false);
		registro.setIsTour(pTour);
		registro.setEstado(pEstado);
		registro.setComentario(pComentario);
		registro.setFechaModificacion(pFechaMod);
		
		editorController.doSave(registro);
		if (isNewDoc) {
			txtProducto.setText(valor2Txt(registro.getIdProducto()));
			isNewDoc = false;
		}
		
		this.setPartName(registro.getTituloDocumento());
		((CommonEditorInput) this.getEditorInput()).setName(registro.getTituloDocumento());
// actualizamos la vista de productos para reflejar el nuevo producto
		actualizarVista(ProductosView.ID);
		removeDirtyFlag();
		MessageDialog.openInformation(getSite().getShell(), "Información", "El registro ha sido guardado exitosamente");
	}
	
	private boolean validarSave() {
		String pCosto = txtCosto.getText();
		String pModificable = comboModificable.getText();
		String pMinimoVenta = txtVenta0.getText();
		String pDescripcion = txtDescripcion.getText();
		String pComentarios = txtComentario.getText();
		
		if (comboCategoria.getSelectionIndex() == -1) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El campo de \"Tipo de producto\" no puede quedar en blanco");
			return false;
		}
		if (pCosto.length() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El campo de \"Precio costo\" no puede quedar en blanco");
			return false;
		}
		if (pModificable.equals("Si") && pMinimoVenta.length() == 0) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El campo de \"Mínimo de venta\" no puede quedar en blanco");
			return false;
		}
		if (pDescripcion.length() > 45) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El nombre del producto no puede superar los 45 caracteres (" + pDescripcion.length() + ").");
			return false;
		}
		if (pComentarios.length() > 200) {
			MessageDialog.openInformation(getSite().getShell(), "Validación de campos",
					"El texto de los comentarios no puede superar los 200 caracteres (" + pComentarios.length() + ").");
			return false;
		}
		return true;
	}
	
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		gridLayout.marginTop = 12;
		gridLayout.marginLeft = 10;
		parent.setLayout(gridLayout);
		
		agregarControles(parent);
		llenarControles();
	}

	protected void agregarControles(Composite parent) {
		GridData gridData;
		
		Label l = new Label(parent, SWT.None);
		l.setText("Tipo prod:");
		comboCategoria = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		comboCategoria.setLayoutData(gridData);
		cdTipoProducto = cdController.getComboDataTipoProductos();
		comboCategoria.setItems(cdTipoProducto.getTexto());
		comboCategoria.addModifyListener(this.createModifyListener());

		l = new Label(parent, SWT.None);
		l.setText("Cód. producto:");
		txtProducto = new Text(parent, SWT.BORDER);
		gridData = new GridData(40, 15);
		gridData.horizontalSpan = 2;
		txtProducto.setLayoutData(gridData);
		txtProducto.setEditable(false);
		txtProducto.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Status:");
		cdKeyword = cdController.getComboDataKeyword("Status general");
		comboStatus = new Combo(parent, SWT.READ_ONLY);
		comboStatus.setItems(cdKeyword.getTexto());
		comboStatus.select(0);
		comboStatus.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.None);
		l.setText("Descripción:");
		txtDescripcion = new Text(parent, SWT.BORDER);
		gridData = new GridData(200,15);
		gridData.horizontalSpan = 2;
		txtDescripcion.setLayoutData(gridData);
		txtDescripcion.addModifyListener(this.createModifyListener());
		
		l= new Label(parent, SWT.NONE);
		l.setText("Es tour?:");
		comboTour = new Combo(parent, SWT.READ_ONLY);
		comboTour.setItems(new String[] {"Si","No"});
		comboTour.select(1);
		comboTour.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.None);
		l.setText("Precio costo:");
		txtCosto = new Text(parent, SWT.BORDER);
		gridData = new GridData(45,15);
		gridData.horizontalSpan = 2;
		txtCosto.setLayoutData(gridData);
		txtCosto.setTextLimit(9);
		txtCosto.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Precio público:");
		txtVenta1 = new Text(parent, SWT.BORDER);
		txtVenta1.setLayoutData(new GridData(45,15));
		txtVenta1.setTextLimit(9);
		txtVenta1.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.None);
		l.setText("Precio operador:");
		txtVenta2 = new Text(parent, SWT.BORDER);
		gridData = new GridData(45,15);
		gridData.horizontalSpan = 2;
		txtVenta2.setLayoutData(gridData);
		txtVenta2.setTextLimit(9);
		txtVenta2.addModifyListener(this.createModifyListener());
		
		l = new Label(parent, SWT.NONE);
		l.setText("Precio comisionable:");
		txtVenta3 = new Text(parent, SWT.BORDER);
		txtVenta3.setLayoutData(new GridData(45,15));
		txtVenta3.setTextLimit(9);
		txtVenta3.addModifyListener(this.createModifyListener());
		
		/*
		l = new Label(parent, SWT.NONE);
		l.setText("Mínimo de venta:");
		txtVenta0 = new Text(parent, SWT.BORDER);
		gridData = new GridData(45,15);
		gridData.horizontalSpan = 1;
		txtVenta0.setLayoutData(gridData);
		txtVenta0.setTextLimit(9);
		txtVenta0.addModifyListener(this.createModifyListener());
		
		bModificable = new Button(parent, SWT.CHECK);
		bModificable.setText("Modificable x ventas");
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gridData.horizontalIndent = 15;
		bModificable.setLayoutData(gridData);
		bModificable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (filled) {
					addDirtyFlag();
				}
			}
		});
		*/
		
		
		l = new Label(parent, SWT.NONE);
		l.setText("Precio modificable:");
		comboModificable = new Combo(parent, SWT.READ_ONLY);
		comboModificable.setItems(new String[] {"Si" , "No"});
		comboModificable.select(1);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		comboModificable.setLayoutData(gridData);
		comboModificable.addModifyListener(this.createModifyListener());
		comboModificable.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (comboModificable.getText().equals("Si")) {
					txtVenta0.setEnabled(true);
					labelModificable.setEnabled(true);
				} else {
					txtVenta0.setText("");
					txtVenta0.setEnabled(false);
					labelModificable.setEnabled(false);
				}
			}
		});

		labelModificable = new Label(parent, SWT.NONE);
		labelModificable.setText("Mínimo de venta:");
		labelModificable.setEnabled(false);
		txtVenta0 = new Text(parent, SWT.BORDER);
		gridData = new GridData(45,15);
		gridData.horizontalSpan = 1;
		txtVenta0.setLayoutData(gridData);
		txtVenta0.setTextLimit(9);
		txtVenta0.setEnabled(false);
		txtVenta0.addModifyListener(this.createModifyListener());
		
		
		l = new Label(parent, SWT.NONE);
		l.setText("Tipo reserva:");
		comboReserva = new Combo(parent, SWT.READ_ONLY);
		comboReserva.setItems(new String[] {"No aplica", "Alimentación", "Boletos", "Bote", "Hospedaje", "Transporte", "Tour", "Vuelo"});
		comboReserva.select(0);
		comboReserva.addModifyListener(this.createModifyListener());
		comboReserva.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				System.out.println("ModifyListener: ComboReserva");
				if (comboReserva.getText().equals("Hospedaje")) {
					bHotelAEP.setEnabled(true);
				} else {
					bHotelAEP.setEnabled(false);
					bHotelAEP.setSelection(false);
				}
			}
		});
		
		bHotelAEP = new Button(parent, SWT.CHECK);
		bHotelAEP.setText("Hotel de AEP");
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gridData.horizontalIndent = 15;
		bHotelAEP.setLayoutData(gridData);
		bHotelAEP.setEnabled(false);
		bHotelAEP.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (filled) {
					addDirtyFlag();
				}
			}
		});
		
		l = new Label(parent, SWT.NONE);
		l.setText("Impuesto:");
		l.setVisible(false);
		comboImpuesto = new Combo(parent, SWT.READ_ONLY);
		comboImpuesto.setItems(new String[] {"ITBMS", "Hospedaje", "No aplica"});
		comboImpuesto.setText("No aplica");
		comboImpuesto.setVisible(false);
		
		l = new Label(parent, SWT.NONE);
		gridData = new GridData(SWT.DEFAULT, SWT.TOP, false, false);
		l.setLayoutData(gridData);
		l.setText("Comentarios:");
		txtComentario = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gridData.heightHint = 55;
		txtComentario.setLayoutData(gridData);
		txtComentario.addModifyListener(this.createModifyListener());
	}

	protected void llenarControles() {
		isNewDoc = getEditorInput().isNewDoc;
		if (isNewDoc) {
			System.out.println("Creando nuevo producto...");
			registro = new Producto();
			this.setPartName(registro.getTituloDocumento());
			getEditorInput().setName(registro.getTituloDocumento());
		} else {
			registro = (Producto) ((CommonEditorInput) this.getEditorInput()).getElemento();
			//editorController.getSession().refresh(registro);
			// Se copian los datos del producto existente
			Long idTipo = registro.getIdTipo();
			String tipoProd = cdTipoProducto.getTextoByKey(idTipo);
			if (tipoProd == null) {
				MessageDialog.openWarning(getSite().getShell(), "Información",
				"No se encontró el tipo de producto " + idTipo);
			} else {
				comboCategoria.select(comboCategoria.indexOf(tipoProd));
			};
			txtProducto.setText(valor2Txt(registro.getIdProducto()));
			comboStatus.select(comboStatus.indexOf(registro.getDspEstado()));
			txtDescripcion.setText(valor2Txt(registro.getDescripcion()));
			comboTour.select(comboTour.indexOf(registro.getIsTour()));
			txtCosto.setText(valor2Txt(registro.getCosto(), "0.00"));
			txtVenta0.setText(valor2Txt(registro.getVenta0(), "0.00"));
			txtVenta1.setText(valor2Txt(registro.getVenta1(), "0.00"));
			txtVenta2.setText(valor2Txt(registro.getVenta2(), "0.00"));
			txtVenta3.setText(valor2Txt(registro.getVenta3(), "0.00"));
			String modificable = (checkNull(registro.isModificable()) ? "Si" : "No");
			comboModificable.setText(modificable);
			comboReserva.select(comboReserva.indexOf(registro.getTipoReserva()));
			bHotelAEP.setSelection(checkNull(registro.isHotelAEP()));
			txtComentario.setText(valor2Txt(registro.getComentario()));
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
