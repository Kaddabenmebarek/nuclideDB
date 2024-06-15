$(function () {
		    
		//validate fields
		//$("#externalForm_tracerId_error_message").hide();
		$("#externalForm_elb_error_message").hide();
		$("#externalForm_amountTaken_error_message").hide();
		//var error_tracerId = false;
		var error_elb = false;
		var error_amountTaken = false;
		var i25_dialog = false;
		var p32_dialog = false;	

		$("#externalForm_elb").focusout(function() {
			check_elb();
		});
		$("#externalForm_amountTaken").focusout(function() {
			check_amountTaken();
		});	
		function check_elb() {
			var elb = $("#externalForm_elb").val().length;
			//alert("form_elb length = " +elb);
			if(elb == 0) {
				$("#externalForm_elb_error_message").html("Biological Lab Journal can't be empty");
				$("#externalForm_elb_error_message").show();
				error_elb = true;
			} else {
				$("#externalForm_elb_error_message").hide();
			}	
		}
		function check_amountTaken() {
			var amnt = $("#externalForm_amountTaken").val();
			if($.isNumeric(amnt)){
				if(parseFloat(amnt) == 0.0) {
					$("#externalForm_amountTaken_error_message").html("Please add an Amount greater than 0");
					$("#externalForm_amountTaken_error_message").show();
					error_amountTaken = true;
				} else {
					$("#externalForm_amountTaken_error_message").hide();
				}	
			}else{
				$("#externalForm_amountTaken_error_message").html("Please add a correct value");
				$("#externalForm_amountTaken_error_message").show();
				error_amountTaken = true;
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
		
		$("#externalForm_amountTaken").on("input", function() {
			$("#externalForm_amountTaken_error_message").hide();
			enableBtn();
		});		
		$("#externalForm_elb").on("input", function() {
			$("#externalForm_elb_error_message").hide();
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
		
		$("#newExternalUsage_form").submit(function() {
			
			setTimeout(() => {	
				disableBtn();
			}, 200);
			
			//alert("in check funtions");
			//error_tracerId = false;
			error_elb = false;
			error_amountTaken = false;
												
			//check_tracerId();
			//alert("check_tracerId " + error_tracerId);
			check_elb();
			//alert("check_elb " + error_elb);
			check_amountTaken();
			//alert("check_amountTaken " + error_amountTaken);
			if(/*error_tracerId == false 
				&& */error_elb == false 
				&& error_amountTaken == false) {
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