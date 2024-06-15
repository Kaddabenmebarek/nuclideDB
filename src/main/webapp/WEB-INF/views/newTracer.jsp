
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

	<title>Nuclide DB - New Tracer Tube</title>
	<jsp:include page="includes/header.jsp" />
	<!--<link rel="stylesheet" href="/resources/demos/style.css">-->
	<script type="text/javascript"	src="resources/js/tracerFormValidation.js"></script>
</head>
<body>
	<jsp:include page="includes/menu.jsp" />
	<div>
		<f:form modelAttribute="tracerTube" method="post"
			action="addNewTracer" id="newTracer_form">
			<table class="table">
				<tr>
					<td><div align="right">Nuclide:</div></td>
					<td><f:select path="nuclideName" id="form_nuclideName">
							<f:option value="-" label="--select nuclide--" />
							<f:options items="${nuclideList}" />
						</f:select></td>
					<td><span class="error_form" id="nuclideName_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Substance Name:</div></td>
					<td><f:input path="substanceName" id="form_substanceName" /></td>
					<td><span class="error_form" id="substanceName_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Batch name (optional):</div></td>
					<td><f:input path="batchName" maxlength="64"/></td>
				</tr>
				<tr>
					<td><div align="right">Aggregation State:</div></td>
					<td>
						<div align="left">
							<label class="radio-button-label container" for="stateY">
								liquid
								<f:radiobutton path="state" id="stateY" value="Y" checked="checked" />
								<span class="checkmark2"></span>
							</label>
							<label class="radio-button-label container" for="stateN">
								solid
								<f:radiobutton path="state" id="stateN" value="N" />
								<span class="checkmark2"></span>
							</label>
						</div>
					</td>
				</tr>
				<tr>
					<td><div align="right">Initial Activity (kBq):</div></td>
					<td><f:input path="initialActivity" id="form_initialActivity" /></td>
					<td><span class="error_form"
						id="initialActivity_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Initial Amount (ul or mg)</div></td>
					<td><f:input path="initialAmount" id="form_initialAmount" /></td>
					<td><span class="error_form" id="initialAmount_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">Date of Initial Activity:</div></td>
					<td>
						<input class="datepicker" id="_Date" name="Date" type="text" value="${today}" readonly="readonly">
						<div id="initialActivityDate"></div> 
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Location:</div></td>
					<td><f:select path="location" id="form_location">
							<f:option value="-" label="--select location--" />
							<f:options items="${locationList}" />
						</f:select></td>
					<td><span class="error_form" id="location_error_message"></span></td>
				</tr>
				<tr>
					<!-- TODO get user session -->
					<td colspan="2"><f:hidden path="userId" value='<%=session.getAttribute("username")%>' /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td colspan="2"><input type="submit" name="Submit"
						value="Submit Record"></td>
				</tr>
			</table>
		</f:form>
	</div>
	<jsp:include page="includes/i125-div.jsp" />
	<jsp:include page="includes/p32-div.jsp" />
	
	<script type="text/javascript">
	$(function () {	
	    $('.datepicker').datepicker({
	    	onSelect: function(date) {
	            //alert(date);
	    		$("#initialActivityDate").empty();
	    		$("#initialActivityDate").append('<input id="iactivityDate" style="display:none" name="activityDate" type="text" value="'+date+'"/>');
	        },
			dateFormat: 'yy-M-d',
      		changeMonth: true,
      		changeYear: true	            
	    });
	  });
	</script>
</body>
</html>