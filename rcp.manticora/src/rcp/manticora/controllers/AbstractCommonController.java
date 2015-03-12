package rcp.manticora.controllers;


import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import rcp.manticora.dao.NewGenericDAOImpl;
import rcp.manticora.services.HibernateUtil;

/**
 * El controller tiene la responsabilidad de obtener la sesi�n de hibernate, pas�rsela al dao
 * y manejar cualquier error de Hibernate.
 * @author Carlos Alfonso
 *
 * @param <X> Objeto de negocios que compone al dao y que utilizar� este controller.
 */

public abstract class AbstractCommonController<X> {
	private Session session;
	private NewGenericDAOImpl<X, Number> dao;
	
	/**
	 * Inicializa una sesi�n de hibernate en base al editor indicado
	 * @param id que se asociar� a la nueva sesi�n
	 */
	public AbstractCommonController(NewGenericDAOImpl<X, Number> dao) {
		this.dao = dao;
		inicializarSesion();
	}
	
	
	private void inicializarSesion() {
		session = HibernateUtil.currentSession();
		session.setFlushMode(FlushMode.MANUAL);
		this.dao.setSession(session);
	}
	

	/**
	 * Persiste un registro en la base de datos
	 * @param registro registro a ser guardado, instancia tipo X
	 */
	public void doSave(X registro) {
		/*
		session.beginTransaction();
		dao.makePersistent(registro);
		dao.flush();                     // extiende las modificaciones al cach�
		session.getTransaction().commit();
		*/
		try {
			inicializarSesion();
			dao.doSave2(registro);
			finalizarSesion();
		} catch (HibernateException he) {
			if (getSession().isOpen()) {
				HibernateUtil.rollback(getSession().getTransaction());
				HibernateUtil.closeSession();
			}
			HibernateUtil.procesarError(he);
		}
	}
	
	
	public void doDelete(X registro) {
		try {
			inicializarSesion();
			
			session.beginTransaction();
			dao.makeTransient(registro);
			dao.flush();
			session.getTransaction().commit();
			
			//dao.doDelete(registro);
			finalizarSesion();
		} catch (HibernateException he) {
			if (getSession().isOpen()) {
				HibernateUtil.rollback(getSession().getTransaction());
				HibernateUtil.closeSession();
			}
			HibernateUtil.procesarError(he);
		}
	}
	
	/**
	 * Finaliza una sesi�n de hibernate
	 * 
	 */
	public void finalizarSesion() {
		System.out.println("Finalizando sesi�n: " + session);
		HibernateUtil.closeSession();     // graba en la base de datos
	}

	
	/**
	 * Retorna la sesi�n que ha sida creada para este editor
	 * @return sesi�n de hibernate
	 */
	public Session getSession() {
		return session;
	}
	
	public NewGenericDAOImpl<X, Number> getDAO() {
		return dao;
	}
	
	
	/**
	 * Permite ver las sesiones activas de Hibernate
	 */
	public void verSesiones() {
		System.out.println("Listando sesiones: ");
		HibernateUtil.verSesiones();
	}
}
