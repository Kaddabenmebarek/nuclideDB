
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>
<head>
	<title>Nuclide DB - Edit User</title>
	<jsp:include page="includes/header.jsp" />
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<style type="text/css">
	input[readonly] {
		background-color : #DCDAD1;
		padding: 2px;
		margin: 0 0 0 0;
		background-image: none;
		width:100%;
	}
	#ui-id-1{text-align: left !important;}
	</style>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
 <h5>Edit User</h5>
<div class="actionDiv">
		<f:form modelAttribute="userToEdit" method="post" action="editUser" id="newUser_form">
			<table class="table">
				<tr>
					<td><div align="right">User Id:</div></td>
					<td>
						<div align="left">
							<input type="text" name="userId" readonly="readonly" id="form_userId" value="${userToEdit.userId}"/>
						</div>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">First Name:</div></td>
					<td>
						<div align="left">
							<input type="text" name="firstName" readonly="readonly" id="form_firstName" value="${userToEdit.firstName}"/>
						</div>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Last Name:</div></td>
					<td>
						<div align="left">
							<input type="text" name="lastName" readonly="readonly" id="form_lastName" value="${userToEdit.lastName}"/>
						</div>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Role:</div></td>
					<td>
						<div align="left">
						<f:select path="role" id="form_role" style="width:100%">
							<f:option value="-" label="--select a role--" />
							<f:option value="admin" label="admin" />
							<f:option value="regular" label="regular" />
						</f:select>
						</div>
					</td>
					<td>&nbsp;</td>
					<td><span class="error_form" id="role_error_message"></span></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td colspan="2">
						<div align="left">
						<input type="submit" name="Submit" value="Edit User">
						</div>
					</td>
				</tr>
			</table>
		</f:form>
	</div>
<script type="text/javascript">
	$(function() {
	 	    
		$("#role_error_message").hide();
		var error_role = false;
		
		function check_role() {			
			var role = $("#form_role").val();
			if(role == '-') {
				$("#role_error_message").html("Role not selected");
				$("#role_error_message").show();
				error_lastName = true;
			}else{
				$("#role_error_message").hide();
			}
		}
		
		$("#newUser_form").submit(function() {
			error_role = false;
			check_role();
			if(error_role == false) {
				return true;
			} else {
				return false;	
			}
		});	
	});
</script>	
	
</body>
</html>