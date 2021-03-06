package spet.sbwo.data.access;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spet.sbwo.data.DatabaseError;
import spet.sbwo.data.DatabaseException;

class DatabaseExecutor implements IDatabaseExecutor {
	static final Logger LOG = LoggerFactory.getLogger(DatabaseExecutor.class);

	protected final EntityManager em;
	protected EntityTransaction tr;

	DatabaseExecutor(EntityManager em) throws DatabaseException {
		this.em = em;
		this.tr = null;
		this.start();
	}

	@Override
	public <T> void update(T entity) throws DatabaseException {
		try {
			em.merge(entity);
		} catch (Exception e) {
			LOG.warn("Database error while executing an update.");
			throw new DatabaseException(e);
		}
	}

	@Override
	public <T> void delete(T entity) throws DatabaseException {
		try {
			em.remove(entity);
		} catch (Exception e) {
			LOG.warn("Database error while executing a delete.");
			throw new DatabaseException(e);
		}
	}

	@Override
	public <T> void create(T entity) throws DatabaseException {
		try {
			em.persist(entity);
		} catch (Exception e) {
			LOG.warn("Database error while executing a create.");
			throw new DatabaseException(e);
		}
	}

	@Override
	public void start() throws DatabaseException {
		if (this.tr == null) {
			this.tr = em.getTransaction();
			this.tr.begin();
		} else {
			throw new DatabaseException(DatabaseError.OTHER, "Transaction already started.");
		}
	}

	@Override
	public void commit() throws DatabaseException {
		this.commit(false);
	}

	@Override
	public void commit(boolean start) throws DatabaseException {
		if (this.tr != null) {
			try {
				this.tr.commit();
			} catch (RollbackException e) {
				LOG.warn("Transaction commit has failed.");
				throw new DatabaseException(e);
			}
			this.tr = null;
		}
		if (start) {
			this.start();
		}
	}

	@Override
	public void rollback() throws DatabaseException {
		this.rollback(false);
	}

	@Override
	public void rollback(boolean start) throws DatabaseException {
		if (this.tr != null) {
			this.tr.rollback();
		} else {
			throw new DatabaseException(DatabaseError.OTHER, "Attempted to rolback without a transaction.");
		}
		this.tr = null;
		if (start) {
			this.start();
		}
	}

	@Override
	public void close() {
		try {
			if (this.tr != null) {
				this.tr.rollback();
			}
			if (this.em != null) {
				this.em.close();
			}
		} catch (Exception e) {
			LOG.error("Error while closing executor.", e);
		}
	}

	@Override
	public <T> T find(Class<T> clazz, Object id) throws DatabaseException {
		try {
			return em.find(clazz, id);
		} catch (Exception e) {
			LOG.error("Unable to find a entity ({}, {}).", clazz.getSimpleName(), id);
			throw new DatabaseException(e);
		}
	}

	@Override
	public <T> List<T> queryList(String name, Class<T> resultType, Object... params) throws DatabaseException {
		try {
			TypedQuery<T> query = em.createNamedQuery(name, resultType);
			for (int i = 0; i < params.length; ++i) {
				query.setParameter(i + 1, params[i]);
			}
			return query.getResultList();
		} catch (Exception e) {
			LOG.error("Unable to run named query {}.", name);
			throw new DatabaseException(e);
		}
	}

	@Override
	public <T> Optional<T> querySingle(String name, Class<T> resultType, Object... params) throws DatabaseException {
		return queryList(name, resultType, params).stream().findFirst();
	}

}
