package rcp.manticora.dialogs;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.Habitacion;
import rcp.manticora.model.LineaHospedajeInt;
import rcp.manticora.model.Pax;
import rcp.manticora.model.Producto;
import rcp.manticora.model.ReservaHospedaje;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;

public class ReservaHospIntDialog extends AbstractAEPDialog {
	private final String pluginId = Application.PLUGIN_ID;
	private Shell shell;
	
	private Combo comboHabitaciones;
	private Text txtNoReserva;
	private Text txtCondiciones;
	private Text txtComentarios;
	private Text txtFechaDesde;
	private Button bFechaDesde;
	private Text txtFechaHasta;
	private Button bFechaHasta;
	private CheckboxTableViewer viewerPaxs;
	
	private ImageDescriptor image;
	private LineaHospedajeInt linea;
	private boolean isNuevaLinea = false;
	
	private ComboData cdHabitacion;
	
	private ComboDataController cdController;
	private Set<Pax> listaPaxsHoja;

	public ReservaHospIntDialog(Shell parentShell, String titulo, ReservaHospedaje reserva,
			LineaHospedajeInt linea) {
		super(parentShell, titulo);
		this.shell = parentShell;
		if (linea == null) {
			this.isNuevaLinea = true;
			this.linea = new LineaHospedajeInt();
		} else {
			this.linea = linea;
		}
		cdController = new ComboDataController();
		Producto hospedaje = reserva.getProducto();
		System.out.println(hospedaje);
		cdHabitacion = cdController.getComboDataHabitacionesByHotel(hospedaje);
		listaPaxsHoja = reserva.getHoja().getListaPaxs();
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout;
		GridData gridData;
		
		Composite composite = new Composite(parent, SWT.NONE);
		layout = new GridLayout(1, false);
		layout.marginWidth = 10;
		layout.marginHeight = 15;
		composite.setLayout(layout);
		
		Group grupo01 = new Group(composite, SWT.NULL);
		grupo01.setText(" Detalles de hospedaje ");
		layout = new GridLayout(6, false);
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;   // espaciado entre las filas
		grupo01.setLayout(layout);
		
		Label l = new Label(grupo01, SWT.NONE);
		l.setText("Habitación:");
		comboHabitaciones = new Combo(grupo01, SWT.READ_ONLY);
		comboHabitaciones.setItems(cdHabitacion.getTexto());
		gridData = new GridData(80, 15);
		gridData.horizontalSpan = 2;
		comboHabitaciones.setLayoutData(gridData);
		comboHabitaciones.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int indice = comboHabitaciones.getSelectionIndex();
				if (indice != -1) {
					Habitacion h = (Habitacion) cdHabitacion.getObjectByIndex(indice);
					llenarCampoCondiciones(h.getListaCondiciones());
				}
			}
		});
		
		l = new Label(grupo01, SWT.NONE);
		l.setText("Reserva:");
		gridData = new GridData();
		gridData.horizontalIndent = 10;
		l.setLayoutData(gridData);
		txtNoReserva = new Text(grupo01, SWT.BORDER);
		gridData = new GridData(40, 15);
		gridData.horizontalSpan = 2;
		txtNoReserva.setLayoutData(gridData);
		
		l = new Label(grupo01, SWT.NONE);
		l.setText("Opciones:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);
		txtCondiciones = new Text(grupo01, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1);
		//gridData.widthHint = 250;
		gridData.heightHint = 55;
		txtCondiciones.setLayoutData(gridData);
		txtCondiciones.setEditable(false);
		
		l = new Label(grupo01, SWT.NONE);
		l.setText("Comentarios:");
		txtComentarios = new Text(grupo01, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1);
		txtComentarios.setLayoutData(gridData);
		
		l = new Label(grupo01, SWT.NONE);
		l.setText("Desde:");
		txtFechaDesde = new Text(grupo01, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFechaDesde.setLayoutData(gridData);
		
		bFechaDesde = new Button(grupo01, SWT.NONE);
		gridData = new GridData(16,16);
		bFechaDesde.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaDesde.setImage(image.createImage());
		bFechaDesde.addSelectionListener(this.crearCalendario(shell, txtFechaDesde));
		
		l = new Label(grupo01, SWT.NONE);
		l.setText("Hasta:");
		gridData = new GridData();
		gridData.horizontalIndent = 10;
		l.setLayoutData(gridData);
		txtFechaHasta = new Text(grupo01, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFechaHasta.setLayoutData(gridData);
		
		bFechaHasta = new Button(grupo01, SWT.NONE);
		gridData = new GridData(16,16);
		bFechaHasta.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaHasta.setImage(image.createImage());
		bFechaHasta.addSelectionListener(this.crearCalendario(shell, txtFechaHasta, txtFechaDesde));
		
// ***************************** asignación de paxs a la habitación ******************
		
		Group grupoPax = new Group(composite, SWT.NONE);
		grupoPax.setText(" Asignación de paxs ");
		layout = new GridLayout(2, false);
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		grupoPax.setLayout(layout);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		grupoPax.setLayoutData(gridData);
/*		
		l = new Label(grupoPax, SWT.NONE);
		l.setText("Paxs:");
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);
*/
		viewerPaxs = CheckboxTableViewer.newCheckList(grupoPax, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		//gridData.widthHint = 350;
		gridData.heightHint = 60;
		viewerPaxs.getTable().setLayoutData(gridData);
		viewerPaxs.setContentProvider(new ViewContentProvider());
		viewerPaxs.setLabelProvider(new ViewLabelProvider());

		viewerPaxs.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				System.out.println("Cambio de check: " + viewerPaxs.getSelection());
				verificar();
			}
			
		});
		
		llenarControles();
		
		return composite;
	}
	
	
	private void verificar() {
		for (Pax pax : listaPaxsHoja) {
			System.out.println(pax.getNombre() + " checked: " + viewerPaxs.getChecked(pax));
		}
	}
	
	
	private void llenarControles() {
		if (isNuevaLinea) {
			viewerPaxs.setInput(listaPaxsHoja);
		} else {
			System.out.println("Cargando información de campos...");
			comboHabitaciones.setText(linea.getHabitacion().getNombre());
			//txtNoReserva.setText(linea.getReserva().getNoReserva());
			txtComentarios.setText(linea.getComentario());
			txtFechaDesde.setText(FechaUtil.toString(linea.getFechaDesde()));
			txtFechaHasta.setText(FechaUtil.toString(linea.getFechaHasta()));
			viewerPaxs.setInput(listaPaxsHoja);
			Set<Pax> listaPaxs = linea.getListaPaxs();
			Pax[] paxsAsignados = (Pax[]) listaPaxs.toArray(new Pax[listaPaxs.size()]);
			viewerPaxs.setCheckedElements(paxsAsignados);
		}
		
		//viewerPaxs.getCheckedElements();  // al guardar
	}
	

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			try {
				if (validarCampos()) {
					// si pasa las validaciones, creamos el registro y cerramos el diálogo
					guardarHospedajeExterno();
					super.buttonPressed(buttonId);
				}
			} catch (Exception e) {
				mensajeError(shell, e);
			}
		} else {
			super.buttonPressed(buttonId);
		}
	}
	
	
	private boolean validarCampos() {
		String pComentarios = txtComentarios.getText();
		String pFechaDesde = txtFechaDesde.getText();
		String pFechaHasta = txtFechaHasta.getText();
		
		if (comboHabitaciones.getSelectionIndex() == -1) {
			MessageDialog.openError(shell, "Validación de campos",
					"No se ha seleccionado el tipo de la habitación.");
			return false;
		}
		if (pComentarios.length() > 100) {
			MessageDialog.openError(shell, "Validación de campos",
				"El campo de comentarios no puede superar los 100 caracteres (" + pComentarios.length() + ").");
			return false;
		}
		if (pFechaDesde.length() == 0) {
			MessageDialog.openError(shell, "Validación de campos",
				"El campo de fecha \"Desde\" no puede quedar en blanco");
			return false;
		}
		if (pFechaHasta.length() == 0) {
			MessageDialog.openError(shell, "Validación de campos",
				"El campo de fecha \"Hasta\" no puede quedar en blanco");
			return false;
		}
		return true;
	}
	
	
	private void guardarHospedajeExterno() {
		String pComentario = txtComentarios.getText();
		Date pFechaDesde = FechaUtil.toDate(txtFechaDesde.getText());
		Date pFechaHasta = FechaUtil.toDate(txtFechaHasta.getText());
		int indice = comboHabitaciones.getSelectionIndex();
		Habitacion pHabitacion = (Habitacion) cdHabitacion.getObjectByIndex(indice);
		Set<Pax> pPaxsAsignados = getPaxAsignados();
		
		linea.setHabitacion(pHabitacion);
		linea.asignarListaPaxs(pPaxsAsignados);
		//linea.agregarPax(pPaxsAsignados.iterator().next());
		linea.setComentario(pComentario);
		linea.setFechaDesde(pFechaDesde);
		linea.setFechaHasta(pFechaHasta);
	}
	
	
	private Set<Pax> getPaxAsignados() {
		Set<Pax> listaTmp = new HashSet<Pax>();
		Object[] arrayTmp = viewerPaxs.getCheckedElements();
		for (int n = 0; n < arrayTmp.length; n++) {
			listaTmp.add((Pax) arrayTmp[n]);
		}
		return listaTmp;
	}
	
	
	private void llenarCampoCondiciones(String[] condiciones) {
		String newline = System.getProperty("line.separator");
		String cadena = "";
		for (int n=0; n < condiciones.length; n++) {
			if (cadena.equals("")) {
				cadena = condiciones[n];
			} else {
				cadena += newline + condiciones[n];
			}
		}
		txtCondiciones.setText(cadena);
	}
	
	/*
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = (String[]) inputElement;
			return resultados;
		}
	}
	
	
	class ViewLabelProvider extends LabelProvider implements ILabelProvider {

		@Override
		public String getText(Object element) {
			return (String) element;
		}
	}
	
	public LineaHospedajeExt getLineaHospedaje() {
		return linea;
	}
	*/
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set<Pax>) inputElement).toArray();
			return resultados;
		}
	}
	
	
	class ViewLabelProvider extends LabelProvider implements ILabelProvider {

		@Override
		public String getText(Object element) {
			
			Pax pax = (Pax) element;
			return pax.getNombre() + " " + pax.getApellido();
			
		}
		
	}
	
	
	public LineaHospedajeInt getLineaHospedaje() {
		return linea;
	}
	
	
	private Object getElementoSeleccionado(TableViewer viewer) {
		Object seleccion = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
		return seleccion;
	}
	
	
	private Pax getPaxSeleccionado() {
		Pax pax = (Pax) getElementoSeleccionado(viewerPaxs);
		return pax;
	}
}
