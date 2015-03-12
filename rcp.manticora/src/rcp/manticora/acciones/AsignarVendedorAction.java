package rcp.manticora.acciones;

import java.util.Date;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import rcp.manticora.dialogs.AsignarVendedorDialog;
import rcp.manticora.dialogs.MyInputDialogData;
import rcp.manticora.editors.SolicitudesEditor;

public class AsignarVendedorAction implements IEditorActionDelegate {
	private IEditorPart targetEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}

	public void run(IAction action) {
		AsignarVendedorDialog midt = new AsignarVendedorDialog(targetEditor.getSite().getShell());
		midt.setText("Asignación de vendedor");
		MyInputDialogData myData = midt.open();
		System.out.println("Data: " + myData.getTextResponse());
		String respuesta;
		if (myData.isButtonResponse()){
			respuesta = myData.getTextResponse();
			System.out.println("Title: " + targetEditor.getTitle());
			((SolicitudesEditor) targetEditor).setTxtVendedor(respuesta);
			((SolicitudesEditor) targetEditor).setTxtEstado("Asignada");
			((SolicitudesEditor) targetEditor).setFechaAsignacion(new Date());
		} else {
			MessageDialog.openInformation(targetEditor.getSite().getShell(), "Información", "La asignación del vendedor ha sido cancelada.");
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
