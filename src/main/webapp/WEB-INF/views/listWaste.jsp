
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - List of Wastes</title>
	<jsp:include page="includes/header.jsp" />

	<script>
		$(document).ready(
				function() {
					$("#mydata").on('init.dt', function() {
						$('#mydata').show();
					}).on(
							'processing.dt',
							function(e, settings, processing) {
								$('#processingIndicator').css('display',
										processing ? 'block' : 'none');
							}).dataTable({
						searchHighlight : true,
						"sDom" : '<"wrapper"Bflipt>rt',
						"iDisplayLength" : 25,
						"bAutoWidth" : false,
						"order": [[ 0, "desc" ]],
						//"bStateSave" : true,
						"columnDefs" : [ {
							type : 'num',
							'targets' : [ 0, 3, 4 ]
						} ],
						//buttons : [/*'copy', 'csv', */'excel', 'pdf', 'print' ]
						buttons: [
							   {
								   extend: 'pdf',
								   text:      '<i class="far fa-file-pdf"></i>',
								   titleAttr: 'Pdf',
								   exportOptions: {
										columns: [0,1,2,3,4,5,7]
									}
							   },
							   {
								   extend: 'excel',
								   text:      '<i class="far fa-file-excel"></i>',
								   titleAttr: 'Excel',
							   },
							   {
								   extend: 'print',
								   text:      '<i class="fas fa-print"></i>',
								   titleAttr: 'Print',
							   }
							]
					});
					setTimeout(function() {
						resetFilter();
					}, 100);
				});
	</script>

	<style type="text/css">
	#mydata_wrapper {
		width: 80% !important;
		margin-left: auto;
		margin-right: auto;
	}
	.text-xs-center {text-align:center!important}
	.text-xs-left {text-align:left!important}
	</style>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div>
		<f:form modelAttribute="wasteOverview" method="post" action="listWaste">
			<table class="table">
				<tr>
					<td><div align="right">Nuclide:</div></td>
					<td><f:select path="nuclideName" id="nuclideNameSelected">
							<f:option value="any" label="any" />
							<f:options items="${nuclideList}" />
						</f:select>
					</td>
				</tr>			
				<tr>
					<td><div align="right">Closure state:</div></td>
					<td><f:select path="closedOn" id="closedOnSelected">
							<f:option value="open" label="All open" />
							<f:option value="closed" label="All closed" />
							<f:option value="discarded" label="All discarded" />							
							<f:options items="${closureDateList}" />
						</f:select>
					</td>					
				</tr>			
				<tr>
					<td><div align="right">Aggregation State:</div></td>
					<td><f:select path="solidLiquidState" id="solidLiquidStateSelected">
							<f:option value="any" label="any" />
							<f:option value="Y" label="Liquid only" />
							<f:option value="N" label="Solid only" />														
						</f:select>
					</td>	
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" name="Submit" value="Show Waste"></td>
				</tr>				
			</table>
		</f:form>
	</div>
	<br/>
	<div>

		<!-- table id="myDatatable" class="display datatable"-->
		<table id="mydata" class="">
			<thead>
			<tr>
				<c:if test="${showCurrentActivity == 'O'}">
					<th>No.</th>
					<th class="text-xs-left">Nuclide</th>
					<th class="text-xs-left">State</th>
					<th>Activity (kBq)</th>
					<th>Disposal Limit (kBq)</th>
					<th class="text-xs-left">Location</th>
					<th>Closed on</th>
					<th>Disposed of on</th>
					<!-- <th>Detail</th> -->
				</c:if>
				<c:if test="${showCurrentActivity == 'C'}">
					<th>No.</th>
					<th class="text-xs-left">Nuclide</th>
					<th class="text-xs-left">State</th>
					<th>Activity (kBq)</th>
					<th>Disposal Limit (kBq)</th>
					<th class="text-xs-left">Location</th>
					<th>Closed on</th>
					<th>Disposed of on</th>
					<!-- <th>Detail</th> -->
				</c:if>
				<c:if test="${showCurrentActivity == 'D'}">
					<th>No.</th>
					<th class="text-xs-left">Nuclide</th>
					<th class="text-xs-left">State</th>
					<th>Closed on</th>
					<th>Disposed of on</th>
					<th>Disposal Activity (kBq)</th>
					<th class="text-xs-left">Disposal Route</th>
					<!-- <th>Detail</th> -->
				</c:if>
				<c:if test="${showCurrentActivity == 'CD'}">
					<th>No.</th>
					<th class="text-xs-left">Nuclide</th>
					<th class="text-xs-left">State</th>
					<th>Closed on</th>
					<th>Disposed of on</th>
					<th>Disposal Activity (kBq)</th>
					<th class="text-xs-left">Disposal Route</th>
					<!-- <th>Detail</th> -->
				</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${wastes }" var="waste">
					<c:if test="${waste.hightlight == 'Y'}">
						<tr class="highlight">
					</c:if>
					<c:if test="${waste.hightlight == 'N'}">
						<tr>
					</c:if>
					<c:if test="${showCurrentActivity == 'O'}">
						<td align="center">
						<a href="/nuclideDB/wasteDetail_${waste.nuclideWasteId }" class="btn2">${waste.nuclideWasteId }</a>
						</td>
						<td align="left">${waste.nuclideName }</td>
						<c:if test="${waste.solidLiquidState == 'Y'}">
							<td align="left">liquid</td>
						</c:if>
						<c:if test="${waste.solidLiquidState == 'N'}">
							<td align="left">solid</td>
						</c:if>
						<td align="center">${waste.activityKbq }</td>
						<td align="center">${waste.disposalLimit }</td>
						<td align="left">${waste.location }</td>
						<td align="center"><spring:url value="/closeWaste${waste.nuclideWasteId }"
								var="closeWaste" /> <input type="button" class="button1"
							value="Close now" onclick="location.href='${closeWaste}'" /></td>
						<td align="center">not closed yet</td>
						<%-- <td align="center"><spring:url value="/wasteDetail_${waste.nuclideWasteId}"
								var="detailWastePath" /> <input type="button" class="button2"
							value="Detail" onclick="location.href='${detailWastePath}'" /></td> --%>
					</c:if>
					<c:if test="${showCurrentActivity == 'C'}">
						<td align="center">
							<a href="/nuclideDB/wasteDetail_${waste.nuclideWasteId }" class="btn2">${waste.nuclideWasteId }</a>
						</td>
						<td align="left">${waste.nuclideName }</td>
						<c:if test="${waste.solidLiquidState == 'Y'}">
							<td align="left">liquid</td>
						</c:if>
						<c:if test="${waste.solidLiquidState == 'N'}">
							<td align="left">solid</td>
						</c:if>
						<td align="center">${waste.activityKbq }</td>
						<td align="center">${waste.disposalLimit }</td>
						<td align="left">${waste.location }</td>
						<td align="center">${waste.closedOn }</td>
						<td align="center"><spring:url value="/disposeWaste${waste.nuclideWasteId }"
								var="disposeWaste" /> <input type="button" class="button1"
							value="Dispose now" onclick="location.href='${disposeWaste}'" />
						</td>
						<%-- <td align="center"><spring:url value="/wasteDetail_${waste.nuclideWasteId}"
								var="detailWastePath" /> <input type="button" class="button2"
							value="Detail" onclick="location.href='${detailWastePath}'" /></td> --%>
					</c:if>
					<c:if test="${showCurrentActivity == 'D'}">
						<td align="center">
							<a href="/nuclideDB/wasteDetail_${waste.nuclideWasteId }" class="btn2">${waste.nuclideWasteId }</a>
						</td>
						<td align="left">${waste.nuclideName }</td>
						<c:if test="${waste.solidLiquidState == 'Y'}">
							<td align="left">liquid</td>
						</c:if>
						<c:if test="${waste.solidLiquidState == 'N'}">
							<td align="left">solid</td>
						</c:if>
						<td align="center">${waste.closedOn }</td>
						<td align="center">${waste.disposedOfOn }</td>
						<td align="center">${waste.activityKbq }</td>
						<td align="left">${waste.disposalRoute }</td>
						<%-- <td align="center"><spring:url value="/wasteDetail_${waste.nuclideWasteId}"
								var="detailWastePath" /> <input type="button" class="button2"
							value="Detail" onclick="location.href='${detailWastePath}'" /></td> --%>
					</c:if>
					<c:if test="${showCurrentActivity == 'CD'}">
						<td align="center">
							<a href="/nuclideDB/wasteDetail_${waste.nuclideWasteId }" class="btn2">${waste.nuclideWasteId }</a>
						</td>
						<td align="left">${waste.nuclideName }</td>
						<c:if test="${waste.solidLiquidState == 'Y'}">
							<td align="left">liquid</td>
						</c:if>
						<c:if test="${waste.solidLiquidState == 'N'}">
							<td align="left">solid</td>
						</c:if>
						<td align="center">${waste.closedOn }</td>
						<td align="center">${waste.disposedOfOn }</td>
						<td align="center">${waste.activityKbq }</td>
						<td align="left">${waste.disposalRoute }</td>
						<%-- <td align="center"><spring:url value="/wasteDetail_${waste.nuclideWasteId}"
								var="detailWastePath" /> <input type="button" class="button2"
							value="Detail" onclick="location.href='${detailWastePath}'" /></td> --%>
					</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br />
		<c:if test="${isCurrentActivity != 'N'}">
			<span>Total activity into selected wastes: ${totalActivityIntoWastes} kBq</span>
		</c:if>
		<c:if test="${isCurrentActivity == 'N'}">
		<div>Summarized disposal activities of waste containers listed by route of disposal:</div>		
		<table class="table1">
		<tr>
			<th>Route of disposal</th>
			<th>Sum of activity / kBq</th>
		</tr>
		<c:forEach items="${disposalRouteActivitySumList}" var="disposalRouteActivitySum">
			<tr>
				<td>${disposalRouteActivitySum.disposalRoute }</td>
				<td>${disposalRouteActivitySum.activitySum }</td>
			</tr>
		</c:forEach>
		</table>
		</c:if>
	</div>

</body>
<script type="text/javascript">

	var nuclideNameSelected = "${nuclideName}";
	$('#nuclideNameSelected option[value="'+ nuclideNameSelected +'"]').attr("selected", "selected");
	var closedOnSelected = "${openStatus}";
	$('#closedOnSelected option[value="'+ closedOnSelected +'"]').attr("selected", "selected");
	var isLiquid = "${isLiquid}";
	$('#solidLiquidStateSelected option[value="'+ isLiquid +'"]').attr("selected", "selected");

	function resetFilter(){      
		$('#mydata_filter input[type=search]').val('').change();
		$('select[name = "mydata_length"] option:eq(1)').prop('selected',true);
	};
</script>
</html>