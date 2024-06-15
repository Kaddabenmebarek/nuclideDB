package org.research.kadda.nuclide.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.research.kadda.nuclide.entity.Nuclide;
import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.models.UserOverview;
import org.research.kadda.nuclide.service.NuclideService;
import org.research.kadda.nuclide.service.NuclideUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("session")
public class NuclideUserController {

	@Autowired
	private NuclideUserService nuclideUserService;
	
	@Autowired
	private NuclideService nuclideService;	
	
	public static final String DOMAIN = "@kadda.com";
	private Map<String,Boolean> employeeStatusMap;
	private Map<String,Boolean> employeeGoneMap;
	private UserOverview transcientUserOverview;
	
	@RequestMapping("/showUsers")
	public String showUsers(UserOverview userOverview, Model model) {
		synchronizeUsersStatus();
		model.addAttribute("userOverview", userOverview);
		return "showUsers";
	}

	@RequestMapping("/listUsers")
	public ModelAndView listUsers(UserOverview userOverview, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/listUsers");
		if(userOverview.getIsActive() == null && userOverview.getLastUsageDate() == null && userOverview.getUser() == null) {
			if(transcientUserOverview == null){
				return new ModelAndView("redirect:/showUsers");
			}
			userOverview = transcientUserOverview;
		}
		String nuclideName = userOverview.getNuclide();
		String userId = userOverview.getUser();
		String usageStatus = userOverview.getLastUsageDate();
		String userStatus = userOverview.getIsActive();
		if(usageStatus.equalsIgnoreCase("last")) {
			model.addAttribute("nuclideUser_displaynone", "all");
		}else {
			model.addAttribute("nuclideUser_displaynone", "last");
		}
		List<UserOverview> nuclideUsers = nuclideUserService.getUserOverviewList(nuclideName, userId, usageStatus, userStatus.charAt(0));
		Collections.sort(nuclideUsers, new Comparator<UserOverview>() {
			@Override
			public int compare(UserOverview u1, UserOverview u2) {
				return u1.getUser().compareTo(u2.getUser());
			}
		});
		
		StringBuilder mailtoSb = new StringBuilder("mailto:");
		for(int i = 0; i< nuclideUsers.size(); i++) {
			UserOverview nuclideUser = nuclideUsers.get(i);
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			try {
				if(nuclideUser.getLastUsageDate()!= null) {
					Date lastUsageDate = format.parse(nuclideUser.getLastUsageDate());
					nuclideUser.setLastUsageDate(new SimpleDateFormat("yyyy-MMM-dd",Locale.US).format(lastUsageDate));
				}else {
					nuclideUser.setLastUsageDate("N/A");
				}
				if(nuclideUser.getUsageDate()!= null) {
					Date usageDate = format.parse(nuclideUser.getUsageDate());
					nuclideUser.setUsageDate(new SimpleDateFormat("yyyy-MMM-dd",Locale.US).format(usageDate));						
				}else {
					nuclideUser.setUsageDate("N/A");						
				}
			} catch (ParseException e) {
				e.getMessage();
			}
			if(employeeGoneMap.get(nuclideUser.getUserId().toLowerCase()) != null && !employeeGoneMap.get(nuclideUser.getUserId().toLowerCase())) {
				mailtoSb.append(nuclideUser.getUser().split(" ")[0].toLowerCase()).append(".").append(nuclideUser.getUser().split(" ")[1].toLowerCase()).append(DOMAIN);
				if(i < nuclideUsers.size() -1) {
					mailtoSb.append(";");
				}
				nuclideUser.setInKadda("Y");
			}else {
				nuclideUser.setInKadda("N");
			}
		}
		String mailto = StringUtils.stripAccents(mailtoSb.toString());
		
		HttpSession session = request.getSession();
		String currentUserId = (String) session.getAttribute("username");
		NuclideUser nuclideUser = nuclideUserService.findByUserId(currentUserId.toUpperCase());
		String isUserAdmin = nuclideUser == null ? "N" : ("admin".equals(nuclideUser.getRole()) ? "Y" : "N");
		String isActiveRadio = "Active".equals(nuclideUsers.get(0).getIsActive()) ? "Y" : "N";
		
		mv.addObject("nuclideUsers", nuclideUsers);
		mv.addObject("mailto", mailto);
		mv.addObject("isUserAdmin", isUserAdmin);
		mv.addObject("isActiveRadio", isActiveRadio);
		return mv;
	}
	
	@RequestMapping("/showAddUser")
	public String showAddUser(UserOverview userOverview, Model model) {
		model.addAttribute("userOverview", userOverview);
		Map<String,String> userIdList = new TreeMap<String,String>();
		Map<String, String> userIdMap = nuclideService.findAllUserId();
		for (Entry<String,String> entry : userIdMap.entrySet()) {
			userIdList.put(entry.getKey(), entry.getKey());
		}
		model.addAttribute("userIdList", userIdList);
		model.addAttribute("userIdMap", userIdMap);
		return "showAddUser";
	}
	
