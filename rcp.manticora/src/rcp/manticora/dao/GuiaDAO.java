package rcp.manticora.dao;

import rcp.manticora.model.Guia;

public class GuiaDAO extends NewGenericDAOImpl<Guia, Long> {
	
	public int getTotalGuias() {
		int n = findAll().size();
		return n;
	}
}
