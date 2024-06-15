
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - Show Wastes</title>
	<jsp:include page="includes/header.jsp"/>
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div>
		<f:form modelAttribute="wasteOverview" method="post" action="listWaste">
			<table class="table">
				<tr>
					<td><div align="right">Nuclide:</div></td>
					<td><f:select path="nuclideName">
							<f:option value="any" label="any" />
							<f:options items="${nuclideList}" />
						</f:select>
					</td>
				</tr>			
				<tr>
					<td><div align="right">Closure state:</div></td>
					<td><f:select path="closedOn">
							<f:option value="open" label="All open" />
							<f:option value="closed" label="All closed" />
							<f:option value="discarded" label="All discarded" />							
							<f:options items="${closureDateList}" />
						</f:select>
					</td>					
				</tr>			
				<tr>
					<td><div align="right">Aggregation State:</div></td>
					<td><f:select path="solidLiquidState">
							<f:option value="any" label="any" />
							<f:option value="Y" label="Liquid only" />
							<f:option value="N" label="Solid only" />														
						</f:select>
					</td>	
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" name="Submit" value="Show Waste"></td>
				</tr>				
			</table>
		</f:form>
	</div>
</body>
</html>