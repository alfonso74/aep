package rcp.manticora.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import rcp.manticora.IEditableDocument;
import rcp.manticora.services.CotizacionesComparator;


public class Cotizacion implements IEditableDocument {
	private Long idCotizacion;
	private Long idVendedor;
	private Long idSolicitud;
	private NumeroTour numeroTour;
	private Date fechaInicio;
	private Date fechaFin;
	private String nombre;
	private String prospecto;
	private Float subtotal;
	private Float porcHospedaje;
	private Float hospedaje;
	private Float porcImpuesto;
	private Float impuesto;
	private Float total;
	private Integer paxs;
	private Float pago;
	private Float porcPago;
	private String noFactura;
	private String creadoPor;
	private Date fechaCreacion;
	private Date fechaFinalizacion;
	private Date fechaContabilidad;
	private String estado;
	private String dspVendedor;
	
	private Set<Comision> listaComisiones;
	private Red redViajes;
	private ICliente cliente;
	private Set<LineaCotizacion> listaActividades;
	private Set<Pax> listaPaxs;
	
	public Cotizacion() {
		listaComisiones = new HashSet<Comision>();
		//listaActividades = new HashSet<LineaCotizacion>();
		listaActividades = new TreeSet<LineaCotizacion>(new CotizacionesComparator());
		listaPaxs = new HashSet<Pax>();
		this.fechaCreacion = new Date();
	}
	
