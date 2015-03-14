package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Keyword implements IEditableDocument {
	private Long idKeyword = -1L;
	private String codigo;
	private String descripcion;
	private String tipo;

	public Keyword() {
	}
	
	public Keyword(Long idKeyword, String codigo, String descripcion, String tipo) {
		this.idKeyword = idKeyword;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.tipo = tipo;
	}
	
	
// **************************** métodos especiales ****************************

	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (descripcion == null) {
			tituloDocumento = "Nuevo keyword";
		} else {
			tituloDocumento = "Keyword: " + getDescripcion();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Keyword (id-desc): " + getIdKeyword() + "-" + getDescripcion();
	}
	
	
// **************************** getters y setters ****************************

	public String getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Long getIdKeyword() {
		return idKeyword;
	}

	public String getTipo() {
		return tipo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setIdKeyword(Long idKeyword) {
		this.idKeyword = idKeyword;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
