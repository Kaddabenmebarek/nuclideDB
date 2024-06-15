package org.research.kadda.nuclide.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.research.kadda.nuclide.TracerTypeEnum;
import org.research.kadda.nuclide.dao.NuclideBottleDao;
import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.entity.NuclideUsage;
import org.research.kadda.nuclide.models.TracerHierarchy;
import org.research.kadda.nuclide.models.TracerOverview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NuclideBottleServiceImpl implements NuclideBottleService {

	@Autowired
	private NuclideBottleDao nuclideBottleDao;
	
	@Autowired
	private NuclideUsageService nuclideUsageService;

	@Override
	public List<NuclideBottle> findAll() {
		return nuclideBottleDao.findAll();
	}

	@Override
	public NuclideBottle findById(int nuclideBottleId) {
		return nuclideBottleDao.findById(nuclideBottleId);
	}
	
	@Override
	public List<TracerOverview> findNuclideBottleByParam(){
		return findNuclideBottleByParam(null, null, null, null, null, null, null, null);
	}
			
	@Override
	public List<TracerOverview> findNuclideBottleByParam(String nuclideName, String location,
			String initialActivityYear, String discardedStatus, String isLiquid, String daughter,
			String scientist, String tracerType) {
		return nuclideBottleDao.findNuclideBottleByParam(nuclideName, location, initialActivityYear, discardedStatus,
				isLiquid, daughter, scientist, tracerType);
	}

	@Override
	public Double getVolumeDifference(double volume, int tracerId) {
		return nuclideBottleDao.getVolumeDifference(volume, tracerId);
	}
	
	@Override
	public List<NuclideBottle> findUsageTraceTubeList() {
		return nuclideBottleDao.findUsageTraceTubeList();
	}	
	
	@Override
	public boolean saveNuclideBottle(NuclideBottle nuclideBottle) {
		return nuclideBottleDao.saveNuclideBottle(nuclideBottle);
	}

	@Override
	public void deleteNuclideBottle(NuclideBottle nuclideBottle) {
		nuclideBottleDao.deleteNuclideBottle(nuclideBottle);
	}

	@Override
	public int getLastIndex() {
		return nuclideBottleDao.getLastIndex();
	}

	@Override
	public NuclideBottle findByIdAndDisposalStatus(int nuclideBottleId) {
		return  nuclideBottleDao.findByIdAndDisposalStatus(nuclideBottleId);
	}

	@Override
	public Double getTracerSum(String nuclideName, String location) {
		return  nuclideBottleDao.getTracerSum(nuclideName, location);
	}

	@Override
	public Map<String, String> getUsersForTracer(int tracerId) {
		return  nuclideBottleDao.getUsersForTracer(tracerId);
	}


	public NuclideBottleDao getNuclideBottleDao() {
		return nuclideBottleDao;
	}

	public void setNuclideBottleDao(NuclideBottleDao nuclideBottleDao) {
		this.nuclideBottleDao = nuclideBottleDao;
	}
	
	public NuclideUsageService getNuclideUsageService() {
		return nuclideUsageService;
	}

	public void setNuclideUsageService(NuclideUsageService nuclideUsageService) {
		this.nuclideUsageService = nuclideUsageService;
	}

	@Override
	public Object getTargetedObject(String query) {
		return nuclideBottleDao.getTargetedObject(query);
	}

	@Override	
	public List<TracerHierarchy> getTracerHierarchyList(String isotope, List<TracerOverview> tracerList){
		List<TracerHierarchy> hierarchyList = new ArrayList<TracerHierarchy>();
		for(TracerOverview parentTracer : tracerList) {
			TracerHierarchy th = new TracerHierarchy();
			hierarchyList.add(th);
			th.setNuclideName(isotope);
			if(isotope == null) th.setNuclideName(parentTracer.getNuclideName());
			th.setNuclideId(parentTracer.getTracerId());
			th.setInitialAmount(parentTracer.getInitialAmount().doubleValue());
			th.setLocation(parentTracer.getLocation());
			th.setState(parentTracer.getSolidLiquidState().charAt(0));
			th.setSubstanceName(parentTracer.getSubstance());
			List<NuclideUsage> usages = nuclideUsageService.findByNuclideBottleId(parentTracer.getTracerId());
			Double usageAmount = 0.0;
			List<Integer> usageIds = new ArrayList<Integer>();
			for(NuclideUsage usage : usages) {
				usageAmount += usage.getVolume();
				usageIds.add(usage.getNewNuclideBottleId());
			}			
			th.setChildrenAmount(usageAmount);
			Double amountPercentage = (100 - ((usageAmount * 100)/th.getInitialAmount()));
			th.setAmountPercentage(amountPercentage.intValue());
			boolean discarded = parentTracer.getDiscardDate() != null || (parentTracer.getDisposalDate() != null && !"".equals(parentTracer.getDisposalDate()));
			th.setDiscarded(discarded);
			if(!usageIds.isEmpty()) {
				processChildren(th, isotope, usageIds);
			}
			boolean externalUsage = TracerTypeEnum.EXTERNAL.getTracerTypeId() == parentTracer.getTracerTypeId();
			th.setExternalUsage(externalUsage);
		}
		
		Collections.sort(hierarchyList, new Comparator<TracerHierarchy>() {
			@Override
			public int compare(TracerHierarchy h1, TracerHierarchy h2) {
				return h2.getNuclideId().compareTo(h1.getNuclideId());
			}
		});
		
		return hierarchyList;
	}

	private void processChildren(TracerHierarchy parent, String isotope, List<Integer> usageIds) {
		Set<TracerHierarchy> children = new HashSet<TracerHierarchy>();
		parent.setNuclideChildrenSet(children);
		usageIds.removeAll(Collections.singleton(null));
		for(int usageId : usageIds) {
			NuclideBottle tracer = findById(usageId);
			if(tracer!= null) {
				TracerHierarchy subTh = new TracerHierarchy();
				children.add(subTh);
				subTh.setNuclideName(isotope);
				if(isotope == null) subTh.setNuclideName(tracer.getNuclide().getNuclideName());
				subTh.setNuclideId(usageId);
				subTh.setInitialAmount(tracer.getVolume().doubleValue());
				subTh.setLocation(tracer.getLocation());
				subTh.setState(tracer.getIsLiquid());
				subTh.setSubstanceName(tracer.getSubstanceName());
				List<NuclideUsage> subUsages = nuclideUsageService.findByNuclideBottleId(tracer.getNuclideBottleId());
				Double tracerAmount = 0.0;
				List<Integer> subUsageIds = new ArrayList<Integer>();
				for(NuclideUsage subUsage : subUsages) {
					tracerAmount += subUsage.getVolume();
					subUsageIds.add(subUsage.getNewNuclideBottleId());
				}			
				subTh.setChildrenAmount(tracerAmount);
				Double usageAmountPercentage = (100 - ((tracerAmount * 100)/subTh.getInitialAmount()));
				subTh.setAmountPercentage(usageAmountPercentage.intValue());
				boolean discarded = tracer.getDisposalDate() != null;
				subTh.setDiscarded(discarded);
				boolean externalUsage = TracerTypeEnum.EXTERNAL.getTracerTypeId() == tracer.getTracerType().getTracerTypeId();
				subTh.setExternalUsage(externalUsage);
				if(!subUsageIds.isEmpty()) {
					processChildren(subTh,isotope, subUsageIds);
				}
			}
		}
	}
	
}
