
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
	<script type="text/javascript" src="resources/js/usageFormValidation.js"></script>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
<%--<small>Authorized user: <%= session.getAttribute("username") %></small>
<table class="menuTable">
	<tr>	
		<th ><a href="newTracer"><i class="fas fa-plus"></i> New Tracer Tube</a></th>
		<th><a href="newWaste"><i class="fas fa-plus"></i> New Waste Container</a></th>
		<th><a href="newUsage" class="selectedA"><i class="fas fa-chart-pie"></i>Define Usage</a></th>
		<th><a href="showTracers"><i class="fas fa-search-location"></i>Tracer Overview</a></th>
		<th><a href="showWaste"><i class="fas fa-dumpster"></i> Wastes</a></th>
		<th><a href="showUsers"><i class="fas fa-users"></i> Users</a></th>
		<th><a href="listLabs"><i class="fas fa-map-marker-alt"></i> Locations</a></th>
		<th><a href="login"><i class="fas fa-sign-out-alt"></i> Logout</a> </th>
		<th><a href="https://snprod.service-now.com/sp" target="_blank" ><i class="fas fa-bug"></i> Bug Report <i class="fas fa-external-link-alt"></i></a></th>
	</tr>
</table>--%>

 <table class="menuTable">
	<tr>
		<td><a href="newUsage"  class="selectedA">Create new assay</a></td>
		<td><a href="newDaughter">Create new daughter tracer tube</a></td>
		<td><a href="newExternal">Define external usage (material leaves the company)</a></td>
		<td><a href="newInVivo">Define in-vivo usage with new sample creation</a></td>
	</tr>
</table>
<h5>Create an assay</h5>
	<div>
		<f:form modelAttribute="biologicalUsage" method="post" action="addNewUsage" id="newUsage_form">
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
					<td><f:input path="amountTaken"  id="form_AmountTaken" /></td>
					<td><span class="error_form" id="amountTaken_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Biological Lab Journal:</div></td>
					<td><f:input path="biologicalLabJournal"  id="form_ELB"/></td>				
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
					<td><div align="right">Liquid Waste:</div></td>
					<td>
						<f:select path="liquidWasteConcat" id="form_liquidwaste">
							<f:option value="-" label="--select liquid waste container--" />
							<f:options items="${liquidWasteList}" />
						</f:select>					
					</td>
					<td><span class="error_form" id="liquidWaste_error_message"></span></td>					
				</tr>
				<tr>
					<td><div align="right">Solid Waste:</div></td>		
					<td>
						<f:select path="solidWasteConcat"  id="form_solidwaste">
							<f:option value="-" label="--select solid waste container--" />
							<f:options items="${solidWasteList}" />
						</f:select>					
					</td>
					<td><span class="error_form" id="solidWaste_error_message"></span></td>					
				</tr>
				<tr>
					<td><div align="right">Fraction put into solid waste (%):</div></td>				
<%-- 					<td><f:input path="solidWastePercentage" /></td>				
					<td><f:errors path="solidWastePercentage"></f:errors> --%>
					<td><f:input path="solidWastePercentage"  id="form_Fraction" /></td>
					<td><span class="error_form" id="fraction_error_message"></span></td>					
														
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
		
		var liquidOpts = $('#form_liquidwaste')[0].options;
		var liquidArray = $.map(liquidOpts, function(elem) {
		    return (elem.text);
		}); 
		//alert(liquidArray);
		var solidOpts = $('#form_solidwaste')[0].options;
		var solidArray = $.map(solidOpts, function(elem) {
		    return (elem.text);
		}); 		
		
		$("#form_tracerId").change(function(){
		    var str  = $('#form_tracerId option:selected').text().split(';')[0];
		    //alert(str);
		    var tracer = str.split('(')[1];
		    var tracerId = str.split(' (')[0];
            //alert(tracerId);
		    
		    if(tracerIdSelected != null){
		    	$('#form_tracerdateId option[value="'+ tracerId +'"]').removeAttr('selected');
            }
            $('#form_tracerdateId option[value="'+ tracerId +'"]').attr("selected", "selected");
		    //alert($('#form_tracerdateId option:selected').text());
		    tracerIdSelected = tracerId;
			$('#form_liquidwaste').find('option:not(:first)').remove();
		    jQuery.each( liquidArray, function( i, val ) {
		    	if (val.indexOf(tracer) >= 0){	    		
		    		//alert(val);
		    		var liquidWasteId = val.split('#').pop().split(':')[0];
		    		//alert(liquidWasteId);
		    		$('#form_liquidwaste').append('<option value="'+liquidWasteId+'">'+val+'</option>');
		    	}
		    });
		    
			$('#form_solidwaste').find('option:not(:first)').remove();
		    jQuery.each( solidArray, function( i, val ) {
		    	if (val.indexOf(tracer) >= 0){	    		
		    		var solidWasteId = val.split('#').pop().split(':')[0];
		    		$('#form_solidwaste').append('<option value="'+solidWasteId+'">'+val+'</option>');
		    	}
		    });	
		    $('#_Date').val('');
		    tracerActivityDateText = $('#form_tracerdateId option:selected').text();
		    refreshDatePicker();
		 });
		 
		
	  });
	</script>
</body>
</html>