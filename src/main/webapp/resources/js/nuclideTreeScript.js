var addedNode;
var isotope;
var easytree = $('#tree_nuclide').easytree();

$(document).ready(function() {
	loadSelectBox();
	initBtn();
	linkTreeToComboItem();
	progressBar();
	var childId = $("#newChildIdRecorded").text();
	if(childId != '' && childId != null){
		initTreePosition(childId);
	}
	initDatePicker();

});

function loadSelectBox() {
	var select = $('#lstNodes')[0];
	var currentlySelected = $('#lstNodes :selected').val();
	//alert(currentlySelected);
	select.length = 0; // clear select box

	var allNodes = easytree.getAllNodes();
	addOptions(allNodes, select, '', currentlySelected);
	linkTreeToComboItem();
}

function addNode() {
	var sourceNode = {};
	if($('#nodeText').val() != '-'){
		sourceNode.text = $('#nodeText').val();
		//alert($('#nodeText').val());
		var targetId = $('#lstNodes :selected').val();
		easytree.addNode(sourceNode, targetId);
		easytree.rebuildTree();
		loadSelectBox();
	}
}
function removeNodeX() {
	var currentlySelected = $('#lstNodes :selected').val();
	var node = easytree.getNode(currentlySelected);
	if (!node) {
		return;
	}
	easytree.removeNode(node.id);
	easytree.rebuildTree();
	loadSelectBox();
}

function addOptions(nodes, select, prefix, currentlySelected) {
	var i = 0;
	for (i = 0; i < nodes.length; i++) {
		var option = new Option();
		//option.text = prefix + ' ' + nodes[i].text;
		option.text = prefix + nodes[i].text;
		option.value = nodes[i].id;
		option.selected = currentlySelected == nodes[i].id;
		if(option.text == "14C" || option.text == "125i" || option.text == "32P" || option.text == "3H" || option.text == "35S" || option.text == "33P"){					
			option.setAttribute("style","background-color:#5e7ea0");
		}
		select.add(option);
		if (nodes[i].children && nodes[i].children.length > 0) {
			addOptions(nodes[i].children, select, prefix + '_',
					currentlySelected);
		}
	}
}

function initBtn() {
	$('table.table input[type=submit]').attr("disabled", "disabled");
	$('table.table input[type=submit]').addClass('disabled');
}

function initForms() {
	$('table.table input[type=text]').val('');
	$('.error_form').hide();
	$('successMessage').val('');
	$("#assayForm_usageDate").empty();
	$("#daughterForm_usageDate").empty();
	$("#externalForm_usageDate").empty();
	$("#invivoForm_usageDate").empty();
	$("#assayForm_liquidwaste").val("-").change();
	$("#assayForm_solidwaste").val("-").change();
	$("#daughterForm_Location").val("-").change();
	$("#invivoForm_sampleLocation").val("-").change();
	$("#invivoForm_solidWaste").val("-").change();
}

