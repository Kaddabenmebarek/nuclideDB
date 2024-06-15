
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>
<head>
	<title>Nuclide DB - Relocate Waste</title>
	<jsp:include page="includes/header.jsp" />
</head>
<body>
<jsp:include page="includes/menu.jsp" />
 <h5>Move Waste Container to Other Location</h5>
<div class="actionDiv">
		<f:form modelAttribute="nuclideWaste" method="post" action="relocateWaste" id="relocateWaste_form">
			<table class="table">
				<tr>
					<td><div align="left">Waste Container No:</div></td>
					<td>
						<f:hidden path="nuclideWasteId" value="${nuclideWaste.nuclideWasteId}" />
						<div align="left">${nuclideWaste.nuclideWasteId}</div>
					</td>					
					<td>&nbsp;</td>
				</tr>			
				<tr>
					<td><div align="left">New Waste Location:</div></td>				
					<td><div align="left"><f:select path="location" id="form_relocateWaste">
							<f:option value="-" label="--select new location--" />
							<f:options items="${locationList}" />
						</f:select></div>
					</td>
					<td><span class="error_form" id="relocateWaste_error_message"></span></td>
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
		$("#relocateWaste_error_message").hide();
		var error_relocateWaste = false;
		function check_relocateWaste() {
			var reloc = $("#form_relocateWaste").val();
			if(reloc == '-') {
				$("#relocateWaste_error_message").html("Please select a new location");
				$("#relocateWaste_error_message").show();
				error_relocateWaste = true;
			} else {
				$("#relocateWaste_error_message").hide();
			}
		}
		$("#relocateWaste_form").submit(function() {
			error_relocateWaste = false;
			check_relocateWaste()
			if(error_relocateWaste == false) {
				return true;
			} else {
				return false;	
			}
		});	
	});
</script>	
	
</body>
</html>