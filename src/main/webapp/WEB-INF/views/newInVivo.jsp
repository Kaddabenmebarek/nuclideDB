
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
	<title>Nuclide DB - New In-Vivo Usage</title>
	<jsp:include page="includes/header.jsp" />
	<!--<link rel="stylesheet" href="/resources/demos/style.css">-->
	<script type="text/javascript" src="resources/js/usageInVivoFormValidation.js"></script>
</head>
<body>
	<jsp:include page="includes/menu.jsp" />

	<table class="menuTable">
		<tr>
		<td><a href="newBiologicalTracer">Create new assay</a></td>
		<td><a href="newDaughter">Create new daughter tracer tube</a></td>
		<td><a href="newExternal">Define external usage (material leaves the company)</a></td>
		<td><a href="newInVivo" class="selectedA">Define in-vivo usage with new sample creation</a></td>
		</tr>
	</table>
	<h4>Define In-Vivo Usage With Sample Creation</h4>
	<div>
		<f:form modelAttribute="biologicalUsage" method="post" action="addNewInVivo" id="newInVivoUsage_form">
			<table class="table">
				<tr>
					<td><div align="right">Tracer Tube:</div></td>
					<td><f:select path="tracerTubeConcat" id="form_tracerId">
							<f:option value="-" label="--select Tracer Tube--" />
							<f:options items="${tracerTubeList}" />
						</f:select></td>
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
					<td><f:input path="amountTaken" id="form_amountTaken"/></td>
					<td><span class="error_form" id="amountTaken_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Biological Lab Journal:</div></td>
					<td><f:input path="biologicalLabJournal"  id="form_elb"/></td>
					<td><span class="error_form" id="elb_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Assay Type:</div></td>
					<td><f:input path="assayType"  id="form_assayType"/></td>
					<td><span class="error_form" id="assayType_error_message"></span></td>
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
					<td><div align="right">Total Sample Volume (ul):</div></td>
					<td><f:input path="totalSampleVolume" id="form_totalSample"/></td>
					<td><span class="error_form" id="totalSample_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Sample Location:</div></td>
					<td><f:select path="sampleLocation" id="form_sampleLocation">
							<f:option value="-" label="--select sample location--" />
							<f:options items="${location}" />
						</f:select></td>
					<td><span class="error_form" id="sampleLocation_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Solid Waste:</div></td>
					<td><f:select path="solidWasteConcat" id="form_solidWaste">
							<f:option value="-" label="--select solid waste container--" />
							<f:options items="${solidWasteList}" />
						</f:select></td>
					<td><span class="error_form" id="solidWaste_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Fraction put into solid waste (%):</div></td>				
					<td><f:input path="solidWastePercentage" /></td>				
					<td><f:errors path="solidWastePercentage"></f:errors>									
				</tr>				
				<tr>
					<td colspan="2"><f:hidden path="newUsageUserId"
							value='<%=session.getAttribute("username")%>' /></td>
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
	    
		var solidOpts = $('#form_solidWaste')[0].options;
		var solidArray = $.map(solidOpts, function(elem) {
		    return (elem.text);
		}); 		
		
		$("#form_tracerId").change(function(){
		    var str  = $('#form_tracerId option:selected').text().split(';')[0];
		    //alert(str);
		    var tracer = str.split('(')[1];
            //alert(tracer);			
    
		    var tracerId = str.split(' (')[0];
            //alert(tracerId);
            if(tracerIdSelected != null){
		    	$('#form_tracerdateId option[value="'+ tracerId +'"]').removeAttr('selected');
            }
            $('#form_tracerdateId option[value="'+ tracerId +'"]').attr("selected", "selected");
		    //alert($('#form_tracerdateId option:selected').text());
		    tracerIdSelected = tracerId;
		    tracerActivityDateText = $('#form_tracerdateId option:selected').text();
			$('#form_solidWaste').find('option:not(:first)').remove();
		    jQuery.each( solidArray, function( i, val ) {
		    	if (val.indexOf(tracer) >= 0){	    		
		    		var solidWasteId = val.split('#').pop().split(':')[0];
		    		$('#form_solidWaste').append('<option value="'+solidWasteId+'">'+val+'</option>');
		    	}
		    });		
		    $('#_Date').val('');
		    refreshDatePicker();
		 });		
		
	    
	  });
	</script>
</body>
</html>