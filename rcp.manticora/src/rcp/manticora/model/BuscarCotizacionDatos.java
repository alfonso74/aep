package rcp.manticora.model;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class BuscarCotizacionDatos {
	
	private Long noCotizacion;
	private Long noCliente;
	private String numeroTour;
	private String nombreCotizacion;
	private String vendedor;
	private Date fechaInicio1;
	private Date fechaInicio2;
	private Date fechaFin1;
	private Date fechaFin2;
	
	private String claseCliente = "N";
	private String clienteNombre;
	private String clienteApellido;
	
	private String paxNombre;
	private String paxApellido;
	
	
	/**
	 * Genera un objeto Criteria de Hibernate de acuerdo con los parámetros introducidos
	 * en la pantalla de buscar cotización.
	 * @param session Sesión de hibernate que se utilizará para generar el Criteria
	 * @return Criteria adecuado a los parámetros de búsqueda
	 */
	public Criteria generarCriteria(Session session) {
		
		Criteria criteria = session.createCriteria(Cotizacion.class);
		
		if (this.hasNoCotizacion()) {
			criteria.add(Restrictions.eq("idCotizacion", this.getNoCotizacion()));
		}
		/*
		if (this.hasNoCliente()) {
			criteria.createCriteria("cliente")
				.add(Restrictions.eq("idCliente", this.getNoCliente()));
		}
		*/
		
		if (this.hasNumeroTour()) {
			criteria.add(Restrictions.like("numeroTour.numero", this.getNumeroTour()));
		}
		
		if (this.hasNombreCotizacion()){
			criteria.add(Restrictions.like("nombre", "%" + this.getNombreCotizacion() + "%"));
		}
		if (this.hasVendedor()) {
			criteria.add(Restrictions.eq("dspVendedor", this.getVendedor()));
		}
		if (this.hasFechaInicio1()) {
			if (this.hasFechaInicio2()) {
				criteria.add(Restrictions.between("fechaInicio", this.getFechaInicio1(), this.getFechaInicio2()));
			} else {
				criteria.add(Restrictions.gt("fechaInicio", this.getFechaInicio1()));
			}
		}
		if (this.hasFechaFin1()) {
			if (this.hasFechaFin2()) {
				criteria.add(Restrictions.between("fechaFin", this.getFechaFin1(), this.getFechaFin2()));
			} else {
				criteria.add(Restrictions.gt("fechaFin", this.getFechaFin1()));
			}
		}
		if (this.hasNoCliente() || this.hasCliente()) {
			Criteria critCliente = criteria.createCriteria("cliente");
			if (this.hasNoCliente()) {
				critCliente.add(Restrictions.eq("idCliente", this.getNoCliente()));
			}
			if (getClaseCliente().equals("N")) {  // consideramos el apellido si el cliente es una persona (tipo N)
				if (this.getClienteNombre() != null && !this.getClienteNombre().equals("")) {
					critCliente.add(Restrictions.like("nombre", "%" + this.getClienteNombre() + "%"));
				}
				if (this.getClienteApellido() != null && !this.getClienteApellido().equals("")) {
					critCliente.add(Restrictions.like("apellido", "%" + this.getClienteApellido() + "%"));
				}
			} else {
				if (this.getClienteNombre() != null && !this.getClienteNombre().equals("")) {
					critCliente.add(Restrictions.like("nombreCia", "%" + this.getClienteNombre() + "%"));
				}
			}
		}
		/*
		if (this.hasNoCliente() || this.hasCliente()) {
			Criteria critCliente = criteria.createCriteria("cliente");
			if (this.hasNoCliente()) {
				critCliente.add(Restrictions.eq("idCliente", this.getNoCliente()));
			}
			if (this.getClienteNombre() != null && !this.getClienteNombre().equals("")) {
				critCliente.add(Restrictions.like("nombre", "%" + this.getClienteNombre() + "%"));
			}
			if (getClaseCliente().equals("N")) {  // consideramos el apellido si el cliente es una persona (tipo N)
				if (this.getClienteApellido() != null && !this.getClienteApellido().equals("")) {
					critCliente.add(Restrictions.like("apellido", "%" + this.getClienteApellido() + "%"));
				}
			}
		}
		*/
		/*
		if (this.hasNoCliente() || this.hasCliente()) {
			Criteria critCliente = criteria.createCriteria("cliente");
			if (this.hasNoCliente()) {
				critCliente.add(Restrictions.eq("idCliente", this.getNoCliente()));
			}
			if (this.getClienteNombre() != null && !this.getClienteNombre().equals("")) {
				critCliente.add(Restrictions.like("nombreCia", "%" + this.getClienteNombre() + "%"));
			}
		}
		*/
		if (this.hasPax()) {
			Criteria critPaxs = criteria.createCriteria("listaPaxs");
			if (this.getPaxNombre() != null && !this.getPaxNombre().equals("")) {
				critPaxs.add(Restrictions.like("nombre", "%" + this.getPaxNombre() + "%"));
			}
			if (this.getPaxApellido() != null && !this.getPaxApellido().equals("")) {
				critPaxs.add(Restrictions.like("apellido", "%" + this.getPaxApellido() + "%"));
			}
		}
		
		return criteria;
		
	}
	
	
// ********************************* métodos de apoyo ****************************************
	
	public boolean hasNoCotizacion() {
		if (noCotizacion != null && noCotizacion.longValue() != 0) return true;
		return false;
	}
	
	public boolean hasNumeroTour() {
		if (numeroTour != null && !numeroTour.equals("")) {
			return true;
		}
		return false;
	}
	
	public boolean hasNoCliente() {
		if (noCliente != null && noCliente.longValue() != 0) return true;
		return false;
	}
	
	public boolean hasNombreCotizacion() {
		if (nombreCotizacion != null && !nombreCotizacion.equals("")) return true;
		return false;
	}
	
	public boolean hasVendedor() {
		if (vendedor != null && !vendedor.equals("")) return true;
		return false;
	}
	
	public boolean hasFechaInicio1() {
		if (fechaInicio1 != null && !fechaInicio1.equals("")) return true;
		return false;
	}
	
	public boolean hasFechaFin1() {
		if (fechaFin1 != null && !fechaFin1.equals("")) return true;
		return false;
	}
	
	public boolean hasFechaInicio2() {
		if (fechaInicio2 != null && !fechaInicio2.equals("")) return true;
		return false;
	}
	
	public boolean hasFechaFin2() {
		if (fechaFin2 != null && !fechaFin2.equals("")) return true;
		return false;
	}
	
	public boolean hasCliente() {
		if ((clienteNombre != null && !clienteNombre.equals("")) ||
				clienteApellido != null && !clienteApellido.equals("")) return true;
		return false;
	}
	
	public boolean hasPax() {
		if ((paxNombre != null && !paxNombre.equals("")) ||
				paxApellido != null && !paxApellido.equals("")) return true;
		return false;
	}
	
	
	
// ****************************************** getters y setters ***********************************
	
	public Long getNoCotizacion() {
		return noCotizacion;
	}
	public void setNoCotizacion(Long noCotizacion) {
		this.noCotizacion = noCotizacion;
	}
	
	public Long getNoCliente() {
		return noCliente;
	}
	public void setNoCliente(Long noCliente) {
		this.noCliente = noCliente;
	}
	
	public String getNumeroTour() {
		return numeroTour;
	}

	public void setNumeroTour(String numeroTour) {
		this.numeroTour = numeroTour;
	}

	public String getNombreCotizacion() {
		return nombreCotizacion;
	}
	public void setNombreCotizacion(String nombreCotizacion) {
		this.nombreCotizacion = nombreCotizacion;
	}
	
	public String getVendedor() {
		return vendedor;
	}
	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}
	
	public Date getFechaInicio1() {
		return fechaInicio1;
	}
	public void setFechaInicio1(Date fechaInicio1) {
		this.fechaInicio1 = fechaInicio1;
	}
	
	public Date getFechaInicio2() {
		return fechaInicio2;
	}
	public void setFechaInicio2(Date fechaInicio2) {
		this.fechaInicio2 = fechaInicio2;
	}
	
	public Date getFechaFin1() {
		return fechaFin1;
	}
	public void setFechaFin1(Date fechaFin1) {
		this.fechaFin1 = fechaFin1;
	}
	
	public Date getFechaFin2() {
		return fechaFin2;
	}
	public void setFechaFin2(Date fechaFin2) {
		this.fechaFin2 = fechaFin2;
	}
	
	public String getClienteNombre() {
		return clienteNombre;
	}
	public void setClienteNombre(String clienteNombre) {
		this.clienteNombre = clienteNombre;
	}
	
	public String getClienteApellido() {
		return clienteApellido;
	}
	public void setClienteApellido(String clienteApellido) {
		this.clienteApellido = clienteApellido;
	}
	
	public String getPaxNombre() {
		return paxNombre;
	}
	public void setPaxNombre(String paxNombre) {
		this.paxNombre = paxNombre;
	}
	
	public String getPaxApellido() {
		return paxApellido;
	}
	public void setPaxApellido(String paxApellido) {
		this.paxApellido = paxApellido;
	}

	public String getClaseCliente() {
		return claseCliente;
	}
	public void setClaseCliente(String claseCliente) {
		this.claseCliente = claseCliente;
	}
	
	

}
