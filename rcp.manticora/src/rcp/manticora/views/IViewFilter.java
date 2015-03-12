package rcp.manticora.views;

import org.eclipse.jface.viewers.StructuredViewer;

/**
 * Interfase utilizada para exponer el viewer de una vista y poder aplicar filtros
 * o refresh especiales.
 * @author Carlos Alfonso
 *
 */
public interface IViewFilter {
	
	public StructuredViewer getViewer();

}
