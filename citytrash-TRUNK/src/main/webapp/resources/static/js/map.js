jQuery(function($) {
	/*Se ejecutara cada vez que se cargue la pagina*/
	$(document).ready(function() {
		function initMap() {
			var map;
			console.log("pasa por aqui initMap 1");
			map = new google.maps.Map(document.getElementById('googleMaps'), {
				center : {
					lat : 41.3818,
					lng : 2.1685
				},
				zoom : 8
			});
			console.log("pasa por aqui initMap 2");
		}
	});
	
 $(document).ready(function() {
	/* Añadimos lo de autocompletar la direccion en los los mapas y calcular la latitud y longitud*/
	$("#geocomplete").geocomplete({
		map : "#mapa",
		mapOptions : {
			center : new google.maps.LatLng(41.3818, 2.1685),
			zoom : 12
		},
		markerOptions : {
			draggable : true
		},
		details : "#localizacion_div",
		detailsAttribute : "data-geo",
		types : [ "geocode", "establishment" ]
	});

	/* Buscar localizacion*/
	$("#findLocalizacion").click(function() {
		$("#geocomplete").trigger("geocode");
	});
});


/* Cargar una direccion a partir de longitud y latitud*/
$(document).on("change", "#lat", function(e) {
	console.log("latitud longitud pasa por aqui paso 1 ");
	var geocoder = new google.maps.Geocoder;
	var lat = $('#lat');
	var lng = $('#lng');
	var localizacion = $('#geocomplete');
	var mapa = $('#mapa');

	var latlng = {
		lat : parseFloat(lat.val()),
		lng : parseFloat(lng.val())
	};

	console.log("latitud longitud pasa por aqui paso 2 " + latlng);
	var map = new google.maps.Map(document.getElementById('mapa'), {
		zoom : 10,
		center : {
			lat : 41.3818,
			lng : 2.1685
		}
	});
	console.log("latitud longitud pasa por aqui paso 3 " + latlng);
	//var infowindow = new google.maps.InfoWindow;

	console.log("latitud longitud pasa por aqui paso 3 ");
	geocoder.geocode({
		'location' : latlng
	}, function(results, status) {
		if (status === 'OK') {
			if (results[1]) {
				console.log("latitud longitud pasa por aqui paso 3 ");
				map.setZoom(12);
				var marker = new google.maps.Marker({
					position : latlng,
					map : map
				});
				//infowindow.setContent(results[1].formatted_address);
				localizacion.val(results[1].formatted_address);
			//infowindow.open(map, marker);
			} else {
				//window.alert('No results found');
			}
		} else {
			//window.alert('Geocoder failed due to: ' + status);
		}
	});
});

}