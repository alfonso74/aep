package rcp.manticora.acciones;

import java.util.Date;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.controllers.CommonController;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.views.IRefreshView;

public class RegistrarCotizacionAction implements IViewActionDelegate {
	private IViewPart view;
	private Object seleccion;
	private CommonController daoController;

	public void init(IViewPart view) {
		this.view = view;
		daoController = new CommonController();
	}

	public void run(IAction action) {
//		Shell shell = view.getViewSite().getShell();
//		InputDialog d = new InputDialog(shell, "Asignación de factura a cotización", "Por favor, introduzca el número de la factura", "", null);
//		int respuesta = d.open();
//		if (respuesta == InputDialog.OK) {
//			Cotizacion c = (Cotizacion) seleccion;
//			System.out.println("Fecha de registro: " + new Date());
//			c.setFechaContabilidad(new Date());
//			String noFactura = d.getValue();
//			c.setNoFactura(noFactura);
//			daoController.doSave(c);
//			actualizarVistas();
//		}
		
		Shell shell = view.getViewSite().getShell();
		boolean respuesta = MessageDialog.openConfirm(shell, "Confirmación", "Desea marcar la cotización como registrada en contabilidad?");
		if (respuesta) {
			Cotizacion c = (Cotizacion) seleccion;
			System.out.println("Fecha de registro: " + new Date());
			c.setFechaContabilidad(new Date());
			daoController.doSave(c);
			actualizarVistas();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		seleccion = ((StructuredSelection) selection).getFirstElement();
	}
	
	protected void actualizarVistas() {
		IViewReference[] viewRef = view.getViewSite().getPage().getViewReferences();
		for (int n = 0; n < viewRef.length; n++) {
			ViewPart vista = (ViewPart) viewRef[n].getView(true);
			if (vista instanceof IRefreshView) {
				((IRefreshView) vista).refrescar();
			}
		}
	}

}
