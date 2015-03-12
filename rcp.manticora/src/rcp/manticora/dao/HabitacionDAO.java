package rcp.manticora.dao;

import java.util.List;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Habitacion;
import rcp.manticora.model.Producto;

public class HabitacionDAO extends NewGenericDAOImpl<Habitacion, Long> {
	
	public List<Habitacion> findByStatus(String status) {
		Criterion c = Restrictions.eq("estado", status);
		List<Habitacion> resultados = findByCriteria(c);
		return resultados;
	}
	
	public List<Habitacion> findByHotel(Producto hotel) {
		//List<Criterion> criterions = new ArrayList<Criterion>();
		Criterion[] criterions = new Criterion[2];
		Criterion c01 = Restrictions.eq("hotel", hotel);
		Criterion c02 = Restrictions.eq("estado", "A");
		//criterions.add(c01);
		//criterions.add(c02);
		criterions[0] = c01;
		criterions[1] = c02;
		List<Habitacion> resultados = findByCriteria(criterions);
		return resultados;
	}
	
}
