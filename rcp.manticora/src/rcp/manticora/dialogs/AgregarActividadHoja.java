package rcp.manticora.dialogs;



import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.LineaActividad;
import rcp.manticora.model.Producto;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.Productos;

public class AgregarActividadHoja extends AbstractAEPTitleAreaDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private ComboDataController cdController;
	private Productos productos;
	private Text txtFecha;
	private Button bFecha;
	private Text txtHora;
	private Combo comboTipo;
	private Combo comboProducto;
	private Text txtComentario;
	private Combo cComidas;
	private Button bBreakfast;
	private Button bLunch;
	private Button bDinner;
	private Button bCocktail;
	private Button bOtros;

	private Composite grupoMeal;
	
	private ImageDescriptor image;
	
	private String fechaDefault;
	private LineaActividad linea;
	private ComboData cdTipo;
	private Shell shell;
	
	public AgregarActividadHoja(Shell parentShell, LineaActividad linea) {
		super(parentShell);
		this.shell = parentShell;
		cdController = new ComboDataController();
		productos = new Productos();
		cdTipo = cdController.getComboDataTipoProductos();
		this.linea = linea;
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
		txtFecha.setTextLimit(10);
		//txtFecha.addKeyListener(this.crearKeyAdapter(txtFecha));
		
		bFecha = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFecha));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Hora:");
		txtHora = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		//gridData.horizontalSpan = 2;
		txtHora.setLayoutData(gridData);
		txtHora.setTextLimit(8);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 2;
		comboTipo.setLayoutData(gridData);
		comboTipo.setItems(cdTipo.getTexto());
		comboTipo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int indice = comboTipo.getSelectionIndex();
				if (indice != -1) {
					long seleccionado = cdTipo.getKeyAsLongByIndex(indice);
					productos.filtrarByTipo(seleccionado, true);       // eliminamos cualquier filtro previo
				}
				comboProducto.setItems(productos.getTexto());
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Actividad:");
		comboProducto = new Combo(composite, SWT.READ_ONLY);
		comboProducto.setLayoutData(new GridData(120,15));
		comboProducto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// actualizar campo de disponibilidad
			}
		});
		
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
		
