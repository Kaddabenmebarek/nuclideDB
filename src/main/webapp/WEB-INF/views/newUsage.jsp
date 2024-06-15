<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
	<title>Nuclide DB - New Usage</title>
	<script>
		let noCombobox = true;
	</script>
	<jsp:include page="includes/header.jsp" />

	<meta charset="UTF-8">
	<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	<link href="resources/css/skin/ui.easytree.css" rel="stylesheet" class="skins" type="text/css" />

	<link rel="stylesheet" type="text/css" href="resources/css/stylesheet.css?v=2.1" >
	<link rel="stylesheet" type="text/css" href="resources/css/stylesheet-dark.css?v=1.0" media="screen and (prefers-color-scheme: dark)">

	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script type="text/javascript" src="resources/js/usageAssayFormValidation.js"></script>
	<script type="text/javascript" src="resources/js/usageDaughterFormValidation.js"></script>
	<script type="text/javascript" src="resources/js/usageExternalFormValidation.js"></script>
	<script type="text/javascript" src="resources/js/usageInVivoFormValidation.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easytree.min.js"></script>
</head>
<body>
	<jsp:include page="includes/menu.jsp" />

<div id="body_wrapper">
	<div id="wrapper">
		<div id="main">
			<div id="content">
				<div id="nuclideSearch">
					<input type="search" id="nuclideTreeSearch" placeholder="Search tracer / compound" />
					<select id="nuclideTreeSearchCombo" style="display:none">
						<!-- <option value="0">tracer</option> -->
					</select>
				</div>
				<br />
				<div class="content_box">
					<div id="tree_nuclide">
						<ul>
							<li class="isFolder isExpanded root" title="14C">14C
							<!-- <li class="isFolder"><strong>14C</strong> -->
								${tree14c}
							</li>
							<li class="isFolder isExpanded root">12i
							<!-- <li class="isFolder"><strong>125i</strong> -->
								${tree125i}
							</li>
							<li class="isFolder isExpanded root">32P
							<!-- <li class="isFolder"><strong>32P</strong> -->
								${tree32p}
							</li>
							<li class="isFolder isExpanded root">3H
							<!-- <li class="isFolder"><strong>3H</strong> -->
								${tree3h}
							</li>
							<li class="isFolder isExpanded root">35S
							<!-- <li class="isFolder"><strong>35S</strong> -->
								${tree35s}
							</li>
							<li class="isFolder isExpanded root">33P
							<!-- <li class="isFolder"><strong>33P</strong> -->
								${tree33p}
							</li>
						</ul>
					</div>
					<div class="box">
						<div class="box_content">
						<div class="usageBox">
								Usage: <select id="usageSelect">
								<option value="-">--select an usage--</option>
								<option value="newAssay" id="newAssay" selected="selected">Create new Assay</option>
								<option value="newDaughter" id="newDaughter">Create new Daughter Tracer Tube</option>
								<option value="newExternal" id="newExternal">Define External Usage</option>
								<option value="newInvivo" id="newInvivo">Define In-Vivo Usage</option>
							</select>
							<input type="text" value="new assay" style="display:none" id="nodeText" />
							</div>
							<br /> 
							<div id="parentSelect">
								<table class="table">
									<tr>
										<td class="parentinfos"><div align="right">Parent:</div></td>
										<td class="parentinfos"><select id="lstNodes"></select></td>
										<td class="parentinfos"><div class="container_progress_bar"></div></td>
										<td class="parentinfos"><div class="container_amountleft"></div></td>
									</tr>
								</table>								
							</div>
							<div style="display: none">
								<select id="form_tracerdateId">
									<c:forEach items="${tracerTubeDateMap}" var="tracerTubeDate">
										<option value="${tracerTubeDate.key}">${tracerTubeDate.value}</option>
									</c:forEach>
								</select>
								<select id="form_traceramountId">
									<c:forEach items="${tracerTubeAmountLeftMap}" var="tracerTubeAmountLeft">
										<option value="${tracerTubeAmountLeft.key}">${tracerTubeAmountLeft.value}</option>
									</c:forEach>
								</select>
								<select id="form_tracerpercentageleft">
									<c:forEach items="${tracerPercentageLeftMap}" var="tracerPercentageLeftVal">
										<option value="${tracerPercentageLeftVal.key}">${tracerPercentageLeftVal.value}</option>
									</c:forEach>
								</select>
								
							</div>
							<br />
							<div id="showNewAssay">
								<f:form modelAttribute="biologicalUsage" method="post" action="addNewUsage" id="newUsage_form">
									<table class="table">
										<tr>
											<td>
												<div align="right" style="display:none">Tracer Tube:</div>
												
												<div style="display:none">
													<f:select path="generik" id="form_liquidwaste_hidden">
														<f:option value="-" label="--select liquid waste container--" />
														<f:options items="${liquidWasteList}" />
													</f:select>
													<f:select path="generik" id="form_solidwaste_hidden">
														<f:option value="-" label="--select solid waste container--" />
														<f:options items="${solidWasteList}" />
													</f:select>														
												</div>
												
											</td>
											<td>
												<div style="display:none">
												<f:select path="tracerTubeConcat" id="form_tracerId">
													<f:option value="-" label="--select Tracer Tube--" />
													<f:options items="${tracerTubeList}" />
												</f:select>
												</div>
											</td>
											<td>
												<div style="display:none">
												<span class="error_form" id="tracerId_error_message"></span>
												</div>
											</td>					
										</tr>
										<tr>
											<td><div align="right">Amount taken (ul or mg):</div></td>
											<td><f:input path="amountTaken"  id="assayForm_AmountTaken" /></td>
											<td><span class="error_form" id="assayForm_amountTaken_error_message"></span></td>
										</tr>
										<tr>
											<td><div align="right">Biological Lab Journal:</div></td>
											<td><f:input path="biologicalLabJournal"  id="assayForm_ELB"/></td>				
											<td><span class="error_form" id="assayForm_elb_error_message"></span></td>					
										</tr>
										<tr>
											<td><div align="right">Assay Type:</div></td>
											<td><f:input path="assayType"  id="assayForm_assayType"/></td>				
											<td><span class="error_form" id="assayForm_assayType_error_message"></span></td>					
										</tr>
										<tr>
											<td><div align="right">Usage Date:</div></td>
											<td>
												<input class="datepicker" id="assay_Date" name="recordUsageDate" type="text" readonly="readonly">
											</td>					
											<td><f:errors path="usageDate"></f:errors>	
										</tr>
										<tr>
											<td><div align="right">Liquid Waste:</div></td>
											<td>
												<f:select path="liquidWasteConcat" id="assayForm_liquidwaste">
													<f:option value="-" label="--select liquid waste container--" />
													<f:options items="${liquidWasteList}" />
												</f:select>					
											</td>
											<td><span class="error_form" id="assayForm_liquidWaste_error_message"></span></td>					
										</tr>
										<tr>
											<td><div align="right">Solid Waste:</div></td>		
											<td>
												<f:select path="solidWasteConcat"  id="assayForm_solidwaste">
													<f:option value="-" label="--select solid waste container--" />
													<f:options items="${solidWasteList}" />
												</f:select>					
											</td>
											<td><span class="error_form" id="assayForm_solidWaste_error_message"></span></td>					
										</tr>
										<tr>
											<td><div align="right">Fraction put into solid waste (%):</div></td>				
						<%-- 					<td><f:input path="solidWastePercentage" /></td>				
											<td><f:errors path="solidWastePercentage"></f:errors> --%>
											<td><f:input path="solidWastePercentage"  id="assayForm_Fraction" /></td>
											<td><span class="error_form" id="assayForm_fraction_error_message"></span></td>					
																				
										</tr>
										<tr>
										<td colspan="2"><f:hidden path="newUsageUserId" value='<%=session.getAttribute("username")%>' /></td>
										</tr>				
										<tr>
											<td><div id="assayTreeId"></div></td>
											<td><div id="assayTreeYPosition"></div></td>											
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td><input type="submit" name="Submit" value="Submit Record"></td>
										</tr>				
									</table>
								</f:form>
							</div>
							<div id="showNewDaughter" style="display: none">
								<f:form modelAttribute="biologicalUsage" method="post" action="addNewDaughter"  id="newDaughter_form">
								<table class="table">
									<tr>
										<td></td>
										<td>
											<div style="display:none">
											<f:select path="tracerTubeConcat" id="form_tracerId">
												<f:option value="-" label="--select Tracer Tube--" />
												<f:options items="${tracerTubeList}" />
											</f:select>
											</div>
										</td>									
									</tr>
									<tr>
										<td><div align="right">Amount taken (ul or mg):</div></td>
										<td><f:input path="amountTaken" id="daughterForm_amountTaken" /></td>
										<td><span class="error_form" id="daughterForm_amountTaken_error_message"></span></td>
									</tr>
									<tr>
										<td><div align="right">New total volume (ul):</div></td>
										<td><f:input path="newTotalVolume" id="daughterForm_totalVolume" /></td>				
										<td><span class="error_form" id="daughterForm_totalVolume_error_message"></span></td>					
									</tr>
									<tr>
										<td><div align="right">New batch name (optional):</div></td>
										<td><f:input path="newBatchName" /></td>				
									</tr>
									<tr>
										<td><div align="right">Usage Date:</div></td>
										<td>
											<input class="datepicker" id="daughter_Date" name="recordUsageDate" type="text" readonly="readonly">
										</td>					
										<td><f:errors path="usageDate"></f:errors>	
									</tr>
									<tr>
										<td><div align="right">Location:</div></td>		
										<td>
											<f:select path="location" id="daughterForm_Location">
												<f:option value="-" label="--select location--" />
												<f:options items="${location}" />
											</f:select>					
										</td>
										<td><span class="error_form" id="daughterForm_location_error_message" ></span></td>					
									</tr>
									<tr>
									<td colspan="2"><f:hidden path="newUsageUserId" value='<%=session.getAttribute("username")%>' /></td>
									</tr>
									<tr>
										<td><div id="daughterTreeId" style="display:none"></div></td>
										<td><div id="daughterTreeYPosition" style="display:none"></div></td>											
									</tr>													
									<tr>
										<td>&nbsp;</td>
										<td><input type="submit" name="Submit" value="Submit Record"></td>
									</tr>				
								</table>
							</f:form>
							</div>
							<div id="showNewExternal" style="display: none">
								<f:form modelAttribute="biologicalUsage" method="post" action="addNewExternal" id="newExternalUsage_form">
									<table class="table">
										<tr>
										<td></td>
										<td>
											<div style="display:none">
											<f:select path="tracerTubeConcat" id="form_tracerId">
												<f:option value="-" label="--select Tracer Tube--" />
												<f:options items="${tracerTubeList}" />
											</f:select>
											</div>
										</td>				
										</tr>	
										<tr>
											<td><div align="right">Amount taken (ul or mg):</div></td>
											<td><f:input path="amountTaken" id="externalForm_amountTaken" /></td>
											<td><span class="error_form" id="externalForm_amountTaken_error_message"></span></td>
										</tr>
										<tr>
											<td><div align="right">Biological Lab Journal:</div></td>
											<td><f:input path="biologicalLabJournal"  id="externalForm_elb"/></td>				
											<td><span class="error_form" id="externalForm_elb_error_message"></span></td>					
										</tr>
										<tr>
											<td><div align="right">Assay Type:</div></td>
											<td><f:input path="assayType"  id="externalForm_assayType"/></td>				
											<td><span class="error_form" id="externalForm_assayType_error_message"></span></td>					
										</tr>
										<tr>
											<td><div align="right">Material send to:</div></td>
											<td><f:input path="materialToSend" /></td>				
											<td><f:errors path="materialToSend"></f:errors>					
										</tr>	
										<tr>
											<td><div align="right">Usage Date:</div></td>
											<td>
												<input class="datepicker" id="external_Date" name="recordUsageDate" type="text" readonly="readonly">
											</td>					
											<td><f:errors path="usageDate"></f:errors>	
										</tr>																					
										<tr>
										<td colspan="2"><f:hidden path="newUsageUserId" value='<%=session.getAttribute("username")%>' /></td>
										</tr>
										<tr>
											<td><div id="externalTreeId" style="display:none"></div></td>
											<td><div id="externalTreeYPosition" style="display:none"></div></td>											
										</tr>																											
										<tr>
											<td>&nbsp;</td>
											<td><input type="submit" name="Submit" value="Submit Record"></td>
										</tr>				
									</table>
								</f:form>
							</div>
							<div id="showNewInVivo" style="display: none">
								<f:form modelAttribute="biologicalUsage" method="post" action="addNewInVivo" id="newInVivoUsage_form">
									<table class="table">
										<tr>
										<td></td>
										<td>
											<div style="display:none">
											<f:select path="tracerTubeConcat" id="form_tracerId">
												<f:option value="-" label="--select Tracer Tube--" />
												<f:options items="${tracerTubeList}" />
											</f:select>
											</div>
										</td>
										</tr>
										<tr>
											<td><div align="right">Amount taken (ul or mg):</div></td>
											<td><f:input path="amountTaken" id="invivoForm_amountTaken"/></td>
											<td><span class="error_form" id="invivoForm_amountTaken_error_message"></span></td>
										</tr>
										<tr>
											<td><div align="right">Biological Lab Journal:</div></td>
											<td><f:input path="biologicalLabJournal"  id="invivoForm_elb"/></td>
											<td><span class="error_form" id="invivoForm_elb_error_message"></span></td>
										</tr>
										<tr>
											<td><div align="right">Assay Type:</div></td>
											<td><f:input path="assayType"  id="invivoForm_assayType"/></td>
											<td><span class="error_form" id="invivoForm_assayType_error_message"></span></td>
										</tr>
										<tr>
											<td><div align="right">Usage Date:</div></td>
											<td>
												<input class="datepicker" id="invivo_Date" name="recordUsageDate" type="text" readonly="readonly">
											</td>					
											<td><f:errors path="usageDate"></f:errors>	
										</tr>										
										<tr>
											<td><div align="right">Total Sample Volume (ul):</div></td>
											<td><f:input path="totalSampleVolume" id="invivoForm_totalSample"/></td>
											<td><span class="error_form" id="invivoForm_totalSample_error_message"></span></td>
										</tr>
										<tr>
											<td><div align="right">Sample Location:</div></td>
											<td><f:select path="sampleLocation" id="invivoForm_sampleLocation">
													<f:option value="-" label="--select sample location--" />
													<f:options items="${location}" />
												</f:select></td>
											<td><span class="error_form" id="invivoForm_sampleLocation_error_message"></span></td>
										</tr>
										<tr>
											<td><div align="right">Solid Waste:</div></td>
											<td><f:select path="solidWasteConcat" id="invivoForm_solidWaste">
													<f:option value="-" label="--select solid waste container--" />
													<f:options items="${solidWasteList}" />
												</f:select></td>
											<td><span class="error_form" id="invivoForm_solidWaste_error_message"></span></td>
										</tr>
										<tr>
											<td><div align="right">Fraction put into solid waste (%):</div></td>				
											<td><f:input path="solidWastePercentage" /></td>				
											<td><f:errors path="solidWastePercentage"></f:errors>									
										</tr>				
										<tr>
											<td colspan="2"><f:hidden path="newUsageUserId"
													value='<%=session.getAttribute("username")%>' /></td>
										</tr>
										<tr>
											<td><div id="invivoTreeId" style="display:none"></div></td>
											<td><div id="invivoTreeYPosition" style="display:none"></div></td>											
										</tr>																							
										<tr>
											<td>&nbsp;</td>
											<td><input type="submit" name="Submit" value="Submit Record"></td>
										</tr>
									</table>
								</f:form>
							</div>
							<jsp:include page="includes/i125-div.jsp" />
							<jsp:include page="includes/p32-div.jsp" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="./resources/js/nuclideTreeScript.js"></script>

</body>
</html>