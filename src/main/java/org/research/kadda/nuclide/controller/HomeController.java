package org.research.kadda.nuclide.controller;

import org.research.kadda.security.entity.User;
import org.research.kadda.security.exception.AuthenticationException;
import org.research.kadda.security.exception.DataReadAccessException;
import org.research.kadda.security.service.UserServiceFactory;
import org.research.kadda.util.SSO;
import org.research.kadda.oauth.ConnectorUtils;
import org.research.kadda.oauth.OktaLogon;
import org.research.kadda.oauth.OktaLogonUser;

import org.research.kadda.nuclide.models.NuclideUserLogin;
import org.research.kadda.nuclide.service.NuclideUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;

@Controller
public class HomeController {

	@Autowired
	private NuclideUserService nuclideUserService;
	
	private final Logger logger = LoggerFactory.getLogger(HomeController.class);
	public static final String OKTA_LOGIN_PATH = "connectByOkta";
	public static final String OKTA_LOGIN_URL = "/" + OKTA_LOGIN_PATH;


	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView displayLogout(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("redirect:/login");
		ConnectorUtils.clearAccessTokenAndUserFromSession(request);
		HttpSession session = request.getSession();
		session.removeAttribute(OktaLogon.USERNAME_ATTRIBUTE);
		session.removeAttribute("userInfo");
		session.removeAttribute("userPicture");
		//This is destroying the session
		SSO.logout(request, response);
		return mv;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView displayLogin(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		NuclideUserLogin nuclideUserLogin = new NuclideUserLogin();

		String loginError = (String) session.getAttribute("loginError");
		if (loginError != null) {
			session.removeAttribute("loginError");
			ModelAndView mv = new ModelAndView("/login");
			mv.addObject("user", nuclideUserLogin);
			mv.addObject("loginError", loginError);
			return mv;
		}

		String[] credentials = SSO.retrieveLogin(request, response);
		if(session.getAttribute("username") == null && (credentials != null && credentials[0] != null)) {
			String username = credentials[0].toUpperCase();
			session.setAttribute("username", username);

			ModelAndView mv = new ModelAndView("redirect:/");
			mv.addObject("nuclideUserId", username.toUpperCase());
			mv.addObject("user", nuclideUserLogin);
			return mv;
		}

		ModelAndView mv = new ModelAndView("/login");
		mv.addObject("user", nuclideUserLogin);
		return mv;
	}

	@RequestMapping(value = OKTA_LOGIN_URL, method = RequestMethod.GET)
	public ModelAndView connectByOkta(@ModelAttribute("SpringWeb") NuclideUserLogin nuclideUserLogin, HttpServletRequest request) {
		HttpSession session = request.getSession();

		OktaLogonUser oktaLogon = new OktaLogonUser(request){
			@Override
			public String handleNullAccessToken(String logonPage) throws MalformedURLException {
				String requestedPage = getRequestedPage();
				if(requestedPage.endsWith(logonPage)){
					return null;
				}
				return redirectToOktaOauthUrl();
			}

			@Override
			public boolean isMember(User user){
				return isUserNuclideUser(user);
			}
		};

		if (request.getParameter("redirectToOktaOauth") != null) {
			try {
				String oktaRedirect = oktaLogon.redirectToOktaOauthUrl();
				return new ModelAndView("redirect:" + oktaRedirect);
			} catch (MalformedURLException e) {
				logger.error(e.getMessage(), e);
				session.setAttribute("loginError", e.getMessage());
				return new ModelAndView("redirect:/login");
			}
		}

		String username = null;
		try {
			String redirect = oktaLogon.logon("/login");
			if(redirect != null){
				return new ModelAndView("redirect:" + redirect );
			}
			username = (String) session.getAttribute(OktaLogon.USERNAME_ATTRIBUTE);
		} catch (Exception e) {
			session.setAttribute("loginError", e.getMessage());
			return new ModelAndView("redirect:/login");
		}

		if(username == null){
			return new ModelAndView("redirect:/login");
		}

		ModelAndView mv = new ModelAndView("redirect:/");
		return mv;
	}

	public boolean isUserNuclideUser(User user){
		return nuclideUserService.findByUserId(user.getUsername()) != null;
	}

	@RequestMapping(value = "/connect", method = RequestMethod.POST)
	public ModelAndView connectWithOsiris(@ModelAttribute("SpringWeb") NuclideUserLogin nuclideUserLogin, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		String username = nuclideUserLogin.getUserId();
		User user = null;
		try {
			user = UserServiceFactory.getUserService().authenticateOsirisUser(username, nuclideUserLogin.getPassword(), true);
		} catch (AuthenticationException | DataReadAccessException e) {
			session.setAttribute("loginError", e.getMessage());
			logger.error(e.getMessage(), e);
			return new ModelAndView("redirect:/login");
		}
		if (user != null) {
			if(isUserNuclideUser(user)){
				String nuclideUserId = username.toUpperCase();

				session.setAttribute(OktaLogon.USER_ATTRIBUTE, user);
				session.setAttribute(OktaLogon.USERNAME_ATTRIBUTE, nuclideUserId);
				SSO.saveLogin(request, response, nuclideUserId, nuclideUserLogin.getPassword());

				ModelAndView mv = new ModelAndView("redirect:/");
				mv.addObject("nuclideUserId", nuclideUserId);
				return mv;
			} else {
				session.removeAttribute(OktaLogon.USERNAME_ATTRIBUTE);
				session.setAttribute("loginError", "Not having rights to this application");
				return new ModelAndView("redirect:/login");
			}
		}
		return new ModelAndView("redirect:/login");
	}
	
	@RequestMapping("/main")
	public String displayMain(Model model) {
		return "main";
	}
	
	@RequestMapping("/main.jsp")
	public String displayMain2(Model model) {
		return "main";
	}
	
	@RequestMapping(value = "/errorCannotRemove", method = RequestMethod.GET)
    public ModelAndView renderErrorRemovePage(ModelAndView model) {
		ModelAndView errorPage = new ModelAndView("error");
		String errorMessage = "You cannot remove this document because you are not the person who uploaded it.";
        errorPage.addObject("errorMessage", errorMessage);
        return errorPage;
	}
	
	@RequestMapping(value = "/errorCannotChange", method = RequestMethod.GET)
    public ModelAndView renderErrorChangePage(ModelAndView model) {
		ModelAndView errorPage = new ModelAndView("error");
		String errorMessage = "Something went wrong when trying to save your changes. It looks like you do not have enough privileges. Please contact the admin.";
        errorPage.addObject("errorMessage", errorMessage);
        return errorPage;
	}

	@RequestMapping(value = "/userManual", method = RequestMethod.GET)
	public String getUserManual() {
		return "userManual";
	}
}
