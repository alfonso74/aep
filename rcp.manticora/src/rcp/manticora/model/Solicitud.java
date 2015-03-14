package rcp.manticora.model;

import java.util.Date;

import rcp.manticora.IEditableDocument;


public class Solicitud implements IEditableDocument {
	private Long idSolicitud;
	private Long idVendedor;
	private String dspVendedor;
	private Long idPais;
	private String dspPais;
	private String nombre;
	private String apellido;
	private String telefono;
	private String celular;
	private String email;
	private Date fechaInicio;
	private Date fechaFin;
	private String programa;
	private String comentario;
	private Date fechaCreacion;
	private Date fechaAsignacion;
	private Date fechaFinalizacion;
	private String estado;

	public Solicitud() {
		this.fechaCreacion = new Date();
	}

	public Solicitud(Long idSolicitud, String nombre, String apellido, String telefono,
			String celular, String email, Long idPais, Date fechaInicio, Date fechaFin,
			String programa, String comentario, String estado) {
		this.idSolicitud = idSolicitud;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.celular = celular;
		this.email = email;
		this.idPais = idPais;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.programa = programa;
		this.comentario = comentario;
		this.fechaCreacion = new Date();
		this.estado = estado;
	}
	
	
// ************************ métodos especiales ******************************

	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (nombre == null && apellido == null) {
			tituloDocumento = "Nueva solicitud";
		} else {
			tituloDocumento = "Sol: " + nombre + " " + apellido;
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Solicitud (id-nombre): " + getIdSolicitud() + "-" + getNombre() + " " + getApellido();
	}
	
	
// *************************** getters y setters ******************************

	public String getApellido() {
		return apellido;
	}

	public String getCelular() {
		return celular;
	}

	public String getComentario() {
		return comentario;
	}

	public String getEmail() {
		return email;
	}

	public Date getFechaAsignacion() {
		return fechaAsignacion;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public Date getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public Long getIdSolicitud() {
		return idSolicitud;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTelefono() {
		return telefono;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDspPais() {
		String resultado = dspPais == null ? "Error" : dspPais;
		return resultado;
	}

	public String getDspVendedor() {
		String resultado = dspVendedor == null ? "No asignado" : dspVendedor;
		return resultado;
	}

	public Long getIdPais() {
		return idPais;
	}

	public Long getIdVendedor() {
		return idVendedor;
	}

	public void setDspPais(String dspPais) {
		this.dspPais = dspPais;
	}

	public void setDspVendedor(String dspVendedor) {
		this.dspVendedor = dspVendedor;
	}

	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}

	public void setIdVendedor(Long idVendedor) {
		this.idVendedor = idVendedor;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

}
