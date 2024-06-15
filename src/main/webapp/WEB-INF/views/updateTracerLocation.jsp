
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - Tracer Relocation Success</title>
	<jsp:include page="includes/header.jsp"/>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div>
		<p>Your tracer tube was successfully relocated.</p>
		<p><input type="submit" name="Submit" value="List tracer tube again" onclick="location.href='showTracers'"></p>
	</div>

</body>
</html>