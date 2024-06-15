<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>


<!DOCTYPE html>
<html>
<head>
<title>Nuclide DB - Insert Tracer Tube Error</title>
	<jsp:include page="includes/header.jsp"/>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div>
		<p class="error_form">${errorMessage}</p>
		<p><input type="submit" name="Submit" value="Go back" onclick="location.href='javascript:history.back()'"></p>
	</div>

</body>
</html>