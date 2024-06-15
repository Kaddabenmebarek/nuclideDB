<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - New Waste Container</title>
	<jsp:include page="includes/header.jsp"/>
	<script type="text/javascript" src="resources/js/wasteFormValidation.js"></script>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div>
		<f:form modelAttribute="wasteContainer" method="post" action="addNewWaste" id="newWaste_form">
			<table class="table">
				<tr>
					<td><div align="right">Nuclide:</div></td>
					<td><f:select path="nuclideName" id="form_nuclideName">
							<f:option value="-" label="--select nuclide--" />
							<f:options items="${nuclideList}" />
						</f:select>
					</td>
					<td><span class="error_form" id="nuclideName_error_message"></span></td>					
				</tr>			
				<tr>
					<td><div align="right">Waste Aggregation State:</div></td>
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
					<td><div align="right">Location:</div></td>				
					<td><f:select path="location" id="form_location">
							<f:option value="-" label="--select location--" />
							<f:options items="${locationList}" />
						</f:select>
					</td>
					<td><span class="error_form" id="location_error_message"></span></td>
				</tr>
				<tr>
				<!-- TODO get user session -->
				<td colspan="2"><f:hidden path="userId" value='<%=session.getAttribute("username")%>' /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" name="Submit" value="Submit Record"></td>
				</tr>				
			</table>
		</f:form>
	</div>
	<%-- <jsp:include page="includes/i125-div.jsp" /> --%>
</body>
</html>