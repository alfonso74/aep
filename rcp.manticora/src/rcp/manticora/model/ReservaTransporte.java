package rcp.manticora.model;

import java.util.Date;

import rcp.manticora.services.FechaUtil;


public class ReservaTransporte extends ReservaAdapter {
	//private Long idReserva = -1L;       // implementado en ReservaAdapter
	//private HojaServicio hoja;                 // implementado en ReservaAdapter
	private String origen;
	private String destino;
	private Date fechaOrigen;
	private Date fechaDestino;
	private Long idTransporte;
	private String tipo;
	private String servicio;
	private String comentario;
	private String dspTransporte;
	
	private String tipoReserva = "Transporte";
	

	public ReservaTransporte() {
	}
	
	/*
	public ReservaTransporte(Long idReserva, String origen, String destino,
			Date fechaOrigen, Date fechaDestino, Long idTransporte,
			String tipo, String servicio, String comentario) {
		this.idReserva = idReserva;
		this.origen = origen;
		this.destino = destino;
		this.fechaOrigen = fechaOrigen;
		this.fechaDestino = fechaDestino;
		this.tipo = tipo;
		this.servicio = servicio;
		this.comentario = comentario;
	}
	*/
	
// *************************** métodos especiales ***********************

	public String getTituloDocumento() {
		return "Reserva " + getIdReserva();
	}

	@Override
	public String toString() {
		String texto = "ResTrans (id-Hoja): " + getIdReserva() + "-" + getHoja().getIdHoja();
		return texto;
	}
	
	public String getFechaDspReserva() {
		String fechaResTxt = FechaUtil.toString(getFechaOrigen(), FechaUtil.formatoFechaHora);
		return fechaResTxt;
	}
	
	
// *************************** getters / setters ************************

	public String getComentario() {
		return comentario;
	}

	public String getDestino() {
		return destino;
	}

	public Date getFechaDestino() {
		return fechaDestino;
	}

	public Date getFechaOrigen() {
		return fechaOrigen;
	}

	public String getOrigen() {
		return origen;
	}

	public String getServicio() {
		return servicio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public void setFechaDestino(Date fechaDestino) {
		this.fechaDestino = fechaDestino;
	}

	public void setFechaOrigen(Date fechaOrigen) {
		this.fechaOrigen = fechaOrigen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Long getIdTransporte() {
		return idTransporte;
	}

	public void setIdTransporte(Long idTransporte) {
		this.idTransporte = idTransporte;
	}

	public String getDspTransporte() {
		return dspTransporte;
	}

	public void setDspTransporte(String dspTransporte) {
		this.dspTransporte = dspTransporte;
	}

	/*
	public Long getIdHoja() {
		return idHoja;
	}

	public void setIdHoja(Long idHoja) {
		this.idHoja = idHoja;
	}
	*/

	/*  TODO: borrar estos métodos (nueva forma de reserva)
	public LineaActividad getActividad() {
		return actividad;
	}

	public void setActividad(LineaActividad actividad) {
		this.actividad = actividad;
	}
	*/

	public String getTipoReserva() {
		return tipoReserva;
	}

}
