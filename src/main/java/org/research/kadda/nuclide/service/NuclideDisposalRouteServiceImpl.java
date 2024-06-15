package org.research.kadda.nuclide.service;

import java.util.List;

import org.research.kadda.nuclide.dao.NuclideDisposalRouteDao;
import org.research.kadda.nuclide.entity.NuclideDisposalRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NuclideDisposalRouteServiceImpl implements NuclideDisposalRouteService {

	@Autowired
	private NuclideDisposalRouteDao nuclideDisposalRouteDao;
	
	@Override
	public List<NuclideDisposalRoute> findAll() {
		return nuclideDisposalRouteDao.findAll();
	}

	public NuclideDisposalRouteDao getNuclideDisposalRouteDao() {
		return nuclideDisposalRouteDao;
	}

	public void setNuclideDisposalRouteDao(NuclideDisposalRouteDao nuclideDisposalRouteDao) {
		this.nuclideDisposalRouteDao = nuclideDisposalRouteDao;
	}

	
	
}
