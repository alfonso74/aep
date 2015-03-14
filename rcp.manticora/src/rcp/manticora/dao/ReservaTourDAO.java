package rcp.manticora.dao;

import rcp.manticora.model.ReservaTour;

public class ReservaTourDAO extends NewGenericDAOImpl<ReservaTour, Long> {
/*
	public ReservaTourDAO() {
	}
	*/
	/*
	public ReservaTour[] retrieveTours() {
		Session session = getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from ReservaTour").list();
		commit();
		return (ReservaTour[]) result.toArray(new ReservaTour[result.size()]);
	}

	public ReservaTour retrieveTour(int idReserva) {
		Session session = getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from ReservaTour where idReserva = ?")
			.setInteger(0, idReserva)
			.list();
		commit();
		if (result.size() > 0) {
			return (ReservaTour) result.get(0);
		} else {
			System.out.println("No se encontró el tour " + idReserva);
			return null;
		}
	}
	*/
}
