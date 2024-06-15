package org.research.kadda.nuclide.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.research.kadda.nuclide.dao.NuclideWasteDao;
import org.research.kadda.nuclide.entity.NuclideWaste;
import org.research.kadda.nuclide.models.WasteOverview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.research.kadda.osiris.OsirisService;

@Service
public class NuclideWasteServiceImpl implements NuclideWasteService {

	@Autowired
	private NuclideWasteDao nuclideWasteDao;
	
	@Autowired
	private NuclideUsageService nuclideUsageService;
	
	@Autowired
	private EmailService emailService;
	
	private final static String TRITIUM = "3H";
	private final static String CARBON_14 = "14C";
	private static final String HEALTH_SAFETY_EXPERT = "engro1";
	
	@Override
	public List<NuclideWaste> findAll() {
		return nuclideWasteDao.findAll();
	}

	@Override
	public NuclideWaste findWasteById(Integer id) {
		return nuclideWasteDao.findWasteById(id);
	}
	
	@Override
	public List<WasteOverview> findWasteByParams() {
		return findWasteByParams(null, null, null);
	}

	@Override
	public List<WasteOverview> findWasteByParams(String nuclideName, String open, String liquid) {
		return nuclideWasteDao.findWasteByParams(nuclideName, open, liquid);
	}
	
	@Override
	public Map<Double, Double> getMaxSumActivityVolume(boolean isLiquid, int wasteId) {
		return nuclideWasteDao.getMaxSumActivityVolume(isLiquid, wasteId);
	}
	
	@Override
	public Map<String, Double> getDisposalRouteActivity(String openStatus) {
		return nuclideWasteDao.getDisposalRouteActivity(openStatus);
	}	
	
	@Override
	public List<String> getDisposalRouteList() {
		return nuclideWasteDao.getDisposalRouteList();
	}
	
	@Override
	public List<NuclideWaste> findNuclideWasteByNuclideName(String nuclideName) {
		return nuclideWasteDao.findNuclideWasteByNuclideName(nuclideName);
	}
	
	@Override
	public List<NuclideWaste> findNuclideWasteByStateStatus(char stateStatus) {
		return nuclideWasteDao.findNuclideWasteByStateStatus(stateStatus);
	}	

	@Override
	public Map<String, String> getUsersForWaste(Integer id) {
		return nuclideWasteDao.getUsersForWaste(id);
	}
	
	@Override
	public boolean save(NuclideWaste nuclideWaste) {
		return nuclideWasteDao.saveNuclideWaste(nuclideWaste);
	}

	@Override
	public void delete(NuclideWaste nuclideWaste) {
		nuclideWasteDao.deleteNuclideWaste(nuclideWaste);
	}

	@Override
	public int getLastId() {
		return nuclideWasteDao.getLastId();
	}	
	
	@Override
	public Double getWasteSum(String nuclideName, String location) {
		return nuclideWasteDao.getWasteSum(nuclideName, location);
	}

	@Override
	public Double getComplementWasteSum(String nuclideName, String location) {
		return nuclideWasteDao.getComplementWasteSum(nuclideName, location);
	}

	@Override
	public Map<String, String> getWasteContainerList(String nuclideName, char isLiquid) {
		return nuclideWasteDao.getWasteContainerList(nuclideName, isLiquid);
	}
	
	@Override
	public Map<String, Integer> findDisposalRouteActivitySum(String nuclideName, String openStatus, String liquid) {
		return nuclideWasteDao.findDisposalRouteActivitySum(nuclideName,openStatus,liquid);
	}

	@Override
	public Object getTargetedObject(String query) {
		return nuclideWasteDao.getTargetedObject(query);
	}

	@Override
	public Set<String> getDiscardedWastes() {
		return nuclideWasteDao.getDiscardedWastes();
	}
	
	@Override
	public void checkIfFifityPercentActivityWasteReached(NuclideWaste nuclideWaste, String userId, HttpServletRequest request) throws AddressException, MessagingException, IOException {
		//check if 3H or 14C waste, if so verify warning limit exceeded (300Mbq for 3H and 4Gbq for 14C)
		boolean isFiftyPercentTritiumWasteLimitReached = false;
		boolean isFiftyPercentCarbonWasteLimitReached = false;
		Double tritiumWasteLimit = 300000.00;
		Double carbonWasteLimit = 4000000.00;
		String subject = "Waste activity has reached 50% (" + nuclideWaste.getNuclideWasteId() + ")";
		if(nuclideWaste.getNuclide().getNuclideName().equals(TRITIUM) || nuclideWaste.getNuclide().getNuclideName().equals(CARBON_14)) {
			
			Double totalTracersInWasteActivity = 0.00;
			Map<String,String> currentActivityKBqLEMap = nuclideUsageService.getCurrentActivityKBqLEMap(nuclideWaste.getIsLiquid(), nuclideWaste.getNuclideWasteId());
			for(Entry<String,String> entry : currentActivityKBqLEMap.entrySet()) {
				if(entry.getKey()!=null) {
					totalTracersInWasteActivity = Double.valueOf(entry.getKey());
				}
				break;
			}
			if(totalTracersInWasteActivity != 0) {
				isFiftyPercentTritiumWasteLimitReached = nuclideWaste.getNuclide().getNuclideName().equals(TRITIUM) && totalTracersInWasteActivity > tritiumWasteLimit ? true : false;
				isFiftyPercentCarbonWasteLimitReached = nuclideWaste.getNuclide().getNuclideName().equals(CARBON_14) && totalTracersInWasteActivity > carbonWasteLimit	 ? true : false;
			}
			boolean testMode = request.getRequestURL() != null && !request.getRequestURL().toString().contains("ares");
			String healAndSafetyExpertEmail = OsirisService.getEmployeeByUserId(HEALTH_SAFETY_EXPERT).getEmail();
			if(isFiftyPercentTritiumWasteLimitReached) {
				emailService.processWasteRadiactivityLimitEmail(nuclideWaste, totalTracersInWasteActivity, tritiumWasteLimit, healAndSafetyExpertEmail, subject, userId, testMode);
			}
			if(isFiftyPercentCarbonWasteLimitReached) {
				emailService.processWasteRadiactivityLimitEmail(nuclideWaste, totalTracersInWasteActivity, carbonWasteLimit, healAndSafetyExpertEmail, subject, userId, testMode);
			}
		}
	}	
	
	
	public NuclideWasteDao getNuclideWasteDao() {
		return nuclideWasteDao;
	}

	public void setNuclideWasteDao(NuclideWasteDao nuclideWasteDao) {
		this.nuclideWasteDao = nuclideWasteDao;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	
}