function linkTreeToComboItem() {$("li span").click(function(e) {
	clearErrorMessage();	
	$("#nuclideTreeSearch").val('');
	$("#nuclideTreeSearchCombo").css("display","none");
	var itemAdded = false;
	var linkable = false;
	var item = $(this).text();
	//alert(item);
	var tracerOpts = $('#lstNodes')[0].options;
	var tracers = $.map(tracerOpts, function(elem) {
		return (elem.text);
	});
	var value;
	var selectedTracer = item.substring(0,5).trim();
	//alert(selectedTracer);
	
	var formTracerOpts = $('#form_tracerId')[0].options;
	var formTracers = $.map(formTracerOpts, function(elem) {
		return (elem.text);
	});
	var matchingFormTracerVal;
	
	if(selectedTracer != '14C' && selectedTracer != '125i' && selectedTracer != '32P' && selectedTracer != '3H' && selectedTracer != '35S' && selectedTracer != '33P'){
		//set progress bar
		var selectedTracerPercentageLeft;
        $('#form_tracerpercentageleft option').each(function(){
            if(this.value == selectedTracer){
            	selectedTracerPercentageLeft = this.text;
            }
        });
		if($.type(selectedTracerPercentageLeft) !== "undefined"){
            $(".container_progress_bar").empty();
			$(".container_progress_bar").append('<div class="progress_bar" pourcent="'+selectedTracerPercentageLeft+'" ></div> '+selectedTracerPercentageLeft+'%');				
		}
		//set amount left
		var selectedTracerAmountLeft;
        $('#form_traceramountId option').each(function(){
            if(this.value == selectedTracer){
            	selectedTracerAmountLeft = this.text;
            }
        });
		if($.type(selectedTracerAmountLeft) !== "undefined"){					
			$(".container_amountleft").empty();
			$(".container_amountleft").append('<em>'+selectedTracerAmountLeft+'</em>');
		}
		progressBar();	
	}else{
		$(".container_progress_bar").empty();
		$(".container_amountleft").empty();
	}
	
	switch (item) {
		case "14C":
			value = "14C";
			break;
		case "125i":
			value = "125i";
			break;
		case "32P":
			value = "32P";
			break;
		case "3H":
			value = "3H";
			break;
		case "35S":
			value = "35S";
			break;
		default:
			$.each(tracers, function(i, val) {
				if (val.indexOf(item) >= 0) {
					value = val;
				}
				if (val.indexOf('new') >= 0) {
					itemAdded = true;
				}
			});
			$.each(formTracers, function(i, val) {
				if (val.indexOf(selectedTracer + ' (') >= 0) {
					matchingFormTracerVal = val;
					linkable = true;
				}
			});
			break;
	}

	if(linkable){
		var str = matchingFormTracerVal;
		linkWastesAndTracer(str)
	}

	var comboTreeId;

	if (value != null) {
		$("#form_tracerId option").each(function(a, b) {
			if ($(this).html() == matchingFormTracerVal) {
				$(this).prop("selected",true);
			}
		});
		$("#lstNodes option").each(function(a, b) {
			if ($(this).html() == value) {
					//alert(value);
					//$(this).attr("selected", "selected");
					var usageSelected = $('#nodeText').val();
					$(this).prop("selected",true);
					
					if (!itemAdded && $(this).html() != '125i' 
							&& $(this).html() != '32P' 
							&& $(this).html() != '3H' 
							&& $(this).html() != '14C' 
							&& $(this).html() != '35S' 
							&& $(this).html() != '33P'
							&& $(this).html().indexOf("(disposed)") == -1
							&& $(this).html().indexOf("(external)") == -1) {
						$('table.table input[type=submit]').removeAttr("disabled");
						$('table.table input[type=submit]').removeClass('disabled');
					} else {
						$('table.table input[type=submit]').attr("disabled","disabled");
						$('table.table input[type=submit]').addClass('disabled');
						$(".container_progress_bar").empty();
						$(".container_amountleft").empty();
					}
					comboTreeId = $(this).val();
				}
			});
		}

	var parentY = $("#tree_nuclide").offset();
	var y = $("#" + comboTreeId).offset();
	var yPosition = y.top - parentY.top;
	providePosition(comboTreeId, yPosition);

	});
}

function processProgressBarAndAmountLeft(tracerId){
	//set progress bar
	var comboSelectedTracerPercentageLeft;
    $('#form_tracerpercentageleft option').each(function(){
        if(this.value == tracerId){
        	comboSelectedTracerPercentageLeft = this.text;
        }
    });
	if($.type(comboSelectedTracerPercentageLeft) !== "undefined"){
        $(".container_progress_bar").empty();
		$(".container_progress_bar").append('<div class="progress_bar" pourcent="'+comboSelectedTracerPercentageLeft+'" ></div> '+comboSelectedTracerPercentageLeft+'%');				
	}
	//set amount left
	var comboSelectedTracerAmountLeft;
    $('#form_traceramountId option').each(function(){
        if(this.value == tracerId){
        	comboSelectedTracerAmountLeft = this.text;
        }
    });
	if($.type(comboSelectedTracerAmountLeft) !== "undefined"){					
		$(".container_amountleft").empty();
		$(".container_amountleft").append('<em>'+comboSelectedTracerAmountLeft+'</em>');
	}
	progressBar();
}

