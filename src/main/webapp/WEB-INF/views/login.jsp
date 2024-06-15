<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- jsp:include page="includes/menu.jsp"/-->
<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - Login</title>
	<jsp:include page="includes/header.jsp" />

	<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" rel="stylesheet" />
	<link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap" rel="stylesheet" />
	<link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/3.6.0/mdb.min.css" rel="stylesheet" />

	<link rel="stylesheet" type="text/css" href="resources/css/stylesheet.css?v=2.1" >
	<link rel="stylesheet" type="text/css" href="resources/css/stylesheet-dark.css?v=1.0" media="screen and (prefers-color-scheme: dark)">
	<link rel="stylesheet" type="text/css" href="resources/css/oktaStyle.css" />

	<script type="text/javascript" src="resources/js/loginFormValidation.js"></script>
</head>
<body>
	<jsp:include page="includes/menu.jsp" />
	<form:form modelAttribute="user" method="post" action="connect" id="login_form">
		<div id="login-table">

			<div id="okta-login">
				<a href="connectByOkta?redirectToOktaOauth" class="btn btn-primary btn-lg" role="button" id="okta-signin">
					<i class="fas fa-sign-in-alt"></i>
					<span>Sign In with</span>
					<img src="https://www.okta.com/themes/custom/okta_www_theme/images/logo.svg?v2" height="26px" alt="Okta"/>
				</a>
			</div>
			<div id="osiris-login">
				<h4>Login with Osiris</h4>
				<div class="row">
					<form:label path="userId">Username</form:label>
					<form:input path="userId" id="form_userId" />
				</div>
				<div class="row">
					<form:label path="password">Password</form:label>
					<form:password path="password" id="form_userPassword" />
				</div>
				<input type="submit" name="Submit" value="Login">
				<span class="error_form" id="userId_error_message"></span>
				<span class="error_form" id="password_error_message"></span>
			</div>
		</div>
	</form:form>
	<p class="error_form">${loginError}</p>
</body>
</html>