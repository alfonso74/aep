package rcp.manticora;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.hibernate.HibernateException;

import rcp.manticora.dialogs.ConnectionData;
import rcp.manticora.dialogs.LoginDialog;
import rcp.manticora.model.HibernateController;
import rcp.manticora.services.AutenticacionUtil;
import rcp.manticora.services.HibernateUtil;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {
	public static final String PLUGIN_ID = "rcp.manticora";
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		
		System.out.println("Iniciando manticora...");
		HibernateController hibController = null;
		Display display = PlatformUI.createDisplay();
		
		//hibController = new HibernateController();
		
		try {
			
			// El código siguiente es utilizado para el login gris (sin splash screen)
			/*
			Platform.endSplash();
			
			boolean autenticado = false;
			while (!autenticado) {
				LoginDialog login = new LoginDialog(display.getActiveShell());
				if (login.open() != IDialogConstants.OK_ID) {
					return IApplication.EXIT_OK;
				}
				ConnectionData datos = login.getDatos();
				autenticado = AutenticacionUtil.verificarUsuario(datos); 
				if (autenticado) {
					System.out.println("Usuario autenticado: " + datos.getUsuario());
				} else {
					System.err.println("Usuario no autenticado correctamente: " + datos.getUsuario());
					MessageDialog.openError(display.getActiveShell(), "Error de validación", "El password introducido es incorrecto, por favor verifique.");
					//return IApplication.EXIT_OK;
				}
			}
			*/
			

			ApplicationWorkbenchAdvisor wbAdvisor = new ApplicationWorkbenchAdvisor();
			//wbAdvisor.setUsuario(AutenticacionUtil.getUsuario());
			
			int returnCode = PlatformUI.createAndRunWorkbench(display, wbAdvisor);
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
			
		} catch (HibernateException he) {
			System.err.println("ExceptionX de Hibernate...");
			he.printStackTrace();
			return null;
		} catch (Exception e) {
			mensajeError(display.getActiveShell(), e);
			e.printStackTrace();
			return null;
		} finally {
			display.dispose();
			if (hibController != null) hibController.close();
			System.out.println("manticora finalizado");
		}
	}
	
	private void mensajeError(Shell shell, Exception e) {
		MessageDialog.openError(shell, "Error en la aplicación", "Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0]);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
