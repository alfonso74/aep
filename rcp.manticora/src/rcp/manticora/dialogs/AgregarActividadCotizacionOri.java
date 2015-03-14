package rcp.manticora.dialogs;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Set;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
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
import rcp.manticora.services.FormUtils;
import rcp.manticora.services.ICalendarUtils;
import rcp.manticora.services.Productos;

public class AgregarActividadCotizacionOri extends TitleAreaDialog implements ICalendarUtils {
	private final String pluginId = Application.PLUGIN_ID;
	private ComboDataController cdController;
	private FormUtils formUtils;
	private Productos productos;
	private Text txtFecha;
	private Button bFecha;
	//private Text txtHora;
	private Text txtDias;
	private Combo comboTipo;
	private Combo comboProducto;
	private List listaTipoPrecio;
	private Text txtPrecio;
	private Text txtComentario;
	/*
	private Combo cComidas;
	private Button bBreakfast;
	private Button bLunch;
	private Button bDinner;
	private Button bCocktail;
	private Button bOtros;
	*/
	
	//private Group grupoMeal;
	private Composite grupoMeal;
	
	private ImageDescriptor image;
	
	private String fechaDefault;
	private LineaCotizacion linea;
	private Set<LineaCotizacion> lineas;
	private ComboData cdTipoProductos;
	private Shell shell;

	/*public AgregarActividadCotizacion(Shell parentShell) {
	 super(parentShell);
	 this.shell = parentShell;
	 controller = HibernateController.getInstance();
	 productos = new Productos();
	 linea = new LineaCotizacion();
	 }*/
	
	// constructor utilizado para crear líneas
	public AgregarActividadCotizacionOri(Shell parentShell, LineaCotizacion linea) {
		super(parentShell);
		this.shell = parentShell;
		cdController = new ComboDataController();
		productos = new Productos();
		this.linea = linea;
		formUtils = new FormUtils();
	}
	
	// constructor utilizado para editar líneas
	public AgregarActividadCotizacionOri(Shell parentShell, Set<LineaCotizacion> lineas) {
		super(parentShell);
		this.shell = parentShell;
		cdController = new ComboDataController();
		productos = new Productos();
		this.lineas = lineas;
		formUtils = new FormUtils();
	}
	
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		
		// Set the title
		setTitle("Agregar actividad");
		
		// Set the message
		setMessage("Por favor, introduzca los detalles de la actividad", IMessageProvider.INFORMATION);
		
		return contents;
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
		txtFecha.addKeyListener(this.crearKeyAdapter(txtFecha));
		
