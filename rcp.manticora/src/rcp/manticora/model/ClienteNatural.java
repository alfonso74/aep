package rcp.manticora.model;

import java.util.Date;

public class ClienteNatural extends Cliente {
	private String nombre;
	private String apellido;
	private Date fechaNacimiento;
	private String sexo;
	private Integer peso;

	public ClienteNatural() {
		//super();
	}
	
	public ClienteNatural(Long idCliente, String nombre, String apellido) {
		//TODO verificar si el super() es necesario para instanciar ClienteNatural
		super.setIdCliente(idCliente);
		this.nombre = nombre;
		this.apellido = apellido;
	}

	public ClienteNatural(Long idCliente, String nombre, String apellido,
			String identificacion, String telefono, String celular, 
			String email, Long idPais, String direccion1,
			String direccion2, String direccion3, String apartado,
			String ciudad, String comentario, String estado) {
		super(idCliente, identificacion, telefono, celular, email,
				idPais, direccion1, direccion2, direccion3,
				apartado, ciudad, comentario, estado);
		this.nombre = nombre;
		this.apellido = apellido;
		//this.fechaNacimiento = fechaNacimiento;
		//this.sexo = sexo;
	}
	
	
// *************************** métodos especiales *****************************
	
	public String getNombreCliente() {
		return getNombre() + " " + getApellido();
	}
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (getNombre() == null && getApellido() == null) {
			tituloDocumento = "Nuevo cliente";
		} else {
			tituloDocumento = "Cliente: " + getNombreCliente();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Cliente natural (id-nombre): " + getIdCliente() + "-" + getNombreCliente();
	}
	
	
// *************************** getters y setters ******************************

	public String getApellido() {
		return apellido;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public String getNombre() {
		return nombre;
	}

	public String getSexo() {
		return sexo;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public Integer getPeso() {
		return peso;
	}
	
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
}
