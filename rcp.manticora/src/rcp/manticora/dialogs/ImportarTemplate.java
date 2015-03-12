package rcp.manticora.dialogs;

import java.util.Date;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.FormUtils;
import rcp.manticora.services.IFormUtils;

public class ImportarTemplate extends Dialog implements IFormUtils {
	private final String pluginId = Application.PLUGIN_ID;
	private Shell parent;       // shell de pantalla principal
	private Shell shell;        // shell del dialogBox
	
	private List listaTemplates;
	private Text txtFechaBase;
	private Text txtPaxs;
	private Combo comboTipoPrecio;
	
	private String fechaDefault;
	private String noPaxsDefault;
	
	private ComboData cdTemplate;
	private Button bOkay;
	private Button bCancelar;
	private Button bFecha;
	private MyInputDialogData data;
	private FormUtils formUtils;
	private ImageDescriptor image;
	
	private ComboDataController cdController;

	public ImportarTemplate(Shell parent, String fechaDefault, String noPaxsDefault) {
		super(parent);
		this.parent = parent;
		cdController = new ComboDataController();
		formUtils = new FormUtils();
		if (fechaDefault == null || fechaDefault.equals("")) {
			fechaDefault = FechaUtil.toString(new Date());
		}
		if (noPaxsDefault == null || noPaxsDefault.equals("")) {
			noPaxsDefault = "1";
		}
		this.fechaDefault = fechaDefault;
		this.noPaxsDefault = noPaxsDefault;
	}

	public ImportarTemplate(Shell parent, int style) {
		super(parent, style);
		this.parent = parent;
		cdController = new ComboDataController();
		formUtils = new FormUtils();
	}
	
	public MyInputDialogData open() {
		data = new MyInputDialogData();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(getText());
		shell.setSize(330,230);		
		shell.setLayout(new RowLayout());
		
		final Composite composite = new Composite(shell, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label l;
		GridData gridData;

// texto de leyenda y separador
		l = new Label(composite, SWT.NONE);
		l.setText("Por favor, seleccione el template a utilizar:");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		l.setLayoutData(gridData);
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 5;
		gridData.horizontalSpan = 2;
		l.setLayoutData(gridData);

// listado de templates para selección del usuario final
		cdTemplate = cdController.getComboDataTemplates();
		listaTemplates = new List(composite, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		listaTemplates.setItems(cdTemplate.getTexto());
		gridData = new GridData();
		gridData.widthHint = 200;
		gridData.heightHint = 65;
		gridData.verticalSpan = 2;
		listaTemplates.setLayoutData(gridData);
		
// botones
		bOkay = new Button (composite, SWT.PUSH);
		bOkay.setText ("Ok");
		gridData = new GridData();
		gridData.widthHint = 70;
		gridData.heightHint = 22;
		gridData.verticalAlignment = SWT.BOTTOM;
		bOkay.setLayoutData(gridData);
		bCancelar = new Button (composite, SWT.PUSH);
		bCancelar.setText ("Cancelar");
		bCancelar.setLayoutData(new GridData(70,22));

		bOkay.addListener(SWT.Selection, this.crearListener("Ok"));
		bCancelar.addListener(SWT.Selection, this.crearListener(("Cancelar")));
		
		Composite bottom = new Composite(composite, SWT.NONE);
		layout = new GridLayout(4, false);
		bottom.setLayout(layout);
		
// selección de fecha base
		l = new Label(bottom, SWT.NONE);
		l.setText("Fecha base:");

		txtFechaBase = new Text(bottom, SWT.BORDER);
		gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		txtFechaBase.setLayoutData(gridData);
		txtFechaBase.setTextLimit(10);
		
		bFecha = new Button(bottom, SWT.NONE);
		gridData = new GridData(16,16);
		gridData.horizontalSpan = 2;
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/dateChooser.gif");
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFechaBase));
		
		l = new Label(bottom, SWT.NONE);
		l.setText("No. de Pax(s):");
		
		txtPaxs = new Text(bottom, SWT.BORDER);
		gridData = new GridData(30, 15);
		gridData.horizontalSpan = 3;
		txtPaxs.setLayoutData(gridData);
		txtPaxs.setTextLimit(3);
		
		l = new Label(bottom, SWT.NONE);
		l.setText("Tipo de precio:");

		comboTipoPrecio = new Combo(bottom, SWT.READ_ONLY);
		comboTipoPrecio.setItems(new String[] {"Comisionable", "Operador", "Público"});
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		comboTipoPrecio.setLayoutData(gridData);
		
// valores default del diálogo
		llenarControles();
		
// inicialización de la ventana
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		return data;
	}
	
