$(function () {
		
		//$("#invivoForm_tracerId_error_message").hide();
		$("#invivoForm_elb_error_message").hide();
		$("#invivoForm_sampleLocation_error_message").hide();
		$("#invivoForm_solidWaste_error_message").hide();
		$("#invivoForm_amountTaken_error_message").hide();
		$("#invivoForm_totalSample_error_message").hide();

		//var error_tracerId = false;
		var error_elb = false;
		var error_wastePercentage = false;
		var error_samplelocation = false;
		var error_solidWaste = false;
		var error_amountTaken = false;
		var error_totalSample = false;
		var i25_dialog = false;
		var p32_dialog = false;	

		$("#invivoForm_elb").focusout(function() {
			check_elb();
		});
		/*$("#invivoForm_ssayType").focusout(function() {
			check_assayType();
		});*/
		$("#invivoForm_amountTaken").focusout(function() {
			check_amountTaken();
		});
		$("#invivoForm_totalSample").focusout(function() {
			check_totalSample();
		});	

		function check_elb() {
			var elb = $("#invivoForm_elb").val().length;
			if(elb == 0) {
				$("#invivoForm_elb_error_message").html("Biological Lab Journal can't be empty");
				$("#invivoForm_elb_error_message").show();
				error_elb = true;
			} else {
				$("#invivoForm_elb_error_message").hide();
			}	
		}
		
		function check_wastePercentage() {
			//var lw = $("#invivoForm_iquidWaste").val();
			var sw = $("#invivoForm_olidWaste").val();
			if(/*lw == '-' && */sw == '-') {
				//$("#invivoForm_liquidWaste_error_message").html("Please select a Liquid or Solid Waste");
				//$("#invivoForm_liquidWaste_error_message").show();
				$("#invivoForm_solidWaste_error_message").html("Please select a Solid or Liquid Waste");
				$("#invivoForm_solidWaste_error_message").show();			
				error_wastePercentage = true;
			} else {
				$("#invivoForm_liquidWaste_error_message").hide();
				$("#invivoForm_solidWaste_error_message").hide();
			}
		}
		
		function check_samplelocation() {
			var sl = $("#invivoForm_sampleLocation").val();
			if(sl == '-') {
				$("#invivoForm_sampleLocation_error_message").html("Please select a Location");
				$("#invivoForm_sampleLocation_error_message").show();
				error_samplelocation = true;
			} else {
				$("#invivoForm_sampleLocation_error_message").hide();
			}
		}
		
		function check_solidWaste() {
			var sw = $("#invivoForm_solidWaste").val();
			if(sw == '-') {
				$("#invivoForm_solidWaste_error_message").html("Please select a Solid Waste");
				$("#invivoForm_solidWaste_error_message").show();
				error_solidWaste = true;
			} else {
				$("#invivoForm_solidWaste_error_message").hide();
			}
		}
		
		function check_amountTaken() {
			var amnt = $("#invivoForm_amountTaken").val();
			if($.isNumeric(amnt)){				
				if(parseFloat(amnt) == 0.0) {
					$("#invivoForm_amountTaken_error_message").html("Please add an Amount greater than 0");
					$("#invivoForm_amountTaken_error_message").show();
					error_amountTaken = true;
				} else {
					$("#invivoForm_amountTaken_error_message").hide();
				}	
			}else{
				$("#invivoForm_amountTaken_error_message").html("Please add a correct value");
				$("#invivoForm_amountTaken_error_message").show();
				error_amountTaken = true;
			}
		}	
		
		function check_totalSample() {
			var volume = $("#invivoForm_totalSample").val();
			if($.isNumeric(volume)){
				if(parseInt(volume) == 0) {
					$("#invivoForm_totalSample_error_message").html("Please add a Sample Volume");
					$("#invivoForm_totalSample_error_message").show();
					error_totalSample = true;
				} else {
					$("#invivoForm_totalSample_error_message").hide();
				}	
			}else{
				$("#invivoForm_totalSample_error_message").html("Please add a correct value");
				$("#invivoForm_totalSample_error_message").show();
				error_totalSample = true;
			}
		}		

		function disableBtn() {
			$('table.table input[type=submit]').attr("disabled", "disabled");
			$('table.table input[type=submit]').addClass('disabled');
		}
		
		function enableBtn() {
			var parentTracer = $( "#lstNodes option:selected" ).text();
			if (parentTracer != '14C' && parentTracer != '32P' && parentTracer != '3H' && parentTracer != '35S' && parentTracer != '33P'){
				$('table.table input[type=submit]').removeAttr("disabled", "disabled");
				$('table.table input[type=submit]').removeClass('disabled');
			}
		}
		
		$("#invivoForm_sampleLocation").change(function() {
			$("#invivoForm_sampleLocation_error_message").hide();
			enableBtn();
		});
		$("#invivoForm_solidWaste").change(function() {
			$("#invivoForm_solidWaste_error_message").hide();
			enableBtn();
		});
		$("#invivoForm_amountTaken").on("input", function() {
			$("#invivoForm_amountTaken_error_message").hide();
			enableBtn();
		});		
		$("#invivoForm_elb").on("input", function() {
			$("#invivoForm_elb_error_message").hide();
			enableBtn();
		});	
		$("#invivoForm_totalSample").on("input", function() {
			$("#invivoForm_totalSample_error_message").hide();
			enableBtn();
		});					

		$("#i125_okbtn").click(function(){
			i25_dialog = true;
			$('#dialog_125i').dialog('close');
			var showNewAssay = !$("#showNewAssay").is(':hidden');
			var showNewDaughter = !$("#showNewDaughter").is(':hidden');
			var showNewExternal = !$("#showNewExternal").is(':hidden');
			var showNewInVivo = !$("#showNewInVivo").is(':hidden');
			
			if(showNewAssay){$("#newUsage_form").submit();}
			if(showNewDaughter){$("#newDaughter_form").submit();}
			if(showNewExternal){$("#newExternalUsage_form").submit();}
			if(showNewInVivo){$("#newInVivoUsage_form").submit();}
		});
		//dialog_P32
		$("#p32_okbtn").click(function(){
			p32_dialog = true;
			$('#dialog_P32').dialog('close');
			var showNewAssay = !$("#showNewAssay").is(':hidden');
			var showNewDaughter = !$("#showNewDaughter").is(':hidden');
			var showNewExternal = !$("#showNewExternal").is(':hidden');
			var showNewInVivo = !$("#showNewInVivo").is(':hidden');
			
			if(showNewAssay){$("#newUsage_form").submit();}
			if(showNewDaughter){$("#newDaughter_form").submit();}
			if(showNewExternal){$("#newExternalUsage_form").submit();}
			if(showNewInVivo){$("#newInVivoUsage_form").submit();}
		});

		$("#newInVivoUsage_form").submit(function() {
												
			setTimeout(() => {	
				disableBtn();
			}, 200);
			
			//error_tracerId = false;
			error_elb = false;
			error_samplelocation == false;
			error_solidWaste == false;
			error_amountTaken = false;
			error_totalSample = false;
			error_wastePercentage = false;
												
			//check_tracerId();
			check_elb();
			check_wastePercentage();
			check_samplelocation();
			check_solidWaste();
			check_amountTaken();
			check_totalSample();
			
			if(/*error_tracerId == false && */error_elb == false && error_wastePercentage == false && error_amountTaken == false && error_totalSample == false && error_solidWaste == false && error_samplelocation == false) {
			    if("125I" == isotope && i25_dialog == false){
			    	$("#dialog_125i").dialog();
					return false;
			    }else if("32P" == isotope && p32_dialog == false){
						$("#dialog_P32").dialog();
						return false;
				}else{			
					return true;
				}
			} else {
				return false;	
			}

		});	

		    
		    
	  });