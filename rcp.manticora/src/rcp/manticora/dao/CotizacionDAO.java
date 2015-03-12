package rcp.manticora.dao;

import java.util.List;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Cotizacion;

public class CotizacionDAO extends NewGenericDAOImpl<Cotizacion, Long> {
	
	public List<Cotizacion> findByStatus(String status) {
		Criterion c = Restrictions.eq("estado", status);
		List<Cotizacion> resultados = findByCriteria(c);
		return resultados;
	}
	
	
	public List<Cotizacion> findRegistradas() {
		Criterion c = Restrictions.isNotNull("fechaContabilidad");
		List<Cotizacion> resultados = findByCriteria(c);
		return resultados;
	}
	
	/*
	public CotizacionDAO() {
	}
	
	public Cotizacion[] retrieveCotizaciones() {
		Session session = getCurrentSession();
		session.beginTransaction();
		Collection result = session.createQuery("from Cotizacion").list();
		commit();
		return ((Cotizacion[]) result.toArray(new Cotizacion[result.size()]));
	}
	
	public Cotizacion[] retrieveCotizaciones(String status) {
		Session session = getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from Cotizacion where estado = ?")
			.setString(0, status)
			.list();
		commit();
		return ((Cotizacion[]) result.toArray(new Cotizacion[result.size()]));
	}
	*/

}
