package org.research.kadda.nuclide.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.research.kadda.nuclide.TracerTypeEnum;
import org.research.kadda.nuclide.models.LabOverview;
import org.research.kadda.nuclide.models.LocationAllowance;
import org.research.kadda.nuclide.models.TracerOverview;
import org.research.kadda.nuclide.models.WasteOverview;
import org.research.kadda.nuclide.service.NuclideBottleService;
import org.research.kadda.nuclide.service.NuclideLocationService;
import org.research.kadda.nuclide.service.NuclideUsageService;
import org.research.kadda.nuclide.service.NuclideWasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("session")
public class NuclideLabController {

	@Autowired
	private NuclideLocationService nuclideLocationService;
	@Autowired
	private NuclideWasteService nuclideWasteService;
	@Autowired
	private NuclideBottleService nuclideBottleService;
	@Autowired
	private NuclideUsageService nuclideUsageService;

	private List<WasteOverview> openWastes;
	private List<WasteOverview> closedWastes;
	private List<TracerOverview> tracers;
	
	private Map<Integer, LabOverview> idLabMap = new HashMap<Integer, LabOverview>();
	
	private final static String ANY = "any";

	@RequestMapping("/listLabs")
	public ModelAndView listLabs(LabOverview labOverview, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/listLabs");
		List<LabOverview> labOverviewList = new ArrayList<LabOverview>();

		List<LocationAllowance> locationLabs = nuclideLocationService.findAllLocationAllowances();
		Map<String, Integer> labActivityThresholdMap = getLabActivityThresholdMap(locationLabs);
		
		openWastes = nuclideWasteService.findWasteByParams(null, "open", null);
		proceedWithWastesActivities(openWastes);
		Map<String, Integer> openWasteActivityLocationMap = getLocationWastesActivityMap(openWastes);
		closedWastes = nuclideWasteService.findWasteByParams(null, "closed", null);
		proceedWithWastesActivities(closedWastes);
		Map<String, Integer> closedWasteActivityLocationMap = getLocationWastesActivityMap(closedWastes);

		tracers = nuclideBottleService.findNuclideBottleByParam(ANY, ANY, ANY, ANY, ANY, ANY, ANY, null);
		proceedWithTracersActivities(tracers);
		Map<String, Integer> tracersActivityLocationMap = getLocationTracersActivityMap(tracers);

		Collections.sort(locationLabs, Comparator.comparing(LocationAllowance::getLocation));

		for (LocationAllowance locationAllowance : locationLabs) {
			String lab = locationAllowance.getLocation();
			String key = locationAllowance.getNuclide() + "_" + locationAllowance.getLocation();
			LabOverview labOvw = new LabOverview();
			labOvw.setLocationName(lab);
			labOvw.setNuclide(locationAllowance.getNuclide());
			int activityThreshold = labActivityThresholdMap.get(key.replaceAll("\\s", ""));
			labOvw.setActivityThreshold(String.valueOf(activityThreshold));
			int openWastesActivity = 0;
			if (openWasteActivityLocationMap.get(key.replaceAll("\\s", "")) != null) {
				openWastesActivity = openWasteActivityLocationMap.get(key.replaceAll("\\s", ""));
			}
			int closedWastesActivity = 0;
			if (closedWasteActivityLocationMap.get(key.replaceAll("\\s", "")) != null) {
				closedWastesActivity = closedWasteActivityLocationMap.get(key.replaceAll("\\s", ""));
			}
			int totalWastesActivity = openWastesActivity + closedWastesActivity;
			labOvw.setOpenWastesActivity(String.valueOf(openWastesActivity));
			labOvw.setClosedWastesActivity(String.valueOf(closedWastesActivity));
			labOvw.setWastesActivity(String.valueOf(totalWastesActivity));
			int tracersActivity = 0;
			if (tracersActivityLocationMap.get(key.replaceAll("\\s", "")) != null) {
				tracersActivity = tracersActivityLocationMap.get(key.replaceAll("\\s", ""));
			}
			labOvw.setTracersActivity(String.valueOf(tracersActivity));
			int totalActivity = totalWastesActivity + tracersActivity;
			labOvw.setTotalActivity(String.valueOf(totalActivity));
			labOvw.setHightlight(activityThreshold < totalActivity ? "Y" : "N");
			labOverviewList.add(labOvw);
		}
		
		for(int i =0; i <labOverviewList.size(); i++) {
			labOverviewList.get(i).setId(i);
			idLabMap.put(i, labOverviewList.get(i));
		}
		
		mv.addObject("labOverviewList", labOverviewList);
		return mv;
	}

	@RequestMapping(value = "/labDetail_{id}", method = RequestMethod.GET)
	public ModelAndView displayDetailLab(@PathVariable Integer id) {
		ModelAndView mv = new ModelAndView("/labDetail");
		LabOverview selectedLab = idLabMap.get(id);
		List<WasteOverview> wOL = new ArrayList<WasteOverview>();
		List<TracerOverview> tOL = new ArrayList<TracerOverview>();
		for(WasteOverview wo : openWastes) {
			if(wo.getLocation().equals(selectedLab.getLocationName()) && wo.getNuclideName().equals(selectedLab.getNuclide())) {
				wo.setStatus("Open");
				wOL.add(wo);
			}
		}
		for(WasteOverview wo : closedWastes) {
			if(wo.getLocation().equals(selectedLab.getLocationName()) && wo.getNuclideName().equals(selectedLab.getNuclide())) {
				wo.setStatus("Closed");
				wOL.add(wo);
			}
		}
		Collections.sort(wOL, new Comparator<WasteOverview>() {
			@Override
			public int compare(WasteOverview w1, WasteOverview w2) {
				return w1.getStatus().compareTo(w2.getStatus());
			}
		});
		selectedLab.setWastesList(wOL);
		
		for(TracerOverview to : tracers) {
			if(to.getLocation().equals(selectedLab.getLocationName()) && to.getNuclideName().equals(selectedLab.getNuclide())) {
				boolean disarded = to.getDiscardDate() != null || to.getDisposalDate() != null; 
				if(!disarded) {tOL.add(to);}
			}
		}
		selectedLab.setTracersList(tOL);
		
		mv.addObject("selectedLab", selectedLab);
		
		return mv;
	}

