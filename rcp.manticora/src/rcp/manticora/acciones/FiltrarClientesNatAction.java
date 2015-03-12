package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import rcp.manticora.dialogs.BuscarClientesNatDialog;
import rcp.manticora.filtros.ClientesNatFilter;
import rcp.manticora.views.IViewFilter;

public class FiltrarClientesNatAction implements IViewActionDelegate {
	private IViewPart view;
	private Shell shell;

	public void init(IViewPart view) {
		this.view = view;
		shell = view.getSite().getShell();
		System.out.println("INIT de acci�n: " + this.getClass().getName());
	}

	public void run(IAction action) {
		TableViewer viewer = null;
		ClientesNatFilter filtro = null;
		try {
			viewer = (TableViewer) ((IViewFilter) view).getViewer();
			viewer.resetFilters();
			BuscarClientesNatDialog dialog = new BuscarClientesNatDialog(shell, "Buscar clientes");
			if (dialog.open() == IDialogConstants.OK_ID) {
				filtro = dialog.getFiltro();
				viewer.addFilter(filtro);
			}
		} catch (Exception e) {
			mensajeError(shell, e);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	protected void mensajeError(Shell shell, Exception e) {
		MessageDialog.openError(shell, "Error en la acci�n", "Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0]);
		e.printStackTrace();
	}

}