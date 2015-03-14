package rcp.manticora.controllers;


import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import rcp.manticora.dao.NewGenericDAOImpl;
import rcp.manticora.services.HibernateUtil;

public abstract class AbstractControllerNew<X> {
	private Session session;
	private String editorId;
	private NewGenericDAOImpl<X, Long> dao;
	
	/**
	 * Inicializa una sesión de hibernate en base al editor indicado
	 * @param id que se asociará a la nueva sesión
	 */
	public AbstractControllerNew(String editorId, NewGenericDAOImpl<X, Long> dao) {
		session = HibernateUtil.getEditorSession(editorId);
		session.setFlushMode(FlushMode.MANUAL);
		//session.setFlushMode(FlushMode.NEVER);
		this.editorId = editorId;
		this.dao = dao;
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
		dao.flush();                     // extiende las modificaciones al caché
		session.getTransaction().commit();
		session = null;
		*/
		//dao.doSave2(registro);
		try {
			dao.doSave2(registro);
		} catch (HibernateException he) {
			if (getSession().isOpen()) {
				HibernateUtil.rollback(getSession().getTransaction());
				HibernateUtil.closeEditorSession(getEditorId());
			}
			HibernateUtil.procesarError(he);
			session = HibernateUtil.getEditorSession(editorId);
			session.setFlushMode(FlushMode.MANUAL);
			dao.setSession(session);
		}
	}
	
	/**
	 * Finaliza una sesión de hibernate
	 * @param editorId id asociado a la sesión que será finalizada
	 */
	//TODO:  Reemplazar esto por finalizarSesion()
	public void finalizar(String editorId) {
		System.out.println("Finalizando sesión: " + editorId);
		HibernateUtil.closeEditorSession(editorId);     // graba en la base de datos
	}
	
	/**
	 * Finaliza una sesión de hibernate.
	 */
	public void finalizarSesion() {
		System.out.println("Finalizando sesión: " + getEditorId());
		HibernateUtil.closeEditorSession(getEditorId());     // graba en la base de datos
	}
	
	/**
	 * Retorna el editor que está asociado a este controller
	 * @return id del editor asociado
	 */
	public String getEditorId() {
		return editorId;
	}
	
	/**
	 * Retorna la sesión que ha sida creada para este editor
	 * @return sesión de hibernate
	 */
	public Session getSession() {
		return session;
	}
	
	public NewGenericDAOImpl<X, Long> getDAO() {
		return dao;
	}
	
	
	/**
	 * Permite ver las sesiones activas de Hibernate
	 */
	public void verSesiones() {
		HibernateUtil.verSesiones();
	}
}
