package rcp.manticora.model;

import rcp.manticora.services.HibernateUtil;

public class HibernateModel {

	public HibernateModel() {
	}
	
	public void open() {
		System.out.println("Inicializando hibernate (HibernateModel)...");
		try {
			Class.forName("rcp.manticora.services.HibernateUtil");
		} catch (ClassNotFoundException e) {
			System.out.println("No se encontró la clase HibernateUtil");
			e.printStackTrace();
		}
	}
	
	public void commit() {
		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
	}

	
	public void close() {
		HibernateUtil.getSessionFactory().close();
		System.out.println("Sesión de Hibernate cerrada");
	}
}
