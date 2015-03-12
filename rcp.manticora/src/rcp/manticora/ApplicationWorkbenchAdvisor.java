package rcp.manticora;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcp.manticora.model.Usuario;
import rcp.manticora.services.AutenticacionUtil;
import rcp.manticora.services.HibernateUtil;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	//private static final String PERSPECTIVE_ID = Perspective.ID;
	private static final String PERSPECTIVE_ID = AdminPerspective.ID;
	private String perspectiva;
	private Usuario usuario;
	private Shell shell;

    // método agregado para cambiar los tabs de clásico a redondeados
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		//PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_PROGRESS_ON_STARTUP, false);
		System.out.println("Preferencias inicializadas...");
		//TODO habilitar setSaveAndRestore para persistir el estado del programa
		//configurer.setSaveAndRestore(true);
	}
	

	@Override
	public void preStartup() {
		super.preStartup();
		System.out.println("Ejecutando preStartup()...");
	}
	

	@Override
	public void postStartup() {
		super.postStartup();
		System.out.println("Ejecutando postStartup()...");
	}
	
	public void setShell(Shell shell) {
		this.shell = shell;
	}



	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		System.out.println("Luego de preferencias inicializadas...");
		return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		// inicializamos el shell que utilizaremos para presentar los errores o mensajes de Hibernate
		HibernateUtil.setShell(shell);
		// y verificamos los roles del usuario para determinar qué perspectiva presentar
		usuario = AutenticacionUtil.getUsuario();
		if (usuario == null) {
			System.out.println("Usuario no especificado");
			perspectiva = PERSPECTIVE_ID;
		} else if (usuario.hasRol("Administrador")) {
			perspectiva = AdminPerspective.ID;
		} else if (usuario.hasRol("Vendedor")) {
			perspectiva = VentasPerspective.ID;
		} else if (usuario.hasRol("Operaciones")) {
			perspectiva = OperacionesPerspective.ID;
		} else if (usuario.hasRol("Contabilidad")) {
			perspectiva = ContabilidadPerspective.ID;
		} else {
			perspectiva = PERSPECTIVE_ID;
		}
		System.out.println("Usando perspectiva: " + perspectiva);
		return perspectiva;
	}
}
