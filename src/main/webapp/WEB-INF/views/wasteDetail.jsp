
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>
<head>
	<title>Nuclide DB - Waste Detail</title>
	<jsp:include page="includes/header.jsp" />
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div class="actionDiv">
		<table class='table' style="width:560px!important">
			<tr>
				<td><div align="left">Waste Container No:</div></td>
				<td><div align="left">${waste.nuclideWasteId}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Nuclide:</div></td>
				<td><div align="left">${waste.nuclideName}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Aggregation state:</div></td>
				<td><div align="left">${waste.solidLiquidState}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">location:</div></td>
				<td><div align="left">${waste.location}</div></td>
				<td>
					<c:if test="${waste.disposalRoute == 'N/A'}">					
						<spring:url value="/relocateWaste${waste.nuclideWasteId}" var="relocateWastePath" /> 
						<input type="button" class="button1" value="Change" onclick="location.href='${relocateWastePath}'" />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><div align="left">Registered by:</div></td>
				<td><div align="left">${waste.nuclideUserByCreationUserId}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Closed by:</div></td>
				<td><div align="left">${waste.nuclideUserByClosureUserId}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Closure date:</div></td>
				<td><div align="left">${waste.closedOn}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Disposed of by:</div></td>
				<td><div align="left">${waste.nuclideUserByDisposalUserId}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Route of disposal:</div></td>
				<td><div align="left">${waste.disposalRoute}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Activity at disposal (kBq):</div></td>
				<td><div align="left">${waste.activityKbq}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Disposal date:</div></td>
				<td><div align="left">${waste.disposedOfOn}</div></td>
				<td>&nbsp;</td>
			</tr>

		</table>
		</div>
		<br />
		<div class="actionDivDetail">
		<h5>Usage List</h5>

		<table class="table1">
			<tr>
				<th>Tracer No.</th>
				<th>Substance Name</th>
				<th>Amount (ul or mg)</th>
				<th>Bio Lab Journal</th>
				<th>Assay Type</th>
				<th>Usage Date</th>
				<th>Scientist</th>
			</tr>
			<c:forEach items="${wasteUsageList }" var="wasteUsage">
				<tr>
					<td><a href="/nuclideDB/tracerDetail_${wasteUsage.tracerId}" class="btn">${wasteUsage.tracerId }</a></td>
					<td align="left">${wasteUsage.substanceName }</td>
					<td>${wasteUsage.amount }</td>
					<td align="left">${wasteUsage.bioLabJournal }</td>
					<td align="left">${wasteUsage.assayType }</td>
					<td>${wasteUsage.usageDate }</td>
					<td align="left">${wasteUsage.scientist }</td>
				</tr>
			</c:forEach>
		</table>
		</div>
</body>
</html>