function procesFormtracerId(tracerId){
	var matchingFormTracerVal;
	var formTracerOpts = $('#form_tracerId')[0].options;
	var formTracers = $.map(formTracerOpts, function(elem) {
		return (elem.text);
	});
	$.each(formTracers, function(i, val) {
		if (val.indexOf(tracerId) >= 0) {
			matchingFormTracerVal = val;
		}
	});

	var showNewAssay = !$("#showNewAssay").is(':hidden');
	var showNewDaughter = !$("#showNewDaughter").is(':hidden');
	var showNewExternal = !$("#showNewExternal").is(':hidden');
	var showNewInVivo = !$("#showNewInVivo").is(':hidden');

	//if(showNewAssay){
		$("#showNewAssay #form_tracerId option").each(function(a, b) {
			if ($(this).html() == matchingFormTracerVal) {
				$(this).prop("selected",true);
			}
		});
	//}
	//if(showNewDaughter){
		$("#showNewDaughter #form_tracerId option").each(function(a, b) {
			if ($(this).html() == matchingFormTracerVal) {
				$(this).prop("selected",true);
			}
		});

	//}
	//if(showNewExternal){
		$("#showNewExternal #form_tracerId option").each(function(a, b) {
			if ($(this).html() == matchingFormTracerVal) {
				$(this).prop("selected",true);
			}
		});

	//}
	//if(showNewInVivo){
		$("#showNewInVivo #form_tracerId option").each(function(a, b) {
			if ($(this).html() == matchingFormTracerVal) {
				$(this).prop("selected",true);
			}
		});
	//}


	var str = $('#form_tracerId option:selected').text();
	//alert(str);
	linkWastesAndTracer(str)
}

$('#lstNodes').change(
		function() {
			//$("#nuclideTreeSearch").val();
			clearErrorMessage();
			//$("#nuclideTreeSearch").val('');
			//$("#nuclideTreeSearchCombo").css("display","none");
			var tracerOpts = $('#lstNodes')[0].options;
			var tracers = $.map(tracerOpts, function(elem) {
				return (elem.text);
			});
			var itemAdded = false;
			$.each(tracers, function(i, val) {
				if (val.indexOf('new') >= 0) {
					itemAdded = true;
				}
			});

			var currentlySelected = $('#lstNodes :selected').html();
			//alert(currentlySelected);
			
			if (!itemAdded && currentlySelected != '125i'
					&& currentlySelected != '32P'
					&& currentlySelected != '3H'
					&& currentlySelected != '14C'
					&& currentlySelected != '35S'
					&& currentlySelected != '33P'
					&& currentlySelected.indexOf("(disposed)") == -1
					&& currentlySelected.indexOf("(external)") == -1) {
						
				$('table.table input[type=submit]').removeAttr("disabled");
				$('table.table input[type=submit]').removeClass('disabled');

				
				var whiteSpacePos = currentlySelected.indexOf(' ');
				var tracerId = currentlySelected.substring(whiteSpacePos-4, whiteSpacePos);
				//alert(tracerId);
				
				processProgressBarAndAmountLeft(tracerId);
				procesFormtracerId(tracerId);
	
				var comboTreeId = $('#lstNodes :selected').val();
				scrollToTreeNode(comboTreeId);	
				//window.find(tracerId);			
			} else {
				$('table.table input[type=submit]').attr("disabled", "disabled");
				$('table.table input[type=submit]').addClass('disabled');
				$(".container_progress_bar").empty();
				$(".container_amountleft").empty();
			}
});

function providePosition(comboTreeId, yPosition){
	$("#assayTreeId").empty();
	$("#assayTreeYPosition").empty();
	$("#assayTreeId").append('<input name="treeNodeId" style="display:none" value="'+comboTreeId+'" />');
	$("#assayTreeYPosition").append('<input name="treeYPosition" style="display:none" value="'+yPosition+'" />');
	$("#daughterTreeId").empty();
	$("#daughterTreeYPosition").empty();
	$("#daughterTreeId").append('<input name="treeNodeId"  value="'+comboTreeId+'" />');
	$("#daughterTreeYPosition").append('<input name="treeYPosition"  value="'+yPosition+'" />');
	$("#newExternalUsage_form #externalTreeId").empty();
	$("#newExternalUsage_form #externalTreeYPosition").empty();
	$("#newExternalUsage_form #externalTreeId").append('<input name="treeNodeId"  value="'+comboTreeId+'" />');
	$("#newUsage_form #externalTreeYPosition").append('<input name="treeYPosition"  value="'+yPosition+'" />');
	$("#newInVivoUsage_form #invivoTreeId").empty();
	$("#newInVivoUsage_form #invivoTreeYPosition").empty();
	$("#newInVivoUsage_form #invivoTreeId").append('<input name="treeNodeId"  value="'+comboTreeId+'" />');
	$("#newUsage_form #invivoTreeYPosition").append('<input name="treeYPosition"  value="'+yPosition+'" />');
}

