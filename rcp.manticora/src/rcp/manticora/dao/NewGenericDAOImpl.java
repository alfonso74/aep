package rcp.manticora.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;

import rcp.manticora.services.HibernateUtil;

public abstract class NewGenericDAOImpl<T, ID extends Serializable> 
	implements INewGenericDao<T, ID> {
	
	private Class<T> persistentClass;
	private Session session;
	
	public NewGenericDAOImpl() {
		persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public void setSession(Session s) {
		session = s;
	}
	
	protected Session getSession() {
		if (session == null) {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			//session = HibernateUtil.getSessionFactory().openSession();
		}
		return session;
	}
	
	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
	/**
	 * Con el ... es opcional poner el argumento, nice!!
	 * @param criterion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}
	
	/**
	 * Retorna resultados ordenados de acuerdo al campo y al orden indicado
	 * @param field Campo usado para ordenar
	 * @param ascending Dirección de ordenamiento (true: ascendente)
	 * @param criterion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByOrderedCriteria(String field, boolean ascending, Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		if (ascending) {
			crit.addOrder(Order.asc(field));
		} else {
			crit.addOrder(Order.desc(field));
		}
		return crit.list();
	}
	
	
// **************** Métodos heredados de la interface *****************
	

	public List<T> findAll() {
		return findByCriteria();
	}
	
	/**
	 * Retorna una lista ordenada con los registros encontrados
	 * @param field Campo a utilizar para el ordenamiento
	 * @param ascending realiza un ordenamiento ascendente (true), si es false es descendente.
	 * @return Lista ordenada de acuerdo al campo ascending
	 */
	public List<T> findAllOrdered(String field, boolean ascending) {
		return findByOrderedCriteria(field, ascending);
	}
	
	@SuppressWarnings("unchecked")
	public T findById(ID id, boolean lock) {
		T entity;
		if (lock) {
			entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
		} else {
			entity = (T) getSession().load(getPersistentClass(), id);
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public T findById2(ID id, boolean lock) {
		T entity;
		if (lock) {
			entity = (T) getSession().get(getPersistentClass(), id, LockMode.UPGRADE);
		} else {
			entity = (T) getSession().get(getPersistentClass(), id);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String... excludeProperty) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		return crit.list();
	}

	public T makePersistent(T entity) {
		//getSession().beginTransaction();
		getSession().saveOrUpdate(entity);
		//getSession().getTransaction().commit();
		return entity;
	}

	public void makeTransient(T entity) {
		getSession().delete(entity);
	}
	
	
	public void doSave2(T entity) {
		getSession().beginTransaction();
		makePersistent(entity);
		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	
	public void flush() {
		getSession().flush();	
	}
	
	public void clear() {
		getSession().clear();
	}
	
// ****************** Agregado por yo!! para pruebas *******************
	
	// TODO: Método temporal.  Si lo dejo, quitar el try-catch, ya que esto debe manejarlo el controller.
	public void doDelete(T entity) {
		try {
			getSession().beginTransaction();
			makeTransient(entity);
			getSession().flush();
			getSession().getTransaction().commit();
		} catch (HibernateException he) {
			if (getSession().isOpen()) {
				HibernateUtil.rollback(getSession().getTransaction());
				getSession().close();
			}
			HibernateUtil.procesarError(he);
		}
	}
	
	/**
	 * Agregado para pruebas
	 */
	public void commit() {
		getSession().getTransaction().commit();
	}
	
	/**
	 * Agregados para pruebas
	 *
	 */
	public void isOpen() {
		System.out.println("Session open (DAO): " + getSession().isOpen());
	}
}
