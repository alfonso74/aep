package rcp.manticora.model;

import java.util.Date;

import rcp.manticora.IEditableDocument;
import rcp.manticora.services.FechaUtil;

public class ReservaGuia implements IEditableDocument {
	
	private Long idReserva = -1L;
	private IHojaServicio hoja;
	private Guia guia;
	private Date fechaDesde;
	private Date fechaHasta;
	
	
// *************************** Métodos especiales *************************
	
	public String getTituloDocumento() {
		return "Reserva " + getIdReserva();
	}
	
	@Override
	public String toString() {
		return "Res Guia (id-Nombre): " + getIdReserva() + "-" + getGuia().getNombreCompleto();
	}
	
	
	public String getFechaDspReserva() {
		String fecha1 = FechaUtil.toString(getFechaDesde(), FechaUtil.formatoFecha);
		String fecha2 = FechaUtil.toString(getFechaHasta(), FechaUtil.formatoFecha);
		String fechaResTxt = fecha1 + " a " + fecha2; 
		return fechaResTxt;
	}
	
	
// ***************************** getters y setters *************************

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
	public Guia getGuia() {
		return guia;
	}
	public void setGuia(Guia guia) {
		this.guia = guia;
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

}