$('#usageSelect').change(function() {
	$('#nodeText').val($(this).val());
	initForms();
	$(".successMessage").empty();
	 switch ($(this).val()) {
	case "-":
		$("#parentSelect").hide();
		$("#showNewAssay").hide();
		$("#showNewDaughter").hide();
		$("#showNewExternal").hide();
		$("#showNewInVivo").hide();
		break;
	case "newAssay":
		$("#parentSelect").show();
		$("#showNewAssay").show();
		$("#showNewDaughter").hide();
		$("#showNewExternal").hide();
		$("#showNewInVivo").hide();
		break;
	case "newDaughter":
		$("#parentSelect").show();
		$("#showNewAssay").hide();
		$("#showNewDaughter").show();
		$("#showNewExternal").hide();
		$("#showNewInVivo").hide();
		break;
	case "newExternal":
		$("#parentSelect").show();
		$("#showNewAssay").hide();
		$("#showNewDaughter").hide();
		$("#showNewExternal").show();
		$("#showNewInVivo").hide();
		break;
	case "newInvivo":
		$("#parentSelect").show();
		$("#showNewAssay").hide();
		$("#showNewDaughter").hide();
		$("#showNewExternal").hide();
		$("#showNewInVivo").show();
		break;
	default:
		break;
	}
});

function getId(nodes, nameName) {
    var i = 0;
    for (i = 0; i < nodes.length; i++) {
        if (nodes[i].href && nodes[i].href.toLowerCase() == nameName) {
            return nodes[i].id;
        }
        if (nodes[i].children && nodes[i].children.length > 0) {
            var id = getId(nodes[i].children, nameName);
            if (id) {
                return id;
            }
        }
    }
}

function initTreePosition(childId){
	var optn;
	var trcOpts = $('#lstNodes')[0].options;
	var trcs = $.map(trcOpts, function(elem) {
		return (elem.text);
	});

	$.each(trcs, function(i, val) {
		if (val.indexOf(childId) >= 0) {
			optn = val;
		}
	});
	//alert(optn);
	var allNodes = easytree.getAllNodes();
	var cmbTreeId;
    //var cmbTreeId = getId(allNodes, treeVal);

    $("#lstNodes option").each(function(a, b) {
		if ($(this).html() == optn) {
			cmbTreeId = $(this).val();
		}
	});

    //alert(cmbTreeId);
    easytree.activateNode(cmbTreeId);
    var partY = $("#tree_nuclide").offset();
	var yP = $("#" + cmbTreeId).offset();
	var yPos = yP.top - partY.top;
	$("#tree_nuclide ul").scrollTop(yPos);
}

function progressBar(){
	$( ".progress_bar" ).each(function() {
		var pourcent = parseInt($( this ).attr("pourcent"));
		//alert(pourcent);
		if(pourcent<11){
			$(this ).delay(300)
			.animate({width: "+="+pourcent+"%"}, 300 )
			.css("background","#cb2025");
		}else if(pourcent<50){
			$(this ).delay(300)
			.animate({width: "+="+pourcent+"%"}, 300 )
			.css("background","#E9AF3E");
		}else if(pourcent>49){
			$(this ).delay(300)
			//.animate({width: "+=97%"}, 300 )
			.animate({width: "+="+pourcent+"%"}, 300 )
			.css("background","#7dc323");
		}
	});
}

function initDatePicker(){
	var dateToday = $("#dateToday").val();
	$('.datepicker').val(dateToday);
   	$('.datepicker').datepicker({
  		onSelect: function(date) {
	    	$("#assay_Date").val(date);
	    	$("#daughter_Date").val(date);
	    	$("#external_Date").val(date);
	    	$("#invivo_Date").val(date);
        },
		dateFormat: 'yy-M-d',
  		changeMonth: true,
  		changeYear: true
    });
    var tracerActivityDateText;
    refreshDatePicker(tracerActivityDateText);
}


function refreshDatePicker(tracerActivityDateText) {
    $(".datepicker").datepicker("destroy");
    $( ".datepicker" ).datepicker("refresh");
	//alert(tracerActivityDateText);
	var tracerActivityDate = new Date(tracerActivityDateText);
	//alert(tracerActivityDate);

    $('.datepicker').datepicker({
    	onSelect: function(date) {
    		$("#assay_Date").val(date);
	    	$("#daughter_Date").val(date);
	    	$("#external_Date").val(date);
	    	$("#invivo_Date").val(date);
        },
		dateFormat: 'yy-M-d',
  		changeMonth: true,
  		changeYear: true
  		,minDate: tracerActivityDate
    });
}

