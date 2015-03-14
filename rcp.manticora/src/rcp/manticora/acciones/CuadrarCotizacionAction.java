package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import rcp.manticora.dialogs.CuadrarCotizacionDialog;

public class CuadrarCotizacionAction implements IEditorActionDelegate {
	private IEditorPart targetEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}

	public void run(IAction action) {
		CuadrarCotizacionDialog c = new CuadrarCotizacionDialog(targetEditor.getSite().getShell(), null, "Cuadrar precios de cotización");
		c.open();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}
