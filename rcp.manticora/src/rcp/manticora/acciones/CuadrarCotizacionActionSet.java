package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import rcp.manticora.dialogs.CuadrarCotizacionDialog;
import rcp.manticora.model.LineaCotizacion;

public class CuadrarCotizacionActionSet implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void run(IAction action) {
		CuadrarCotizacionDialog c = new CuadrarCotizacionDialog(window.getShell(), null, "Cuadrar precios de cotización");
		c.open();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		Object linea = ((IStructuredSelection) selection).getFirstElement();
		if (!(linea instanceof LineaCotizacion)) {
			System.out.println("CAMBIOF: " + linea);
			action.setEnabled(false);
		} else {
			System.out.println("CAMBIOT: " + linea);
			action.setEnabled(true);
		}
	}


}
