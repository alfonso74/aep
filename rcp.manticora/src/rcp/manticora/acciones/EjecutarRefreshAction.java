package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import rcp.manticora.editors.CotizacionesEditor;

public class EjecutarRefreshAction implements IEditorActionDelegate {
	private CotizacionesEditor targetEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = (CotizacionesEditor) targetEditor;
	}

	public void run(IAction action) {
		System.out.println("Se realiza refresh del formulario");
		targetEditor.calcularTotales();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}
