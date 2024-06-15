
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>
<head>
	<title>Nuclide DB - Add User</title>
	<jsp:include page="includes/header.jsp" />
	<style type="text/css">
	input[readonly] {
		/*color: #D1D1D1;*/
		background-color : #DCDAD1;
		padding: 2px;
		margin: 0 0 0 0;
		background-image: none;
		width:100%;
	}
	.ui-menu-item{}
	#ui-id-1{text-align: left !important;}
	</style>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
 <h5>Add a Nuclide User</h5>
<div class="actionDiv">
		<f:form modelAttribute="userOverview" method="post" action="addUser" id="newUser_form">
			<table class="table">
				<tr>
					<td><div align="right">Search for a user:</div></td>
					<td>
						<div align="left">
							<input id="autocplt" style="width:100%"/>
							<br />
						</div>
						<div style="display: none">
							<f:select path="userIdS" id="autocpltValues">
								<f:option value="-" label="--select a userId--" />
								<f:options items="${userIdList}" />
							</f:select>
							<f:select path="generik" id="form_userInfo">
								<f:options items="${userIdMap}" />
							</f:select>
						</div>
					</td>
					<td>
						<a href="javascript:void(0);" class="btn" id="clearUserFields">clear</a>
					</td>
					<td><span class="error_form" id="search_error_message"></span></td>
				</tr>
				<tr>
					<td><div align="right">User Id:</div></td>
					<td>
						<div align="left">
							<input type="text" name="userId" readonly="readonly" id="form_userId"/>
						</div>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">First Name:</div></td>
					<td>
						<div align="left">
							<input type="text" name="firstName" readonly="readonly" id="form_firstName"/>
						</div>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Last Name:</div></td>
					<td>
						<div align="left">
							<input type="text" name="lastName" readonly="readonly" id="form_lastName"/>
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
						<input type="submit" name="Submit" value="Add User">
						</div>
					</td>
				</tr>
			</table>
		</f:form>
	</div>
<script type="text/javascript">
	$(function() {
	 	var values =$.map($('#autocpltValues')[0].options ,function(option) {
	        return option.value;
	    });
		var availableUserIds = values;
	    $("#autocplt").autocomplete({
	        source: 	availableUserIds,
	        autoFocus: 	true,
	        minLength: 	3,   
	        delay: 		100,
	        select: 	showUserInfos
	      }).on('focus', function() {
	    	 	$(this).keydown();
	    	 });
	    
	    function showUserInfos(event, ui) {
	    	var selectedUserId = jQuery.trim(ui.item.value);
	    	$("#form_userInfo").val(selectedUserId);
			var userInfos = $("#form_userInfo option:selected").text();
			//alert(userInfos);
			var userId = userInfos.split('*')[0];
			var firstName = userInfos.split('*').pop().split('~')[0];
			var lastName = userInfos.split('~')[1];
			//alert(userId);
			//alert(firstName);
			//alert(lastName);
			$("#form_userId").val(userId);
			$("#form_firstName").val(firstName);
			$("#form_lastName").val(lastName);
	    }
	    
		$("#search_error_message").hide();
		$("#role_error_message").hide();
		
		var error_search = false;
		var error_role = false;
		
		function check_search() {
			var search = $("#autocplt").val().length;
			if(search == 0) {
				$("#search_error_message").html("Search is empty");
				$("#search_error_message").show();
				error_search = true;
			} else {
				$("#search_error_message").hide();
			}
		}
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
			error_search = false;
			error_role = false;
			check_search();
			check_role();
			if(error_search == false && error_role == false) {
				return true;
			} else {
				return false;	
			}
		});	
		
		$("#clearUserFields").click(function() {
			$("#autocplt").val("");
			$("#form_userId").val("");
			$("#form_firstName").val("");
			$("#form_lastName").val("");
		});
	});
</script>	
	
</body>
</html>