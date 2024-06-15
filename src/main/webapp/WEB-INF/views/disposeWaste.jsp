
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>Nuclide DB - Dispose Waste Container</title>
	<jsp:include page="includes/header.jsp" />
</head>
<body>
	<jsp:include page="includes/menu.jsp" />
	<h5>Dispose Of This Waste Container:</h5>
	<div class="actionDiv">
		<f:form modelAttribute="wasteToDisplay" method="post"
			action="disposeWaste"  id="disposeWaste_form">
			<table class="table">
				<tr>
					<td><div align="left">Waste Container No:</div></td>
					<td><f:hidden path="nuclideWasteId"
							value="${wasteToDisplay.nuclideWasteId}" /> 
							<div align="left">${nuclideWasteId}</div></td>							
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Nuclide:</div></td>
					<td><div align="left">${wasteToDisplay.nuclideName}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Aggregation state:</div></td>
					<td><div align="left">${wasteToDisplay.solidLiquidState}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Registered by:</div></td>
					<td><div align="left">${wasteToDisplay.nuclideUserByCreationUserId}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Closed by:</div></td>
					<td><div align="left">${wasteToDisplay.nuclideUserByClosureUserId}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Closed on:</div></td>
					<td><div align="left"><span id="wasteClosureDate">${wasteToDisplay.closedOn}</span></div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Date of disposal:</div></td>
					<!-- <td>
						<input class="datepicker" id="_Date" name="Date" type="text" value="${today}" >
						<span id="activityDate"></span>						
						<div style="display:none">
							<f:input type="date" path="closureDate" value="${theFormattedDate}"/>
							<input type="text" id="hightlight" value="${wasteToDisplay.hightlight}"/>
							<input type="text" id="selectedNuclide" value="${wasteToDisplay.nuclideName}"/>
						</div>
					</td>-->
					<td>
						<input class="datepicker" id="_Date" name="Date" type="text" value="${today}" readonly="readonly">
						<div id="activityDate"></div> 
					</td>	
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Route of disposal:</div></td>
					<td><div align="left"><f:select path="disposalRoute" id="form_wasteDisposalRoute">
							<f:option value="-" label="--select route of disposal--" />
							<f:options items="${disposalRouteList}" />
						</f:select></div></td>
					<td>
						<span class="error_form" id="wasteDisposalRoute_error_message"></span>
						<span class="error_form" id="disposalLimitRouteChosen_error_message"></span>
						<span class="error_form" id="activityTooHigh_error_message"></span>
					</td>
				</tr>
				<tr>
					<td><div align="left">Current Activity / kBq:</div></td>
					<td><f:hidden path="currentActivityKBq" value="${wasteToDisplay.currentActivityKBq}" id="currentActivity" />
						<div align="left">${wasteToDisplay.currentActivityKBq}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Current Activity / LE:</div></td>
					<td><div align="left">${wasteToDisplay.currentActivityLE}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<!-- TODO get user from session -->
					<td colspan="3"><f:hidden path="nuclideUserByDisposalUserId"
							value='<%=session.getAttribute("username")%>' /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td ><div align="left"><input type="submit" name="Submit" value="Modify Waste"></div></td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</f:form>
	</div>
<script type="text/javascript">
	$(function() {
		$("#wasteDisposalRoute_error_message").hide();
		var error_wasteDisposalRoute = false;
		var error_disposalLimitRouteChosen = false;
		var error_activityTooHigh = false;
		function check_wasteDisposalRoute() {
			var route = $("#form_wasteDisposalRoute").val();
			if(route == '-') {
				$("#wasteDisposalRoute_error_message").html("Please select Route od disposal");
				$("#wasteDisposalRoute_error_message").show();
				error_wasteDisposalRoute = true;
			} else {
				$("#wasteDisposalRoute_error_message").hide();
			}
		}
		function check_disposalLimitRouteChosen() {
			var route = $("#form_wasteDisposalRoute").val();
			var highlight = $("#hightlight").val();
			if(highlight == 'Y'){
				if(route == 'with the solvent waste' || route == 'down the drain' || route == 'into the waste basket'){
					$("#disposalLimitRouteChosen_error_message").html("This disposal route is not allowed, please contact the Radiosafety Officer");
					$("#disposalLimitRouteChosen_error_message").show();
					error_disposalLimitRouteChosen = true;
				}else{
					$("#disposalLimitRouteChosen_error_message").hide();
				}
			}else{
				$("#disposalLimitRouteChosen_error_message").hide();
			}
		}
		function check_ifActivityTooHigh() {
			var currentActivity = $("#currentActivity").val();
			var nuclide = $("#selectedNuclide").val();
			$("#activityTooHigh_error_message").hide();
			if(nuclide == '14C' && currentActivity > 300000){
				$("#activityTooHigh_error_message").html("Too full, activity too high!");
				$("#activityTooHigh_error_message").show();
				error_activityTooHigh = true;
			}
			if(nuclide == '3H' && currentActivity > 8000000){
				$("#activityTooHigh_error_message").html("Too full, activity too high!");
				$("#activityTooHigh_error_message").show();
				error_activityTooHigh = true;
			}
		}
		$("#disposeWaste_form").submit(function() {
			error_wasteDisposalRoute = false;
			error_disposalLimitRouteChosen = false;
			error_activityTooHigh = false;
			check_wasteDisposalRoute();
			check_disposalLimitRouteChosen();
			check_ifActivityTooHigh();
			if(error_wasteDisposalRoute == false && error_disposalLimitRouteChosen == false && error_activityTooHigh == false) {
				return true;
			} else {
				return false;	
			}
		});	
		var closureDateText = $("#wasteClosureDate").text();
		var closureDate = new Date(closureDateText);
	    $('.datepicker').datepicker({
	    	onSelect: function(date) {
	    		$("#activityDate").empty();
	    		$("#activityDate").append('<input id="disposalStrDate" style="display:none" name="disposalStrDate" type="text" value="'+date+'"/>');
	        },
			dateFormat: 'yy-M-d',
      		changeMonth: true,
      		changeYear: true
      		,minDate: closureDate
	    });	
		
	});
</script>
</body>
</html>