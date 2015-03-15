package rcp.manticora.dao;

import java.util.List;



import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Keyword;
import rcp.manticora.model.TipoKeyword;

public class KeywordDAO extends NewGenericDAOImpl<Keyword, Long> {

	public List<Keyword> findByTipo(TipoKeyword tipoKeyword) {
		Criterion criterion = Restrictions.like("tipo", tipoKeyword.getDescripcion());
		List<Keyword> resultados = findByCriteria(criterion); 
		return resultados;
	}

}
