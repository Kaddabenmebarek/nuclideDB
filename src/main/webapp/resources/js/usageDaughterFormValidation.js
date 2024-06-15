$(function () {
		    
		//$("#daughterForm_tracerId_error_message").hide();
		$("#daughterForm_Location_error_message").hide();
		$("daughterForm_amountTaken_error_message").hide();
		$("daughterForm_totalVolume_error_message").hide();

		//var error_tracerId = false;
		var error_Location = false;
		var error_amountTaken = false;
		var error_totalVolume = false;
		var i25_dialog = false;
		var p32_dialog = false;

		$("#daughterForm_amountTaken").focusout(function() {
			check_amountTaken();
		});	

		$("#daughterForm_totalVolume").focusout(function() {
			check_totalVolume();
		});		
		
		/*function check_tracerId() {
			var tracerId = $("#daughterForm_tracerId").val();
			if(tracerId == '-') {
				$("#daughterForm_tracerId_error_message").html("Please select a Tracer Tube");
				$("#daughterForm_tracerId_error_message").show();
				error_tracerId = true;
			} else {
				$("#daughterForm_tracerId_error_message").hide();
			}
		}*/

		function check_Loc() {
			var iloc = $("#daughterForm_Location").val();
			if(iloc == '-') {
				$("#daughterForm_Location_error_message").html("Please select a Location");
				$("#daughterForm_Location_error_message").show();
				error_Location = true;
			} else {
				$("#daughterForm_Location_error_message").hide();
			}	
		}
		function check_amountTaken() {
			var amnt = $("#daughterForm_amountTaken").val();
			if($.isNumeric(amnt)){
				if(parseFloat(amnt) == 0.0) {
					$("#daughterForm_amountTaken_error_message").html("Please add an Amount greater than 0");
					$("#daughterForm_amountTaken_error_message").show();
					error_amountTaken = true;
				} else {
					$("#daughterForm_amountTaken_error_message").hide();
				}	
				
			}else{
				$("#daughterForm_amountTaken_error_message").html("Please add a correct value");
				$("#daughterForm_amountTaken_error_message").show();
				error_amountTaken = true;
			}
		}	
		function check_totalVolume() {
			var volume = $("#daughterForm_totalVolume").val();
			if($.isNumeric(volume)){
				if(parseInt(volume) == 0) {
					$("#daughterForm_totalVolume_error_message").html("Please add a Volume");
					$("#daughterForm_totalVolume_error_message").show();
					error_totalVolume = true;
				} else {
					$("#daughterForm_totalVolume_error_message").hide();
				}	
			}else{
				$("#daughterForm_totalVolume_error_message").html("Please add a correct value");
				$("#daughterForm_totalVolume_error_message").show();
				error_totalVolume = true;
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
		
		$("#daughterForm_Location").change(function() {
			$("#daughterForm_Location_error_message").hide();
			enableBtn();
		});
		$("#daughterForm_amountTaken").on("input", function() {
			$("#daughterForm_amountTaken_error_message").hide();
			enableBtn();
		});		
		$("#daughterForm_totalVolume").on("input", function() {
			$("#daughterForm_totalVolume_error_message").hide();
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
		
		$("#newDaughter_form").submit(function() {
			
			setTimeout(() => {	
				disableBtn();
			}, 200);
			
			//error_tracerId = false;
			error_Location = false;
			error_amountTaken = false;
			error_totalVolume = false;
												
			//check_tracerId();
			check_Loc();
			check_amountTaken();
			check_totalVolume();
			
			
			if(/*error_tracerId == false && */error_Location == false && error_amountTaken == false && error_totalVolume == false) {
			    //alert(isotope);
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