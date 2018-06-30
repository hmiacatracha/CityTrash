jQuery(function($) {

	/*Evento que muestra la lista de una lista al pasar con el raton, en vez de darle click que es el que trae por defecto */
	$(document).on("mouseenter mouseleave click tap", ".dropdown", function(e) {
		//console.log("EVENT => mouseenter mouseleave click tap");
		//alert("EVENT => mouseenter mouseleave click tap")
		$(this).toggleClass("open");
	});

	/*Si es una venta modal y tiene un formulario, al cerrarse limpia el formulario */
	$(document).on("hidden.bs.modal", ".modal", function(e) {
		//console.log("EVENT => hidden.bs.modal .modal");
		//alert("EVENT => hidden.bs.modal .modal")
		if ($(this).find('form')) {
			try {
				$(this).find('form')[0].reset();
			//console.log("EVENT => hidden.bs.modal, clear modal body true");
			} catch (err) {
				//console.log("EVENT => hidden.bs.modal, clear modal body true");
			}
		}
	});

	/*$(document).on("submit", 'form[data-async]', function(e) {		
		console.log("submit form[data-async] => submit 1");
		var $form = $(this);
		var $target = $($form.attr('data-target'));
		var submitButton = $("input[type='submit'][clicked=true], button[type='submit'][clicked=true]", $form);
		var formData = $form.serializeArray();

		console.log("imprimir name=>" + $(submitButton[0]).attr("name"));
		console.log("imprimir value=>" + $(submitButton[0]).attr("value"));
		
		try {
			formData.push({
				name : $(submitButton[0]).attr("name"),
				value : $(submitButton[0]).attr("value")
			});
		} catch (err) {
			
		}

		console.log("submit form[data-async] => submit 2");
		$.ajax({
			type : $form.attr('method'),
			url : $form.attr('action'),
			data : formData,
			success : function(data, status) {
				console.log("submit form[data-async] => submit 3 =>");
				console.log("data =>" + data);
				$target.html(data);
				$("#content").html(data);
				console.log("submit form[data-async] => submit 4");
			}
		});
		event.preventDefault();
	});
	*/
	/*Si perteneces a la clase registroDialog y se hace un submit => se realiza el registro vía ajax*/
	$(document).on("submit", "#registroDialog", function(e) {
		event.preventDefault();
		//console.log("EVENT => submit #registroDialog");
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

				//$("#content").text("");
				//$("#content").html(data);
				//$dialog.modal("hide");
				$dialog_body.text("");
				$dialog_body.html(data);
				$dialog.scrollTop(0);

			} else {
				//alert('success');
				console.log("SUCCESS");
			//$dialog.modal("hide");
			//$dialog.modal("close");
			}
		}).fail(function(data) {
			//alert('error');
			console.log("ERROR FROM SERVER");
			$dialog.modal("hide");
		});
	});


	/*Muestra la info de la tab cada vez que se muestra */
	$(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function(e) {
		console.log("pasa por aqui => tabRemoteContent");
		event.preventDefault();
		e.target // newly activated tab
		e.relatedTarget // previous active tab
		var url = $(this).attr("href");
		var target = $(this).data("target"); // the target pane
		var pane = $(this); // this tab	

		console.log("URL =>" + url);
		console.log("TARGET =>" + target);
		console.log("pane =>" + pane);
		if (target != undefined && url != undefined) {
			$.ajax({
				url : url,
				cache : false,
				success : function(data) {
					//alert("success target =>" + target + " url =>" + url)	
					//console.log("DATA =>" + data);
					$(target).html(data);
					pane.tab('show');
				},
				error : function(xhr, error) {
					//alert("error");
					//console.log("ERROR =>" + error);
					$(target).html("");
					pane.tab('show');
				}
			});
		}
	});

	/* Dialog*/


	/*
	$(document).on("click", "#link_ver_detalles", function(e) {
		event.preventDefault();
		var workingObject = $(this);
		var url = $(this).attr("href");
		$.ajax({
			url : url,
			type : "GET",
			success : function(data) {
				alert().html(data);
			//$("#content").load(url);
			},
			error : function(xhr, error) {
				alert("ERROR: ", e);
			}
		});
	});*/

	/*Habilita o deshabilita el estado de los trabajadores, camiones, etc  */
	$(document).on("click", "tr #link_change_state", function(e) {
		event.preventDefault();
		console.log("cambiar de estado");
		var workingObject = $(this);
		var url = $(this).attr("href");
		var i = $(this).children("i");
		console.log("url=>" + url);
		console.log("class=>" + i);
		$.ajax({
			url : url,
			type : "POST",
			success : function(data) {
				/*Activado */
				if (data == true) {
					//console.log("devolvio true");
					i.attr('class', "fa fa-toggle-on");
				//i.attr('title', "#{lbl_desactivar}")
				} else if (data == false) {
					//console.log("devolvio false");
					i.attr('class', "fa fa-toggle-off");
				//i.attr('title', "{lbl_activar}")
				}
			},
			error : function(xhr, error) {
				//alert("ERROR AL INTENTAR CAMBIAR EL ESTADO EN EL SERVIDOR de" + url);
			}
		});
	});


	$(document).ready(function() {

		/* searchForm change*/
		/*$("#searchForm").change(function() {
			console.log("searchForm change");
			$("form").submit();
		});*/

		$('#confirmDelete').on('show.bs.modal', function(e) {
			console.log("confirmDelete1");
			$message = $(e.relatedTarget).attr('data-message');
			$(this).find('.modal-body p').text($message);
			$title = $(e.relatedTarget).attr('data-title');
			$(this).find('.modal-title').text($title);

			// Pass form reference to modal for submission on yes/ok
			var form = $(e.relatedTarget).closest('form');
			var name = $(e.relatedTarget).attr('name');
			var value = $(e.relatedTarget).attr('value');

			$(this).find('.modal-footer #confirm').data('form', form);
			$(this).find('.modal-footer #confirm').data('name', name);
			$(this).find('.modal-footer #confirm').data('value', value);
			console.log("confirmDelete3");
		});

		/* Form confirm (yes/ok) handler, submits form */
		$('#confirmDelete').find('.modal-footer #confirm').on('click', function() {
			console.log("confirmDelete 1 yes");
			var $form = $(this).data('form');
			var type = $form.attr('method');
			var url = $form.attr('action');
			var formData = $form.serializeArray();

			formData.push({
				name : $(this).data('name'),
				value : $(this).data('value')
			});

			$.ajax({
				type : type,
				url : url,
				data : formData,
				success : function(data, status) {
					console.log("confirmDelete");
					console.log("data =>" + data);
					$("#sensoresList").html(data);
				}
			});
		});

	});

	$(document).ready(function() {
		/*Comprobamos si el documento tiene nav, si es así entonces activamos el primer tab*/
		if ($(this).find('.nav-tabs')) {
			try {
				$('.nav-tabs > li:first-child > a')[0].click();
			} catch (err) {
				//console.log("activar nav-tab 0 error");
			}
		}
		/*Activamos el tooltip*/
		$('[data-toggle="tooltip"]').tooltip();
	});




	/*Si es una venta modal y tiene un formulario, al cerrarse limpia el formulario */
	$(document).on("submit", "#busquedaTrabajadoresForm", function(e) {
		//alert("pasa por aqui");
		/*event.preventDefault();
		var $form = $(this);
		var formData = $form.serializeArray();
		var $content = $('#content');
		var $listado = $('#listaTrabajadoresContent');

		console.log("pasa por aqui");
		console.log("method => " + $form.attr('method'));
		console.log("method => " + $form.attr('action'));

		$.ajax({
			type : $form.attr('method'),
			url : $form.attr('action'),
			data : formData,
			cache : false,
			success : function(data) {

				if (data.error) {

					$content.text("");
					$content.html(data);
					$content.scrollTop(0);
				}

				// $('.wait').remove();

				if (data.success) {					

					$listado.text("");
					$listado.html(data);
					$content.scrollTop(0);
				} else {
					//return data;
					$listado.text("");
					$listado.html(data);
					$content.scrollTop(0);
				//alert('it worked... but won\'t redirect...');
				}
			
		});}*/

	});

});