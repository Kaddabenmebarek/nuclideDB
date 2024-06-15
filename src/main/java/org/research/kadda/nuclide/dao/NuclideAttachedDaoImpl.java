package org.research.kadda.nuclide.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.research.kadda.nuclide.entity.NuclideAttached;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

@Transactional
public class NuclideAttachedDaoImpl implements NuclideAttachedDao {
	
	private final Logger logger = LoggerFactory.getLogger(NuclideAttachedDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideAttached> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from NuclideAttached").setHint(QueryHints.HINT_CACHEABLE, true).list();
	}

	@Override
	public NuclideAttached findById(int nuclideAttachedId) {
		return (NuclideAttached) sessionFactory.getCurrentSession().get(NuclideAttached.class, nuclideAttachedId);
	}

	@Override
	public boolean saveNuclideAttached(NuclideAttached nuclideAttached) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(nuclideAttached);
			logger.info("The attached file " + nuclideAttached.getFileName() + "has been added");
		} catch (InvalidDataAccessResourceUsageException ex) {
			logger.error(ex.getMessage());
		    return false;
	    } 	
		return true;
	}

	@Override
	public void deleteNuclideAttached(NuclideAttached nuclideAttached) {
		sessionFactory.getCurrentSession().remove(nuclideAttached);
		logger.info("The artached file " + nuclideAttached.getFileName() + "has been removed");
	}

	@Override
	public int getLastIndex() {
		Query query = sessionFactory.getCurrentSession().createQuery("select MAX(nuclideAttachedId) from NuclideAttached");
		if(query.getSingleResult() == null) {
			return 0;
		}else {
			return (int) query.getSingleResult();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideAttached> findByTracerId(int nuclideBottleId) {
		Query query = sessionFactory.getCurrentSession()
				.createQuery("from NuclideAttached where nuclideBottle.nuclideBottleId = :nuclideBottleId");
		query.setParameter("nuclideBottleId", nuclideBottleId);
		List<NuclideAttached> result = null;
		try {
			result = (List<NuclideAttached>) query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
		} catch (NoResultException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

}
