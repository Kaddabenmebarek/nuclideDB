
<%@ page import="java.sql.*,java.text.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - List of Labs</title>
	<jsp:include page="includes/header.jsp" />
<style type="text/css">
	#mydata_wrapper {width: 70% !important;	margin-left: auto;margin-right: auto;}
	.text-xs-center {text-align:center!important}
	.text-xs-left {text-align:left!important}
</style> 
<script>
	$(document).ready(			
			function() {
				//var init = true;
				//applyStyle();
				$("#mydata").on('init.dt', function() {
						$('#mydata').show();
					}).on('order.dt',  function () { 
						var table = $('#mydata').DataTable();
						var order = table.order();
						/*if(!init){
							if(order[0][0] == 0){
								applyStyle();
							}else{
								removeStyle();
							}
						}
						init = false;*/
					}).on('processing.dt', function(e, settings, processing) {
							$('#processingIndicator').css('display', processing ? 'block' : 'none');
						}).dataTable({
							searchHighlight : true,
							"sDom" : '<"wrapper"Bflipt>rt',
							"aLengthMenu": [10, 25, 50, 100, 120],
							"iDisplayLength" : 120,
							"bAutoWidth" : false,
							//"bStateSave" : true,
							buttons: [
							       {
							           	extend: 'pdf',
									   	text:      '<i class="far fa-file-pdf"></i>',
									   	titleAttr: 'Pdf',
							           	exportOptions: {
											columns: [0,1,2,3,4,5] 
							       		}
							       },
							       {
									   extend: 'excel',
									   	text:      '<i class="far fa-file-excel"></i>',
									   	titleAttr: 'Excel',
								   },
							       {
									   extend: 'print',
									   	text:      '<i class="fas fa-print"></i>',
									   	titleAttr: 'Print',
								   }
							    ] 
				});
				/*function applyStyle(){
					$('#mydata tbody tr').eq(0).addClass('newLabo');
					$('#mydata tbody tr').eq(6).addClass('newLabo');
					$('#mydata tbody tr').eq(12).addClass('newLabo');
					$('#mydata tbody tr').eq(18).addClass('newLabo');
					$('#mydata tbody tr').eq(24).addClass('newLabo');
					$('#mydata tbody tr').eq(30).addClass('newLabo');
					$('#mydata tbody tr').eq(36).addClass('newLabo');
					$('#mydata tbody tr').eq(42).addClass('newLabo');
					$('#mydata tbody tr').eq(48).addClass('newLabo');
					$('#mydata tbody tr').eq(54).addClass('newLabo');
					$('#mydata tbody tr').eq(60).addClass('newLabo');
					$('#mydata tbody tr').eq(66).addClass('newLabo');
					$('#mydata tbody tr').eq(72).addClass('newLabo');
					$('#mydata tbody tr').eq(78).addClass('newLabo');
					$('#mydata tbody tr').eq(84).addClass('newLabo');
					$('#mydata tbody tr').eq(90).addClass('newLabo');
					$('#mydata tbody tr').eq(96).addClass('newLabo');
				}
				function removeStyle(){
					$('#mydata tbody tr').removeClass('newLabo');
				}*/
				setTimeout(function() {
					resetFilter();
			    }, 100);
			});
</script>
<meta charset="UTF-8">
</head>
<body>
<jsp:include page="includes/menu.jsp" />
	<div>
			<table id="mydata" class="table1">
				<thead>
					<tr>
						<th class="text-xs-left">Lab</th>
						<th class="text-xs-left">Nuclide</th>
						<th>Activity Threshold (kBq)</th>
						<th>Wastes Activity (kBq)</th>
						<th>Tracers Activity (kBq)</th>
						<th>Total Activity (kBq)</th>
						<th>Detail</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${labOverviewList }" var="labOverview">
						<tr>
						<c:if test="${labOverview.hightlight == 'Y'}">
							<td class="highlight" align="left">${labOverview.locationName }</td>
							<td class="highlight" align="left">${labOverview.nuclide }</td>
							<td class="highlight" align="center">${labOverview.activityThreshold }</td>
							<td class="highlight" align="center">${labOverview.wastesActivity }</td>
							<td class="highlight" align="center">${labOverview.tracersActivity }</td>
							<td class="highlight" align="center">${labOverview.totalActivity }</td>
							<td class="highlight" align="center">
								<spring:url value="/labDetail_${labOverview.id }" var="labDetailPath" /> <input type="button" class="button2" value="Detail" onclick="location.href='${labDetailPath}'" />
							</td>
						</c:if>
						<c:if test="${labOverview.hightlight == 'N'}">
							<td align="left">${labOverview.locationName }</td>
							<td align="left">${labOverview.nuclide }</td>
							<td align="center">${labOverview.activityThreshold }</td>
							<td align="center">${labOverview.wastesActivity }</td>
							<td align="center">${labOverview.tracersActivity }</td>
							<td align="center">${labOverview.totalActivity }</td>
							<td align="center">
								<spring:url value="/labDetail_${labOverview.id }" var="labDetailPath" /> <input type="button" class="button2" value="Detail" onclick="location.href='${labDetailPath}'" />
							</td>
						</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</div>

</body>
<script type="text/javascript">
	function resetFilter(){      
		$('#mydata_filter input[type=search]').val('').change();
		$('select[name = "mydata_length"] option:eq(4)').prop('selected',true);
	};
</script>
</html>
