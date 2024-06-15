package org.research.kadda.nuclide.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.research.kadda.nuclide.TracerTypeEnum;
import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.entity.NuclideLocation;
import org.research.kadda.nuclide.entity.NuclideTracerType;
import org.research.kadda.nuclide.entity.NuclideUsage;
import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.entity.NuclideWaste;
import org.research.kadda.nuclide.models.BiologicalUsage;
import org.research.kadda.nuclide.models.TracerHierarchy;
import org.research.kadda.nuclide.models.TracerOverview;
import org.research.kadda.nuclide.service.EmailService;
import org.research.kadda.nuclide.service.NuclideBottleService;
import org.research.kadda.nuclide.service.NuclideLocationService;
import org.research.kadda.nuclide.service.NuclideUsageService;
import org.research.kadda.nuclide.service.NuclideUserService;
import org.research.kadda.nuclide.service.NuclideWasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NuclideUsageController {

	@Autowired
	private NuclideUsageService nuclideUsageService;

	@Autowired
	private NuclideWasteService nuclideWasteService;

	@Autowired
	private NuclideBottleService nuclideBottleService;

	@Autowired
	private NuclideLocationService nuclideLocationService;

	@Autowired
	private NuclideUserService nuclideUserService;

	@Autowired
	private EmailService emailService;

	private int nuclideBottleId;
	private boolean tenPercentReached = false;
	private Map<String,String> tracersAmountLeftMap; 

	private final static String NOT_AVAILABLE = "N/A";
	private final static String DATE_PATTERN = "yyyy-MMM-dd";
	private final static String NUCLIDE_125I = "125I";
	private final static String NUCLIDE_32P = "32P";
	private final static String NUCLIDE_3H = "3H";
	private final static String NUCLIDE_14C = "14C";
	private final static String NUCLIDE_35S = "35S";
	private final static String NUCLIDE_33P = "33P";
	

	@RequestMapping("/newUsage")
	public String newUsage(@Valid BiologicalUsage biologicalUsage, BindingResult bindingResult, Model model) {
		processHierarchyTracers(model);
		model.addAttribute("tracerTubeAmountLeftMap", tracersAmountLeftMap);
		return "newUsage";
	}
	
	@RequestMapping("/newBiologicalTracer")
	public String newBiologicalTracer(@Valid BiologicalUsage biologicalUsage, BindingResult bindingResult,
			Model model) {
		model.addAttribute("biologicalUsage", new BiologicalUsage());
		return "newBiologicalTracer";
	}

	@RequestMapping("/newUsageMessage")
	public String newUsageMessage() {
		return "newUsageMessage";
	}

	private void processHierarchyTracers(Model model) {
		List<TracerOverview> orginal125iTracers = new ArrayList<TracerOverview>();
		List<TracerOverview> orginal32pTracers = new ArrayList<TracerOverview>();
		List<TracerOverview> orginal3hTracers = new ArrayList<TracerOverview>();
		List<TracerOverview> orginal14cTracers = new ArrayList<TracerOverview>();
		List<TracerOverview> orginal35sTracers = new ArrayList<TracerOverview>();
		List<TracerOverview> orginal33pTracers = new ArrayList<TracerOverview>();
		
		List<TracerOverview> allOriginalTracers = nuclideBottleService.findNuclideBottleByParam(null, null, null, "B", null, "original", null, null);
		List<TracerOverview> daughterTracersNotDisposed = nuclideBottleService.findNuclideBottleByParam(null, null, null, "N", null, "daughter", null, null);
		Map<Integer, List<TracerOverview>> parentIdChildTracersMap = new HashMap<Integer, List<TracerOverview>>();
		for(TracerOverview cto : daughterTracersNotDisposed) {
			if(parentIdChildTracersMap.get(cto.getParentId()) == null) {
				List<TracerOverview> tList = new ArrayList<TracerOverview>();
				tList.add(cto);
				parentIdChildTracersMap.put(cto.getParentId(), tList);
			}else {
				parentIdChildTracersMap.get(cto.getParentId()).add(cto);
			}
		}
		List<TracerOverview> originalTracers = new ArrayList<TracerOverview>();
		for(TracerOverview cto : allOriginalTracers) {
			if(cto.getDisposalDate() != null && !"".equals(cto.getDisposalDate())) {
				if(parentIdChildTracersMap.get(cto.getTracerId()) != null) originalTracers.addAll(parentIdChildTracersMap.get(cto.getTracerId()));
			}else {
				originalTracers.add(cto);
			}
		}
		/*Collections.sort(originalTracers, new Comparator<TracerOverview>() {
			@Override
			public int compare(TracerOverview to1, TracerOverview to2) {
				return Integer.valueOf(to1.getTracerId()).compareTo(Integer.valueOf(to2.getTracerId()));
			}
		});*/
		
		for(TracerOverview to : originalTracers) {
			if(to.getNuclideName().equals(NUCLIDE_125I)) {
				orginal125iTracers.add(to);
			}
			if(to.getNuclideName().equals(NUCLIDE_32P)) {
				orginal32pTracers.add(to);
			}
			if(to.getNuclideName().equals(NUCLIDE_3H)) {
				orginal3hTracers.add(to);
			}
			if(to.getNuclideName().equals(NUCLIDE_14C)) {
				orginal14cTracers.add(to);
			}
			if(to.getNuclideName().equals(NUCLIDE_35S)) {
				orginal35sTracers.add(to);
			}
			if(to.getNuclideName().equals(NUCLIDE_33P)) {
				orginal33pTracers.add(to);
			}
		}
		Map<String, String> tracerPercentageLeftMap = new HashMap<String, String>();
		
		List<TracerHierarchy> hierarchy14C = nuclideBottleService.getTracerHierarchyList(NUCLIDE_14C, orginal14cTracers);
		StringBuilder sbTree14c = new StringBuilder();
		nuclideUsageService.buildHtmlTree(hierarchy14C, sbTree14c,tracerPercentageLeftMap);
		model.addAttribute("tree14c", sbTree14c.toString());
		
		List<TracerHierarchy> hierarchy125i = nuclideBottleService.getTracerHierarchyList(NUCLIDE_125I, orginal125iTracers);
		StringBuilder sbTree125i = new StringBuilder();
		nuclideUsageService.buildHtmlTree(hierarchy125i, sbTree125i,tracerPercentageLeftMap);
		model.addAttribute("tree125i", sbTree125i.toString());
		
		List<TracerHierarchy> hierarchy32p = nuclideBottleService.getTracerHierarchyList(NUCLIDE_32P,orginal32pTracers);
		StringBuilder sbTree32p = new StringBuilder();
		nuclideUsageService.buildHtmlTree(hierarchy32p, sbTree32p,tracerPercentageLeftMap);
		model.addAttribute("tree32p", sbTree32p.toString());
		
		List<TracerHierarchy> hierarchy3h = nuclideBottleService.getTracerHierarchyList(NUCLIDE_3H,orginal3hTracers);
		StringBuilder sbTree3h = new StringBuilder();
		nuclideUsageService.buildHtmlTree(hierarchy3h, sbTree3h,tracerPercentageLeftMap);
		model.addAttribute("tree3h", sbTree3h.toString());
		
		List<TracerHierarchy> hierarchy35s = nuclideBottleService.getTracerHierarchyList(NUCLIDE_35S,orginal35sTracers);
		StringBuilder sbTree35s = new StringBuilder();
		nuclideUsageService.buildHtmlTree(hierarchy35s, sbTree35s,tracerPercentageLeftMap);
		model.addAttribute("tree35s", sbTree35s.toString());
		
		List<TracerHierarchy> hierarchy33p = nuclideBottleService.getTracerHierarchyList(NUCLIDE_33P,orginal33pTracers);
		StringBuilder sbTree33p = new StringBuilder();
		nuclideUsageService.buildHtmlTree(hierarchy33p, sbTree33p,tracerPercentageLeftMap);
		model.addAttribute("tree33p", sbTree33p.toString());
		
		model.addAttribute("tracerPercentageLeftMap",tracerPercentageLeftMap);
		
	}

//	@RequestMapping("/newBiologicalTracer")
//	public String newBiologicalTracer(@Valid BiologicalUsage biologicalUsage, BindingResult bindingResult,
//			Model model) {
//		model.addAttribute("biologicalUsage", new BiologicalUsage());
//		return "newBiologicalTracer";
//	}

	@RequestMapping("/error")
	public String errorMessage() {
		return "error";
	}

	@RequestMapping("/warning")
	public String warningMessage() {
		return "warning";
	}

	@RequestMapping(value = { "/addNewUsage" }, method = RequestMethod.POST)
	public ModelAndView addNewUsage(BiologicalUsage biologicalUsage, Model model, HttpServletRequest request,
			HttpServletResponse response) throws AddressException, MessagingException, IOException {
		ModelAndView mv = new ModelAndView("/newUsageMessage");
		NuclideUser curentNuclideUser = nuclideUserService.findByUserId(biologicalUsage.getNewUsageUserId());
		setNuclideBottleId(Integer.valueOf(biologicalUsage.getTracerTubeConcat()));
		NuclideBottle nb = nuclideBottleService.findByIdAndDisposalStatus(getNuclideBottleId());
		String unit = nb.getIsLiquid() == 'Y' ? "ul" : "mg";
		Double destAmount = biologicalUsage.getAmountTaken();
		Double sumVolume = nuclideUsageService.getSumVolumePerTracerId(getNuclideBottleId());
		Double amountTaken = sumVolume == null ? 0 : sumVolume;
		Double sourceAmount = nb.getVolume();
		if (sourceAmount - amountTaken < destAmount) {
			ModelAndView errorPage = new ModelAndView("error");
			Double valueToDisplay = sourceAmount - amountTaken >= 0 ?  sourceAmount - amountTaken : 0.0;
			errorPage.addObject("errorMessage", "There are only " + (valueToDisplay) + " " + unit
					+ " left. You requested " + destAmount + " " + unit + ".");
			return errorPage;
		}
		Collection<NuclideWaste> nwl = new ArrayList<NuclideWaste>();
		NuclideWaste liquidNW = null;
		NuclideWaste solidNW = null;
		if (!"-".equals(biologicalUsage.getLiquidWasteConcat())) {
			liquidNW = nuclideWasteService.findWasteById(Integer.valueOf(biologicalUsage.getLiquidWasteConcat()));
		}
		if (!"-".equals(biologicalUsage.getSolidWasteConcat())) {
			solidNW = nuclideWasteService.findWasteById(Integer.valueOf(biologicalUsage.getSolidWasteConcat()));
		}
		if ((liquidNW != null
				&& !liquidNW.getNuclide().getNuclideName().equalsIgnoreCase(nb.getNuclide().getNuclideName()))
				|| (solidNW != null
						&& !solidNW.getNuclide().getNuclideName().equalsIgnoreCase(nb.getNuclide().getNuclideName()))) {
			ModelAndView errorPage = new ModelAndView("error");
			errorPage.addObject("errorMessage",
					"The nuclide in one of your waste containers doesn't match the nuclide of the tracer.");
			return errorPage;
		} else {
			nwl.add(liquidNW);
			nwl.add(solidNW);
		}

		NuclideUsage usageToSave = new NuclideUsage();
		usageToSave.setVolume(biologicalUsage.getAmountTaken());
		usageToSave.setBioLabJournal(biologicalUsage.getBiologicalLabJournal());
		usageToSave.setAssayType(biologicalUsage.getAssayType());
		NuclideBottle parentTracer = nuclideBottleService.findById(getNuclideBottleId());
		try {
			DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
			// datepicker has not been used --> new date
			Date usageDate = (biologicalUsage.getRecordUsageDate() == null || "".equals(biologicalUsage.getRecordUsageDate())) ? new Date()
					: format.parse(biologicalUsage.getRecordUsageDate());
			usageToSave.setUsageDate(usageDate);
		} catch (ParseException e) {
			e.getMessage();
		}
		usageToSave.setNuclideUser(curentNuclideUser);
		usageToSave.setNuclideBottle(parentTracer);
		if (liquidNW != null) {
			usageToSave.setNuclideWasteByLiquidWasteId(
					nuclideWasteService.findWasteById(Integer.valueOf(biologicalUsage.getLiquidWasteConcat())));
		}
		usageToSave.setSolidWastePercentage(biologicalUsage.getSolidWastePercentage());
		double liquidWastePercentage = Double.valueOf(usageToSave.getSolidWastePercentage()) != null
				? (100 - usageToSave.getSolidWastePercentage())
				: 100;
		usageToSave.setLiquidWastePercentage(liquidWastePercentage);
		if (solidNW != null) {
			usageToSave.setNuclideWasteBySolidWasteId(
					nuclideWasteService.findWasteById(Integer.valueOf(biologicalUsage.getSolidWasteConcat())));
		}

		if (curentNuclideUser == null) {
			ModelAndView cannotChange = new ModelAndView("redirect:/errorCannotChange");
			return cannotChange;
		}
		tenPercentReached = false;
		if(parentTracer!=null && nuclideUsageService.isTenPercentLimitReached(parentTracer, usageToSave)) {
			tenPercentReached = true;
		}
		if (!nuclideUsageService.saveNuclide(usageToSave)) {
			ModelAndView warningPage = new ModelAndView("redirect:/warning");
			return warningPage;
		}

		//refresh parentTracer with updated current amount
		parentTracer = nuclideBottleService.findById(getNuclideBottleId());
		
		StringBuilder sb = new StringBuilder();
		sb.append("Your assay was successfully registered.");
		sb.append(".<br /><span id='newChildIdRecorded' style='display:none'></span>");

		nwl.removeAll(Collections.singleton(null));

		if(tenPercentReached) {
			boolean testMode = request.getRequestURL() != null && (request.getRequestURL().toString().contains("apollo") || request.getRequestURL().toString().contains("localhost"));
			emailService.processTenPercentReached(nb, parentTracer, sb, testMode);
		}
		// check for maximum allowance and display warning, if exceeded
		for (NuclideWaste nw : nwl) {
			Double tracerSum = nuclideBottleService.getTracerSum(nb.getNuclide().getNuclideName(), nw.getLocation());
			Double wasteSum = nuclideWasteService.getWasteSum(nb.getNuclide().getNuclideName(), nw.getLocation());
			wasteSum += nuclideWasteService.getComplementWasteSum(nb.getNuclide().getNuclideName(), nw.getLocation());
			Double allowance = nuclideLocationService.getAllowance(nb.getNuclide().getNuclideName(), nw.getLocation());
			if (tracerSum + wasteSum > allowance) {
				sb.append("<br /><font color=\"red\"><b> WARNING: The combined ")
				.append(nw.getNuclide().getNuclideName()).append(" activity of current tracers (")
				.append(tracerSum.intValue()).append(" kBq) and waste (").append(wasteSum.intValue())
				.append(" kBq) in ").append(nw.getLocation()).append(") exceeds the allowed limit of ")
				.append(allowance).append(" kBq.</b></font>");
				//send email to admin
				boolean testMode = request.getRequestURL() != null && !request.getRequestURL().toString().contains("ares");
				emailService.processRadiactivityLimitEmail(nb, tracerSum, allowance, testMode);
				break;
			}
		}
		try {
			if(liquidNW!=null) nuclideWasteService.checkIfFifityPercentActivityWasteReached(liquidNW, curentNuclideUser.getUserId(), request);
			if(solidNW!=null) nuclideWasteService.checkIfFifityPercentActivityWasteReached(solidNW, curentNuclideUser.getUserId(), request);
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
		
		biologicalUsage.setMessageToDisplay(sb.toString());
		//model.addAttribute("newUsageMessage", sb.toString());
		mv.addObject("newUsageMessage", sb.toString());
		//mv.addObject("treeId", biologicalUsage.getTreeNodeId());
		//mv.addObject("treeYPosition", biologicalUsage.getTreeYPosition());
		
		//processHierarchyTracers(model);
		
		return mv;
	}
	
	@RequestMapping("/newDaughter")
	public String newDaughter(@Valid BiologicalUsage biologicalUsage, BindingResult bindingResult, Model model) {
		model.addAttribute("biologicalUsage", new BiologicalUsage());
		return "newDaughter";
	}

	@RequestMapping(value = { "/addNewDaughter" }, method = RequestMethod.POST)
	public ModelAndView addNewDaughter(BiologicalUsage biologicalUsage, BindingResult result, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/newUsageMessage");
		NuclideUser curentNuclideUser = nuclideUserService.findByUserId(biologicalUsage.getNewUsageUserId());
		System.out.println("STACK --> " + curentNuclideUser.getLastName());
		setNuclideBottleId(Integer.valueOf(biologicalUsage.getTracerTubeConcat()));
		System.out.println("STACK --> " + biologicalUsage.getTracerTubeConcat());
		Double destAmount = biologicalUsage.getAmountTaken();
		Double sumVolume = nuclideUsageService.getSumVolumePerTracerId(getNuclideBottleId());
		System.out.println("STACK --> " + sumVolume);
		Double amountTaken = sumVolume == null ? 0 : sumVolume;
		NuclideBottle nb = nuclideBottleService.findById(getNuclideBottleId());
		System.out.println("STACK --> " + nb.getNuclideBottleId());
		Double sourceActivity = nb.getActivity();
		Double sourceAmount = nb.getVolume();
		String unit = nb.getIsLiquid() == 'Y' ? "ul" : "mg";
		if (sourceAmount - amountTaken < destAmount) {
			System.out.println("STACK --> sourceAmount - amountTaken < destAmount");
			ModelAndView errorPage = new ModelAndView("error");
			Double valueToDisplay = sourceAmount - amountTaken >= 0 ?  sourceAmount - amountTaken : 0.0;
			errorPage.addObject("errorMessage", "There are only " + (valueToDisplay) + " " + unit
					+ " left. You requested " + destAmount + " " + unit + ".");
			return errorPage;
		}

		NuclideUsage daughterUsageToSave = new NuclideUsage();
		daughterUsageToSave.setVolume(biologicalUsage.getAmountTaken());
		daughterUsageToSave.setBioLabJournal(biologicalUsage.getBiologicalLabJournal());
		NuclideBottle parentTracer = nuclideBottleService.findById(getNuclideBottleId());
		System.out.println("STACK --> " + parentTracer.getNuclideBottleId());
		try {
			DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
			// datepicker has not been used --> new date
			Date usageDate = (biologicalUsage.getRecordUsageDate() == null || "".equals(biologicalUsage.getRecordUsageDate())) ? new Date()
					: format.parse(biologicalUsage.getRecordUsageDate());
			daughterUsageToSave.setUsageDate(usageDate);
			System.out.println("STACK --> " + usageDate);
		} catch (ParseException e) {
			e.getMessage();
		}
		daughterUsageToSave.setBioLabJournal(
				biologicalUsage.getBiologicalLabJournal() != null ? biologicalUsage.getBiologicalLabJournal()
						: NOT_AVAILABLE);
		daughterUsageToSave.setNuclideUser(curentNuclideUser);
		daughterUsageToSave.setNuclideBottle(parentTracer);
		
		String location = biologicalUsage.getLocation() != null ? biologicalUsage.getLocation()
				: nb.getLocation();
		Double activity = (double) ((long) (1000 * sourceActivity * destAmount / sourceAmount)) / 1000;
		System.out.println("STACK --> " + activity);
		//save new tracer
		NuclideBottle associatedNuclideBottle = new NuclideBottle(nb.getNuclide(), curentNuclideUser, null,
				nb.getSubstanceName(), activity, biologicalUsage.getNewTotalVolume(), location, nb.getActivityDate(),
				null, nb.getIsLiquid(), biologicalUsage.getNewBatchName(), null, null);
		associatedNuclideBottle.setTracerType(new NuclideTracerType(TracerTypeEnum.DAUGHTER.getTracerTypeId(),
				TracerTypeEnum.DAUGHTER.getTracerTypeName()));
		nuclideBottleService.saveNuclideBottle(associatedNuclideBottle);
		System.out.println("STACK --> associatedNuclideBottle saved");
		int newNuclideBottleId = nuclideUsageService.getLastId();
		System.out.println("STACK --> " + newNuclideBottleId);
		daughterUsageToSave.setNewNuclideBottleId(newNuclideBottleId);
		daughterUsageToSave.setAssayType(biologicalUsage.getAssayType() != null ? biologicalUsage.getAssayType()
				: "'New Tracer Creation #" + newNuclideBottleId);
		if (nuclideUserService.findByUserId(curentNuclideUser.getUserId().toUpperCase()) == null) {
			System.out.println("STACK --> errorCannotChange");
			ModelAndView cannotChange = new ModelAndView("redirect:/errorCannotChange");
			return cannotChange;
		}
		tenPercentReached = false;
		if(parentTracer != null && nuclideUsageService.isTenPercentLimitReached(parentTracer, daughterUsageToSave)) {
			tenPercentReached = true;
		}
		
		//save new usage
		if (!nuclideUsageService.saveNuclide(daughterUsageToSave)) {
			System.out.println("STACK --> can't save");
			ModelAndView warning = new ModelAndView("redirect:/errorCannotChange");
			return warning;
		}
		//refresh parentTracer with updated current amount
		parentTracer = nuclideBottleService.findById(getNuclideBottleId());

		if(tenPercentReached) {
			System.out.println("STACK --> tenPercentReached");
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("Your Daughter Tracer Tube was successfully registered (usage number <a href='/nuclideDB/tracerDetail_" + daughterUsageToSave.getNewNuclideBottleId() + "' class='btn2' id='newDaughterIdRecorded'>"
						+ daughterUsageToSave.getNewNuclideBottleId() + "</a>).<br /><span id='newChildIdRecorded' style='display:none'>"+ daughterUsageToSave.getNewNuclideBottleId() + "</span> ");
				boolean testMode = request.getRequestURL() != null && !request.getRequestURL().toString().contains("ares");
				emailService.processTenPercentReached(nb, parentTracer, sb, testMode);
				model.addAttribute("newUsageMessage", sb.toString());
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			model.addAttribute("newUsageMessage",
					"Your Daughter Tracer Tube was successfully registered (usage number <a href='/nuclideDB/tracerDetail_" + daughterUsageToSave.getNewNuclideBottleId() + "' class='btn2' id='newDaughterIdRecorded'>"
							+ daughterUsageToSave.getNewNuclideBottleId() + "</a>).<br /><span id='newChildIdRecorded' style='display:none'>"+ daughterUsageToSave.getNewNuclideBottleId() + "</span> ");			
		}
		
		//mv.addObject("treeId", biologicalUsage.getTreeNodeId());
		//mv.addObject("treeYPosition", biologicalUsage.getTreeYPosition());
		//processHierarchyTracers(model);
		return mv;
	}

	@RequestMapping("/newExternal")
	public String newExternal(@Valid BiologicalUsage biologicalUsage, BindingResult bindingResult, Model model) {
		model.addAttribute("biologicalUsage", new BiologicalUsage());
		return "newExternal";
	}
	
	@RequestMapping(value = { "/addNewExternal" }, method = RequestMethod.POST)
	public ModelAndView addNewExternal(BiologicalUsage biologicalUsage, BindingResult result, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/newUsageMessage");
		NuclideUser curentNuclideUser = nuclideUserService.findByUserId(biologicalUsage.getNewUsageUserId());
		setNuclideBottleId(Integer.valueOf(biologicalUsage.getTracerTubeConcat()));
		NuclideBottle nb = nuclideBottleService.findByIdAndDisposalStatus(getNuclideBottleId());
		String unit = nb.getIsLiquid() == 'Y' ? "ul" : "mg";
		Double destAmount = biologicalUsage.getAmountTaken();
		Double sumVolume = nuclideUsageService.getSumVolumePerTracerId(getNuclideBottleId());
		Double amountTaken = sumVolume == null ? 0 : sumVolume;
		Double sourceAmount = nb.getVolume();
		if (sourceAmount - amountTaken < destAmount) {
			ModelAndView errorPage = new ModelAndView("error");
			Double valueToDisplay = sourceAmount - amountTaken >= 0 ?  sourceAmount - amountTaken : 0.0;
			errorPage.addObject("errorMessage", "There are only " + (valueToDisplay) + " " + unit
					+ " left. You requested " + destAmount + " " + unit + ".");
			return errorPage;
		}

		NuclideUsage externalUsageToSave = new NuclideUsage();
		externalUsageToSave.setVolume(biologicalUsage.getAmountTaken());
		externalUsageToSave.setBioLabJournal(biologicalUsage.getBiologicalLabJournal());
		externalUsageToSave
				.setAssayType(biologicalUsage.getAssayType() != null ? biologicalUsage.getAssayType() : NOT_AVAILABLE);
		NuclideBottle parentTracer = nuclideBottleService.findById(getNuclideBottleId());
		try {
			DateFormat format = new SimpleDateFormat(DATE_PATTERN,Locale.US);
			// datepicker has not been used --> new date
			Date usageDate = (biologicalUsage.getRecordUsageDate() == null || "".equals(biologicalUsage.getRecordUsageDate())) ? new Date()
					: format.parse(biologicalUsage.getRecordUsageDate());
			externalUsageToSave.setUsageDate(usageDate);
		} catch (ParseException e) {
			e.getMessage();
		}
		externalUsageToSave.setBioLabJournal(
				biologicalUsage.getBiologicalLabJournal() != null ? biologicalUsage.getBiologicalLabJournal()
						: NOT_AVAILABLE);
		externalUsageToSave.setDestination(biologicalUsage.getMaterialToSend());
		externalUsageToSave.setNuclideUser(curentNuclideUser);
		externalUsageToSave.setNuclideBottle(parentTracer);

		if (curentNuclideUser == null) {
			ModelAndView cannotChange = new ModelAndView("redirect:/errorCannotChange");
			return cannotChange;
		}

		Double externalTracerInitialActivity = (nb.getActivity() * externalUsageToSave.getVolume()) / nb.getVolume();
		NuclideBottle associatedNuclideBottle = new NuclideBottle(nb.getNuclide(), curentNuclideUser, null,
				nb.getSubstanceName(), externalTracerInitialActivity, externalUsageToSave.getVolume(),
				externalUsageToSave.getDestination(), nb.getActivityDate(), null, nb.getIsLiquid(), null, null, null);
		associatedNuclideBottle.setTracerType(new NuclideTracerType(TracerTypeEnum.EXTERNAL.getTracerTypeId(),
				TracerTypeEnum.EXTERNAL.getTracerTypeName()));
		nuclideBottleService.saveNuclideBottle(associatedNuclideBottle);

		int newNuclideBottleId = nuclideUsageService.getLastId();
		externalUsageToSave.setNewNuclideBottleId(newNuclideBottleId);
		tenPercentReached = false;
		if(parentTracer!=null && nuclideUsageService.isTenPercentLimitReached(parentTracer, externalUsageToSave)) {
			tenPercentReached = true;
		}
		
		if (!nuclideUsageService.saveNuclide(externalUsageToSave)) {
			ModelAndView warning = new ModelAndView("redirect:/warning");
			return warning;
		}
		//refresh parentTracer with updated current amount
		parentTracer = nuclideBottleService.findById(getNuclideBottleId());

		if(tenPercentReached) {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("Your Tracer to External Destination was successfully registered (usage number <a href='/nuclideDB/tracerDetail_" + externalUsageToSave.getNewNuclideBottleId() + "' class='btn2' id='newExternalIdRecorded'>"
						+ externalUsageToSave.getNewNuclideBottleId() + "</a>).<br /><span id='newChildIdRecorded' style='display:none'>"+ externalUsageToSave.getNewNuclideBottleId() + "</span> ");
				boolean testMode = request.getRequestURL() != null && (request.getRequestURL().toString().contains("apollo") || request.getRequestURL().toString().contains("localhost"));
				emailService.processTenPercentReached(nb, parentTracer, sb, testMode);
				model.addAttribute("newUsageMessage", sb.toString());
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			model.addAttribute("newUsageMessage",
					"Your Tracer to External Destination was successfully registered (usage number <a href='/nuclideDB/tracerDetail_" + externalUsageToSave.getNewNuclideBottleId() + "' class='btn2' id='newExternalIdRecorded'>"
							+ externalUsageToSave.getNewNuclideBottleId() + "</a>).<br /><span id='newChildIdRecorded' style='display:none'>"+ externalUsageToSave.getNewNuclideBottleId() + "</span> ");
		}
		
		//mv.addObject("treeId", biologicalUsage.getTreeNodeId());
		//mv.addObject("treeYPosition", biologicalUsage.getTreeYPosition());
		//processHierarchyTracers(model);

		return mv;
	}
	
	@RequestMapping("/newInVivo")
	public String newInVivo(@Valid BiologicalUsage biologicalUsage, BindingResult bindingResult, Model model) {
		model.addAttribute("biologicalUsage", new BiologicalUsage());
		return "newInVivo";
	}


	@RequestMapping(value = { "/addNewInVivo" }, method = RequestMethod.POST)
	public ModelAndView addNewInVivo(BiologicalUsage biologicalUsage, BindingResult result, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/newUsageMessage");
		NuclideUser curentNuclideUser = nuclideUserService.findByUserId(biologicalUsage.getNewUsageUserId());
		setNuclideBottleId(Integer.valueOf(biologicalUsage.getTracerTubeConcat()));
		NuclideBottle nb = nuclideBottleService.findByIdAndDisposalStatus(getNuclideBottleId());
		String unit = nb.getIsLiquid() == 'Y' ? "ul" : "mg";
		Double destAmount = biologicalUsage.getAmountTaken();
		Double sumVolume = nuclideUsageService.getSumVolumePerTracerId(getNuclideBottleId());
		Double amountTaken = sumVolume == null ? 0 : sumVolume;
		Double sourceActivity = nb.getActivity();
		Double sourceAmount = nb.getVolume();
		if (sourceAmount - amountTaken < destAmount) {
			ModelAndView errorPage = new ModelAndView("error");
			Double valueToDisplay = sourceAmount - amountTaken >= 0 ?  sourceAmount - amountTaken : 0.0;
			errorPage.addObject("errorMessage", "There are only " + (valueToDisplay) + " " + unit
					+ " left. You requested " + destAmount + " " + unit + ".");
			return errorPage;
		}

		NuclideUsage inVivoUsageToSave = new NuclideUsage();
		inVivoUsageToSave.setVolume(biologicalUsage.getAmountTaken());
		inVivoUsageToSave.setBioLabJournal(biologicalUsage.getBiologicalLabJournal());
		inVivoUsageToSave.setAssayType("In-Vivo Experiment");
		NuclideBottle parentTracer = nuclideBottleService.findById(getNuclideBottleId());
		try {
			DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
			// datepicker has not been used --> new date
			Date usageDate = (biologicalUsage.getRecordUsageDate() == null || "".equals(biologicalUsage.getRecordUsageDate())) ? new Date()
					: format.parse(biologicalUsage.getRecordUsageDate());
			inVivoUsageToSave.setUsageDate(usageDate);
		} catch (ParseException e) {
			e.getMessage();
		}
		inVivoUsageToSave.setBioLabJournal(
				biologicalUsage.getBiologicalLabJournal() != null ? biologicalUsage.getBiologicalLabJournal()
						: NOT_AVAILABLE);
		inVivoUsageToSave.setNuclideUser(curentNuclideUser);
		inVivoUsageToSave.setNuclideBottle(parentTracer);
		inVivoUsageToSave.setSolidWastePercentage(biologicalUsage.getSolidWastePercentage());
		NuclideWaste solidWaste = nuclideWasteService.findWasteById(Integer.valueOf(biologicalUsage.getSolidWasteConcat()));
		if (!biologicalUsage.getSolidWasteConcat().equals("-")) {
			inVivoUsageToSave.setNuclideWasteBySolidWasteId(solidWaste);
		}
		
		//save tracer
		String location = biologicalUsage.getLocation() != null ? biologicalUsage.getLocation()
				: nb.getLocation();

		Double activity = (double) ((long) (1000 * sourceActivity * destAmount / sourceAmount)) / 1000;
		Double currentAmount = biologicalUsage.getTotalSampleVolume();

		NuclideBottle associatedNuclideBottle = new NuclideBottle(nb.getNuclide(), curentNuclideUser, null,
				nb.getSubstanceName(), activity, currentAmount, location, nb.getActivityDate(), null, nb.getIsLiquid(),
				"In-Vivo-Sample " + biologicalUsage.getBiologicalLabJournal(), null, null);
		associatedNuclideBottle.setTracerType(new NuclideTracerType(TracerTypeEnum.INVIVO.getTracerTypeId(),
				TracerTypeEnum.INVIVO.getTracerTypeName()));
		nuclideBottleService.saveNuclideBottle(associatedNuclideBottle);
		tenPercentReached = false;
		if(parentTracer!=null && nuclideUsageService.isTenPercentLimitReached(parentTracer, inVivoUsageToSave)) {
			tenPercentReached = true;
		}
		
		//save usage
		int newNuclideBottleId = nuclideUsageService.getLastId();
		inVivoUsageToSave.setNewNuclideBottleId(newNuclideBottleId);
		inVivoUsageToSave.setDestination("new sample #" + newNuclideBottleId);
		if (!nuclideUsageService.saveNuclide(inVivoUsageToSave)) {
			ModelAndView warning = new ModelAndView("redirect:/warning");
			return warning;
		}
		
		//refresh parentTracer with updated current amount
		parentTracer = nuclideBottleService.findById(getNuclideBottleId());
		
		if(tenPercentReached) {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("Your In-Vivo Usage With Sample Creation was successfully registered (usage number <a href='/nuclideDB/tracerDetail_" + inVivoUsageToSave.getNewNuclideBottleId() + "' class='btn2' id='newinVivoIdRecorded'>"
						+ inVivoUsageToSave.getNewNuclideBottleId() + "</a>).<br /><span id='newChildIdRecorded' style='display:none'>"+ inVivoUsageToSave.getNewNuclideBottleId() + "</span> ");
				boolean testMode = request.getRequestURL() != null && (request.getRequestURL().toString().contains("apollo") || request.getRequestURL().toString().contains("localhost"));
				emailService.processTenPercentReached(nb, parentTracer, sb, testMode);
				
				model.addAttribute("newUsageMessage", sb.toString());
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			model.addAttribute("newUsageMessage",
					"Your In-Vivo Usage With Sample Creation was successfully registered (usage number <a href='/nuclideDB/tracerDetail_" + inVivoUsageToSave.getNewNuclideBottleId() + "' class='btn2' id='newinVivoIdRecorded'>"
							+ inVivoUsageToSave.getNewNuclideBottleId() + "</a>).<br /><span id='newChildIdRecorded' style='display:none'>"+ inVivoUsageToSave.getNewNuclideBottleId() + "</span> ");			
//			model.addAttribute("newUsageMessage",
//					"Your In-Vivo Usage With Sample Creation was successfully registered (usage number <a href='/nuclideDB/tracerDetail_" + inVivoUsageToSave.getNewNuclideBottleId() + "' class='btn2 newChildIdRecorded' id='newInVivoIdRecorded'>"
//							+ inVivoUsageToSave.getNewNuclideBottleId() + "</a>).<br /><span id='newChildIdRecorded' style='display:none'>"+ inVivoUsageToSave.getNewNuclideBottleId() + "</span> ");
		}

		try {
			if(solidWaste!=null) nuclideWasteService.checkIfFifityPercentActivityWasteReached(solidWaste, curentNuclideUser.getUserId(), request);
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
		
		//mv.addObject("treeId", biologicalUsage.getTreeNodeId());
		//mv.addObject("treeYPosition", biologicalUsage.getTreeYPosition());
		//processHierarchyTracers(model);
		
		return mv;
	}

	@RequestMapping("/insertUsage")
	public String insertUsage(NuclideUsage nuclideUsageToSave, Model model) {
		return "insertWaste";
	}

	@ModelAttribute("tracerTubeList")
	public Map<String, String> getTracerTubeList() {
		Map<Integer, String> tracerTubes = new TreeMap<Integer, String>(Collections.reverseOrder());
		List<NuclideBottle> nuclideBottleList = nuclideBottleService.findUsageTraceTubeList();
		for (NuclideBottle nuclideBottle : nuclideBottleList) {
			StringBuilder sb = new StringBuilder(nuclideBottle.getNuclideBottleId());
			setNuclideBottleId(nuclideBottle.getNuclideBottleId());
			sb.append(nuclideBottle.getNuclideBottleId());
			sb.append(" ");
			sb.append("(");
			sb.append(nuclideBottle.getNuclide().getNuclideName());
			sb.append("; ");
			sb.append(nuclideBottle.getSubstanceName());
			sb.append(")");
			tracerTubes.put(nuclideBottle.getNuclideBottleId(), sb.toString());
		}
		Map<String, String> tracerTubeList = new LinkedHashMap<String, String>();
		for(Entry<Integer, String> entry : tracerTubes.entrySet()) {
			tracerTubeList.put(String.valueOf(entry.getKey()), entry.getValue());
		}
		return tracerTubeList;
	}
		
	@ModelAttribute("tracerTubeDateMap")
	public Map<String, String> getTracerTubeDateMap() {
		Map<String, String> tracerTubeDateMap = new TreeMap<String, String>(Collections.reverseOrder());
		tracersAmountLeftMap = new TreeMap<String, String>(Collections.reverseOrder());
		List<NuclideBottle> nuclideBottleList = nuclideBottleService.findUsageTraceTubeList();
		for (NuclideBottle nuclideBottle : nuclideBottleList) {
			Date creationDateTracer = nuclideUsageService.getUsageDate(nuclideBottle.getNuclideBottleId());
			if (creationDateTracer == null) {
				creationDateTracer = nuclideBottle.getActivityDate();
			}
			tracerTubeDateMap.put(String.valueOf(nuclideBottle.getNuclideBottleId()),
					new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(creationDateTracer));
			
			boolean amountLeftDefined = TracerTypeEnum.EXTERNAL.getTracerTypeId() != nuclideBottle.getTracerType()
					.getTracerTypeId() && nuclideBottle.getDisposalDate() == null;
			if(amountLeftDefined) {
				Double amountLeft = nuclideBottle.getVolume() - nuclideBottle.getSumVolume().doubleValue();
				String unit = nuclideBottle.getIsLiquid() == 'Y' ? "ul" : "mg";
				tracersAmountLeftMap.put(String.valueOf(nuclideBottle.getNuclideBottleId()),
						"(" + String.valueOf(amountLeft.intValue()) + unit + " left)");
			}
		}
		return tracerTubeDateMap;
	}


	@ModelAttribute("liquidWasteList")
	public Map<String, String> getLiquidWasteList() {
		Map<String, String> liquidWasteList = new TreeMap<String, String>();
		List<NuclideWaste> nuclideWasteList = nuclideWasteService.findNuclideWasteByStateStatus('Y');
		for (NuclideWaste nuclideWaste : nuclideWasteList) {
			StringBuilder sb = new StringBuilder("Waste #");
			sb.append(String.valueOf(nuclideWaste.getNuclideWasteId()));
			sb.append(": ");
			sb.append(nuclideWaste.getNuclide().getNuclideName());
			sb.append(", ");
			sb.append(nuclideWaste.getLocation());
			liquidWasteList.put(String.valueOf(nuclideWaste.getNuclideWasteId()), sb.toString());
		}
		return liquidWasteList;
	}

	@ModelAttribute("solidWasteList")
	public Map<String, String> getSolidWasteList() {
		Map<String, String> solidWasteList = new TreeMap<String, String>();
		List<NuclideWaste> nuclideWasteList = nuclideWasteService.findNuclideWasteByStateStatus('N');
		for (NuclideWaste nuclideWaste : nuclideWasteList) {
			StringBuilder sb = new StringBuilder("Waste #");
			sb.append(String.valueOf(nuclideWaste.getNuclideWasteId()));
			sb.append(": ");
			sb.append(nuclideWaste.getNuclide().getNuclideName());
			sb.append(", ");
			sb.append(nuclideWaste.getLocation());
			solidWasteList.put(String.valueOf(nuclideWaste.getNuclideWasteId()), sb.toString());
		}
		return solidWasteList;
	}

	@ModelAttribute("Location")
	public Map<String, String> getLocation() {
		Map<String, String> location = new TreeMap<String, String>();
		List<NuclideLocation> nuclideLocationList = nuclideLocationService.findAll(); 
		for (NuclideLocation nuclideLocation : nuclideLocationList) {
			location.put(nuclideLocation.getLocation(), nuclideLocation.getLocation());
		}
		return location;
	}

	@ModelAttribute("today")
	public String getCurrentDate() {
		String pattern = "yyyy-MMM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String today = simpleDateFormat.format(new Date());
		return today;
	}

	public int getNuclideBottleId() {
		return nuclideBottleId;
	}

	public void setNuclideBottleId(int nuclideBottleId) {
		this.nuclideBottleId = nuclideBottleId;
	}

	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
		Comparator<K> valueComparator = new Comparator<K>() {
			public int compare(K k1, K k2) {
				int compare = map.get(k2).compareTo(map.get(k1));
				if (compare == 0)
					return 1;
				else
					return compare;
			}
		};
		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}

}