// *********************** Sección de alimentación ****************************
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 10;
		gridData.horizontalSpan = 5;
		l.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Alimentación:");
		gridData = new GridData();
		l.setLayoutData(gridData);
		
		cComidas = new Combo(composite, SWT.READ_ONLY);
		cComidas.setItems(new String[] {"Sin comidas", "Se ofrecen comidas", "No aplica"});
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		cComidas.setLayoutData(gridData);
		cComidas.select(0);
		cComidas.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				toggleComidas();
			}
		});
		
		//Group grupoMeal = new Group(composite, SWT.NONE);
		grupoMeal = new Composite(composite, SWT.NONE);
		layout = new GridLayout(2, true);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.horizontalSpan = 2;
		gridData.verticalSpan = 3;
		grupoMeal.setLayout(layout);
		grupoMeal.setLayoutData(gridData);
		
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
		
		
		llenarCampos();
		
		return composite;
	}
	
	
	private String obtenerTipoReserva() {
		Long pIdProducto = productos.getIdProductoByIndex(comboProducto.getSelectionIndex());
		Producto p = productos.getProductoByIdProducto(pIdProducto);
		return p.getTipoReserva();
	}
	
	
	protected void llenarCampos() {
// Set the title
		setTitle("Agregar actividad");	
// Set the message
		setMessage("Por favor, introduzca los detalles de la actividad", IMessageProvider.INFORMATION);
		String texto;
		//if (linea.getDspProducto() != null) {
		if (linea.getProducto() != null) {
			//texto = new SimpleDateFormat("dd-MM-yyyy").format(linea.getFecha());
			texto = FechaUtil.toString(linea.getFecha(), FechaUtil.formatoFechaHora);
			txtFecha.setText(texto.substring(0,10));
			if (linea.getHora() != null) {
				txtHora.setText(texto.substring(11));
			}
			// combobox de tipo de productos
			texto = productos.getProductoByIdProducto(linea.getIdProducto()).getDspTipo();
			comboTipo.setText(texto);
			long idTipo = cdTipo.getKeyAsLongByTexto(texto);
			System.out.println("idTipo: " + idTipo);
			// combobox de productos
			productos.filtrarByTipo(idTipo, true);
			comboProducto.setItems(productos.getTexto());
			comboProducto.setText(linea.getDspProducto());
			txtComentario.setText(linea.getComentario());
			if (linea.hasComidas()) {
				cComidas.setText("Se ofrecen comidas");
				bBreakfast.setSelection(linea.hasBreakfast());
				bLunch.setSelection(linea.hasLunch());
				bDinner.setSelection(linea.hasDinner());
				bCocktail.setSelection(linea.hasWelcomeCocktail());
				bOtros.setSelection(linea.hasOther());
			} else if (linea.getComidas() == null || linea.getComidas().equals("")) {
				cComidas.setText("No aplica");
			} else if (linea.getComidas().equals("X")) {
				// la X implica que aunque la actividad puede tener comidas, no se ofrece por algún motivo
				cComidas.setText("Sin comidas");
			}
		} else {
			txtFecha.setText(fechaDefault);
			cComidas.setText("No aplica");
		}
		toggleComidas();
	}
	
	
	private void guardarLineaActividad() {
		int pos;
		Long seleccion;
		pos = comboTipo.getSelectionIndex();
		// obtenemos la posición del elemento seleccionado
		pos = comboProducto.getSelectionIndex();
		// obtenemos el código del producto seleccionado
		seleccion = productos.getIdProductoByIndex(pos);
		Producto pProducto = productos.getProductoByIdProducto(seleccion);
		System.out.println("Pos: " + pos + ",  seleccion: " + seleccion);

		//linea.setIdProducto(seleccion);      // comentado x linea.setProducto()
		linea.setProducto(pProducto);
		//linea.setDspProducto(comboProducto.getText());
		if (txtHora.getText().equals("")) {
			linea.setFecha(FechaUtil.toDate(txtFecha.getText()));
			linea.setHora(null);
		} else {
			linea.setFecha(FechaUtil.toDateHour(txtFecha.getText() + " " + txtHora.getText()));
			linea.setHora(FechaUtil.toHour(txtHora.getText()));
		}
		//linea.setBreakfast(bBreakfast.getSelection());
		//linea.setLunch(bLunch.getSelection());
		//linea.setDinner(bDinner.getSelection());
		linea.setComidas(definirComidas());
		linea.setTipoReserva(obtenerTipoReserva());
		linea.setComentario(txtComentario.getText());
	}
	
	public boolean validarSave() {
		String hora, hh, mm, aa;
		hora = txtHora.getText().trim();
		if (!hora.equals("")) {
			if (FechaUtil.toHour(hora) == null) {
				MessageDialog.openInformation(shell, "Validación de campos",
				"El formato de hora debe ser hh:mm aa (Ejemplo: \"11:45 AM\").");
				return false;
			}
			hh = hora.substring(0,hora.indexOf(":"));
			mm = hora.substring(3,hora.indexOf(" "));
			aa = hora.substring(hora.indexOf(" ") + 1);
			System.out.println(hh + "-" + mm + "-" + aa);
		}
		return true;
	}
	
	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			try {
				if (validarSave()) {
					guardarLineaActividad();
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
	
	
	public void setFechaDefault(String fechaDefault) {
		this.fechaDefault = fechaDefault == null ? "" : fechaDefault;
	}
	
	
	private void toggleComidas() {
		if (cComidas.getText().equals("Se ofrecen comidas")) {
			bBreakfast.setEnabled(true);
			bCocktail.setEnabled(true);
			bLunch.setEnabled(true);
			bOtros.setEnabled(true);
			bDinner.setEnabled(true);
		} else {
			bBreakfast.setSelection(false);
			bCocktail.setSelection(false);
			bLunch.setSelection(false);
			bOtros.setSelection(false);
			bDinner.setSelection(false);
			bBreakfast.setEnabled(false);
			bCocktail.setEnabled(false);
			bLunch.setEnabled(false);
			bOtros.setEnabled(false);
			bDinner.setEnabled(false);
		}
	}
	
	
	private String definirComidas() {
		String comidas = "";
		if (cComidas.getText().equals("Sin comidas")) {
			comidas = "X";
		} else {
			comidas += bBreakfast.getSelection() ? "B" : "";
			comidas += bLunch.getSelection() ? "L" : "";
			comidas += bDinner.getSelection() ? "D" : "";
			comidas += bCocktail.getSelection() ? "W" : "";
			comidas += bOtros.getSelection() ? "O" : "";
		}
		return comidas;
	}
	
}

