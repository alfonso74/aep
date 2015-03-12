package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public interface IReservaX extends IEditableDocument {
	
	public abstract Long getIdReserva();
	
	public abstract void setIdReserva(Long idReserva);
	
	public abstract IHojaServicio getHoja();
	
	public abstract void setHoja(IHojaServicio hoja);
	
	public abstract LineaActividad getActividad();
	
	public abstract void setActividad(LineaActividad actividad);
	
//	public abstract LineaActividad getActividades();
	
//	public abstract void agregarActividad(LineaActividad actividad);
	
	public abstract String getComentario();
	
	public abstract void setComentario(String comentario);
	
	public abstract String getClase();
	
}
