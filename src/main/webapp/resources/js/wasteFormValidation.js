$(function() {

	$("#nuclideName_error_message").hide();
	$("#location_error_message").hide();

	var error_nuclidename = false;
	var error_location = false;
	//var i25_dialog = false;

	function check_nuclidename() {
		var nuclname = $("#form_nuclideName").val();
		if(nuclname == '-') {
			$("#nuclideName_error_message").html("Please select a Nuclide Name");
			$("#nuclideName_error_message").show();
			error_nuclidename = true;
		} else {
			$("#nuclideName_error_message").hide();
		}
	
	}
	function check_location() {
		var loc = $("#form_location").val();
		if(loc == '-') {
			$("#location_error_message").html("Please select a Location");
			$("#location_error_message").show();
			error_nuclidename = true;
		} else {
			$("#location_error_message").hide();
		}
	
	}
	
	function disableBtn() {
		$('table.table input[type=submit]').attr("disabled", "disabled");
		$('table.table input[type=submit]').addClass('disabled');
	}

	function enableBtn() {
		$('table.table input[type=submit]').removeAttr("disabled", "disabled");
		$('table.table input[type=submit]').removeClass('disabled');
	}
	
	 /*$("#i125_okbtn").click(function(){
		i25_dialog = true;
		$('#dialog_125i').dialog('close');
		$("#newWaste_form").submit();
	 });*/	

	$("#newWaste_form").submit(function() {
		
		setTimeout(() => {	
			disableBtn();
		}, 200);
													
		error_nuclidename = false;
		error_location = false;
											
		check_nuclidename()
		check_location()
		
		if(error_nuclidename == false && error_location == false) {
			/*var isotope  = $('#form_nuclideName option:selected').text();
		    if("125I" == isotope && i25_dialog == false){
		    	$("#dialog_125i").dialog();
				return false;
		    }else{			
				return true;
			}*/
			return true;
		} else {
			return false;	
		}

	});	

});