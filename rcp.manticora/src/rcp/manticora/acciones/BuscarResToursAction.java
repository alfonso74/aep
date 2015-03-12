package rcp.manticora.acciones;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import rcp.manticora.dialogs.BuscarResToursDialog;
import rcp.manticora.views.IRefreshView;
import rcp.manticora.views.IViewFilter;

public class BuscarResToursAction implements IViewActionDelegate {
	private IViewFilter view;
	private Shell shell;

	public void init(IViewPart view) {
		this.view = (IViewFilter) view;
		shell = view.getSite().getShell();
		System.out.println("INIT de vista");
	}

	
	public void run(IAction action) {
		((IRefreshView) view).refrescar();      // refrescamos para mostrar todos los registros (x default)
		BuscarResToursDialog dialog = new BuscarResToursDialog(shell, "Buscar reservas");
		if (dialog.open() == IDialogConstants.OK_ID) {
			System.out.println("Viewer: " + view.getViewer());
			view.getViewer().setInput(dialog.getDTO().buscarReservasTours());
			view.getViewer().refresh();
			System.out.println("OK");
		}
	}

	
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
