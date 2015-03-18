package rcp.manticora.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.TipoCliente;

public class TipoClienteDAO extends NewGenericDAOImpl<TipoCliente, Long> {
	
	public List<TipoCliente> findByStatus(String status) {
		Criterion c = Restrictions.eq("estado", status);
		List<TipoCliente> resultados = findByOrderedCriteria("descripcion", true, c);
		return resultados;
	}
	
}
