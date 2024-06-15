package org.research.kadda.nuclide.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.research.kadda.nuclide.TracerTypeEnum;
import org.research.kadda.nuclide.entity.Nuclide;
import org.research.kadda.nuclide.entity.NuclideAttached;
import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.entity.NuclideLocation;
import org.research.kadda.nuclide.entity.NuclideTracerType;
import org.research.kadda.nuclide.entity.NuclideUsage;
import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.entity.NuclideWaste;
import org.research.kadda.nuclide.models.AttachedFile;
import org.research.kadda.nuclide.models.TracerOverview;
import org.research.kadda.nuclide.models.TracerTube;
import org.research.kadda.nuclide.models.UsageOverview;
import org.research.kadda.nuclide.service.DateUtils;
import org.research.kadda.nuclide.service.EmailService;
import org.research.kadda.nuclide.service.NuclideAttachedService;
import org.research.kadda.nuclide.service.NuclideBottleService;
import org.research.kadda.nuclide.service.NuclideLocationService;
import org.research.kadda.nuclide.service.NuclideService;
import org.research.kadda.nuclide.service.NuclideUsageService;
import org.research.kadda.nuclide.service.NuclideUserService;
import org.research.kadda.nuclide.service.NuclideWasteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.research.kadda.osiris.OsirisService;
import org.research.kadda.osiris.data.EmployeeDto;

@Controller
public class NuclideTracerController {
	
	private final Logger logger = LoggerFactory.getLogger(NuclideTracerController.class);
	private static final String HEALTH_SAFETY_EXPERT = "engro1";

	@Autowired
	private NuclideBottleService nuclideBottleService;
	
	@Autowired
	private NuclideService nuclideService;

	@Autowired
	private NuclideLocationService nuclideLocationService;

	@Autowired
	private NuclideUserService nuclideUserService;
	
	@Autowired
	private NuclideUsageService nuclideUsageService;
	
	@Autowired
	private NuclideWasteService nuclideWasteService;
	
	@Autowired
	private NuclideAttachedService nuclideAttachedService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private DateUtils dateUtils;
	

	private final static String TRACER_DISPOSAL = "tracer disposal";
	private final static String ANY = "any";
	private final static String ORIGINAL = "original";
	private final static String DESCENDANT = "daughter";
	private final static String ALL_TRACERS = "All Tracers";
	private final static String NON_DISCARDED_TRACERS = "Non Discarded Tracers";
	private final static String DISCARDED_TRACERS = "Discarded Tracers";
	private final static String EXTERNAL = "external";	
	private final static String ORIGINAL_TRACER = "Original Tracers Only";
	private final static String DAUGHTER_TRACER = "Daughter Tracers Only";
	private final static String EXTERNAL_TRACER = "Tracers for external usage";
	private final static String DATE_PATTERN = "yyyy-MMM-dd";
	
	@RequestMapping("/showTracers")
	public String showTracers(TracerOverview tracerOverview, Model model) {
		logger.info("showTracers()");
		model.addAttribute("tracerOverview", tracerOverview);
		return "showTracers";
	}

