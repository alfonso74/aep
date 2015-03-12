package rcp.manticora.acciones;

import rcp.manticora.editors.CommonEditorInput;

//import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;

public class ImprimirProductosAction implements IViewActionDelegate {
	private Shell shell;
	private IViewPart view;

	public void init(IViewPart view) {
		shell = view.getSite().getShell();
		this.view = view;
	}

	public void run(IAction action) {
		//String reporte = "reporte01.rptdesign";
		//WebViewer.display(reporte, WebViewer.HTML, false);
		CommonEditorInput input = new CommonEditorInput();
		input.setName("Listado de Productos");
		input.setRutaReporte("reports/prodPorCodigo.rptdesign");
		try {
			view.getSite().getPage().openEditor(input, "manticora.editors.reporte");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
