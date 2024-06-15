package org.research.kadda.nuclide.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.research.kadda.nuclide.dao.NuclideDao;
import org.research.kadda.nuclide.entity.Nuclide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "nuclideUserService")
public class NuclideServiceImpl implements NuclideService {
	
	@Autowired
	private NuclideDao nuclideDao;

	@Override
	public List<Nuclide> findAll() {
		return nuclideDao.findAll();
	}
	
	@Override
	public List<Nuclide> getNuclideList() {
		return nuclideDao.getNuclideList();
	}
	
	@Override
	public Nuclide findByName(String nuclideName) {
		return nuclideDao.findByName(nuclideName);
	}
	
	@Override
	public boolean save(Nuclide nuclide) {
		return nuclideDao.saveNuclide(nuclide);
	}
	
	@Override
	public void delete(Nuclide nuclide) {
		nuclideDao.deleteNuclide(nuclide);
	}

	public NuclideDao getNuclideDao() {
		return nuclideDao;
	}

	public void setNuclideDao(NuclideDao nuclideDao) {
		this.nuclideDao = nuclideDao;
	}

	@Override
	public Map<String,String> findAllUserId() {
		return nuclideDao.findAllUserId();
	}

	@Override
	public Map<String, Set<String>> getTracersIntoDiscardedWasteLocation(char liquidState) {
		return nuclideDao.getTracersIntoDiscardedWasteLocation(liquidState);
	}

}
