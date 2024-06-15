package org.research.kadda.nuclide.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.research.kadda.nuclide.entity.Nuclide;
import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.entity.NuclideLocation;
import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.entity.NuclideWaste;
import org.research.kadda.nuclide.models.DisposalRouteActivitySum;
import org.research.kadda.nuclide.models.Usage;
import org.research.kadda.nuclide.models.WasteContainer;
import org.research.kadda.nuclide.models.WasteOverview;
import org.research.kadda.nuclide.service.DateUtils;
import org.research.kadda.nuclide.service.NuclideBottleService;
import org.research.kadda.nuclide.service.NuclideLocationService;
import org.research.kadda.nuclide.service.NuclideService;
import org.research.kadda.nuclide.service.NuclideUsageService;
import org.research.kadda.nuclide.service.NuclideUserService;
import org.research.kadda.nuclide.service.NuclideWasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NuclideWasteController {

	@Autowired
	private NuclideWasteService nuclideWasteService;

	@Autowired
	private NuclideService nuclideService;

	@Autowired
	private NuclideLocationService nuclideLocationService;
	
	@Autowired
	private NuclideUserService nuclideUserService;	
	
	@Autowired
	private NuclideUsageService nuclideUsageService;
	
	@Autowired
	private NuclideBottleService nuclideBottleService;
	
	@Autowired
	private DateUtils dateUtils;
	
	@RequestMapping("/newWaste")
	public String newWaste(@Valid WasteContainer wasteContainer, BindingResult bindingResult, Model model) {
		model.addAttribute("wasteContainer", new WasteContainer());
		return "newWaste";
	}

	private int nuclideWasteId;
	
	private final static String DATE_PATTERN = "yyyy-MMM-dd";
	
	@RequestMapping(value = { "/addNewWaste" }, method = RequestMethod.POST)
	public ModelAndView addNewWaste(WasteContainer wasteContainer, BindingResult result, Model model) {
		ModelAndView mv = new ModelAndView("/insertWaste");
		NuclideWaste wasteToSave = new NuclideWaste();
		wasteToSave.setNuclide(nuclideService.findByName(wasteContainer.getNuclideName()));
		wasteToSave.setIsLiquid(wasteContainer.getState());
		wasteToSave.setLocation(wasteContainer.getLocation());
		wasteToSave.setNuclideUserByCreationUserId(nuclideUserService.findByUserId(wasteContainer.getUserId()));
		
		if (nuclideUserService.findByUserId(wasteContainer.getUserId()) == null) {
			ModelAndView cannotChange = new ModelAndView("redirect:/errorCannotChange");
			return cannotChange;
		}
		
		if(!nuclideWasteService.save(wasteToSave)) {
			ModelAndView warning = new ModelAndView("redirect:/warning");
			return warning;
		}
		model.addAttribute("newWasteSaveSuccess", "Your waste container was successfully registered (waste number  <a href='/nuclideDB/wasteDetail_"+wasteToSave.getNuclideWasteId()+"' class='btn3' id='wasteIdRecorded'>" + wasteToSave.getNuclideWasteId() + "</a>).");
		return mv;
	}	
	
	@RequestMapping("/showWaste")
	public String showWaste(WasteOverview wasteOverview, Model model) {
		model.addAttribute("wasteOverview", wasteOverview);
		return "showWaste";
	}

	@RequestMapping("/insertWaste")
	public String insertWaste(NuclideWaste wasteToSave, Model model) {
		return "insertWaste";
	}
	

	@RequestMapping("/listWaste")
	public String listWaste(WasteOverview wasteOverview, Model model) {
		String nuclideName = wasteOverview.getNuclideName();
		model.addAttribute("nuclideName", nuclideName);
		String openStatus = wasteOverview.getClosedOn();
		model.addAttribute("openStatus", openStatus);
		String showCurrentActivity = null;
		String isLiquid = wasteOverview.getSolidLiquidState();
		model.addAttribute("isLiquid", isLiquid);

		if(openStatus == null){
			return "showWaste";
		}

		boolean isCurrentActivity = true;
		switch (openStatus) {
		case "open":
			showCurrentActivity = "O"; 
			break;
		case "closed":
			showCurrentActivity = "C";
			break;
		case "discarded":
			showCurrentActivity = "D";
			isCurrentActivity = false;
			break;
		default:
			showCurrentActivity = "CD";
			isCurrentActivity = false;
		}		
		model.addAttribute("showCurrentActivity", showCurrentActivity);
		String liquid = String.valueOf(wasteOverview.getSolidLiquidState());
		List<WasteOverview> wastes = nuclideWasteService.findWasteByParams(nuclideName, openStatus, liquid);
		String activityKbq = null;
		if(isCurrentActivity) {
			for(WasteOverview nuclideWaste : wastes) {
				Integer totalTracersInWasteActivity = 0;
				Map<String,String> currentActivityKBqLEMap = nuclideUsageService.getCurrentActivityKBqLEMap(nuclideWaste.getSolidLiquidState().charAt(0), nuclideWaste.getNuclideWasteId());
				for(Entry<String,String> entry : currentActivityKBqLEMap.entrySet()) {
					if(entry.getKey()!=null) {
						totalTracersInWasteActivity = Double.valueOf(entry.getKey()).intValue();
					}
					break;
				}
				//Integer totalTracersInWasteActivity = nuclideWasteService.getWasteTracersActivity(nuclideWaste);
				int allowance = nuclideWaste.getDisposalLimit();
				nuclideWaste.setHightlight("N");
				if(totalTracersInWasteActivity != 0) {
					if(totalTracersInWasteActivity > allowance) {nuclideWaste.setHightlight("Y");}
					activityKbq = String.valueOf(totalTracersInWasteActivity);
				}else {
					activityKbq = "0";
				}
				nuclideWaste.setActivityKbq(activityKbq);
				formatDates(nuclideWaste);
			}
		}else {
			for(WasteOverview nuclideWaste : wastes) {
				if(nuclideWaste.getActivityKbq() != null) {					
					double act = (double)((long)(1000*Double.parseDouble(nuclideWaste.getActivityKbq())))/1000;
					double all = Double.valueOf(nuclideWaste.getDisposalLimit());
					if (act > all) {
						nuclideWaste.setHightlight("Y");
					}
					activityKbq = String.valueOf(new BigDecimal(act).setScale(0,RoundingMode.HALF_UP));
				}else {
					activityKbq = "0";
				}
				nuclideWaste.setActivityKbq(activityKbq);
				formatDates(nuclideWaste);
			}
		}
		Collections.sort(wastes, new Comparator<WasteOverview>() {
			@Override
			public int compare(WasteOverview w1, WasteOverview w2) {
				return w2.getNuclideWasteId() - w1.getNuclideWasteId();
			}
		});
		
		model.addAttribute("wastes", wastes);
		
		Map<String, Integer> disposalRouteActivitySumMap = nuclideWasteService.findDisposalRouteActivitySum(nuclideName, openStatus, liquid);
		disposalRouteActivitySumMap.values().removeAll(Collections.singleton(null));
		
		List<DisposalRouteActivitySum> disposalRouteActivitySumList = new ArrayList<DisposalRouteActivitySum>();
		for(Entry<String, Integer> entry : disposalRouteActivitySumMap.entrySet()) {
			DisposalRouteActivitySum disposalRouteActivitySum = new DisposalRouteActivitySum(entry.getKey(), entry.getValue());
			disposalRouteActivitySumList.add(disposalRouteActivitySum);
		}
		
		model.addAttribute("isCurrentActivity", isCurrentActivity? "Y" : "N");
		model.addAttribute("disposalRouteActivitySumList", disposalRouteActivitySumList);
		
		int totalActivityIntoWastes = 0;
		for(WasteOverview waste : wastes) {
			if(waste.getActivityKbq() != null) {
				totalActivityIntoWastes = totalActivityIntoWastes + Integer.valueOf(waste.getActivityKbq());
			}
		}
		model.addAttribute("totalActivityIntoWastes", totalActivityIntoWastes);
		
		return "listWaste";
	}

	private void formatDates(WasteOverview nuclideWaste) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(nuclideWaste.getClosedOn() != null) {
			try {
				Date closureDate = format.parse(nuclideWaste.getClosedOn());
				nuclideWaste.setClosedOn(new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(closureDate));
			} catch (ParseException e) {
				e.getMessage();
			}	
		}
		if(nuclideWaste.getDisposedOfOn() != null) {
			try {
				Date disposalDate = format.parse(nuclideWaste.getDisposedOfOn());
				nuclideWaste.setDisposedOfOn(new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(disposalDate));
			} catch (ParseException e) {
				e.getMessage();
			}	
		}
	}

	@RequestMapping(value = "/wasteDetail_{nuclideWasteId}", method = RequestMethod.GET)
	public ModelAndView displayDetailWaste(@PathVariable Long nuclideWasteId) {
		ModelAndView mv = new ModelAndView("/wasteDetail");
		NuclideWaste nuclideWaste = nuclideWasteService.findWasteById(nuclideWasteId.intValue());
		//keys: creationUserId / closureUserId / disposalUserId
		Map<String, String> relatedWasteUsers = nuclideWasteService.getUsersForWaste(nuclideWasteId.intValue());
		NuclideUser creationUser = nuclideUserService.findByUserId(relatedWasteUsers.get("creationUserId"));
		NuclideUser closureUser = null;
		NuclideUser disposalUser = null;
		if(relatedWasteUsers.get("closureUserId") != null) {
			closureUser = nuclideUserService.findByUserId(relatedWasteUsers.get("closureUserId"));
		}
		if(relatedWasteUsers.get("disposalUserId") != null) {
			disposalUser = nuclideUserService.findByUserId(relatedWasteUsers.get("disposalUserId"));
		}				
		
		WasteOverview waste = new WasteOverview();
		waste.setNuclideWasteId(nuclideWaste.getNuclideWasteId());
		waste.setNuclideName(nuclideWaste.getNuclide().getNuclideName());
		waste.setSolidLiquidState(nuclideWaste.getIsLiquid() == 'Y'? "liquid" : "solid");
		waste.setLocation(nuclideWaste.getLocation());
		waste.setNuclideUserByCreationUserId(creationUser.getFirstName() + " " + creationUser.getLastName());
		waste.setNuclideUserByClosureUserId(closureUser != null ? closureUser.getFirstName() + " " + closureUser.getLastName() : "N/A");
		waste.setClosedOn(nuclideWaste.getClosureDate() != null ? new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(nuclideWaste.getClosureDate()) : "not closed yet");
		waste.setNuclideUserByDisposalUserId(disposalUser != null ? disposalUser.getFirstName() + " " + disposalUser.getLastName() : "N/A");
		waste.setDisposalRoute(nuclideWaste.getDisposalRoute() != null ? nuclideWaste.getDisposalRoute() : "N/A" );
		waste.setActivityKbq(nuclideWaste.getDisposalActivity() != null ? nuclideWaste.getDisposalActivity().toString() : "N/A" );
		waste.setDisposedOfOn(nuclideWaste.getDisposalDate() != null ? new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(nuclideWaste.getDisposalDate()) : "not disposed of yet");
		mv.addObject("waste", waste);
		
		NuclideWaste nWaste = nuclideWasteService.findWasteById(Integer.valueOf(waste.getNuclideWasteId()));
		boolean isLiquideWaste = nWaste != null ? nWaste.getIsLiquid() == 'Y' : false;
		
		List<Usage> wasteUsageList = nuclideUsageService.getWasteUsageList(nuclideWaste.getNuclideWasteId(), nuclideWaste.getIsLiquid());
		for(Usage usage : wasteUsageList) {
			boolean isInVivo = "In-Vivo Experiment".equals(usage.getAssayType());
			Double initialAmount = usage.getAmount() != null? new BigDecimal(usage.getAmount()).setScale(0, RoundingMode.HALF_UP).doubleValue() : null;
			if(isInVivo) {
				usage.setTracerId(usage.getNewTracerId());
				initialAmount = nuclideBottleService.findById(usage.getTracerId()).getVolume();
			}else {				
				usage.setTracerId(usage.getTracerId());
			}
			
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date usageDate = format.parse(usage.getUsageDate());
				usage.setUsageDate(new SimpleDateFormat("yyyy-MMM-dd",Locale.US).format(usageDate));
			} catch (ParseException e) {
				e.getMessage();
			}
			//remove decimal
			
			Double amountToDisplay = null;
			if(isLiquideWaste) {
				amountToDisplay = initialAmount.doubleValue()*(100-usage.getSolideWastePercentage())/100;
				usage.setAmount(amountToDisplay != null ? new BigDecimal(amountToDisplay).setScale(0, RoundingMode.HALF_UP).toString():null);
			}else {
				amountToDisplay = initialAmount - (Double.valueOf(usage.getAmount())*(100-usage.getSolideWastePercentage())/100);
				if(isInVivo) {amountToDisplay = initialAmount - (Double.valueOf(initialAmount)*(100-usage.getSolideWastePercentage())/100);}
				usage.setAmount(amountToDisplay != null ? new BigDecimal(amountToDisplay).setScale(0, RoundingMode.HALF_UP).toString():null);
			}
		}
		mv.addObject("wasteUsageList", wasteUsageList);
		
		return mv;
	}

	
	//relocateWaste
	@RequestMapping(value = "/relocateWaste{nuclideWasteId}", method = RequestMethod.GET)
	public ModelAndView displayRelocateWaste(@PathVariable Long nuclideWasteId) {
		ModelAndView mv = new ModelAndView("/relocateWaste");
		NuclideWaste nuclideWaste = nuclideWasteService.findWasteById(nuclideWasteId.intValue());
		setNuclideWasteId(nuclideWasteId.intValue());
		mv.addObject("nuclideWaste", nuclideWaste);
		
		return mv;
	}
	
	@RequestMapping(value = "/relocateWaste{nuclideWasteId}", method = RequestMethod.POST)
	public ModelAndView relocateWaste(@ModelAttribute NuclideWaste waste, BindingResult result, HttpServletRequest request) {
		NuclideWaste wastToRelocate = nuclideWasteService.findWasteById(waste.getNuclideWasteId());
		wastToRelocate.setLocation(waste.getLocation());
		
		HttpSession session = request.getSession();
		boolean userIdfound = (String) session.getAttribute("username") != null && !StringUtils.isEmpty((String) session.getAttribute("username"));
		
		if (userIdfound && nuclideUserService.findByUserId(((String) session.getAttribute("username")).toUpperCase()) == null) {
			ModelAndView modv = new ModelAndView("redirect:/errorCannotChange");
			return modv;
		}
		
		if(!nuclideWasteService.save(wastToRelocate)) {
			ModelAndView mv = new ModelAndView("redirect:/warning");
			return mv;			
		}
		ModelAndView mv = new ModelAndView("redirect:/updateWasteLocation");
		return mv;
	}
	
	@RequestMapping("/updateWasteLocation")
	public String updateWasteLocation(NuclideWaste wasteToSave, Model model) {
		return "updateWasteLocation";
	}	
	
	//closeWaste
	
	@RequestMapping(value = "/closeWaste{nuclideWasteId}", method = RequestMethod.GET)
	public ModelAndView displayCloseWaste(@PathVariable Long nuclideWasteId) {
		ModelAndView mv = new ModelAndView("/closeWaste");
		NuclideWaste waste = nuclideWasteService.findWasteById(nuclideWasteId.intValue());
		List<Integer> disposedTracerIds = nuclideUsageService.getAllTracersInsideWaste(waste.getNuclideWasteId());
		List<NuclideBottle> disposedTracers = new ArrayList<NuclideBottle>();
		for(Integer i : disposedTracerIds) {
			disposedTracers.add(nuclideBottleService.findById(i));
		}
		Date lastDisposedTracerDate = dateUtils.getMostRecentDisposedTracerDate(disposedTracers);
		String mostRecentDisposedTracerDate = lastDisposedTracerDate != null ? new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(lastDisposedTracerDate) : "N/A";
		mv.addObject("mostRecentDisposedTracerDate", mostRecentDisposedTracerDate);
		mv.addObject("nuclideWaste", waste);
		String solidLiquidState = waste.getIsLiquid() == 'Y' ? "liquid":"solid";
		mv.addObject("solidLiquidState", solidLiquidState);
		NuclideUser registeredUser = nuclideUserService.findByUserId(waste.getNuclideUserByCreationUserId().getUserId());
		mv.addObject("registeredBy", registeredUser);
		
		return mv;
	}
	
	@RequestMapping(value = "/closeWaste{nuclideWasteId}", method = RequestMethod.POST)
	public ModelAndView closeWaste(@ModelAttribute NuclideWaste nuclideWaste, BindingResult result) {
		NuclideWaste wasteToClose = nuclideWasteService.findWasteById(nuclideWaste.getNuclideWasteId());
		try {
			DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
			Date closureDate = nuclideWaste.getClosureStrDate() != null ? format.parse(nuclideWaste.getClosureStrDate()) : new Date();
			wasteToClose.setClosureDate(closureDate);
		} catch (ParseException e) {
			e.getMessage();
		}
		NuclideUser closureUser = nuclideUserService.findByUserId(nuclideWaste.getNuclideUserByClosureUserId().getUserId());
		wasteToClose.setNuclideUserByClosureUserId(closureUser);
		
		if (nuclideUserService.findByUserId(closureUser.getUserId()) == null) {
			ModelAndView modv = new ModelAndView("redirect:/errorCannotChange");
			return modv;
		}
		
		if(!nuclideWasteService.save(wasteToClose)) {
			ModelAndView mv = new ModelAndView("redirect:/warning");
			return mv;
		}
		ModelAndView mv = new ModelAndView("redirect:/closeWasteSuccess");
		return mv;
	}
	
	@RequestMapping("/closeWasteSuccess")
	public String closeWasteSuccess(NuclideWaste wasteToSave, Model model) {
		return "closeWasteSuccess";
	}	
	
	//disposeWAste
	@RequestMapping(value = "/disposeWaste{nuclideWasteId}", method = RequestMethod.GET)
	public ModelAndView displayDisposeWaste(@PathVariable Long nuclideWasteId) {
		ModelAndView mv = new ModelAndView("/disposeWaste");
		NuclideWaste nuclideWaste = nuclideWasteService.findWasteById(nuclideWasteId.intValue());
		
		WasteOverview wasteToDisplay = new WasteOverview();
		wasteToDisplay.setNuclideWasteId(nuclideWaste.getNuclideWasteId());
		wasteToDisplay.setNuclideName(nuclideWaste.getNuclide().getNuclideName());
		wasteToDisplay.setSolidLiquidState(nuclideWaste.getIsLiquid() == 'Y'? "liquid": "solid");
		//keys: creationUserId / closureUserId 
		Map<String, String> relatedWasteUsers = nuclideWasteService.getUsersForWaste(nuclideWasteId.intValue());
		NuclideUser creationUser = nuclideUserService.findByUserId(relatedWasteUsers.get("creationUserId"));
		NuclideUser closureUser = null;
		if(relatedWasteUsers.get("closureUserId") != null) {
			closureUser = nuclideUserService.findByUserId(relatedWasteUsers.get("closureUserId"));
		}
		wasteToDisplay.setNuclideUserByCreationUserId(creationUser.getFirstName()+" "+creationUser.getLastName());
		wasteToDisplay.setNuclideUserByClosureUserId(closureUser != null? closureUser.getFirstName()+" "+closureUser.getLastName():null);
		wasteToDisplay.setClosedOn(new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(nuclideWaste.getClosureDate()));

		Map<String,String> currentActivityKBqLEMap = nuclideUsageService.getCurrentActivityKBqLEMap(nuclideWaste.getIsLiquid(), nuclideWaste.getNuclideWasteId());
		for(Entry<String,String> entry : currentActivityKBqLEMap.entrySet()) {
			BigDecimal currentActivityKbq = entry.getKey() != null ? new BigDecimal(entry.getKey()).setScale(0, RoundingMode.HALF_UP):null;
			if(currentActivityKbq != null)
				wasteToDisplay.setCurrentActivityKBq(currentActivityKbq.toString());
			BigDecimal currentActivityLE = entry.getKey() != null ? new BigDecimal(entry.getValue()).setScale(0, RoundingMode.HALF_UP):null;
			if(currentActivityLE != null)
				wasteToDisplay.setCurrentActivityLE(currentActivityLE.toString());
		}
		
		String activity = wasteToDisplay.getCurrentActivityKBq();
		Nuclide nuclide = nuclideService.findByName(wasteToDisplay.getNuclideName());
		BigDecimal allowance = nuclide.getDisposalLimit();
		wasteToDisplay.setHightlight("N");
		if(activity!=null && !StringUtils.isEmpty(activity)) {
			double act = (double)((long)(1000*Double.parseDouble(activity)))/1000;
			double all = allowance.doubleValue();
			if (act > all) {
				wasteToDisplay.setHightlight("Y");
			}
		}
		

		mv.addObject("wasteToDisplay", wasteToDisplay);
		
		return mv;
	}
	
	@RequestMapping(value = "/disposeWaste{nuclideWasteId}", method = RequestMethod.POST)
	public ModelAndView disposeWaste(@ModelAttribute WasteOverview wasteToDisplay, BindingResult result) {
		NuclideWaste wasteToDispose = nuclideWasteService.findWasteById(wasteToDisplay.getNuclideWasteId());
		try {
			DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
			Date disposalDate = wasteToDisplay.getDisposalStrDate() != null ? format.parse(wasteToDisplay.getDisposalStrDate()) : new Date();
			wasteToDispose.setDisposalDate(disposalDate);
		} catch (ParseException e) {
			e.getMessage();
		}
		NuclideUser disposalUser = nuclideUserService.findByUserId(wasteToDisplay.getNuclideUserByDisposalUserId());
		wasteToDispose.setNuclideUserByDisposalUserId(disposalUser);
		wasteToDispose.setDisposalActivity(
				wasteToDisplay.getCurrentActivityKBq() != null && !StringUtils.isEmpty(wasteToDisplay.getCurrentActivityKBq())
						? Double.valueOf(wasteToDisplay.getCurrentActivityKBq())
						: null);
		wasteToDispose.setDisposalRoute(wasteToDisplay.getDisposalRoute());
		
		if (nuclideUserService.findByUserId(disposalUser.getUserId()) == null) {
			ModelAndView modv = new ModelAndView("redirect:/errorCannotChange");
			return modv;
		}
		
		if(!nuclideWasteService.save(wasteToDispose)) {
			ModelAndView mv = new ModelAndView("redirect:/warning");
			return mv;
		}
		ModelAndView mv = new ModelAndView("redirect:/disposeWasteSuccess");
		return mv;
	}
	
	@RequestMapping("/disposeWasteSuccess")
	public String disposeWasteSuccess(NuclideWaste wasteToSave, Model model) {
		return "disposeWasteSuccess";
	}	
	
	
	@ModelAttribute("nuclideList")
	public Map<String, String> getNuclideList() {
		Map<String, String> nuclideList = new TreeMap<String, String>();
		for (Nuclide nuclide : nuclideService.findAll()) {
			nuclideList.put(nuclide.getNuclideName(), nuclide.getNuclideName());
		}
		return nuclideList;
	}

	@ModelAttribute("closureDateList")
	public Map<String, String> getClosureDateList() {
		Map<String, String> closureDateList = new LinkedHashMap<String, String>();
		final int year = Calendar.getInstance().get(Calendar.YEAR);		
		for (int i = year; i >= 2002; i--) {
			closureDateList.put(String.valueOf(i), "Discarded in " + String.valueOf(i));
		}
		return closureDateList;
	}
	
	@ModelAttribute("locationList")
	public Map<String, String> getLocationList() {
		Map<String, String> locationList = new TreeMap<String, String>();
		for (NuclideLocation nuclideLocation : nuclideLocationService.findAll()) {
			locationList.put(nuclideLocation.getLocation(), nuclideLocation.getLocation());
		}
		return locationList;
	}
	
	
	@ModelAttribute("disposalRouteList")
	public Map<String, String> getDisposalRouteList() {
		Map<String, String> disposalRouteList = new TreeMap<String, String>();
		for (String route : nuclideWasteService.getDisposalRouteList()) {
			disposalRouteList.put(route, route);
		}
		return disposalRouteList;
	}
	
	@ModelAttribute("today")
	public String getCurrentDate() {		
		String pattern = "yyyy-MMM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
		String today = simpleDateFormat.format(new Date());		
		return today;
	}	

	public int getNuclideWasteId() {
		return nuclideWasteId;
	}

	public void setNuclideWasteId(int nuclideWasteId) {
		this.nuclideWasteId = nuclideWasteId;
	}	
	
	
}
