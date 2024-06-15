
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nuclide DB - Close waste Success</title>
	<jsp:include page="includes/header.jsp"/>
</head>
<body>
	<jsp:include page="includes/menu.jsp" />
	<div>
		<p>Your waste closure date has been set successfully.</p>
		<p><input type="submit" name="Submit" value="Show waste list" onclick="location.href='showWaste'"></p>
	</div>

</body>
</html>