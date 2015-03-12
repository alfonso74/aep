package rcp.manticora.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.controllers.UsuariosController;
import rcp.manticora.model.Usuario;
import rcp.manticora.services.AutenticacionUtil;

public class CambiarPasswordDialog extends AbstractAEPDialog {
	public static final String ID = "cambiarPassword";
	private Text txtPasswordViejo;
	private Text txtPasswordNuevo;
	private Text txtPasswordConf;
	
	private UsuariosController controller;
	
	private Shell shell;

	
	public CambiarPasswordDialog(Shell parentShell) {
		super(parentShell, "Actualización de password");
		this.shell = parentShell;
		controller = new UsuariosController(ID);
	}
	
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout;
		
		Composite composite = new Composite(parent, SWT.NONE);
		layout = new GridLayout(2, false);
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		composite.setLayout(layout);
		
		Label l = new Label(composite, SWT.NONE);
		l.setText("Password actual:");
		txtPasswordViejo = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtPasswordViejo.setLayoutData(new GridData(100, 15));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Password nuevo:");
		txtPasswordNuevo = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtPasswordNuevo.setLayoutData(new GridData(100, 15));

		l = new Label(composite, SWT.NONE);
		l.setText("Confirmar password:");
		txtPasswordConf = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtPasswordConf.setLayoutData(new GridData(100, 15));
		
		return composite;
	}
	
	
	@Override
	protected void okPressed() {
		try {
			if (validarCampos()) {
				// cargamos el registro del usuario
				Usuario usuario = AutenticacionUtil.getUsuario();
				// verificamos el password actual vs el existente en la base de datos
				String pPasswordActual01 = usuario.getPassword();
				String pPasswordActual02 = AutenticacionUtil.encodePasswordMD5(txtPasswordViejo.getText());
				if (pPasswordActual01.equals(pPasswordActual02)) {
					// codificamos el password nuevo
					String pPassword = AutenticacionUtil.encodePasswordMD5(txtPasswordNuevo.getText());
					// actualizar password y guardar registro del usuario
					usuario.setPassword(pPassword);
					controller.doSave(usuario);
					// confirmar cambio y cerrar diálogo
					MessageDialog.openInformation(shell, "Actualización de password", "El password se ha actualizado exitosamente.");
					System.out.println("Actualizar registro y cerrar diálogo.");
					super.okPressed();
				} else {
					MessageDialog.openError(shell, "Error de validación", "El password introducido en el campo de \"Password actual\" no coincide\ncon el password del usuario.  Por favor verifique.");
				}
			}
		}catch (Exception e) {
			mensajeError(shell, e);
		}
	}
	
	
	private boolean validarCampos() {
		String pPassViejo = txtPasswordViejo.getText();
		String pPassNuevo = txtPasswordNuevo.getText();
		String pPassConf = txtPasswordConf.getText();
		
		if (pPassViejo.equals("")) {
			MessageDialog.openError(shell, "Error de validación", "El campo de \"Password actual\" no puede quedar en blanco.");
			txtPasswordViejo.setFocus();
			return false;
		}
		if (pPassNuevo == "") {
			MessageDialog.openError(shell, "Error de validación", "El campo de \"Password nuevo\" no puede quedar en blanco.");
			txtPasswordNuevo.setFocus();
			return false;
		}
		if (pPassConf == "") {
			MessageDialog.openError(shell, "Error de validación", "El campo de \"Confirmar password\" no puede quedar en blanco.");
			txtPasswordConf.setFocus();
			return false;
		}
		if (!pPassNuevo.equals(pPassConf)) {
			MessageDialog.openError(shell, "Error de validación", "El nuevo password no ha sido confirmado correctamente (Campo de \"Confirmar password\").");
			txtPasswordConf.setFocus();
			return false;
		}
		
		return true;
	}


	@Override
	public boolean close() {
		controller.finalizar(ID);
		return super.close();
	}

}
