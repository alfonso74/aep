package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import rcp.manticora.filtros.ProductosFilter;
import rcp.manticora.model.Producto;
import rcp.manticora.views.ProductosView;

public class FiltrarProductos implements IViewActionDelegate {
	Producto seleccion;
	IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		TableViewer viewer = ((ProductosView) view).getViewer();
		if (action.isChecked()) {
			if (seleccion == null) {
				action.setChecked(false);
				// no hacemos nada 
			} else {
				viewer.addFilter(new ProductosFilter(seleccion.getDspTipo()));
			}
		} else {
			viewer.resetFilters();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		seleccion = (Producto) ((StructuredSelection) selection).getFirstElement();
		if (seleccion != null) {
			action.setText("Ver tipo: " + seleccion.getDspTipo());
		}
	}
}