		bFecha = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFecha));
		/*
		l = new Label(composite, SWT.NONE);
		l.setText("Hora:");
		l.setVisible(false);
		txtHora = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		//gridData.horizontalSpan = 2;
		txtHora.setLayoutData(gridData);
		txtHora.setVisible(false);
		*/
		final Label labelNoches = new Label(composite, SWT.NONE);
		labelNoches.setText("Cantidad:");
		txtDias = new Text(composite, SWT.BORDER);
		gridData = new GridData(25,15);
		//gridData.horizontalSpan = 2;
		txtDias.setLayoutData(gridData);
		
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
					long seleccionado = cdTipoProductos.getKeyAsLongByIndex(indice);
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
		comboProducto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Long idProducto = productos.getIdProductoByName(comboProducto.getText());
				Producto p = productos.getProductoByIdProducto(idProducto);
				String tipoReserva = p.getTipoReserva();
				if (tipoReserva.equals("Hotel")) {
					labelNoches.setText("Noches:");
					labelNoches.pack();
					labelNoches.redraw();
				} else {
					labelNoches.setText("Cantidad:");
					labelNoches.pack();
					labelNoches.redraw();
				}
				listaTipoPrecio.setEnabled(true);
				listaTipoPrecio.deselectAll();
				txtPrecio.setText("");
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo de precio:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
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
		l.setLayoutData(gridData);
		txtPrecio = new Text(composite, SWT.BORDER);
		txtPrecio.setEditable(false);
		gridData = new GridData(50,15);
		gridData.verticalAlignment = SWT.TOP;
		//gridData.horizontalSpan = 2;
		txtPrecio.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Comentarios:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);
		txtComentario = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.horizontalSpan = 4;
		gridData.heightHint = 40;
		txtComentario.setLayoutData(gridData);
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 10;
		gridData.horizontalSpan = 5;
		l.setLayoutData(gridData);
		/*
		l = new Label(composite, SWT.NONE);
		l.setText("Alimentación:");
		gridData = new GridData();
		l.setLayoutData(gridData);
		
		cComidas = new Combo(composite, SWT.READ_ONLY);
		cComidas.setItems(new String[] {"Sin comidas", "Se ofrecen comidas", "No aplica"});
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		cComidas.setLayoutData(gridData);
		cComidas.select(1);
		cComidas.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (cComidas.getSelectionIndex() == 1) {
					bBreakfast.setEnabled(true);
					bCocktail.setEnabled(true);
					bLunch.setEnabled(true);
					bOtros.setEnabled(true);
					bDinner.setEnabled(true);
				} else {
					bBreakfast.setEnabled(false);
					bCocktail.setEnabled(false);
					bLunch.setEnabled(false);
					bOtros.setEnabled(false);
					bDinner.setEnabled(false);
				}
			}
		});
		*/
		
		//Group grupoMeal = new Group(composite, SWT.NONE);
		grupoMeal = new Composite(composite, SWT.NONE);
		layout = new GridLayout(2, true);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.horizontalSpan = 2;
		gridData.verticalSpan = 3;
		grupoMeal.setLayout(layout);
		grupoMeal.setLayoutData(gridData);
		/*
		bBreakfast = new Button(grupoMeal, SWT.CHECK);
		bBreakfast.setText("Desayuno");
		bCocktail = new Button(grupoMeal, SWT.CHECK);
		bCocktail.setText("Welcome cocktail");
		bLunch = new Button(grupoMeal, SWT.CHECK);
		bLunch.setText("Almuerzo");
		bOtros = new Button(grupoMeal, SWT.CHECK);
		bOtros.setText("Otros");
		bDinner = new Button(grupoMeal, SWT.CHECK);
		bDinner.setText("Cena");
		*/
		
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
			txtPrecio.setText(new DecimalFormat("0.00").format(precio));
		}
	}
	
	private void llenarCampos() {
		String texto;
		if (lineas.isEmpty()) {    // estamos creando una nueva línea
			txtFecha.setText(fechaDefault);
			txtDias.setText("1");
			listaTipoPrecio.setEnabled(false);
			txtPrecio.setText("");
		} else {  // estamos modificando
			linea = lineas.iterator().next();
			texto = FechaUtil.toString(linea.getFecha());
			txtFecha.setText(texto);
			txtDias.setText("1");
			// combobox de tipo de productos
			texto = productos.getProductoByIdProducto(linea.getIdProducto()).getDspTipo();
			comboTipo.setText(texto);
			long idTipo = cdTipoProductos.getKeyAsLongByTexto(texto);
			System.out.println("idTipo: " + idTipo);
			// combobox de productos
			productos.filtrarByTipo(idTipo, true);
			comboProducto.setItems(productos.getTexto());
			comboProducto.setText(linea.getDspProducto());
			texto = new DecimalFormat("0.00").format(linea.getPrecio());
			txtPrecio.setText(texto);
			txtComentario.setText(linea.getComentario());
		}
	}
	
	private void guardarLineaActividad() {
		int pos;
		Long seleccion;
		int noDias;
		pos = comboTipo.getSelectionIndex();
		// obtenemos la posición del elemento seleccionado
		pos = comboProducto.getSelectionIndex();
		// obtenemos el código del producto seleccionado
		seleccion = productos.getIdProductoByIndex(pos);
		System.out.println("Pos: " + pos + ",  seleccion: " + seleccion);
		noDias = Integer.parseInt(txtDias.getText());
		Date fechaBase = FechaUtil.toDate(txtFecha.getText());
		Date fechaLinea = null;
		System.out.println("Fecha1: " + FechaUtil.toString(fechaBase, FechaUtil.formatoFechaHora));
		
		for (int n=0; n < noDias; n++) {
			fechaLinea = FechaUtil.ajustarFecha(fechaBase, n);
			if (n == 0 && !lineas.isEmpty()) {
// si iniciamos el ciclo y la lista de líneas tiene información, entonces estamos editando
				System.out.println("Editando línea de cotización...");
			} else {
				linea = new LineaCotizacion();
			}
			linea.setIdProducto(seleccion);
			linea.setDspProducto(comboProducto.getText());
			linea.setFecha(fechaLinea);
			linea.setPrecio(Float.parseFloat(txtPrecio.getText()));
			linea.setComentario(txtComentario.getText());
			lineas.add(linea);
		}
	}
	
	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			guardarLineaActividad();  
		};
		return super.close();
	}
	
	
	/**
	 * Creates the buttons for the button bar
	 * 
	 * @param parent the parent composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	protected void buttonPressed(int buttonId) {
		setReturnCode(buttonId);
		close();
	}
	
	public void setFechaDefault(String fechaDefault) {
		this.fechaDefault = fechaDefault == null ? "" : fechaDefault;
	}
	
	public SelectionAdapter crearCalendario(Shell shell, Text txtCampo) {
		//return new Calendario(shell, txtCampo);
		return formUtils.crearCalendario(shell, txtCampo);
	}

	public SelectionAdapter crearCalendario(Shell shell, Text txtCampo, Text txtFechaDefault) {
		return formUtils.crearCalendario(shell, txtCampo, txtFechaDefault);
	}

	public KeyAdapter crearKeyAdapter(Text txtCampo) {
		return formUtils.crearKeyAdapter(txtCampo);
	}
}
