
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - New Usage</title>
	<jsp:include page="includes/header.jsp"/>
	<!--<link rel="stylesheet" href="/resources/demos/style.css">-->
	<script type="text/javascript" src="resources/js/usageDaughterFormValidation.js"></script>
</head>
<body>
<jsp:include page="includes/menu.jsp" />

 <table class="menuTable">
	<tr>
		<td><a href="newBiologicalTracer">Create new assay</a></td>
		<td><a href="newDaughter" class="selectedA">Create new daughter tracer tube</a></td>
		<td><a href="newExternal">Define external usage (material leaves the company)</a></td>
		<td><a href="newInVivo">Define in-vivo usage with new sample creation</a></td>
	</tr>
</table>
<h5>Create Daughter Tracer Tube</h5>
	<div>
		<f:form modelAttribute="biologicalUsage" method="post" action="addNewDaughter"  id="newDaughter_form">
			<table class="table">
				<tr>
					<td><div align="right">Tracer Tube:</div></td>
					<td>
						<f:select path="tracerTubeConcat" id="form_tracerId">
							<f:option value="-" label="--select Tracer Tube--" />
							<f:options items="${tracerTubeList}" />
						</f:select>
					</td>
					<td><span class="error_form" id="tracerId_error_message"></span></td>					
				</tr>
				<tr>
					<td colspan="3">
						<div style="display:none">
							<f:select path="generik" id="form_tracerdateId">
								<f:option value="-" label="--select Tracer Tube--" />
								<f:options items="${tracerTubeDateMap}" />
							</f:select>
						</div>
					</td>
				</tr>			
				<tr>
					<td><div align="right">Amount taken (ul or mg):</div></td>
					<td><f:input path="amountTaken" id="form_amountTaken" /></td>
					<td><span class="error_form" id="amountTaken_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">New total volume (ul):</div></td>
					<td><f:input path="newTotalVolume" id="form_totalVolume" /></td>				
					<td><span class="error_form" id="totalVolume_error_message"></span></td>					
				</tr>
				<tr>
					<td><div align="right">New batch name (optional):</div></td>
					<td><f:input path="newBatchName" /></td>				
				</tr>
				<tr>
					<td><div align="right">Usage Date:</div></td>
					<td>
						<input class="datepicker" id="_Date" name="Date" type="text" value="${today}" readonly="readonly">
						<div id="usageDate"></div> 
					</td>					
					<td><f:errors path="usageDate"></f:errors>										
				</tr>
				<tr>
					<td><div align="right">Location:</div></td>		
					<td>
						<f:select path="location" id="form_Location">
							<f:option value="-" label="--select location--" />
							<f:options items="${location}" />
						</f:select>					
					</td>
					<td><span class="error_form" id="location_error_message" ></span></td>					
				</tr>
				<tr>
				<!-- TODO get user session -->
				<td colspan="2"><f:hidden path="newUsageUserId" value='<%=session.getAttribute("username")%>' /></td>
				</tr>				
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" name="Submit" value="Submit Record"></td>
				</tr>				
			</table>
		</f:form>
	</div>
	<script type="text/javascript">
	$(function () {
	    $('.datepicker').datepicker({
	    	onSelect: function(date) {
	            //alert(date);
	    		$("#usageDate").empty();
	    		$("#usageDate").append('<input id="recordUsageDate" style="display:none" name="recordUsageDate" type="text" value="'+date+'"/>');
	        },
			dateFormat: 'yy-M-d',
      		changeMonth: true,
      		changeYear: true	            
	    });	
	    
	    var tracerActivityDateText;
	    var tracerIdSelected;
	    function refreshDatePicker() {
	        $(".datepicker").datepicker("destroy");
	        $( ".datepicker" ).datepicker("refresh");
	        
	    	//alert(tracerActivityDateText);
			var tracerActivityDate = new Date(tracerActivityDateText);
			//alert(tracerActivityDate);
			
		    $('.datepicker').datepicker({
		    	onSelect: function(date) {
		            //alert(date);
		    		$("#usageDate").empty();
		    		$("#usageDate").append('<input id="recordUsageDate" style="display:none" name="recordUsageDate" type="text" value="'+date+'"/>');
		        },
				dateFormat: 'yy-M-d',
	      		changeMonth: true,
	      		changeYear: true
	      		,minDate: tracerActivityDate
		    });
	    }
		$("#form_tracerId").change(function(){
		    var str  = $('#form_tracerId option:selected').text();
		    var tracer = str.split('(').pop().split(';')[0]
		    var tracerId = str.split(' (')[0];
            //alert(tracerId);
            if(tracerIdSelected != null){
		    	$('#form_tracerdateId option[value="'+ tracerId +'"]').removeAttr('selected');
            }
            $('#form_tracerdateId option[value="'+ tracerId +'"]').attr("selected", "selected");
		    //alert($('#form_tracerdateId option:selected').text());
		    tracerIdSelected = tracerId;
		    tracerActivityDateText = $('#form_tracerdateId option:selected').text();
		    $('#_Date').val('');
		    refreshDatePicker();
		 });
	  });
	</script>
</body>
</html>