package rcp.manticora.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AgregarLineaHS extends AbstractAEPDialog {

	public AgregarLineaHS(Shell parentShell, String titulo) {
		super(parentShell, titulo);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		
		Label l;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Seleccione el tipo:");
		
		Button bActividad = new Button(composite, SWT.RADIO);
		bActividad.setText("Actividad");
		
		Button bTour = new Button(composite, SWT.RADIO);
		bTour.setText("Tour");
		
		Button bReserva = new Button(composite, SWT.RADIO);
		bReserva.setText("Reserva");
		
		
		return composite;
	}

}
