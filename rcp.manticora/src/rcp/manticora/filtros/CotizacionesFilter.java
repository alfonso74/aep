package rcp.manticora.filtros;

import java.util.Date;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rcp.manticora.model.Cliente;
import rcp.manticora.model.Cotizacion;

public class CotizacionesFilter extends ViewerFilter {
	private Long noCotizacion;
	private Long noCliente;
	private String nombre;
	private String vendedor;
	private Date fechaInicio1;
	private Date fechaInicio2;
	private Date fechaFin1;
	private Date fechaFin2;

	public CotizacionesFilter() {
	}
	
	public CotizacionesFilter(Long noCotizacion, Long noCliente, String nombre, String vendedor,
			Date fechaInicio1, Date fechaInicio2, Date fechaFin1, Date fechaFin2) {
		this.noCotizacion = noCotizacion;
		this.noCliente = noCliente;
		this.nombre = nombre.toLowerCase();
		this.vendedor = vendedor;
		this.fechaInicio1 = fechaInicio1;
		this.fechaInicio2 = fechaInicio2;
		this.fechaFin1 = fechaFin1;
		this.fechaFin2 = fechaFin2;
	}
	
	

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Cotizacion c = (Cotizacion) element;

		if (noCotizacion != null && noCotizacion.longValue() != 0) {
			if (c.getIdCotizacion().longValue() != noCotizacion) return false;
		}
		if (noCliente != null && noCliente.longValue() != 0) {
			Cliente cliente = c.getCliente();
			// si la cotización tiene cliente verificamos el número de cliente
			if (cliente != null) {
				if (cliente.getIdCliente().longValue() != noCliente) return false;
			} else {  // y si no tiene, no califica
				return false;
			}
		}
		if (nombre != null && !nombre.equals("")) {
			if (!c.getNombre().toLowerCase().contains(nombre)) return false;
		}
		if (vendedor != null && !vendedor.equals("")) {
			// si alguna cotización no tiene vendedor entonces será retornada
			// (si cumple con los otros criterios)
			if (c.getDspVendedor() != null && !c.getDspVendedor().equals(vendedor)) return false;
		}
		if (fechaInicio1 != null && !fechaInicio1.equals("")) {
			if (fechaInicio2 != null && !fechaInicio2.equals("")) {
				if (c.getFechaInicio().compareTo(fechaInicio1) < 0) return false;
				if (c.getFechaInicio().compareTo(fechaInicio2) > 0) return false;
			} else {
				if (c.getFechaInicio().compareTo(fechaInicio1) != 0) return false;
			}
		}
		if (fechaFin1 != null && !fechaFin1.equals("")) {
			if (fechaFin2 != null && !fechaFin2.equals("")) {
				if (c.getFechaFin().compareTo(fechaFin1) < 0) return false;
				if (c.getFechaFin().compareTo(fechaFin2) > 0) return false;
			} else {
				if (c.getFechaFin().compareTo(fechaFin1) != 0) return false;
			}
		}
		return true;
	}

}
