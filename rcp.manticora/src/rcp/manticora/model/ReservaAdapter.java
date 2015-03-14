package rcp.manticora.model;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Implementa los métodos comunes de la interfase IReserva.</p>
 * Incluye las propiedades:<br>
 * 		- Long idReserva<br>
 * 		- HojaServicio hoja<br>
 * 		- Set<AsignacionReserva> listaAsignaciones<br>
 */

public abstract class ReservaAdapter implements IReserva {
	private Long idReserva = -1L;
	private IHojaServicio hoja;
	private Set<AsignacionReserva> listaAsignaciones = new HashSet<AsignacionReserva>();
	
	
	public Long getIdReserva() {
		return idReserva;
	}
	
	public void setIdReserva(Long idReserva) {
		this.idReserva = idReserva;
	}
	
	public IHojaServicio getHoja() {
		return hoja;
	}
	
	public void setHoja(IHojaServicio hoja) {
		this.hoja = hoja;
	}
	
	public boolean hasActividades() {
		if (getListaAsignaciones() != null && getListaAsignaciones().size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Set<LineaActividad> getListaActividades() {
		Set<LineaActividad> listaActividades = new HashSet<LineaActividad>();
		for (AsignacionReserva asignacion : getListaAsignaciones()) {
			listaActividades.add(asignacion.getActividad());
		}
		return listaActividades;
	}

	public void agregarAsignacion(AsignacionReserva asignacion) {
		listaAsignaciones.add(asignacion);
	}
	
	public void eliminarAsignacion(AsignacionReserva asignacion) {
		listaAsignaciones.remove(asignacion);
	}

	public Set<AsignacionReserva> getListaAsignaciones() {
		return listaAsignaciones;
	}
	
	public void setListaAsignaciones(Set<AsignacionReserva> listaAsignaciones) {
		this.listaAsignaciones = listaAsignaciones;
	}

}

