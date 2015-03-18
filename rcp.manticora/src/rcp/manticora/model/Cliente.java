package rcp.manticora.model;

import java.util.LinkedHashSet;
import java.util.Set;

import rcp.manticora.IEditableDocument;


public class Cliente implements IEditableDocument, ICliente, Comparable<ICliente> {
	private Long idCliente = -1L;
	private TipoCliente tipo;
	private String clase;
	private Long idPais;
	private String dspPais;
	private String numero;
	private String identificacion;
	private String telefono;
	private String telefono2;
	private String email;
	private String direccion1, direccion2, direccion3;
	private String apartado;
	private String ciudad;
	private String comentario;
	private Boolean comision;
	private String estado;
	private String formaPago;
	private String fuente;
	

	public Cliente() {};

	public Cliente(Long idCliente,
			 String identificacion, String telefono, String telefono2, String email, 
			 Long idPais, String direccion1, String direccion2, String direccion3,
			 String apartado, String ciudad, String comentario, String estado) {
		this.idCliente = idCliente;
		this.idPais = idPais;
		this.identificacion = identificacion;
		this.telefono = telefono;
		this.telefono2 = telefono2;
		this.email = email;
		this.direccion1 = direccion1;
		this.direccion2 = direccion2;
		this.direccion3 = direccion3;
		this.apartado = apartado;
		this.ciudad = ciudad;
		this.comentario = comentario;
		this.estado = estado;
	}
	
//	************************* métodos especiales ****************************

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getTituloDocumento()
	 */

	public String getTituloDocumento() {
		return "Registro faltante";
	}
	
	public String getNombreCliente() {
		return "Registro faltante en tablas de detalle";
	}
	
	public Set<String> generarDireccion() {
		Set<String> set = new LinkedHashSet<String>();
		String lineaDireccion = "";
		
		lineaDireccion = getNombreCliente();
		if (lineaDireccion != null && !lineaDireccion.equals("")) {
			set.add(lineaDireccion);
		}
		lineaDireccion = getDireccion1();
		if (lineaDireccion != null && !lineaDireccion.equals("")) {
			set.add(lineaDireccion);
		}
		lineaDireccion = getDireccion2();
		if (lineaDireccion != null && !lineaDireccion.equals("")) {
			set.add(lineaDireccion);
		}
		lineaDireccion = getDireccion3();
		if (lineaDireccion != null && !lineaDireccion.equals("")) {
			set.add(lineaDireccion);
		}
		return set;
	}

	
	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getDspPais()
	 */
	public String getDspPais() {
		if (dspPais == null) {
			return "";
		} else {
			return dspPais;
		}
	}
	
	
//	*************************** getters y setters **************************

	public Boolean getComision() {
		return comision;
	}

	public void setComision(Boolean comision) {
		this.comision = comision == null ? Boolean.FALSE : comision.booleanValue();
	}
	
	public TipoCliente getTipo() {
		return tipo;
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getApartado()
	 */
	public String getApartado() {
		return apartado;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getCelular()
	 */
	public String getTelefono2() {
		return telefono2;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getCiudad()
	 */
	public String getCiudad() {
		return ciudad;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getComentario()
	 */
	public String getComentario() {
		return comentario;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getDireccion1()
	 */
	public String getDireccion1() {
		return direccion1;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getDireccion2()
	 */
	public String getDireccion2() {
		return direccion2;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getDireccion3()
	 */
	public String getDireccion3() {
		return direccion3;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getEmail()
	 */
	public String getEmail() {
		return email;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getEstado()
	 */
	public String getEstado() {
		return estado;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getIdCliente()
	 */
	public Long getIdCliente() {
		return idCliente;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getIdentificacion()
	 */
	public String getIdentificacion() {
		return identificacion;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getIdPais()
	 */
	public Long getIdPais() {
		return idPais;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getNumero()
	 */
	public String getNumero() {
		return numero;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#getTelefono()
	 */
	public String getTelefono() {
		return telefono;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setApartado(java.lang.String)
	 */
	public void setApartado(String apartado) {
		this.apartado = apartado;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setCelular(java.lang.String)
	 */
	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setCiudad(java.lang.String)
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setComentario(java.lang.String)
	 */
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setDireccion1(java.lang.String)
	 */
	public void setDireccion1(String direccion1) {
		this.direccion1 = direccion1;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setDireccion2(java.lang.String)
	 */
	public void setDireccion2(String direccion2) {
		this.direccion2 = direccion2;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setDireccion3(java.lang.String)
	 */
	public void setDireccion3(String direccion3) {
		this.direccion3 = direccion3;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setDspPais(java.lang.String)
	 */
	public void setDspPais(String dspPais) {
		this.dspPais = dspPais;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setEmail(java.lang.String)
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setEstado(java.lang.String)
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setIdCliente(int)
	 */
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setIdentificacion(java.lang.String)
	 */
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setIdPais(int)
	 */
	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}
	
	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setNumero(java.lang.String)
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/* (non-Javadoc)
	 * @see manticora.model.ICliente#setTelefono(java.lang.String)
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getFormaPago() {
		return formaPago;
	}

	public String getFuente() {
		return fuente;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public void setFuente(String fuente) {
		this.fuente = fuente;
	}
	
	
	public enum Clase {
		JURIDICO("C"),
		NATURAL("N");
		
		private String codigo; 
		
		private Clase(String codigo) {
			this.codigo = codigo;
		}
		
		public String getCodigo() {
			return codigo;
		}

		public Clase fromCodigo(String codigo) {
			for (Clase v : values()) {
				if (v.codigo.equalsIgnoreCase(codigo)) {
					return v;
				}
			}
			throw new IllegalArgumentException("Clase de cliente no definida: " + codigo);
		}

	}


	public int compareTo(ICliente other) {
	    final int EQUAL = 0;
	    
	    if (this == other) return EQUAL;
	    //note that null objects will throw an exception here
	    int comparison = this.getNombreCliente().compareToIgnoreCase(other.getNombreCliente());
	    if (comparison != EQUAL) return comparison;
		
		return EQUAL;
	}

}
