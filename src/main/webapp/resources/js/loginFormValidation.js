$(function() {

	$("#userId_error_message").hide();
	$("#password_error_message").hide();

	var error_userId = false;
	var error_password = false;

	function check_userId() {
		var uid = $("#form_userId").val().length;
		if(uid == 0) {
			$("#userId_error_message").html("Please type your login");
			$("#userId_error_message").show();
			error_userId = true;
		} else {
			$("#userId_error_message").hide();
		}	
	}
	
	function check_password() {
		var pass = $("#form_userPassword").val().length;
		if(pass == 0) {
			$("#password_error_message").html("Please type your password");
			$("#password_error_message").show();
			error_password = true;
		} else {
			$("#password_error_message").hide();
		}	
	}

	$("#login_form").submit(function() {
											
		error_userId = false;
		error_password = false;
										
		check_userId()
		check_password();
		
		if(error_userId == false && error_password == false) {
			return true;
		} else {
			return false;	
		}

	});	

});