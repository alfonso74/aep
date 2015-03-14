package rcp.manticora.model;

import java.util.Date;
import java.util.Set;

public interface ILineaHospedaje {
	
	// métodos especiales
	
	public void asignarListaPaxs(Set<Pax> paxs);
	
	public int getNoPaxAsignados();
	
	public String getNombresPaxAsignados();
	
	// getters y setters
	// en las interfases, los métodos son implícitamente abstract
	
	public abstract Long getIdLinea();
	
	public abstract void setIdLinea(Long idLinea);
	
	public abstract ReservaHospedaje getReserva();
	
	public abstract void setReserva(ReservaHospedaje reserva);
	
	public abstract Date getFechaDesde();
	
	public abstract void setFechaDesde(Date fechaDesde);
	
	public abstract Date getFechaHasta();
	
	public abstract void setFechaHasta(Date fechaHasta);
	
	public abstract String getComentario();
	
	public abstract void setComentario(String comentario);
	
	public abstract Set<Pax> getListaPaxs();
	
	public abstract void setListaPaxs(Set<Pax> listaPaxs);

}
