$(function() {

	$("#nuclideName_error_message").hide();
	$("#substanceName_error_message").hide();
	$("#initialActivity_error_message").hide();
	$("initialAmount_error_message").hide();
	$("#location_error_message").hide();

	var error_nuclidename = false;
	var error_substancename = false;
	var error_initialActivity = false;
	var error_initialAmount = false;
	var error_location = false;
	var i25_dialog = false;
	var p32_dialog = false;


	$("#form_substanceName").focusout(function() {
		check_substancename();
	});
	$("#form_initialActivity").focusout(function() {
		check_initialActivity();
	});
	$("#form_initialAmount").focusout(function() {
		check_initialAmount();
	});

	function check_nuclidename() {
		var nuclname = $("#form_nuclideName").val();
		if (nuclname == '-') {
			$("#nuclideName_error_message").html("Please select a Nuclide Name");
			$("#nuclideName_error_message").show();
			error_nuclidename = true;
		} else {
			$("#nuclideName_error_message").hide();
		}

	}
	function check_substancename() {
		var substname = $("#form_substanceName").val().length;
		if (substname == 0) {
			$("#substanceName_error_message").html("Substance name can't be empty");
			$("#substanceName_error_message").show();
			error_substancename = true;
		} else {
			$("#substanceName_error_message").hide();
		}
	}
	function check_initialActivity() {
		var initAct = $("#form_initialActivity").val();
		if (parseInt(initAct) == 0) {
			$("#initialActivity_error_message").html("Please add an Initial Activity");
			$("#initialActivity_error_message").show();
			error_initialActivity = true;
		} else {
			$("#initialActivity_error_message").hide();
		}
	}
	function check_initialAmount() {
		var initAmt = $("#form_initialAmount").val();
		if (parseInt(initAmt) == 0) {
			$("#initialAmount_error_message").html("Please add an Initial Amount");
			$("#initialAmount_error_message").show();
			error_initialAmount = true;
		} else {
			$("#initialAmount_error_message").hide();
		}
	}
	function check_location() {
		var loc = $("#form_location").val();
		if (loc == '-') {
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

	$("#i125_okbtn").click(function() {
		i25_dialog = true;
		$('#dialog_125i').dialog('close');
		$("#newTracer_form").submit();
	});
	$("#p32_okbtn").click(function() {
		p32_dialog = true;
		$('#dialog_P32').dialog('close');
		$("#newTracer_form").submit();
	});

	$("#newTracer_form").submit(function() {

		setTimeout(() => {	
			disableBtn();
		}, 200);
		
		error_nuclidename = false;
		error_substancename = false;
		error_initialActivity = false;
		error_initialAmount = false
		error_location = false;

		check_nuclidename();
		check_substancename();
		check_initialActivity();
		check_initialAmount();
		check_location();

		if (error_nuclidename == false && error_substancename == false && error_initialActivity == false && error_initialAmount == false && error_location == false) {
			var isotope = $('#form_nuclideName option:selected').text();
			//alert("isotope: " + isotope);
			if ("125I" == isotope && i25_dialog == false) {
				$("#dialog_125i").dialog();
				return false;
			} else if ("32P" == isotope && p32_dialog == false) {
				$("#dialog_P32").dialog();
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}

	});

});