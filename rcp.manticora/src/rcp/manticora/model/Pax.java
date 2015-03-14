package rcp.manticora.model;

import java.util.Date;

public class Pax {
	private Long idPax = -1L;       // indica que es un nuevo objeto Pax
	private Long idPais;
	private Long idCotizacion;
	private String nombre = "";
	private String apellido = "";
	private String identificacion = "";
	private Date fechaNacimiento;
	private Integer peso;
	private String condiciones = "";
	
	private String dspPais;

	public Pax() {}
	
	
// ***************************** métodos especiales **************************
	
	@Override
	public String toString() {
		return "Pax (id-idCot-nombre): " + getIdPax() + "-" + getIdCotizacion() + "-" + getNombre() + " " + getApellido();
	}
	
	public String getNombreCompleto() {
		return getNombre() + " " + getApellido();
	}
	
	
// ***************************** getters y setters ***************************
	
	public String getApellido() {
		return apellido;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public Long getIdCotizacion() {
		return idCotizacion;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public Long getIdPais() {
		return idPais;
	}

	public Long getIdPax() {
		return idPax;
	}

	public String getNombre() {
		return nombre;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public void setIdCotizacion(Long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}

	public void setIdPax(Long idPax) {
		this.idPax = idPax;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public String getDspPais() {
		return dspPais;
	}

	public void setDspPais(String dspPais) {
		this.dspPais = dspPais;
	}

	public String getCondiciones() {
		return condiciones;
	}

	public void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}
}
