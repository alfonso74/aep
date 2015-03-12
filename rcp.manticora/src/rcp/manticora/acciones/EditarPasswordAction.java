package rcp.manticora.acciones;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import rcp.manticora.dialogs.CambiarPasswordDialog;

public class EditarPasswordAction implements IWorkbenchWindowActionDelegate {
	private Shell shell;

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.shell = window.getShell();
	}

	public void run(IAction action) {
		CambiarPasswordDialog d = new CambiarPasswordDialog(shell);
		d.open();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
