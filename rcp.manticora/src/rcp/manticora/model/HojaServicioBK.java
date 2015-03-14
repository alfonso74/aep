package rcp.manticora.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import rcp.manticora.IEditableDocument;


public class HojaServicioBK implements IEditableDocument {
	private Long idHoja;
	private Long idCotizacion;
	private Long idProducto;
	private Date fechaInicio;
	private Date fechaFin;
	private String numero;
	private String nombre;
	private Integer paxs;
	private String comentario;
	private Date fechaCreacion;
	private Date fechaOperaciones;
	private String estado;
	private String clase;
	private String tipo;
	private String dspVendedor;
	private int version;
	
	private ICliente cliente;
	private Set<LineaActividad> listaActividades;
	private Set<Pax> listaPaxs;
	private Set<IReserva> listaReservas;

	public HojaServicioBK() {
		this.fechaCreacion = new Date();
		listaActividades = new HashSet<LineaActividad>();
		listaPaxs = new HashSet<Pax>();
		listaReservas = new HashSet<IReserva>();
	}
	
	public HojaServicioBK(Long idHoja, Long idCotizacion, Date fechaInicio,
			Date fechaFin, String numero, String nombre, Integer paxs, String estado) {
		this.idHoja = idHoja;
		this.idCotizacion = idCotizacion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.numero = numero;
		this.nombre = nombre;
		this.paxs = paxs;
		this.estado = estado;
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
	
	public void agregarReserva(IReserva reserva) {
		listaReservas.add(reserva);
		//TODO: puesto en comentario al hacer backup (el this debe ser HojaServicio, no HojaServicioBK)
		//reserva.setHoja(this);
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
		return "HojaServicio (id-idCot-nombre): " + getIdHoja() + "-" + getIdCotizacion() + "-" + getNombre();
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

	public Long getIdCotizacion() {
		return idCotizacion;
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

	public String getNumero() {
		return numero;
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

	public void setIdCotizacion(Long idCotizacion) {
		this.idCotizacion = idCotizacion;
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

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public Integer getPaxs() {
		return paxs;
	}

	public void setPaxs(Integer paxs) {
		this.paxs = paxs;
	}

	public String getClase() {
		return clase;
	}

	public String getTipo() {
		return tipo;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public String getDspVendedor() {
		String resultado = dspVendedor == null ? "No encontrado" : dspVendedor;
		return resultado;
	}

	public void setDspVendedor(String dspVendedor) {
		this.dspVendedor = dspVendedor;
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
}
