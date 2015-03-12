package rcp.manticora.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CancelarDocumentoDialog extends Dialog {
	private Text txtMotivo;
	private MyInputDialogData data;

	public CancelarDocumentoDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Acción de cancelar");
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		composite.setLayout(gridLayout);
		
		Label l;
		GridData gridData;

		l = new Label(composite, SWT.NONE);
		l.setText("Por favor, indique el motivo de cancelación:");
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		l.setLayoutData(gridData);
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		//gridData.widthHint = 225;
		gridData.heightHint = 5;
		gridData.horizontalSpan = 1;
		l.setLayoutData(gridData);
		
		txtMotivo = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 45;
		gridData.widthHint = 250;
		txtMotivo.setLayoutData(gridData);
		
		return composite;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected void okPressed() {
		data = new MyInputDialogData();
		data.setTextResponse(txtMotivo.getText());
		data.setButtonResponse(true);
		super.okPressed();
	}
	
	public MyInputDialogData getDatos() {
		return data;
	}
}
