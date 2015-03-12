package rcp.manticora.model;

import java.util.Set;

public class HojaServicioTour extends HojaServicio {
	
	private String numero;
	private Integer capacidad;
	private String tipo;
	private DisponibilidadTour disponibilidad;
	private Set<ReservaGuia> listaResGuias;
	
	
	public HojaServicioTour() {
		super();
		//setClase("T");
	}
	

// ***************************** métodos especiales *************************
	
	@Override
	public String toString() {
		return "HojaServicioOps (id-nombre): " + getIdHoja() + "-" + getNombre();
	}
	
	public void agregarReservaGuia(ReservaGuia reserva) {
		listaResGuias.add(reserva);
		reserva.setHoja(this);
	}
	
	public void eliminarReservaGuia(ReservaGuia reserva) {
		listaResGuias.remove(reserva);
	}
	
	
// ***************************** getters y setters **************************
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Integer getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public DisponibilidadTour getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(DisponibilidadTour disponibilidad) {
		this.disponibilidad = disponibilidad;
	}


	public Set<ReservaGuia> getListaResGuias() {
		return listaResGuias;
	}
	public void setListaResGuias(Set<ReservaGuia> listaResGuias) {
		this.listaResGuias = listaResGuias;
	}

}
