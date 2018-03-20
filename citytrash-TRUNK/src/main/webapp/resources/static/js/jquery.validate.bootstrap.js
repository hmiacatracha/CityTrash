
/* Reference: https: //stackoverflow.com/questions/18754020/bootstrap-3-with-jquery-validation-plugin
 * https://progressive-code.com/post/12/Simple-Spring-Boot-CRUD-application-with-Client-side-form-validation
 */

jQuery.validator.setDefaults({
	errorElement : 'span',
	errorClass : 'label label-danger',
	// Style the HTML element in case of validation errors
	highlight : function(element, errorClass, validClass) {

		if (!$(element).hasClass('has-error')) {
			console.log("has-error 1");
			$(element).closest('.form-group').removeClass('has-error has-feedback').addClass('has-success has-feedback');
		}

		if (!$(element).hasClass('novalidation')) {
			$(element).closest('.form-group').removeClass('has-error help-block');
			$(element).closest('.form-group').removeClass('has-success has-feedback').addClass('has-error has-feedback');
		}

	},

	// Style the HTML element in case of validation success
	unhighlight : function(element, errorClass, validClass) {

		if (!$(element).hasClass('has-error')) {
			console.log("has-error 2");
			$(element).closest('.form-group').removeClass('has-error has-feedback').addClass('has-success has-feedback');
		}

		if (!$(element).hasClass('novalidation')) {
			$(element).closest('.form-group').removeClass('has-error help-block');
			$(element).closest('.form-group').removeClass('has-error has-feedback').addClass('has-success has-feedback');
		}
	},

	// Place the error text for different input element types

	errorPlacement : function(error, element) {
		if (element.parent('.input-group').length) {
			error.insertAfter(element.parent());
		} else if (element.prop('type') === 'radio' && element.parent('.radio-inline').length) {
			error.insertAfter(element.parent().parent());
		} else if (element.prop('type') === 'checkbox' || element.prop('type') === 'radio') {
			error.appendTo(element.parent().parent());
		} else {
			error.insertAfter(element);
		}
	}
});