package rcp.manticora.services;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rcp.manticora.model.Solicitud;

public class SolicitudesByStatus extends ViewerFilter {
	String estado;

	public SolicitudesByStatus() {
	}
	
	public SolicitudesByStatus(String estado) {
		this.estado = estado;
	}

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Solicitud s = (Solicitud) element;
		if (s.getEstado().equals(estado)) {
			return true;
		} else {
			return false;
		}
	}

}
