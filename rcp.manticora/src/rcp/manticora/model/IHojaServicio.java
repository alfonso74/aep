package rcp.manticora.model;

import java.util.Date;
import java.util.Set;

public interface IHojaServicio {

	public abstract void resetListaActividades();

	public abstract void agregarActividad(LineaActividad linea);

	public abstract void eliminarActividad(LineaActividad linea);

	public abstract void resetListaPaxs();

	public abstract void agregarPax(Pax pax);
	
	public abstract void excluirPax(Pax pax);

	public abstract void agregarReserva(IReserva reserva);

	public abstract void eliminarReserva(IReserva reserva);

	public abstract void importarPAXsCotizacion(Set<Pax> paxs);

	public abstract String getTituloDocumento();

	public abstract String toString();

	public abstract String getEstado();
	
	public abstract String getDspEstado();

	public abstract Date getFechaCreacion();

	public abstract Date getFechaFin();

	public abstract Date getFechaInicio();

	public abstract Date getFechaOperaciones();

	public abstract Long getIdHoja();

	public abstract Set<LineaActividad> getListaActividades();

	public abstract String getNombre();

	public abstract void setEstado(String estado);
	
	public abstract void setDspEstado(String dspEstado);

	public abstract void setFechaCreacion(Date fechaCreacion);

	public abstract void setFechaFin(Date fechaFin);

	public abstract void setFechaInicio(Date fechaInicio);

	public abstract void setFechaOperaciones(Date fechaOperaciones);

	public abstract void setIdHoja(Long idHoja);

	public abstract void setListaActividades(
			Set<LineaActividad> listaActividades);

	public abstract void setNombre(String nombre);

	public abstract String getClase();

	public abstract void setClase(String clase);

	public abstract Long getIdProducto();

	public abstract void setIdProducto(Long idProducto);

	public abstract String getComentario();

	public abstract void setComentario(String comentario);

	public abstract Set<Pax> getListaPaxs();

	public abstract void setListaPaxs(Set<Pax> listaPaxs);

	/** 
	 * Si pongo ICliente entonces genera error
	 * @return
	 */
	// Si pongo ICliente se genera error porque no está mapeada la interface
	public abstract Cliente getCliente();

	public abstract void setCliente(ICliente cliente);

	public abstract Set<IReserva> getListaReservas();

	public abstract void setListaReservas(Set<IReserva> listaReservas);

	public abstract int getVersion();

	public abstract void setVersion(int version);

}