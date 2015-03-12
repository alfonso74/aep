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
import rcp.manticora.model.Pax;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;

public class AgregarPaxCotizacion extends AbstractAEPTitleAreaDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private Text txtNombre;
	private Text txtApellido;
	private Text txtNacimiento;
	private Text txtPeso;
	private Text txtIdent;
	private Combo comboPais;
	private Button bCondiciones1;
	private Button bCondiciones2;
	private Text txtCondiciones;

	private Button bFecha;
	private ImageDescriptor image;

	private Pax linea;
	private ComboData cdPais;
	private ComboDataController cdController;
	private boolean isNewPax = false;

	private Shell shell;

	public AgregarPaxCotizacion(Shell parentShell, Pax linea) {
		super(parentShell);
		if (linea == null) {
			linea = new Pax();
			isNewPax = true;
		}
		this.shell = parentShell;
		this.linea = linea;
		cdController = new ComboDataController();
		cdPais = cdController.getComboDataPaises();
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
		//layout.marginWidth = 15;
		//layout.marginHeight = 10;
		//layout.verticalSpacing = 8;
		composite.setLayout(layout);

		Label l;
		GridData gridData;

		l = new Label(composite, SWT.NONE);
		l.setText("Nombre:");
		txtNombre = new Text(composite, SWT.BORDER);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 2;
		txtNombre.setLayoutData(gridData);
		txtNombre.setTextLimit(25);

		l = new Label(composite, SWT.NONE);
		l.setText("Apellido:");
		txtApellido = new Text(composite, SWT.BORDER);
		txtApellido.setLayoutData(new GridData(120,15));
		txtApellido.setTextLimit(25);

		l = new Label(composite, SWT.NONE);
		l.setText("Fecha nac.:");
		txtNacimiento = new Text(composite, SWT.BORDER);
		txtNacimiento.setLayoutData(new GridData(60,15));
		//txtNacimiento.addKeyListener(this.crearKeyAdapter(txtNacimiento));

		bFecha = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		//bFecha.addSelectionListener(this.crearCalendario(txtNacimiento));
		bFecha.addSelectionListener(this.crearCalendario(shell, txtNacimiento));

		l = new Label(composite, SWT.NONE);
		l.setText("Peso (lbs):");
		txtPeso = new Text(composite, SWT.BORDER);
		txtPeso.setLayoutData(new GridData(40,15));
		txtPeso.setTextLimit(3);

		l = new Label(composite, SWT.NONE);
		l.setText("Identificación:");
		txtIdent = new Text(composite, SWT.BORDER);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 4;
		txtIdent.setLayoutData(gridData);
		txtIdent.setTextLimit(20);

		l = new Label(composite, SWT.NONE);
		l.setText("País:");
		comboPais = new Combo(composite, SWT.READ_ONLY);
		comboPais.setItems(cdPais.getTexto());
		gridData = new GridData(80,15);
		gridData.horizontalSpan = 4;
		comboPais.setLayoutData(gridData);

		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		//gridData = new GridData();
		//gridData.widthHint = 225;
		gridData.heightHint = 10;
		gridData.horizontalSpan = 5;
		l.setLayoutData(gridData);

		l = new Label(composite, SWT.NONE);
		l.setText("Condiciones especiales, historial médico, dietas:");
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		l.setLayoutData(gridData);

		bCondiciones1 = new Button(composite, SWT.RADIO);
		bCondiciones1.setText("Sí");
		bCondiciones1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				txtCondiciones.setEnabled(true);
			}
		});
		bCondiciones2 = new Button(composite, SWT.RADIO);
		bCondiciones2.setText("No");
		bCondiciones2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				txtCondiciones.setText("");
				txtCondiciones.setEnabled(false);
			}
		});

		//Text txtIdent = new Text(composite, SWT.BORDER);
		//gridData = new GridData(250,15);
		txtCondiciones = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.horizontalSpan = 5;
		gridData.verticalSpan = 2;
		gridData.heightHint = 40;
		txtCondiciones.setLayoutData(gridData);
		txtCondiciones.setTextLimit(100);

		llenarCampos();

		return composite;
	}


	protected void llenarCampos() {
//		Set the title
		setTitle("Agregar Paxs");
//		Set the message
		setMessage("Por favor, introduzca los detalles del pasajero", IMessageProvider.INFORMATION);
		
		if (!isNewPax) {
			txtNombre.setText(linea.getNombre());
			txtApellido.setText(linea.getApellido());
			txtNacimiento.setText(FechaUtil.toString(linea.getFechaNacimiento()));
			txtPeso.setText(valor2Txt(linea.getPeso()));
			txtIdent.setText(linea.getIdentificacion());
			System.out.println("Pais: " + linea.getDspPais());
			if (linea.getDspPais() != null) {
				comboPais.select(comboPais.indexOf(linea.getDspPais()));
			}
			txtCondiciones.setText(linea.getCondiciones());
		}
	}


	private void guardarLineaActividad() {
		linea.setNombre(txtNombre.getText());
		linea.setApellido(txtApellido.getText());
		linea.setFechaNacimiento(FechaUtil.toDate(txtNacimiento.getText()));
		if (txtPeso.getText().equals("")) {
			linea.setPeso(0);
		} else {
			linea.setPeso(Integer.parseInt(txtPeso.getText()));  
		};
		linea.setIdentificacion(txtIdent.getText());
		// TODO: evaluar esto con long vs Long
		long noPais = cdPais.getKeyAsLongByIndex(comboPais.getSelectionIndex());
		if (noPais != -1) {
			linea.setIdPais(noPais);
			linea.setDspPais(comboPais.getItem(comboPais.getSelectionIndex()));
		}
		linea.setCondiciones(txtCondiciones.getText());
	}


	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			// si pasan las validaciones se guarda la línea y se cierra la ventana
			if (validarCampos()) {
				guardarLineaActividad();
			} else {
				// en caso contrario, se cancela el cierre de ventana
				return false;
			}
		};
		return super.close();
	}


	private boolean validarCampos() {
		String pNombre = txtNombre.getText();
		String pApellido = txtApellido.getText();
		
		if (pNombre.length() == 0) {
			MessageDialog.openInformation(shell, "Validación de campos",
					"El campo de \"Nombre\" no puede quedar en blanco.");
			return false;
		}
		if (pApellido.length() == 0) {
			MessageDialog.openInformation(shell, "Validación de campos",
					"El campo de \"Apellido\" no puede quedar en blanco.");
			return false;
		}
		if (comboPais.getSelectionIndex() == -1) {
			MessageDialog.openInformation(shell, "Validación de campos",
			"El campo de \"País\" no puede quedar en blanco");
			return false;
		}
		if (bCondiciones1.getSelection()) {
			if (txtCondiciones.getText().equals("")) {
				MessageDialog.openInformation(shell, "Validación de campos",
						"El campo de condiciones especiales es requerido");
				txtCondiciones.setFocus();
				return false;
			}
		};
		return true;
	}

	// si el parámetro linea pasa como null al constructor debemos usar este
	// método para obtener la nueva línea (Pax) 
	public Pax getLinea() {
		return linea;
	}
}

