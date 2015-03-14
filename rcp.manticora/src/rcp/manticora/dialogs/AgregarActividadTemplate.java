package rcp.manticora.dialogs;



import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
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

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.LineaTemplate;
import rcp.manticora.model.Producto;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.Productos;

public class AgregarActividadTemplate extends AbstractAEPTitleAreaDialog {
	private ComboDataController cdController;
	private Productos productos;
	private Combo comboTipo;
	private Combo comboProducto;
	private Combo comboReserva;
	private Text txtDia;
	private Text txtSecuencia;
	private Text txtCantidad;
	private Text txtComentario;
	
	private Combo comboComidas;
	private Button bBreakfast;
	private Button bCocktail;
	private Button bLunch;
	private Button bOtros;
	private Button bDinner;
	
	private ComboData comboData;
	private LineaTemplate linea;
	private String diaDefault;
	private String seqDefault;
	private boolean isNewLinea = false;
	
	private Shell shell;
	
	/**
	 * Agrega o modifica una línea de template
	 * @param parentShell el shell utilizado para dibujar el diálogo
	 * @param linea si es null, se crea una nueva línea para el template,
	 *              en caso contrario se editan los contenidos de la línea
	 *              pasada como parámetro.
	 */
	public AgregarActividadTemplate(Shell parentShell, LineaTemplate linea) {
		super(parentShell);
		this.shell = parentShell;
		cdController = new ComboDataController();
		productos = new Productos();
		if (linea == null) {
			// si se pasó null como parámetro para LineaTemplate, para tener 
			// acceso al objeto linea fuera del diálogo debemos usar getLinea() 
			linea = new LineaTemplate();
			isNewLinea = true;
		}
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
		
		GridLayout layout = new GridLayout(6, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		comboTipo.setLayoutData(gridData);
		comboData = cdController.getComboDataTipoProductos();
		comboTipo.setItems(comboData.getTexto());
		comboTipo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int indice = comboTipo.getSelectionIndex();
				if (indice != -1) {
					long seleccionado = comboData.getKeyAsLongByIndex(indice);
					productos.filtrarByTipo(seleccionado, true);       // eliminamos cualquier filtro previo
				}
				comboProducto.setItems(productos.getTexto());
				comboReserva.select(0);
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Actividad:");
		comboProducto = new Combo(composite, SWT.READ_ONLY);
		comboProducto.setLayoutData(new GridData(120,15));
		comboProducto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// obtenemos el código del producto seleccionado
				Long idProducto = productos.getIdProductoByIndex(comboProducto.getSelectionIndex());
				Producto p = productos.getProductoByIdProducto(idProducto);
				comboReserva.setText(p.getTipoReserva());
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Día:");
		txtDia = new Text(composite, SWT.BORDER);
		gridData = new GridData(35, 15);
		txtDia.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Secuencia:");
		l.setAlignment(SWT.RIGHT);
		gridData = new GridData(70, 15);
		l.setLayoutData(gridData);
		txtSecuencia = new Text(composite, SWT.BORDER);
		gridData = new GridData(35, 15);
		txtSecuencia.setLayoutData(gridData);
		txtSecuencia.setEnabled(false);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Cantidad:");
		txtCantidad = new Text(composite, SWT.BORDER);
		gridData = new GridData(35, 15);
		txtCantidad.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Reserva:");
		comboReserva = new Combo(composite, SWT.READ_ONLY);
		comboReserva.setItems(new String[] {"No aplica", "Alimentación", "Bote", "Hospedaje", "Transporte", "Ticket", "Tour", "Vuelo"});
		comboReserva.select(0);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		comboReserva.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Comentarios:");
		//Text txtIdent = new Text(composite, SWT.BORDER);
		//gridData = new GridData(250,15);
		txtComentario = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 2);
		gridData.heightHint = 40;
		txtComentario.setLayoutData(gridData);
		
		
// *********************** Sección de alimentación ****************************
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 10;
		gridData.horizontalSpan = 6;
		l.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Alimentación:");
		gridData = new GridData();
		l.setLayoutData(gridData);
		
		comboComidas = new Combo(composite, SWT.READ_ONLY);
		comboComidas.setItems(new String[] {"Sin comidas", "Se ofrecen comidas", "No aplica"});
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		comboComidas.setLayoutData(gridData);
		comboComidas.select(1);
		comboComidas.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (comboComidas.getSelectionIndex() == 1) {
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
		});
		
