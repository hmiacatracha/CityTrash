

   $(document).ready(function(){
    alert('Hello World');
});


   
    (function($) { 
        /* Dropdowns on Hover and on Click*/
        $('.dropdown').on('mouseenter mouseleave click tap', function() {
            $(this).toggleClass("open");
        });
    })(jQuery); //fin docucumento ready

    (function($) {
        /*select cambiar el color */
        $('select[id$=-status][id^=id_item-]').change(
            function() {
                var color = $('option:selected', this).css('background-color');
                $(this).css('background-color', color);
            }
        ).change();
    })(jQuery);

    (function($) {
        /* Reset any content manually when modal is hidden*/
        $(".modal").on("hidden.bs.modal", function() {
            $('.form').length {
                $(this).find('form')[0].reset();
                console.log("clear modal body true");
            }
            console.log("clear modal body");
        });
    })(jQuery);


    (function($) {
        /*Registro dialog sumbit */
        $('#registroDialog').submit(function() {
            // Prevent the form from submitting via the browser.
            alert("registroDialog submit");
            event.preventDefault();
            console.log("registroDialog sumbit => Añadir trabajador");
            var $dialog = $(this);
            var $dialog_body = $('#registroDialogBody');
            var $form = $('#registroForm');
            var $target = $($form.attr('data-target'));
            var formData = $form.serializeArray();

            $.ajax({
                type: $form.attr('method'),
                url: $form.attr('action'),
                data: formData,
                cache: false,
            }).done(function(data) {
                if (!data.success) {
                    console.log("UNSUCESS");
                    alert('unsuccess');
                    $dialog_body.text("");
                    $dialog_body.html(data);
                    $dialog.scrollTop(0);

                } else {
                    alert('success');
                    console.log("SUCCESS");
                    //$dialog.modal("hide");
                    //$dialog.modal("close");
                }
            }).fail(function(data) {
                alert('error');
                console.log("ERROR FROM SERVER");
                $dialog.modal("hide");
            });
        });

    })(jQuery);


    (function($) {
        /*TABS => tabs con url remote*/
        $('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
            event.preventDefault();
            e.target // newly activated tab
            e.relatedTarget // previous active tab
            console.log("pasa por aqui => tabRemoteContent");
            var url = $(this).attr("href");
            var target = $(this).data("target"); // the target pane
            var pane = $(this); // this tab			
            //alert(url);

            if (target != undefined && url != undefined) {
                $.ajax({
                    url: url,
                    cache: false,
                    success: function(data) {
                        //alert("success target =>" + target + " url =>" + url)						
                        $(target).html(data);
                        pane.tab('show');
                    },
                    error: function(xhr, error) {
                        //alert("error");
                        $(target).html("");
                        pane.tab('show');
                    }
                });
            }
        });

    })(jQuery);

    (function($) {
        /* Activar el primer tab*/
        $('.nav-tabs > li:first-child > a').length {
            activarElPrimerTab();
        }

        function activarElPrimerTab() {
            $('.nav-tabs > li:first-child > a')[0].click();
        }

    })(jQuery);

    (function($) {
        /*Enabled tooltip */
        $('[data-toggle="tooltip"]').tooltip();
    })(jQuery);

    (function($) {
        /*submit busqueda de trabajadores */
        $('#busquedaTrabajadoresForm').submit(function() {
            event.preventDefault();
            alert("pasa por aqui");
        });
    })(jQuery);

/*}); //fin jqyery