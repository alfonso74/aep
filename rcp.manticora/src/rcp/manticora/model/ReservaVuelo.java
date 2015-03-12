package rcp.manticora.model;

import java.util.Date;

import rcp.manticora.services.FechaUtil;


public class ReservaVuelo extends ReservaAdapter {
	
	private String localizador;
	private String aerolinea;
	private String vuelo;
	private Date fecha;
	private String comentario;
	
	private String tipoReserva = "Vuelo";

	
// *************************** Métodos especiales *************************
	
	public String getTituloDocumento() {
		return "Reserva " + getIdReserva();
	}
	
	@Override
	public String toString() {
		return "ResVuelo (id-hoja): " + getIdReserva() + "-" + getHoja().getIdHoja();
	}
	
	public String getFechaDspReserva() {
		String fechaResTxt = FechaUtil.toString(getFecha(), FechaUtil.formatoFechaHora);
		return fechaResTxt;
	}
	
	
// *************************** Getters y setters **************************
	

	public String getTipoReserva() {
		return tipoReserva;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}


	public String getAerolinea() {
		return aerolinea;
	}
	public void setAerolinea(String aerolinea) {
		this.aerolinea = aerolinea;
	}


	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getLocalizador() {
		return localizador;
	}
	public void setLocalizador(String localizador) {
		this.localizador = localizador;
	}


	public String getVuelo() {
		return vuelo;
	}
	public void setVuelo(String vuelo) {
		this.vuelo = vuelo;
	}

}

