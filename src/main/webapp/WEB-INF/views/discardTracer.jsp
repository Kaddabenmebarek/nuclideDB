
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>
<head>
<title>Nuclide DB - Discard Tracer</title>
	<jsp:include page="includes/header.jsp" />
</head>
<body>
	<jsp:include page="includes/menu.jsp" />
	<h5>Discard this tracer:</h5>
	<div class="actionDiv">
		<f:form modelAttribute="tracerToDisplay" method="post" action="discardTracer" id="discardWaste_form">
			<table class="table">
				<tr>
					<td><div align="left">Tracer Tube No:</div></td>
					<td><f:hidden path="tracerId"
							value="${tracerToDisplay.tracerId}" />
						<div align="left">${tracerToDisplay.tracerId}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Nuclide:</div></td>
					<td><f:hidden path="nuclideName"
							value="${tracerToDisplay.nuclideName}" />
						<div align="left"><span id="isotp">${tracerToDisplay.nuclideName}</span></div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Substance:</div></td>
					<td><f:hidden path="substance"
							value="${tracerToDisplay.substance}" />
						<div align="left">${tracerToDisplay.substance}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Location:</div></td>
					<td><f:hidden path="location"
							value="${tracerToDisplay.location}" />
						<div align="left">${tracerToDisplay.location}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Initial Activity (kBq):</div></td>
					<td><f:hidden path="initialActivity"
							value="${tracerToDisplay.initialActivity}" />
						<div align="left">${tracerToDisplay.initialActivity}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Initial Volume (ul or mg):</div></td>
					<td><f:hidden path="initialAmount"
							value="${tracerToDisplay.initialAmount}" />
						<div align="left">${tracerToDisplay.initialAmount}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Registered by:</div></td>
					<td><f:hidden path="creationUserId"
							value="${tracerToDisplay.creationUserId}" />
						<div align="left">${tracerToDisplay.nuclideUserByCreationUserId}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Waste Container:</div></td>
					<td><div align="left"><f:select path="wasteContainer" id="form_wasteContainer">
							<f:option value="-" label="--select location--" />
							<f:options items="${tracerToDisplay.wasteContainerList}" />
						</f:select></div></td>
					<td><span class="error_form" id="wasteContainer_error_message"></span></td>
				</tr>	
				<tr>
					<td>
						<div style="display:none">
							<span id="creationDateTracer">${tracerToDisplay.creationDate}</span>
						</div>
					</td>
				</tr>			
				<tr>
					<td><div align="left">Current Volume (ul or mg):</div></td>
					<td><f:hidden path="currentAmount" value="${tracerToDisplay.currentAmount}" />
						<div align="left">${tracerToDisplay.currentAmount}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Date of disposal:</div></td>
					<td>
						<input class="datepicker" id="_Date" name="Date" type="text" value="${tracerToDisplay.creationDate}" readonly="readonly">
						<div id="activityDate"></div> 
					</td>					
					<td><f:errors path="discardDate"></f:errors>					
				</tr>
				<tr>
					<td colspan="3"><f:hidden path="userId" value='<%=session.getAttribute("username")%>' /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><div align="left"><input type="submit" name="Submit" value="Submit Record"></div></td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</f:form>
	</div>
	<jsp:include page="includes/i125-div.jsp" />
<script type="text/javascript">
	$(function() {
		$("#wasteContainer_error_message").hide();
		var error_wasteContainer = false;
		var i25_dialog = false;
		function check_wasteContainer() {
			var cont = $("#form_wasteContainer").val();
			if(cont == '-') {
				$("#wasteContainer_error_message").html("Please select a waste container");
				$("#wasteContainer_error_message").show();
				error_wasteContainer = true;
			} else {
				$("#wasteContainer_error_message").hide();
			}
		}
		
		$("#i125_okbtn").click(function(){
			i25_dialog = true;
			$('#dialog_125i').dialog('close');
			$("#discardWaste_form").submit();
		});
				
		$("#discardWaste_form").submit(function() {
			error_wasteContainer = false;
			check_wasteContainer()
			if(error_wasteContainer == false) {
				var isotope = $("#isotp").text();
			    if("125I" == isotope && i25_dialog == false){
			    	$("#dialog_125i").dialog();
					return false;
			    }else{			
					return true;
				}
			} else {
				return false;	
			}
		});	

		var tracerCreationDateText = $('#creationDateTracer').text();
        //alert(tracerCreationDateText);
        var tracerCreationDate = new Date(tracerCreationDateText);
		
	    $('.datepicker').datepicker({
	    	onSelect: function(date) {
	            //alert(date);
	    		$("#activityDate").empty();
	    		$("#activityDate").append('<input id="disposalDate" style="display:none" name="disposalDate" type="text" value="'+date+'"/>');
	        },
			dateFormat: 'yy-M-d',
      		changeMonth: true,
      		changeYear: true	            
      		,minDate: tracerCreationDate
	    });	
		
	});
</script>	
</body>
</html>