package rcp.manticora;


import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcp.manticora.model.Usuario;

public class XApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	//private static final String PERSPECTIVE_ID = Perspective.ID;
	private static final String PERSPECTIVE_ID = AdminPerspective.ID;
	private String perspectiva;
	private Usuario usuario;

    // método agregado para cambiar los tabs de clásico a redondeados
	public void initialize(IWorkbenchConfigurer configurer) {
		//super.initialize(configurer);
		//PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		//PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_PROGRESS_ON_STARTUP, false);
		System.out.println("Preferencias inicializadas...");
		//TODO habilitar setSaveAndRestore para persistir el estado del programa
		//configurer.setSaveAndRestore(true);
	}

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		if (usuario == null) {
			perspectiva = PERSPECTIVE_ID;
		} else if (usuario.hasRol("Administrador")) {
			perspectiva = AdminPerspective.ID;
		} else if (usuario.hasRol("Vendedor")) {
			perspectiva = VentasPerspective.ID;
		} else if (usuario.hasRol("Operaciones")) {
			perspectiva = OperacionesPerspective.ID;
		} else if (usuario.hasRol("Contabilidad")) {
			perspectiva = AdminPerspective.ID;
		} else {
			perspectiva = PERSPECTIVE_ID;
		}
		System.out.println("Usando perspectiva: " + perspectiva);
		return perspectiva;
	}
	
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