	public Cotizacion(Long idCotizacion, Date fechaInicio, Date fechaFin,
			String nombre, Float subtotal, Float porcHospedaje, Float hospedaje,
			Float porcImpuesto, Float impuesto, Float total, Float pago, 
			Integer paxs, String estado) {
		this.idCotizacion = idCotizacion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.nombre = nombre;
		this.subtotal = subtotal;
		this.porcHospedaje = porcHospedaje;
		this.hospedaje = hospedaje;
		this.porcImpuesto = porcImpuesto;
		this.impuesto = impuesto;
		this.total = total;
		this.pago = pago;
		this.paxs = paxs;
		this.estado = estado;
		this.fechaCreacion = new Date();
		//listaActividades = new HashSet<LineaCotizacion>();
		listaActividades = new TreeSet<LineaCotizacion>();
		listaPaxs = new HashSet<Pax>();
	}

// ***************************** métodos especiales **************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (getNombre() == null) {
			tituloDocumento = "Nueva cotización";
		} else {
			tituloDocumento = "Cot: " + getNombre();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Cotización (id-nombre): " + getIdCotizacion() + "-" + getNombre();
	}
		
	public void resetListaActividades() {
		this.listaActividades.clear();
	}

	public void agregarActividad(LineaCotizacion linea) {
		if (linea.getFecha() == null) {
			System.err.println("La línea de cotización tiene la fecha en blanco: " + linea);
		} else if (linea.getSecuencia() == null) {
			System.err.println("La línea de cotización no tiene secuencia: " + linea);
		} else {
			listaActividades.add(linea);
		}
	}

	public void eliminarActividad(LineaCotizacion linea) {
		System.out.println("Removiendo línea: " + linea);
		listaActividades.remove(linea);
	}

	public void resetListaPaxs() {
		if (listaPaxs != null) {
			this.listaPaxs.clear();
		}
	}

	public void agregarPax(Pax linea) {
		if (listaPaxs == null) System.out.println("listaPaxs: NULL");
		System.out.println("Agregando: " + linea);
		listaPaxs.add(linea);
	}

	public void eliminarPax(Pax linea) {
		listaPaxs.remove(linea);
	}
	
	
	public boolean hasNombrePax(String nombre) {
		for (Pax pax : listaPaxs) {
			if (pax.getNombreCompleto().toLowerCase().indexOf(nombre) != -1) {
				return true; 
			}
		}
		return false;
	}
	
	
	public boolean hasPagoParcial() {
		if (getPorcPago() != 0 && getPorcPago() != 100) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Helper method utilizado para inicializar el campo de CreadoPor.  Usado
	 * en CotizacionesEditor.
	 * @param creador
	 */
	public void setCreador(String creador) {
		if (getIdCotizacion() == null || getIdCotizacion().longValue() == -1) {
			this.creadoPor = creador;
		}
	}
	
	
	public String getNumeroTourAsString() {
		return numeroTour == null ? null : getNumeroTour().getNumero();
	}
	
	public void setNumeroTour(String numero) {
		this.numeroTour = new NumeroTour(numero); 
	}
	
	public NumeroTour getNumeroTour() {
		return numeroTour;
	}
	
	public void setNumeroTour(NumeroTour numeroTour) {
		this.numeroTour = numeroTour; 
	}
	
	
// ****************************** getters y setters *************************

	public String getEstado() {
		return estado;
	}
	
	public Comision getComision() {
		Comision comision = null;
		if (listaComisiones != null && !listaComisiones.isEmpty()) {
			comision = listaComisiones.iterator().next();
		}
		return comision;
	}

	public void setComision(Comision comision) {
		if (comision == null) {
			getListaComisiones().clear();
		} else {
			getListaComisiones().add(comision);
		}
	}
	
	protected Set<Comision> getListaComisiones() {
		return listaComisiones;
	}

	protected void setListaComisiones(Set<Comision> listaComisiones) {
		this.listaComisiones = listaComisiones;
	}

	public Red getRedViajes() {
		return redViajes;
	}

	public void setRedViajes(Red redViajes) {
		this.redViajes = redViajes;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public Long getIdCotizacion() {
		return idCotizacion;
	}

	public Long getIdVendedor() {
		return idVendedor;
	}

	public Float getImpuesto() {
		return impuesto;
	}

	public Set<LineaCotizacion> getListaActividades() {
		return listaActividades;
	}
	
	public Set<Pax> getListaPaxs() {
		return listaPaxs;
	}

	public Integer getPaxs() {
		return paxs;
	}

	public Float getSubtotal() {
		return subtotal;
	}

	public Float getTotal() {
		return total;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setIdCotizacion(Long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public void setIdVendedor(Long idVendedor) {
		this.idVendedor = idVendedor;
	}

	public void setImpuesto(Float impuesto) {
		this.impuesto = impuesto;
	}

	public void setListaActividades(Set<LineaCotizacion> listaActividades) {
		this.listaActividades = listaActividades;
	}
	
	public void setListaPaxs(Set<Pax> listaPaxs) {
		this.listaPaxs = listaPaxs;
	}

	public void setPaxs(Integer paxs) {
		this.paxs = paxs;
	}

	public void setSubtotal(Float subtotal) {
		this.subtotal = subtotal;
	}

	public void setTotal(Float total) {
		this.total = total;
	}
	
	public String getDspVendedor() {
		return dspVendedor;
	}

	public void setDspVendedor(String dspVendedor) {
		this.dspVendedor = dspVendedor;
	}
	

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Float getHospedaje() {
		return hospedaje;
	}

	public Float getPago() {
		return pago;
	}

	public Float getPorcHospedaje() {
		return porcHospedaje;
	}

	public Float getPorcImpuesto() {
		return porcImpuesto;
	}
	
	public void setHospedaje(Float descuento) {
		this.hospedaje = descuento;
	}

	public void setPago(Float pago) {
		this.pago = pago;
	}

	public void setPorcHospedaje(Float porcDescuento) {
		this.porcHospedaje = porcDescuento;
	}

	public void setPorcImpuesto(Float porcImpuesto) {
		this.porcImpuesto = porcImpuesto;
	}
	
	public Cliente getCliente() {
		return (Cliente) cliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setCliente(ICliente cliente) {
		this.cliente = cliente;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Date getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public Date getFechaContabilidad() {
		return fechaContabilidad;
	}

	public void setFechaContabilidad(Date fechaContabilidad) {
		this.fechaContabilidad = fechaContabilidad;
	}

	public Float getPorcPago() {
		Float resultado = porcPago == null ? 0 : porcPago;
		return resultado;
	}

	public void setPorcPago(Float porcPago) {
		this.porcPago = porcPago;
	}

	public String getProspecto() {
		return prospecto;
	}

	public void setProspecto(String prospecto) {
		this.prospecto = prospecto;
	}

	public Long getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	public String getNoFactura() {
		return noFactura;
	}

	public void setNoFactura(String noFactura) {
		this.noFactura = noFactura;
	}
	
}

