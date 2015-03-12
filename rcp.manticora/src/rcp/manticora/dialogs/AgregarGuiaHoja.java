package rcp.manticora.dialogs;

import java.util.Date;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
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
import rcp.manticora.model.Guia;
import rcp.manticora.model.ReservaGuia;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;

public class AgregarGuiaHoja extends AbstractAEPTitleAreaDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private ComboDataController cdController;
	private Combo comboGuia;
	private Text txtFechaDesde;
	private Text txtFechaHasta;
	
	private ImageDescriptor image;
	private Shell shell;
	
	private ReservaGuia reserva;
	private ComboData cdGuia;
	
	
	public AgregarGuiaHoja(Shell parentShell, ReservaGuia reserva) {
		super(parentShell);
		this.shell = parentShell;
		this.reserva = reserva;
		cdController = new ComboDataController();
		cdGuia = cdController.getComboDataGuias();
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(6, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Nombre del guía:");
		comboGuia = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(200,15);
		gridData.horizontalSpan = 5;
		comboGuia.setLayoutData(gridData);
		comboGuia.setItems(cdGuia.getTexto());
		
		l = new Label(composite, SWT.NONE);
		l.setText("Fecha de inicio:");
		txtFechaDesde = new Text(composite, SWT.BORDER);
		txtFechaDesde.setLayoutData(new GridData(60,15));
		
		Button bFechaIni = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		gridData.horizontalSpan = 1;
		bFechaIni.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaIni.setImage(image.createImage());
		bFechaIni.addSelectionListener(this.crearCalendario(shell, txtFechaDesde));
		
		//l = new Label(composite, SWT.NONE);
		//l.setLayoutData(new GridData(30, 15));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Fecha final:");
		gridData = new GridData(60,15);
		gridData.horizontalIndent = 20;
		l.setLayoutData(gridData);
		txtFechaHasta = new Text(composite, SWT.BORDER);
		txtFechaHasta.setLayoutData(new GridData(60,15));
		
		Button bFechaFin = new Button(composite, SWT.PUSH);
		bFechaFin.setLayoutData(new GridData(16,16));
		bFechaFin.setImage(image.createImage());
		bFechaFin.addSelectionListener(this.crearCalendario(shell, txtFechaHasta, txtFechaDesde));
		
		llenarCampos();
		
		return composite;
	}
	

	@Override
	protected void llenarCampos() {
// Set the title
		setTitle("Agregar guía");	
// Set the message
		setMessage("Por favor, introduzca los detalles de la asignación del guía", IMessageProvider.INFORMATION);
		
		String texto = "";
		if (reserva.getGuia() != null) {
			comboGuia.setText(reserva.getGuia().getNombreCompleto());
			texto = FechaUtil.toString(reserva.getFechaDesde(), FechaUtil.formatoFecha);
			txtFechaDesde.setText(texto);
			texto = FechaUtil.toString(reserva.getFechaHasta(), FechaUtil.formatoFecha);
			txtFechaHasta.setText(texto);
		}
	}
	
	
	private void guardarLineaActividad() {
		Guia pGuia = (Guia) cdGuia.getObjectByIndex(comboGuia.getSelectionIndex());
		Date pFechaDesde = FechaUtil.toDate(txtFechaDesde.getText());
		Date pFechaHasta = FechaUtil.toDate(txtFechaHasta.getText());
		
		reserva.setGuia(pGuia);
		reserva.setFechaDesde(pFechaDesde);
		reserva.setFechaHasta(pFechaHasta);
	}
	
	
	private boolean validarSave() {
		Date pFechaDesde = FechaUtil.toDate(txtFechaDesde.getText());
		Date pFechaHasta = FechaUtil.toDate(txtFechaDesde.getText());
		if (comboGuia.getSelectionIndex() == -1) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"Debe seleccionar un guía para realizar la asignación.");
			return false;
		}
		if (pFechaDesde == null) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"El campo de fecha inicial tiene una fecha no válida.");
			return false;
		}
		if (pFechaHasta == null) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"El campo de fecha final tiene una fecha no válida.");
			return false;
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

}
