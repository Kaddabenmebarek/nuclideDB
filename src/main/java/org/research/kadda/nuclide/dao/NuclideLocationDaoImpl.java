package org.research.kadda.nuclide.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.research.kadda.nuclide.entity.NuclideLocation;
import org.research.kadda.nuclide.entity.NuclideLocationAllowance;
import org.research.kadda.nuclide.models.LocationAllowance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

@Transactional
public class NuclideLocationDaoImpl implements NuclideLocationDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private final Logger logger = LoggerFactory.getLogger(NuclideLocationDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideLocation> findAll() {
		Query query = sessionFactory.getCurrentSession().createQuery("from NuclideLocation where isEnable = :isEnable");
		query.setParameter("isEnable", 'Y');
		return query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
		//return sessionFactory.getCurrentSession().createQuery("from NuclideLocation").list();
	}

	@Override
	public Double getAllowance(String nuclideName, String location) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("select nuclideAllowance.maxAmount from NuclideLocationAllowance where nuclideLocation.location = :location and nuclideAllowance.nuclide.nuclideName = :nuclideName");
		query.setParameter("nuclideName", nuclideName);
		query.setParameter("location", location);
		
		Double res = null;
		try {
			res = (Double) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error(e.getMessage());
		}

		if(res == null){
			return (double) 0;
		}
		
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<LocationAllowance> findAllLocationAllowances() {
		List<LocationAllowance> locationAllowanceList = new ArrayList<LocationAllowance>();
		Query query = sessionFactory.getCurrentSession().createQuery("from NuclideLocationAllowance where nuclideLocation.isEnable = :isEnable");
		query.setParameter("isEnable", 'Y');
		List<NuclideLocationAllowance> results = ((org.hibernate.query.Query) query).setHint(QueryHints.HINT_CACHEABLE, true).list();
		for(NuclideLocationAllowance res : results) {
			LocationAllowance locationAllowance = new LocationAllowance();
			locationAllowance.setNuclide(res.getNuclideAllowance().getNuclide().getNuclideName());
			locationAllowance.setActivityThreshold(res.getNuclideAllowance().getMaxAmount());
			locationAllowance.setLocation(res.getNuclideLocation().getLocation());
			locationAllowanceList.add(locationAllowance);
		}
		
		return locationAllowanceList;
	}

}
