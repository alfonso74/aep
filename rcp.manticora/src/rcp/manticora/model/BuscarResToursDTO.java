package rcp.manticora.model;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.services.HibernateUtil;

public class BuscarResToursDTO {
	
	private String tipo = null;
	private String producto = null;
	private Date fechaDesde = null;
	private Date fechaHasta = null;
	private boolean reservas = false;
	
	
	/**
	 * Busca disponibilidades de tours en base a los parámetros suministrados a la clase.
	 * @return Array de objetos DisponibilidadTour
	 */
	public DisponibilidadTour[] buscarReservasTours() {
		List<DisponibilidadTour> resultados = buscarReservasToursAsList();
		return (DisponibilidadTour[]) resultados.toArray(new DisponibilidadTour[resultados.size()]);
	}
	
	/**
	 * Busca disponibilidades de tours en base a los parámetros suministrados a la clase.
	 * @return Lista de objetos DisponibilidadTour
	 */
	@SuppressWarnings("unchecked")
	public List<DisponibilidadTour> buscarReservasToursAsList() {
		String pTipo = this.getTipo();
		String pProducto = this.getProducto();
		Date pFechaDesde = this.getFechaDesde();
		Date pFechaHasta = this.getFechaHasta();
		boolean pReservas = this.hasReservas();
		
		System.out.println("Tipo: " + pTipo);
		System.out.println("Prod: " + pProducto);
		System.out.println("Desde: " + pFechaDesde);
		System.out.println("Hasta: " + pFechaHasta);
		System.out.println("Res: " + pReservas);
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(DisponibilidadTour.class);
		if (pTipo != null) {
			Criteria critTour = criteria.createCriteria("tour");
			critTour.add(Restrictions.eq("dspTipoProducto", pTipo));
			if (pProducto != null && !pProducto.equals("Todos")) {
				critTour.add(Restrictions.eq("dspProducto", pProducto));
			}
		} else {
			if (pProducto != null) {
				criteria.createCriteria("tour")
					.add(Restrictions.eq("dspProducto", pProducto));
			}
		}
		if (pFechaDesde != null) {
			if (pFechaHasta != null) {
				criteria.add(Restrictions.between("fecha", pFechaDesde, pFechaHasta));
			} else {
				criteria.add(Restrictions.gt("fecha", pFechaDesde));
			}
		}
		
		if (hasReservas()) {
			criteria.createCriteria("hoja");   // solo presenta aquellas fechas que tienen reserva
		}
		criteria.addOrder(Order.asc("fecha"));
		criteria.setFetchMode("tour", FetchMode.JOIN);
		criteria.setFetchMode("hoja", FetchMode.JOIN);
		
		List<DisponibilidadTour> resultados = criteria.list();
		session.getTransaction().commit();
		
		return resultados;
	}
	
	
	
// ****************************************** getters y setters ***********************************
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		if (tipo == null || tipo.equals("")) {
			this.tipo = null;
		} else {
			this.tipo = tipo;
		}
		this.tipo = tipo.equals("") ? null : tipo;
	}
	
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto == null || producto.equals("") ? null : producto;
	}
	
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde == null || fechaDesde.equals("") ? null : fechaDesde;
	}
	
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta == null || fechaHasta.equals("") ? null : fechaHasta;
	}
	
	public boolean hasReservas() {
		return reservas;
	}
	public void setReservas(boolean reservas) {
		this.reservas = reservas;
	}
	
}
