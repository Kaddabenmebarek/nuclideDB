package org.research.kadda.nuclide.dao;

import java.util.List;
import java.util.Map;

import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.models.TracerHierarchy;
import org.research.kadda.nuclide.models.TracerOverview;

public interface NuclideBottleDao {

	List<NuclideBottle> findAll();

	NuclideBottle findById(int nuclideBottleId);

	List<TracerOverview> findNuclideBottleByParam(String nuclideName, String location, String initialActivityYear,
			String discardedStatus, String isLiquid, String daughter, String scientist, String tracerType);

	Double getVolumeDifference(double volume, int tracerId);
	
	boolean saveNuclideBottle(NuclideBottle nuclideBottle);

	void deleteNuclideBottle(NuclideBottle nuclideBottle);

	int getLastIndex();

	List<NuclideBottle> findUsageTraceTubeList();

	NuclideBottle findByIdAndDisposalStatus(int nuclideBottleId);

	Double getTracerSum(String nuclideName, String location);

	Map<String, String> getUsersForTracer(int tracerId);

	Object getTargetedObject(String query);

	List<TracerHierarchy> getTracerHierarchyList(String isotope);

}
