package org.research.kadda.nuclide.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.research.kadda.nuclide.TracerTypeEnum;
import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.models.TracerHierarchy;
import org.research.kadda.nuclide.models.TracerOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.util.StringUtils;

@Transactional
public class NuclideBottleDaoImpl implements NuclideBottleDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private final static String ANY = "any";
	private final static String ORIGINAL = "original";
	private final static String EXTERNAL = "external";
	
	private final Logger logger = LoggerFactory.getLogger(NuclideBottleDaoImpl.class);
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideBottle> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from NuclideBottle").setHint(QueryHints.HINT_CACHEABLE, true).list();
	}

	@Override
	public NuclideBottle findById(int nuclideBottleId) {
		NuclideBottle nuclideBottle = sessionFactory.getCurrentSession().get(NuclideBottle.class, nuclideBottleId);
		if(nuclideBottle != null) {
			//set current amount
			Query currentAmountQuery = sessionFactory.getCurrentSession().createNativeQuery("select sum(volume) from osiris.Nuclide_Usage where nuclide_bottle_id = " + nuclideBottleId);
			BigDecimal sumVolume = (BigDecimal) currentAmountQuery.getSingleResult();
			nuclideBottle.setSumVolume(sumVolume);
			
			//set currentActivity
			Query query = sessionFactory.getCurrentSession().createNativeQuery("select b.activity/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_Date))/n.half_Time) ,b.activity from osiris.Nuclide_Bottle b, osiris.Nuclide n where b.nuclide_Name = n.nuclide_Name and b.nuclide_bottle_id = " + nuclideBottleId);
			Object[] result = (Object[]) query.getSingleResult();
			Double caluclatedActivity = ((BigDecimal) result[0]).doubleValue();
			Double initialActivity = ((BigDecimal) result[1]).doubleValue();
			Double currentActivity = initialActivity.intValue() - caluclatedActivity.intValue() >=0 ? Math.round((caluclatedActivity*100.0)/100.0)  : initialActivity;  
			nuclideBottle.setCurrentActivity(currentActivity); 
		}
		
		return nuclideBottle;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TracerOverview> findNuclideBottleByParam(String nuclideName, String location, String initialActivityYear,
			String discardedStatus, String isLiquid, String daughter, String scientist, String tracerType) {

		int lastYears = Calendar.getInstance().get(Calendar.YEAR) - 15;
		
		List<TracerOverview> tracerOverviewDtoList = new ArrayList<TracerOverview>();
		Map<String, Object> queryParameters = new HashMap<String, Object>();
  
		boolean isExternal = TracerTypeEnum.EXTERNAL.getTracerTypeName().equals(tracerType);
		StringBuilder sb = new StringBuilder("select b.nuclideBottleId, b.nuclide.nuclideName, b.substanceName, b.isLiquid,");
		sb.append(" (select sum(u.volume) from NuclideUsage u where u.nuclideBottle.nuclideBottleId=b.nuclideBottleId),");
		sb.append(" b.activity/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activityDate))/n.halfTime),");
		sb.append(" b.volume,b.activity,b.nuclideUserByNuclideUserId.userId,TO_CHAR(b.disposalDate,'DD-MM-YYYY'), b.location, b.batchName,");
		sb.append(" (select solidWastePercentage from NuclideUsage u where u.newNuclideBottleId=b.nuclideBottleId and rownum = 1),");
		sb.append(" b.tracerType.tracerTypeId");
		sb.append(", (select u.nuclideBottle.nuclideBottleId from NuclideUsage u where u.newNuclideBottleId=b.nuclideBottleId and rownum = 1)");
		if(isExternal) {
			sb.append(", (select u.usageDate from NuclideUsage u where u.newNuclideBottleId=b.nuclideBottleId and rownum = 1)");
			//sb.append(", (select u.nuclideBottle.nuclideBottleId from NuclideUsage u where u.newNuclideBottleId=b.nuclideBottleId and rownum = 1)");
		}
		sb.append(" from NuclideBottle b, Nuclide n");
		sb.append(" where b.nuclide.nuclideName = n.nuclideName");
		if(nuclideName!=null && !StringUtils.isEmpty(nuclideName) && !nuclideName.equals(ANY)) {			
			sb.append(" and b.nuclide.nuclideName = :nuclideName");
			queryParameters.put("nuclideName", nuclideName);
		}
		if(location != null && !location.equals(ANY)) {
			sb.append(" and b.location = :location");
			queryParameters.put("location", location);
		}
		String activityYear;
		if(initialActivityYear != null && !"any".equals(initialActivityYear) ) {
			activityYear = "'01-01-" + initialActivityYear + "', 'MM-DD-YYYY'";
			sb.append(" and TRUNC(b.activityDate,'YEAR') = TRUNC(TO_DATE(").append(activityYear).append("),'YEAR')");
		}else {			
			activityYear = "'01-01-" + lastYears + "', 'MM-DD-YYYY'";
			sb.append(" and TRUNC(b.activityDate,'YEAR') >= TRUNC(TO_DATE(").append(activityYear).append("),'YEAR')");
		}
		
		if(discardedStatus!=null && !discardedStatus.contentEquals(ANY)) {
			switch (discardedStatus) {
			case "Y":
				sb.append(" and b.disposalDate is not null");
				break;
			case "N":
				sb.append(" and b.disposalDate is null");
				break;
			case "E":
				sb.append(" and b.disposalDate is null");
				break;
			case "B":
				sb.append(""); //both
				break;
			default:
				if(isInt(discardedStatus)) {					
					String discardedYear = "01-01-"+discardedStatus;
					sb.append(" and TRUNC(b.disposalDate,'YEAR')=TO_DATE(:discardedYear, 'DD-MM-YYYY')");
					queryParameters.put("discardedYear", discardedYear);
				}else {
					sb.append(" and b.disposalDate is not null");
				}
			}
		}
		if(isLiquid != null && !isLiquid.equals(ANY)) {
			sb.append(" and b.isLiquid=:isLiquid");
			queryParameters.put("isLiquid", isLiquid.charAt(0));
			//overFlow = false;
		}
		if(daughter!= null && !daughter.equals(ANY) && !daughter.equals(EXTERNAL)) {
			if(daughter.equals(ORIGINAL)) {
				sb.append(" and not exists(from NuclideUsage u where u.newNuclideBottleId=b.nuclideBottleId)");
			}else {
				sb.append(" and exists(from NuclideUsage u where u.newNuclideBottleId=b.nuclideBottleId)");
			}
		}
		if(scientist!=null && !scientist.equals(ANY)) {
			sb.append(" and b.nuclideUserByNuclideUserId.userId=:scientist");
			queryParameters.put("scientist", scientist);
		}
		if(tracerType != null) {
			sb.append(" and b.tracerType.tracerTypeName=:tracerTypeName");
			queryParameters.put("tracerTypeName",tracerType);
		}else {
			if(!"any".equalsIgnoreCase(discardedStatus)) {				
				sb.append(" and b.tracerType.tracerTypeName!=:tracerTypeName");
				queryParameters.put("tracerTypeName","external");
			}
		}
		
		sb.append(" order by b.nuclideBottleId");
		
//		if(overFlow) {
//			findAndReplace(sb, "EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activityDate))/n.halfTime)", "125*(TRUNC(SYSDATE)-TRUNC(b.activityDate))/n.halfTime");
//		}
		
		Query query = sessionFactory.getCurrentSession().createQuery(sb.toString());
		for(Entry<String, Object> entry : queryParameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		List<Object[]> results = query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
		
		//System.out.println(results.size());
		
	for (Object[] result : results) {
		//if no parentid no instance
			TracerOverview tracerOverviewDto = new TracerOverview();
			tracerOverviewDto.setTracerId(result[0]!=null?((Number)result[0]).intValue():null);
			tracerOverviewDto.setNuclideName(result[1]!=null?(String)result[1]:null);
			tracerOverviewDto.setSubstance(result[2]!=null?(String)result[2]:null);
			tracerOverviewDto.setSolidLiquidState(result[3]!=null?((Character)result[3]).toString():null);
			Double initialAmount = (Double)result[6];
			Double usageAmount = result[4] != null ? (Double)result[4] : null;
			Double currentAmount;
			if(usageAmount != null) {
				currentAmount = initialAmount - usageAmount;
			}else {
				currentAmount = initialAmount;
			}
			Double initialActivity = (Double)result[7];
			if(currentAmount >= 0) {
				tracerOverviewDto.setCurrentAmount(String.valueOf(new BigDecimal(currentAmount)));
			}
			if(currentAmount < 0) {
				tracerOverviewDto.setCurrentAmount(String.valueOf(new BigDecimal(0)));
			}
			Double currentActivity = result[5] != null ?  Math.round((((Double)result[5])*100.0)/100.0) :(Double)result[7];
			if (initialActivity - currentActivity >= 0) {
				tracerOverviewDto.setCurrentActivity(String.valueOf(new BigDecimal(currentActivity)));
			} else {
				tracerOverviewDto.setCurrentActivity(String.valueOf(new BigDecimal(initialActivity)));
			}
			tracerOverviewDto.setInitialAmount(new BigDecimal(initialAmount));
			tracerOverviewDto.setInitialActivity(new BigDecimal(initialActivity));				
			tracerOverviewDto.setResponsible(result[8]!=null?(String)result[8]:null);
			tracerOverviewDto.setDisposalDate(result[9]!=null?(String)result[9]:null);
			tracerOverviewDto.setLocation(result[10]!=null?(String)result[10]:null);
			tracerOverviewDto.setBatchName(result[11]!=null?(String)result[11]:null);
			tracerOverviewDto.setSolideWastePercentage(result[12]!=null?((Double)result[12]):null);
			tracerOverviewDto.setTracerTypeId(result[13]!=null?((Number)result[13]).intValue():null);
			if(result[14] != null) {					
				tracerOverviewDto.setParentId(((Number)result[14]).intValue());
			}			
			if(isExternal) {
				tracerOverviewDto.setCreationDate(result[15]!=null?new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format((Date)result[15]):"N/A");
//				tracerOverviewDto.setCreationDate(result[14]!=null?new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format((Date)result[14]):"N/A");
//				if(result[15] != null) {					
//					tracerOverviewDto.setParentId(((Number)result[15]).intValue());
//				}
			}
			tracerOverviewDtoList.add(tracerOverviewDto);
   
	}
		
		return tracerOverviewDtoList;
	}


	@Override
	public Double getVolumeDifference(double volumeForDifference, int tracerId) {
		Query query = sessionFactory.getCurrentSession().createQuery("select :volumeForDifference -SUM(volume) from NuclideBottle where nuclideBottleId = :tracerId");
		query.setParameter("volumeForDifference", volumeForDifference);
		query.setParameter("tracerId", tracerId);
		return (Double) query.getSingleResult();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NuclideBottle> findUsageTraceTubeList() {
		Query query = sessionFactory.getCurrentSession().createQuery("from NuclideBottle where disposalDate is null and tracerType.tracerTypeId != :external order by nuclideBottleId desc");
		query.setParameter("external", TracerTypeEnum.EXTERNAL.getTracerTypeId());
		
		List<NuclideBottle> results = ((org.hibernate.query.Query) query).setHint(QueryHints.HINT_CACHEABLE, true).list();
		for(NuclideBottle nb : results) {			
			Query currentAmountQuery = sessionFactory.getCurrentSession().createNativeQuery("select sum(volume) from osiris.Nuclide_Usage where nuclide_bottle_id = " + nb.getNuclideBottleId());
			Object res = currentAmountQuery.getSingleResult();
			if(res != null) {
				BigDecimal sumVolume = (BigDecimal) res;
				if(sumVolume.intValue() < 0) sumVolume = new BigDecimal(0);
				nb.setSumVolume(sumVolume);				
			}else {
				nb.setSumVolume(new BigDecimal(0));
			}
		}
		return results;
	}	
	
	@Override
	public boolean saveNuclideBottle(NuclideBottle nuclideBottle) {
		try {			
			sessionFactory.getCurrentSession().saveOrUpdate(nuclideBottle);
			logger.info("The tracer " + nuclideBottle.getNuclideBottleId() + "has been saved");
		} catch (InvalidDataAccessResourceUsageException ex) {
			logger.error(ex.getMessage());
		    return false;
	    } 	
		return true;
	}

	@Override
	public void deleteNuclideBottle(NuclideBottle nuclideBottle) {
		sessionFactory.getCurrentSession().remove(nuclideBottle);
		logger.info("The tracer " + nuclideBottle.getNuclideBottleId() + "has been removed");
	}

	@Override
	public int getLastIndex() {
		Query query = sessionFactory.getCurrentSession().createQuery("select MAX(nuclideBottleId) from NuclideBottle");
		return (int) query.getSingleResult();
	}

	@Override
	public NuclideBottle findByIdAndDisposalStatus(int nuclideBottleId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from NuclideBottle where disposalDate is null and nuclideBottleId = :nuclideBottleId");
		query.setParameter("nuclideBottleId", nuclideBottleId);
		NuclideBottle nuclideBottle = (NuclideBottle) query.getSingleResult();
		
		//set current amount
		Query currentAmountQuery = sessionFactory.getCurrentSession().createNativeQuery("select sum(volume) from osiris.Nuclide_Usage where nuclide_bottle_id = " + nuclideBottleId);
		BigDecimal sumVolume = (BigDecimal) currentAmountQuery.getSingleResult();
		nuclideBottle.setSumVolume(sumVolume);
		
		//set currentActivity
		Query activityQuery = sessionFactory.getCurrentSession().createNativeQuery("select b.activity/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_Date))/n.half_Time) ,b.activity from osiris.Nuclide_Bottle b, osiris.Nuclide n where b.nuclide_Name = n.nuclide_Name and b.nuclide_bottle_id = " + nuclideBottleId);
		Object[] result = (Object[]) activityQuery.getSingleResult();
		Double caluclatedActivity = ((BigDecimal) result[0]).doubleValue();
		Double initialActivity = ((BigDecimal) result[1]).doubleValue();
		Double currentActivity = initialActivity.intValue() - caluclatedActivity.intValue() >=0 ? Math.round((caluclatedActivity*100.0)/100.0)  : initialActivity;  
		nuclideBottle.setCurrentActivity(currentActivity); 
		
		return nuclideBottle;
	}

	@Override
	public Double getTracerSum(String nuclideName, String location) {
		StringBuilder sb = new StringBuilder("SELECT NVL(SUM(b.activity/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time)");
		sb.append("*(b.volume-NVL((SELECT SUM(u.volume) from osiris.nuclide_usage u where u.nuclide_bottle_id=b.nuclide_bottle_id),0))/b.volume),0)");
		sb.append(" FROM osiris.nuclide_bottle b,osiris.nuclide n");
		sb.append(" WHERE b.nuclide_name=n.nuclide_name AND b.nuclide_name='").append(nuclideName).append("'");
		sb.append(" AND b.location='").append(location).append("'");
		sb.append(" AND b.disposal_date IS NULL");
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		return query.getSingleResult() != null ? ((BigDecimal) query.getSingleResult()).doubleValue() : 0;
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, String> getUsersForTracer(int tracerId) {
		Map<String, String> userForTracerMap = new HashMap<String, String>();
		Query query = sessionFactory.getCurrentSession().createNativeQuery("select nuclide_user_id, disposal_user_id from osiris.nuclide_bottle where nuclide_bottle_id = " + tracerId);
		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		for (Object[] result : results) {
			userForTracerMap.put("creationUserId", result[0]!=null?(String)result[0]:null);
			userForTracerMap.put("disposalUserId", result[1]!=null?(String)result[1]:null);
		}
		
		return userForTracerMap;
	}
	
	public static void findAndReplace(StringBuilder sb, String from, String to)
	{
	    int index = sb.indexOf(from);
	    while (index != -1)
	    {
	        sb.replace(index, index + from.length(), to);
	        index += to.length();
	        index = sb.indexOf(from, index);
	    }
	}

	@Override
	public Object getTargetedObject(String queryToUse) {
		Query query = sessionFactory.getCurrentSession().createNativeQuery(queryToUse);
		return query.getSingleResult();
	}
	
	public static boolean isInt(String s) {
		  try {
		    Integer.parseInt(s);
		    return true;
		  } catch (NumberFormatException nfe) {
		    return false;
		  }
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<TracerHierarchy> getTracerHierarchyList(String nuclideName) {
		List<TracerHierarchy> res = new ArrayList<TracerHierarchy>();
		
		StringBuilder hql = new StringBuilder("select b.nuclideBottleId, u.newNuclideBottleId, b.substanceName, b.isLiquid, b.volume, u.volume, b.location");
		hql.append(" from NuclideBottle b, NuclideUsage u");
		//hql.append(" where u.newNuclideBottleId = b.nuclideBottleId");
		hql.append(" where u.nuclideBottle.nuclideBottleId = b.nuclideBottleId");
		hql.append(" and b.nuclide.nuclideName = :nuclideName");
		hql.append(" and b.disposalDate is null");
		hql.append(" and b.tracerType.tracerTypeId != :external");
		hql.append(" order by b.nuclideBottleId");
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setParameter("nuclideName", nuclideName);
		query.setParameter("external", TracerTypeEnum.EXTERNAL.getTracerTypeId());
		
		List<Object[]> results = query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
		
		for (Object[] result : results) {
			TracerHierarchy th = new TracerHierarchy();
			th.setNuclideId(((Number)result[0]).intValue());
			//th.setNuclideChildren(String.valueOf((Number)result[1]));
			th.setSubstanceName(((String)result[2]));
			th.setState(((Character)result[3]));
			th.setInitialAmount((Double)result[4]);
			th.setChildrenAmount((Double)result[5]);
			th.setLocation(((String)result[6]));
			res.add(th);
		}
		
		return res;
	}

}
