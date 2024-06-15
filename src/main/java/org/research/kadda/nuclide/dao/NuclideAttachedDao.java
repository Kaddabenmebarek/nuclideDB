package org.research.kadda.nuclide.dao;

import java.util.List;

import org.research.kadda.nuclide.entity.NuclideAttached;

public interface NuclideAttachedDao {

	List<NuclideAttached> findAll();

	NuclideAttached findById(int nuclideAttachedId);
	
	List<NuclideAttached> findByTracerId(int nuclideBottleId);

	boolean saveNuclideAttached(NuclideAttached nuclideAttached);

	void deleteNuclideAttached(NuclideAttached nuclideAttached);

	int getLastIndex();

}
