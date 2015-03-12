package rcp.manticora.dao;

import java.util.List;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Solicitud;

public class SolicitudDAO extends NewGenericDAOImpl<Solicitud, Long> {
	
	public List<Solicitud> findByStatus(String status) {
		Criterion c = Restrictions.eq("estado", status);
		List<Solicitud> resultados = findByCriteria(c);
		return resultados;
	}
	
}
