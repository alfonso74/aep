package rcp.manticora.model;

import java.util.Date;

public class LineaCotizacion {
	private Long idDetalle = -1L;
	private Long idProducto;
	private String dspProducto;
	private Long idCotizacion;
	private Date fecha;
	private Integer secuencia;
	private Integer cantidad;
	private Float precio;
	private Integer espacios;
	private Boolean hotelAEP;
	private Boolean visible;
	private String comentario;
	private int version;
	
	private Producto producto;
	
	public LineaCotizacion() {}

	public LineaCotizacion(Long idDetalle, Long idProducto, Long idCotizacion,
			Date fecha, String comentario) {
		this.idDetalle = idDetalle;
		this.idProducto = idProducto;
		this.idCotizacion = idCotizacion;
		this.fecha = fecha;
		this.comentario = comentario;
	}
	
	public LineaCotizacion(Long idDetalle, String producto, Long idCotizacion,
			Date fecha, String comentario) {
		this.idDetalle = idDetalle;
		this.idCotizacion = idCotizacion;
		this.fecha = fecha;
		this.comentario = comentario;
	}
	

// ************************** métodos especiales ******************************
	
	@Override
	public String toString() {
		return "LineaCotizacion (id-idCot-idProd-seq): " + getIdDetalle() + "-" + getIdCotizacion() + "-" + getIdProducto() + "-" + getSecuencia() + "-" + getFecha().toString();
	}
	
	/*
	@Override
	public int hashCode() {
		//System.out.println("Hash de " + this + ": " + super.hashCode());
		//return super.hashCode();
		return 29 * getIdDetalle().intValue();
	}
	*/
	
	/*
	@Override
	public int hashCode() {
		System.out.println("HashCode()");
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idDetalle == null) ? 0 : idDetalle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("equals()");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LineaCotizacion other = (LineaCotizacion) obj;
		if (idDetalle == null) {
			if (other.idDetalle != null)
				return false;
		} else if (!idDetalle.equals(other.idDetalle))
			return false;
		return true;
	}
	*/
	
	
// *************************** getters y setters *****************************
	

	public String getComentario() {
		return comentario;
	}

	public Date getFecha() {
		return fecha;
	}

	public Long getIdCotizacion() {
		return idCotizacion;
	}

	public Long getIdDetalle() {
		return idDetalle;
	}

	public Long getIdProducto() {
		//return idProducto;
		return getProducto().getIdProducto();
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setIdCotizacion(Long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public void setIdDetalle(Long idDetalle) {
		this.idDetalle = idDetalle;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getDspProducto() {
		return dspProducto;
	}

	public void setDspProducto(String dspProducto) {
		this.dspProducto = dspProducto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getEspacios() {
		return espacios;
	}

	public void setEspacios(Integer espacios) {
		this.espacios = espacios;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	public Boolean isVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean isHotelAEP() {
		return hotelAEP;
	}

	public void setHotelAEP(Boolean hotelAEP) {
		this.hotelAEP = hotelAEP;
	}

	public Integer getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(Integer secuencia) {
		this.secuencia = secuencia;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
