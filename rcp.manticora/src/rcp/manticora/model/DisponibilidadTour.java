package rcp.manticora.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DisponibilidadTour {
	private Long idDisponibilidad = -1L;
	private Long idTour;
	private Date fecha;
	private Integer capacidad;
	private Integer ocupacion;
	private String tipo;
	private String comentario;
	
	//private String dspProducto;      // usado en AgregarTour2Hoja, si lo borro también quitar del mapeo
	private Tour tour;
	private HojaServicioTour hoja;
	private Set<ReservaTour> listaReservas;

	public DisponibilidadTour() {
		listaReservas = new HashSet<ReservaTour>();
	}
	
// ************************* métodos especiales **************************
	
	@Override
	public String toString() {
		//return "Disponibilidad (" + idDisponibilidad + "), tour (" + getTour().getIdTour() + "): al " + fecha.toString();
		return "Disponibilidad (id, idTour, fecha): " + idDisponibilidad + ", " + getIdTour() + ", " + fecha.toString();
	}
	
	
	public boolean equals(Object obj) {
		if (!(obj instanceof DisponibilidadTour)) {
			return false;
		};
		return true;
	}

	public int hashCode() {
		System.out.println("Hash: " + super.hashCode());
		//return (this.idTour.intValue() + fecha.hashCode());
		return (this.idDisponibilidad.intValue() + fecha.hashCode());
	}
	
	public void eliminarReserva(IReserva reserva) {
		if (this.listaReservas.remove(reserva)) {
			System.out.println("Reserva eliminada de disponibilidad: " + this.getTour().getNombre());
		}
	}
	
	public void agregarReserva(IReserva reserva) {
		this.listaReservas.add((ReservaTour) reserva);
	}
	
	// ocupación es un campo de lectura proveniente de una fórmula
	public Integer getOcupacion() {
		if (ocupacion == null) {
			return 0;
		}
		return ocupacion;
	}

	// ocupación es un campo de lectura proveniente de una fórmula
	public void setOcupacion(Integer ocupacion) {
		this.ocupacion = ocupacion;
	}
	
	public Integer getDisponibles() {
		return new Integer(getCapacidad().intValue() - getOcupacion().intValue());
	}
	
	
	/**
	 * Retorna un string que representa la disponibilidad actual del tour.
	 * @return String con ocupación y capacidad.
	 */
	public String getResumenDisp() {
		String cadena = "";
		//cadena = getOcupacion().toString() + "/" + getDisponibles().toString() + "/" + getCapacidad().toString();
		cadena = getOcupacion().toString() + " / " + getCapacidad().toString();
		return cadena;
	}
	
	public String getNumero() {
		String numeroTour = "No asignado";
		if (getHoja() != null) {
			if (getHoja().getNumero() != null) {
				numeroTour = getHoja().getNumero();
			}
		}
		return numeroTour;
	}
	

// **************************** getters y setters ***************************

	public Integer getCapacidad() {
		return capacidad;
	}

	public String getComentario() {
		return comentario;
	}

	public Date getFecha() {
		return fecha;
	}

	public Long getIdDisponibilidad() {
		return idDisponibilidad;
	}

	public Long getIdTour() {
		return idTour;
	}

	public String getTipo() {
		return tipo;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setIdDisponibilidad(Long idDisponibilidad) {
		this.idDisponibilidad = idDisponibilidad;
	}

	public void setIdTour(Long idTour) {
		this.idTour = idTour;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/*
	public String getDspProducto() {
		return dspProducto;
	}

	public void setDspProducto(String dspProducto) {
		this.dspProducto = dspProducto;
	}
	*/

	public Tour getTour() {
		return tour;
	}

	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public Set<ReservaTour> getListaReservas() {
		return listaReservas;
	}

	public void setListaReservas(Set<ReservaTour> listaReservas) {
		this.listaReservas = listaReservas;
	}

	public HojaServicioTour getHoja() {
		return hoja;
	}

	public void setHoja(HojaServicioTour hoja) {
		this.hoja = hoja;
	}

}
