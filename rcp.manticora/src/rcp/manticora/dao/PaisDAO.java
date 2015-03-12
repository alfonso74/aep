package rcp.manticora.dao;

import java.util.List;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Pais;

public class PaisDAO extends NewGenericDAOImpl<Pais, Long> {
	
	public List<Pais> findByStatus(String status) {
		Criterion c = Restrictions.eq("estado", status);
		List<Pais> resultados = findByOrderedCriteria("descripcion", true, c);
		return resultados;
	}
	
	public List<Pais> findAllAscending() {
		return findAllOrdered("descripcion", true);
	}

}
