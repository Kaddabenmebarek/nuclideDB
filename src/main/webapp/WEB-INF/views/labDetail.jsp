
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>
<head>
<title>Nuclide DB - Lab Detail</title>
	<jsp:include page="includes/header.jsp" />
</head>
<body>
<jsp:include page="includes/menu.jsp" />
<div class="actionDiv">
		<table class='table' style="width:560px!important">
			<tr>
				<td><div align="left">Lab Name:</div></td>
				<td><div align="left">${selectedLab.locationName}</div></td>
			</tr>
			<tr>
				<td><div align="left">Nuclide:</div></td>
				<td><div align="left">${selectedLab.nuclide}</div></td>
			</tr>			
			<tr>
				<td><div align="left">Activity Threshold:</div></td>
				<td><div align="left">${selectedLab.activityThreshold} (kBq)</div></td>
			</tr>
			<tr>
				<td><div align="left">Total Wastes Activity:</div></td>
				<td><div align="left">${selectedLab.wastesActivity} (kBq)</div></td>
			</tr>
			<tr>
				<td><div align="left">Open Wastes Activity:</div></td>
				<td><div align="left">${selectedLab.openWastesActivity} (kBq)</div></td>
			</tr>
			<tr>
				<td><div align="left">Closed Wastes Activity:</div></td>
				<td><div align="left">${selectedLab.closedWastesActivity} (kBq)</div></td>
			</tr>
			<tr>
				<td><div align="left">Total Tracers Activity:</div></td>
				<td><div align="left">${selectedLab.tracersActivity} (kBq)</div></td>
			</tr>
			<tr>
				<td><div align="left">Lab Total Activity:</div></td>
				<td><div align="left"><b>${selectedLab.totalActivity}</b> (kBq)</div></td>
			</tr>

		</table>
</div>
		<br />
<div class="actionDivDetail">
		<h5>Wastes List</h5>
		<table class="table1">
			<tr>
				<th>Waste Id</th>
				<th>Status</th>
				<th>Activity (kBq)</th>
			</tr>
			<c:forEach items="${selectedLab.wastesList }" var="waste">
				<tr>
					<td><a href="/nuclideDB/wasteDetail_${waste.nuclideWasteId }" class="btn">${waste.nuclideWasteId }</a></td>
					<td>${waste.status }</td>
					<td>${waste.activityKbq }</td>
				</tr>
			</c:forEach>
		</table>

		<h5>Tracers List</h5>

		<table class="table1">
			<tr>
				<th>Tracer Id</th>
				<th>Activity (kBq)</th>
			</tr>
			<c:forEach items="${selectedLab.tracersList }" var="tracer">
				<tr>
					<td><a href="/nuclideDB/tracerDetail_${tracer.tracerId }" class="btn">${tracer.tracerId }</a></td>
					<td>${tracer.currentActivity }</td>
				</tr>
			</c:forEach>
		</table>
</div>
</body>
</html>