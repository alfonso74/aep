package rcp.manticora.dialogs;


import org.eclipse.jface.dialogs.IDialogConstants;
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
import rcp.manticora.filtros.ClientesNatFilter;
import rcp.manticora.services.ComboData;

public class BuscarClientesNatDialog extends AbstractAEPDialog {
	private Text txtCodigo;
	private Text txtNombre;
	private Text txtApellido;
	private Text txtIdentificacion;
	private Combo comboPais;
	private ComboData cdPais;
	
	private ClientesNatFilter filtro;
	
	private ComboDataController cdController;
	
	public BuscarClientesNatDialog(Shell parentShell, String titulo) {
		super(parentShell, titulo);
		cdController = new ComboDataController();
		cdPais = cdController.getComboDataPaises();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout (2, false);
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		composite.setLayout(gridLayout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		l.setLayoutData(gridData);
		l.setText("Por favor, introduzca los criterios de búsqueda");
		
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
	    gridData = new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
		gridData.heightHint = 10;
		//gridData.horizontalSpan = 5;
		l.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Código:");
		txtCodigo = new Text(composite, SWT.BORDER);
		gridData = new GridData(40, 15);
		gridData.horizontalSpan = 1;
		txtCodigo.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Nombre:");
		txtNombre = new Text(composite, SWT.BORDER);
		gridData = new GridData(120, 15);
		gridData.horizontalSpan = 1;
		txtNombre.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Apellido:");
		txtApellido = new Text(composite, SWT.BORDER);
		gridData = new GridData(120, 15);
		gridData.horizontalSpan = 1;
		txtApellido.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Identificación:");
		txtIdentificacion = new Text(composite, SWT.BORDER);
		gridData = new GridData(80, 15);
		gridData.horizontalSpan = 1;
		txtIdentificacion.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("País:");
		comboPais = new Combo(composite, SWT.NONE);
		comboPais.setItems(cdPais.getTexto());
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		comboPais.setLayoutData(gridData);
		
		return composite;
	}
	
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "&Buscar", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	
	@Override
	protected void okPressed() {
		Long pCodigo = txt2Long(txtCodigo.getText());
		String pNombre = txtNombre.getText();
		String pApellido = txtApellido.getText();
		String pIdentificacion = txtIdentificacion.getText();
		String pPais = comboPais.getText();
		
		filtro = new ClientesNatFilter(pCodigo, pNombre, pApellido, pIdentificacion,
				pPais);
		super.okPressed();
	}

	
	public ClientesNatFilter getFiltro() {
		return filtro;
	}
	
}
