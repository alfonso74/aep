package rcp.manticora.dialogs;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.services.ComboData;

public class AsignarVendedorDialog extends Dialog {
	private ComboDataController cdController;
	private List listaVendedor;
	private ComboData cdVendedor;
	private Shell parent;       // shell de pantalla principal
	private Shell shell;        // shell del dialogBox

	private Button bOkay;
	private Button bCancelar;
	private MyInputDialogData data;

	public AsignarVendedorDialog(Shell parent) {
		super(parent);
		this.parent = parent;
		cdController = new ComboDataController();
	}

	public AsignarVendedorDialog(Shell parent, int style) {
		super(parent, style);
		this.parent = parent;
		cdController = new ComboDataController();
	}
	
	public MyInputDialogData open () {
		data = new MyInputDialogData();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(getText());
		shell.setSize(325,150);		
		shell.setLayout(new RowLayout());
		
		final Composite composite = new Composite(shell, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label l;
		GridData gridData;

		l = new Label(composite, SWT.NONE);
		l.setText("Por favor, seleccione un vendedor:");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		l.setLayoutData(gridData);
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		//gridData = new GridData();
		//gridData.widthHint = 225;
		gridData.heightHint = 5;
		gridData.horizontalSpan = 2;
		l.setLayoutData(gridData);
		cdVendedor = cdController.getComboDataVendedoresActivos();
		listaVendedor = new List(composite, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		listaVendedor.setItems(cdVendedor.getTexto());
		//gridData = new GridData(220, 60);
		gridData = new GridData();
		gridData.widthHint = 200;
		gridData.heightHint = 55;
		gridData.verticalSpan = 2;
		listaVendedor.setLayoutData(gridData);
		
		bOkay = new Button (composite, SWT.PUSH);
		bOkay.setText ("Ok");
		gridData = new GridData();
		gridData.widthHint = 70;
		gridData.heightHint = 20;
		gridData.verticalAlignment = SWT.BOTTOM;
		bOkay.setLayoutData(gridData);
		bCancelar = new Button (composite, SWT.PUSH);
		bCancelar.setText ("Cancelar");
		bCancelar.setLayoutData(new GridData(70,20));

		bOkay.addListener(SWT.Selection, this.crearListener("Ok"));
		bCancelar.addListener(SWT.Selection, this.crearListener(("Cancelar")));
		
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		return data;
	}
	
	public Listener crearListener(final String codigoBoton) {
		return new Listener() {
			public void handleEvent (Event event) {
				System.out.println("Widget: " + event.widget);
				if (codigoBoton.equals("Ok")) {
					data.setButtonResponse(true);
					data.setTextResponse(listaVendedor.getItem(listaVendedor.getSelectionIndex()));
				} else {
					data.setButtonResponse(false);
					data.setTextResponse("");
				}
				shell.close ();
			}
		};
	}

}
