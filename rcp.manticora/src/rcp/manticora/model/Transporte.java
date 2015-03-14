package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Transporte implements IEditableDocument {
	private Long idTransporte = -1L;
	private String nombre;
	private String apellido;
	private String estado;
	private String dspEstado;

	public Transporte() {
	}
	
	public Transporte(Long idTransporte, String nombre, String apellido, String estado) {
		this.idTransporte = idTransporte;
		this.nombre = nombre;
		this.apellido = apellido;
		this.estado = estado;
	}
	
	
// **************************** métodos especiales ***************************

	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (getNombre() == null && getApellido() == null) {
			tituloDocumento = "Nuevo transportista";
		} else {
			tituloDocumento = "Transp: " + getNombre() + " " + getApellido();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Transporte (id-nombre): " + getIdTransporte() + "-" + getNombre() + " " + getApellido();
	}
	
	
// **************************** getters y setters ****************************

	public String getApellido() {
		return apellido;
	}

	public String getEstado() {
		return estado;
	}

	public Long getIdTransporte() {
		return idTransporte;
	}

	public String getNombre() {
		return nombre;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setIdTransporte(Long idTransporte) {
		this.idTransporte = idTransporte;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDspEstado() {
		return dspEstado;
	}

	public void setDspEstado(String dspEstado) {
		this.dspEstado = dspEstado;
	}

}
