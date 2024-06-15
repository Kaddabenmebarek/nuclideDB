
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - Insert Tracer Tube</title>
	<jsp:include page="includes/header.jsp"/>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div>
	
		<p class="error_form">Something went wrong when trying to save your changes. It looks like you do not have enough privileges.</p>
		<p class="error_form">Please contact the admin.</p>
	</div>

</body>
</html>