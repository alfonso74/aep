package rcp.manticora.model;

public class ClienteJuridico extends Cliente {
	private String nombreCia;
	private String referencia;
	private String contacto;

	public ClienteJuridico() {
		//super();
	}

	public ClienteJuridico(Long idCliente, String nombre, String identificacion,
			String referencia, String contacto, String telefono, String celular,
			String email, Long idPais, String direccion1,
			String direccion2, String direccion3, String apartado,
			String ciudad, String comentario, String estado) {
		super(idCliente, identificacion, telefono, celular, email, idPais,
				direccion1, direccion2, direccion3, apartado, ciudad,
				comentario, estado);
		this.nombreCia = nombre;
		this.referencia = referencia;
		this.contacto = contacto;
	}
	
	
// ****************************** métodos especiales **************************
	
	public String getNombreCliente() {
		return getNombreCia();
	}
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (getNombreCia() == null) {
			tituloDocumento = "Nueva compañía";
		} else {
			tituloDocumento = "Compañía: " + getNombreCliente();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Cliente jurídico (id-nombre): " + getIdCliente() + "-" + getNombreCliente();
	}
	
	
// ******************************* getters y setters **************************

	public String getContacto() {
		return contacto;
	}

	public String getNombreCia() {
		return nombreCia;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public void setNombreCia(String nombre) {
		this.nombreCia = nombre;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}
