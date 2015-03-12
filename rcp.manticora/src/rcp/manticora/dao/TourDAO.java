package rcp.manticora.dao;

import rcp.manticora.model.Tour;

public class TourDAO extends NewGenericDAOImpl<Tour, Long> {
	
	/*
	public TourDAO() {
	}
	
	public Tour[] retrieveTours() {
		Session session = getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from Tour").list();
		commit();
		return (Tour[]) result.toArray(new Tour[result.size()]);
	}
	*/
}