	@RequestMapping("/listTracers")
	public String listTracers(TracerOverview tracer, Model model) {
		model.addAttribute("showExternalOnly", false);
		String nuclideName = tracer.getNuclideName();
		model.addAttribute("nuclideName", nuclideName);
		String lab = tracer.getLocation();
		model.addAttribute("lab", lab);
		String initialActivityYear = tracer.getActivityYear();
		model.addAttribute("initialActivityYear", initialActivityYear);
		String discardedStatusSelected = tracer.getDiscardedStatus();
		model.addAttribute("discardedStatusSelected", discardedStatusSelected);
		String isLiquid = tracer.getSolidLiquidState();
		model.addAttribute("isLiquid", isLiquid);
		String tracerTypeExt = "E".equals(tracer.getDiscardedStatus()) ? TracerTypeEnum.EXTERNAL.getTracerTypeName() : null;  
		String daughter = tracerTypeExt != null? tracerTypeExt : tracer.getOriginalDaughterState();
		model.addAttribute("daughter", daughter);
		String scientist = tracer.getResponsible();
		model.addAttribute("scientist", scientist);

		if(daughter == null){
			return "showTracers";
		}
		
		List<TracerOverview> tracers = nuclideBottleService.findNuclideBottleByParam(nuclideName, lab,
				initialActivityYear, discardedStatusSelected, isLiquid, daughter, scientist, tracerTypeExt);
		if(tracerTypeExt != null && tracerTypeExt.equals(TracerTypeEnum.EXTERNAL.getTracerTypeName())) {
			for(TracerOverview to : tracers) {
				String destination = nuclideUsageService.getDestination(to.getTracerId());
				to.setDestination(destination);
				to.setExternalTracersOnly(true);
				to.setLocation(destination);
			}
			model.addAttribute("showExternalOnly", "E".equals(tracer.getDiscardedStatus()));
		}
		int initialActivitySum = 0;
		int currentActivitySum = 0;

		Map<String, Set<String>> tracertIntoDiscardedWateLocationMap = getTracersIntoDiscardedWasteLocation();
		
		//make sure that no decimal value is displayed
		for(TracerOverview tracerOvw : tracers) {
			Double currentAmount = null;
			Double calculatedCurrenActivity = null;
			boolean isInvivo = TracerTypeEnum.INVIVO.getTracerTypeId() == tracerOvw.getTracerTypeId();
			if(isInvivo) {
				Double solideWastePercenatge = tracerOvw.getSolideWastePercentage() != null? tracerOvw.getSolideWastePercentage() : 100;
				Double initialAmount = tracerOvw.getInitialAmount().doubleValue();
				currentAmount = initialAmount - initialAmount * (solideWastePercenatge/100);
				if(tracerOvw.getCurrentAmount() != null) {
					Double amountTaken = initialAmount - Double.valueOf(tracerOvw.getCurrentAmount()); 
					currentAmount -= amountTaken;
				}
			}else {				
				currentAmount = tracerOvw.getCurrentAmount() != null ? Double.valueOf(tracerOvw.getCurrentAmount()):null;
			}
			if(currentAmount.intValue() < 0 || tracerOvw.getDisposalDate() != null) {
				currentAmount = 0.0;
			}
			Double currentActivity = tracerOvw.getCurrentActivity() != null ? Double.valueOf(tracerOvw.getCurrentActivity()):null;
			Double initialAmount = tracerOvw.getInitialAmount() != null ? tracerOvw.getInitialAmount().doubleValue():null;
			Double initialActivity = tracerOvw.getInitialActivity() != null ? tracerOvw.getInitialActivity().doubleValue():null;
			
			if(currentAmount == 0.0 && initialAmount == 0.0) {
				calculatedCurrenActivity = 0.0;
			}else {
				calculatedCurrenActivity =  (currentActivity.doubleValue() * currentAmount.doubleValue()) / initialAmount.doubleValue();
			}
			String currAmount = new BigDecimal(currentAmount).setScale(0, RoundingMode.HALF_UP).toString();
			tracerOvw.setCurrentAmount(currAmount);
			if(tracerOvw.getTracerTypeId() != TracerTypeEnum.EXTERNAL.getTracerTypeId()) {
				boolean tracerDisposed = tracerOvw.getDisposalDate() != null;
				if(tracerDisposed) {
					tracerOvw.setCurrentActivity(String.valueOf(0));
					tracerOvw.setCurrentAmount(String.valueOf(0));
					formatDates(tracerOvw);
				}else {				
					tracerOvw.setCurrentActivity(new BigDecimal(calculatedCurrenActivity).setScale(0, RoundingMode.HALF_UP).toString());
					tracerOvw.setCurrentAmount(new BigDecimal(currentAmount).setScale(0, RoundingMode.HALF_UP).toString());
				}
				if (tracerDisposed && tracertIntoDiscardedWateLocationMap.get(String.valueOf(tracerOvw.getTracerId())) != null
						&& !tracertIntoDiscardedWateLocationMap.get(String.valueOf(tracerOvw.getTracerId()))
								.isEmpty()) {
					StringBuilder sb = new StringBuilder();
					List<String> locs = new ArrayList<String>();
					locs.addAll(tracertIntoDiscardedWateLocationMap.get(String.valueOf(tracerOvw.getTracerId())));
					for(int i = 0; i<locs.size();i++) {
						sb.append(locs.get(i));
						if(locs.size()-i >1) {sb.append(", ");}
					}					
					tracerOvw.setLocation(sb.toString());
				}
			}else {
				tracerOvw.setCurrentActivity("N/A");
				tracerOvw.setCurrentAmount("N/A");
				tracerOvw.setDisposalDate("N/A");
				String destination = nuclideUsageService.getDestination(tracerOvw.getTracerId());
				tracerOvw.setLocation(destination);
			}
			tracerOvw.setInitialActivity(new BigDecimal(initialActivity).setScale(0, RoundingMode.HALF_UP));
			tracerOvw.setInitialAmount(new BigDecimal(initialAmount).setScale(0, RoundingMode.HALF_UP));
			initialActivitySum += tracerOvw.getInitialActivity().intValue();
			if(tracerOvw.getCurrentActivity().matches("-?\\d+(\\.\\d+)?")) {			
				currentActivitySum += Integer.valueOf(tracerOvw.getCurrentActivity());
			}
			boolean tracerGotAttachedFile = CollectionUtils.isNotEmpty(nuclideAttachedService.findByTracerId(tracerOvw.getTracerId()));
			tracerOvw.setAttachedFilesLabel(tracerGotAttachedFile? " " : "N/A");
			tracerOvw.setAttachedFiles(tracerGotAttachedFile? "Y" : "N");
			int parentId = tracerOvw.getParentId();
			tracerOvw.setIsChildren(parentId == 0 ? "N" : "Y");
		}
		
		Collections.sort(tracers, new Comparator<TracerOverview>() {
			@Override
			public int compare(TracerOverview t1, TracerOverview t2) {
				return t2.getTracerId() - t1.getTracerId();
			}
		});
		model.addAttribute("tracers", tracers);
		
		model.addAttribute("initialActivitySum", initialActivitySum);
		model.addAttribute("currentActivitySum", currentActivitySum);
		
		String tracerHierarchyTitle = null;
		switch (daughter) {
		case ANY:
			tracerHierarchyTitle = ALL_TRACERS;
			break;
		case ORIGINAL:
			tracerHierarchyTitle = ORIGINAL_TRACER;
			break;
		case EXTERNAL:
			tracerHierarchyTitle = EXTERNAL_TRACER;
			break;			
		default:
			tracerHierarchyTitle = DAUGHTER_TRACER;			
		}		
		model.addAttribute("tracerHierarchyTitle", tracerHierarchyTitle);
		if(tracerTypeExt == null) {		
			switch (tracer.getDiscardedStatus()) {
			case "any" :
				model.addAttribute("discardedStatus", ALL_TRACERS);
				break;
			case "N":
				model.addAttribute("discardedStatus", NON_DISCARDED_TRACERS);
				break;				
			default:
				model.addAttribute("discardedStatus", DISCARDED_TRACERS);			
			}
		}
		
		//build treeview for tracers
		Map<Integer, TracerOverview> tracerMap = new HashMap<Integer, TracerOverview>();
		for(TracerOverview to : tracers) {
			to.setChildren(getChildren(tracers, to.getTracerId()));
			tracerMap.put(to.getTracerId(), to);
		}
		List<TracerOverview> treeTracers = new ArrayList<TracerOverview>();
		if(TracerTypeEnum.EXTERNAL.getTracerTypeName().equals(tracerTypeExt) || ORIGINAL.equals(daughter) || DESCENDANT.equals(daughter)) {
			treeTracers.addAll(tracers);
		}else {			
			for(TracerOverview to : tracers) {
				if(to.getParentId() == 0 || (to.getParentId() != 0 && tracerMap.get(to.getParentId()) == null)) {treeTracers.add(to);}
			}
		}
		for(TracerOverview to : treeTracers) {
			if(to.getChildren() != null && !to.getChildren().isEmpty()) {
				processChildren(tracerMap, to);
			}
		}
		StringBuilder treeBuilder = new StringBuilder();
		for(TracerOverview to : treeTracers) {
			treeBuilder.append("<tr class=\"treegrid-").append(to.getTracerId()).append("\">");
			buildColumnHtmlTree(model, treeBuilder, to);
			treeBuilder.append("</tr>");
			buildChildHtmlTree(model, treeBuilder, to);
		}
		
		model.addAttribute("treeTracersTable", treeBuilder.toString());
	
		return "listTracers";
	}

