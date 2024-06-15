package org.research.kadda.nuclide.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.research.kadda.nuclide.entity.Nuclide;

public interface NuclideService {
	
	List<Nuclide> findAll();
	
	List<Nuclide> getNuclideList();
	
	Nuclide findByName(String nuclideName);
	
	boolean save(Nuclide nuclide);
	
	void delete(Nuclide nuclide);

	Map<String,String> findAllUserId();

	Map<String, Set<String>> getTracersIntoDiscardedWasteLocation(char c);

}
