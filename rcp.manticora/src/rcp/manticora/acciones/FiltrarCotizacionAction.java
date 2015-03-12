package rcp.manticora.acciones;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.dialogs.BuscarCotizacionesDialog2;
import rcp.manticora.views.IRefreshView;
import rcp.manticora.views.IViewFilter;

public class FiltrarCotizacionAction implements IViewActionDelegate {
	private IViewFilter view;
	private Shell shell;

	public void init(IViewPart view) {
		this.view = (IViewFilter) view;
		shell = view.getSite().getShell();
		System.out.println("INIT de vista");
	}
	
	
	public void run(IAction action) {
		((IRefreshView) view).refrescar();    // refrescamos para mostrar todos los registros (x default)
		BuscarCotizacionesDialog2 dialog = new BuscarCotizacionesDialog2(shell, "Buscar cotizaciones");
		if (dialog.open() == IDialogConstants.OK_ID) {
			ViewController vc = new ViewController();
			view.getViewer().setInput(vc.buscarCotizaciones(dialog.getDatosQuery()));
			view.getViewer().refresh();
		}
	}
	

	/*
	public void run(IAction action) {
		CotizacionesFilter filtro = null;
		view.getViewer().resetFilters();
		BuscarCotizacionesDialog2 dialog = new BuscarCotizacionesDialog2(shell, "Buscar cotizaciones");
		if (dialog.open() == IDialogConstants.OK_ID) {
			filtro = dialog.getFiltro();
			//view.getViewer().addFilter(filtro);
			ViewController vc = new ViewController();
			view.getViewer().setInput(vc.buscarCotizaciones());
			view.getViewer().refresh();
		}
	}
	*/

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
