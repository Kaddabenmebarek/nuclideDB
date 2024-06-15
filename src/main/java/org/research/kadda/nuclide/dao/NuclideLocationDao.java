package org.research.kadda.nuclide.dao;

import java.util.List;

import org.research.kadda.nuclide.entity.NuclideLocation;
import org.research.kadda.nuclide.models.LocationAllowance;

public interface NuclideLocationDao {
	
	List<NuclideLocation> findAll();

	Double getAllowance(String nuclideName, String location);

	List<LocationAllowance> findAllLocationAllowances();

}
