package org.research.kadda.nuclide.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.research.kadda.nuclide.entity.NuclideUsage;
import org.research.kadda.nuclide.models.Usage;
import org.research.kadda.nuclide.models.UsageOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

@Transactional
public class NuclideUsageDaoImpl implements NuclideUsageDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private final Logger logger = LoggerFactory.getLogger(NuclideUsageDaoImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideUsage> findAllUsage() {
		return sessionFactory.getCurrentSession().createQuery("from NuclideUsage").list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideUsage> findByNuclideBottleId(int tracerId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from NuclideUsage where nuclideBottle.nuclideBottleId = :tracerId");
		query.setParameter("tracerId", tracerId);
		return query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
	}
	
	@Override
	public Date getUsageDateByTracerId(Integer tracerId) {
		Date extUsageDate = null;
		Query query = sessionFactory.getCurrentSession().createQuery("from NuclideUsage where newNuclideBottleId = :tracerId and rownum = 1");
		query.setParameter("tracerId", tracerId);
		NuclideUsage extUsage = (NuclideUsage)  query.getSingleResult();
		if(extUsage != null) {
			extUsageDate = extUsage.getUsageDate(); 
		}
		return extUsageDate;
	}
	
	@Override
	public NuclideUsage findByUsageId(int usageId) {
		return (NuclideUsage) sessionFactory.getCurrentSession().get(NuclideUsage.class, usageId);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Usage> getWasteUsageList(int wasteId, char solidLiquidStatus) {
		List<Usage> wasteUsageList = new ArrayList<Usage>();
		StringBuilder sb = new StringBuilder("SELECT b.nuclide_bottle_id,b.substance_name,u.volume, ");
		sb.append(" u.bio_lab_journal,u.assay_type,TO_CHAR(u.usage_date,'DD-MM-YYYY'),s.first_name||' '||s.last_name, u.new_nuclide_bottle_id, u.solid_waste_percentage");
		sb.append(" FROM osiris.nuclide_usage u, osiris.nuclide_bottle b, osiris.nuclide_user s");
		sb.append(" where (u.nuclide_bottle_id = b.nuclide_bottle_id)");// or b.nuclide_bottle_id = u.new_nuclide_bottle_id)");
		sb.append(" AND u.user_id=s.user_id");
		sb.append(" AND (u.liquid_waste_id = ").append(wasteId).append(" OR u.solid_waste_id =").append(wasteId).append(")");
		sb.append(" ORDER BY u.usage_date");
		
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		
		for (Object[] result : results) {
			Usage wasteUsage = new Usage();
			wasteUsage.setTracerId(result[0]!=null?((Number)result[0]).intValue():null);
			wasteUsage.setSubstanceName(result[1]!=null?(String)result[1]:null);
			wasteUsage.setAmount(result[2]!=null?((BigDecimal)result[2]).toString():null);
			wasteUsage.setBioLabJournal(result[3]!=null?(String)result[3]:null);
			wasteUsage.setAssayType(result[4]!=null?(String)result[4]:null);
			wasteUsage.setUsageDate(result[5]!=null?(String)result[5]:null);
			wasteUsage.setScientist(result[6]!=null?(String)result[6]:null);
			wasteUsage.setScientist(result[6]!=null?(String)result[6]:null);
			if(result[7]!=null) {				
				wasteUsage.setNewTracerId(((Number)result[7]).intValue());
			}
			wasteUsage.setSolideWastePercentage(result[8]!=null?((BigDecimal)result[8]).doubleValue():null);
			wasteUsageList.add(wasteUsage);
		}
		return wasteUsageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getCurrentActivityKBqLEMap(char isLiquid, int nuclideWasteId) {
		Map<String, String> currentActivityKBqLEMap = new HashMap<String, String>();
		String activity = "";
		String allowance = "";
		StringBuilder sb = new StringBuilder();
		if(isLiquid == 'Y') {
			sb.append("SELECT MAX(n.kbq_per_freigrenze), SUM(b.activity/b.volume*u.volume*u.liquid_waste_percentage/100");
			sb.append("/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time))");
			sb.append(" FROM osiris.nuclide n, osiris.nuclide_usage u, osiris.nuclide_bottle b");
			sb.append(" WHERE u.nuclide_bottle_id=b.nuclide_bottle_id");
			sb.append(" AND b.nuclide_name=n.nuclide_name");
			sb.append(" AND u.liquid_waste_id=").append(nuclideWasteId);
		}else {
			sb.append("SELECT MAX(n.kbq_per_freigrenze),SUM(b.activity/b.volume*u.volume*u.solid_waste_percentage/100");
			sb.append("/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time))");
			sb.append(" FROM osiris.nuclide n, osiris.nuclide_usage u, osiris.nuclide_bottle b");
			sb.append(" WHERE u.nuclide_bottle_id=b.nuclide_bottle_id");
			sb.append(" AND b.nuclide_name=n.nuclide_name");
			sb.append(" AND u.solid_waste_id=").append(nuclideWasteId);
		}
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		List<Object[]> results = query.getResultList();
		
		for (Object[] result : results) {			
			activity = result[1]!=null?((BigDecimal)result[1]).toString():null;
			if(activity != null) {				
				double act = (double)((long)(1000*Double.parseDouble(activity)))/1000;
				activity = ""+act;
				double all = (double)((long)(1000*act/((BigDecimal)result[0]).doubleValue()))/1000;
				allowance = ""+all;	
			}
			break;
		}
		currentActivityKBqLEMap.put(activity, allowance);
		return currentActivityKBqLEMap;
	}
	
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public String getCurrentActivityKBq(char isLiquid, int nuclideWasteId) {
		String activity = "";
		StringBuilder sb = new StringBuilder();
		if(isLiquid == 'Y') {
			sb.append("SELECT MAX(n.kbq_per_freigrenze), SUM(DECODE(b.volume,0,0, b.activity/b.volume*u.volume*u.liquid_waste_percentage/100");
			sb.append("/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time)))");
			sb.append(" FROM osiris.nuclide n, osiris.nuclide_usage u, osiris.nuclide_bottle b");
			sb.append(" WHERE u.nuclide_bottle_id=b.nuclide_bottle_id");
			sb.append(" AND b.nuclide_name=n.nuclide_name");
			sb.append(" AND u.liquid_waste_id=").append(nuclideWasteId);
		}else {
			sb.append("SELECT MAX(n.kbq_per_freigrenze),SUM(DECODE(b.volume,0,0, b.activity/b.volume*u.volume*u.solid_waste_percentage/100");
			sb.append("/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time)))");
			sb.append(" FROM osiris.nuclide n, osiris.nuclide_usage u, osiris.nuclide_bottle b");
			sb.append(" WHERE u.nuclide_bottle_id=b.nuclide_bottle_id");
			sb.append(" AND b.nuclide_name=n.nuclide_name");
			sb.append(" AND u.solid_waste_id=").append(nuclideWasteId);
		}		
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		for (Object[] result : results) {			
			activity = result[1]!=null?((BigDecimal)result[1]).toString():null;
			break;
		}
		return activity;
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	public String getInVivoActivityKBq(int nuclideWasteId) {
		String activity = "";
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT MAX(n.kbq_per_freigrenze),SUM(DECODE(b.volume,0,0, b.activity/b.volume*b.volume*u.solid_waste_percentage/100");
		sb.append("/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time)))");
		sb.append(" FROM osiris.nuclide n, osiris.nuclide_usage u, osiris.nuclide_bottle b");
		sb.append(" WHERE u.new_nuclide_bottle_id=b.nuclide_bottle_id");
		sb.append(" AND b.nuclide_name=n.nuclide_name");
		sb.append(" AND b.batch_name like 'In-Vivo%'");
		sb.append(" AND u.solid_waste_id=").append(nuclideWasteId);
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		for (Object[] result : results) {			
			activity = result[1]!=null?((BigDecimal)result[1]).toString():null;
			break;
		}
		return activity;
	}

	@Override
	public boolean saveNuclide(NuclideUsage nuclideUsage) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(nuclideUsage);
			logger.info("The nuclideUsage " + nuclideUsage.getNuclideUsageId() + "has been saved");
		} catch (InvalidDataAccessResourceUsageException ex) {
			logger.error(ex.getMessage());
		    return false;
	    } 	
		return true;
	}

	@Override
	public int finLastIndex() {
		return (Integer) sessionFactory.getCurrentSession().createQuery("select MAX(nuclideUsageId) from NuclideUsage").getSingleResult();
	}

	@Override
	public double getSumVolumePerTracerId(Integer tracerId) {
		Query query = sessionFactory.getCurrentSession().createQuery("select SUM(volume) from NuclideUsage where nuclideBottle.nuclideBottleId = :tracerId");
		query.setParameter("tracerId", tracerId);
		return query.getSingleResult() != null ? (double) query.getSingleResult()  : 0;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Double getWasteVolume(int nuclideBottleId) {
		Query query = sessionFactory.getCurrentSession().createNativeQuery("select b.volume, u.solid_waste_percentage from nuclide_bottle b, nuclide_usage u where b.nuclide_bottle_id = u.new_nuclide_bottle_id and u.new_nuclide_bottle_id = "+nuclideBottleId);
		Double volume = 0.0;
		Double percentage = 0.0;
		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		for (Object[] res : results) {
			if(res[0]!=null) {volume = ((Number)res[0]).doubleValue();}
			if(res[1]!=null) {percentage = ((Number)res[1]).doubleValue();}
		}
		return (volume * percentage)/100;
	}


	@Override
	public String getElb(int tracerId) {
		Query query = sessionFactory.getCurrentSession().createQuery("select max(bioLabJournal) from NuclideUsage where newNuclideBottleId = :tracerId");
		query.setParameter("tracerId", tracerId);
		return query.getSingleResult() != null ? (String) query.getSingleResult()  : "N/A";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Integer getParentTracerTube(int tracerId) {
		StringBuilder sb = new StringBuilder("select * from (");
		sb.append("select nuclide_bottle_id, usage_date from osiris.nuclide_usage where new_nuclide_bottle_id =").append(tracerId);
		sb.append(" order by usage_date desc");
		sb.append(") where rownum = 1");
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		Integer result = null;
		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		for (Object[] res : results) {
			result = res[0]!=null?((Number)res[0]).intValue():null;
		}
		return result;
	}

	@Override
	public int getLastId() {
		Query query = sessionFactory.getCurrentSession().createNativeQuery("select max(nuclide_bottle_id) from osiris.nuclide_bottle");
		return ((BigDecimal) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<UsageOverview> getTracerUsageList(int nuclideBottleId, boolean isChildren) {
		List<UsageOverview> usageOverviewList = new ArrayList<UsageOverview>();
		
		
		StringBuilder sb = new StringBuilder("SELECT u.liquid_waste_id,");
		sb.append(" u.solid_waste_id,");
		sb.append(" u.liquid_waste_percentage,");
		sb.append(" u.solid_waste_percentage,");
		sb.append(" u.volume,");
		sb.append(" u.bio_lab_journal,");
		sb.append(" u.assay_type,");
		sb.append(" u.destination,");
		sb.append(" TO_CHAR(u.usage_date, 'DD-MM-YYYY'),");
		sb.append(" s.first_name || ' ' || s.last_name,");
		sb.append(" u.new_nuclide_bottle_id,");
		sb.append(" b.tracer_type_id,");
		sb.append(" u.nuclide_usage_id");
		sb.append(" FROM osiris.nuclide_usage u, osiris.nuclide_user s, osiris.nuclide_bottle b");
		sb.append(" WHERE u.user_id = s.user_id");
		sb.append(" AND u.nuclide_bottle_id = b.nuclide_bottle_id");
		sb.append(" AND u.nuclide_bottle_id = ").append(nuclideBottleId);
		sb.append(" ORDER BY u.usage_date");
		
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());

		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		
		for (Object[] result : results) {
			Integer liquidPercentage = result[2]!=null?((Number)result[2]).intValue():null;
			Integer solidPercentage = result[3]!=null?((Number)result[3]).intValue():null;
			Integer overallPercentage = liquidPercentage + solidPercentage;
			//liquid
			UsageOverview liquidUsageOverview = new UsageOverview();
			liquidUsageOverview.setWastId(result[0]!=null?((Number)result[0]).toString():null);
			liquidUsageOverview.setPercentage(liquidPercentage);
			liquidUsageOverview.setAmount(result[4]!=null?((BigDecimal)result[4]):null);
			liquidUsageOverview.setBioLabJournal(result[5]!=null?(String)result[5]:"N/A");
			liquidUsageOverview.setAssayType(result[6]!=null?(String)result[6]:"N/A");
			liquidUsageOverview.setDestination(result[7]!=null?(String)result[7]:"N/A");
			liquidUsageOverview.setUsageDate(result[8]!=null?(String)result[8]:"N/A");
			liquidUsageOverview.setUser(result[9]!=null?(String)result[9]:"N/A");
			liquidUsageOverview.setNewTracerId(result[10]!=null?(BigDecimal)result[10]:null);
			liquidUsageOverview.setTracerType(result[11]!=null?(BigDecimal)result[11]:null);			
			liquidUsageOverview.setOverallPercentage(overallPercentage);
			liquidUsageOverview.setUsageId(result[12]!=null?(BigDecimal)result[12]:null);
			usageOverviewList.add(liquidUsageOverview);
			
			//solid
			UsageOverview solidUsageOverview = new UsageOverview();
			solidUsageOverview.setWastId(result[1]!=null?((Number)result[1]).toString():null);
			solidUsageOverview.setPercentage(solidPercentage);
			solidUsageOverview.setAmount(result[4]!=null?((BigDecimal)result[4]):null);
			solidUsageOverview.setBioLabJournal(result[5]!=null?(String)result[5]:"N/A");
			solidUsageOverview.setAssayType(result[6]!=null?(String)result[6]:"N/A");
			solidUsageOverview.setDestination(result[7]!=null?(String)result[7]:"N/A");
			solidUsageOverview.setUsageDate(result[8]!=null?(String)result[8]:"N/A");
			solidUsageOverview.setUser(result[9]!=null?(String)result[9]:"N/A");
			solidUsageOverview.setNewTracerId(result[10]!=null?(BigDecimal)result[10]:null);
			solidUsageOverview.setTracerType(result[11]!=null?(BigDecimal)result[11]:null);			
			solidUsageOverview.setOverallPercentage(overallPercentage);
			solidUsageOverview.setUsageId(result[12]!=null?(BigDecimal)result[12]:null);
			usageOverviewList.add(solidUsageOverview);
		}
		return usageOverviewList;
	}

	@Override
	public boolean isChildren(int nuclideBottleId) {
		Query query = sessionFactory.getCurrentSession().createQuery("select count(nuclideUsageId) from NuclideUsage where newNuclideBottleId = :nuclideBottleId ");
		query.setParameter("nuclideBottleId", nuclideBottleId);
		Long result = (Long) query.getSingleResult();
		return result != 0L;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<UsageOverview> getInVivoUsages(int nuclideBottleId) {
		List<UsageOverview> inVivoUsages = new ArrayList<UsageOverview>();

		StringBuilder sb = new StringBuilder("SELECT u.solid_waste_id,");
		sb.append(" u.solid_waste_percentage,");
		sb.append(" u.volume as usagevolume,");
		sb.append(" u.bio_lab_journal,");
		sb.append(" u.assay_type,");
		sb.append(" u.destination,");
		sb.append(" TO_CHAR(u.usage_date, 'DD-MM-YYYY'),");
		sb.append(" s.first_name || ' ' || s.last_name,");
		sb.append(" b.volume  as bootlevolume,");
		sb.append(" u.nuclide_usage_id");
		sb.append(" FROM osiris.nuclide_usage u, osiris.nuclide_user s, osiris.nuclide_bottle b");
		sb.append(" WHERE u.user_id = s.user_id");
		sb.append(" AND b.nuclide_bottle_id = ").append(nuclideBottleId);
		sb.append(" AND u.new_nuclide_bottle_id = ").append(nuclideBottleId);
		sb.append(" AND u.assay_type like 'In-Vivo%'");
		sb.append(" ORDER BY u.usage_date");
		
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		List<Object[]> results = ((org.hibernate.query.Query) query).list();
		
		for (Object[] result : results) {
			UsageOverview solidUsageOverview = new UsageOverview();
			Double solidPercentage = result[1]!=null?((Number)result[1]).doubleValue():100.0;
			BigDecimal sampleVolume = result[8]!=null?((BigDecimal)result[8]):new BigDecimal(0);
			solidUsageOverview.setWastId(result[0]!=null?((Number)result[0]).toString():null);
			solidUsageOverview.setPercentage(solidPercentage.intValue());
			solidUsageOverview.setAmount(sampleVolume);
			solidUsageOverview.setBioLabJournal(result[3]!=null?(String)result[3]:"N/A");
			solidUsageOverview.setAssayType(result[4]!=null?(String)result[4]:"N/A");
			solidUsageOverview.setDestination(result[0]!=null?"waste #" + ((Number)result[0]).toString():"N/A");
			solidUsageOverview.setUsageDate(result[6]!=null?(String)result[6]:"N/A");
			solidUsageOverview.setUser(result[7]!=null?(String)result[7]:"N/A");
			solidUsageOverview.setOverallPercentage(solidPercentage.intValue());
			solidUsageOverview.setUsageId(result[9]!=null?(BigDecimal)result[9]:null);
			inVivoUsages.add(solidUsageOverview);
		}
		
		return inVivoUsages;
	}

	@Override
	public Double getSolidWastePercentage(int nuclideBottleId) {
		Query query = sessionFactory.getCurrentSession().createQuery("select solidWastePercentage from NuclideUsage where newNuclideBottleId = :newNuclideBottleId and rownum = 1");
		query.setParameter("newNuclideBottleId", nuclideBottleId);
		Double	result = (Double) query.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideUsage> getWasteUsages(int nuclideWasteId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from NuclideUsage where nuclideWasteBySolidWasteId.nuclideWasteId = :nuclideWasteId or nuclideWasteByLiquidWasteId.nuclideWasteId = :nuclideWasteId");
		query.setParameter("nuclideWasteId", nuclideWasteId);
		return query.setHint(QueryHints.HINT_CACHEABLE, true).getResultList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Integer> getAllTracersInsideWaste(int nuclideWasteId) {
		StringBuilder sb = new StringBuilder("SELECT nuclide_bottle_id FROM osiris.nuclide_usage WHERE solid_waste_id = ").append(nuclideWasteId).append(" OR liquid_waste_id = ").append(nuclideWasteId);
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		List<Integer> result = new ArrayList<Integer>();
		List<BigDecimal> results = ((org.hibernate.query.Query) query).list();
		for (BigDecimal res : results) {
			result.add((res).intValue());
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Date getUsageDate(int tracerId) {
		StringBuilder sb = new StringBuilder("SELECT usage_date FROM osiris.nuclide_usage WHERE new_nuclide_bottle_id = ").append(tracerId).append(" order by usage_date desc");
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		Date usageDate = null;
		if(!query.getResultList().isEmpty()) {
			List<Date> results = ((org.hibernate.query.Query) query).list();
			usageDate = (Date) results.get(0);
		}
		return usageDate;
	}
	
	
	@Override
	public int getLastSeq() {
		int	currentSequence = 0;
		try {
			Query query = sessionFactory.getCurrentSession().createNativeQuery("select osiris.nuclide_usage_seq_id.currval from dual");
			currentSequence = ((BigDecimal) query.getSingleResult()).intValue();
		} catch (Exception e) {
			// currval not yet defined OracleDatabaseException
			Query query = sessionFactory.getCurrentSession().createNativeQuery("select osiris.nuclide_usage_seq_id.nextval from dual");
			currentSequence = ((BigDecimal) query.getSingleResult()).intValue();
		}
		return currentSequence;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getDestination(int tracerId) {
		StringBuilder sb = new StringBuilder("SELECT destination FROM osiris.nuclide_usage WHERE new_nuclide_bottle_id = ").append(tracerId);
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
		String destination = null;
		if(!query.getResultList().isEmpty()) {
			List<String> results = ((org.hibernate.query.Query) query).list();
			destination = (String) results.get(0);
		}
		return destination;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Integer getParent(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery("select nuclideBottle.nuclideBottleId from NuclideUsage where newNuclideBottleId = :id");
		query.setParameter("id", id);
		return (Integer) query.getResultList().stream().findFirst().orElse(null);
	}



}
