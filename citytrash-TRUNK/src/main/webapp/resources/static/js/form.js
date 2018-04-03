jQuery(function($) {

	/*https://github.com/thymeleaf/thymeleafexamples-layouts*/
	/*https://github.com/thymeleaf/thymeleafexamples-layouts/blob/master/src/main/java/thymeleafexamples/layouts/signup/SignupController.java*/
	/*https://qtzar.com/2017/03/24/ajax-and-thymeleaf-for-modal-dialogs/*/
	/*http://blog.teamtreehouse.com/create-ajax-contact-form*/
	/*http://librosweb.es/libro/ajax/capitulo_10/la_libreria_jquery.html*/
	/*http://librosweb.es/libro/fundamentos_jquery/*/
	/*http://librosweb.es/libro/fundamentos_jquery/capitulo_7/metodos_ajax_de_jquery.html*/
	/*https://codingsomething.wordpress.com/2011/04/23/libro-gratuito-on-line-sobre-jquery/*/

	/*
	jQuery('ul.nav li.dropdown').hover(function() {
		jQuery(this).find('.dropdown-menu').stop(true, true).delay(200).fadeIn();
	}, function() {
		jQuery(this).find('.dropdown-menu').stop(true, true).delay(200).fadeOut();
	}); */

	/*
	jQuery('ul.nav li.dropdown').hover(function() {
		jQuery(this).find('.dropdown-menu').stop(true, true).delay(200).fadeIn();
		jQuery(this).addClass('open');
	}, function() {
		jQuery(this).find('.dropdown-menu').stop(true, true).hide();
		jQuery(this).removeClass('open');
	});*/

	$(document).ready(function() {

		/* Dropdowns on Hover and on Click*/
		$('.dropdown').on('mouseenter mouseleave click tap', function() {
			$(this).toggleClass("open");
		});

		$('select[id$=-status][id^=id_item-]').change(
			function() {
				var color = $('option:selected', this).css('background-color');
				$(this).css('background-color', color);
			}
		).change();

		/* Reset any content manually when modal is hidden*/
		$(".modal").on("hidden.bs.modal", function() {
			if ($(this).find('.form')) {
				$(this).find('form')[0].reset();
			}
			console.log("clear modal body");
		});

		/* Reset any content manually when modal is hidden*/
		$(".modal").on("shown.bs.modal", function() {});

		$('#registroDialog').submit(function() {
			// Prevent the form from submitting via the browser.
			event.preventDefault();
			console.log("registroDialog sumbit => Añadir trabajador");
			var $dialog = $(this);
			var $dialog_body = $('#registroDialogBody');
			var $form = $('#registroForm');
			var $target = $($form.attr('data-target'));
			var formData = $form.serializeArray();

			$.ajax({
				type : $form.attr('method'),
				url : $form.attr('action'),
				data : formData,
				cache : false,
			}).done(function(data) {
				if (!data.success) {
					console.log("UNSUCESS");
					//alert('unsuccess');				
					$dialog_body.text("");
					$dialog_body.html(data);
					$dialog.scrollTop(0);

				} else {
					//alert('success');
					console.log("SUCCESS");
					$dialog.modal("hide");
				}
			}).fail(function(data) {
				//alert('error');
				console.log("ERROR FROM SERVER");
				$dialog.modal("hide");
			});
		});

	}); //fin docucumento ready
}); //fin jqyery