package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public interface ICliente extends IEditableDocument {

	//public abstract String getTituloDocumento();
	
	public abstract String getNombreCliente();
	

	// métodos modificados
	
	Boolean getComision();
	
	public abstract String getClase();
	
	public abstract String getDspPais();

	public abstract String getApartado();

	public abstract String getTelefono2();

	public abstract String getCiudad();

	public abstract String getComentario();

	public abstract String getDireccion1();

	public abstract String getDireccion2();

	public abstract String getDireccion3();

	public abstract String getDspTipo();

	public abstract String getEmail();

	public abstract String getEstado();

	public abstract Long getIdCliente();

	public abstract String getIdentificacion();

	public abstract Long getIdPais();

	public abstract Long getIdTipo();

	public abstract String getNumero();

	public abstract String getTelefono();
	
	public abstract String getFormaPago();

	public abstract String getFuente();
	
	void setComision(Boolean comision);
	
	public abstract void setClase(String clase);

	public abstract void setApartado(String apartado);

	public abstract void setTelefono2(String celular);

	public abstract void setCiudad(String ciudad);

	public abstract void setComentario(String comentario);

	public abstract void setDireccion1(String direccion1);

	public abstract void setDireccion2(String direccion2);

	public abstract void setDireccion3(String direccion3);

	public abstract void setDspPais(String dspPais);

	public abstract void setDspTipo(String dspTipo);

	public abstract void setEmail(String email);

	public abstract void setEstado(String estado);

	public abstract void setIdCliente(Long idCliente);

	public abstract void setIdentificacion(String identificacion);

	public abstract void setIdPais(Long idPais);

	public abstract void setIdTipo(Long idTipo);

	public abstract void setNumero(String numero);

	public abstract void setTelefono(String telefono);

	public abstract void setFormaPago(String formaPago);

	public abstract void setFuente(String fuente);

}