function linkWastesAndTracer(str){

	var tracerIdSelected;
	var tracerId = str.split(' (')[0];
    //alert(tracerId);
    var selectedDate;
    $('#form_tracerdateId option').each(function(){
        if(this.value == tracerId){
        	//alert(this.text);
        	selectedDate = this.text;
        }
    });            
    tracerIdSelected = tracerId;
    var tracer = str.substring(str.indexOf("(")+1, str.indexOf(";"));
    isotope = tracer;
    //alert(isotope);
    populateWastes(tracer);
	$("#assay_Date").val('');
	$("#daughter_Date").val('');
	$("#external_Date").val('');
	$("#invivo_Date").val('');
    var tracerActivityDateText = selectedDate;
    //alert(tracerActivityDateText);
    refreshDatePicker(tracerActivityDateText);
}

function populateWastes(isotop){
	$('#assayForm_liquidwaste').find('option:not(:first)').remove();
	$("#form_liquidwaste_hidden option").each(function(a, b) {
		if ($(this).text().indexOf(isotop) >= 0){
			$('#assayForm_liquidwaste').append('<option value="'+$(this).val()+'">'+$(this).text()+'</option>');
		}
	});
	//$('#form_solidwaste').find('option:not(:first)').remove();
	$('form#newUsage_form #assayForm_solidwaste').find('option:not(:first)').remove();
	$('form#newUsage_form #assayForm_liquidwaste').find('option:not(:first)').remove();
	$('form#newInVivoUsage_form #invivoForm_solidWaste').find('option:not(:first)').remove();
	$("#form_solidwaste_hidden option").each(function(a, b) {
		if ($(this).text().indexOf(isotop) >= 0){
			//alert($(this).text());
			$('form#newUsage_form #assayForm_solidwaste').append('<option value="'+$(this).val()+'">'+$(this).text()+'</option>');
			$('form#newInVivoUsage_form #invivoForm_solidWaste').append('<option value="'+$(this).val()+'">'+$(this).text()+'</option>');
			//$('#form_solidwaste').append('<option value="'+$(this).val()+'">'+$(this).text()+'</option>');
		}
	});
	$("#form_liquidwaste_hidden option").each(function(a, b) {
		if ($(this).text().indexOf(isotop) >= 0){
			$('form#newUsage_form #assayForm_liquidwaste').append('<option value="'+$(this).val()+'">'+$(this).text()+'</option>');
		}
	});
}

var topRootNodeId;
$("#nuclideTreeSearch").on("input", function() {
	$("#nuclideTreeSearchCombo").css("display","none");
	var value = $(this).val().toUpperCase();
	var prepend = false;
	$("#lstNodes option").each(function(a, b) {
		if (value &&  $(this).html() != '125i' 
			&& $(this).html() != '32P' 
			&& $(this).html() != '3H' 
			&& $(this).html() != '14C' 
			&& $(this).html() != '35S' 
			&& $(this).html() != '33P'
			&& $(this).html().indexOf("(disposed)") == -1
			&& $(this).html().indexOf("(external)") == -1) {
				var tracer = $(this).html().substring(0, $(this).html().indexOf(" ")).replace(/\_/g, '');
				var compound = $(this).html().substring($(this).html().indexOf(" ")+1, $(this).html().length);
				//console.log(tracer);				
				if (tracer === value) {
					$("#tree_nuclide ul").scrollTop(10);
					//get treenode id
					var comboTreeId;
					$("span .easytree-title").each(function() {
						var treeTracer = $(this).html().substring(0, $(this).html().indexOf(" "));
						if(value === treeTracer){
							comboTreeId = $(this).parent().attr('id');
						}
					});
					scrollToTreeNode(comboTreeId);
					//window.find(tracer);
					$(this).prop("selected",true);
					$('table.table input[type=submit]').removeAttr("disabled");
					$('table.table input[type=submit]').removeClass('disabled');
					processProgressBarAndAmountLeft(tracer);
					procesFormtracerId(tracer);
					
					var matchingFormTracerVal;
					var selectedTracer = tracer + ' (';
					var formTracerOpts = $('#form_tracerId')[0].options;
					var formTracers = $.map(formTracerOpts, function(elem) {
						return (elem.text);
					});
					$.each(formTracers, function(i, val) {
						if (val.indexOf(selectedTracer) >= 0) {
							matchingFormTracerVal = val;
							linkable = true;
						}
					});
					if(linkable){
						linkWastesAndTracer(matchingFormTracerVal);
						$("#form_tracerId option").each(function(a, b) {
							if ($(this).html() == matchingFormTracerVal) {
								$(this).prop("selected",true);
							}
						});
					}
				}
				if(compound.toUpperCase() == value){
					$("#nuclideTreeSearchCombo").css("display","block");
					$("#nuclideTreeSearchCombo").children().remove();
					$("span .easytree-title").each(function() {
						if($(this).html().indexOf("(disposed)") == -1
						&& $(this).html().indexOf("(external)") == -1){
							var treeCompound = $(this).html().substring($(this).html().indexOf(" ")+1, $(this).html().length);
							var treeTracer = $(this).html().substring(0, $(this).html().indexOf(" "));
							if(value === treeCompound.toUpperCase()){
								$("#nuclideTreeSearchCombo").append('<option value="'+$(this).parent().attr('id')+'">'+treeTracer+'</option>');
							}
						}
					});
					var map = {};
					$("#nuclideTreeSearchCombo option").each(function () {
					    if (map[this.text]) {
					        $(this).remove()
					    }
					    map[this.text] = true;
					})
					sortNuclideTreeSearchCombo();
					if(prepend === false){
						$("#nuclideTreeSearchCombo").prepend("<option value='' selected='selected'>tracer</option>");
						prepend = true;
					}
				}
		}
	});
});

