package rcp.manticora.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import rcp.manticora.IEditableDocument;

public abstract class HojaServicio implements IHojaServicio, IEditableDocument {
	private Long idHoja;
	private Long idProducto;
	private Date fechaInicio;
	private Date fechaFin;
	private String nombre;
	private String comentario;
	private Date fechaCreacion;
	private Date fechaOperaciones;
	private String estado;
	private String dspEstado;
	private String clase;
	
	private int version;
	
	private ICliente cliente;
	private Set<LineaActividad> listaActividades;
	private Set<Pax> listaPaxs;
	private Set<IReserva> listaReservas;

	public HojaServicio() {
		this.fechaCreacion = new Date();
		listaActividades = new HashSet<LineaActividad>();
		listaPaxs = new HashSet<Pax>();
		listaReservas = new HashSet<IReserva>();
	}
	
	
//	************** métodos adicionales para manejar líneas de actividad ***********
	
	public void resetListaActividades() {
		this.listaActividades.clear();
	}
	
	public void agregarActividad(LineaActividad linea) {
		listaActividades.add(linea);
	}
	
	public void eliminarActividad(LineaActividad linea) {
		listaActividades.remove(linea);
	}
	
	public void resetListaPaxs() {
		this.listaPaxs.clear();
	}
	
	public void agregarPax(Pax pax) {
		listaPaxs.add(pax);
	}
	
	public void excluirPax(Pax pax) {
		listaPaxs.remove(pax);
	}
	
	public void agregarReserva(IReserva reserva) {
		listaReservas.add(reserva);
		reserva.setHoja(this);
	}
	
	public void eliminarReserva(IReserva reserva) {
		listaReservas.remove(reserva);
	}
	
	public void importarPAXsCotizacion(Set<Pax> paxs) {
		for (Pax pax : paxs) {
			agregarPax(pax);
		}
	}
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (getNombre() == null) {
			tituloDocumento = "Nueva hoja";
		} else {
			tituloDocumento = "HS: " + getNombre();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "HojaServicio (id-nombre): " + getIdHoja() + "-" + getNombre();
	}
	
	
// ******************* fin de métodos adicionales ************************

	public String getEstado() {
		return estado;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public Date getFechaOperaciones() {
		return fechaOperaciones;
	}

	public Long getIdHoja() {
		return idHoja;
	}

	public Set<LineaActividad> getListaActividades() {
		return listaActividades;
	}

	public String getNombre() {
		return nombre;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFechaOperaciones(Date fechaOperaciones) {
		this.fechaOperaciones = fechaOperaciones;
	}

	public void setIdHoja(Long idHoja) {
		this.idHoja = idHoja;
	}

	public void setListaActividades(Set<LineaActividad> listaActividades) {
		this.listaActividades = listaActividades;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Set<Pax> getListaPaxs() {
		return listaPaxs;
	}

	public void setListaPaxs(Set<Pax> listaPaxs) {
		this.listaPaxs = listaPaxs;
	}
	
	/** 
	 * Si pongo ICliente entonces genera error
	 * @return
	 */
	// Si pongo ICliente se genera error porque no está mapeada la interface
	public Cliente getCliente() {
		return (Cliente) cliente;
	}

	public void setCliente(ICliente cliente) {
		this.cliente = cliente;
	}

	public Set<IReserva> getListaReservas() {
		return listaReservas;
	}

	public void setListaReservas(Set<IReserva> listaReservas) {
		this.listaReservas = listaReservas;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getDspEstado() {
		return dspEstado;
	}

	public void setDspEstado(String dspEstado) {
		this.dspEstado = dspEstado;
	}
}
