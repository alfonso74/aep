package rcp.manticora.model;

import java.util.Date;

import rcp.manticora.services.FechaUtil;


public class ReservaTour extends ReservaAdapter {
//	private Long idReserva = -1L;       // implementado en ReservaAdapter
	private Integer idHoja;
	private Integer idDisponibilidad;
	private Integer idCapitan;
	private Integer cantidad;
	private String estado;
	private String dspEstado;
	private String comentario;
	
	private String tipoReserva = "Tour";
//	private HojaServicio hoja;                 // implementado en ReservaAdapter
//	private LineaActividad actividad;            // ya no aplica
	private DisponibilidadTour disponibilidad;

	public ReservaTour() {
	}
	

// **************************** métodos especiales ***************************

	public String getTituloDocumento() {
		return "Reserva " + getIdReserva();
	}
	
	@Override
	public String toString() {
		String texto = "ReservaTour (id-Hoja): " + getIdReserva() + "-" + getHoja().getIdHoja();
		return texto;
	}
	
	/**
	 * Permite obtener la fecha para la que se está reservando el tour
	 * @return Fecha de reserva (tipo Date)
	 */
	public Date getFecha() {
		return disponibilidad.getFecha();
	}
	
	public String getFechaDspReserva() {
		String fechaResTxt = FechaUtil.toString(getFecha(), FechaUtil.formatoFecha);
		return fechaResTxt;
	}
	
	
// ***************************** getters y setters ***************************

	public String getDspEstado() {
		return dspEstado;
	}

	public String getEstado() {
		return estado;
	}

	public void setDspEstado(String dspEstado) {
		this.dspEstado = dspEstado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/*  TODO: borrar con nueva versión de reservas
	public LineaActividad getActividad() {
		return actividad;
	}

	public void setActividad(LineaActividad actividad) {
		this.actividad = actividad;
	}
	*/

	public DisponibilidadTour getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidadX(DisponibilidadTour disponibilidad) {
		// si la reserva está asociada a una disponibilidad entonces
		// eliminamos la asociación y LUEGO asignamos la nueva disponibilidad
		if (getDisponibilidad() != null) {
			getDisponibilidad().eliminarReserva(this);
		}
		// ...asociamos la nueva disponibilidad
		this.disponibilidad = disponibilidad;
		// y asociamos el otro extremo (agregamos esta reserva a la nueva disp)
		getDisponibilidad().agregarReserva(this);
	}
	
	public void setDisponibilidad(DisponibilidadTour disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getIdDisponibilidad() {
		return idDisponibilidad;
	}

	public void setIdDisponibilidad(Integer idDisponibilidad) {
		this.idDisponibilidad = idDisponibilidad;
	}

	public Integer getIdHoja() {
		return idHoja;
	}

	public void setIdHoja(Integer idHoja) {
		this.idHoja = idHoja;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getTipoReserva() {
		return tipoReserva;
	}

	public Integer getIdCapitan() {
		return idCapitan;
	}

	public void setIdCapitan(Integer idCapitan) {
		this.idCapitan = idCapitan;
	}

}
