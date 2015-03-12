package rcp.manticora.model;

import java.util.Date;

import rcp.manticora.services.FechaUtil;


public class ReservaAlimento extends ReservaAdapter {
	
	private String ubicacion;
	private Date fecha;
	private String comentario;
	
	private String tipoReserva = "Alimentaci�n";
	
	
// *************************** M�todos especiales *************************

	public String getTituloDocumento() {
		return "Reserva " + getIdReserva();
	}
	
	@Override
	public String toString() {
		return "ResAlimentacion (id-hoja)" + getIdReserva() + "-" + getHoja().getIdHoja();
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

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

}

