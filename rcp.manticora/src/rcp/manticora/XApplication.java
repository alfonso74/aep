package rcp.manticora;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.hibernate.HibernateException;

import rcp.manticora.dialogs.ConnectionData;
import rcp.manticora.dialogs.LoginDialog;
import rcp.manticora.model.HibernateController;
import rcp.manticora.services.AutenticacionUtil;

/**
 * This class controls all aspects of the application's execution
 */
public class XApplication implements IApplication {
	public static final String PLUGIN_ID = "rcp.manticora";
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Iniciando manticora...");
		HibernateController hibController = null;
		
		Display display = PlatformUI.createDisplay();
		try {
			hibController = new HibernateController();
			Platform.endSplash();
			
			LoginDialog login = new LoginDialog(null);
			if (login.open() != IDialogConstants.OK_ID) {
				return IApplication.EXIT_OK;
			}
			
			ConnectionData datos = login.getDatos();
			if (AutenticacionUtil.verificarUsuario(datos)) {
				System.out.println("Usuario autenticado: " + datos.getUsuario());
			} else {
				System.err.println("Usuario no autenticado correctamente: " + datos.getUsuario());
				return IApplication.EXIT_OK;
			}

			ApplicationWorkbenchAdvisor wbAdvisor = new ApplicationWorkbenchAdvisor();
			//wbAdvisor.setUsuario(AutenticacionUtil.getUsuario());
			
			
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
		} catch (HibernateException he) {
			System.err.println("Exception de Hibernate...");
			he.printStackTrace();
			return null;
		} finally {
			display.dispose();
			//controller.close();
			if (hibController != null) hibController.close();
			System.out.println("manticora finalizado");
		}
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
