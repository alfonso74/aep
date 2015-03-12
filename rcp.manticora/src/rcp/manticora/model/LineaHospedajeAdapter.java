package rcp.manticora.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class LineaHospedajeAdapter implements ILineaHospedaje {
	
	private Long idLinea = -1L;
	private ReservaHospedaje reserva;
	private Date fechaDesde;
	private Date fechaHasta;
	private String comentario;
	
	private Set<Pax> listaPaxs;
	
	public LineaHospedajeAdapter() {
		listaPaxs = new HashSet<Pax>();
	}
	
	
//	 *************************** métodos especiales ***************************
	
	@Override
	public String toString() {
		return "Hospedaje (id-idRes-Desde-Hasta): " + getIdLinea() + "-" + getReserva().getIdReserva() 
			+ "-" + getFechaDesde() + "-" + getFechaHasta();
	}
	
	/**
	 * Permite especificar los paxs que están asignados esta línea de hospedaje
	 * @param paxs Listado (Set) de paxs que duermen en la habitación
	 */
	public void asignarListaPaxs(Set<Pax> paxs) {
		listaPaxs.clear();
		listaPaxs.addAll(paxs);
	}
	
	public int getNoPaxAsignados() {
		return listaPaxs.isEmpty() ? 0 : listaPaxs.size();
	}
	
	public String getNombresPaxAsignados() {
		String nombres = "";
		for (Pax pax : listaPaxs) {
			if (nombres.equals("")) {
				nombres = pax.getNombreCompleto();
			} else {
				nombres += ", " + pax.getNombreCompleto();
			}
		}
		return nombres;
	}
	
	
//	 ***************************** getters y setters ***************************

	public Long getIdLinea() {
		return idLinea;
	}
	public void setIdLinea(Long idLinea) {
		this.idLinea = idLinea;
	}

	public ReservaHospedaje getReserva() {
		return reserva;
	}
	public void setReserva(ReservaHospedaje reserva) {
		this.reserva = reserva;
	}

	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Set<Pax> getListaPaxs() {
		return listaPaxs;
	}
	public void setListaPaxs(Set<Pax> listaPaxs) {
		this.listaPaxs = listaPaxs;
	}
}

