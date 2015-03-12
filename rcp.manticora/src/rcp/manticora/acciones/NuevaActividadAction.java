package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.model.Template;

public class NuevaActividadAction implements IEditorActionDelegate {
	private IEditorPart targetEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}

	public void run(IAction action) {
		System.out.println("JA1: " + targetEditor.isDirty());
		Template x = (Template) ((CommonEditorInput) targetEditor.getEditorInput()).getElemento();
		System.out.println("Nombre: " + x.getNombre());
	}

	public void selectionChanged(IAction action, ISelection selection) {
		System.out.println("JA2");
	}
}

