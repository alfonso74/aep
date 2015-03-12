package rcp.manticora.model;

import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class LineaActividad {
	private Long idActividad = -1L;
	//private Long idHoja;
	//private Long idProducto;
	//private String dspProducto;
	private Date fecha;
	private Date hora;
	private Float precio;
	private String tipoReserva;
	private String comidas;      // este campo no existe en la tabla
	private boolean breakfast;
	private boolean lunch;
	private boolean dinner;
	private boolean welcomeCocktail;
	private boolean other;
	private String comentario;
	private int version;
	
	private Producto producto;
	
	private Set<AsignacionReserva> listaAsignaciones = new HashSet<AsignacionReserva>();
	private IHojaServicio hoja;
	private HojaServicioTour hojaTour;
	
	//private LineaActividad parent;
	//private Set<LineaActividad> children = new HashSet<LineaActividad>();

	public LineaActividad() {
	}
	
	
// *************************** métodos especiales ***************************
	@Override
	public String toString() {
		if (hasReservas()) {
			return "Actividad (id-Hoja-Prod-#Res): " + getIdActividad() + "-" + getIdHoja() + "-" + getIdProducto() + "-" + getListaAsignaciones().size();
		} else {
			return "Actividad (id-Hoja-Prod-Res): " + getIdActividad() + "-" + getIdHoja() + "-" + getIdProducto() + "-" + null + "-" + this.getFecha();
		}
	}
	
	
	public boolean hasReservas() {
		if (getListaAsignaciones() != null && getListaAsignaciones().size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void agregarAsignacionReserva(AsignacionReserva asignacion) {
		listaAsignaciones.add(asignacion);
	}
	
	public void eliminarAsignacionReserva(AsignacionReserva asignacion) {
		listaAsignaciones.remove(asignacion);
	}
	
	
	public boolean hasComidas() {
		if (hasBreakfast() || hasLunch() || hasDinner() || hasWelcomeCocktail() || hasOther()) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getComidas() {
		return comidas;
	}
	
// helper method para asignar las comidas
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

	
	
// **************************** getters y setters ****************************

	public Set<AsignacionReserva> getListaAsignaciones() {
		return listaAsignaciones;
	}
	
	public void setListaAsignaciones(Set<AsignacionReserva> listaAsignaciones) {
		this.listaAsignaciones = listaAsignaciones;
	}
	
	public String getComentario() {
		return comentario;
	}

	public Date getFecha() {
		return fecha;
	}
	
	public Date getHora() {
		return hora;
	}

	public Long getIdActividad() {
		return idActividad;
	}

	public Long getIdHoja() {
		//return idHoja;
		return getHoja().getIdHoja();
	}

	public Float getPrecio() {
		return precio;
	}

	public String getTipoReserva() {
		return tipoReserva;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public void setHora(Date hora) {
		this.hora = hora;
	}

	public void setIdActividad(Long idActividad) {
		this.idActividad = idActividad;
	}

	/*
	public void setIdHoja(Long idHoja) {
		this.idHoja = idHoja;
	}
	*/

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	public void setTipoReserva(String tipoReserva) {
		this.tipoReserva = tipoReserva;
	}

	public Long getIdProducto() {
		//return idProducto;
		return getProducto().getIdProducto();
	}

	/*
	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	*/

	
	public String getDspProducto() {
		//return dspProducto;
		return getProducto().getDescripcion();
	}

	/*
	public void setDspProducto(String dspProducto) {
		this.dspProducto = dspProducto;
	}
	*/

	public boolean hasBreakfast() {
		return breakfast;
	}

	public boolean hasDinner() {
		return dinner;
	}

	public boolean hasLunch() {
		return lunch;
	}
	
	public boolean hasWelcomeCocktail() {
		return welcomeCocktail;
	}
	
	public boolean hasOther() {
		return other;
	}

	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setBreakfast(boolean breakfast) {
		this.breakfast = breakfast;
	}

	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setDinner(boolean dinner) {
		this.dinner = dinner;
	}

	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setLunch(boolean lunch) {
		this.lunch = lunch;
	}
	
	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setWelcomeCocktail(boolean welcomeCocktail) {
		this.welcomeCocktail = welcomeCocktail;
	}
	
	// esta propiedad es seteada automáticamente por el método setComidas()
	private void setOther(boolean other) {
		this.other = other;
	}

	public IHojaServicio getHoja() {
		return hoja;
	}

	public void setHoja(IHojaServicio hoja) {
		this.hoja = hoja;
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
/*
	public Set<LineaActividad> getChildren() {
		return children;
	}

	public void setChildren(Set<LineaActividad> children) {
		this.children = children;
	}

	public LineaActividad getParent() {
		return parent;
	}

	public void setParent(LineaActividad parent) {
		this.parent = parent;
	}
*/
	public HojaServicioTour getHojaTour() {
		return hojaTour;
	}

	public void setHojaTour(HojaServicioTour hojaTour) {
		this.hojaTour = hojaTour;
	}
	
}
