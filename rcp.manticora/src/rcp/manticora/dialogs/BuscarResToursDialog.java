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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.BuscarResToursDTO;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.Productos;


public class BuscarResToursDialog extends AbstractAEPDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private ComboDataController cdController;
	private Combo comboTipo;
	private Combo comboProducto;
	private Text txtFechaDesde;
	private Text txtFechaHasta;
	private Button bFecha;
	private Button bFechaHasta;
	private Button bReservas;
	
	private ImageDescriptor image;
	private BuscarResToursDTO dto; 
	
	private Productos productos;
	private ComboData cdTipo;
	private Shell shell;
	
	
	public BuscarResToursDialog(Shell parentShell, String titulo) {
		super(parentShell, titulo);
		this.shell = parentShell;
		cdController = new ComboDataController();
		productos = new Productos();
	}
	

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);

		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginWidth = 15;
		gridLayout.marginHeight = 10;
		gridLayout.verticalSpacing = 12;
		composite.setLayout(gridLayout);

		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalSpan = 6;
		l.setLayoutData(gridData);
		l.setText("Por favor, introduzca los criterios de búsqueda");
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(GridData.FILL, GridData.CENTER, false, false, 6, 1);
		gridData.heightHint = 10;
		l.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 5;
		comboTipo.setLayoutData(gridData);
		cdTipo = cdController.getComboDataTipoProductosTour(true);
		comboTipo.setItems(cdTipo.getTexto());
		comboTipo.add("Todos", 0);
		comboTipo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int indice = comboTipo.getSelectionIndex();
				if (indice != -1 && indice != 0) {
					Long seleccionado = cdTipo.getKeyAsLongByIndex(indice-1);
					productos.filtrarByTipo(seleccionado, true);   // filtramos si hay algún tipo de prod. seleccionado
				} else {
					productos.filtrarTours(true);  // si no hay selección de tipo, mostramos todos los productos tipo tour
				}
				comboProducto.setItems(productos.getTexto());
				comboProducto.add("Todos", 0);
				comboProducto.select(0);
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tour:");
		comboProducto = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(200,15);
		gridData.horizontalSpan = 5;
		comboProducto.setLayoutData(gridData);
		productos.filtrarTours(true);
		comboProducto.setItems(productos.getTexto());
		comboProducto.add("Todos", 0);
		comboProducto.select(0);
		
		
		
		
		
		l = new Label(composite, SWT.NONE);
		l.setText("Desde:");
		txtFechaDesde = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFechaDesde.setLayoutData(gridData);
		
		bFecha = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFechaDesde));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Hasta:");
		txtFechaHasta = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFechaHasta.setLayoutData(gridData);
		
		bFechaHasta = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		//gridData.horizontalSpan = 2;
		bFechaHasta.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaHasta.setImage(image.createImage());
		bFechaHasta.addSelectionListener(this.crearCalendario(shell, txtFechaHasta, txtFechaDesde));
		
		l = new Label(composite, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalSpan = 6;
		l.setLayoutData(gridData);
		
		bReservas = new Button(composite, SWT.CHECK);
		gridData = new GridData();
		gridData.horizontalSpan = 6;
		bReservas.setLayoutData(gridData);
		bReservas.setText("Solamente mostrar tours activos (con reservas realizadas)");
		
		llenarCampos();
		
		return composite;
	}
	
	
	private void llenarCampos() {
		// por default, están seleccionados todos los tipos de tours disponibles para la siguiente semana
		Date fechaDesde = FechaUtil.ajustarFecha(new Date(), 1);
		Date fechaHasta = FechaUtil.ajustarFecha(new Date(), 7);
		comboTipo.select(0);
		comboProducto.select(0);
		txtFechaDesde.setText(FechaUtil.toString(fechaDesde));
		txtFechaHasta.setText(FechaUtil.toString(fechaHasta));
		bReservas.setSelection(true);
	}
	
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "&Buscar", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}


	@Override
	protected void okPressed() {
		System.out.println("OK pressed");
		dto = new BuscarResToursDTO();
		/*
		String pTipo = comboTipo.getText();
		String pProducto = comboProducto.getText();
		Date pFechaDesde = FechaUtil.toDate(txtFechaDesde.getText());
		Date pFechaHasta = FechaUtil.toDate(txtFechaHasta.getText());
		*/
		if (comboTipo.getSelectionIndex() != -1 && comboTipo.getSelectionIndex() != 0) {
			String pTipo = comboTipo.getText();
			dto.setTipo(pTipo);
		}
		if (comboProducto.getSelectionIndex() != -1 && comboProducto.getSelectionIndex() != 0) {
			String pProducto = comboProducto.getText();
			dto.setProducto(pProducto);
		}
		if (!txtFechaDesde.getText().equals("")) {
			Date pFechaDesde = FechaUtil.toDate(txtFechaDesde.getText());
			dto.setFechaDesde(pFechaDesde);
		}
		if (!txtFechaHasta.getText().equals("")) {
			Date pFechaHasta = FechaUtil.toDate(txtFechaHasta.getText());
			dto.setFechaHasta(pFechaHasta);
		}
		
		/*
		dto.setTipo(pTipo);
		dto.setProducto(pProducto);
		dto.setFechaDesde(pFechaDesde);
		dto.setFechaHasta(pFechaHasta);
		*/
		dto.setReservas(bReservas.getSelection());
		
		super.okPressed();
	}
	
	
	public BuscarResToursDTO getDTO() {
		return dto;
	}

}
