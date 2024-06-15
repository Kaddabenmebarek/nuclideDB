package org.research.kadda.nuclide.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.research.kadda.nuclide.entity.Nuclide;

public interface NuclideDao {
	
	List<Nuclide> findAll();
	
	List<Nuclide> getNuclideList();
	
	Nuclide findByName(String nuclideName);
	
	boolean saveNuclide(Nuclide nuclide);
	
	void deleteNuclide(Nuclide nuclide);

	Map<String,String> findAllUserId();

	Map<String, Set<String>> getTracersIntoDiscardedWasteLocation(char liquidState);
}
