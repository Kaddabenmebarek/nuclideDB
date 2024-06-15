package org.research.kadda.nuclide.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.research.kadda.nuclide.dao.NuclideUsageDao;
import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.entity.NuclideUsage;
import org.research.kadda.nuclide.models.TracerHierarchy;
import org.research.kadda.nuclide.models.Usage;
import org.research.kadda.nuclide.models.UsageOverview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NuclideUsageServiceImpl implements NuclideUsageService {

	public static final BigDecimal ONE_HUNDRED_PERCENT = new BigDecimal(100);
	public static final BigDecimal TEN_HUNDRED_PERCENT = new BigDecimal(10);
	
	@Autowired
	private NuclideUsageDao nuclideUsageDao;

	@Override
	public List<NuclideUsage> findAllUsage() {
		return nuclideUsageDao.findAllUsage();
	}

	@Override
	public NuclideUsage findByUsageId(int usageId) {
		return nuclideUsageDao.findByUsageId(usageId);
	}

	@Override
	public List<Usage> getWasteUsageList(int wasteId, char solidLiquidStatus) {
		return nuclideUsageDao.getWasteUsageList(wasteId, solidLiquidStatus);
	}

	@Override
	public Map<String, String> getCurrentActivityKBqLEMap(char isLiquid, int nuclideWasteId) {
		return nuclideUsageDao.getCurrentActivityKBqLEMap(isLiquid, nuclideWasteId);
	}

	@Override
	public int finLastIndex() {
		return nuclideUsageDao.finLastIndex();
	}

	@Override
	public boolean saveNuclide(NuclideUsage nuclideUsage) {
		return nuclideUsageDao.saveNuclide(nuclideUsage);
	}

	@Override
	public double getSumVolumePerTracerId(Integer tracerId) {
		return nuclideUsageDao.getSumVolumePerTracerId(tracerId) + nuclideUsageDao.getWasteVolume(tracerId);
	}

	@Override
	public String getElb(int tracerId) {
		return nuclideUsageDao.getElb(tracerId);
	}

	@Override
	public Integer getParentTracerTube(int tracerId) {
		return nuclideUsageDao.getParentTracerTube(tracerId);
	}

	@Override
	public String getCurrentActivityKBq(char isLiquid, int nuclideWasteId) {
		return nuclideUsageDao.getCurrentActivityKBq(isLiquid, nuclideWasteId);
	}

	@Override
	public String getInVivoActivityKBq(int nuclideWasteId) {
		return nuclideUsageDao.getInVivoActivityKBq(nuclideWasteId);
	}

	@Override
	public int getLastId() {
		return nuclideUsageDao.getLastId();
	}

	@Override
	public List<UsageOverview> getTracerUsageList(int nuclideBottleId, boolean isChildren) {
		return nuclideUsageDao.getTracerUsageList(nuclideBottleId, isChildren);
	}

	@Override
	public boolean isChildren(int nuclideBottleId) {
		return nuclideUsageDao.isChildren(nuclideBottleId);
	}

	public NuclideUsageDao getNuclideUsageDao() {
		return nuclideUsageDao;
	}

	public void setNuclideUsageDao(NuclideUsageDao nuclideUsageDao) {
		this.nuclideUsageDao = nuclideUsageDao;
	}

	@Override
	public List<UsageOverview> getInVivoUsages(int nuclideBottleId) {
		return nuclideUsageDao.getInVivoUsages(nuclideBottleId);
	}

	@Override
	public Double getSolidWastePercentage(int nuclideBottleId) {
		return nuclideUsageDao.getSolidWastePercentage(nuclideBottleId);
	}

	@Override
	public List<NuclideUsage> getWasteUsages(int nuclideWasteId) {
		return nuclideUsageDao.getWasteUsages(nuclideWasteId);
	}

	@Override
	public List<Integer> getAllTracersInsideWaste(int nuclideWasteId) {
		return nuclideUsageDao.getAllTracersInsideWaste(nuclideWasteId);
	}

	@Override
	public Date getUsageDate(int tracerId) {
		return nuclideUsageDao.getUsageDate(tracerId);
	}
	
	
	@Override
	public int getLastSeq() {
		return nuclideUsageDao.getLastSeq();
	}

	@Override
	public Double getWasteVolume(int nuclideBottleId) {
		return nuclideUsageDao.getWasteVolume(nuclideBottleId);
	}
	
	@Override
	public String getDestination(int tracerId) {
		return nuclideUsageDao.getDestination(tracerId);
	}

	@Override
	public Date getUsageDateByTracerId(Integer tracerId) {
		return nuclideUsageDao.getUsageDateByTracerId(tracerId);
	}
	
	@Override
	public boolean isTenPercentLimitReached(NuclideBottle parentTracer, NuclideUsage usageToSave) {
		BigDecimal parentTracerCurrentAmount;
		if(parentTracer.getSumVolume() != null) {			
			parentTracerCurrentAmount = new BigDecimal(parentTracer.getVolume()).subtract(parentTracer.getSumVolume());
		}else {
			parentTracerCurrentAmount = new BigDecimal(parentTracer.getVolume());
		}
		BigDecimal usageAmount = new BigDecimal(usageToSave.getVolume());
		BigDecimal limit = percentage(new BigDecimal(parentTracer.getVolume()),TEN_HUNDRED_PERCENT);
		if(parentTracerCurrentAmount.subtract(usageAmount).compareTo(limit) <= 0) {
			return true;
		}
		return false;
		
	}

	private static BigDecimal percentage(BigDecimal val, BigDecimal pct){
	    return val.multiply(pct).divide(ONE_HUNDRED_PERCENT);
	}

	@Override
	public Integer getParent(int id) {
		return nuclideUsageDao.getParent(id);
	}

	@Override
	public List<NuclideUsage> findByNuclideBottleId(int tracerId) {
		return nuclideUsageDao.findByNuclideBottleId(tracerId);
	}
	
	@Override
	public void buildHtmlTree(List<TracerHierarchy> tracerHierarchy, StringBuilder sbTree, Map<String,String> tracerPercentageLeftMap) {
		sbTree.append("<ul>").append("\r\n");
		for(TracerHierarchy hierarchy : tracerHierarchy) {
			sbTree.append(" <li class=\"isExpanded\">");
			//sbTree.append(" <li>");
			//sbTree.append("<span class=\"nodeNuclideId\">").append(hierarchy.getNuclideId()).append("</span>");
			sbTree.append(hierarchy.getNuclideId());
			sbTree.append(" ").append(hierarchy.getSubstanceName());
			int amountPercentage = hierarchy.getAmountPercentage();
			if(amountPercentage < 0) {amountPercentage = 0;}
			//sbTree.append(" (").append(amountPercentage).append("%");
			
			tracerPercentageLeftMap.put(String.valueOf(hierarchy.getNuclideId()), String.valueOf(amountPercentage));
			
			if(hierarchy.isDiscarded()) {
				sbTree.append(" (disposed)");	
			}
			if(hierarchy.isExternalUsage()) {
				sbTree.append(" (external)");
			}
			sbTree.append("\r\n");
			if(hierarchy.getNuclideChildrenSet() != null && !hierarchy.getNuclideChildrenSet().isEmpty()) {
				buildHtmlTree(new ArrayList<TracerHierarchy>(hierarchy.getNuclideChildrenSet()), sbTree,tracerPercentageLeftMap);
			}else {
				sbTree.append(" </li>").append("\r\n");
			}
		}
		sbTree.append("</ul>").append("\r\n");
	}

}
