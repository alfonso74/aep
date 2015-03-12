package rcp.manticora.dialogs;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import rcp.manticora.model.LineaActividad;
/**
 * <p>Dialogo para capturar la acción que el usuario desea realizar con relación a las reservas.</p>
 * 
 * @param parentShell shell para dibujar el diálogo
 * @param linea actividad sobre la que vamos a trabajar
 *
 */


public class ReservaGestionDialog extends AbstractAEPTitleAreaDialog {
	
	private LineaActividad linea;
	
	private Button bOpcion1;
	private Button bOpcion2;
	private Button bOpcion3;
	private Button bOpcion4;
	
	private String accion;
	
	/**
	 * Constructor
	 * @param parentShell shell para dibujar el diálogo
	 * @param linea actividad sobre la que vamos a trabajar
	 */
	public ReservaGestionDialog(Shell parentShell, LineaActividad linea) {
		super(parentShell);
		this.linea = linea;
	}
	
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		/*
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Seleccione una acción:");
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 5;
		gridData.horizontalSpan = 1;
		l.setLayoutData(gridData);
		*/
		
		bOpcion1 = new Button(composite, SWT.RADIO);
		bOpcion1.setText("Crear nueva reserva y asignar a la actividad seleccionada");
		bOpcion2 = new Button(composite, SWT.RADIO);
		bOpcion2.setText("Asignar reserva existente a la actividad seleccionada");
		bOpcion3 = new Button(composite, SWT.RADIO);
		bOpcion3.setText("Consultar reserva asignada");
		bOpcion4 = new Button(composite, SWT.RADIO);
		bOpcion4.setText("Borrar asignación de reserva");
		bOpcion1.setEnabled(false);
		bOpcion2.setEnabled(false);
		bOpcion3.setEnabled(false);
		bOpcion4.setEnabled(false);
		
		llenarCampos();
		
		return composite;
	}

	
	@Override
	protected void llenarCampos() {
		setTitle("Gestión de reservas");
		setMessage("Por favor, indique el tipo de acción a realizar");
		
		if (linea.hasReservas()) {
			bOpcion3.setEnabled(true);
			bOpcion4.setEnabled(true);
		} else {
			bOpcion1.setEnabled(true);
			bOpcion2.setEnabled(true);
		}
	}


	@Override
	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			guardarSeleccion();
		}
		return super.close();
	}
	
	
	private void guardarSeleccion() {
		accion = null;
		if (bOpcion1.getSelection()) {
			// estamos creando una nueva reserva
			accion = "crear";
		} else if (bOpcion2.getSelection()) {
			// estamos asignando una reserva existente
			accion = "asignar";
		} else if (bOpcion3.getSelection()) {
			// queremos borrar una asignación de reserva
			accion = "consultar";
		} else if (bOpcion4.getSelection()) {
			accion = "borrar";
		}
	}
	
	
	public String getAccion() {
		return accion;
	}

}
