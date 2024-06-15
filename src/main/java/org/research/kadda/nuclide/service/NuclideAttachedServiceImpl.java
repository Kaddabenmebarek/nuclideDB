package org.research.kadda.nuclide.service;

import java.util.List;

import org.research.kadda.nuclide.dao.NuclideAttachedDao;
import org.research.kadda.nuclide.entity.NuclideAttached;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NuclideAttachedServiceImpl implements NuclideAttachedService {

	@Autowired
	private NuclideAttachedDao nuclideAttachedDao;	
	
	@Override
	public List<NuclideAttached> findAll() {
		return nuclideAttachedDao.findAll();
	}

	@Override
	public NuclideAttached findById(int nuclideAttachedId) {
		return nuclideAttachedDao.findById(nuclideAttachedId);
	}
	
	@Override
	public List<NuclideAttached> findByTracerId(int nuclideBottleId) {
		return nuclideAttachedDao.findByTracerId(nuclideBottleId);
	}	

	@Override
	public boolean saveNuclideAttached(NuclideAttached nuclideAttached) {
		return nuclideAttachedDao.saveNuclideAttached(nuclideAttached);
	}

	@Override
	public void deleteNuclideAttached(NuclideAttached nuclideAttached) {
		nuclideAttachedDao.deleteNuclideAttached(nuclideAttached);
	}

	@Override
	public int getLastIndex() {
		return nuclideAttachedDao.getLastIndex();
	}

	public NuclideAttachedDao getNuclideAttachedDao() {
		return nuclideAttachedDao;
	}

	public void setNuclideAttachedDao(NuclideAttachedDao nuclideAttachedDao) {
		this.nuclideAttachedDao = nuclideAttachedDao;
	}

}
