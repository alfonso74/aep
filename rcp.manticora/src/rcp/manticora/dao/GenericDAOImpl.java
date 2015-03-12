package rcp.manticora.dao;


import org.hibernate.Session;

import rcp.manticora.services.HibernateUtil;

// Clase para generalizar el borrado de documentos.  En vez de tener en el 
// controller un método "deleteX" para cada clase que se maneja, generalizamos
// y logramos una sola clase que maneja todas las borradas

public class GenericDAOImpl implements IGenericDAO {

	public GenericDAOImpl() {
		super();
	}
	
	public Session getCurrentSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public void commit() {
		getCurrentSession().getTransaction().commit();
	}
	
	public void close() {
		HibernateUtil.getSessionFactory().close();
	}
	
	public void makePersistent(Object registro) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.saveOrUpdate(registro);
	}
	
	public void makeTransient(Object registro) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.delete(registro);
	}
}
