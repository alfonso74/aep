package rcp.manticora.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import rcp.manticora.dao.ClienteDAO;
import rcp.manticora.model.ICliente;
import rcp.manticora.services.HibernateUtil;


public class ClientesController extends AbstractControllerNew<ICliente> {
	
	public ClientesController(String editorId) {
		super(editorId, new ClienteDAO());
	}
	
	public List buscarClientesNaturales(String buscarPor, String cadena) {
		return ((ClienteDAO) getDAO()).buscarClienteNatural(buscarPor, cadena);
	}
	
	public List buscarClientesJuridicos(String buscarPor, String cadena) {
		return ((ClienteDAO) getDAO()).buscarClienteJuridico(buscarPor, cadena);
	}
	
	
	public int verificarClientePax(String nombre, String apellido) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("from Pax where nombre = ? and apellido = ?")
			.setString(0, nombre)
			.setString(1, apellido);
		List paxs = q.list();
		int noPaxs = paxs.size();
		session.getTransaction().commit();
		return noPaxs;
	}
}
