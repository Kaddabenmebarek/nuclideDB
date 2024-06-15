package org.research.kadda.nuclide.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.jpa.QueryHints;
import org.research.kadda.nuclide.entity.Nuclide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

@Transactional
public class NuclideDaoImpl implements NuclideDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private final Logger logger = LoggerFactory.getLogger(NuclideDaoImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<Nuclide> findAll() {
		List<Nuclide> result = new ArrayList<Nuclide>();
		try {
			result = sessionFactory.getCurrentSession().createQuery("from Nuclide").setHint(QueryHints.HINT_CACHEABLE, true).list();
			Collections.sort(result, new Comparator<Nuclide>() {
				@Override
				public int compare(Nuclide n1, Nuclide n2) {
					return n1.getNuclideName().compareTo(n2.getNuclideName());
				}
			});

		} catch (HibernateException e) {
			if (sessionFactory.getCurrentSession().getTransaction() != null) {
				try {
					sessionFactory.getCurrentSession().getTransaction().rollback();
				} catch (Exception ex) {
					System.err.println("Error when trying to rollback transaction:");
					ex.printStackTrace();
				}
			}
			System.err.println("Original error when executing query:");
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Nuclide> getNuclideList() {
		List<Nuclide> nuclides = sessionFactory.getCurrentSession().createQuery("from Nuclide").setHint(QueryHints.HINT_CACHEABLE, true).list();
//		Query query = sessionFactory.getCurrentSession().createNativeQuery("SELECT * FROM OSIRIS.NUCLIDE");
//		List<Object[]> results = query.getResultList();
//		for(Object[] result : results) {
//			Nuclide nuclide = new Nuclide((String) result[0], ((Number)result[1]).doubleValue(), ((Number)result[2]).doubleValue(), (BigDecimal)result[3]);
//			nuclides.add(nuclide);
//		}
		return nuclides;
	}	

	@Override
	public Nuclide findByName(String nuclideName) {
		return (Nuclide) sessionFactory.getCurrentSession().get(Nuclide.class, nuclideName);
	}

	@Override
	public boolean saveNuclide(Nuclide nuclide) {
		try {			
			sessionFactory.getCurrentSession().saveOrUpdate(nuclide);
			logger.info("The nuclide " + nuclide.getNuclideName() + "has been saved");
			return true;
		} catch (SQLGrammarException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,String> findAllUserId() {
		Map<String,String> userIdMap = new TreeMap<String,String>();
		Query query = sessionFactory.getCurrentSession().createNativeQuery("select first_name ||' '|| last_name ||' - '|| user_name, user_name ||'*'|| first_name ||'~'|| last_name, last_name from osiris.employee order by last_name");
		List<Object[]> results = query.getResultList();
		for(Object[] result : results) {
			userIdMap.put(((String) result[0]).toUpperCase(), (String) result[1]);
		}
		return userIdMap;
	}
	
	
	@Override
	public void deleteNuclide(Nuclide nuclide) {
		sessionFactory.getCurrentSession().remove(nuclide);
		logger.info("The nuclide " + nuclide.getNuclideName() + "has been removed");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Set<String>> getTracersIntoDiscardedWasteLocation(char liquidState) {
		Map<String, Set<String>> tracersIntoDiscardedWasteLocation = new HashMap<String, Set<String>>();
		StringBuilder sb = new StringBuilder("SELECT b.nuclide_bottle_id, w.disposal_route, ");
		if(liquidState == 'Y') {			
			sb.append("u.liquid_waste_percentage");
		}else {
			sb.append("u.solid_waste_percentage");
		}
		sb.append(" FROM osiris.nuclide_usage u, osiris.nuclide_bottle b, osiris.nuclide_waste w");
		sb.append(" WHERE u.nuclide_bottle_id = b.nuclide_bottle_id");
		if(liquidState == 'Y') {			
			sb.append(" AND u.liquid_waste_id = w.nuclide_waste_id");
			sb.append(" AND u.liquid_waste_id in (select nuclide_waste_id from nuclide_waste where disposal_route is not null and is_liquid = 'Y')");
		}else {
			sb.append(" AND u.solid_waste_id = w.nuclide_waste_id");
			sb.append(" AND u.solid_waste_id in (select nuclide_waste_id from nuclide_waste where disposal_route is not null and is_liquid = 'N')");
		}
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		List<Object[]> results = query.getResultList();
		for(Object[] result : results) {
			String tracerId = ((BigDecimal) result[0]).toString();
			if(tracersIntoDiscardedWasteLocation.get(tracerId) == null) {
				Set<String> locations = new HashSet<String>();
				locations.add((String) result[1]);
				tracersIntoDiscardedWasteLocation.put(tracerId, locations);
			}else {
				tracersIntoDiscardedWasteLocation.get(tracerId).add((String) result[1]);
			}
		}
		return tracersIntoDiscardedWasteLocation;
	}

}
