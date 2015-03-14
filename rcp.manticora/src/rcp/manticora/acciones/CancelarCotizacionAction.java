package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import rcp.manticora.dialogs.CancelarDocumentoDialog;
import rcp.manticora.dialogs.MyInputDialogData;
import rcp.manticora.editors.CotizacionesEditor;

public class CancelarCotizacionAction implements IEditorActionDelegate {
	private CotizacionesEditor targetEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = (CotizacionesEditor) targetEditor;
	}

	public void run(IAction action) {
		CancelarDocumentoDialog dialog = new CancelarDocumentoDialog(targetEditor.getSite().getShell());
		if (dialog.open() == IDialogConstants.OK_ID) {
			MyInputDialogData data = dialog.getDatos();
			String respuesta = data.getTextResponse();
			System.out.println("Respuesta: " + respuesta);
			targetEditor.setTxtEstado("Cancelada");
			targetEditor.addDirtyFlag();
		} else {
			MessageDialog.openInformation(targetEditor.getSite().getShell(), "Información", "La acción ha sido cancelada.");
			return;
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
