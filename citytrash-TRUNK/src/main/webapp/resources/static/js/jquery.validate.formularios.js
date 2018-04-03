/* http://www.carlos-garcia.es/tutorial/validacion-de-fomularios-jquery-validator-plugin*/
/* https://techbrij.com/username-check-asp-net-jquery-validation*/
/* http://makitweb.com/add-validation-to-the-form-with-jquery-validation-plugin */
/* https://blog.trescomatres.com/2012/08/validar-un-formulario-con-jquery-antes-de-enviarlo */
/* https://progressive-code.com/post/12/Simple-Spring-Boot-CRUD-application-with-Client-side-form-validation*/
/* https://jqueryvalidation.org/validate/*/
/* https://jqueryvalidation.org/documentation/ */
/* https://www.sitepoint.com/basic-jquery-form-validation-tutorial/ */
/* http://formvalidation.io/addons/i18n/  => internalizacion de mensajes*/


/*
$(document).ready(function() {


	$.validator.addMethod("DateFormat", function(value, element) {
		return value.match(/(?:0[1-9]|[12][0-9]|3[01])\/(?:0[1-9]|1[0-2])\/(?:19|20\d{2})/);
	},
		"Please enter a date in the format mm/dd/yyyy"
	);


	$("#registroForm").validate({
		framework : 'bootstrap',
		debug : true,
		rules : {
			'documento' : {
				required : true,
				minlength : 9,
				maxlength : 15,
				remote : function() {
					return {
						cache : false,
						async : false,
						url : window.location.origin + '/citytrash/' + 'cuenta/validarDocumento?doc=' + $('#documento').val(),
						type : 'POST',
						data : JSON.stringify({
							documento : $('#documento').val()
						}),
						dataFilter : function(data) {
							var result = (JSON.parse(data));
							return JSON.stringify(result);
						}
					}
				}
			},
			'fechaNacimiento' : {
				required : true,
				maxlength : 10,
				minlength : 10,
				DateFormat : true
			},
			'nombre' : {
				required : true,
			},
			'apellidos' : {
				required : true,
			},
			'email' : {
				required : true,
				email : true,
				remote : function() {
					return {
						url : window.location.origin + '/citytrash/' + 'cuenta/validarEmail?email=' + $('#email').val(),
						type : 'POST',
						dataType : "json",
						data : JSON.stringify({
							email : $('#email').val()
						}),
						dataFilter : function(data) {
							var result = (JSON.parse(data));
							return JSON.stringify(result);
						}
					}
				}
			},
			'confirmarEmail' : {
				required : true,
				email : true,
				equalTo : '#email'
			}
		},
		messages : {
			documento : {
				required : "#{dni_nie_invalido}",
				minlength : "#{dni_nie_invalido}",
				maxlength : "#{dni_nie_invalido}",
				remote : "El documento ya se ha asignado a otro trabajador"
			},
			fechaNacimiento : {
				required : "Introduzca la Fecha de Nacimiento",
				date : "Elija una fecha correcta",
				minlength : "Fecha no válida",
				maxlength : "Fecha no válida"
			},
			email : {
				required : "Introduzca un email",
				email : "Email no válido",
				remote : "Ya existe otro usuario la misma cuenta de email"
			},
			confirmarEmail : {
				required : "Introduzca un email",
				email : "Email no válido",
				equalTo : "El email de confirmación que introdució no es igual al email"
			}
		},
		submitHandler : function(form) {
			console.log('submitHabdler javaScript');
			form.submit();
		},
	});
});


*/