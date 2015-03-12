package rcp.manticora.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Rol;


public class RolDAO extends NewGenericDAOImpl<Rol, Long> {
	
	/**
	 * @return Listado de roles en orden ascendente
	 */
	public List<Rol> findAllAscending() {
		return findAllOrdered("descripcion", true);
	}
	
	/**
	 * @param status Status de los registros a retornar
	 * @return Lista de roles con el status indicado
	 */
	public List<Rol> findByStatus(String status) {
		Criterion c = Restrictions.eq("estado", status);
		List<Rol> resultados = findByOrderedCriteria("descripcion", true, c);
		return resultados;
	}

}
