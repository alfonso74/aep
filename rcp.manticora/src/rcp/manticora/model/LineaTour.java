package rcp.manticora.model;

public class LineaTour {
	private Long idDetalle = -1L;
	//private Long idProducto;
	private Long idTour;
	private Integer dia;
	private Integer secuencia;
	private String tipoReserva;
	private String comidas;
	private String comentario;

	//private String dspProducto;
	private boolean breakfast = false;
	private boolean lunch = false;
	private boolean dinner = false;
	private boolean welcomeCocktail = false;
	private boolean other = false;
	
	private Producto producto;

	public LineaTour() {
	}
	
	
// ***************************** métodos especiales **************************
	
	@Override
	public String toString() {
		return "LineaTour (id-idTour-idProd): " + getIdDetalle() + "-" + getIdTour() + "-" + getIdProducto();
	}
	
	
	public boolean hasComidas() {
		if (hasBreakfast() || hasLunch() || hasDinner() || hasWelcomeCocktail() || hasOther()) {
			return true;
		} else {
			return false;
		}
	}
	
	
// ***************************** getters y setters ***************************

	public String getComentario() {
		return comentario;
	}

	public Integer getDia() {
		return dia;
	}

	public String getDspProducto() {
		//return dspProducto;
		return getProducto().getDescripcion();
	}

	public Long getIdDetalle() {
		return idDetalle;
	}

	public Long getIdProducto() {
		//return idProducto;
		return getProducto().getIdProducto();
	}

	public Long getIdTour() {
		return idTour;
	}

	public Integer getSecuencia() {
		return secuencia;
	}

	public String getTipoReserva() {
		return tipoReserva;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	/*
	public void setDspProducto(String dspProducto) {
		this.dspProducto = dspProducto;
	}
	*/

	public void setIdDetalle(Long idDetalle) {
		this.idDetalle = idDetalle;
	}

	/*
	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	*/

	public void setIdTour(Long idTour) {
		this.idTour = idTour;
	}

	public void setSecuencia(Integer secuencia) {
		this.secuencia = secuencia;
	}

	public void setTipoReserva(String tipoReserva) {
		this.tipoReserva = tipoReserva;
	}

	public String getComidas() {
		return comidas;
	}

	public void setComidas(String comidas) {
		if (comidas != null) {
			Boolean b = (comidas.toUpperCase().contains("B") ? true : false);
			Boolean l = (comidas.toUpperCase().contains("L") ? true : false);
			Boolean d = (comidas.toUpperCase().contains("D") ? true : false);
			Boolean w = (comidas.toUpperCase().contains("W") ? true : false);
			Boolean o = (comidas.toUpperCase().contains("O") ? true : false);
			setBreakfast(b);
			setLunch(l);
			setDinner(d);
			setWelcomeCocktail(w);
			setOther(o);
		}
		this.comidas = comidas;
	}

	public Boolean hasBreakfast() {
		return breakfast;
	}

	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setBreakfast(Boolean breakfast) {
		this.breakfast = breakfast;
	}
	
	public Boolean hasDinner() {
		return dinner;
	}

	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setDinner(Boolean dinner) {
		this.dinner = dinner;
	}

	public Boolean hasLunch() {
		return lunch;
	}

	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setLunch(Boolean lunch) {
		this.lunch = lunch;
	}
	
	public Boolean hasWelcomeCocktail() {
		return welcomeCocktail;
	}
	
	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setWelcomeCocktail(Boolean welcomeCocktail) {
		this.welcomeCocktail = welcomeCocktail;
	}
	
	public Boolean hasOther() {
		return other;
	}
	
	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setOther(Boolean other) {
		this.other = other;
	}


	public Producto getProducto() {
		return producto;
	}


	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}