	private void proceedWithWastesActivities(List<WasteOverview> wastes) {
		for (WasteOverview nuclideWaste : wastes) {
			Integer totalTracersInWasteActivity = 0;
			Map<String,String> currentActivityKBqLEMap = nuclideUsageService.getCurrentActivityKBqLEMap(nuclideWaste.getSolidLiquidState().charAt(0), nuclideWaste.getNuclideWasteId());
			for(Entry<String,String> entry : currentActivityKBqLEMap.entrySet()) {
				if(entry.getKey()!=null) {
					totalTracersInWasteActivity = Double.valueOf(entry.getKey()).intValue();
				}
				break;
			}
			//Integer totalTracersInWasteActivity = nuclideWasteService.getWasteTracersActivity(nuclideWaste);
			nuclideWaste.setActivityKbq(String.valueOf(totalTracersInWasteActivity));
		}
	}

	private void proceedWithTracersActivities(List<TracerOverview> tracers) {
		for (TracerOverview tracerOvw : tracers) {
			Double currentAmount = tracerOvw.getCurrentAmount() != null ? Double.valueOf(tracerOvw.getCurrentAmount())
					: null;
			if (currentAmount.intValue() < 0 || tracerOvw.getDisposalDate() != null || tracerOvw.getTracerTypeId() == TracerTypeEnum.EXTERNAL.getTracerTypeId()) {
				currentAmount = 0.0;
			}
			Double currentActivity = tracerOvw.getCurrentActivity() != null
					? Double.valueOf(tracerOvw.getCurrentActivity())
					: null;
			Double initialAmount = tracerOvw.getInitialAmount() != null ? tracerOvw.getInitialAmount().doubleValue()
					: null;
			BigDecimal initialActivity = tracerOvw.getInitialActivity() != null
					? tracerOvw.getInitialActivity().setScale(0, RoundingMode.HALF_UP)
					: null;
			Double calculatedCurrenActivity;
			if (currentAmount == 0.0 && initialAmount == 0.0) {
				calculatedCurrenActivity = 0.0;
			} else {
				calculatedCurrenActivity = (currentActivity.doubleValue() * currentAmount.doubleValue()) / initialAmount.doubleValue();
				if(tracerOvw.getBatchName() != null && TracerTypeEnum.INVIVO.getTracerTypeId() == tracerOvw.getTracerTypeId() && tracerOvw.getSolideWastePercentage() != null) {
					calculatedCurrenActivity -= (currentActivity.doubleValue() * (tracerOvw.getSolideWastePercentage() / 100));
				}
			}
			BigDecimal currActivity = new BigDecimal(calculatedCurrenActivity).setScale(0, RoundingMode.HALF_UP);
			if (tracerOvw.getDisposalDate() != null) {
				tracerOvw.setCurrentActivity(String.valueOf(0));
			} else {
				tracerOvw.setCurrentActivity(currActivity.toString());
			}
			BigDecimal currAmount = new BigDecimal(currentAmount).setScale(0, RoundingMode.HALF_UP);
			tracerOvw.setCurrentAmount(currAmount.toString());
			tracerOvw.setInitialActivity(initialActivity);
			tracerOvw.setInitialAmount(new BigDecimal(initialAmount).setScale(0, RoundingMode.HALF_UP));
		}
	}
	
	private Map<String, Integer> getLabActivityThresholdMap(List<LocationAllowance> labs) {
		Map<String, Integer> returnedMap = new HashMap<String, Integer>();
		for(LocationAllowance lab : labs) {
			String key = lab.getNuclide() + "_"  + lab.getLocation().replaceAll("\\s","");
			returnedMap.put(key, (int) lab.getActivityThreshold());
		}
		return returnedMap;
	}

	private Map<String, Integer> getLocationWastesActivityMap(List<WasteOverview> wastes) {
		Map<String, Integer> returnedMap = new HashMap<String, Integer>();
		for (WasteOverview waste : wastes) {
			String key = waste.getNuclideName() + "_" + waste.getLocation().replaceAll("\\s",""); 
			Double activity = Double.valueOf(waste.getActivityKbq());
			if (returnedMap.get(key) != null) {
				activity += returnedMap.get(key);
			}
			returnedMap.put(key, activity.intValue());
		}
		return returnedMap;
	}

	private Map<String, Integer> getLocationTracersActivityMap(List<TracerOverview> tracers) {
		Map<String, Integer> returnedMap = new HashMap<String, Integer>();
		for(TracerOverview tracer : tracers) {
			String key = tracer.getNuclideName() + "_" + tracer.getLocation().replaceAll("\\s","");
			Integer activity = Integer.valueOf(tracer.getCurrentActivity());
			if (returnedMap.get(key) != null) {
				activity += returnedMap.get(key);
			}
			returnedMap.put(key, activity);
		}
		return returnedMap;
	}
}
