
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!-- jsp:include page="includes/header.jsp" /-->

<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - Insert Tracer Tube</title>

	<jsp:include page="includes/header.jsp"/>
</head>
<body>
	<jsp:include page="includes/menu.jsp" />

	<div>
		<p>${newTracerSaveSuccess}</p>
		<h5>Do you want to attach the purity data sheet and/or the
			certificate of the new tracer?</h5>
			
			<a href="javascript:void(0)" id="expandBtn" class="btn">Yes</a>
			<a href="newTracer" class="btn">No</a>
			
		<div id="attachForm" >
			<span id="tracerForwarded" style="display: none">${nuclideBottleId}</span>
			<span id="currentUserForwarded" style="display: none"><%=session.getAttribute("username")%></span>
			<form id='fileUpload' onsubmit="return false;">
				Attach File <input type='file' id='file' multiple="multiple">
				<input type="submit" name="Upload" value="Upload" onclick=""
					id="uploadBtn"> <span class="success_form"
					style="display: none">added</span>
			</form>
		</div>
		
	</div>

	<script type="text/javascript">
		$(function() {
			$('#attachForm').hide();
			$('#expandBtn').click(function(){
			    $('#attachForm').show();
			});
			
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
					url : 'insertTracer',
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
						$(window).attr('location','listAttachedFiles'+$("#tracerForwarded").text());
					},
				    error: function (request, status, error) {
				    	$(window).attr('location','listAttachedFiles'+$("#tracerForwarded").text());
				    }
				});
				//$(window).attr('location','listAttachedFiles'+$("#tracerForwarded").text())
			}
		});
	</script>
	
</body>
</html>