		Composite grupoMeal = new Composite(composite, SWT.NONE);
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
	
	
	protected void llenarCampos() {
// Set the title
		setTitle("Agregar actividad");		
// Set the message
		setMessage("Por favor, introduzca los detalles de la actividad", IMessageProvider.INFORMATION);
		String texto;
		if (isNewLinea) {
			diaDefault = "1";
			seqDefault = "999";
			txtDia.setText(diaDefault);
			txtSecuencia.setText(seqDefault);
			txtCantidad.setText("1");
		} else {
			try {
				diaDefault = valor2Txt(linea.getDia());
				seqDefault = valor2Txt(linea.getSecuencia());
				texto = productos.getProductoByIdProducto(linea.getIdProducto()).getDspTipo();
				comboTipo.setText(texto);
				long idTipo = comboData.getKeyAsLongByTexto(texto);
				System.out.println("idTipo: " + idTipo);
				productos.filtrarByTipo(idTipo, true);
				comboProducto.setItems(productos.getTexto());
				comboProducto.setText(linea.getDspProducto());
				comboReserva.setText(linea.getTipoReserva());
				txtDia.setText(diaDefault);
				txtSecuencia.setText(seqDefault);
				txtCantidad.setText(valor2Txt(linea.getCantidad()));
				bBreakfast.setSelection(linea.isBreakfast());
				bLunch.setSelection(linea.isLunch());
				bDinner.setSelection(linea.isDinner());
				txtComentario.setText(linea.getComentario());
			} catch(Exception e) {
				mensajeError(shell, "Inicialización de campos", e);
			}
		}
	}
	
	
	private void guardarLineaTemplate() {
		int pos;
		Long seleccion;
		pos = comboTipo.getSelectionIndex();
		// obtenemos la posición del elemento seleccionado
		pos = comboProducto.getSelectionIndex();
		// obtenemos el código del producto seleccionado
		seleccion = productos.getIdProductoByIndex(pos);
		Producto pProducto = productos.getProductoByIdProducto(seleccion);
		//linea.setIdProducto(seleccion);
		//linea.setDspProducto(comboProducto.getText());
		linea.setProducto(pProducto);
		linea.setDia(txt2Integer(txtDia.getText()));
		linea.setSecuencia(txt2Integer(asignarSecuencia()));
		linea.setCantidad(txt2Integer(txtCantidad.getText()));
		linea.setComentario(txtComentario.getText());
		linea.setTipoReserva(comboReserva.getItem(comboReserva.getSelectionIndex()));
		System.out.println("Comidas: " + definirComidas());
		linea.setComidas(definirComidas());
	}
	
	private String asignarSecuencia() {
		// si no es una nueva línea, y si el día no ha sido modificado por el
		// usuario final, retorna la misma seq del diálogo.  Si no, 999.
		if (!isNewLinea && txtDia.getText().equals(diaDefault)) {
			return txtSecuencia.getText();
		} else {
			return "999";
		}
	}
	
	private String definirComidas() {
		String comidas = "";
		comidas += bBreakfast.getSelection() ? "B" : "";
		comidas += bLunch.getSelection() ? "L" : "";
		comidas += bDinner.getSelection() ? "D" : "";
		return comidas;
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
		return true;
	}
	
	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			if (validarSave()) {
				//guardarLineaTemplate();
				
				try {
					guardarLineaTemplate();
				} catch (Exception e) {
					mensajeError(shell, "Guardando línea", e);
				}
				
			} else {
				return false;
			}
		};
		return super.close();
	}
	
	// si el parámetro linea pasa como null al constructor debemos usar este
	// método para obtener la nueva línea (LineaTemplate) 
	public LineaTemplate getLinea() {
		return linea;
	}
}
