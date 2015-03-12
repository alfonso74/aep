package rcp.manticora.dao;

import java.util.List;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Producto;

public class ProductoDAO extends NewGenericDAOImpl<Producto, Long> {
	
	public List<Producto> findProductosByTipoReserva(String tipoReserva) {
		Criterion c = Restrictions.eq("tipoReserva", tipoReserva);
		List<Producto> resultados = findByOrderedCriteria("descripcion", true, c); 
		return resultados;
	}
	
	public List<Producto> findAllAscending() {
		return findAllOrdered("descripcion", true);
	}
	
}
