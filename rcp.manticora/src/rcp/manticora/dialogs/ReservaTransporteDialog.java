package rcp.manticora.dialogs;

import java.util.Date;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.model.LineaActividad;
import rcp.manticora.model.ReservaTransporte;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;

public class ReservaTransporteDialog extends AbstractAEPTitleAreaDialog
	implements IReservaDialog {
	
	private ComboDataController controller;
	private Text txtOrigen;
	private Text txtDestino;
	private Text txtHoraIni;
	private Text txtHoraFin;
	private Combo comboTrans;
	private Combo comboTipo;
	private Combo comboServicio;
	private Text txtComentario;
	// campos existentes pero no presentados en el formulario
	private String txtFechaOrigen;
	private String txtFechaDestino;
	
	private ComboData cdKeyword;
	private ComboData cdTrans;
	private boolean isNuevaReserva = false;
	
	private LineaActividad linea;
	private ReservaTransporte reserva;
	private Shell shell;

	
	public ReservaTransporteDialog(Shell parentShell, LineaActividad linea, ReservaTransporte reserva) {
		super(parentShell);
		this.shell = parentShell;
		this.linea = linea;
		if (reserva == null) {
			isNuevaReserva = true;
			this.reserva = new ReservaTransporte();
		} else {
			this.reserva = reserva;
		}
		controller = new ComboDataController();
	}
	
	public ReservaTransporteDialog(Shell parentShell, ReservaTransporte reserva) {
		super(parentShell);
		this.shell = parentShell;
		this.reserva = reserva;
		controller = new ComboDataController();
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
		l.setText("Origen:");
		txtOrigen = new Text(composite, SWT.BORDER);
		gridData = new GridData(150,15);
		txtOrigen.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		gridData = new GridData(60,15);
		l.setLayoutData(gridData);
		l.setText("Hora:");
		l.setAlignment(SWT.RIGHT);
		txtHoraIni = new Text(composite, SWT.BORDER);
		gridData = new GridData(50,15);
		txtHoraIni.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Destino:");
		txtDestino = new Text(composite, SWT.BORDER);
		gridData = new GridData(150,15);
		txtDestino.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		gridData = new GridData(60,15);
		l.setLayoutData(gridData);
		l.setText("Hora:");
		l.setAlignment(SWT.RIGHT);
		txtHoraFin = new Text(composite, SWT.BORDER);
		gridData = new GridData(50,15);
		txtHoraFin.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Transportista:");
		comboTrans = new Combo(composite, SWT.READ_ONLY);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 1;
		comboTrans.setLayoutData(gridData);
		cdTrans = controller.getComboDataTransportes();
		comboTrans.setItems(cdTrans.getTexto());
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		l.setAlignment(SWT.RIGHT);
		gridData = new GridData(60,15);
		gridData.horizontalSpan = 1;
		l.setLayoutData(gridData);
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		cdKeyword = controller.getComboDataKeyword("Tipo de vehículo");
		comboTipo.setItems(cdKeyword.getTexto());
		
		l = new Label(composite, SWT.NONE);
		l.setText("Servicio:");
		comboServicio = new Combo(composite, SWT.READ_ONLY);
		comboServicio.setItems(new String[] {"Compartido", "Privado"});
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		comboServicio.setLayoutData(gridData);
		
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

		llenarCampos();
		
		return composite;
	}
	
	protected void llenarCampos() {
// Set the title
		setTitle("Reserva de transporte terrestre ");		
// Set the message
		if (linea != null) {
			setMessage("Por favor, introduzca los detalles de la reserva (" + FechaUtil.toString(linea.getFecha()) + ")", IMessageProvider.INFORMATION);
		} else {
			setMessage("Por favor, introduzca los detalles de la reserva", IMessageProvider.INFORMATION);
		}
		
		if (isNuevaReserva) {
			System.out.println("Nueva reserva...");
			txtFechaOrigen = FechaUtil.toString(new Date());
			txtFechaDestino = FechaUtil.toString(new Date());
			txtHoraIni.setText("01:00 PM");
		} else {
			System.out.println("Cargando información de campos...");
			txtOrigen.setText(valor2Txt(reserva.getOrigen()));
			txtDestino.setText(valor2Txt(reserva.getDestino()));
			txtFechaOrigen = FechaUtil.toString(reserva.getFechaOrigen(), FechaUtil.formatoFecha);
			txtFechaDestino = FechaUtil.toString(reserva.getFechaDestino(), FechaUtil.formatoFecha);
			txtHoraIni.setText(FechaUtil.toString(reserva.getFechaOrigen(), FechaUtil.formatoHora));
			txtHoraFin.setText(FechaUtil.toString(reserva.getFechaDestino(), FechaUtil.formatoHora));
			comboTrans.select(comboTrans.indexOf(reserva.getDspTransporte()));
			comboTipo.select(comboTipo.indexOf(reserva.getTipo()));
			comboServicio.select(comboServicio.indexOf(reserva.getServicio()));
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
		Long pTransporte = cdTrans.getKeyAsLongByIndex(comboTrans.getSelectionIndex());
		String pOrigen = txtOrigen.getText();
		String pDestino = txtDestino.getText();
		Date pFechaOrigen = FechaUtil.toDateHour(txtFechaOrigen + " " + txtHoraIni.getText().trim());
		Date pFechaDestino = FechaUtil.toDateHour(txtFechaDestino + " " + txtHoraFin.getText().trim());
		String pTipo = comboTipo.getItem(comboTipo.getSelectionIndex());
		String pServicio = comboServicio.getItem(comboServicio.getSelectionIndex());
		String pComentario = txtComentario.getText();
		
		reserva.setIdTransporte(pTransporte);
		reserva.setDspTransporte(comboTrans.getText());
		reserva.setOrigen(pOrigen);
		reserva.setDestino(pDestino);
		reserva.setFechaOrigen(pFechaOrigen);
		reserva.setFechaDestino(pFechaDestino);
		reserva.setTipo(pTipo);
		reserva.setServicio(pServicio);
		reserva.setComentario(pComentario);
	}
	
	
	public boolean validarSave() {
		String pHoraOrigen = txtHoraIni.getText().trim();
		String pHoraDestino = txtHoraFin.getText().trim();
		
		if (FechaUtil.toHour(pHoraOrigen) == null) {
			MessageDialog.openInformation(shell, "Validación de campos",
					"El formato de la hora de origen debe ser hh:mm aa (Ejemplo: \"11:45 AM\").");
			return false;
		}
		if (FechaUtil.toHour(pHoraDestino) == null) {
			MessageDialog.openInformation(shell, "Validación de campos",
					"El formato de hora de destino debe ser hh:mm aa (Ejemplo: \"11:45 AM\").");
			return false;
		}
		
		return true;
	}
	
	
	public ReservaTransporte getReserva() {
		return reserva;
	}
}
