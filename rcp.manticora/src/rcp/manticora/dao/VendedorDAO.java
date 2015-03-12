package rcp.manticora.dao;

import java.util.List;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Vendedor;

public class VendedorDAO extends NewGenericDAOImpl<Vendedor, Long> {

	public VendedorDAO() {
	}
	/*
	public Vendedor[] retrieveVendedores() {
		Session session = getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from Vendedor").list();
		commit();
		return (Vendedor[]) result.toArray(new Vendedor[result.size()]);
	}
	*/
	
	/**
	 * Buscamos los vendedores activos en orden ascendente por nombre
	 */
	public List<Vendedor> findByStatus(String status) {
		Criterion c = Restrictions.eq("estado", status);
		List<Vendedor> resultados = findByOrderedCriteria("nombre", true, c);
		return resultados;
	}
	
	public List<Vendedor> findAllAscending() {
		return findAllOrdered("nombre", true);
	}
	
}
