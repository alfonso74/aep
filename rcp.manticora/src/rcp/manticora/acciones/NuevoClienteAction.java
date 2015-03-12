package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import rcp.manticora.editors.ClientesEditor;
import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.model.Cliente;
import rcp.manticora.model.ICliente;

public class NuevoClienteAction implements IViewActionDelegate {
	private IWorkbenchPage wbp;
	private String nombreCliente;

	public void init(IViewPart view) {
		System.out.println("Acción - init: " + view.toString());
		//view.getViewSite().getWorkbenchWindow().getActivePage().openEditor()
		System.out.println("Acción - Active page: " + view.getViewSite().getWorkbenchWindow().getActivePage());
		wbp = view.getViewSite().getWorkbenchWindow().getActivePage();
	}

	
	public void run(IAction action) {
		System.out.println("Acción - run: " + action.toString());
		//MessageDialog.openInformation(view.getSite().getShell(),
		//		MessageUtil.getString("Readme_Editor"),  
		//		MessageUtil.getString("View_Action_executed"));
		try {
			//wbp.showView(ProductosEditorView.ID, null, IWorkbenchPage.VIEW_ACTIVATE);
			
			CommonEditorInput input = new CommonEditorInput();
			wbp.openEditor(input, ClientesEditor.ID);
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	
	public void selectionChanged(IAction action, ISelection selection) {
		System.out.println("Acción nuevo cliente - selectionChanged: " + selection.toString());
		Object seleccion = ((StructuredSelection) selection).getFirstElement();
		if (seleccion instanceof Cliente) {
			nombreCliente = ((ICliente) seleccion).getNombreCliente();
			System.out.println("Objeto cliente: " + nombreCliente);
		} else {
			System.out.println("Otra cosa");
			nombreCliente = "";
		}
	}
}