	private void llenarControles() {
		txtFechaBase.setText(fechaDefault);
		txtPaxs.setText(noPaxsDefault);
		comboTipoPrecio.select(2);
	}
	
	public Listener crearListener(final String codigoBoton) {
		return new Listener() {
			public void handleEvent (Event event) {
				System.out.println("Widget: " + event.widget);
				if (codigoBoton.equals("Ok")) {
					if (validarCampos()) {
						data.setButtonResponse(true);
						data.setCodigoTemplate(cdTemplate.getCodeByIndex(listaTemplates.getSelectionIndex()));
						data.setFechaBase(txtFechaBase.getText());
						data.setNoPaxs(txtPaxs.getText());
						data.setTipoPrecio(comboTipoPrecio.getText());
						shell.close ();
					}
				} else {
					data.setButtonResponse(false);
					data.setCodigoTemplate("");
					data.setFechaBase("");
					data.setNoPaxs("");
					data.setTipoPrecio("");
					shell.close ();
				}
				
			}
		};
	}
	
	public boolean validarCampos() {
		String pFecha = txtFechaBase.getText();
		String pPaxs = txtPaxs.getText();
		
		if (listaTemplates.getSelectionIndex() == -1) {
			MessageDialog.openWarning(shell, "Validación de campos",
				"Debe seleccionar el template a ser utilizado para la importación.");
			return false;
		}
		if (pFecha.length() == 0) {
			MessageDialog.openWarning(shell, "Validación de campos",
				"El campo de \"Fecha base\" no puede quedar en blanco.");
			return false;
		}
		if (pPaxs.length() == 0) {
			MessageDialog.openWarning(shell, "Validación de campos",
				"El campo de \"No. de pax(s)\" no puede quedar en blanco.");
			return false;
		}
		if (pPaxs.equals("0")) {
			MessageDialog.openWarning(shell, "Validación de campos",
				"El campo de \"No. de pax(s)\" no puede tener valor 0.");
			return false;
		}
		return true;
	}
	
	public Double txt2Currency(String valorCampo) {
		return formUtils.txt2Currency(valorCampo);
	}
	
	public Integer txt2Integer(String valorCampo) {
		return formUtils.txt2Integer(valorCampo);
	}
	
	public Float txt2Float(String valorCampo) {
		return formUtils.txt2Float(valorCampo);
	}
	
	public Long txt2Long(String valorCampo) {
		return formUtils.txt2Long(valorCampo);
	}
	
	public String valor2Txt(Object valorCampo, String formato) {
		return formUtils.valor2Txt(valorCampo, formato);
	}

	public String valor2Txt(Object valorCampo) {
		return formUtils.valor2Txt(valorCampo);
	}
	
	public boolean valor2Bool(Boolean valorCampo) {
		return formUtils.valor2Bool(valorCampo);
	}
	
	public SelectionAdapter crearCalendario(Shell shell, Text txtCampo) {
		return formUtils.crearCalendario(shell, txtCampo);
	}
	
	public KeyAdapter crearKeyAdapter(Text txtCampo) {
		return formUtils.crearKeyAdapter(txtCampo);
	}
}

