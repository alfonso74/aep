package rcp.manticora.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CuadrarCotizacionDialog extends AbstractAEPDialog {
	private Text txtPrecio;
	private Text txtImpuesto;
	private Text txtHospedaje;
	private Text txtIPAT;
	private Text txtSugerido;
	private Text txtUnidades;
	private Text txtSugeridoUnitario;
	private Float precioSugerido = 0f;
	private boolean bIncluye = false;
	
	private Button bAplicar;
	
	private Shell shell;
	private AgregarActividadCotizacion parentDialog;

	public CuadrarCotizacionDialog(Shell parentShell, AgregarActividadCotizacion parentDialog, String titulo) {
		super(parentShell, titulo);
		this.shell = parentShell;
		this.parentDialog = parentDialog;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Cuadrar", false);
		bAplicar = createButton(parent, IDialogConstants.PROCEED_ID, "Aplicar", false);
		bAplicar.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cerrar", true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout;
		GridData gridData;
		
		Composite composite = new Composite(parent, SWT.NONE);
		layout = new GridLayout(3, false);
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		composite.setLayout(layout);
		
		Label l = new Label(composite, SWT.NONE);
		l.setText("");
		// SWT.CENTER:  alineamiento del texto dentro del control
		l = new Label(composite, SWT.NONE | SWT.CENTER);
		gridData = new GridData();
		// SWT.CENTER:  alineamiento del control dentro de la celda del grid
		gridData.horizontalAlignment = SWT.CENTER;
		l.setLayoutData(gridData);
		l.setText("Precio\nofrecido");
		
		l = new Label(composite, SWT.NONE | SWT.CENTER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.CENTER;
		l.setLayoutData(gridData);
		l.setText("Impuesto\naplicado (%)");
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gridData.heightHint = 7;
		l.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Paquete (sin impuesto):");
		l.setToolTipText("Sin impuesto pero incluyendo hospedaje");
		txtPrecio = new Text(composite, SWT.BORDER | SWT.RIGHT);
		txtPrecio.setLayoutData(new GridData(45, 15));
		
		txtImpuesto = new Text(composite, SWT.BORDER | SWT.RIGHT);
		gridData = new GridData(13, 15);
		gridData.horizontalAlignment = SWT.CENTER;
		txtImpuesto.setLayoutData(gridData);
		txtImpuesto.setText("5");
		txtImpuesto.setTextLimit(2);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Hospedaje (sin impuesto):");
		l.setToolTipText("Sin impuesto de IPAT");
		txtHospedaje = new Text(composite, SWT.BORDER | SWT.RIGHT);
		txtHospedaje.setLayoutData(new GridData(45, 15));
		
		txtIPAT = new Text(composite, SWT.BORDER | SWT.RIGHT);
		gridData = new GridData(13, 15);
		gridData.horizontalAlignment = SWT.CENTER;
		txtIPAT.setLayoutData(gridData);
		txtIPAT.setText("10");
		txtIPAT.setTextLimit(2);
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gridData.heightHint = 7;
		l.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Nuevo precio a utilizar:");
		
		txtSugerido = new Text(composite, SWT.BORDER | SWT.RIGHT);
		gridData = new GridData(45, 15);
		gridData.horizontalSpan = 2;
		txtSugerido.setLayoutData(gridData);
		txtSugerido.setEditable(false);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Cantidad a considerar:");
		
		txtUnidades = new Text(composite, SWT.BORDER | SWT.RIGHT);
		gridData = new GridData(45, 15);
		gridData.horizontalSpan = 2;
		txtUnidades.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Precio unitario sugerido:");
		
		txtSugeridoUnitario = new Text(composite, SWT.BORDER | SWT.RIGHT);
		gridData = new GridData(45, 15);
		gridData.horizontalSpan = 2;
		txtSugeridoUnitario.setLayoutData(gridData);
		txtSugeridoUnitario.setEditable(false);
		
// llenado default de los campos, si hay un parentDialog
		if (parentDialog != null) {
			txtUnidades.setText(parentDialog.getTxtEspacios());
		}
		
		return composite;
	}

	
	@Override
	protected void okPressed() {
		try {
			if (validarCampos()) {
				if (bIncluye) {
					calcularSugerido2();
				} else {
					calcularSugerido1();
				}
			}
		}catch (Exception e) {
			mensajeError(shell, e);
		}
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.PROCEED_ID) {
			if (parentDialog.getTxtPrecio().getEditable() == true) {
				parentDialog.setTxtPrecio(precioSugerido);
			} else {
				MessageDialog.openInformation(shell, "No puede aplicarse el precio", "El campo de precio no es modificable para el registro seleccionado.");
			}
		}
		super.buttonPressed(buttonId);
	}

	
	/**
	 * Calcula el valor sugerido sin incluir el precio del hospedaje
	 * en el resultado
	 *
	 */
	private void calcularSugerido1() throws Exception {

		Float base = txt2Float(txtPrecio.getText());
		
//TODO: validación de campos numéricos y usar BigDecimal por float/double
		/*
		ParsePosition pp = new ParsePosition(0);
		NumberFormat nf = NumberFormat.getNumberInstance();
//		nf.setMinimumIntegerDigits(0);
		nf.setMaximumIntegerDigits(2);
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		//nf.setParseIntegerOnly(true);
		System.out.println(nf.getMaximumFractionDigits());
		System.out.println(nf.getMaximumIntegerDigits());
		System.out.println(nf.getMinimumFractionDigits());
		System.out.println(nf.getMinimumIntegerDigits());
		nf.parse(txtPrecio.getText(), pp);
		if (txtPrecio.getText().length() != pp.getIndex()) {
			System.out.println("ERROR parseando");
		} else {
			System.out.println("BASE1: " + nf.parse(txtPrecio.getText()));
		}
		*/
		
		float hospedaje = txt2Float(txtHospedaje.getText());
		float impuesto = txt2Float(txtImpuesto.getText());
		float ipat = txt2Float(txtIPAT.getText());
		int unidades = txt2Integer((txtUnidades.getText()));
		
		float t1 = base * (1 + impuesto / 100);
		float t2 = hospedaje * (1 + ipat / 100);
		float t3 = 1 + impuesto / 100;
		
		float resultado = (t1 - t2) / t3;
		float resultadoUnitario = resultado / unidades;
		
		precioSugerido = Float.valueOf(resultadoUnitario);
		txtSugerido.setText(valor2Txt(resultado, "#,##0.00"));
		txtSugeridoUnitario.setText(valor2Txt(resultadoUnitario , "#,##0.00"));
		if (parentDialog != null) {
			bAplicar.setEnabled(true);
		}
	}
	
	/**
	 * Calcula el valor sugerido incluyendo el precio del hospedaje en
	 * el resultado
	 *
	 */
	private void calcularSugerido2() throws Exception {

		float base = txt2Float(txtPrecio.getText());
		float hospedaje = txt2Float(txtHospedaje.getText());
		float impuesto = txt2Float(txtImpuesto.getText());
		float ipat = txt2Float(txtIPAT.getText());
		int unidades = txt2Integer((txtUnidades.getText()));
		
		float t1 = base * (1 + impuesto / 100);
		float t2 = hospedaje * ((ipat - impuesto) / 100);
		float t3 = 1 + impuesto / 100;
		
		float resultado = (t1 - t2) / t3;
		float resultadoUnitario = resultado / unidades;
		
		precioSugerido = Float.valueOf(resultadoUnitario);
		txtSugerido.setText(valor2Txt(resultado, "#,##0.00"));
		txtSugeridoUnitario.setText(valor2Txt(resultadoUnitario , "#,##0.00"));
		if (parentDialog != null) {
			bAplicar.setEnabled(true);
		}
	}
	
	private boolean validarCampos() {
		if (txtUnidades.getText() == "") {
			txtUnidades.setText("1");
		}
		int unidades = txt2Integer((txtUnidades.getText()));
		if (unidades < 1) {
			txtUnidades.setText("1");
		}
		if (txtPrecio.getText() == "") {
			MessageDialog.openError(shell, "Error de validación", "El campo de \"Paquete\" no puede quedar en blanco.");
			return false;
		}
		if (txtHospedaje.getText() == "") {
			MessageDialog.openError(shell, "Error de validación", "El campo de \"Hospedaje\" no puede quedar en blanco.");
			return false;
		}
		return true;
	}
}
