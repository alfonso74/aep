package rcp.manticora;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class XApplicationActionBarAdvisor extends ActionBarAdvisor {
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction saveAction;
	private IWorkbenchAction preferencesAction;

    public XApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    	exitAction = ActionFactory.QUIT.create(window);
    	exitAction.setText("&Salir");
    	register(exitAction);
    	aboutAction = ActionFactory.ABOUT.create(window);
    	aboutAction.setText("Acerca de");
    	register(aboutAction);
    	saveAction = ActionFactory.SAVE.create(window);
    	saveAction.setText("&Guardar");
    	register(saveAction);
    	preferencesAction = ActionFactory.PREFERENCES.create(window);
    	preferencesAction.setText("&Preferencias...");
    	register(preferencesAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	MenuManager appMenu1 = new MenuManager("&Aplicacion", "manticora");
    	appMenu1.add(saveAction);
    	appMenu1.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    	//appMenu1.add(new Separator());
    	appMenu1.add(preferencesAction);
    	MenuManager appMenuL2 = new MenuManager("Cotización", "level2");
    	MenuManager appMenuL3 = new MenuManager(null, "level3");
    	//appMenuLvl2.add(preferencesAction);
    	appMenuL2.add(appMenuL3);
    	appMenu1.add(appMenuL2);
    	appMenu1.add(new Separator());
    	appMenu1.add(exitAction);
    	MenuManager helpMenu = new MenuManager("&Ayuda", "ayuda");
    	helpMenu.add(aboutAction);
    	menuBar.add(appMenu1);
    	// placeholder de "additions" (aparece entre Aplicación y Ayuda)
    	menuBar.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    	menuBar.add(helpMenu);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
    	IToolBarManager toolBar = new ToolBarManager(coolBar.getStyle());
    	coolBar.add(toolBar);
    	toolBar.add(saveAction);
    	coolBar.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    	coolBar.add(new Separator("toolbar"));
    }
    
}
