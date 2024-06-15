package org.research.kadda.nuclide.service;

import java.util.List;

import org.research.kadda.nuclide.dao.NuclideLocationDao;
import org.research.kadda.nuclide.entity.NuclideLocation;
import org.research.kadda.nuclide.models.LocationAllowance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NuclideLocationServiceImpl implements NuclideLocationService {

	@Autowired
	private NuclideLocationDao nuclideLocationDao;
	
	@Override
	public List<NuclideLocation> findAll() {
		return nuclideLocationDao.findAll();
	}

	public NuclideLocationDao getNuclideLocationDao() {
		return nuclideLocationDao;
	}

	public void setNuclideLocationDao(NuclideLocationDao nuclideLocationDao) {
		this.nuclideLocationDao = nuclideLocationDao;
	}

	@Override
	public Double getAllowance(String nuclideName, String location) {
		return nuclideLocationDao.getAllowance(nuclideName, location);
	}

	@Override
	public List<LocationAllowance> findAllLocationAllowances() {
		return nuclideLocationDao.findAllLocationAllowances();
	}

}
