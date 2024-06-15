
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
	
		<p>${newUserMessage}</p>
		<p><input type="submit" name="Submit" value="${buttonText}" onclick="location.href='${buttonLink}'"></p>
	</div>

</body>
</html>