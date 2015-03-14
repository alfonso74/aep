package rcp.manticora.dialogs;

import java.util.Date;

import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.BuscarCotizacionDatos;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;

public class BuscarCotizacionesDialog2 extends AbstractAEPDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private Text txtCodigo;
	private Text txtNumeroTour;
	private Text txtNoCliente;
	private Text txtNombre;
	private Combo comboVendedor;
	private Text txtFechaInicio1;
	private Text txtFechaInicio2;
	private Text txtFechaFin1;
	private Text txtFechaFin2;
	
	private Button bClaseCliente;
	private Text txtNombreCliente;
	private Text txtApellidoCliente;
	private Text txtNombrePax;
	private Text txtApellidoPax;
	
	private BuscarCotizacionDatos datosQuery;
	private ComboData cdVendedor;
	private Shell shell;
	
	private ImageDescriptor image;
	
	private ComboDataController cdController;

	public BuscarCotizacionesDialog2(Shell parentShell, String titulo) {
		super(parentShell, titulo);
		this.shell = parentShell;
		cdController = new ComboDataController();
		cdVendedor = cdController.getComboDataVendedores();
	}

	
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(5, false);
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		composite.setLayout(gridLayout);
		
		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		TabItem tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("Generales");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/infoGeneral2.gif");
		tabGeneral.setImage(image.createImage());
		tabGeneral.setControl(crearTabGeneralControl(tabFolder));
		
		TabItem tabClientePax = new TabItem(tabFolder, SWT.NONE);
		tabClientePax.setText("Cliente/Pax");
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "icons/detallePAX.gif");
		tabClientePax.setImage(image.createImage());
		tabClientePax.setControl(crearTabClientePaxControl(tabFolder));
		
		return composite;
	}
	
	
	private Control crearTabGeneralControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout(5, false);
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		composite.setLayout(gridLayout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		l.setLayoutData(gridData);
		l.setText("Por favor, introduzca los criterios de búsqueda");
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
		gridData.heightHint = 10;
		//gridData.horizontalSpan = 5;
		l.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("No. de cotización:");
		txtCodigo = new Text(composite, SWT.BORDER);
		gridData = new GridData(40, 15);
		gridData.horizontalSpan = 4;
		txtCodigo.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("No. de gira:");
		txtNumeroTour = new Text(composite, SWT.BORDER);
		gridData = new GridData(55, 15);
		gridData.horizontalSpan = 4;
		txtNumeroTour.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Nombre del tour:");
		txtNombre = new Text(composite, SWT.BORDER);
		gridData = new GridData(150, 15);
		gridData.horizontalSpan = 4;
		txtNombre.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Vendedor:");
		comboVendedor = new Combo(composite, SWT.NONE);
		comboVendedor.setItems(cdVendedor.getTexto());
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		comboVendedor.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		l.setLayoutData(gridData);
		
		Group gFechas = new Group(composite, SWT.NONE);
		gFechas.setText(" Fechas de inicio y finalización ");
		gridLayout = new GridLayout(6, false);
		gridLayout.marginHeight = 10;
		gFechas.setLayout(gridLayout);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		gFechas.setLayoutData(gridData);
		
		l = new Label(gFechas, SWT.NONE);
		l.setText("Inicio entre:");
		
		txtFechaInicio1 = new Text(gFechas, SWT.BORDER);
		txtFechaInicio1.setLayoutData(new GridData(60, 15));
		Button bFechaInicio1 = new Button(gFechas, SWT.PUSH);
		bFechaInicio1.setLayoutData(new GridData(16, 16));
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaInicio1.setImage(image.createImage());
		bFechaInicio1.addSelectionListener(this.crearCalendario(shell, txtFechaInicio1));
		
		l = new Label(gFechas, SWT.NONE);
		l.setText(" y ");
		
		txtFechaInicio2 = new Text(gFechas, SWT.BORDER);
		txtFechaInicio2.setLayoutData(new GridData(60, 15));
		Button bFechaInicio2 = new Button(gFechas, SWT.PUSH);
		bFechaInicio2.setLayoutData(new GridData(16, 16));
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaInicio2.setImage(image.createImage());
		bFechaInicio2.addSelectionListener(this.crearCalendario(shell, txtFechaInicio2, txtFechaInicio1));
		
		l = new Label(gFechas, SWT.NONE);
		l.setText("Fin entre:");
		
		txtFechaFin1 = new Text(gFechas, SWT.BORDER);
		txtFechaFin1.setLayoutData(new GridData(60, 15));
		Button bFechaFin1 = new Button(gFechas, SWT.PUSH);
		bFechaFin1.setLayoutData(new GridData(16, 16));
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaFin1.setImage(image.createImage());
		bFechaFin1.addSelectionListener(this.crearCalendario(shell, txtFechaFin1));
		
		l = new Label(gFechas, SWT.NONE);
		l.setText(" y ");
		
		txtFechaFin2 = new Text(gFechas, SWT.BORDER);
		txtFechaFin2.setLayoutData(new GridData(60, 15));
		Button bFechaFin2 = new Button(gFechas, SWT.PUSH);
		bFechaFin2.setLayoutData(new GridData(16, 16));
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaFin2.setImage(image.createImage());
		bFechaFin2.addSelectionListener(this.crearCalendario(shell, txtFechaFin2, txtFechaFin1));
		
		return composite;
	}
	
	
	private Control crearTabClientePaxControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		composite.setLayout(gridLayout);
		
		Label l;
		GridData gridData;
		
