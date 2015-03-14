package rcp.manticora.model;

public class LineaTemplate {
	private Long idTemplate;
	private Long idDetalle = -1L;
	//private Long idProducto;
	//private String dspProducto;   // nombre del producto
	private Integer dia;
	private Integer secuencia;
	private Integer cantidad;
	private String reserva;
	private String tipoReserva;
	private String comentario;
	private boolean breakfast = false;
	private boolean lunch = false;
	private boolean dinner = false;
	private String comidas;
	
	private Producto producto;
	

	public LineaTemplate(Long idTemplate, Long idDetalle, Long idProducto, String tipoReserva, 
			String comentario) {
		this.idTemplate = idTemplate;
		this.idDetalle = idDetalle;
		//this.idProducto = idProducto;
		this.tipoReserva = tipoReserva;
		this.comentario = comentario;
	}
	
	public LineaTemplate() {}

	
// **************************** métodos especiales ****************************
	
	@Override
	public String toString() {
		return "LineaTemplate (id-idTemp-idProd): " + getIdDetalle() + "-" + getIdTemplate() + "-" + getIdProducto();
	}
	
	
// **************************** getters y setters *****************************
	
	public String getComentario() {
		return comentario;
	}

	public String getDspProducto() {
		//return dspProducto;
		return getProducto().getDescripcion();
	}

	public Long getIdProducto() {
		//return idProducto;
		return getProducto().getIdProducto();
	}
	
	public Long getIdDetalle() {
		return idDetalle;
	}

	public Long getIdTemplate() {
		return idTemplate;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	/*
	public void setDspProducto(String descripcion) {
		this.dspProducto = descripcion;
	}
	*/

	/*
	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	*/

	public void setIdTemplate(Long idTipo) {
		this.idTemplate = idTipo;
	}

	public void setIdDetalle(Long idLinea) {
		this.idDetalle = idLinea;
	}

	public String getReserva() {
		return reserva;
	}

	public void setReserva(String reserva) {
		this.reserva = reserva;
	}

	public String getTipoReserva() {
		return tipoReserva;
	}

	public void setTipoReserva(String tipoReserva) {
		this.tipoReserva = tipoReserva;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public Integer getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(Integer secuencia) {
		this.secuencia = secuencia;
	}

	public Boolean isBreakfast() {
		return breakfast;
	}

	public void setBreakfast(Boolean breakfast) {
		this.breakfast = breakfast;
	}

	public String getComidas() {
		return comidas;
	}

	public void setComidas(String comidas) {
		this.comidas = comidas;
		if (comidas != null) {
			Boolean b = (comidas.toUpperCase().contains("B") ? true : false);
			Boolean l = (comidas.toUpperCase().contains("L") ? true : false);
			Boolean d = (comidas.toUpperCase().contains("D") ? true : false);
			setBreakfast(b);
			setLunch(l);
			setDinner(d);
		}
	}

	public Boolean isDinner() {
		return dinner;
	}

	public void setDinner(Boolean dinner) {
		this.dinner = dinner;
	}

	public Boolean isLunch() {
		return lunch;
	}

	public void setLunch(Boolean lunch) {
		this.lunch = lunch;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}
