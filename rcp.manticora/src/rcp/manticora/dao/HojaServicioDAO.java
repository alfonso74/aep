package rcp.manticora.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.IHojaServicio;

public class HojaServicioDAO extends NewGenericDAOImpl<IHojaServicio, Long> {

	public HojaServicioDAO() {
	}
	
	public List<IHojaServicio> findByStatus(String status) {
		Criterion c = Restrictions.eq("estado", status);
		List<IHojaServicio> resultados = findByCriteria(c);
		return resultados;
	}
  	
	public List<IHojaServicio> findHojasVentasByStatus(String status) {
		Criterion c01 = Restrictions.eq("estado", status);
		Criterion c02 = Restrictions.eq("clase", "V");
		List<IHojaServicio> resultados = findByCriteria(c01, c02);
		return resultados;
	}
	
	public List<IHojaServicio> findHojasOpsByStatus(String status) {
		Criterion c01 = Restrictions.eq("estado", status);
		Criterion c02 = Restrictions.eq("clase", "T");
		List<IHojaServicio> resultados = findByCriteria(c01, c02);
		return resultados;
	}
	
	
	/*
	public HojaServicio[] retrieveHojas() {
		Session session = getCurrentSession();
		session.beginTransaction();
		Collection result = session.createQuery("from HojaServicio").list();
		commit();
		return (HojaServicio[]) result.toArray(new HojaServicio[result.size()]);
	}
	
// Obtiene registros en base a la clase (H = hoja de servicios, T = tour)
	public HojaServicio[] retrieveHojas(String clase) {
		Session session = getCurrentSession();
		session.beginTransaction();
		Collection result = session.createQuery("from HojaServicio where clase = ?")
			.setString(0, clase)
			.list();
		commit();
		return (HojaServicio[]) result.toArray(new HojaServicio[result.size()]);
	}
	*/

}
