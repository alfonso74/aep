package rcp.manticora.model;

import java.util.Date;

import rcp.manticora.IEditableDocument;


public class Producto implements IEditableDocument {
	private Long idProducto = new Long(-1);
	private Long idTipo;
	private String descripcion;
	private Float costo;
	private Float venta0;       // precio mínimo de venta
	private Float venta1;       // precio público
	private Float venta2;       // precio operador
	private Float venta3;       // precio comisionable
	private String tipoReserva;
	private String isTour;
	private Boolean tour;
	private Boolean hotelAEP;
	private Boolean modificable;
	private String comentario;
	private String estado;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String dspEstado;
	private String dspTipo;
	
	public Producto() {
		fechaCreacion = new Date();
		fechaModificacion = fechaCreacion;
	};
	
	public Producto(Long idTipo, Long idProducto, String descripcion, Float costo, 
			Float venta0, Float venta1, Float venta2, Float venta3, String estado) {
		this.idTipo = idTipo;
		this.idProducto = idProducto;
		this.descripcion = descripcion;
		this.costo = costo;
		this.venta0 = venta0;
		this.venta1 = venta1;
		this.venta2 = venta2;
		this.venta3 = venta3;
		this.estado = estado;
		this.fechaCreacion = new Date();
		this.fechaModificacion = this.fechaCreacion;
	}
	
	public Producto(Long idTipo, Long idProducto, String descripcion, Float costo, 
			Float venta0, Float venta1, Float venta2, Float venta3, String tipoReserva,
			String isTour, String estado, String comentario) {
		this.idTipo = idTipo;
		this.idProducto = idProducto;
		this.descripcion = descripcion;
		this.costo = costo;
		this.venta0 = venta0;
		this.venta1 = venta1;
		this.venta2 = venta2;
		this.venta3 = venta3;
		this.tipoReserva = tipoReserva;
		this.isTour = isTour;
		this.estado = estado;
		this.comentario = comentario;
		this.fechaCreacion = new Date();
		this.fechaModificacion = this.fechaCreacion;
	}
	
// ************************** métodos especiales ****************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (getDescripcion() == null) {
			tituloDocumento = "Nuevo producto";
		} else {
			tituloDocumento = getDescripcion();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Producto (id-nombre): " + getIdProducto() + "-" + getDescripcion();
	}

	public Float getPrecioByTipo(String tipoPrecio) {
		Float precio = null;
		if (tipoPrecio.equals("Comisionable")) {
			precio = getVenta3();
		}
		if (tipoPrecio.equals("Operador")) {
			precio = getVenta2();
		}
		if (tipoPrecio.equals("Público")) {
			precio = getVenta1();
		}
		return precio;
	}
	
	public Float getPrecioMinimo() {
		return (getVenta0() == null ? 0f : getVenta0());
		//return getVenta0();
	}
	
	/**
	 * Retorna la descripción de un producto más el texto "Night(s) at" al inicio del mismo.  Solo
	 * tiene sentido usarlo con hoteles.
	 * @return Nombre del producto, incluyendo "Night(s) at ".
	 */
	public String getDescripcionHotel() {
		String descripcion = getDescripcion();
		// Si el producto es de tipo "Hospedaje"
		if (getIdTipo() == 4) {
			if (getDescripcion().indexOf("Night") == -1) {  // y no tiene el texto "Night" x ningún lado
				descripcion = "Night(s) at " + descripcion;
			}
		}
		return descripcion;
	}
	
// *************************** getters y setters ***************************

	public Float getCosto() {
		return costo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public Long getIdTipo() {
		return idTipo;
	}

	public String getEstado() {
		return estado;
	}

	public String getIsTour() {
		return isTour;
	}

	public String getTipoReserva() {
		return tipoReserva;
	}

	public void setCosto(float costo) {
		this.costo = costo;
	}

	public void setDescripcion(String descripcion) {
		
		if (descripcion.startsWith("Night(s) at ")) {
			this.descripcion = descripcion.substring(12);
		} else if (descripcion.startsWith("Night at ")){
			this.descripcion = descripcion.substring(9);
		} else if (descripcion.startsWith("Night(s) ")) {
			this.descripcion = descripcion.substring(9);
		} else if (descripcion.startsWith("Night(s)")) {
			this.descripcion = descripcion.substring(8);
		} else if (descripcion.startsWith("Night ")) {
			this.descripcion = descripcion.substring(6);
		} else {
			this.descripcion = descripcion;
		}
		
		this.descripcion = descripcion;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}

	public void setIsTour(String isTour) {
		this.isTour = isTour;
	}

	public void setTipoReserva(String reserva) {
		this.tipoReserva = reserva;
	}

	public Float getVenta1() {
		return venta1;
	}

	public Float getVenta2() {
		return venta2;
	}

	public Float getVenta3() {
		return venta3;
	}

	public void setCosto(Float costo) {
		this.costo = costo;
	}

	public void setVenta1(Float venta1) {
		this.venta1 = venta1;
	}

	public void setVenta2(Float venta2) {
		this.venta2 = venta2;
	}

	public void setVenta3(Float venta3) {
		this.venta3 = venta3;
	}

	public String getDspEstado() {
		return dspEstado;
	}

	public void setDspEstado(String estadoTexto) {
		this.dspEstado = estadoTexto;
	}

	public String getDspTipo() {
		return dspTipo;
	}

	public void setDspTipo(String dspTipo) {
		this.dspTipo = dspTipo;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Boolean isHotelAEP() {
		return hotelAEP;
	}

	public void setHotelAEP(Boolean hotelAEP) {
		this.hotelAEP = hotelAEP;
	}

	public Float getVenta0() {
		return venta0;
	}

	public void setVenta0(Float venta0) {
		this.venta0 = venta0;
	}

	public Boolean isModificable() {
		return modificable;
	}

	public void setModificable(Boolean modificable) {
		this.modificable = modificable;
	}
	
	public Boolean isTour() {
		return tour;
	}
	
	public void setTour(Boolean tour) {
		this.tour = tour;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}
