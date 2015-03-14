package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Vendedor implements IEditableDocument {
	private Long idVendedor;
	private String nombre;
	private String apellido;
	private String estado;
	// variable utilizada solamente para display el texto del código de estado
	private String dspEstado;

	public Vendedor() {
	}
	
	public Vendedor(Long idVendedor, String nombre, String apellido, String estado) {
		this.idVendedor = idVendedor;
		this.nombre = nombre;
		this.apellido = apellido;
		this.estado = estado;
	}


// **************************** métodos especiales ****************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (getNombre() == null && getApellido() == null) {
			tituloDocumento = "Nuevo vendedor";
		} else {
			tituloDocumento = "Vendedor: " + getNombre() + " " + getApellido();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Vendedor (id-nombre): " + getIdVendedor() + "-" + getNombre() + " " + getApellido();
	}
	
	
// ***************************** getters y setters ****************************

	public String getApellido() {
		return apellido;
	}

	public Long getIdVendedor() {
		return idVendedor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setIdVendedor(Long idVendedor) {
		this.idVendedor = idVendedor;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDspEstado() {
		return dspEstado;
	}

	public void setDspEstado(String dspEstado) {
		this.dspEstado = dspEstado;
	}

}
