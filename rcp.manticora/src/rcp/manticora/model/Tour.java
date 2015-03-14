package rcp.manticora.model;

import java.util.HashSet;
import java.util.Set;

import rcp.manticora.IEditableDocument;


public class Tour implements IEditableDocument {
	private int idTour = -1;
	private Long idProducto;
	private String nombre;
	private Integer capacidad;
	private String estado;
	private String comentario;
	private String dspTipoProducto;
	private String dspProducto;
	private String dspEstado;
	
	private Set<LineaTour> listaActividades;
	private Set<DisponibilidadTour> listaDisponibilidad;


	public Tour() {
		listaActividades = new HashSet<LineaTour>();
		listaDisponibilidad = new HashSet<DisponibilidadTour>();
	}
	
//	************** métodos adicionales para manejar líneas de actividad ***********
	
	public void agregarActividad(LineaTour linea) {
		listaActividades.add(linea);
	}
	
	public void eliminarActividad(LineaTour linea) {
		listaActividades.remove(linea);
	}

	public void agregarDisponibilidad(DisponibilidadTour linea) {
		listaDisponibilidad.add(linea);
	}
	
	public void eliminarDisponibilidad(DisponibilidadTour linea) {
		//TODO: eliminar objetos asociados y mandar mensaje en algún lado (controller??)
		if (linea.getListaReservas() != null) {
			// eliminar las reservas asociadas a la disponibilidad
			// y cada reserva debe borrar su asociación a linea y hoja
		}
		listaDisponibilidad.remove(linea);
	}
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (nombre == null) {
			tituloDocumento = "Nuevo tour";
		} else {
			tituloDocumento = "Tour: " + nombre;
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Tour (id-nombre): " + getIdTour() + "-" + getNombre();
	}
	
	
// ******************* fin de métodos adicionales ************************

	public Integer getCapacidad() {
		return capacidad;
	}

	public String getDspEstado() {
		String resultado = dspEstado == null ? "No encontrado" : dspEstado;
		return resultado;
	}

	public String getDspProducto() {
		String resultado = dspProducto == null ? "No encontrado" : dspProducto;
		return resultado;
	}

	public String getEstado() {
		return estado;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public int getIdTour() {
		return idTour;
	}

	public String getNombre() {
		return nombre;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public void setDspEstado(String dspEstado) {
		this.dspEstado = dspEstado;
	}

	public void setDspProducto(String dspProducto) {
		this.dspProducto = dspProducto;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public void setIdTour(int idReserva) {
		this.idTour = idReserva;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Set<LineaTour> getListaActividades() {
		return listaActividades;
	}

	public void setListaActividades(Set<LineaTour> listaActividades) {
		this.listaActividades = listaActividades;
	}

	public String getDspTipoProducto() {
		String resultado = dspTipoProducto == null ? "No encontrado" : dspTipoProducto;
		return resultado;
	}

	public void setDspTipoProducto(String dspTipoProducto) {
		this.dspTipoProducto = dspTipoProducto;
	}

	public Set<DisponibilidadTour> getListaDisponibilidad() {
		return listaDisponibilidad;
	}

	public void setListaDisponibilidad(Set<DisponibilidadTour> listaDisponibilidad) {
		this.listaDisponibilidad = listaDisponibilidad;
	}
}
