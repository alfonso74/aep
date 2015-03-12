package rcp.manticora.dialogs;

import java.util.Date;
import java.util.Set;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.LineaCotizacion;
import rcp.manticora.model.Producto;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.Productos;

public class AgregarActividadCotizacion extends AbstractAEPTitleAreaDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private ComboDataController cdController;
	private Productos productos;
	private Text txtFecha;
	private Button bFecha;
	private Text txtCantidad;
	private Text txtEspacios;
	private Combo comboTipo;
	private Combo comboProducto;
	private List listaTipoPrecio;
	private Text txtPrecio;
	private Button bVisible;
	private Text txtComentario;
	
	private ImageDescriptor image;
	
	private Boolean isRecursoAEP = Boolean.FALSE;
	private Boolean isModificable = Boolean.FALSE;
	private Float precioMinimo;
	
	private String fechaDefault;
	private String espaciosDefault;                 // paxs o rooms
	private LineaCotizacion linea;
	private Set<LineaCotizacion> lineas;
	private ComboData cdTipoProductos;
	private Shell shell;
	
	// constructor utilizado para crear líneas
	/*
	public AgregarActividadCotizacion(Shell parentShell, LineaCotizacion linea) {
		super(parentShell);
		this.shell = parentShell;
		controller = HibernateController.getInstance();
		productos = new Productos();
		this.linea = linea;
	}
	*/
	
	// constructor utilizado para editar líneas
	public AgregarActividadCotizacion(Shell parentShell, Set<LineaCotizacion> lineas) {
		super(parentShell);
		this.shell = parentShell;
		cdController = new ComboDataController();
		productos = new Productos();
		this.lineas = lineas;
	}
	
	
	
	/**
	 * Creates the gray area
	 * 
	 * @param parent the parent composite
	 * @return Control
	 */
	protected Control createDialogArea(Composite parent) {
		//Composite composite = (Composite) super.createDialogArea(parent);
		Composite composite = new Composite(parent, SWT.NULL);
		
		GridLayout layout = new GridLayout(5, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Fecha:");
		txtFecha = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFecha.setLayoutData(gridData);
		//txtFecha.addKeyListener(this.crearKeyAdapter(txtFecha));  // calendar ya incluye esto
		
		bFecha = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFecha));

		final Label labelNoches = new Label(composite, SWT.NONE);
		labelNoches.setText("Cantidad:");
		
		Composite compQty = new Composite(composite, SWT.NONE);
		layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		compQty.setLayout(layout);
		
		txtCantidad = new Text(compQty, SWT.BORDER);
		gridData = new GridData(25,15);
		txtCantidad.setLayoutData(gridData);
		
		final Label labelEspacios = new Label(compQty, SWT.NONE);
		labelEspacios.setText("PAX(s):");
		labelEspacios.setAlignment(SWT.RIGHT);
		gridData = new GridData(45, 15);
		gridData.horizontalIndent = 10;
		labelEspacios.setLayoutData(gridData);
		txtEspacios = new Text(compQty, SWT.BORDER);
		txtEspacios.setLayoutData(new GridData(25,15));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.widthHint = 120;
		gridData.horizontalSpan = 2;
		comboTipo.setLayoutData(gridData);
		cdTipoProductos = cdController.getComboDataTipoProductos();
		comboTipo.setItems(cdTipoProductos.getTexto());
		comboTipo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int indice = comboTipo.getSelectionIndex();
				if (indice != -1) {
					Long seleccionado = cdTipoProductos.getKeyAsLongByIndex(indice);
					productos.filtrarByTipo(seleccionado, true);       // eliminamos cualquier filtro previo
				}
				comboProducto.setItems(productos.getTexto());
			}
		});
		
		
		l = new Label(composite, SWT.NONE);
		l.setText("Actividad:");
		comboProducto = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.widthHint = 150;
		comboProducto.setLayoutData(gridData);
		/*
		comboProducto.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("widgetDefSel: " + comboProducto.getText());
			}

			public void widgetSelected(SelectionEvent e) {
				System.out.println("widgetSelected: " + comboProducto.getText());	
			}
		});
		comboProducto.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				System.out.println("modifyText: " + comboProducto.getText());	
			}
		});
		*/
		
		comboProducto.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!comboProducto.getText().equals("")) {
					Long idProducto = productos.getIdProductoByName(comboProducto.getText());
					Producto p = productos.getProductoByIdProducto(idProducto);
					String tipoReserva = p.getTipoReserva();
					isRecursoAEP = p.isHotelAEP();
					isModificable = p.isModificable();
					precioMinimo = p.getPrecioMinimo();
					System.out.println("Minimo: " + precioMinimo);
					if (tipoReserva.equals("Hotel") || tipoReserva.equals("Hospedaje")) {
						labelNoches.setText("Noche(s):");
						labelNoches.pack();
						labelNoches.redraw();
						labelEspacios.setText("Room(s):");
						labelEspacios.pack();
						labelEspacios.redraw();
					} else {
						labelNoches.setText("Cantidad:");
						labelNoches.pack();
						labelNoches.redraw();
						labelEspacios.setText("PAX(s):");
						labelEspacios.setAlignment(SWT.RIGHT);
						labelEspacios.pack();
						labelEspacios.redraw();
					}
					listaTipoPrecio.setEnabled(true);
					listaTipoPrecio.deselectAll();
					txtPrecio.setText("");
					txtPrecio.setEditable(isModificable == null ? false : isModificable.booleanValue());
				}
			}
		});
		
		/*
		comboProducto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int idProducto = productos.getIdProductoByName(comboProducto.getText());
				Producto p = productos.getProductoByIdProducto(idProducto);
				String tipoReserva = p.getTipoReserva();
				if (tipoReserva.equals("Hotel") || tipoReserva.equals("Hospedaje")) {
					labelNoches.setText("Noche(s):");
					labelNoches.pack();
					labelNoches.redraw();
					labelEspacios.setText("Room(s):");
					labelEspacios.pack();
					labelEspacios.redraw();
				} else {
					labelNoches.setText("Cantidad:");
					labelNoches.pack();
					labelNoches.redraw();
					labelEspacios.setText("PAXs:");
					labelEspacios.setAlignment(SWT.RIGHT);
					labelEspacios.pack();
					labelEspacios.redraw();
				}
				listaTipoPrecio.setEnabled(true);
				listaTipoPrecio.deselectAll();
				txtPrecio.setText("");
			}
		});
		*/
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo de precio:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		gridData.verticalIndent = 3;
		l.setLayoutData(gridData);
		gridData = new GridData();
		gridData.widthHint = 75;
		gridData.horizontalSpan = 2;

		listaTipoPrecio = new List(composite, SWT.SINGLE | SWT.BORDER);
		listaTipoPrecio.setLayoutData(gridData);
		listaTipoPrecio.setItems(new String[] {"Comisionable", "Operador", "Público"});
		listaTipoPrecio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				obtenerPrecio(listaTipoPrecio.getSelection()[0]);
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Precio:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		gridData.verticalIndent = 3;
		l.setLayoutData(gridData);
		
		Composite compPrecio = new Composite(composite, SWT.NONE);
		layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		compPrecio.setLayout(layout);
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		compPrecio.setLayoutData(gridData);
		
		txtPrecio = new Text(compPrecio, SWT.BORDER);
		txtPrecio.setEditable(false);
		gridData = new GridData(50,15);
		gridData.verticalAlignment = SWT.TOP;
		txtPrecio.setLayoutData(gridData);

		Button bCuadrar = new Button(compPrecio, SWT.PUSH);
		gridData = new GridData(12,16);
		bCuadrar.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/cuadrar.gif");
		bCuadrar.setImage(image.createImage());
		bCuadrar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de cuadrar...");
				cuadrarCotizacion();
			}
		});	
		
		bVisible = new Button(compPrecio, SWT.CHECK);
		bVisible.setText("Visible");
		gridData = new GridData();
		gridData.horizontalIndent = 15;
		bVisible.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Comentarios:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		gridData.verticalIndent = 3;
		l.setLayoutData(gridData);
		txtComentario = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.horizontalSpan = 4;
		gridData.heightHint = 40;
		txtComentario.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Un \"*\" al inicio del comentario mostrará el mismo en el PDF de la cotización.");
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		l.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 10;
		gridData.horizontalSpan = 5;
		l.setLayoutData(gridData);

		llenarCampos();
		
		return composite;
	}
	
	private void obtenerPrecio(String tipoPrecio) {
		Long pIdProducto = productos.getIdProductoByIndex(comboProducto.getSelectionIndex());
		Producto p = productos.getProductoByIdProducto(pIdProducto);
		Float precio = p.getPrecioByTipo(tipoPrecio);
		if (precio == null) {
			txtPrecio.setText("-1.00");
		} else {
			txtPrecio.setText(valor2Txt(precio, "#,##0.00"));
		}
	}
	
	protected void llenarCampos() {
		setTitle("Agregar actividad");
		setMessage("Por favor, introduzca los detalles de la actividad");
		if (lineas.isEmpty()) {    // estamos creando una nueva línea
			txtFecha.setText(fechaDefault);
			txtCantidad.setText("1");
			txtEspacios.setText(espaciosDefault);
			listaTipoPrecio.setEnabled(false);
			txtPrecio.setText("");
			bVisible.setSelection(true);
		} else {  // estamos modificando
			try {
				String texto;
				linea = lineas.iterator().next();
				System.out.println("PP: " + linea.getIdProducto() + " - " + linea.getProducto().getIdProducto());
				txtFecha.setText(FechaUtil.toString(linea.getFecha()));
				txtCantidad.setText(valor2Txt(linea.getCantidad()));
				txtEspacios.setText(valor2Txt(linea.getEspacios()));
				// combobox de tipo de productos
				texto = productos.getProductoByIdProducto(linea.getIdProducto()).getDspTipo();
				comboTipo.setText(texto);
				long idTipo = cdTipoProductos.getKeyAsLongByTexto(texto);
				System.out.println("idTipo: " + idTipo);
				// combobox de productos
				productos.filtrarByTipo(idTipo, true);
				comboProducto.setItems(productos.getTexto());
				comboProducto.setText(linea.getDspProducto());
				txtPrecio.setText(valor2Txt(linea.getPrecio(), "0.00"));
				bVisible.setSelection(valor2Bool(linea.isVisible()));
				txtComentario.setText(linea.getComentario());
				//txtComentario.setText(linea.getSecuencia().toString());
			} catch (Exception e) {
				mensajeError(shell, "Inicialización de campos", e);
			}
		}
	}
	
	private void guardarLineaActividad() throws Exception {
		int pos;
		Long pIdProducto;
		// obtenemos la posición del elemento seleccionado
		pos = comboProducto.getSelectionIndex();
		// obtenemos el código del producto seleccionado
		pIdProducto = productos.getIdProductoByIndex(pos);
		Producto pProducto = productos.getProductoByIdProducto(pIdProducto);
		System.out.println("Pos: " + pos + ",  seleccion: " + pIdProducto);
		Date fechaBase = FechaUtil.toDate(txtFecha.getText());
		Date pFechaLinea = null;
		System.out.println("Fecha1: " + FechaUtil.toString(fechaBase, FechaUtil.formatoFechaHora));
		Integer pCantidad = txt2Integer(txtCantidad.getText());
		Integer pEspacios = txt2Integer(txtEspacios.getText());
		Float pPrecio = txt2Float(txtPrecio.getText());
		Boolean pVisible = bVisible.getSelection();
		String pComentario = txtComentario.getText();
		
// originalmente se generaban varias líneas a partir este editor, específicamente en
// el caso de hospedajes, pero ahora solo se genera una línea.
		for (int n=0; n < 1; n++) {
			pFechaLinea = FechaUtil.ajustarFecha(fechaBase, n);
			if (n == 0 && !lineas.isEmpty()) {
// si iniciamos el ciclo y la lista de líneas tiene información, entonces estamos editando
				System.out.println("Editando línea de cotización...");
// si al editar, la fecha ha sido modificada, reseteamos la secuencia a 999 para evitar
// conflictos en la reenumeración (que la secuencia coincida en otra fecha).
				if (pFechaLinea.getTime() != linea.getFecha().getTime()) {
					linea.setSecuencia(999);
				}
			} else {
				linea = new LineaCotizacion();
				linea.setSecuencia(999);
			}
			//linea.setIdProducto(pIdProducto);      // eliminada x el setProducto()
			linea.setDspProducto(comboProducto.getText());
			linea.setProducto(pProducto);
			linea.setFecha(pFechaLinea);
			linea.setCantidad(pCantidad);
			linea.setPrecio(pPrecio);
			linea.setEspacios(pEspacios);
			linea.setHotelAEP(isRecursoAEP);
			linea.setVisible(pVisible);
			linea.setComentario(pComentario);
			lineas.add(linea);
		}
	}
	
	private boolean validarSave() {
		if (comboTipo.getSelectionIndex() == -1) {
			MessageDialog.openInformation(shell, "Validación de campos",
					"Debe seleccionar el tipo de la actividad.");
			return false;
		}
		if (comboProducto.getSelectionIndex() == -1) {
			MessageDialog.openInformation(shell, "Validación de campos",
					"Debe seleccionar una actividad.");
			return false;
		}
		Float pPrecio = txt2Float(txtPrecio.getText());
		System.out.println("P1: " + pPrecio + ", p2: " + precioMinimo);
		if (isModificable && pPrecio.floatValue() < precioMinimo.floatValue()) {
			MessageDialog.openInformation(shell, "Validación de campos",
					"El precio introducido no es válido.  El mínimo permitido es "  + valor2Txt(precioMinimo, "#,##0.00") + ".");
			return false;
		}
		return true;
	}
	
	public boolean close() {
		// si el botón presionado es OK validamos, guardamos y cerramos
		if (getReturnCode() == IDialogConstants.OK_ID) {
			try {
				if (validarSave()) {
					guardarLineaActividad();
					return super.close();
				} else {
					return false;
				}
			} catch (Exception e) {
				mensajeError(shell, e);
				return false;
			}
		};
		return super.close();
	}
	
	private void cuadrarCotizacion() {
		CuadrarCotizacionDialog c = new CuadrarCotizacionDialog(shell, this, "Cuadrar precios de cotización");
		c.open();
	}
	
	
	public void setFechaDefault(String fechaDefault) {
		this.fechaDefault = fechaDefault == null ? "" : fechaDefault;
	}
	
	// setea el no. de paxs o rooms (depende de la actividad)
	public void setEspaciosDefault(String espacios) {
		this.espaciosDefault = espacios == null ? "1" : espacios;
	}
	
	public void setTxtPrecio(Float precio) {
		txtPrecio.setText(valor2Txt(precio, "#,##0.00"));
	}
	
	public Text getTxtPrecio() {
		return txtPrecio;
	}
	
	public String getTxtEspacios() {
		return txtEspacios.getText();
	}
}
