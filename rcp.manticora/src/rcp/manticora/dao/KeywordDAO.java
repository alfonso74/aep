package rcp.manticora.dao;

import java.util.List;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Keyword;

public class KeywordDAO extends NewGenericDAOImpl<Keyword, Long> {

	@SuppressWarnings("unchecked")
	public List<Keyword> findByStatus(String tipoKeyword) {
		Criterion criterion = Restrictions.like("tipo", tipoKeyword);
		List<Keyword> resultados = findByCriteria(criterion); 
		return resultados;
	}

}
