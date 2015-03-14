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
import rcp.manticora.model.IReserva;
import rcp.manticora.model.LineaActividad;
import rcp.manticora.model.ReservaVuelo;
import rcp.manticora.services.FechaUtil;

public class ReservaVueloDialog extends AbstractAEPTitleAreaDialog implements
		IReservaDialog {
	
	private final String pluginId = Application.PLUGIN_ID;
	private boolean isNuevaReserva;
	
	private Text txtLocalizador;
	private Combo comboLinea;
	private Text txtVuelo;
	private Text txtFecha;
	private Button bFecha;
	private Text txtHora;
	private Text txtComentario;
	
	private ImageDescriptor image;
	private LineaActividad linea;
	private ReservaVuelo reserva;
	private Shell shell;
	
	public ReservaVueloDialog(Shell parentShell, LineaActividad linea,
			ReservaVuelo reserva) {
		super(parentShell);
		this.shell = parentShell;
		this.linea = linea;
		if (reserva == null) {
			isNuevaReserva = true;
			this.reserva = new ReservaVuelo();
		} else {
			this.reserva = reserva;
		}
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(5, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Línea:");
		comboLinea = new Combo(composite, SWT.NONE);
		comboLinea.setItems(new String[] {"AeroPerlas", "AirPanama"});
		gridData = new GridData(150, 15);
		gridData.horizontalSpan = 2;
		comboLinea.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Vuelo:");
		txtVuelo = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		gridData.horizontalSpan = 1;
		txtVuelo.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Localizador:");
		txtLocalizador = new Text(composite, SWT.BORDER);
		gridData = new GridData(90,15);
		gridData.horizontalSpan = 4;
		txtLocalizador.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Fecha:");
		txtFecha = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		gridData.horizontalSpan = 1;
		txtFecha.setLayoutData(gridData);
		
		bFecha = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFecha));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Hora:");
		txtHora = new Text(composite, SWT.BORDER);
		gridData = new GridData(60,15);
		gridData.horizontalSpan = 1;
		txtHora.setLayoutData(gridData);
		
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
		
		llenarCampos();
		
		return composite;
	}

	
	@Override
	protected void llenarCampos() {
		if (isNuevaReserva) {
			setTitle("Nueva reserva de vuelo");
			setMessage("Por favor, introduzca los detalles de la nueva reserva", IMessageProvider.INFORMATION);
			
			txtFecha.setText(FechaUtil.toString(linea.getFecha(), FechaUtil.formatoFecha));
		} else {
			setTitle("Editando reserva de vuelo");
			setMessage("Por favor, especifique los detalles de la reserva", IMessageProvider.INFORMATION);
			
			System.out.println("Cargando información de campos...");
			txtLocalizador.setText(valor2Txt(reserva.getLocalizador()));
			comboLinea.setText(valor2Txt(reserva.getAerolinea()));
			txtVuelo.setText(valor2Txt(reserva.getVuelo()));
			txtFecha.setText(FechaUtil.toString(reserva.getFecha()));
			txtHora.setText(FechaUtil.toString(reserva.getFecha(), FechaUtil.formatoHora));
			txtComentario.setText(valor2Txt(reserva.getComentario()));
		}
	}
	
	
	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			if (validarSave()) {
				guardarReserva();
			} else {
				return false;
			}
		}
		return super.close();
	}
	
	
	private void guardarReserva() {
		String pLocalizador = txtLocalizador.getText().trim();
		String pAerolinea = comboLinea.getText().trim();
		String pVuelo = txtVuelo.getText().trim();
		Date pFecha = FechaUtil.toDateHour(txtFecha.getText().trim() + " " + txtHora.getText().trim());
		String pComentario = txtComentario.getText().trim();
		
		reserva.setLocalizador(pLocalizador);
		reserva.setAerolinea(pAerolinea);
		reserva.setVuelo(pVuelo);
		reserva.setFecha(pFecha);
		reserva.setComentario(pComentario);
	}
	
	
	private boolean validarSave() {
		String pLocalizador = txtLocalizador.getText();
		String pAerolinea = comboLinea.getText();
		String pVuelo = txtVuelo.getText();
		String pFecha = txtFecha.getText().trim();
		String pHora = txtHora.getText().trim();
		
		if (pLocalizador.length() == 0) {
			MessageDialog.openInformation(shell, "Validación de campo",
					"El campo de \"Localizador\" no puede quedar en blanco.");
			return false;
		} else if (pLocalizador.length() > 30) {
			MessageDialog.openInformation(shell, "Validación de campo",
					"El campo de \"Localizador\" no puede superar los 30 caracteres (" + pLocalizador.length() + ").");
			return false;
		}
		if (pAerolinea.length() == 0) {
			MessageDialog.openInformation(shell, "Validación de campo",
					"El campo de \"Línea\" no puede quedar en blanco.");
			return false;
		}
		if (pVuelo.length() == 0) {
			MessageDialog.openInformation(shell, "Validación de campo",
					"El campo de \"Vuelo\" no puede quedar en blanco.");
			return false;
		}
		
		if (FechaUtil.toDate(pFecha) == null) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"El formato de la fecha debe ser dd-MM-yyyy (Ejemplo: \"25-11-2008\").");
			return false;
		}
		if (FechaUtil.toHour(pHora) == null) {
			MessageDialog.openInformation(shell, "Validación de campos",
					"El formato de hora debe ser hh:mm aa (Ejemplo: \"11:45 AM\").");
			return false;
		}
		
		return true;
	}
	

	public IReserva getReserva() {
		return reserva;
	}

}
