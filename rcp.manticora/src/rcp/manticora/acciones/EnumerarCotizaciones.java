package rcp.manticora.acciones;

import java.util.Set;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import rcp.manticora.controllers.CotizacionesController;
import rcp.manticora.controllers.ViewController;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.model.LineaCotizacion;
import rcp.manticora.views.IViewFilter;

public class EnumerarCotizaciones implements IViewActionDelegate {
	private IViewFilter view;
	private ViewController vc;
	private CotizacionesController controller;
	private Object seleccion;
	
	public void init(IViewPart view) {
		this.view = (IViewFilter) view;
		vc = new ViewController();
	}

	public void run(IAction action) {
		controller = new CotizacionesController("ENUMERAR");
		/*
		Cotizacion c = (Cotizacion) seleccion;
		controller.getSession().refresh(c);
		Set<LineaCotizacion> lineas = c.getListaActividades();
		controller.reenumerarLineas(lineas);
		controller.doSave(c);
		*/
		
		TableViewer viewer = (TableViewer) view.getViewer();
		int elementos = viewer.getTable().getItemCount();
		for (int n = 0; n < elementos; n++) {
			Cotizacion c = (Cotizacion) viewer.getElementAt(n);
			controller.getSession().refresh(c);
			System.out.println("Procesando: " + c.getNombre());
			Set<LineaCotizacion> lineas = c.getListaActividades();
			controller.reenumerarLineas(lineas);
			controller.doSave(c);
		}
		
		controller.finalizar("ENUMERAR");
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		seleccion = ((StructuredSelection) selection).getFirstElement();
	}

}
