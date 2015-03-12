package rcp.manticora.services;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rcp.manticora.model.Cotizacion;

public class CotizacionesPagoParcial extends ViewerFilter {

	public CotizacionesPagoParcial() {
	}

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Cotizacion c = (Cotizacion) element;
		/*
		if (c.getEstado().equals("P") && c.getPorcPago() != 100) {
			return true;
		} else {
			return false;
		}
		*/
		return c.hasPagoParcial();
	}

}
