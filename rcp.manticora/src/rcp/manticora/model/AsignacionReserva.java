package rcp.manticora.model;

import java.util.Date;

import org.hibernate.proxy.HibernateProxy;

public class AsignacionReserva {
	
	private Long idAsignacion = -1L;
	private LineaActividad actividad;
	private IReserva reserva;
	private String tipo;       // usado como discriminador para diferentes tipos de reserva
	private String usuario;
	private Date fechaCreacion;
	
// ************************************ métodos especiales **************************************
	
	@Override
	public String toString() {
		String act = getActividad() == null ? "null" : getActividad().getIdActividad().toString();
		String res = getReserva() == null ? "null" : getReserva().getIdReserva().toString();
		return "Asignación (id-act-res): " + getIdAsignacion() + "-" + act + "-" + res;
	}
	
	public void eliminar() {
		actividad.eliminarAsignacionReserva(this);
		reserva.eliminarAsignacion(this);
	}
	
// ************************************* getters y setters *************************************
	
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public LineaActividad getActividad() {
		return actividad;
	}
	
	public void setActividad(LineaActividad actividad) {
		this.actividad = actividad;
	}
	
	public IReserva getReserva() {
		if (reserva instanceof HibernateProxy) {
			reserva = (IReserva) ((HibernateProxy) reserva).getHibernateLazyInitializer().getImplementation();
		}
		return reserva;
	}
	
	public void setReserva(IReserva reserva) {
		this.reserva = reserva;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Long getIdAsignacion() {
		return idAsignacion;
	}

	public void setIdAsignacion(Long idAsignacion) {
		this.idAsignacion = idAsignacion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}

