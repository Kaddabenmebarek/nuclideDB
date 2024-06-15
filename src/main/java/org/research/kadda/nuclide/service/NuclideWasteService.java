package org.research.kadda.nuclide.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.research.kadda.nuclide.entity.NuclideWaste;
import org.research.kadda.nuclide.models.WasteOverview;

public interface NuclideWasteService {

	List<NuclideWaste> findAll();
	
	NuclideWaste findWasteById(Integer id);
	
	List<String> getDisposalRouteList();
	
	List<WasteOverview> findWasteByParams();
	
	List<WasteOverview> findWasteByParams(String nuclideName, String open, String liquid);
	
	Map<Double,Double> getMaxSumActivityVolume(boolean isLiquid, int wasteId);
	
	Map<String, Double> getDisposalRouteActivity(String openStatus);
	
	List<NuclideWaste> findNuclideWasteByNuclideName(String nuclideName);
	
	List<NuclideWaste> findNuclideWasteByStateStatus(char stateStatus);
	
	Map<String, String> getUsersForWaste(Integer id);
	
	boolean save(NuclideWaste nuclideWaste);
	
	void delete(NuclideWaste nuclideWaste);

	int getLastId();

	Double getWasteSum(String nuclideName, String location);

	Double getComplementWasteSum(String nuclideName, String location);

	Map<String, String> getWasteContainerList(String nuclideName, char isLiquid);
	
	Map<String, Integer> findDisposalRouteActivitySum(String nuclideName, String openStatus, String liquid);

	Object getTargetedObject(String query);

	Set<String> getDiscardedWastes();

	void checkIfFifityPercentActivityWasteReached(NuclideWaste waste, String userId, HttpServletRequest request) throws AddressException, MessagingException, IOException;
	
}
