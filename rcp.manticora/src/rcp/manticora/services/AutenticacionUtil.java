package rcp.manticora.services;

import java.util.Set;

import org.hibernate.Session;

import rcp.manticora.dialogs.ConnectionData;
import rcp.manticora.model.Rol;
import rcp.manticora.model.Usuario;


public class AutenticacionUtil {
	private static Usuario usuario;
	
	public static Usuario getUsuario() {
		return usuario;
	}
	
	public static Set<Rol> getRoles() {
		if (usuario != null) {
			return usuario.getListaRoles();
		} else {
			return null;
		}
	}
	
	public static boolean verificarUsuario(ConnectionData datos) {
		boolean resultado = false;
		Session session;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();


			/*
		Usuario u = (Usuario) session.createQuery("from Usuario u where u.userName = ?")
			.setString(0, username).uniqueResult();
			 */
			Usuario u = (Usuario) session.createQuery("from Usuario u " + 
			"left join fetch u.listaRoles where u.userName = ?")  // para hacer un solo query con los roles incluidos
			.setString(0, datos.getUsuario()).uniqueResult();
			//List<Usuario> x = (List<Usuario>) session.createQuery("from Usuario").list();
			if (u != null) {
				System.out.println("Usuario: " + u);
				System.out.println("Roles: " + u.getListaRoles());
				String passwordMD5 = encodePasswordMD5(datos.getPassword());
				if (passwordMD5.equals(u.getPassword())) {
					System.out.println("Password aceptado");
					usuario = u;
					resultado = true;
				} else {
					System.err.println("El password suministrado no coincide...");
				}
			}
			// la sesión puede haber sido cerrada por el encodePasswordMD5(), así que verificamos
			if (session.isOpen()) {
				session.getTransaction().commit();
			}
		} catch (Exception e) {
			System.err.println("Exception al autenticar: " + e.toString());
			System.err.println("Stack trace: " + e.getStackTrace()[0] + "\n" + e.getStackTrace()[1]);
		}
		return resultado;
	}
	
	public static String encodePasswordMD5(String password) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		//String md5Password = (String) session.createQuery("select md5(" + password + ") from dual").uniqueResult();
		String md5Password = (String) session.createSQLQuery("select cast(md5('" + password + "') as char) as password from dual").uniqueResult();		
		System.out.println("PassMD5: " + md5Password);
		session.getTransaction().commit();
		return md5Password;
	}
	
}
