package rcp.manticora.dao;

import rcp.manticora.model.Template;

public class TemplateDAO extends NewGenericDAOImpl<Template, Long> {
	
	/*
	public TemplateDAO() {
	}
	
	public Template[] retrieveTemplates() {
		Session session = getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from Template").list();
		commit();
		return (Template[]) result.toArray(new Template[result.size()]);
	}
	
	public Template retrieveTemplate(String codigoTemplate) {
		Session session = getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from Template where idTemplate = ?")
			.setString(0, codigoTemplate)
			.list();
		commit();
		return (Template) result.get(0);
	}
	*/
}