	@RequestMapping(value = "/tracerDetail_{nuclideBottleId}", method = RequestMethod.GET)
	public ModelAndView displayDetailTracer(@PathVariable Long nuclideBottleId) {
		ModelAndView mv = new ModelAndView("/tracerDetail");
		NuclideBottle nuclideBottle = nuclideBottleService.findById(nuclideBottleId.intValue());
		TracerOverview tracer = new TracerOverview();
		tracer.setTracerId(nuclideBottle.getNuclideBottleId());
		Integer parentTracerTube = nuclideUsageService.getParentTracerTube(nuclideBottleId.intValue());
		tracer.setParentTracerTube(parentTracerTube != null ? String.valueOf(parentTracerTube) : "N/A");
		tracer.setElbTracerCreation(nuclideUsageService.getElb(nuclideBottleId.intValue()));
		tracer.setNuclideName(nuclideBottle.getNuclide().getNuclideName());
		tracer.setSubstance(nuclideBottle.getSubstanceName());
		tracer.setBatchName(nuclideBottle.getBatchName());
		tracer.setSolidLiquidState(nuclideBottle.getIsLiquid() == 'Y' ? "liquid":"solid");
		tracer.setInitialAmount(new BigDecimal(nuclideBottle.getVolume()).setScale(0, RoundingMode.HALF_UP));
		tracer.setInitialActivity(new BigDecimal(nuclideBottle.getActivity()).setScale(0, RoundingMode.HALF_UP));
		tracer.setDisposalDate(nuclideBottle.getDisposalDate() != null ? new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(nuclideBottle.getDisposalDate()) : "not disposed of yet");
		tracer.setTracerTypeId(nuclideBottle.getTracerType().getTracerTypeId());
		boolean showExternalOnly = TracerTypeEnum.EXTERNAL.getTracerTypeId() == tracer.getTracerTypeId();
		boolean tracerIntoDisposedWaste = false;
		if(showExternalOnly) {
			String destination = nuclideUsageService.getDestination(tracer.getTracerId());
			tracer.setDestination(destination);
			tracer.setExternalTracersOnly(true);
		}
		
		Double currentAmount = 0.0;
		if(nuclideBottle.getBatchName() != null && TracerTypeEnum.INVIVO.getTracerTypeId() == nuclideBottle.getTracerType().getTracerTypeId()) {
			Double solideWastePercentage = nuclideUsageService.getSolidWastePercentage(nuclideBottle.getNuclideBottleId());
			if(nuclideBottle.getVolume() != null) {				
				currentAmount =  nuclideBottle.getVolume() - (nuclideBottle.getVolume() * (solideWastePercentage/100));
				if(nuclideBottle.getSumVolume() != null) {currentAmount -= nuclideBottle.getSumVolume().doubleValue();}
			}
		}else {			
			if(nuclideBottle != null) {			
				currentAmount = nuclideBottle.getSumVolume() != null? nuclideBottle.getVolume() - nuclideBottle.getSumVolume().doubleValue(): nuclideBottle.getVolume();
			}
		}
		if(currentAmount.intValue() < 0 || !tracer.getDisposalDate().equalsIgnoreCase("not disposed of yet")) {
			currentAmount = 0.0;
		}
		tracer.setCurrentAmount(String.valueOf(currentAmount.intValue()));
		
		BigDecimal currentActivity =  currentAmount == 0.0 && tracer.getInitialAmount().intValue() == 0 ? new BigDecimal(0) : new BigDecimal((nuclideBottle.getCurrentActivity() * currentAmount) / tracer.getInitialAmount().doubleValue()); 
		if(currentActivity.intValue() < 0 || !tracer.getDisposalDate().equalsIgnoreCase("not disposed of yet")) {
			currentActivity = new BigDecimal(0);
		}
		tracer.setCurrentActivity(currentActivity.setScale(0, RoundingMode.HALF_UP).toString());
		
		if(showExternalOnly) {
			tracer.setCurrentAmount("N/A");
			tracer.setCurrentActivity("N/A");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd",Locale.US);
		tracer.setInitialActivityDate(nuclideBottle.getActivityDate()!=null ? sdf.format(nuclideBottle.getActivityDate()) : null);
		tracer.setLocation(nuclideBottle.getLocation());
		if(TracerTypeEnum.EXTERNAL.getTracerTypeId() == nuclideBottle.getTracerType().getTracerTypeId()) {
			Date externalUsageDate = nuclideUsageService.getUsageDateByTracerId(nuclideBottle.getNuclideBottleId());
			tracer.setInitialActivityDate(externalUsageDate!=null ? sdf.format(externalUsageDate) : null);
		}else {			
			tracer.setInitialActivityDate(nuclideBottle.getActivityDate()!=null ? sdf.format(nuclideBottle.getActivityDate()) : null);
			Map<String, Set<String>> tracersIntoDiscardedWasteLocationMap = getTracersIntoDiscardedWasteLocation();
			if (tracer.getDiscardDate() != null
					&& tracersIntoDiscardedWasteLocationMap.get(String.valueOf(tracer.getTracerId())) != null
					&& !tracersIntoDiscardedWasteLocationMap.get(String.valueOf(tracer.getTracerId())).isEmpty()) {
				StringBuilder sb = new StringBuilder();
				List<String> locs = new ArrayList<String>();
				locs.addAll(tracersIntoDiscardedWasteLocationMap.get(String.valueOf(tracer.getTracerId())));
				for(int i = 0; i<locs.size();i++) {
					sb.append(locs.get(i));
					if(locs.size()-i >1) {sb.append(", ");}
				}					
				tracer.setLocation(sb.toString());
				tracerIntoDisposedWaste = true;
			}
		}		
		//keys: creationUserId / disposalUserId
		Map<String, String> relatedWasteUsers = nuclideBottleService.getUsersForTracer(nuclideBottleId.intValue());
		NuclideUser creaUser = nuclideUserService.findByUserId(relatedWasteUsers.get("creationUserId"));
		String creationUser = creaUser.getFirstName() + " " + creaUser.getLastName();
		String disposalUser = "N/A";
		if(relatedWasteUsers.get("disposalUserId") != null) {
			NuclideUser displUser = nuclideUserService.findByUserId(relatedWasteUsers.get("disposalUserId"));
			disposalUser = displUser.getFirstName() + " " + displUser.getLastName();
		}				
		tracer.setNuclideUserByCreationUserId(creationUser);
		tracer.setNuclideUserByDisposalUserId(disposalUser);
		boolean tracerGotAttachedFile = CollectionUtils.isNotEmpty(nuclideAttachedService.findByTracerId(nuclideBottle.getNuclideBottleId()));
		tracer.setAttachedFilesLabel(tracerGotAttachedFile? " " : "N/A");
		tracer.setAttachedFiles(tracerGotAttachedFile? "Y" : "N");
		
		boolean isChildren = nuclideUsageService.isChildren(nuclideBottle.getNuclideBottleId());
		List<UsageOverview> tracerUsgList = nuclideUsageService.getTracerUsageList(nuclideBottle.getNuclideBottleId(), isChildren);
		if(nuclideBottle.getBatchName() != null && TracerTypeEnum.INVIVO.getTracerTypeId() == nuclideBottle.getTracerType().getTracerTypeId()) {
			tracerUsgList.addAll(nuclideUsageService.getInVivoUsages(nuclideBottle.getNuclideBottleId()));
		}
		
		//remove duplicates
		Set<UsageOverview> tracerUsageList = new LinkedHashSet<UsageOverview>(tracerUsgList);
		
		List<UsageOverview> finalUsageList = new ArrayList<UsageOverview>();
		if(CollectionUtils.isNotEmpty(tracerUsageList)) {
			for(UsageOverview usage : tracerUsageList) {
				if(usage.getTracerType() != null && usage.getTracerType().intValue() == TracerTypeEnum.EXTERNAL.getTracerTypeId()) {
					String concatDestination = usage.getDestination() + " #" + usage.getNewTracerId().toString();
					usage.setDestination(concatDestination);
				}				
				UsageOverview finalUsage = null;
				if(usage.getOverallPercentage() < 99.9) {
						finalUsage = new UsageOverview();
						finalUsage.setUsageId(usage.getUsageId());
						String wasteId = usage.getWastId() != null ? usage.getWastId() : "N/A";  
						finalUsage.setWastId(wasteId);
						Double amountToDisplay = null;
						if(usage.getPercentage() != 0) {
							amountToDisplay = usage.getAmount().doubleValue()*(usage.getPercentage().doubleValue() / 100);
							finalUsage.setAmount(amountToDisplay != null ? new BigDecimal(amountToDisplay).setScale(0, RoundingMode.HALF_UP):null);
						}else {
							amountToDisplay = (usage.getAmount().doubleValue()*(100-usage.getOverallPercentage().doubleValue())/100);
							finalUsage.setAmount(amountToDisplay != null ? new BigDecimal(amountToDisplay).setScale(0, RoundingMode.HALF_UP):null);
						}
						finalUsage.setBioLabJournal(usage.getBioLabJournal());
						finalUsage.setAssayType(usage.getAssayType());
						finalUsage.setDestination(usage.getDestination());
						if(usage.getAssayType() != null && usage.getAssayType().contains("#")) {
							StringBuilder aTS = new StringBuilder(usage.getAssayType().split("#")[0]);
							aTS.append("<a href=\"/nuclideDB/tracerDetail_"+usage.getAssayType().split("#")[1].strip()+"\" class=\"btn2\">");
							aTS.append(usage.getAssayType().split("#")[1].strip());
							aTS.append("</a>");
							finalUsage.setAssayType(aTS.toString());
						}else {							
							finalUsage.setAssayType(usage.getAssayType());
						}
						if(usage.getDestination() != null && usage.getDestination().contains("#")) {
							StringBuilder aTD = new StringBuilder(usage.getDestination().split("#")[0]);
							aTD.append("<a href=\"/nuclideDB/tracerDetail_"+usage.getDestination().split("#")[1].strip()+"\" class=\"btn2\">");
							aTD.append(usage.getDestination().split("#")[1].strip());
							aTD.append("</a>");
							finalUsage.setDestination(aTD.toString());
						}else {
							finalUsage.setDestination(usage.getDestination());
						}
						DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
						try {
							Date usageDate = format.parse(usage.getUsageDate());
							finalUsage.setUsageDate(new SimpleDateFormat("yyyy-MMM-dd",Locale.US).format(usageDate));
						} catch (ParseException e) {
							e.getMessage();
						}						
						finalUsage.setUser(usage.getUser());
				}else {
						NuclideWaste waste = usage.getWastId() != null ? nuclideWasteService.findWasteById(Integer.valueOf(usage.getWastId())) : null;
						boolean isLiquideWaste = waste != null ? waste.getIsLiquid() == 'Y' : false;
						finalUsage = new UsageOverview();
						finalUsage.setUsageId(usage.getUsageId());
						String wasteId = usage.getWastId() != null ? usage.getWastId() : "N/A";
						finalUsage.setWastId(wasteId);
						Double amountToDisplay = null;
						if(usage.getPercentage() != 0) {						
							if(isLiquideWaste) {							
								amountToDisplay = usage.getAmount().doubleValue()*(usage.getPercentage().doubleValue() / 100);
								finalUsage.setAmount(amountToDisplay != null ? new BigDecimal(amountToDisplay).setScale(0, RoundingMode.HALF_UP):null);
							}else {
								amountToDisplay = usage.getAmount().doubleValue()*(usage.getPercentage().doubleValue() / 100);
								finalUsage.setAmount(amountToDisplay != null ? new BigDecimal(amountToDisplay).setScale(0, RoundingMode.HALF_UP):null);
							}
						}else {
							if(waste == null) {
								continue;
							}else {
								if(isLiquideWaste) {							
									amountToDisplay = usage.getAmount().doubleValue()*(100-usage.getOverallPercentage().doubleValue())/100;
									finalUsage.setAmount(amountToDisplay != null ? new BigDecimal(amountToDisplay).setScale(0, RoundingMode.HALF_UP):null);
								}else {
									amountToDisplay = (usage.getAmount().doubleValue()*(100-usage.getOverallPercentage().doubleValue())/100);
									finalUsage.setAmount(amountToDisplay != null ? new BigDecimal(amountToDisplay).setScale(0, RoundingMode.HALF_UP):null);
								}
							}
						}					
						finalUsage.setAmount(amountToDisplay != null ? new BigDecimal(amountToDisplay).setScale(0, RoundingMode.HALF_UP):null);
						finalUsage.setBioLabJournal(usage.getBioLabJournal());
						finalUsage.setAssayType(usage.getAssayType());
						finalUsage.setDestination(usage.getDestination());
						if(usage.getAssayType() != null && usage.getAssayType().contains("#")) {
							StringBuilder aTS = new StringBuilder(usage.getAssayType().split("#")[0]);
							aTS.append("<a href=\"/nuclideDB/tracerDetail_\" "+usage.getAssayType().split("#")[1].strip()+" class=\"btn2\">");
							aTS.append(usage.getAssayType().split("#")[1].strip());
							aTS.append("</a>");
						}else {							
							finalUsage.setAssayType(usage.getAssayType());
						}
						if(usage.getDestination() != null && usage.getDestination().contains("#")) {
							StringBuilder aTD = new StringBuilder(usage.getDestination().split("#")[0]);
							aTD.append("<a href=\"/nuclideDB/tracerDetail_"+usage.getDestination().split("#")[1].strip()+"\" class=\"btn2\">");
							aTD.append(usage.getDestination().split("#")[1].strip());
							aTD.append("</a>");
							finalUsage.setDestination(aTD.toString());
						}else {
							finalUsage.setDestination(usage.getDestination());
						}
						DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
						try {
							Date usageDate = format.parse(usage.getUsageDate());
							finalUsage.setUsageDate(new SimpleDateFormat("yyyy-MMM-dd",Locale.US).format(usageDate));
						} catch (ParseException e) {
							e.getMessage();
						}						
						finalUsage.setUser(usage.getUser());
					
				}
				if(finalUsage.getAmount().intValue() >= 0) {					
					finalUsageList.add(finalUsage);
				}
			}
		}
		finalUsageList.removeAll(Collections.singleton(null));
		proceedWithInvivoUsages(finalUsageList);
		tracer.setUsageList(finalUsageList);
		mv.addObject("tracer", tracer);
		mv.addObject("showExternalOnly", showExternalOnly);
		mv.addObject("tracerIntoDisposedWaste", tracerIntoDisposedWaste);	
		return mv;
	}

	@RequestMapping(value = "/listAttachedFiles{nuclideBottleId}", method = RequestMethod.GET)
	public ModelAndView displayAttachedFiles(@PathVariable Long nuclideBottleId) {
		ModelAndView mv = new ModelAndView("/listAttachedFiles");
		List<AttachedFile> attachedFileList = new ArrayList<AttachedFile>();
		List<NuclideAttached> nuclideAttachedList = nuclideAttachedService.findByTracerId(nuclideBottleId.intValue());
		for(NuclideAttached natch : nuclideAttachedList) {
			AttachedFile attachedFileToDisplay = new AttachedFile();			
			attachedFileToDisplay.setNuclideBottle(nuclideBottleId.toString());
			attachedFileToDisplay.setFileName(natch.getFileName());
			attachedFileToDisplay.setFileType(natch.getFileType());
			attachedFileToDisplay.setDeleteParam(String.valueOf(nuclideBottleId)+"("+String.valueOf(natch.getNuclideAttachedId()+")"+natch.getNuclideUser().getUserId()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
			attachedFileToDisplay.setFileDate(sdf.format(natch.getFileDate()));
			NuclideUser creaUser = nuclideUserService.findByUserId(natch.getNuclideUser().getUserId());
			String creationUser = creaUser.getFirstName() + " " + creaUser.getLastName();
			attachedFileToDisplay.setNuclideUser(creationUser);
			attachedFileToDisplay.setFilePath(nuclideBottleId.toString() + "&" + natch.getFileName()+ "&" + natch.getFileType());
			//attachedFileToDisplay.setFilePath(natch.getFileFullPath());
			
			attachedFileList.add(attachedFileToDisplay);
		}

		mv.addObject("attachedFileList", attachedFileList);	
		return mv;
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/download_attached{fileName}", method = RequestMethod.GET)
	public ResponseEntity downloadAttachFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
		Properties prop = fetchProperties(request);
		String path = prop.getProperty("files.path");
		//String path = "D:\\nuclideDB\\resources\\uploads";
		
		int tracerId = Integer.valueOf(fileName.split("&")[0]);
		String filename = fileName.split("&")[1];
		List<NuclideAttached> nas = nuclideAttachedService.findByTracerId(tracerId);
		NuclideAttached nuclideAttached = null;
		for(NuclideAttached na : nas) {
			if(na.getFilePath().contains(filename)) {
				nuclideAttached = na;
			}
		}
		String file;
		String[] s = fileName.split("&");
		if(s.length>2) {			
			file = fileName.split("&")[0] + File.separator + fileName.split("&")[1] + "."
					+ fileName.split("&")[2];
		}else {
			file = fileName.split("&")[0] + File.separator + filename + "." + nuclideAttached.getFileType();
		}
	    //InputStream inputStream = new FileInputStream(new File("file:/" + filePath));
	    InputStream inputStream = new FileInputStream(new File(nuclideAttached.getFileFullPath()));
	    InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
	    HttpHeaders headers = new HttpHeaders();
	    String extension = nuclideAttached.getFileType();
		switch (extension) {
		case "pdf":
			headers.setContentType(MediaType.APPLICATION_PDF);
			break;
		case "jpg":
			headers.setContentType(MediaType.IMAGE_JPEG);
			break;
		case "doc":
			headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
			break;			
		default:
		}	
	    headers.set("Content-Disposition", "attachment; filename=" + file);
	    headers.setContentLength(Files.size(Paths.get(nuclideAttached.getFileFullPath())));
	    return new ResponseEntity(inputStreamResource, headers, HttpStatus.OK);
	}
	
	public Properties fetchProperties(HttpServletRequest request){
        Properties properties = new Properties();
        try {
        	File file;
        	if(request.getRequestURL() != null && (request.getRequestURL().toString().contains("ares"))) {
        		file = ResourceUtils.getFile("classpath:datasource-prod.properties");
        	}else {
        		file = ResourceUtils.getFile("classpath:datasource-test.properties");
        	}
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
        	logger.error(e.getMessage());
        }
        return properties;
    }
	
	//disposeTracer
	@RequestMapping(value = "/discardTracer{nuclideBottleId}", method = RequestMethod.GET)
	public ModelAndView discardTracer(@PathVariable Long nuclideBottleId) {
		ModelAndView mv = new ModelAndView("/discardTracer");
		NuclideBottle nuclideBottle = nuclideBottleService.findById(nuclideBottleId.intValue());
		boolean isInVivoTracer = TracerTypeEnum.INVIVO.getTracerTypeId() == nuclideBottle.getTracerType().getTracerTypeId();
		
		TracerOverview tracerToDisplay = new TracerOverview();
		tracerToDisplay.setTracerId(nuclideBottle.getNuclideBottleId());
		tracerToDisplay.setNuclideName(nuclideBottle.getNuclide().getNuclideName());
		tracerToDisplay.setSubstance(nuclideBottle.getSubstanceName());
		tracerToDisplay.setLocation(nuclideBottle.getLocation());
		tracerToDisplay.setInitialActivity(new BigDecimal(nuclideBottle.getActivity()).setScale(0, RoundingMode.HALF_UP));
		tracerToDisplay.setInitialAmount(new BigDecimal(nuclideBottle.getVolume()).setScale(0, RoundingMode.HALF_UP));
		BigDecimal currentVolume = nuclideBottle != null && nuclideBottle.getSumVolume() != null ? new BigDecimal(nuclideBottle.getVolume()).subtract(nuclideBottle.getSumVolume()) : new BigDecimal(nuclideBottle.getVolume());
		if(isInVivoTracer) {
			Double solidWastePercent = nuclideUsageService.getSolidWastePercentage(nuclideBottle.getNuclideBottleId());
			Double currAmount = nuclideBottle.getVolume() - (nuclideBottle.getVolume() * (solidWastePercent / 100));
			if(nuclideBottle.getSumVolume() != null) {
				currAmount -= nuclideBottle.getSumVolume().doubleValue();
			}
			currentVolume = new BigDecimal(currAmount).setScale(0, RoundingMode.HALF_UP);
		}
		tracerToDisplay.setCurrentAmount(currentVolume.setScale(0, RoundingMode.HALF_UP).toString());
		try {
			DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
			Date discardDate = tracerToDisplay.getDisposalDate() == null ? new Date() : format.parse(tracerToDisplay.getDisposalDate());
			tracerToDisplay.setDiscardDate(discardDate);
		} catch (ParseException e) {
			e.getMessage();
		}		
		//keys: creationUserId / closureUserId 
		Map<String, String> relatedWasteUsers = nuclideBottleService.getUsersForTracer(nuclideBottleId.intValue());
		tracerToDisplay.setCreationUserId(relatedWasteUsers.get("creationUserId"));
		NuclideUser creationUser = nuclideUserService.findByUserId(relatedWasteUsers.get("creationUserId"));
		tracerToDisplay.setNuclideUserByCreationUserId(creationUser.getFirstName()+" "+creationUser.getLastName());
		Map<String, String> liquidWasteMap = nuclideWasteService.getWasteContainerList(nuclideBottle.getNuclide().getNuclideName(), 'Y');
		Map<String, String> solidWasteMap = nuclideWasteService.getWasteContainerList(nuclideBottle.getNuclide().getNuclideName(), 'N');
		if("Y".equals(tracerToDisplay.getSolidLiquidState())) {
			tracerToDisplay.setWasteContainerList(liquidWasteMap);
		}else {
			tracerToDisplay.setWasteContainerList(solidWasteMap);
		}
		Date creationDate = nuclideUsageService.getUsageDate(tracerToDisplay.getTracerId());
		if(creationDate == null) {
			creationDate = nuclideBottle.getActivityDate();
		}		
		tracerToDisplay.setCreationDate(dateUtils.getFormatedDate(creationDate));
		mv.addObject("tracerToDisplay", tracerToDisplay);
		
		return mv;
	}
	
	@RequestMapping(value = "/discardTracer{nuclideWasteId}", method = RequestMethod.POST)
	public ModelAndView discardTracer(@ModelAttribute TracerOverview tracerOverview, BindingResult result, 
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {
		NuclideBottle tracerToDispose = nuclideBottleService.findById(tracerOverview.getTracerId());
		NuclideUser disposalUser = nuclideUserService.findByUserId(tracerOverview.getUserId());
		int wasteId = Integer.valueOf(tracerOverview.getWasteContainer());
		NuclideWaste waste = null;
		//save usage		
		if(Double.valueOf(tracerOverview.getCurrentAmount()) > 0.0000001) {
			NuclideUsage usage = new NuclideUsage();
			usage.setVolume(Double.valueOf(tracerOverview.getCurrentAmount()));
			usage.setBioLabJournal(TRACER_DISPOSAL);
			usage.setAssayType(TRACER_DISPOSAL);
			try {
				DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
				//datepicker not user the value has not been set
				Date disposalDate = tracerOverview.getDisposalDate() == null ? new Date() : format.parse(tracerOverview.getDisposalDate());
				usage.setUsageDate(disposalDate);
			} catch (ParseException e) {
				e.getMessage();
			}				
			usage.setNuclideUser(disposalUser);
			usage.setNuclideBottle(tracerToDispose);
			usage.setLiquidWastePercentage(0);
			usage.setNuclideWasteByLiquidWasteId(null);
			usage.setSolidWastePercentage(100);
			waste = nuclideWasteService.findWasteById(wasteId);
			usage.setNuclideWasteBySolidWasteId(waste);
			
			if (nuclideUserService.findByUserId(disposalUser.getUserId()) == null) {
				ModelAndView modv = new ModelAndView("redirect:/errorCannotChange");
				return modv;
			}
			
			if(!nuclideUsageService.saveNuclide(usage)) {
				ModelAndView mv = new ModelAndView("redirect:/warning");
				return mv;
			}
		}
		
		//update tracer
		tracerToDispose.setNuclideUserByDisposalUserId(disposalUser);
		try {
			DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
			Date disposalDate = tracerOverview.getDisposalDate() == null ? new Date() : format.parse(tracerOverview.getDisposalDate());
			tracerToDispose.setDisposalDate(disposalDate);
		} catch (ParseException e) {
			e.getMessage();
		}
		if (nuclideUserService.findByUserId(disposalUser.getUserId().toUpperCase()) == null) {
			ModelAndView modv = new ModelAndView("redirect:/errorCannotChange");
			return modv;
		}
		
		if(!nuclideBottleService.saveNuclideBottle(tracerToDispose)) {
			ModelAndView mv = new ModelAndView("redirect:/warning");
			return mv;
		}
		
		if(waste!=null) {
			try {
				nuclideWasteService.checkIfFifityPercentActivityWasteReached(waste, disposalUser.getUserId(), request);
			} catch (MessagingException | IOException e) {
				e.printStackTrace();
			}
		}
		
		ModelAndView mv = new ModelAndView("redirect:/discardTracerSuccess");
		return mv;
	}

	@RequestMapping("/discardTracerSuccess")
	public String discardTracerSuccess(NuclideBottle bottleToSave, Model model) {
		return "discardTracerSuccess";
	}
	
	
	//relocateTracer
	@RequestMapping(value = "/relocateTracer{nuclideBottleId}", method = RequestMethod.GET)
	public ModelAndView displayRelocateTracer(@PathVariable Long nuclideBottleId) {
		ModelAndView mv = new ModelAndView("/relocateTracer");
		NuclideBottle nuclideBottle = nuclideBottleService.findById(nuclideBottleId.intValue());
		mv.addObject("nuclideBottle", nuclideBottle);
		
		return mv;
	}
	
	@RequestMapping(value = "/relocateTracer{nuclideBottleId}", method = RequestMethod.POST)
	public ModelAndView relocateTracer(@ModelAttribute NuclideBottle tracer, BindingResult result, HttpServletRequest request) {
		NuclideBottle nuclidebottle = nuclideBottleService.findById(tracer.getNuclideBottleId());
		nuclidebottle.setLocation(tracer.getLocation());

		HttpSession session = request.getSession();
		boolean userIdfound = (String) session.getAttribute("username") != null && !StringUtils.isEmpty((String) session.getAttribute("username"));

		
		if (userIdfound && nuclideUserService.findByUserId(((String) session.getAttribute("username")).toUpperCase()) == null) {
			ModelAndView modv = new ModelAndView("redirect:/errorCannotChange");
			return modv;
		}
		
		if(!nuclideBottleService.saveNuclideBottle(nuclidebottle)) {
			ModelAndView mv = new ModelAndView("redirect:/warning");
			return mv;
		}		
		ModelAndView mv = new ModelAndView("redirect:/updateTracerLocation");
		return mv;
	}
	
	@RequestMapping("/updateTracerLocation")
	public String updateWasteLocation(NuclideWaste wasteToSave, Model model) {
		return "updateTracerLocation";
	}
		
	
	@RequestMapping("/newTracer")
	public String newTracer(TracerTube tracerTube, BindingResult bindingResult, Model model) {
		model.addAttribute("tracerTube", new TracerTube());
		return "newTracer";
	}

	@RequestMapping(value = { "/addNewTracer" }, method = RequestMethod.POST)
	public ModelAndView addNewTracer(TracerTube tracer, BindingResult result, Model model, HttpServletRequest request) throws AddressException, MessagingException, IOException {	
		ModelAndView mv = new ModelAndView("/insertTracer");
		NuclideBottle newNuclideBottle = new NuclideBottle();
		newNuclideBottle.setNuclide(nuclideService.findByName(tracer.getNuclideName()));
		newNuclideBottle.setNuclideUserByNuclideUserId(nuclideUserService.findByUserId(tracer.getUserId()));
		newNuclideBottle.setSubstanceName(tracer.getSubstanceName());
		newNuclideBottle.setBatchName(tracer.getBatchName());
		newNuclideBottle.setIsLiquid(tracer.getState());
		newNuclideBottle.setActivity(tracer.getInitialActivity());
		newNuclideBottle.setVolume(tracer.getInitialAmount());
		try {
			DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
			//datepicker has not been used --> new date
			Date activityDate = tracer.getActivityDate() == null ? new Date() : format.parse(tracer.getActivityDate());
			newNuclideBottle.setActivityDate(activityDate);
		} catch (ParseException e) {
			e.getMessage();
		}
		newNuclideBottle.setLocation(tracer.getLocation());

		if (nuclideUserService.findByUserId(tracer.getUserId().toUpperCase()) == null) {
			ModelAndView cannotChange = new ModelAndView("redirect:/errorCannotChange");
			return cannotChange;
		}
			
		NuclideTracerType tracerType = new NuclideTracerType(TracerTypeEnum.PARENT.getTracerTypeId(), TracerTypeEnum.PARENT.getTracerTypeName());
		newNuclideBottle.setTracerType(tracerType);	
		if(!nuclideBottleService.saveNuclideBottle(newNuclideBottle)) {
			ModelAndView warningPage = new ModelAndView("redirect:/warning");
			return warningPage;			
		}
		//if new tracer is P32 warn the health and safety expert
		if("32P".equals(newNuclideBottle.getNuclide().getNuclideName())) {
			EmployeeDto user = OsirisService.getEmployeeByUserId(newNuclideBottle.getNuclideUserByNuclideUserId().getUserId().toLowerCase());
			String healAndSafetyExpertEmail = OsirisService.getEmployeeByUserId(HEALTH_SAFETY_EXPERT).getEmail();
			String subject = "New P-32 tracer";
			boolean testMode = request.getRequestURL() != null && !request.getRequestURL().toString().contains("ares");
			emailService.process32PUsageEmail(user.getFirstName() + " " + user.getLastName(),healAndSafetyExpertEmail, subject, testMode);
		}
		
		Double tracerSum = nuclideBottleService.getTracerSum(newNuclideBottle.getNuclide().getNuclideName(), newNuclideBottle.getLocation());
		Double wasteSum = nuclideWasteService.getWasteSum(newNuclideBottle.getNuclide().getNuclideName(), newNuclideBottle.getLocation());
		wasteSum += nuclideWasteService.getComplementWasteSum(newNuclideBottle.getNuclide().getNuclideName(), newNuclideBottle.getLocation());
		Double allowance = nuclideLocationService.getAllowance(newNuclideBottle.getNuclide().getNuclideName(), newNuclideBottle.getLocation());
		
		if (tracerSum+wasteSum>allowance) {
			StringBuilder sb = new StringBuilder();
			sb.append("Your tracer tube was successfully registered (tracer number <a href='/nuclideDB/tracerDetail_"+newNuclideBottle.getNuclideBottleId()+"' class='btn3' id='tracerIdRecorded'>").append( newNuclideBottle.getNuclideBottleId()).append("</a>).");
			sb.append("<br /><font color=\"red\"><b> WARNING: The combined ").append(newNuclideBottle.getNuclide().getNuclideName()).append(" activity of current tracers (").append(tracerSum.intValue()).append(" kBq) and waste (").append(newNuclideBottle.getLocation()).append(") exceeds the allowed limit of ").append(allowance).append(" kBq.</b></font>");
			model.addAttribute("newTracerSaveSuccess", sb.toString());
			
			//send email to admin
			boolean testMode = request.getRequestURL() != null && !request.getRequestURL().toString().contains("ares");
			emailService.processRadiactivityLimitEmail(newNuclideBottle, tracerSum, allowance, testMode);
		}else {
			model.addAttribute("newTracerSaveSuccess", "Your tracer tube was successfully registered (tracer number <a href='/nuclideDB/tracerDetail_"+newNuclideBottle.getNuclideBottleId()+"' class='btn3' id='tracerIdRecorded'>"+ newNuclideBottle.getNuclideBottleId() +"</a>).");	
		}
		
		
		
		model.addAttribute("nuclideBottleId", newNuclideBottle.getNuclideBottleId());
		
		return mv;
	}

	
	@ModelAttribute("nuclideList")
	public Map<String, String> getNuclideList() {
		Map<String, String> nuclideList = new TreeMap<String, String>();
		List<Nuclide> nuclides = nuclideService.getNuclideList();
		for (Nuclide nuclide : nuclides) {
			nuclideList.put(nuclide.getNuclideName(), nuclide.getNuclideName());
		}
		return nuclideList;
	}

	@ModelAttribute("locationList")
	public Map<String, String> getLocationList() {
		Map<String, String> locationList = new TreeMap<String, String>();
		for (NuclideLocation nuclideLocation : nuclideLocationService.findAll()) {
			locationList.put(nuclideLocation.getLocation(), nuclideLocation.getLocation());
		}
		return locationList;
	}

	@ModelAttribute("yearList")
	public Map<String, String> getYearList() {
		Map<String, String> yearList = new LinkedHashMap<String, String>();
		final int year = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = year; i >= 2002; i--) {
			yearList.put(String.valueOf(i), String.valueOf(i));
		}
		return yearList;
	}

	@ModelAttribute("responsibleList")
	public Map<String, String> getResponsibleList() {
		Map<String, NuclideUser> responsibleMap = new TreeMap<String, NuclideUser>();
		Map<String, String> responsibleList = new LinkedHashMap<String, String>();
		List<NuclideUser> nuclideUsers = nuclideUserService.findAll();
		Collections.sort(nuclideUsers, new Comparator<NuclideUser>() {
			public int compare(NuclideUser nu1, NuclideUser nu2) {
				return nu1.getFirstName().compareTo(nu2.getFirstName());
			}
		});
		for (NuclideUser nuclideUser : nuclideUsers) {
			//INC0093451 - Keep All Users For Search
			//if (nuclideUser.getIsActive() == 'Y') {
				responsibleMap.put(nuclideUser.getFirstName() + " " + nuclideUser.getLastName(), nuclideUser);
			//}
		}
		for(Entry<String, NuclideUser> entry : responsibleMap.entrySet()) {
			responsibleList.put(entry.getValue().getUserId(), entry.getKey());
		}
		return responsibleList;
	}
	
	@ModelAttribute("today")
	public String getCurrentDate() {		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
		String today = simpleDateFormat.format(new Date());		
		return today;
	}
	
	@ModelAttribute("discardDateList")
	public Map<String, String> getDiscardDateList() {
		Map<String, String> discardDateList = new LinkedHashMap<String, String>();
		final int year = Calendar.getInstance().get(Calendar.YEAR);		
		for (int i = year; i >= 2002; i--) {
			discardDateList.put(String.valueOf(i), "Tracers discarded in " + String.valueOf(i));
		}
		return discardDateList;
	}
	
	public Map<String, Set<String>> getTracersIntoDiscardedWasteLocation() {
		Map<String, Set<String>> tracersIntoDiscardedLiquidWasteLocation = nuclideService.getTracersIntoDiscardedWasteLocation('Y');
		Map<String, Set<String>> tracersIntoDiscardedSolidWasteLocation = nuclideService.getTracersIntoDiscardedWasteLocation('N');
		Map<String, Set<String>> finalMap = new HashMap<String, Set<String>>();
		finalMap.putAll(tracersIntoDiscardedLiquidWasteLocation);
		//merge maps
		for(Entry<String, Set<String>> entry : tracersIntoDiscardedSolidWasteLocation.entrySet()) {
			if(finalMap.get(entry.getKey())!=null) {
				finalMap.get(entry.getKey()).addAll(entry.getValue());
			}else {
				finalMap.put(entry.getKey(), entry.getValue());
			}
		}
		return finalMap;
	}
	
	private void formatDates(TracerOverview tracerOvw) {
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date disposalDate = format.parse(tracerOvw.getDisposalDate());
			tracerOvw.setDisposalDate(new SimpleDateFormat("yyyy-MMM-dd",Locale.US).format(disposalDate));
		} catch (ParseException e) {
			e.getMessage();
		}
	}
	
	private void proceedWithInvivoUsages(List<UsageOverview> finalUsageList) {
		//TODO review algo
		Map<String,List<UsageOverview>> inVivoUsagesMap = new HashMap<String, List<UsageOverview>>();
		for(UsageOverview invivoUsage : finalUsageList) {
			if(invivoUsage.getAssayType() != null && invivoUsage.getAssayType().contains("In-Vivo")) {
				String key = (invivoUsage.getBioLabJournal() + invivoUsage.getAssayType() + invivoUsage.getDestination() + invivoUsage.getUsageDate()).replaceAll("\\s","");  
				if(inVivoUsagesMap.get(key)!=null) {
					inVivoUsagesMap.get(key).add(invivoUsage);
				}else {
					List<UsageOverview> inVivoUsages = new ArrayList<UsageOverview>();
					inVivoUsages.add(invivoUsage);
					inVivoUsagesMap.put(key, inVivoUsages);
				}
			}
		}
		Iterator<UsageOverview> it = finalUsageList.iterator(); 
		while(it.hasNext()) {
			UsageOverview invivoUsage = it.next();
			String key = (invivoUsage.getBioLabJournal() + invivoUsage.getAssayType() + invivoUsage.getDestination() + invivoUsage.getUsageDate()).replaceAll("\\s",""); 
			if(inVivoUsagesMap.get(key) != null) {
				it.remove();
			}
		}
		List<UsageOverview> mergedInviVoUsages = new ArrayList<UsageOverview>();
		for(Entry<String,List<UsageOverview>> entry : inVivoUsagesMap.entrySet()) {
			List<UsageOverview> inVivoUsages = entry.getValue();
			Collections.sort(inVivoUsages, new Comparator<UsageOverview>() {
				@Override
				public int compare(UsageOverview uo1, UsageOverview uo2) {
					return uo2.getWastId().compareTo(uo1.getWastId());
				}
			});
			UsageOverview invivoUsageToKeep = inVivoUsages.get(0);
			int amount = 0;
			for(UsageOverview invivoUsage : entry.getValue()) {
				amount += invivoUsage.getAmount() != null ? invivoUsage.getAmount().intValue(): 0;
			}
			NuclideUsage ref = nuclideUsageService.findByUsageId(invivoUsageToKeep.getUsageId().intValue());
			Double refAmount = ref != null? ref.getVolume() : null;
			if(refAmount != null && refAmount.intValue() != amount) {
				invivoUsageToKeep.setAmount(new BigDecimal(refAmount.intValue()));
			}else {				
				invivoUsageToKeep.setAmount(new BigDecimal(amount));
			}
			mergedInviVoUsages.add(invivoUsageToKeep);
		}
		for(UsageOverview usg : mergedInviVoUsages) {
			if(!"N/A".equals(usg.getWastId())) {
				NuclideUsage persistedUsg = nuclideUsageService.findByUsageId(usg.getUsageId().intValue());
				NuclideBottle refTracer = nuclideBottleService.findById(persistedUsg.getNewNuclideBottleId());
				double percentageIntoWaste = persistedUsg.getSolidWastePercentage() + persistedUsg.getLiquidWastePercentage();
				double amount = (refTracer.getVolume() * percentageIntoWaste) / 100;
				usg.setAmount(new BigDecimal(amount));
			}
		}
		
		finalUsageList.addAll(mergedInviVoUsages);
	}
	
	private List<TracerOverview> getChildren(List<TracerOverview> tracers, int parentId) {
		List<TracerOverview> children = new ArrayList<TracerOverview>();
		for(TracerOverview tracerOvw : tracers) {
			if(tracerOvw.getParentId() == parentId) children.add(tracerOvw);
		}
		return children;
	}

	private void processChildren(Map<Integer, TracerOverview> tracerMap, TracerOverview to) {
		List<TracerOverview> childTemp = new ArrayList<TracerOverview>();
		childTemp.addAll(to.getChildren());
		to.setChildren(new ArrayList<TracerOverview>());
		for(TracerOverview cto : childTemp) {
			TracerOverview child = tracerMap.get(cto.getTracerId());
			to.getChildren().add(child);
			if(child.getChildren() != null && !child.getChildren().isEmpty()) {
				processChildren(tracerMap, child);
			}
		}
	}
	
	private void buildChildHtmlTree(Model model, StringBuilder treeBuilder, TracerOverview to) {
		if(to.getChildren() != null && !to.getChildren().isEmpty()) {
			for(TracerOverview cto : to.getChildren()) {					
				treeBuilder.append("<tr class=\"treegrid-").append(cto.getTracerId()).append(" treegrid-parent-").append(cto.getParentId()).append("\">");
				buildColumnHtmlTree(model, treeBuilder, cto);
				treeBuilder.append("</tr>");
				if(cto.getChildren() != null && !cto.getChildren().isEmpty()) {
					buildChildHtmlTree(model, treeBuilder, cto);
				}
			}
		}
	}
	
	private void buildColumnHtmlTree(Model model, StringBuilder treeBuilder, TracerOverview to) {
		treeBuilder.append("<td align=\"left\" width=\"130\">").append("<a href=\"/nuclideDB/tracerDetail_").append(to.getTracerId()).append("\" class=\"btn2\">").append(to.getTracerId()).append("</a>");
		if(to.getAttachedFiles().equals("Y")) { treeBuilder.append("<i class=\"fas fa-download\"></i>"); }
		String showExternalOnly = "showExternalOnly";
		if(to.getParentId() != 0 && (boolean) model.asMap().get(showExternalOnly)) {
			treeBuilder.append("<td align=\"center\">").append("<a href=\"/nuclideDB/tracerDetail_").append(to.getParentId()).append("\" class=\"btn\">").append(to.getParentId()).append("</a></td>");
		}
		treeBuilder.append("<td align=\"center\">").append(to.getNuclideName()).append("</td>");
		treeBuilder.append("<td align=\"left\">").append(to.getSubstance()).append("</td>");
		if(to.getSolidLiquidState().equals("Y")) {
			treeBuilder.append("<td align=\"left\">").append("liquid").append("</td>");
		}else {
			treeBuilder.append("<td align=\"left\">").append("solid").append("</td>");
		}
		if((boolean) model.asMap().get(showExternalOnly).equals(Boolean.FALSE)) {
			treeBuilder.append("<td align=\"center\">").append(to.getCurrentAmount()).append("</td>");
			treeBuilder.append("<td align=\"center\">").append(to.getCurrentActivity()).append("</td>");
		}
		treeBuilder.append("<td align=\"center\">").append(to.getInitialAmount()).append("</td>");
		treeBuilder.append("<td align=\"center\">").append(to.getInitialActivity()).append("</td>");
		treeBuilder.append("<td align=\"left\">").append(to.getResponsible()).append("</td>");
		if((boolean) model.asMap().get(showExternalOnly).equals(Boolean.FALSE)) {
			treeBuilder.append("<td align=\"left\">").append(to.getLocation()).append("</td>");
			if(to.getDisposalDate() == null && TracerTypeEnum.EXTERNAL.getTracerTypeId() != to.getTracerTypeId()) {
				treeBuilder.append("<td align=\"center\"><input type=\"button\" class=\"button1\" value=\"Dispose now\" onclick=\"location.href='").append("/nuclideDB/discardTracer").append(to.getTracerId()).append("'\" /></td>");
			}else {
				treeBuilder.append("<td align=\"center\">").append(to.getDisposalDate()).append("</td>");
			}
		}else {
			treeBuilder.append("<td align=\"left\">").append(to.getDestination()).append("</td>");
			treeBuilder.append("<td align=\"left\">").append(to.getCreationDate()).append("</td>");
		}
	}

}
