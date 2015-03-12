package rcp.manticora.filtros;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rcp.manticora.model.Producto;

public class ProductosFilter extends ViewerFilter {
	String tipo;

	public ProductosFilter() {
		super();
	}
	
	public ProductosFilter(String tipo) {
		super();
		this.tipo = tipo;
	}
	

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Producto p = (Producto) element;
		
		if (p.getDspTipo().equals(tipo)) {
			return true;
		} else {
			return false;
		}
		//return element instanceof Producto && ((Producto) element).getEstado().equals("A");
		
	}

}
