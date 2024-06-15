package org.research.kadda.nuclide.filter;

import org.research.kadda.oauth.OktaLogon;

import org.research.kadda.nuclide.controller.HomeController;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Order(1)
public class LoginFilter extends GenericFilterBean {

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        if (!uri.startsWith(contextPath + HomeController.OKTA_LOGIN_URL)
                && !uri.startsWith(contextPath +"/login")
                && !uri.startsWith(contextPath +"/connect")
                && !uri.equals(contextPath +"/errorCannotRemove")
                && !uri.equals(contextPath +"/errorCannotChange")
                && !uri.equals(contextPath +"/errorLogin")
                && !uri.startsWith(contextPath +"/resources/")
                && session.getAttribute(OktaLogon.USERNAME_ATTRIBUTE)==null) {


            String requestedPage = req.getRequestURL().toString();
            String redirectPage = HomeController.OKTA_LOGIN_PATH;
            if(req.getQueryString() !=null){
                requestedPage += (requestedPage.contains("?") ? "&" : "?") + req.getQueryString();
                redirectPage += (redirectPage.contains("?") ? "&" : "?") + req.getQueryString();
            }
            if(!uri.equals(contextPath +"/sat")) {
                session.setAttribute(OktaLogon.REQUESTED_PAGE, requestedPage);
            }
            res.sendRedirect(redirectPage);
        } else {
            chain.doFilter(request, res);
        }
    }
}
