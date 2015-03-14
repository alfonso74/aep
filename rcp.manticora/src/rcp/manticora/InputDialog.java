package rcp.manticora;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class InputDialog extends Dialog {
	private String message;
	private String input;

	public InputDialog(Shell parent) {
		super(parent);
		setText("Input Dialog");
		setMessage("Please enter a value:");
	}

	public InputDialog(Shell parent, int style) {
		super(parent, style);
		setText("Input Dialog");     // heredado
		setMessage("Please enter a value:");    //implementado, no heredado
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getInput() {
		return input;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public String open() {
		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		createContents(shell);
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return getInput();
	}
	
	
	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(2, true));
		
		// mostrar el mensaje
		Label label = new Label(shell, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		label.setLayoutData(gridData);
		label.setText(getMessage());
		
		// mostrar el input box
		final Text text = new Text(shell, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		text.setLayoutData(gridData);
		
		// agregar botón OK y handler para setear el input al valor introducido
		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		ok.setLayoutData(gridData);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				input = text.getText();
				shell.close();
			}
		});
		
		// botón de Cancel
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		cancel.setLayoutData(gridData);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				input = null;
				shell.close();
			}
		});
		
		// botón de OK como default
		shell.setDefaultButton(ok);
	}


}
