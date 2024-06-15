package org.research.kadda.nuclide.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.research.kadda.nuclide.entity.NuclideUsage;
import org.research.kadda.nuclide.models.Usage;
import org.research.kadda.nuclide.models.UsageOverview;


public interface NuclideUsageDao {

	List<NuclideUsage> findAllUsage();
	
	NuclideUsage findByUsageId(int usageId);
	
	Date getUsageDateByTracerId(Integer tracerId);
	
	List<Usage> getWasteUsageList(int wasteId, char solidLiquidStatus);

	Map<String, String> getCurrentActivityKBqLEMap(char isLiquid, int nuclideWasteId);
	
	boolean saveNuclide(NuclideUsage nuclideUsage);

	int finLastIndex();

	double getSumVolumePerTracerId(Integer tracerId);

	String getElb(int tracerId);

	Integer getParentTracerTube(int tracerId);

	int getLastId();

	String getCurrentActivityKBq(char isLiquid, int nuclideWasteId);
	
	String getInVivoActivityKBq(int nuclideWasteId);

	List<UsageOverview> getTracerUsageList(int nuclideBottleId, boolean isChildren);

	boolean isChildren(int nuclideBottleId);

	List<UsageOverview> getInVivoUsages(int nuclideBottleId);

	Double getSolidWastePercentage(int nuclideBottleId);

	List<NuclideUsage> getWasteUsages(int nuclideWasteId);

	List<Integer> getAllTracersInsideWaste(int nuclideWasteId);

	Date getUsageDate(int tracerId);
	
	int getLastSeq();

	Double getWasteVolume(int nuclideBottleId);
	
	String getDestination(int tracerId);

	Integer getParent(int id);

	List<NuclideUsage> findByNuclideBottleId(int tracerId);

}