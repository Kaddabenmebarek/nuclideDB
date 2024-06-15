
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - Show Users</title>
	<jsp:include page="includes/header.jsp"/>
	<style type="text/css">.toHide{display:none}</style>
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
</body>
</html>