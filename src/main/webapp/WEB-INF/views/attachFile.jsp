<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - Insert Tracer Tube</title>

	<jsp:include page="includes/header.jsp"/>
</head>
<body>
	<jsp:include page="includes/menu.jsp"/>

	<div>
		<h5>Attach the purity data sheet and/or the certificate of the tracer ${tracerId}</h5>
						
		<div id="attachForm" >
			<span id="tracerForwarded" style="display: none">${tracerId}</span>
			<span id="currentUserForwarded" style="display: none"><%=session.getAttribute("username")%></span>
			<form id='fileUpload' onsubmit="return false;" >
				Attach File <input type='file' id='file' multiple="multiple">
				<input type="submit" name="Upload" value="Upload" onclick="" id="uploadBtn">
			</form>
		</div>
		
	</div>

	<script type="text/javascript">
		$(function() {

			var loc = 'attachFile'+parseInt($("#tracerForwarded").text());
			
			$("#uploadBtn").click(function() {
				attachFile()
			});

			function attachFile() {
				var data = new FormData();
				jQuery.each(jQuery('#file')[0].files, function(i, file) {
					data.append('file-' + i, file);
				});
				data.append('tracerId', $("#tracerForwarded").text());
				data.append('currentUserId', $("#currentUserForwarded").text());
				$.ajax({
					url : loc,
					data : data,
					cache : false,
					contentType : false,
					processData : false,
					async : true,
					type : 'POST',
					success : function(response) {
						/*alert('Ok');
						$(window).attr('location',response.redirect)
						if (response.resultat == 200) {
							alert('Ok');
						} else {
							alert('Error');
						}*/
						var redirectionValue = 'listAttachedFiles'+$("#tracerForwarded").text();
						$(window).attr('location',redirectionValue);
					},
				    error: function (request, status, error) {
				    	var redirectionValue = 'listAttachedFiles'+$("#tracerForwarded").text();
						$(window).attr('location',redirectionValue);
				    }
				});
				
				//var redirectionValue = 'listAttachedFiles'+$("#tracerForwarded").text();
				//$(window).attr('location',redirectionValue);
			}
		});
	</script>
	
</body>
</html>