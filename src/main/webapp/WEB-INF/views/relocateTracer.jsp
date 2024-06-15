
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
	<title>Nuclide DB - Relocate Tracer</title>
	<jsp:include page="includes/header.jsp" />
</head>
<body>
<jsp:include page="includes/menu.jsp" />
 <h5>Move Tracer Tube to Other Lab</h5>
<div class="actionDiv">
		<f:form modelAttribute="nuclideBottle" method="post" action="relocateTracer" id="relocateTracer_form">
			<table class="table">
				<tr>
					<td><div align="left">Tracer Tube No:</div></td>
					<td>
						<f:hidden path="nuclideBottleId" value="${nuclideBottle.nuclideBottleId}" />
						<div align="left">${nuclideBottle.nuclideBottleId}</div>
					</td>					
					<td>&nbsp;</td>
				</tr>			
				<tr>
					<td><div align="left">New Tracer Location:</div></td>				
					<td><f:select path="location" id="form_relocateTracer">
							<f:option value="-" label="--select new location--" />
							<f:options items="${locationList}" />
						</f:select>
					</td>
					<td><span class="error_form" id="relocateTracer_error_message"></span></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><div align="left"><input type="submit" name="Submit" value="Submit Record"></div></td>
				</tr>				
			</table>
		</f:form>
	</div>
<script type="text/javascript">
	$(function() {
		$("#relocateTracer_error_message").hide();
		var error_relocateTracer = false;
		function check_relocateTracer() {
			var relocT = $("#form_relocateTracer").val();
			if(relocT == '-') {
				$("#relocateTracer_error_message").html("Please select a new location");
				$("#relocateTracer_error_message").show();
				error_relocateTracer = true;
			} else {
				$("#relocateTracer_error_message").hide();
			}
		}
		$("#relocateTracer_form").submit(function() {
			error_relocateTracer = false;
			check_relocateTracer()
			if(error_relocateTracer == false) {
				return true;
			} else {
				return false;	
			}
		});	
	});
</script>		
</body>
</html>