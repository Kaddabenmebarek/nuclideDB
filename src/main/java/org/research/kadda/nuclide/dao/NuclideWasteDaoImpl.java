package org.research.kadda.nuclide.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.research.kadda.nuclide.entity.NuclideDisposalRoute;
import org.research.kadda.nuclide.entity.NuclideWaste;
import org.research.kadda.nuclide.models.WasteOverview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

@Transactional
public class NuclideWasteDaoImpl implements NuclideWasteDao {

	private final static String OPEN = "open";
	private final static String CLOSED = "closed";
	private final static String DICARDED = "discarded";
	private final static String ANY = "any";
	
	private final Logger logger = LogManager.getLogger(NuclideWasteDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideWaste> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from NuclideWaste").setHint(QueryHints.HINT_CACHEABLE, true).list();
	}

	@Override
	public NuclideWaste findWasteById(Integer id) {
		return (NuclideWaste) sessionFactory.getCurrentSession().get(NuclideWaste.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WasteOverview> findWasteByParams(String nuclideName, String open, String liquid) {		

		List<WasteOverview> wasteListWithParam = new ArrayList<WasteOverview>();
		StringBuilder sb = new StringBuilder("select w.nuclideWasteId, w.nuclide.nuclideName, w.isLiquid, w.location, w.closureDate, w.disposalDate, w.disposalActivity, w.disposalRoute, n.disposalLimit");
		sb.append(" from NuclideWaste w, Nuclide n where w.nuclide.nuclideName = n.nuclideName");
		switch (open) {
		case OPEN:
			sb.append(" and w.closureDate is null");
			break;
		case CLOSED:
			sb.append(" and w.closureDate is not null and w.disposalDate is null");
			break;
		case DICARDED:
			sb.append(" and w.disposalDate is not null");
			break;
		default:
			int year = Integer.parseInt(open)+1;
			sb.append(" and w.disposalDate > TRUNC(TO_DATE('01-01-").append(open).append("', 'MM-DD-YYYY'))");
			sb.append(" and w.disposalDate < TRUNC(TO_DATE('01-01-").append(String.valueOf(year)).append("', 'MM-DD-YYYY'))");			
		}
		if(nuclideName != null && !nuclideName.equals(ANY)) {
			sb.append(" and w.nuclide.nuclideName = :nuclideName");
		}
		if(liquid != null && !liquid.equals(ANY)) {
			sb.append(" and w.isLiquid = :liquid");
		}
		sb.append(" order by w.nuclideWasteId desc");
			
		Query query = sessionFactory.getCurrentSession().createQuery(sb.toString());
		if(nuclideName != null && !nuclideName.equals(ANY)) {
			query.setParameter("nuclideName",nuclideName);
		}
		if(liquid != null && !liquid.equals(ANY)) {
			query.setParameter("liquid",liquid.charAt(0));
		}
	
		List<Object[]> results = query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
		
		for (Object[] result : results) {
			WasteOverview wasteOverviewDto = new WasteOverview();
			wasteOverviewDto.setNuclideWasteId(((Number)result[0]).intValue());
			wasteOverviewDto.setNuclideName(result[1]!=null?(String)result[1]:null);
			wasteOverviewDto.setSolidLiquidState(result[2]!=null?((Character)result[2]).toString():null);
			wasteOverviewDto.setLocation(result[3]!=null?(String)result[3]:null);
			wasteOverviewDto.setClosedOn(result[4]!=null?((Date)result[4]).toString():null);
			wasteOverviewDto.setDisposedOfOn(result[5]!=null?((Date)result[5]).toString():null);
			wasteOverviewDto.setActivityKbq(result[6]!=null?((Double)result[6]).toString():null);
			wasteOverviewDto.setDisposalRoute(result[7]!=null?(String)result[7]:null);
			wasteOverviewDto.setDisposalLimit(result[8]!=null?((BigDecimal)result[8]).intValue():null);
			wasteListWithParam.add(wasteOverviewDto);
		}
		return wasteListWithParam;
	}

	@Override
	public Map<Double, Double> getMaxSumActivityVolume(boolean isLiquid, int wasteId) {
		
		Map<Double, Double> resultMap = new HashMap<Double, Double>();
		StringBuilder sb = new StringBuilder("select max(n.kbqPerFreigrenze), ");
		if(isLiquid) {	
			sb.append("sum(b.activity / b.volume * u.volume * u.liquidWastePercentage / ").append(100).append(" / exp(ln(2.0) * (trunc(SYSDATE) - trunc(b.activityDate)) / n.halfTime))");
		}else {
			sb.append("sum(b.activity / b.volume * u.volume * u.solidWastePercentage / ").append(100).append(" / exp(ln(2.0) * (trunc(SYSDATE) - trunc(b.activityDate)) / n.halfTime))");
		}
		sb.append(" from Nuclide n, NuclideUsage u, NuclideBottle b");
		sb.append(" where u.nuclideBottle.nuclideBottleId = b.nuclideBottleId");
		sb.append(" and n.nuclideName = b.nuclide.nuclideName");
		if(isLiquid) {
			sb.append(" and u.nuclideWasteByLiquidWasteId.nuclideWasteId = :wasteId");			
		}else {
			sb.append(" and u.nuclideWasteBySolidWasteId.nuclideWasteId = :wasteId");
		}
		Query query = sessionFactory.getCurrentSession().createQuery(sb.toString());
		query.setParameter("wasteId", wasteId);
		
		Object[] obj = (Object[]) query.getSingleResult();		
		resultMap.put((Double) obj[0], (Double) obj[1]);
		
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Double> getDisposalRouteActivity(String openStatus) {
		Map<String, Double> routeActivtyMap = new HashMap<String, Double>();
		StringBuilder sb = new StringBuilder("select disposalRoute, SUM(disposalActivity) from NuclideWaste where");
		switch (openStatus) {
		case OPEN:
			sb.append(" closureDate is not null");
			break;
		case CLOSED:
			sb.append(" closureDate is not null and disposalDate is null");
			break;
		case DICARDED:
			sb.append(" disposalDate is null");
			break;
		default:
			sb.append("");
		}
		sb.append(" group by disposalRoute order by disposalRoute");
		Query query = sessionFactory.getCurrentSession().createQuery(sb.toString());
		List<Object[]> results = query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
		
		for (Object[] result : results) {
			routeActivtyMap.put(result[0]!=null?(String)result[0]:null, result[1]!=null?(Double)result[1]:null);
		}
		
		return routeActivtyMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDisposalRouteList() {
		List<String> disposalRouteList = new ArrayList<String>(); 
		List<NuclideDisposalRoute> nuclideDisposalRouteList = sessionFactory.getCurrentSession().createQuery("from NuclideDisposalRoute").setHint(QueryHints.HINT_CACHEABLE, true).list();
		for(NuclideDisposalRoute disposalRoute : nuclideDisposalRouteList) {
			disposalRouteList.add(disposalRoute.getDisposalRoute());
		}
		Collections.sort(disposalRouteList);
		return disposalRouteList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> findDisposalRouteActivitySum(String nuclideName, String openStatus, String liquid) {
		
		Map<String, Integer> disposalRouteActivitySum = new HashMap<String, Integer>();
		boolean yearProvided = false;
		StringBuilder sb = new StringBuilder("select disposalRoute, SUM(disposalActivity) from NuclideWaste");
		switch (openStatus) {
		case OPEN:
			sb.append(" where closureDate is not null");
			break;
		case CLOSED:
			sb.append(" where closureDate is not null");
			break;
		case DICARDED:
			sb.append(" where disposalDate is not null");
			break;
		default:
			sb.append(" where to_char(disposalDate,'YYYY')  = :openStatus");
			yearProvided = true;
		}
		if(nuclideName != null && !nuclideName.equals(ANY)) {
			sb.append(" and nuclide.nuclideName = :nuclideName");
		}
		if(liquid != null && !liquid.equals(ANY)) {
			sb.append(" and isLiquid = :liquid");
		}
		
		sb.append(" group by disposalRoute order by disposalRoute");
		
		Query query = sessionFactory.getCurrentSession().createQuery(sb.toString());
		
		if(yearProvided) {
			query.setParameter("openStatus",openStatus);
		}
		
		if(nuclideName != null && !nuclideName.equals(ANY)) {
			query.setParameter("nuclideName",nuclideName);
		}
		if(liquid != null && !liquid.equals(ANY)) {
			query.setParameter("liquid",liquid.charAt(0));
		}
		
		List<Object[]> results = query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
		
		for (Object[] result : results) {
			disposalRouteActivitySum.put(result[0]!=null?(String)result[0]:null, result[1]!=null?((Double)result[1]).intValue():null);
		}
		
		return disposalRouteActivitySum;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideWaste> findNuclideWasteByNuclideName(String nuclideName) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from NuclideWaste where nuclide.nuclideName = :nuclideName and isLiquid = 'Y' and closureDate is null order by nuclideWasteId");
		query.setParameter("nuclideName", nuclideName);
		return query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideWaste> findNuclideWasteByStateStatus(char stateStatus) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from NuclideWaste where isLiquid = :stateStatus and closureDate is null order by nuclideWasteId");
		query.setParameter("stateStatus", stateStatus);
		return query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getUsersForWaste(Integer id) {
		Map<String, String> userForWasteMap = new HashMap<String, String>();
		Query query = sessionFactory.getCurrentSession().createNativeQuery("select creation_user_id, closure_user_id, disposal_user_id from osiris.nuclide_waste where nuclide_waste_id = " + id);
		List<Object[]> results = query.getResultList();
		for (Object[] result : results) {
			userForWasteMap.put("creationUserId", result[0]!=null?(String)result[0]:null);
			userForWasteMap.put("closureUserId", result[1]!=null?(String)result[1]:null);
			userForWasteMap.put("disposalUserId", result[2]!=null?(String)result[2]:null);
		}
		
		return userForWasteMap;
	}	
	
	@Override
	public boolean saveNuclideWaste(NuclideWaste nuclideWaste) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(nuclideWaste);
			logger.info("The nuclideWaste " + nuclideWaste.getNuclideWasteId() + "has been saved");
		} catch (InvalidDataAccessResourceUsageException ex) {
			logger.error(ex.getMessage());
		    return false;
	    } 	
		return true;
	}

	@Override
	public void deleteNuclideWaste(NuclideWaste nuclideWaste) {
		sessionFactory.getCurrentSession().remove(nuclideWaste);
		logger.info("The nuclideWaste " + nuclideWaste.getNuclideWasteId() + "has been removed");
	}

	@Override
	public int getLastId() {
		Query query = sessionFactory.getCurrentSession().createQuery("select MAX(nuclideWasteId) from NuclideWaste");
		return (int) query.getSingleResult();
	}

	@Override
	public Double getWasteSum(String nuclideName, String location) {
		StringBuilder sb = new StringBuilder("SELECT NVL(SUM((b.activity*u.liquid_waste_percentage/100*u.volume/b.volume)/");
		sb.append("EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time)),0)");
		sb.append(" FROM osiris.nuclide_bottle b,osiris.nuclide_waste w,osiris.nuclide_usage u,osiris.nuclide n");
		sb.append(" WHERE w.nuclide_name=n.nuclide_name AND u.nuclide_bottle_id=b.nuclide_bottle_id");
		sb.append(" AND u.liquid_waste_id=w.nuclide_waste_id AND w.nuclide_name='").append(nuclideName).append("'");
		sb.append(" AND b.location='").append(location).append("'");
		sb.append(" AND b.disposal_date IS NULL");
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		return query.getSingleResult() != null ? ((BigDecimal) query.getSingleResult()).doubleValue() : 0;
	}

	@Override
	public Double getComplementWasteSum(String nuclideName, String location) {
		StringBuilder sb = new StringBuilder("SELECT NVL(SUM((b.activity*u.solid_waste_percentage/100*u.volume/b.volume)/");
		sb.append("EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time)),0)");
		sb.append(" FROM osiris.nuclide_bottle b,osiris.nuclide_waste w,osiris.nuclide_usage u,osiris.nuclide n");
		sb.append(" WHERE w.nuclide_name=n.nuclide_name AND u.nuclide_bottle_id=b.nuclide_bottle_id");
		sb.append(" AND u.solid_waste_id=w.nuclide_waste_id AND w.nuclide_name='").append(nuclideName).append("'");
		sb.append(" AND b.location='").append(location).append("'");
		sb.append(" AND b.disposal_date IS NULL");
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		return query.getSingleResult() != null ? ((BigDecimal) query.getSingleResult()).doubleValue() : 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getWasteContainerList(String nuclideName, char isLiquid) {
		Map<String, String> wasteContainerList = new HashMap<String,String>();
		StringBuilder sb = new StringBuilder(
				"select nuclideWasteId, nuclide.nuclideName from NuclideWaste where nuclide.nuclideName =:nuclideName and  isLiquid = :isLiquid and closureDate is null order by nuclideWasteId");
		Query query = sessionFactory.getCurrentSession().createQuery(sb.toString());
		query.setParameter("nuclideName", nuclideName);
		query.setParameter("isLiquid", isLiquid);
		List<Object[]> results = query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
		for (Object[] result : results) {
			String wasteId = result[0]!=null?((Number)result[0]).toString() : null;
			String nuclide = result[1]!=null?(String)result[1]:null;
			StringBuilder nuclideNameConcat = new StringBuilder("Waste #").append(wasteId).append(": ").append(nuclide);
			wasteContainerList.put(wasteId,nuclideNameConcat.toString());
		}
		return wasteContainerList;
		
	}

	@Override
	public Object getTargetedObject(String queryToUse) {
		Query query = sessionFactory.getCurrentSession().createNativeQuery(queryToUse);
		return query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getDiscardedWastes() {
		List<NuclideWaste> discardedWastes = sessionFactory.getCurrentSession().createQuery("from NuclideWaste where disposalRoute is not null").setHint(QueryHints.HINT_CACHEABLE, true).list();
		Set<String> discardedWastesId = new HashSet<String>();
		for(NuclideWaste nw : discardedWastes) {
			discardedWastesId.add(String.valueOf(nw.getNuclideWasteId()));
		}
		return discardedWastesId;
	}
	
}
