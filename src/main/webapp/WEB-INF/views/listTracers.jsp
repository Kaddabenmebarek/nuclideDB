
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - List of Tracers</title>
	<jsp:include page="includes/header.jsp" />
	<script>
		$(document).ready(
				function() {
					$("#mydata").on('init.dt', function() {
						$('#mydata').show();
					})
					.on(
						'processing.dt',
						function(e, settings, processing) {

							$('#processingIndicator').css('display',processing ? 'block' : 'none');
						})
					.dataTable({
						searchHighlight : true,
						"sDom" : '<"wrapper"Bflipt>rt',
						"iDisplayLength" : 25,
						"bAutoWidth" : true,
						"order": [[ 0, "desc" ]],
						"bStateSave" : false,
						"footerCallback": function ( row, data, start, end, display ) {
							var api = this.api(), data;
							var intVal = function ( i ) {
								return typeof i === 'string' ?
									i.replace(/[\$,]/g, '')*1 :
									typeof i === 'number' ?
										i : 0;
							};
							// Total over all pages
							totalCurrentActivity = api
								.column( 5 )
								.data()
								.reduce( function (a, b) {
									return intVal(a) + intVal(b);
								}, 0 );
							totalInitialActivity = api
								.column( 7 )
								.data()
								.reduce( function (a, b) {
									return intVal(a) + intVal(b);
								}, 0 );
							// Total over this page
							pageTotalCurrentActivity = api
								.column( 5, { page: 'current'} )
								.data()
								.reduce( function (a, b) {
									return intVal(a) + intVal(b);
								}, 0 );

							pageTotalInitialActivity = api
								.column( 7, { page: 'current'} )
								.data()
								.reduce( function (a, b) {
									return intVal(a) + intVal(b);
								}, 0 );
							//update display
							$("#total-currentactivity").empty();
							//$("#total-currentactivity").append('The total current activity of tracer tubes listed above is '+pageTotalCurrentActivity+' kBq');
							$("#total-currentactivity").append('Total activity of selected tracers : '+${currentActivitySum}+' kBq');
							$("#total-initialactivity").empty();
							//$("#total-initialactivity").append('The total initial activity of tracer tubes listed above is '+pageTotalInitialActivity+' kBq');
							$("#total-initialactivity").append('Total initial activity of selected tracers : '+${initialActivitySum}+' kBq');
						},
						buttons: [
							   {
									extend: 'pdf',
								   	text:      '<i class="far fa-file-pdf"></i>',
								   	titleAttr: 'Pdf',
									orientation: 'landscape',
									exportOptions: {
										columns: [0,1,2,3,4,5,6,7,8,9]
									},
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

					$('#mydataTree').treegrid();
					$("#mydataTree").on('init.dt', function() {
						$('#mydataTree').show();
					})
					.on(
						'processing.dt',
						function(e, settings, processing) {

							$('#processingIndicator').css('display',processing ? 'block' : 'none');
						})
					.dataTable({
						searchHighlight : true,
						"sDom" : '<"wrapper"Bflipt>rt',
						"iDisplayLength" : 100,
						"bAutoWidth" : true,
						"order": [[ 3, "desc" ]],
						"bStateSave" : false,
						"footerCallback": function ( row, data, start, end, display ) {
							var api = this.api(), data;
							var intVal = function ( i ) {
								return typeof i === 'string' ?
									i.replace(/[\$,]/g, '')*1 :
									typeof i === 'number' ?
										i : 0;
							};
							// Total over all pages
							totalCurrentActivity = api
								.column( 5 )
								.data()
								.reduce( function (a, b) {
									return intVal(a) + intVal(b);
								}, 0 );
							totalInitialActivity = api
								.column( 7 )
								.data()
								.reduce( function (a, b) {
									return intVal(a) + intVal(b);
								}, 0 );
							// Total over this page
							pageTotalCurrentActivity = api
								.column( 5, { page: 'current'} )
								.data()
								.reduce( function (a, b) {
									return intVal(a) + intVal(b);
								}, 0 );

							pageTotalInitialActivity = api
								.column( 7, { page: 'current'} )
								.data()
								.reduce( function (a, b) {
									return intVal(a) + intVal(b);
								}, 0 );
							//update display
							$("#total-currentactivity").empty();
							//$("#total-currentactivity").append('The total current activity of tracer tubes listed above is '+pageTotalCurrentActivity+' kBq');
							$("#total-currentactivity").append('Total activity of selected tracers : '+${currentActivitySum}+' kBq');
							$("#total-initialactivity").empty();
							//$("#total-initialactivity").append('The total initial activity of tracer tubes listed above is '+pageTotalInitialActivity+' kBq');
							$("#total-initialactivity").append('Total initial activity of selected tracers : '+${initialActivitySum}+' kBq');
						},
						buttons: [
							   {
									extend: 'pdf',
								   	text:      '<i class="far fa-file-pdf"></i>',
								   	titleAttr: 'Pdf',
									orientation: 'landscape',
									exportOptions: {
										columns: [0,1,2,3,4,5,6,7,8,9]
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
		<f:form modelAttribute="tracerOverview" method="post" action="listTracers" id="showTracersForm">
			<table class="table">
				<tr>
					<td><div align="right">Nuclide:</div></td>
					<td><f:select path="nuclideName" id="nulideNameSelect">
							<f:option value="any" label="any" />
							<f:options items="${nuclideList}" />
						</f:select>
					</td>
					<td>&nbsp;</td>
				</tr>			
				<tr>
					<td><div align="right">Lab:</div></td>
					<td><f:select path="location" id="labSelect">
							<f:option value="any" label="any" />
							<f:options items="${locationList}" />
						</f:select>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Year of initial activity:</div></td>
					<td><f:select path="activityYear" id="initialActivityYearSelect">
							<f:option value="any" label="any" />
							<f:options items="${yearList}" />
						</f:select>
					</td>
					<td>&nbsp;</td>					
				</tr>
				<tr>
					<td><div align="right">Tracer status:</div></td>
					<td><f:select path="discardedStatus" id="discardedStatusSelect">
							<f:option value="any" label="All tracers" />
							<f:option value="E" label="Tracers for external usage" />
							<f:option value="N" label="Non-discarded tracers" />
							<f:option value="Y" label="Discarded tracers" />
							<f:options items="${discardDateList}" />														
						</f:select>
					</td>
					<td><span class="error_form" id="discard_error_message"></span></td>
				</tr>				
				<tr>
					<td><div align="right">Aggregation State:</div></td>
					<td><f:select path="solidLiquidState" id="isLiquidSelect">
							<f:option value="any" label="any" />
							<f:option value="Y" label="Liquid only" />
							<f:option value="N" label="Solid only" />
						</f:select>
					</td>	
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Original/Daughter state:</div></td>
					<td><f:select path="originalDaughterState" id="daughterSelect">
							<f:option value="any" label="any" />
							<f:option value="original" label="Original tracers only" />
							<f:option value="daughter" label="Daugter tracers only" />														
						</f:select>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Responsible Scientist:</div></td>				
					<td><f:select path="responsible" id="scientistSelect">
							<f:option value="any" label="any" />
							<f:options items="${responsibleList}" />
						</f:select>
					</td>
					<td>&nbsp;</td>
				</tr>
<!-- 				<tr>
					<td><div align="right">Show external tracers only:</div></td>				
					<td>
						<input type="checkbox" id="externalTracersOnly" name="externalTracersOnly">
					</td>
					<td>&nbsp;</td>
				</tr> -->	
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" name="Submit" value="Show Tracer Tubes"></td>
				</tr>
			</table>
		</f:form>
	</div>
	<br/>
	<div>
		Select a display:
		<input type="checkbox" id="displayTreeView" name="displayView" value="TreeView">&nbsp;<label for="displayView">Hierarchy view</label>
    	<!-- <label for="displayTreeView" class="container">Hierarchy view
			<input type="checkbox" id="displayTreeView" name="displayView" value="TreeView">
			<span class="checkmark"></span>
		</label> -->
	</div>
	<br />	
	<div>
	<h4>
		<c:choose>
			<c:when test="${discardedStatus != null}">
				${discardedStatus}
			</c:when>
			<c:otherwise>
				${tracerHierarchyTitle}
			</c:otherwise>
		</c:choose>
	</h4>
	
	<div id="mydataTable">
		<table id="mydata" class="display">
			<thead>
				<tr>
					<th class="text-xs-center">No.</th>
					<c:if test="${showExternalOnly == true}">
						<th class="text-xs-center">Mother Tube</th>
					</c:if>
					<th class="text-xs-center">Nuclide</th>
					<th class="text-xs-left">Substance</th>
					<th class="text-xs-left">State</th>
					<c:if test="${showExternalOnly == false}">
						<th class="text-xs-center">Current Amount<br/>(ul or mg)</th>
						<th class="text-xs-center">Current Activity<br/>(kBq)</th>
						<th class="text-xs-center">Initial Amount<br/>(ul or mg)</th>
						<th class="text-xs-center">Initial Activity<br/>(kBq)</th>
					</c:if>
					<c:if test="${showExternalOnly == true}">
						<th class="text-xs-center">Amount<br/>(ul or mg)</th>
						<th class="text-xs-center">Activity<br/>(kBq)</th>
					</c:if>
					<th class="text-xs-left">Responsible</th>
					<c:if test="${showExternalOnly == false}">
						<th class="text-xs-left">Location</th>
						<th class="text-xs-center">Disposal Date</th>
					</c:if>
					<c:if test="${showExternalOnly == true}">
						<th class="text-xs-left">Sent to</th>
						<th class="text-xs-left">Sent Date</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${tracers }" var="tracer">
					<tr class="treegrid-${tracer.tracerId}">
						<td align="left" width="90">
							<a href="/nuclideDB/tracerDetail_${tracer.tracerId }" class="btn2">${tracer.tracerId }</a>
							<c:if test="${tracer.attachedFiles == 'Y'}"><i class="fas fa-download"></i></c:if>
						</td>
						<c:if test="${showExternalOnly == true}">
							<td align="center">
								<a href="/nuclideDB/tracerDetail_${tracer.parentId }" class="btn">${tracer.parentId }</a>
							</td>
						</c:if>
						<td align="center">${tracer.nuclideName }</td>
						<td align="left">${tracer.substance }</td>
						<c:choose>
							<c:when test="${tracer.solidLiquidState == 'Y'}">
								<td align="left">liquid</td>
							</c:when>
							<c:when test="${tracer.solidLiquidState == 'N'}">
								<td align="left">solid</td>
							</c:when>
							<c:otherwise>
								<td align="left">both</td>
							</c:otherwise>
						</c:choose>
						<c:if test="${showExternalOnly == false}">
							<td align="center">${tracer.currentAmount }</td>
							<td align="center">${tracer.currentActivity }</td>
						</c:if>
						<td align="center">${tracer.initialAmount }</td>
						<td align="center">${tracer.initialActivity }</td>
						<td align="left">${tracer.responsible }</td>
						<c:if test="${showExternalOnly == false}">
							<td align="left">
								${tracer.location }
							</td>
							<c:choose>
								<c:when test="${tracer.disposalDate == null && tracer.tracerTypeId != 4}">
									<td align="center"><spring:url value="/discardTracer${tracer.tracerId }"
											var="discardTracerPath" /> <input type="button"
										class="button1" value="Dispose now"
										onclick="location.href='${discardTracerPath}'" /></td>
								</c:when>
								<c:otherwise>
									<td align="center">${tracer.disposalDate }</td>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${showExternalOnly == true}">
							<td align="left">${tracer.destination }</td>
							<td align="left">${tracer.creationDate }</td>
						</c:if>
<%-- 						<c:if test="${showExternalOnly == false}">
							<td align="center">
								<spring:url value="/tracerDetail_${tracer.tracerId }" var="tracerDetailPath" /> <input type="button" class="button2" value="Detail" onclick="location.href='${tracerDetailPath}'" />
							</td>
						</c:if> --%>
						<%-- <td align="center">${tracer.parentId}</td> --%>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		
		<div id="mydataTreeTable">
			<table id="mydataTree" class="display">
				<thead>
					<tr>
						<th class="text-xs-center">No.</th>
						<c:if test="${showExternalOnly == true}">
							<th class="text-xs-center">Mother Tube</th>
						</c:if>
						<th class="text-xs-center">Nuclide</th>
						<th class="text-xs-left">Substance</th>
						<th class="text-xs-left">State</th>
						<c:if test="${showExternalOnly == false}">
							<th class="text-xs-center">Current Amount<br/>(ul or mg)</th>
							<th class="text-xs-center">Current Activity<br/>(kBq)</th>
							<th class="text-xs-center">Initial Amount<br/>(ul or mg)</th>
							<th class="text-xs-center">Initial Activity<br/>(kBq)</th>
						</c:if>
						<c:if test="${showExternalOnly == true}">
							<th class="text-xs-center">Amount<br/>(ul or mg)</th>
							<th class="text-xs-center">Activity<br/>(kBq)</th>
						</c:if>
						<th class="text-xs-left">Responsible</th>
						<c:if test="${showExternalOnly == false}">
							<th class="text-xs-left">Location</th>
							<th class="text-xs-center">Disposal Date</th>
						</c:if>
						<c:if test="${showExternalOnly == true}">
							<th class="text-xs-left">Sent to</th>
							<th class="text-xs-left">Sent Date</th>
						</c:if>
					</tr>
				</thead>
				<tbody>${treeTracersTable}</tbody>
			</table>
		</div>		
		<div id="total-currentactivity"></div>
		<div id="total-initialactivity"></div>
	</div>
</body>
<script type="text/javascript">

	var nuclideName = "${nuclideName}";
	$('#nulideNameSelect option[value="'+ nuclideName +'"]').attr("selected", "selected");
	var lab = "${lab}";
	$('#labSelect option[value="'+ lab +'"]').attr("selected", "selected");
	var initialActivityYear = "${initialActivityYear}";
	$('#initialActivityYearSelect option[value="'+ initialActivityYear +'"]').attr("selected", "selected");
	var discardedStatusSelected = "${discardedStatusSelected}";
	$('#discardedStatusSelect option[value="'+ discardedStatusSelected +'"]').attr("selected", "selected");
	var isLiquid = "${isLiquid}";
	$('#isLiquidSelect option[value="'+ isLiquid +'"]').attr("selected", "selected");
	var daughter = "${daughter}";
	$('#daughterSelect option[value="'+ daughter +'"]').attr("selected", "selected");
	var scientist = "${scientist}";
	$('#scientistSelect option[value="'+ scientist +'"]').attr("selected", "selected");
	
	/* alert("filters = " + nuclideName + ", " + lab + ", "
			+ initialActivityYear + ", " + discardedStatus + ", " + isLiquid
			+ ", " + daughter + ", " + scientist); */

	$('#externalTracersOnly').change(function() {
		$("#discard_error_message").hide();
		if ($('#externalTracersOnly').is(':checked')) {
			$('#originalDaughterState option:eq(1)').prop('selected', true);
			$('#discardedStatus option:eq(2)').prop('selected', true);
		} else {
			$('#originalDaughterState option:eq(0)').prop('selected', true);
		}
	});
	$('#discardedStatus').change(function() {
		$("#discard_error_message").hide();
	})
	$("#discard_error_message").hide();
	var error_discard = false;
	function check_discard() {
		var discardStatus = $("#discardedStatus option:selected").text();
		if ($('#externalTracersOnly').is(':checked')
				&& discardStatus != 'Non-discarded tubes') {
			$("#discard_error_message")
					.html(
							"Please change to 'Non-discarded tubes' or uncheck the option 'Show external tracers only'.");
			$("#discard_error_message").show();
			error_discard = true;
		} else {
			$("#discard_error_message").hide();
		}
	}
	$("#showTracersForm").submit(function() {
		if ($('#externalTracersOnly').is(':checked')) {
			$('#originalDaughterState option:eq(1)').prop('selected', true);
		}
		error_discard = false;
		check_discard();
		if (error_discard == false) {
			return true;
		} else {
			return false;
		}
	});
	function resetFilter() {
		$('#mydata_filter input[type=search]').val('').change();
		$('select[name = "mydata_length"] option:eq(1)').prop('selected', true);
	};

	$('[id="displayTreeView"]').change(function(){
		if ($(this).is(':checked')){
        	$("#mydataTreeTable").fadeIn();
        	$("#mydataTable").fadeOut();
        } else {
        	$("#mydataTable").fadeIn();
            $("#mydataTreeTable").fadeOut();
        }
	})
	
	window.addEventListener("DOMContentLoaded", function(){
		setTimeout(() => {			
		if(!$("#displayTreeView").is(':checked')){
			$("#mydataTable").show();
	        $("#mydataTreeTable").hide();
		}else{
			$("#mydataTable").hide();
	        $("#mydataTreeTable").show();
		}
		}, 50);
	});
    	
</script>
</html>