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
import rcp.manticora.model.ReservaBoleto;
import rcp.manticora.services.FechaUtil;

public class ReservaBoletosDialog extends AbstractAEPTitleAreaDialog
		implements IReservaDialog {
	
	private final String pluginId = Application.PLUGIN_ID;
	private boolean isNuevaReserva;
	
	private Text txtLugar;
	private Text txtFecha;
	private Button bFecha;
	private Text txtHora;
	private Text txtComentario;
	
	private ImageDescriptor image;
	private LineaActividad linea;
	private ReservaBoleto reserva;
	private Shell shell;
	
	public ReservaBoletosDialog(Shell parentShell, LineaActividad linea,
			ReservaBoleto reserva) {
		super(parentShell);
		this.shell = parentShell;
		this.linea = linea;
		if (reserva == null) {
			isNuevaReserva = true;
			this.reserva = new ReservaBoleto();
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
		l.setText("Lugar:");
		txtLugar = new Text(composite, SWT.BORDER);
		gridData = new GridData(160,15);
		gridData.horizontalSpan = 4;
		txtLugar.setLayoutData(gridData);
		
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
		l.setAlignment(SWT.RIGHT);
		gridData = new GridData(50,15);
		l.setLayoutData(gridData);
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
		
		return parent;
	}

	
	@Override
	protected void llenarCampos() {
		if (isNuevaReserva) {
			setTitle("Nueva reserva de boletos");
			setMessage("Por favor, introduzca los detalles de la nueva reserva", IMessageProvider.INFORMATION);
			
			txtFecha.setText(FechaUtil.toString(linea.getFecha(), FechaUtil.formatoFecha));
		} else {
			setTitle("Editando reserva de boletos");
			setMessage("Por favor, especifique los detalles de la reserva", IMessageProvider.INFORMATION);
			
			System.out.println("Cargando información de campos...");
			txtLugar.setText(valor2Txt(reserva.getUbicacion()));
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
		String pLugar = txtLugar.getText().trim();
		Date pFecha = FechaUtil.toDateHour(txtFecha.getText().trim() + " " + txtHora.getText().trim());
		String pComentario = txtComentario.getText().trim();
		
		reserva.setUbicacion(pLugar);
		reserva.setFecha(pFecha);
		reserva.setComentario(pComentario);
	}
	
	
	private boolean validarSave() {
		String pLugar = txtLugar.getText().trim();
		String pFecha = txtFecha.getText().trim();
		String pHora = txtHora.getText().trim();
		
		if (pLugar.length() == 0) {
			MessageDialog.openInformation(shell, "Validación de campo",
					"El campo de \"Lugar\" no puede quedar en blanco.");
			return false;
		} else if (pLugar.length() > 30) {
			MessageDialog.openInformation(shell, "Validación de campo",
					"El campo de \"Lugar\" no puede superar los 30 caracteres (" + pLugar.length() + ").");
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
