$(function () {
		
		//$("#assayForm_tracerId_error_message").hide();
		$("#assayForm_elb_error_message").hide();
		$("#assayForm_liquidWaste_error_message").hide();
		$("#assayForm_solidWaste_error_message").hide();
		$("#assayForm_amountTaken_error_message").hide();
		$("#assayForm_fraction_error_message").hide();

		//var error_tracerId = false;
		var error_elb = false;
		var error_liquidWaste = false;
		var error_solidWaste = false;
		var error_amountTaken = false;
		var error_fraction = false;
		var i25_dialog = false;
		var p32_dialog = false;
		

		$("#assayForm_ELB").focusout(function() {
			check_elb();
		});
		$("#assayForm_AmountTaken").focusout(function() {
			check_amountTaken();
		});	
		function check_elb() {
			var elb = $("#assayForm_ELB").val().length;
			if(elb == 0) {
				$("#assayForm_elb_error_message").html("Biological Lab Journal can't be empty");
				$("#assayForm_elb_error_message").show();
				error_elb = true;
			} else {
				$("#assayForm_elb_error_message").hide();
				error_elb = false;
			}	
		}
		function check_waste(){
			var lw = $("#assayForm_liquidwaste").val();
			var sw = $("#assayForm_solidwaste").val();
			if(lw == '-' && sw == '-') {
				$("#assayForm_liquidWaste_error_message").html("Please select a Liquid or Solid Waste");
				$("#assayForm_liquidWaste_error_message").show();
				error_liquidWaste = true;
			}else {
				$("#assayForm_liquidWaste_error_message").hide();
				error_liquidWaste = false;
			}
		}
		
		function check_amountTaken() {
			var initAct = $("#assayForm_AmountTaken").val();
			if($.isNumeric(initAct)){
				if(parseFloat(initAct) == 0.0) {
					$("#assayForm_amountTaken_error_message").html("Please add an Amount greater than 0");
					$("#assayForm_amountTaken_error_message").show();
					error_amountTaken = true;
				} else {
					$("#assayForm_amountTaken_error_message").hide();
					error_amountTaken = false;
				}	
			}else{
				$("#assayForm_amountTaken_error_message").html("Please add a correct value");
				$("#assayForm_amountTaken_error_message").show();
				error_amountTaken = true;
			}
		}	
		function check_fraction() {
			var fraction = $("#assayForm_Fraction").val();
			var sw = $("#assayForm_solidwaste").val();
			var lw = $("#assayForm_liquidwaste").val();
			if($.isNumeric(fraction)){
				if(parseInt(fraction) == 0 && lw == '-' && sw != '-') {
					$("#assayForm_fraction_error_message").html("Please add an Fraction");
					$("#assayForm_fraction_error_message").show();
					error_fraction = true;
				}else if(parseInt(fraction) == 0 && lw != '-' && sw != '-') {
					$("#assayForm_solidWaste_error_message").html("Please remove solid waste as 100% goes into liquid waste");
					$("#assayForm_solidWaste_error_message").show();
					error_solidWaste = true;
				}else if(parseInt(fraction) != 0 && lw != '-' && sw == '-') {
					$("#assayForm_solidWaste_error_message").html("Please select a solid waste");
					$("#assayForm_solidWaste_error_message").show();
					error_solidWaste = true;
				}else if(parseInt(fraction) == 100 && lw != '-' && sw != '-') {
					$("#assayForm_liquidWaste_error_message").html("Please remove liquid waste as 100% goes into solid waste");
					$("#assayForm_liquidWaste_error_message").show();
					error_solidWaste = true;
				}else {
					$("#assayForm_fraction_error_message").hide();
					$("#assayForm_solidWaste_error_message").hide();
					error_fraction = false;
					error_solidWaste = false;
				}	
			}else{
				$("#assayForm_fraction_error_message").html("Please add a correct value");
				$("#assayForm_fraction_error_message").show();
				error_fraction = true;
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
		
		$("#assayForm_liquidwaste").change(function() {
			$("#assayForm_liquidWaste_error_message").hide();
			enableBtn();
		});
		$("#assayForm_solidwaste").change(function() {
			$("#assayForm_solidWaste_error_message").hide();
			enableBtn();
		});
		$("#assayForm_AmountTaken").on("input", function() {
			$("#assayForm_amountTaken_error_message").hide();
			enableBtn();
		});		
		$("#assayForm_ELB").on("input", function() {
			$("#assayForm_elb_error_message").hide();
			enableBtn();
		});
		$("#assayForm_Fraction").on("input", function() {
			$("#assayForm_fraction_error_message").hide();
			enableBtn();
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
		
		$("#newUsage_form").submit(
			function() {
				
				setTimeout(() => {	
					//alert("in");			
					disableBtn();
				}, 200);
				
				//error_tracerId = false;
				error_elb = false;
				error_liquidWaste == false;
				error_solidWaste == false;
				error_amountTaken = false;
				error_fraction = false;

				//check_tracerId();
				check_elb();
				check_waste();
				check_waste();
				check_amountTaken();
				check_fraction();

				if (/*error_tracerId == false 
						&& */error_elb == false
						&& error_liquidWaste == false
						&& error_solidWaste == false
						&& error_amountTaken == false
						&& error_fraction == false) {
					//alert("isotope: " + isotope);
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