function sortNuclideTreeSearchCombo(){
	var options = $("#nuclideTreeSearchCombo option");         
	options.detach().sort(function(a,b) {
	    var at = $(a).text();
	    var bt = $(b).text();
	    return (at > bt)?1:((at < bt)?-1:0);
	});
	options.appendTo("#nuclideTreeSearchCombo");
}

$("#nuclideTreeSearchCombo").change(function() {
	var comboTreeId = $(this).val();
	$("#tree_nuclide ul").scrollTop(10);
	scrollToTreeNode(comboTreeId);
	$("#lstNodes").val(comboTreeId).change();
	$('table.table input[type=submit]').removeAttr("disabled");
	$('table.table input[type=submit]').removeClass('disabled');
	processProgressBarAndAmountLeft(selectedTracer);
});

function scrollToTreeNode(comboTreeId){
	easytree.activateNode(comboTreeId);
	var parentY = $("#tree_nuclide").offset();
	var y = $("#" + comboTreeId).offset();
	var yPosition = y.top - parentY.top;						
	setTimeout(() => {	
		$("#tree_nuclide ul").scrollTop(yPosition);
		providePosition(comboTreeId, yPosition);
	}, 100);
}

var toggled = false;

function toggleRootNodes(){
	$("span .easytree-title").each(function() {
		var treeTracer = $(this).html();
		if(toggled === false 
			&& (treeTracer === '125i' 
			|| treeTracer === '32P' 
			|| treeTracer === '3H' 
			|| treeTracer === '14C' 
			|| treeTracer === '35S' 
			|| treeTracer === '33P')){
				comboTreeId = $(this).parent().attr('id');
				easytree.toggleNode(comboTreeId);
				toggled = true;
		}
	});
}

function clearErrorMessage(){
	//assay
	$("#assayForm_elb_error_message").hide();
	$("#assayForm_liquidWaste_error_message").hide();
	$("#assayForm_solidWaste_error_message").hide();
	$("#assayForm_amountTaken_error_message").hide();
	$("#assayForm_fraction_error_message").hide();
	//daughter
	$("#daughterForm_Location_error_message").hide();
	$("daughterForm_amountTaken_error_message").hide();
	$("daughterForm_totalVolume_error_message").hide();
	//external
	$("#externalForm_elb_error_message").hide();
	$("#externalForm_amountTaken_error_message").hide();	
	//invivo
	$("#invivoForm_elb_error_message").hide();
	$("#invivoForm_sampleLocation_error_message").hide();
	$("#invivoForm_solidWaste_error_message").hide();
	$("#invivoForm_amountTaken_error_message").hide();
	$("#invivoForm_totalSample_error_message").hide();	
	
	//$("#nuclideTreeSearchCombo").empty();
}
