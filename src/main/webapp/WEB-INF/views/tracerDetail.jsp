
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
	<title>Nuclide DB - Tracer Detail ${tracer.tracerId}</title>
	<jsp:include page="includes/header.jsp" />
</head>
<body>
	<jsp:include page="includes/menu.jsp" />
<div class="actionDiv">
	<table class='table' style="width:560px!important">
		<tr>
			<td><div align="left">Tracer Tube No:</div></td>
			<td><div align="left">${tracer.tracerId}</div></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="left">Parent Tracer Tube No:</div></td>
			<td>
				<div align="left">
					<c:choose>
						<c:when test="${tracer.parentTracerTube == 'N/A'}">
							${tracer.parentTracerTube }
						</c:when>
						<c:otherwise>
							<a href="/nuclideDB/tracerDetail_${tracer.parentTracerTube }" class="btn2">${tracer.parentTracerTube }</a>
						</c:otherwise>
					</c:choose>
				</div>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="left">ELB (tracer creation):</div></td>
			<td><div align="left">${tracer.elbTracerCreation}</div></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="left">Nuclide:</div></td>
			<td><div align="left">${tracer.nuclideName}</div></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="left">Substance:</div></td>
			<td><div align="left">${tracer.substance}</div></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="left">Batch:</div></td>
			<td><div align="left">${tracer.batchName}</div></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="left">Aggregation state:</div></td>
			<td><div align="left">${tracer.solidLiquidState}</div></td>
			<td>&nbsp;</td>
		</tr>
		<c:if test="${showExternalOnly == false}">
		<tr>
			<td><div align="left">Current amount (ul or mg):</div></td>
			<td>
				<div align="left">
					${tracer.currentAmount}
					<c:if test="${tracer.solidLiquidState == 'liquid'}">ul</c:if>
					<c:if test="${tracer.solidLiquidState == 'solid'}">mg</c:if>
				</div>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="left">Current activity (kBq):</div></td>
			<td><div align="left">${tracer.currentActivity}</div></td>
			<td>&nbsp;</td>
		</tr>
		</c:if>
		<tr>
			<td><div align="left">Initial amount (ul or mg):</div></td>
			<td>
				<div align="left">
					${tracer.initialAmount}
					<c:if test="${tracer.solidLiquidState == 'liquid'}">ul</c:if>
					<c:if test="${tracer.solidLiquidState == 'solid'}">mg</c:if>
				</div>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="left">Initial activity (kBq):</div></td>
			<td><div align="left">${tracer.initialActivity}</div></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="left">Date of initial activity:</div></td>
			<td><div align="left">${tracer.initialActivityDate} </div></td>
			<td>&nbsp;</td>
		</tr>
		<c:if test="${showExternalOnly == false}">
		<c:if test="${tracerIntoDisposedWaste == false}">
			<tr>
				<td><div align="left">location:</div></td>
				<td><div align="left">${tracer.location}</div></td>
				<td>
					<div align="left">
						<spring:url value="/relocateTracer${tracer.tracerId}"
							var="relocateTracerPath" />
						<input type="button" class="button1" value="Change"
							onclick="location.href='${relocateTracerPath}'" />
					</div>
				</td>
			</tr>
			</c:if>
		</c:if>
		<c:if test="${tracerIntoDisposedWaste == true}">
			<tr>
				<td><div align="left">Location:</div></td>
				<td><div align="left">${tracer.location}</div></td>
				<td>&nbsp;</td>
			</tr>
		</c:if>		
		<tr>
			<td><div align="left">Registered by:</div></td>
			<td><div align="left">${tracer.nuclideUserByCreationUserId}</div></td>
			<td>&nbsp;</td>
		</tr>
		<c:if test="${showExternalOnly == false}">
			<tr>
				<td><div align="left">Disposed of by:</div></td>
				<td><div align="left">${tracer.nuclideUserByDisposalUserId}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Disposal date:</div></td>
				<td><div align="left">${tracer.disposalDate}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Attached files:</div></td>
				<td><div align="left">
									${tracer.attachedFilesLabel}
									<spring:url value="/attachFile${tracer.tracerId}" var="insertTracerPath" />
									<a href="${insertTracerPath}" class="btn"><i class="fas fa-plus"></i></a>
					</div>
				</td>
				<td>
					<c:if test="${tracer.attachedFiles == 'Y'}">
						<div align="left">
							<spring:url value="/listAttachedFiles${tracer.tracerId}"
								var="listeAttachedFilesPath" />
							<input type="button" class="button1" value="Show attached files"
								onclick="location.href='${listeAttachedFilesPath}'" />
						</div>
					</c:if> 
					<c:if test="${tracer.attachedFiles == 'N'}">
						&nbsp;
					</c:if>
				</td>
			</tr>
		</c:if>
		<c:if test="${showExternalOnly == true}">
			<tr>
				<td><div align="left">Sent to:</div></td>
				<td><div align="left">${tracer.destination}</div></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><div align="left">Sent date:</div></td>
				<td><div align="left">${tracer.initialActivityDate}</div></td>
				<td>&nbsp;</td>
			</tr>
		</c:if>
	</table>
	</div>
	<br/>
	<c:if test="${showExternalOnly == false}">
	<div class="actionDivDetail">
	<h5>Usage List</h5>
	<table class="table1">
		<tr>
			<th>Waste No.</th>
			<th>Amount (ul or mg)</th>
			<th>Bio Lab Journal</th>
			<th>Assay Type</th>
			<th>Destination</th>
			<th>Usage Date</th>
			<th>Scientist</th>
		</tr>
		<c:forEach items="${tracer.usageList }" var="traceUsage">
			<tr>
				<c:choose>
					<c:when test="${traceUsage.wastId == 'N/A'}">
						<td>${traceUsage.wastId }</td>
					</c:when>
					<c:otherwise>
						<td><a href="/nuclideDB/wasteDetail_${traceUsage.wastId}" class="btn">${traceUsage.wastId }</a></td>
					</c:otherwise>
				</c:choose>
				<td>${traceUsage.amount }</td>
				<td align="left">${traceUsage.bioLabJournal }</td>
				<td align="left">${traceUsage.assayType }</td>
				<td align="left">${traceUsage.destination }</td>
				<td>${traceUsage.usageDate }</td>
				<td align="left">${traceUsage.user }</td>
			</tr>
		</c:forEach>
	</table>
	</div>
	</c:if>
</body>
</html>