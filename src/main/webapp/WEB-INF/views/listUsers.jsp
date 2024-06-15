
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - List of Users</title>
	<jsp:include page="includes/header.jsp" />
	<script>
		$(document).ready(
				function() {
					$("#mydata").on('init.dt', function(e, settings) {
						$('#mydata').show();
						var isAdmin = "${isUserAdmin}";
						if(isAdmin == 'N'){
							$('.adduser').css('display','none');
							$('.sendemail').css('display','none');
						}
						var isActive = "${isActiveRadio}";
						if(isActive == 'N'){
							$('.adduser').css('display','none');
							$('.sendemail').css('display','none');
						}
					}).on('processing.dt', function(e, settings, processing) {
							$('#processingIndicator').css('display',processing ? 'block' : 'none');
						}).dataTable({
							searchHighlight : true,
							"sDom" : '<"wrapper"Bflipt>rt',
							"iDisplayLength" : 50,
							"bAutoWidth" : true,
							"order": [[ 1, "asc" ]],
							//"bStateSave" : true,
							buttons : 	[
											{
												extend: 'pdf',
												text:      '<i class="far fa-file-pdf"></i>',
												titleAttr: 'Pdf',
												exportOptions: {
													columns: [0,1,2]
												}
											},
											{
											   	extend: 'excel',
												text:      '<i class="far fa-file-excel"></i>',
												titleAttr: 'Excel',
											   	exportOptions: {
													columns: [0,1,2]
												}
											},
											{
											   	extend: 'print',
												text:      '<i class="fas fa-print"></i>',
												titleAttr: 'Print',
											   	exportOptions: {
													columns: [0,1,2]
												}
											},
											{
												text: 'Send Email',
												className: 'sendemail',
												action: function ( e, dt, node, config ) {
													location.href="${mailto}";
												}
											},
											{
												text: 'Add User',
												className: 'adduser',
												action: function ( e, dt, node, config ) {
													location.href="/nuclideDB/showAddUser";
												}
											}
										],
										columnDefs:[
											{ orderSequence:["desc"], targets:[0] },
											{ searchable:!1 },
											{ orderable:!1 }
										]
					});
					setTimeout(function() {
						resetFilter();
					}, 100);
				});
	</script>
	<style type="text/css">
	#mydata_wrapper {
		width: 50% !important;
		margin-left: auto;
		margin-right: auto;
	}
	.toHide{
		display:none
	}
	.text-xs-center {text-align:center!important}
	</style>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div>
		<f:form modelAttribute="userOverview" method="post" action="listUsers">
			<table class="table">
				<tr  style="display:none">
					<td><div align="right">Nuclide User:</div></td>
					<td><f:select path="user">
							<f:option value="any" label="any" />
							<%-- <f:options items="${responsibleList}" /> --%>
						</f:select>
					</td>
				</tr>
				<tr>
					<td><div align="right" style="display:none">Show Nuclide User:</div></td>
					<td>
						<div align="left" style="display:none">
							<label class="radio-button-label container" for="last">
								last usage only
								<f:radiobutton path="lastUsageDate" id="last" value="last" checked="checked" />
								<span class="checkmark2"></span>
							</label>
							<label class="radio-button-label container" for="all">
								all usages
								<f:radiobutton path="lastUsageDate" id="all" value="all" />
								<span class="checkmark2"></span>
							</label>
						</div>
					</td>
				</tr>
				<tr>
					<td><div align="right">User status:</div></td>
					<td>
						<div align="left">
							<label class="radio-button-label container" for="isActiveY">
								Active
								<f:radiobutton path="isActive" id="isActiveY" value="Y" checked="checked" />
								<span class="checkmark2"></span>
							</label>
							<label class="radio-button-label container" for="isActiveN">
								Inactive
								<f:radiobutton path="isActive" id="isActiveN" value="N" />
								<span class="checkmark2"></span>
							</label>
						</div>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" name="Submit" value="Show Users"></td>
				</tr>				
			</table>
		</f:form>
	</div>
	<br/>
	<div>
	<c:if test="${isActiveRadio == 'Y'}"><h4>List Of Active Users</h4></c:if>
	<c:if test="${isActiveRadio == 'N'}"><h4>List Of Inactive Users</h4></c:if>
	<br />
		<c:if test="${nuclideUser_displaynone == 'all'}">
			<table id="mydata" class="table1">
				<thead>
					<tr>
						<th class="toHide">UserId</th>
						<th>User</th>
						<th class="text-xs-center">Usage Date</th>
						<!-- <th>User Status</th> -->
						<c:if test="${isUserAdmin == 'Y'}">
							<th class="text-xs-center">Change Status</th>
								<c:if test="${isActiveRadio == 'Y'}">
									<th class="text-xs-center">Edit User</th>
								</c:if>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${nuclideUsers }" var="nuclideUser">
						<tr>
							<td class="toHide" align="center">${nuclideUser.userId }</td>
							<td align="left">${nuclideUser.user }</td>
							<td align="center">${nuclideUser.lastUsageDate }</td>
							<%-- <td align="center">${nuclideUser.isActive }</td> --%>
							<c:if test="${isUserAdmin == 'Y'}">
								<td align="center">
									<spring:url value="/changeUserStatus_${nuclideUser.userId }" var="changeUserStatusPath" />
									<c:if test="${isActiveRadio == 'Y'}">
										<input type="button" class="button2" value="Set Inactive" onclick="needConfirmation('${nuclideUser.userId}', '${nuclideUser.user}')" />
									</c:if>
									<c:if test="${isActiveRadio == 'N'}">
										<c:if test="${nuclideUser.in == 'N' || nuclideUser.in == null}">N/A</c:if>
										<c:if test="${nuclideUser.in == 'Y'}">
											<input type="button" class="button2" value="Set Active" onclick="needConfirmation('${nuclideUser.userId}', '${nuclideUser.user}')" />
										</c:if>
									</c:if>
								</td>
								<c:if test="${isActiveRadio == 'Y'}">
									<td align="center">
											<spring:url value="/showEditUser${nuclideUser.userId}" var="editUserPath" /> 
											<input type="button" class="button2" value="Edit" onclick="location.href='${editUserPath}'" />
									</td>
								</c:if>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
		<c:if test="${nuclideUser_displaynone == 'last'}">
			<table id="mydata" class="table1">
				<thead>
					<tr>
						<th>User</th>
						<th class="toHide">Nuclide</th>
						<th>Last Usage Date</th>
						<th>User Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${nuclideUsers }" var="nuclideUser">
						<tr>
							<td align="center">${nuclideUser.user }</td>
							<td class="toHide">${nuclideUser.nuclide }</td>
							<td align="center">${nuclideUser.lastUsageDate }</td>
							<td align="center">${nuclideUser.isActive }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</body>
<script type="text/javascript">
	function needConfirmation(userId, userName){
		var txt;
		var r = confirm("Do you really want to change the status of the user " + userName + "?");
		if (r == true) {
			location.href="/nuclideDB/changeStatus" + userId;
		} else {//do nothing
		}
	};
	function resetFilter(){      
		$('#mydata_filter input[type=search]').val('').change();
		$('select[name = "mydata_length"] option:eq(2)').prop('selected',true);
	};
</script>
</html>