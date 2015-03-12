package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Pais implements IEditableDocument {
	private Long idPais = -1L;
	private String descripcion;
	private String estado;
	
	public Pais() {};
	
	public Pais(Long idPais, String descripcion) {
		this.idPais = idPais;
		this.descripcion = descripcion;
	}
	
	
// *************************** métodos especiales ****************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (descripcion == null) {
			tituloDocumento = "Nuevo país";
		} else {
			tituloDocumento = "País: " + descripcion;
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "País (id-desc): " + getIdPais() + "-" + getDescripcion();
	}
	
	
// *************************** getters y setters *****************************

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return Returns the idPais.
	 */
	public Long getIdPais() {
		return idPais;
	}
	
	public void setIdPais(Long idTipo) {
		this.idPais = idTipo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
