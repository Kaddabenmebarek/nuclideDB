
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - Show Tracers</title>
	<jsp:include page="includes/header.jsp"/>
</head>
<body>
<jsp:include page="includes/menu.jsp" />

	<div>
		<f:form modelAttribute="tracerOverview" method="post" action="listTracers" id="showTracersForm">
			<table class="table">
				<tr>
					<td><div align="right">Nuclide:</div></td>
					<td><f:select path="nuclideName">
							<f:option value="any" label="any" />
							<f:options items="${nuclideList}" />
						</f:select>
					</td>
					<td>&nbsp;</td>
				</tr>			
				<tr>
					<td><div align="right">Lab:</div></td>
					<td><f:select path="location">
							<f:option value="any" label="any" />
							<f:options items="${locationList}" />
						</f:select>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Year of initial activity:</div></td>
					<td><f:select path="activityYear">
							<f:option value="any" label="any" />
							<f:options items="${yearList}" />
						</f:select>
					</td>
					<td>&nbsp;</td>					
				</tr>
				<tr>
					<td><div align="right">Tracer status:</div></td>
					<td><f:select path="discardedStatus">
							<f:option value="any" label="All tracers" />
							<f:option value="E" label="Tracers for external usage" />
							<f:option value="N" label="Non-discarded tracers" selected="true" />
							<f:option value="Y" label="Discarded tracers" />
							<f:options items="${discardDateList}" />														
						</f:select>
					</td>
					<td><span class="error_form" id="discard_error_message"></span></td>
				</tr>				
				<tr>
					<td><div align="right">Aggregation State:</div></td>
					<td><f:select path="solidLiquidState">
							<f:option value="any" label="any" />
							<f:option value="Y" label="Liquid only" />
							<f:option value="N" label="Solid only" />
						</f:select>
					</td>	
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Original/Daughter state:</div></td>
					<td><f:select path="originalDaughterState">
							<f:option value="any" label="any" />
							<f:option value="original" label="Original tracers only" />
							<f:option value="daughter" label="Daugter tracers only" />														
						</f:select>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Responsible Scientist:</div></td>				
					<td><f:select path="responsible">
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
</body>
<script type="text/javascript">
	$('#externalTracersOnly').change(function(){
		$("#discard_error_message").hide();
 		if($('#externalTracersOnly').is(':checked')){
 			$('#originalDaughterState option:eq(1)').prop('selected',true);
 			$('#discardedStatus option:eq(2)').prop('selected',true);
		}else{
 			$('#originalDaughterState option:eq(0)').prop('selected',true);
		}
	});
	$('#discardedStatus').change(function(){
		$("#discard_error_message").hide();
	})
	$("#discard_error_message").hide();
	var error_discard = false;
	function check_discard() {
		var discardStatus = $("#discardedStatus option:selected").text();
		if($('#externalTracersOnly').is(':checked') && discardStatus != 'Non-discarded tubes') {
			$("#discard_error_message").html("Please change to 'Non-discarded tubes' or uncheck the option 'Show external tracers only'.");
			$("#discard_error_message").show();
			error_discard = true;
		} else {
			$("#discard_error_message").hide();
		}
	}
	$("#showTracersForm").submit(function() {
		if($('#externalTracersOnly').is(':checked')){			
			$('#originalDaughterState option:eq(1)').prop('selected',true);
		}
		error_discard = false;
		check_discard();
		if(error_discard == false) {
			return true;
		} else {
			return false;	
		}
	});	
</script>
</html>