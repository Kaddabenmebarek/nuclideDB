package org.research.kadda.nuclide.service;

import java.util.List;

import org.research.kadda.nuclide.entity.NuclideLocation;
import org.research.kadda.nuclide.models.LocationAllowance;

public interface NuclideLocationService {

	List<NuclideLocation> findAll();

	Double getAllowance(String nuclideName, String location);
	
	List<LocationAllowance> findAllLocationAllowances();
	
}
