package rcp.manticora.dialogs;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
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
import rcp.manticora.model.LineaTour;
import rcp.manticora.model.Producto;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.Productos;

public class AgregarLineaTour extends AbstractAEPTitleAreaDialog {
	private ComboDataController cdController;
	
	private Combo comboTipo;
	private Combo comboProducto;
	private ComboData cdTipo;
	private Productos productos;
	private Text txtDia;
	private Text txtSecuencia;
	private Text txtComentario;
	
	private Combo cComidas;
	private Button bBreakfast;
	private Button bCocktail;
	private Button bLunch;
	private Button bOtros;
	private Button bDinner;
	
	private LineaTour linea;
	private LineaTour actividadBase;

	public AgregarLineaTour(Shell parentShell, LineaTour linea, LineaTour base) {
		super(parentShell);
		cdController = new ComboDataController();
		productos = new Productos();
		if (linea == null) {
			linea = new LineaTour();
		}
		this.linea = linea;
		this.actividadBase = base;
	}

	
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		comboTipo.setLayoutData(new GridData(110, 15));
		cdTipo = cdController.getComboDataTipoProductos();
		comboTipo.setItems(cdTipo.getTexto());
		comboTipo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int indice = comboTipo.getSelectionIndex();
				if (indice != -1) {
					Long seleccionado = cdTipo.getKeyAsLongByIndex(indice);
					productos.filtrarByTipo(seleccionado, true);       // eliminamos cualquier filtro previo
				}
				comboProducto.setItems(productos.getTexto());
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Actividad");
		comboProducto = new Combo(composite, SWT.READ_ONLY);
		comboProducto.setLayoutData(new GridData(125,15));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Día no.:");
		txtDia = new Text(composite, SWT.BORDER);
		txtDia.setLayoutData(new GridData(35, 15));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Secuencia:");
		txtSecuencia = new Text(composite, SWT.BORDER);
		txtSecuencia.setLayoutData(new GridData(35, 15));
		txtSecuencia.setEnabled(false);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Comentarios:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);
		txtComentario = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.horizontalSpan = 3;
		gridData.heightHint = 40;
		txtComentario.setLayoutData(gridData);
		
// *********************** Sección de alimentación ****************************
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 10;
		gridData.horizontalSpan = 4;
		l.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Alimentación:");
		gridData = new GridData();
		l.setLayoutData(gridData);
		
		cComidas = new Combo(composite, SWT.READ_ONLY);
		cComidas.setItems(new String[] {"Sin comidas", "Se ofrecen comidas", "No aplica"});
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		cComidas.setLayoutData(gridData);
		cComidas.select(0);
		cComidas.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				toggleComidas();
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
		setTitle("Adición de actividades");
		setMessage("Por favor, introduzca los detalles de la actividad", IMessageProvider.INFORMATION);
		if (linea.getProducto() != null) {
			String pTipo = productos.getProductoByIdProducto(linea.getIdProducto()).getDspTipo();
			long idTipo = cdTipo.getKeyAsLongByTexto(pTipo);
			comboTipo.setText(pTipo);
			productos.filtrarByTipo(idTipo, true);
			comboProducto.setItems(productos.getTexto());
			comboProducto.setText(linea.getDspProducto());
			txtDia.setText(valor2Txt(linea.getDia()));
			txtSecuencia.setText(valor2Txt(linea.getSecuencia()));
			if (linea.hasComidas()) {
				cComidas.setText("Se ofrecen comidas");
				bBreakfast.setSelection(linea.hasBreakfast());
				bLunch.setSelection(linea.hasLunch());
				bDinner.setSelection(linea.hasDinner());
				bCocktail.setSelection(linea.hasWelcomeCocktail());
				bOtros.setSelection(linea.hasOther());
			} else if (linea.getComidas().equals("X")) {
				// la X implica que aunque la actividad puede tener comidas, no se ofrece por algún motivo
				cComidas.setText("Sin comidas");
			} else {
				cComidas.setText("No aplica");
			}
			txtComentario.setText(linea.getComentario());
		} else {
			if (actividadBase == null) {
				txtDia.setText("1");
				txtSecuencia.setText("999");
			} else {
				txtDia.setText(valor2Txt(actividadBase.getDia()));
				txtSecuencia.setText("999");
			}
			cComidas.setText("No aplica");
		}
		toggleComidas();
	}
	
	
	private void guardarLinea() {
		int pos;
		Long seleccion;
		
		// obtenemos la posición del elemento seleccionado
		pos = comboProducto.getSelectionIndex();
		// obtenemos el código del producto seleccionado
		seleccion = productos.getIdProductoByIndex(pos);
		System.out.println("Pos: " + pos + ",  seleccion: " + seleccion);
		Producto p = productos.getProductoByIdProducto(seleccion);
		
		//linea.setIdProducto(seleccion);
		//linea.setDspProducto(comboProducto.getText());
		linea.setProducto(p);
		linea.setDia(txt2Integer(txtDia.getText()));
		linea.setSecuencia(txt2Integer(txtSecuencia.getText()));
		linea.setTipoReserva(p.getTipoReserva());
		linea.setComidas(definirComidas());
		linea.setComentario(txtComentario.getText());
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
	
	public boolean close() {
		System.out.println("Close: " + getReturnCode());
		if (getReturnCode() == IDialogConstants.OK_ID) {
			guardarLinea();  
		};
		return super.close();
	}
	
	
	public LineaTour getLinea() {
		return linea;
	}
}
