package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.editors.CotizacionesEditor;
import rcp.manticora.editors.GuiasEditor;
import rcp.manticora.editors.HabitacionesEditor;
import rcp.manticora.editors.HojaOpsEditor;
import rcp.manticora.editors.HojaVentasEditor;
import rcp.manticora.editors.KeywordsEditor;
import rcp.manticora.editors.PaisesEditor;
import rcp.manticora.editors.ProductosEditor;
import rcp.manticora.editors.RedesEditor;
import rcp.manticora.editors.RolesEditor;
import rcp.manticora.editors.SolicitudesEditor;
import rcp.manticora.editors.TemplatesEditor;
import rcp.manticora.editors.TipoClientesEditor;
import rcp.manticora.editors.TipoHabitacionesEditor;
import rcp.manticora.editors.TipoProductosEditor;
import rcp.manticora.editors.ToursEditor;
import rcp.manticora.editors.TransportesEditor;
import rcp.manticora.editors.UsuariosEditor;
import rcp.manticora.editors.VendedoresEditor;
import rcp.manticora.model.Usuario;
import rcp.manticora.services.AutenticacionUtil;
import rcp.manticora.views.CotizacionesActivasView;
import rcp.manticora.views.CotizacionesView;
import rcp.manticora.views.GuiasView;
import rcp.manticora.views.HabitacionesView;
import rcp.manticora.views.HojasOpsNuevasView;
import rcp.manticora.views.HojasView;
import rcp.manticora.views.KeywordsView;
import rcp.manticora.views.PaisesView;
import rcp.manticora.views.ProductosView;
import rcp.manticora.views.RedesView;
import rcp.manticora.views.RolesView;
import rcp.manticora.views.SolicitudesView;
import rcp.manticora.views.TemplatesView;
import rcp.manticora.views.TipoClientesView;
import rcp.manticora.views.TipoHabitacionesView;
import rcp.manticora.views.TipoProductosView;
import rcp.manticora.views.ToursView;
import rcp.manticora.views.TransportesView;
import rcp.manticora.views.UsuariosView;
import rcp.manticora.views.VendedoresView;

public class NuevoElementoAction implements IViewActionDelegate {
	private IWorkbenchPage wbp;
	private IViewPart view;
	private Shell shell;

	public void init(IViewPart view) {
		wbp = view.getViewSite().getWorkbenchWindow().getActivePage();
		this.view = view;
		shell = view.getViewSite().getShell();
	}

	public void run(IAction action) {
		String editorID = "";
		try {
			CommonEditorInput input = new CommonEditorInput();
			editorID = getEditorID(view);
			wbp.openEditor(input, editorID);
		} catch (PartInitException e) {
			MessageDialog.openError(shell, "Error al inicializar editor " + this, "No se pudo abrir el editor: " + editorID);
			mensajeError(shell, e);
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	public String getEditorID(IViewPart view) {
		String editorID = null;
		if (view instanceof VendedoresView) {
			editorID = VendedoresEditor.ID;
		} else if (view instanceof CotizacionesView || view instanceof CotizacionesActivasView) {
			editorID = CotizacionesEditor.ID;
		} else if (view instanceof GuiasView) {
			editorID = GuiasEditor.ID;
		} else if (view instanceof HojasView) {
			Usuario usuario = AutenticacionUtil.getUsuario();
			if (usuario.hasRol("Vendedor")) {
				editorID = HojaVentasEditor.ID;
			} else {
				editorID = HojaOpsEditor.ID;
			}
		} else if (view instanceof HojasOpsNuevasView) {
			editorID = HojaOpsEditor.ID;
		} else if (view instanceof KeywordsView) {
			editorID = KeywordsEditor.ID;
		} else if (view instanceof PaisesView) {
			editorID = PaisesEditor.ID;
		} else if (view instanceof ProductosView) {
			editorID = ProductosEditor.ID;
		} else if (view instanceof SolicitudesView) {
			editorID = SolicitudesEditor.ID;
		} else if (view instanceof TemplatesView) {
			editorID = TemplatesEditor.ID;
		} else if (view instanceof TipoClientesView) {
			editorID = TipoClientesEditor.ID;
		} else if (view instanceof TipoHabitacionesView) {
			editorID = TipoHabitacionesEditor.ID;
		} else if (view instanceof TipoProductosView) {
			editorID = TipoProductosEditor.ID;
		} else if (view instanceof ToursView) {
			editorID = ToursEditor.ID;
		} else if (view instanceof TransportesView) {
			editorID = TransportesEditor.ID;
		} else if (view instanceof HabitacionesView) {
			editorID = HabitacionesEditor.ID;
		} else if (view instanceof RolesView) {
			editorID = RolesEditor.ID;
		} else if (view instanceof UsuariosView) {
			editorID = UsuariosEditor.ID;
		} else if (view instanceof RedesView) {
			editorID = RedesEditor.ID;
		} else {
			//MessageDialog.openError(shell, "Aviso en " + this, "No se ha definido el editor de esta vista: " + view.toString());
			mensajeError(shell, "No se ha definido el editor de esta vista: " + view.toString());
		}
		System.out.println("Usando editorID: " + editorID);
		return editorID;
	}
	
	private void mensajeError(Shell shell, Exception e) {
		String nombre = this.toString();
		int puntoFinal = nombre.indexOf(".") + 1;
		int arroba = nombre.indexOf("@");
		MessageDialog.openError(shell, "Error en " + nombre.substring(puntoFinal, arroba), "Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0] + "\n" + e.getStackTrace()[1]);
	}
	
	private void mensajeError(Shell shell, String mensaje) {
		String nombre = this.toString();
		int puntoFinal = nombre.indexOf(".") + 1;
		int arroba = nombre.indexOf("@");
		MessageDialog.openError(shell, "Error en " + nombre.substring(puntoFinal, arroba), mensaje);
	}
	

}
