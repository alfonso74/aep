package rcp.manticora.services;

import java.util.HashMap;


import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import rcp.manticora.Application;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory;
	private static Shell shell;

	static {
		try {
			// obtenemos las preferencias de conexión (servidor y base de datos)
			IPreferencesService service = Platform.getPreferencesService();
			String servidor = service.getString(Application.PLUGIN_ID, "servidor", "servidorX", null);
			String schema = service.getString(Application.PLUGIN_ID, "schema", "testX", null);
			servidor = service.getString(Application.PLUGIN_ID, "servidor", "error", null);
			schema = service.getString(Application.PLUGIN_ID, "schema", "test2", null);
			//servidor = "localhost";
			//schema = "test2";
			if (servidor.equals("error")) {
				servidor = "localhostx";
				schema = "test2x";
			}
			String url = "jdbc:mysql://" + servidor + ":3306/" + schema;
			System.out.println("URL de conexión (HibernateUtil.java): " + url);
			// inicializamos hibernate via xml (hibernate.cfg.xml) y seteamos las propiedades
			// obtenidas del preference store de Eclipse
			Configuration config = new Configuration().configure();
			config.setProperty("hibernate.connection.url", url);
			sessionFactory = config.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Error durante inicio de sesión: " + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
// ********************* Session per editor ******************************
	public static final HashMap<String, Session> sessionMap = new HashMap<String, Session>();
	
	
	/**
	 * Permite obtener la sesión de hibernate asociada a un editorID.  Si el editor
	 * no tiene una sesión asociada se crea una nueva sesión.
	 * @param editor ID del editor
	 * @return sesión existente que está asociada al editor, o una nueva sesión
	 * @throws HibernateException
	 */
	public static Session getEditorSession(String editor) throws HibernateException {
		if (editor == null) {
			System.err.println("Error:  el valor del editor es NULL (HibernateUtil)");
		}
		Session s = (Session) sessionMap.get(editor);
		if (s == null) {
			s = sessionFactory.openSession();
			sessionMap.put(editor, s);
		}
		return s;
	}
	
	public static void closeEditorSession(String editor) throws HibernateException {
		Session s = (Session) sessionMap.get(editor);
		if (s != null)
			s.close();
		sessionMap.remove(editor);
	}
	
	public static void verSesiones() {
		for (String s : sessionMap.keySet()) {
			System.out.println("Sesión: " + s);
		}
	}
	
	
// *********************Inicio del código copiado de la web **********************
	
	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();
	
	/**
	 * Obtiene la sesión asociada a este thread, y si no existe se crea.
	 * Debe usarse en conjunto con closeSession().
	 * @return Sesión de Hibernate
	 * @throws HibernateException
	 */
	public static Session currentSession() throws HibernateException {
		Session s = (Session) session.get();
		// Open a new Session, if this thread has none yet
		if (s == null) {
			s = sessionFactory.openSession();
			// Store it in the ThreadLocal variable
			session.set(s);
		}
		return s;
	}

	public static void closeSession() throws HibernateException {
		Session s = (Session) session.get();
		if (s != null)
			s.close();
		session.set(null);
	}
	
	/**
	 * This is a simple method to reduce the amount of code that needs
	 * to be written every time hibernate is used.
	 */
	public static void rollback(org.hibernate.Transaction tx) {
		if (tx != null) {
			try {
				tx.rollback( );
			} catch (HibernateException ex) {
				// Probably don't need to do anything -	this is likely being
				// called because of another exception, and we don't want to
				// mask it with yet another exception.
			}
		}
	}
	/**
	 * This is a simple method to reduce the amount of code that needs
	 * to be written every time hibernate is used.
	 */
	public static void commit(org.hibernate.Transaction tx) {
		if (tx != null) {
			try {
				tx.commit( );
			} catch (HibernateException ex) {
				// Probably don't need to do anything -	this is likely being
				// called because of another exception,	and we don't want to
				// mask it with yet another exception.
			}
		}
	}
	
	
	public static void setShell(Shell eclipseShell) {
		shell = eclipseShell;
	}
	
	
	public static void procesarError(HibernateException he) {
		MessageDialog.openError(shell, "Error de Hibernate", "Error: " + he.toString() + "\n\nStack trace: " + he.getStackTrace()[0] + "\n" + he.getStackTrace()[1]);
		he.printStackTrace();
	}

}
