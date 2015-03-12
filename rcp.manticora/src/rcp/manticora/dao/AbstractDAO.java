package rcp.manticora.dao;


import org.hibernate.Session;

import rcp.manticora.services.HibernateUtil;

public class AbstractDAO implements IGenericDAO {

	public AbstractDAO() {
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
