$(function() {

	$("#wasteContainer_error_message").hide();

	var error_wastecontainer = false;

	function check_nuclidename() {
		var nuclname = $("#form_nuclideName").val();
		if(nuclname == '-') {
			$("#wasteContainer_error_message").html("Please select a Waste Container");
			$("#wasteContainer_error_message").show();
			error_wastecontainer = true;
		} else {
			$("#wasteContainer_error_message").hide();
		}
	
	}

	$("#dicardTracer_form").submit(function() {
											
		error_wastecontainer = false;
		check_nuclidename()
		
		if(error_wastecontainer == false) {
			return true;
		} else {
			return false;	
		}

	});	

});