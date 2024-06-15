package org.research.kadda.nuclide.service;

import java.util.List;
import java.util.Map;

import org.research.kadda.nuclide.dao.NuclideUserDao;
import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.models.UserOverview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NuclideUserServiceImpl implements NuclideUserService {

	@Autowired
	private NuclideUserDao nuclideUserDao;

	@Override
	public List<NuclideUser> findAll() {
		return nuclideUserDao.findAll();
	}

	@Override
	public NuclideUser findByUserId(String userId) {
		return nuclideUserDao.findByUserId(userId.toUpperCase());
	}

	@Override
	public List<UserOverview> getUserOverviewList(String nuclideName, String userId, String lastUsageOnly,
			char isActive) {
		return nuclideUserDao.getUserOverviewList(nuclideName, userId.toUpperCase(), lastUsageOnly, isActive);
	}

	@Override
	public List<NuclideUser> findUserByStatus(char isActive) {
		return nuclideUserDao.findUserByStatus(isActive);
	}

	public NuclideUserDao getNuclideUserDao() {
		return nuclideUserDao;
	}

	public void setNuclideUserDao(NuclideUserDao nuclideUserDao) {
		this.nuclideUserDao = nuclideUserDao;
	}

	@Override
	public Map<String, Boolean> getUsersStatusFromEmployee(List<NuclideUser> allNuclideUsers) {
		return nuclideUserDao.getUsersStatusFromEmployee(allNuclideUsers);
	}

	@Override
	public Map<String, Boolean> getUsersGoneStatusFromEmployee(List<NuclideUser> allNuclideUsers) {
		return nuclideUserDao.getUsersGoneStatusFromEmployee(allNuclideUsers);
	}

	@Override
	public void updateAll(List<NuclideUser> usersToUpdate) {
		nuclideUserDao.updateAll(usersToUpdate);
	}

	@Override
	public void saveUser(NuclideUser nuclideUser) {
		nuclideUserDao.saveUser(nuclideUser);
	}

}
