

<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - List of Attached Files</title>
	<jsp:include page="includes/header.jsp" />
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div>
		<p>${attachedFile.warningMessage}</p>
		<table id="mydata" class="table1">
			<thead>
				<tr>
					<th>Tracer</th>
					<th>File Name</th>
					<th>File Type</th>
					<th>User</th>
					<th>Date</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${attachedFileList }" var="attachedFile">
					<tr>
						<td>${attachedFile.nuclideBottle }</td>
						<td>${attachedFile.fileName }</td>
						<td>${attachedFile.fileType }</td>
						<td>${attachedFile.nuclideUser }</td>
						<td>${attachedFile.fileDate }</td>
						<td>
							<%-- <a href="${attachedFile.filePath }" class="btn">Download</a> --%> 							
							<spring:url value="/download_attached${attachedFile.filePath}" var="filePath" />
							<a href="${filePath}" class="popup1_open" id="btn2">Download</a>
							
							<spring:url value="/deleteFile${attachedFile.deleteParam}" var="deleteFilePath" />
							<a href="${deleteFilePath}" class="popup1_open" id="btn2">Remove</a>
						</td>
					</tr>		
				</c:forEach>
			</tbody>
		</table>
	</div>

<script>
	$(document).ready(
			function() {
				$("#mydata").on('init.dt', function() {
					$('#mydata').show();
				}).on(
						'processing.dt',
						function(e, settings, processing) {
							$('#processingIndicator').css('display',
									processing ? 'block' : 'none');
						}).dataTable({
					searchHighlight : true,
					"sDom" : '<"wrapper"Bflipt>rt',
					"iDisplayLength" : 25,
					"bAutoWidth" : true,
					"bStateSave" : true,
					buttons : [ /*'copy', 'csv', 'excel', 'pdf', 'print'*/ ]
				});

				/*$('#esc_btn').click(function() {
					jQuery.event.trigger({ type : 'keypress', which : 27 });
				});*/
				setTimeout(function() {
					resetFilter();
			    }, 100);
			});
</script>
<script src="https://cdn.jsdelivr.net/gh/vast-engineering/jquery-popup-overlay@2/jquery.popupoverlay.min.js"></script>

</body>
<script type="text/javascript">
	function resetFilter(){      
		$('#mydata_filter input[type=search]').val('').change();
		$('select[name = "mydata_length"] option:eq(1)').prop('selected',true);
	};
</script>
</html>