	@RequestMapping(value = { "/addUser" }, method = RequestMethod.POST)
	public ModelAndView addUser(UserOverview userOverview, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/newUserMessage");
		
		NuclideUser nuclideUser = new NuclideUser();
		nuclideUser.setUserId(userOverview.getUserId().toUpperCase());
		nuclideUser.setFirstName(userOverview.getFirstName());
		nuclideUser.setLastName(userOverview.getLastName());
		nuclideUser.setIsActive('Y');
		nuclideUser.setRole("regular".equals(userOverview.getRole()) ? null : userOverview.getRole());
		
		StringBuilder sb = new StringBuilder("The user ");
		sb.append(nuclideUser.getFirstName()).append(" ").append(nuclideUser.getLastName());		
		if(nuclideUserService.findByUserId(userOverview.getUserId().toUpperCase()) != null) {
			sb.append(" already exists, please make sure to add a user that is not a NuclideDB user.");
		}else {
			nuclideUserService.saveUser(nuclideUser);
			sb.append(" has been successfully added.");
		}
		model.addAttribute("newUserMessage",sb.toString());
		model.addAttribute("buttonText","Register another User");
		model.addAttribute("buttonLink","showAddUser");
		return mv;
		
	}
	
	@RequestMapping(value = "/showEditUser{userId}", method = RequestMethod.GET)
	public ModelAndView showEditUser(@PathVariable String userId) {
		ModelAndView mv = new ModelAndView("/showEditUser");
		NuclideUser user = nuclideUserService.findByUserId(userId);
		
		UserOverview userToEdit = new UserOverview();
		userToEdit.setUserId(userId);
		userToEdit.setFirstName(user.getFirstName());
		userToEdit.setLastName(user.getLastName());
		userToEdit.setRole(user.getRole() == null ? "regular" : "admin");
		
		mv.addObject("userToEdit", userToEdit);
		
		return mv;
	}
	
	@RequestMapping(value = { "/editUser" }, method = RequestMethod.POST)
	public ModelAndView editUser(UserOverview userOverview, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/newUserMessage");
		
		NuclideUser nuclideUser = nuclideUserService.findByUserId(userOverview.getUserId().toUpperCase());
		nuclideUser.setRole("regular".equals(userOverview.getRole()) ? null : userOverview.getRole());
		
		StringBuilder sb = new StringBuilder("The user ");
		sb.append(nuclideUser.getFirstName()).append(" ").append(nuclideUser.getLastName());		
		nuclideUserService.saveUser(nuclideUser);
		sb.append(" has been successfully updated.");
		model.addAttribute("newUserMessage",sb.toString());
		model.addAttribute("buttonText","Go to Users");
		model.addAttribute("buttonLink","showUsers");
		return mv;
		
	}
	
	@RequestMapping("/newUserMessage")
	public String newUserMessage() {
		return "newUserMessage";
	}
	
	@RequestMapping(value = "/changeStatus{userId}", method = RequestMethod.GET)
	public ModelAndView changeStatus(@PathVariable String userId) {
		ModelAndView mv = new ModelAndView("redirect:/listUsers");
		NuclideUser user = nuclideUserService.findByUserId(userId);
		char status = user.getIsActive() == 'Y' ? 'N' : 'Y';
		user.setIsActive(status);
		nuclideUserService.saveUser(user);
		transcientUserOverview = new UserOverview();
		transcientUserOverview.setIsActive(status == 'Y' ? "N" : "Y");
		transcientUserOverview.setLastUsageDate("last");
		transcientUserOverview.setUser("any");
		return mv;
	}
	
	@ModelAttribute("nuclideList")
	public Map<String, String> getNuclideList() {
		Map<String, String> nuclideList = new HashMap<String, String>();
		for (Nuclide nuclide : nuclideService.findAll()) {
			nuclideList.put(nuclide.getNuclideName(), nuclide.getNuclideName());
		}
		return nuclideList;
	}	
	
	@ModelAttribute("responsibleList")
	public Map<String, String> getResponsibleList() {
		Map<String, String> responsibleList = new TreeMap<String, String>();
		List<NuclideUser> nuclideUsers = nuclideUserService.findAll();

		for (NuclideUser nuclideUser : nuclideUsers) {
			if (nuclideUser.getIsActive() == 'Y') {
				responsibleList.put(nuclideUser.getUserId(),
						nuclideUser.getFirstName() + " " + nuclideUser.getLastName());
			}
		}
		return responsibleList;
	}
	
	private void synchronizeUsersStatus() {
		//get all nuclide users
		Map<String,Boolean> nuclideUserStatusMap = new HashMap<String, Boolean>();
		Map<String,NuclideUser> nuclideUserMap = new HashMap<String, NuclideUser>();
		List<NuclideUser> allNuclideUsers = nuclideUserService.findAll();
		for(NuclideUser nuclideUser : allNuclideUsers) {
			nuclideUserStatusMap.put(nuclideUser.getUserId().toLowerCase(), nuclideUser.getIsActive() == 'Y' ? true : false);
			nuclideUserMap.put(nuclideUser.getUserId().toLowerCase(), nuclideUser);
		}
		//get nuclide user status from employee table
		employeeStatusMap = nuclideUserService.getUsersStatusFromEmployee(allNuclideUsers);
		employeeGoneMap = nuclideUserService.getUsersGoneStatusFromEmployee(allNuclideUsers);
		//update user status
		List<NuclideUser> usersToUpdate = new ArrayList<NuclideUser>();
		for(Entry<String,Boolean> entry : employeeStatusMap.entrySet()) {
			if(!entry.getValue() && nuclideUserStatusMap.get(entry.getKey())) {
				NuclideUser userToUpdate = nuclideUserMap.get(entry.getKey());
				userToUpdate.setIsActive('N');
				usersToUpdate.add(userToUpdate);
			}
		}
		if(!CollectionUtils.isEmpty(usersToUpdate)) {
			nuclideUserService.updateAll(usersToUpdate);
		}
	}

}
