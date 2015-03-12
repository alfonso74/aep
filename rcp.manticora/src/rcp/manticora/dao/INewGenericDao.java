package rcp.manticora.dao;

import java.io.Serializable;
import java.util.List;

public interface INewGenericDao<X, ID extends Serializable>  {
	
	public X findById(ID id, boolean lock);
	
	public List<X> findAll();
	
	public List<X> findByExample(X exampleInstance, String... excludeProperty);
	
	public X makePersistent(X entity);
	
	public void makeTransient(X entity);
	
	public void flush();
	
	public void clear();
}