// controles para búsqueda por nombre de cliente (natural o jurídico)
		Group gCliente = new Group(composite, SWT.NONE);
		gCliente.setText(" Parámetros para búsqueda por cliente ");
		gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 10;
		gCliente.setLayout(gridLayout);
		gCliente.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		l = new Label(gCliente, SWT.NONE);
		l.setText("No. cliente:");
		txtNoCliente = new Text(gCliente, SWT.BORDER);
		gridData = new GridData(40, 15);
		gridData.horizontalSpan = 1;
		txtNoCliente.setLayoutData(gridData);
		
		bClaseCliente = new Button(gCliente, SWT.CHECK);
		bClaseCliente.setText("Compañía");
		gridData = new GridData(90, 15);
		gridData.horizontalIndent = 15;
		bClaseCliente.setLayoutData(gridData);
		bClaseCliente.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toggleCamposCliente();
			}
		});
		
		l = new Label(gCliente, SWT.NONE);
		l.setText("Nombre:");
		txtNombreCliente = new Text(gCliente, SWT.BORDER);
		gridData = new GridData(120, 15);
		gridData.horizontalSpan = 2;
		txtNombreCliente.setLayoutData(gridData);
		
		l = new Label(gCliente, SWT.NONE);
		l.setText("Apellido:");
		txtApellidoCliente = new Text(gCliente, SWT.BORDER);
		gridData = new GridData(120, 15);
		gridData.horizontalSpan = 2;
		txtApellidoCliente.setLayoutData(gridData);
		
// controles para la búsqueda por nombre/apellido de un pax
		Group gPax = new Group(composite, SWT.NONE);
		gPax.setText(" Parámetros para búsqueda por pax ");
		gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 10;
		gPax.setLayout(gridLayout);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.verticalIndent = 15;
		gPax.setLayoutData(gridData);
		
		l = new Label(gPax, SWT.NONE);
		l.setText("Nombre:");
		txtNombrePax = new Text(gPax, SWT.BORDER);
		gridData = new GridData(120, 15);
		gridData.horizontalSpan = 1;
		txtNombrePax.setLayoutData(gridData);
		
		l = new Label(gPax, SWT.NONE);
		l.setText("Apellido:");
		txtApellidoPax = new Text(gPax, SWT.BORDER);
		gridData = new GridData(120, 15);
		gridData.horizontalSpan = 1;
		txtApellidoPax.setLayoutData(gridData);
		
		return composite;
	}
	

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "&Buscar", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	
	protected void okPressed() {
		Long pCodigo = txt2Long(txtCodigo.getText().trim());
		String pNumeroTour = txtNumeroTour.getText().trim();
		String pNombre = txtNombre.getText().trim();
		String pVendedor = comboVendedor.getText();
		Date pFechaIni1 = FechaUtil.toDate(txtFechaInicio1.getText());
		Date pFechaIni2 = FechaUtil.toDate(txtFechaInicio2.getText());
		Date pFechaFin1 = FechaUtil.toDate(txtFechaFin1.getText());
		Date pFechaFin2 = FechaUtil.toDate(txtFechaFin2.getText());
		Long pNoCliente = txt2Long(txtNoCliente.getText());
		String pClaseCliente = bClaseCliente.getSelection() ? "C" : "N";
		String pClienteNombre = txtNombreCliente.getText();
		String pClienteApellido = txtApellidoCliente.getText();
		String pPaxNombre = txtNombrePax.getText();
		String pPaxApellido = txtApellidoPax.getText();
		
		datosQuery = new BuscarCotizacionDatos();
		datosQuery.setNoCotizacion(pCodigo);
		datosQuery.setNumeroTour(pNumeroTour);
		datosQuery.setNombreCotizacion(pNombre);
		datosQuery.setVendedor(pVendedor);
		datosQuery.setFechaInicio1(pFechaIni1);
		datosQuery.setFechaInicio2(pFechaIni2);
		datosQuery.setFechaFin1(pFechaFin1);
		datosQuery.setFechaFin2(pFechaFin2);
		datosQuery.setNoCliente(pNoCliente);
		datosQuery.setClaseCliente(pClaseCliente);
		datosQuery.setClienteNombre(pClienteNombre);
		datosQuery.setClienteApellido(pClienteApellido);
		datosQuery.setPaxNombre(pPaxNombre);
		datosQuery.setPaxApellido(pPaxApellido);
		
		super.okPressed();
	}
	
	
	public BuscarCotizacionDatos getDatosQuery() {
		return datosQuery;
	}
	
	
	private void toggleCamposCliente() {
		if (bClaseCliente.getSelection()) {
			txtApellidoCliente.setText("");
			txtApellidoCliente.setEnabled(false);
		} else {
			txtApellidoCliente.setEnabled(true);
		}
	}

}
