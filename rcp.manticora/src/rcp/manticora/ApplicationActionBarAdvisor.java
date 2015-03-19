package rcp.manticora;

import org.eclipse.jface.action.IAction;
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

import rcp.manticora.acciones.SimpleReportFactory;


public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction saveAction;
	private IWorkbenchAction preferencesAction;
	private IWorkbenchAction passwordAction;
	
	private IWorkbenchAction editActionSetsAction;
	
	private IAction reporteProductos;
	private IAction reporteCotizacionesMes;
	private IAction reporteCotizacionesMesInicio;
	private IAction reporteCotizacionesMesGira;
	private IAction reporteCotizacionesVendedor;
	private IAction reporteCotizacionesRedViajes;
	private IAction reporteCotizacionesComisiones;
	private IAction reportePaxs;
	private IAction reportePaxsGiras;
	
	private IAction reporteVentasPorVendedor;
	private IAction reporteComparativoVentasPorVendedor;
	private IAction reporteComparativoVentas;
	

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
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
    	passwordAction = ActionFactory.COPY.create(window);
    	passwordAction.setText("Pass&word...");
    	register(passwordAction);
    	editActionSetsAction = ActionFactory.EDIT_ACTION_SETS.create(window);
    	editActionSetsAction.setText("ActionSet?");
    	register(editActionSetsAction);
    	
    	SimpleReportFactory factory = new SimpleReportFactory();
    	reporteProductos = factory.createAction("Productos", window);
    	reporteCotizacionesMes = factory.createAction("Cotizaciones x Mes", window);
    	reporteCotizacionesMesInicio = factory.createAction("Cotizaciones x Mes de Inicio", window);
    	reporteCotizacionesMesGira = factory.createAction("Cotizaciones x Mes de Gira", window);
    	reporteCotizacionesVendedor = factory.createAction("Cotizaciones x Vendedor", window);
    	reporteCotizacionesRedViajes = factory.createAction("Cotizaciones x Red de Viajes", window);
    	reporteCotizacionesComisiones = factory.createAction("Cotizaciones con Comisiones", window);
    	reportePaxs = factory.createAction("Paxs x Mes", window);
    	reportePaxsGiras = factory.createAction("Paxs x Giras", window);
    	
    	reporteVentasPorVendedor = factory.createAction("Ventas por Vendedor", window);
    	reporteComparativoVentasPorVendedor = factory.createAction("Comparativo de Ventas (x Vendedor)", window);
    	reporteComparativoVentas = factory.createAction("Comparativo de Ventas Anuales", window);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	MenuManager appMenu1 = new MenuManager("&Aplicacion", "manticora");
    	appMenu1.add(saveAction);
    	// separador para grupo de additions
    	appMenu1.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    	MenuManager appMenuL2 = new MenuManager("Cotización", "level2");
    	MenuManager appMenuL3 = new MenuManager(null, "level3");
    	appMenuL2.add(appMenuL3);
    	appMenu1.add(appMenuL2);
    	// separador para grupo de additions2
    	appMenu1.add(new Separator("additions2"));
    	// separador para "Preferencias..." y "Salir"
    	appMenu1.add(new Separator());
    	appMenu1.add(preferencesAction);
    	appMenu1.add(exitAction);
    	
    	MenuManager reportsMenu = new MenuManager("&Reportes", "reportes");
    	//reportsMenu.add(editActionSetsAction);
    	reportsMenu.add(reporteProductos);
    	reportsMenu.add(reporteCotizacionesMes);
    	reportsMenu.add(reporteCotizacionesMesInicio);
    	reportsMenu.add(reporteCotizacionesMesGira);
    	reportsMenu.add(reporteCotizacionesVendedor);
    	reportsMenu.add(reporteCotizacionesRedViajes);
    	reportsMenu.add(reporteCotizacionesComisiones);
    	reportsMenu.add(reportePaxs);
    	reportsMenu.add(reportePaxsGiras);
    	reportsMenu.add(new Separator());
    	reportsMenu.add(reporteVentasPorVendedor);
    	reportsMenu.add(reporteComparativoVentasPorVendedor);
    	reportsMenu.add(reporteComparativoVentas);
    	
    	MenuManager helpMenu = new MenuManager("&Ayuda", "ayuda");
    	helpMenu.add(aboutAction);
    	
    	// registramos los menús con el MenuManager principal (objeto menuBar)
    	menuBar.add(appMenu1);
    	// placeholder de "additions" (aparece entre Aplicación y Ayuda)
    	menuBar.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    	menuBar.add(reportsMenu);
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
