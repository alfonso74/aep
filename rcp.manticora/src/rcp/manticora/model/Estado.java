package rcp.manticora.model;

public class Estado {
	
	private Long idKeyword = -1L;
	private String codigo;
	private String descripcion;
	private String tipo;
	
	public Long getIdKeyword() {
		return idKeyword;
	}
	public void setIdKeyword(Long idKeyword) {
		this.idKeyword = idKeyword;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
