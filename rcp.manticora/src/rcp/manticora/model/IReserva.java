package rcp.manticora.model;

import java.util.Set;

import rcp.manticora.IEditableDocument;


public interface IReserva extends IEditableDocument {
	
//	 en las interfases, los métodos son implícitamente abstract
	
	public abstract Long getIdReserva();
	
	public abstract void setIdReserva(Long idReserva);
	
	public abstract IHojaServicio getHoja();
	
	public abstract void setHoja(IHojaServicio hoja);
	
	//public abstract LineaActividad getActividad();
	
	//public abstract void setActividad(LineaActividad actividad);
	
	
	public abstract Set<AsignacionReserva> getListaAsignaciones();
	
	public abstract void setListaAsignaciones(Set<AsignacionReserva> listaAsignaciones);
	
	public abstract void agregarAsignacion(AsignacionReserva asignacion);
	
	public abstract void eliminarAsignacion(AsignacionReserva asignacion);
	
	//public abstract void setListaAsignaciones(Set<AsignacionReserva> asignaciones);
	
	public abstract boolean hasActividades();
	
	public abstract Set<LineaActividad> getListaActividades();
	
	
	
	public abstract String getComentario();
	
	public abstract void setComentario(String comentario);
	
	public abstract String getTipoReserva();
	
	/**
	 * Prepara la fecha de la reserva para presentarla en un viewer o impresión.
	 * Cada tipo de reserva tiene su propio formato (algunas incluyen hasta horas),
	 * y es requerido que todas implementen este método.
	 * @return Fecha de la reserva con su formato particular.
	 */
	public String getFechaDspReserva();
	
}
