
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<html>
<head>
    <title>Nuclide DB - Edit Waste</title>
    <jsp:include page="includes/header.jsp" />
</head>
<body>
    <jsp:include page="includes/menu.jsp" />
	<h1>${headerMessage}</h1>
        <f:form method="POST" action="editWaste" modelAttribute="waste">
        	<f:hidden path="nuclideWasteId" /> 
             <table>
                <tr>
                    <td><f:label path="nuclide.nuclideName">Nuclide</f:label></td>
                    <td><f:input path="nuclide.nuclideName"/></td>
                </tr>
                <tr>
                    <td><f:label path="location">Location</f:label></td>
                    <td><f:input path="location"/></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit"/></td>
                </tr>
            </table>
        </f:form>
</body>
</html>