
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Nuclide DB - Discard Tracer Success</title>
	<jsp:include page="includes/header.jsp"/>
</head>
<body>
	<jsp:include page="includes/menu.jsp" />
	<div>
		<p>Your tracer disposal date has been set successfully.</p>
		<p><input type="submit" name="Submit" value="Show tracer list" onclick="location.href='showTracers'"></p>
	</div>

</body>
</html>