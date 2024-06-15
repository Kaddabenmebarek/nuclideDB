package org.research.kadda.nuclide.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.research.kadda.nuclide.entity.NuclideWaste;
import org.research.kadda.nuclide.models.WasteOverview;

public interface NuclideWasteDao {
	
	List<NuclideWaste> findAll();
	
	NuclideWaste findWasteById(Integer id);
	
	List<WasteOverview> findWasteByParams(String nuclideName, String open, String liquid);
	
	Map<Double,Double> getMaxSumActivityVolume(boolean isLiquid, int wasteId);

	Map<String, Double> getDisposalRouteActivity(String openStatus);
	
	List<NuclideWaste> findNuclideWasteByNuclideName(String nuclideName);
	
	List<NuclideWaste> findNuclideWasteByStateStatus(char stateStatus);
	
	Map<String, String> getUsersForWaste(Integer id);
	
	boolean saveNuclideWaste(NuclideWaste nuclideWaste);
	
	void deleteNuclideWaste(NuclideWaste nuclideWaste);

	int getLastId();

	Double getWasteSum(String nuclideName, String location);

	Double getComplementWasteSum(String nuclideName, String location);

	Map<String, String> getWasteContainerList(String nuclideName, char isLiquid);
	
	Map<String, Integer> findDisposalRouteActivitySum(String nuclideName, String openStatus, String liquid);

	List<String> getDisposalRouteList();

	Object getTargetedObject(String query);

	Set<String> getDiscardedWastes();
	
}
