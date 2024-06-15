package org.research.kadda.nuclide.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;

import org.hibernate.jpa.QueryHints;
import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.models.UserOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.util.StringUtils;

@Transactional
public class NuclideUserDaoImpl implements NuclideUserDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	private final Logger logger = LoggerFactory.getLogger(NuclideUserDaoImpl.class);
	private final static String ANY = "any";

	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideUser> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from NuclideUser order by firstName").setHint(QueryHints.HINT_CACHEABLE, true).list();
	}
	
	@Override
	public NuclideUser findByUserId(String userId) {
		return (NuclideUser) sessionFactory.getCurrentSession().get(NuclideUser.class, userId);
	}	
	
	@Override
	public void saveUser(NuclideUser nuclideUser) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(nuclideUser);
			logger.info("The nuclideUser " + nuclideUser.getUserId() + "has been saved");
		} catch (InvalidDataAccessResourceUsageException ex) {
			logger.error(ex.getMessage());
		}
	}
	
	@Override
	public void updateAll(List<NuclideUser> usersToUpdate) {
		for(NuclideUser nuclideUserToUpdate : usersToUpdate) {		
			try {			
				sessionFactory.getCurrentSession().update(nuclideUserToUpdate);
				logger.info("The nuclideUser " + nuclideUserToUpdate.getUserId() + "has been updated");
			} catch (InvalidDataAccessResourceUsageException ex) {
				logger.error(ex.getMessage());
			} 
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<UserOverview> getUserOverviewList(String nuclideName, String userId, String lastUsageOnly, char isActive) {

		List<UserOverview> userOverviewList = new ArrayList<UserOverview>();
		Map<String, String> paramList = new HashMap<String,String>();
		StringBuilder sb = new StringBuilder();
		Query query = null;
		List<Object[]> results = null;
		if(lastUsageOnly.equalsIgnoreCase("last")) {
			sb.append("SELECT MAX(u.first_name || ' ' || u.last_name) as username,");
			sb.append(" TO_CHAR(MAX(n.usage_date),'DD-MM-YYYY'),");
			sb.append(" MAX(u.is_active),");
			sb.append(" u.user_id");
			sb.append(" FROM osiris.nuclide_user u");
			sb.append(" LEFT JOIN osiris.nuclide_usage n ON n.user_id = u.user_id");
			sb.append(" WHERE u.is_active = '").append(isActive).append("'");
			if(userId != null && !StringUtils.isEmpty(userId) && !ANY.equalsIgnoreCase(userId)) {			
				sb.append(" AND u.user_id='").append(userId).append("'");
			}
			sb.append(" GROUP BY u.user_id, u.is_active");
			sb.append(" ORDER BY username");
			query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());			
			
			results = ((org.hibernate.query.Query) query).list();
		}else {			
			sb.append("select usr.firstName || ' '|| usr.lastName, b.nuclide.nuclideName, TO_CHAR(u.usageDate, 'DD-MM-YYYY'), u.bioLabJournal, u.assayType, usr.isActive, usr.userId");
			sb.append(" from NuclideUser usr, NuclideUsage u, NuclideBottle b");
			sb.append(" where u.nuclideUser.userId = usr.userId");
			sb.append(" and u.nuclideBottle.nuclideBottleId = b.nuclideBottleId");
			if(nuclideName != null && !StringUtils.isEmpty(nuclideName) && !ANY.equalsIgnoreCase(nuclideName)) {			
				sb.append(" and b.nuclide.nuclideName = :nuclideName");
				paramList.put("nuclideName", nuclideName);
			}
			if(userId != null && !StringUtils.isEmpty(userId) && !ANY.equalsIgnoreCase(userId)) {			
				sb.append(" and usr.userId = :userId");
				paramList.put("userId", userId);
			}
			sb.append(" and usr.isActive = '").append(isActive).append("'");
			sb.append(" order by u.usageDate ");
			query = sessionFactory.getCurrentSession().createQuery(sb.toString());
			for(Entry<String, String> entry : paramList.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
			results = query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
		}
		
		if(results != null) {			
			for (Object[] result : results) {
				UserOverview userOverviewDto = new UserOverview();
				userOverviewDto.setUser(result[0]!=null?(String)result[0]:null);
				userOverviewDto.setLastUsageDate(result[1]!=null?(String)result[1]:null);
				userOverviewDto.setIsActive((Character)result[2] == 'Y' ? "Active" : "Inactive");
				userOverviewDto.setUserId(result[3]!=null?(String)result[3]:null);
				userOverviewList.add(userOverviewDto);
			}
		}
		
		return userOverviewList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideUser> findUserByStatus(char isActive) {
		Query query = sessionFactory.getCurrentSession().createQuery("from NuclideUser where isActive = :isActive order by userId");
		query.setParameter("isActive", isActive);
		return query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Boolean> getUsersStatusFromEmployee(List<NuclideUser> allNuclideUsers) {
		Map<String, Boolean> usersStatusMap = new HashMap<String, Boolean>();
		StringBuilder sb = new StringBuilder("select disabled, user_name, lastworkingdate ");
		sb.append("from osiris.employee ");
		sb.append("where user_name in (");
		for(int i = 0; i < allNuclideUsers.size(); i++) {
			sb.append("'").append(allNuclideUsers.get(i).getUserId().toLowerCase()).append("'");
			if(i < allNuclideUsers.size() - 1) {
				sb.append(",");
			}
		}
		sb.append(")");
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		for (Object[] result : results) {
			usersStatusMap.put((String)result[1], ((BigDecimal)result[0]).intValue() == 1 || result[2] != null ? false : true);
		}
		return usersStatusMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Boolean> getUsersGoneStatusFromEmployee(List<NuclideUser> allNuclideUsers) {
		Map<String, Boolean> usersGoneMap = new HashMap<String, Boolean>();
		StringBuilder sb = new StringBuilder("select lastworkingdate, user_name, disabled ");
		sb.append("from osiris.employee ");
		sb.append("where user_name in (");
		for(int i = 0; i < allNuclideUsers.size(); i++) {
			sb.append("'").append(allNuclideUsers.get(i).getUserId().toLowerCase()).append("'");
			if(i < allNuclideUsers.size() - 1) {
				sb.append(",");
			}
		}
		sb.append(")");
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		for (Object[] result : results) {
			usersGoneMap.put((String)result[1], result[0] != null || ((BigDecimal)result[2]).intValue() == 1 ? true : false);
		}
		return usersGoneMap;
	}


}
