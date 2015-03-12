package rcp.manticora.dao;

import java.util.List;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Producto;
import rcp.manticora.model.TipoHabitacion;

public class TipoHabitacionDAO extends NewGenericDAOImpl<TipoHabitacion, Long> {
	
	public List<TipoHabitacion> findByHotel(Producto hotel) {
		Criterion c = Restrictions.eq("hotel", hotel);
		List<TipoHabitacion> resultados = findByCriteria(c);
		return resultados;
	}
}
