package org.research.kadda.nuclide.service;

import java.util.List;
import java.util.Map;

import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.models.UserOverview;

public interface NuclideUserService {
	
	List<NuclideUser> findAll();
	
	NuclideUser findByUserId(String userId);
	
	List<UserOverview> getUserOverviewList(String nuclideName, String userId, String lastUsageOnly, char userStatus);
	
	List<NuclideUser> findUserByStatus(char isActive);

	Map<String, Boolean> getUsersStatusFromEmployee(List<NuclideUser> allNuclideUsers);

	Map<String, Boolean> getUsersGoneStatusFromEmployee(List<NuclideUser> allNuclideUsers);

	void updateAll(List<NuclideUser> usersToUpdate);
	
	void saveUser(NuclideUser nuclideUser);


}
