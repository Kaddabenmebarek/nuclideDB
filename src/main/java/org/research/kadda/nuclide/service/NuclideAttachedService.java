package org.research.kadda.nuclide.service;

import java.util.List;

import org.research.kadda.nuclide.entity.NuclideAttached;

public interface NuclideAttachedService {

	List<NuclideAttached> findAll();

	NuclideAttached findById(int nuclideAttachedId);
	
	List<NuclideAttached> findByTracerId(int nuclideBottleId);

	boolean saveNuclideAttached(NuclideAttached nuclideAttached);

	void deleteNuclideAttached(NuclideAttached nuclideAttached);

	int getLastIndex();

}
