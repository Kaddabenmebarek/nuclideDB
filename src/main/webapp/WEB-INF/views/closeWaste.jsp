<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>
<head>
	<title>Nuclide DB - Close Waste</title>
	<jsp:include page="includes/header.jsp" />
</head>
<body>
	<jsp:include page="includes/menu.jsp" />
	<h5>Close This Waste Container:</h5>
	<div class="actionDiv">
		<f:form modelAttribute="nuclideWaste" method="post" action="closeWaste" id="wasteclosure">
			<table class="table">
				<tr>
					<td><div align="left">Waste Container No:</div></td>
					<td><f:hidden path="nuclideWasteId"
							value="${nuclideWaste.nuclideWasteId}" />
						<div align="left">${nuclideWaste.nuclideWasteId}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Nuclide:</div></td>
					<td><div align="left"><span id="isotp">${nuclideWaste.nuclide.nuclideName}</span></div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Aggregation state:</div></td>
					<td><div align="left">${solidLiquidState}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Registered by:</div></td>
					<td><div align="left">${registeredBy.firstName} ${registeredBy.lastName}</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Last tracer disposed on:</div></td>
					<td><div align="left"><span id="mostRecentDisposedTracerDate">${mostRecentDisposedTracerDate}</span></div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="left">Closure date:</div></td>
					<td>
						<input class="datepicker" id="_Date" name="Date" type="text" value="${today}" readonly="readonly">
						<div id="activityDate"></div> 
					</td>						
					<td><f:errors path="closureDate"></f:errors>
				</tr>
				<tr>
				<!-- TODO get user from session -->
				<td colspan="3"><f:hidden path="nuclideUserByClosureUserId"
							value='<%=session.getAttribute("username")%>' />
				</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" name="Submit" value="Modify Waste"></td>
				</tr>
			</table>
		</f:form>
	</div>
	<jsp:include page="includes/i125-div.jsp" />
	<script type="text/javascript">
	$(function () {
		var tracerDisposalDateText = $("#mostRecentDisposedTracerDate").text();
		var tracerDisposalDate = new Date(tracerDisposalDateText);
		var i25_dialog = false;
		
	    $('.datepicker').datepicker({
	    	onSelect: function(date) {
	            //alert(date);
	    		$("#activityDate").empty();
	    		$("#activityDate").append('<input id="closureStrDate" style="display:none" name="closureStrDate" type="text" value="'+date+'"/>');
	        },
			dateFormat: 'yy-M-d',
      		changeMonth: true,
      		changeYear: true
      		,minDate: tracerDisposalDate
	    });	
	    
		$("#i125_okbtn").click(function(){
			i25_dialog = true;
			$('#dialog_125i').dialog('close');
			$("#wasteclosure").submit();
		});
		
		$("#wasteclosure").submit(function() {
			var isotope = $("#isotp").text();
		    if("125I" == isotope && i25_dialog == false){
		    	$("#dialog_125i").dialog();
				return false;
		    }else{			
				return true;
			}
		});
	    
	    
	  });
	</script>	
</body>
</html>