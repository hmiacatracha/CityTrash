/*Lista de sensores en el formulario editar Contenedor */
jQuery(function($) {

	$(document).on('change', 'select', function(e) {
		console.log("select 1");
	});

	$(document).on('change', 'select #1', function(e) {
		console.log("select #1");
	});

	$(document).on('change', 'select #2', function(e) {
		console.log("select #2");
	});

	$(document).on('click', 'form #addSensor', function(e) {
		//event.preventDefault();
		/*console.log("add sensor ");
		try {
			var $form = $('form');
			var contenedorForm = $form.serializeArray();
			var sensores = $("#sensores");
			console.log("form contenedor #addSensor click =>" + JSON.stringify(contenedorForm));
			$.ajax({
				type : $form.attr('method'),
				url : $form.attr('action') + '?addSensor',
				data : contenedorForm,
				success : function(data, status) {
					//console.log("success add sensor");
					console.log("success add sensor data status=>" + status);
					console.log("success add sensor data =>" + data);
					sensores.html(data);
					console.log("bien ad =>" + data);

				}
			});
		} catch (err) {
			console.log("error add sensor click");
		}*/
	});


});

var map = L.map('mapa').setView([ 40.0000000, -4.0000000 ], 5);
//var geocodeService = new L.esri.Services.Geocoding();

L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	attribution : '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

/*Layer*/
var results = new L.LayerGroup().addTo(map);

/***
 * Geocodig search => buscar latitud y longitud por el nombre de la calle en el mapa
 */
//L.esri.Geocoding.geosearch => a control for auto-complete enabled search
var searchControl = L.esri.Geocoding.geosearch({
	providers : [
		L.esri.Geocoding.arcgisOnlineProvider({
			categories : [ 'Address', 'Postal', 'Populated Place', ],
			maxResults : 3
		}),
		L.esri.Geocoding.mapServiceProvider({
			label : 'States and Counties',
			url : 'http://sampleserver6.arcgisonline.com/arcgis/rest/services/Census/MapServer',
			layers : [ 2, 3 ],
			searchFields : [ 'NAME', 'STATE_NAME', 'ORGANIZATION' ]
		})
	]
}).addTo(map);



searchControl.on('results', function(data) {
	results.clearLayers();
	for (var i = data.results.length - 1; i >= 0; i--) {
		console.log("result =>" + data.results[i].latlng);
		$('#direccion').val(data.results[i].text);
		$('#lat').val(data.results[i].latlng.lat);
		$('#lng').val(data.results[i].latlng.lng);
		//var id = $('#id').val();
		var nombre = $('#nombre').val();
		var modeloId = $("#modelo").val();
		console.log("nombre =>" + nombre)
		console.log("modeloId =>" + modeloId)
		//console.log("id =>" + id)
		//results.addLayer(L.marker(data.results[i].latlng));
		//results.addLayer(getMarket(data.results[i].latlng.lat, data.results[i].latlng.lng, modeloId, id, nombre))
		results.addLayer(getMarket(data.results[i].latlng, modeloId, nombre))
	}
});


/**
 * Devuelve un L.Market a partir de la latitud, longitud, id y nombre 
 */
function getMarket(latlng, modeloId, nombre) {
	var tipo = function() {
		var tmp = "NONE";
		$.ajax({
			async : false,
			type : "GET",
			global : false,
			url : window.location.origin + '/citytrash/contenedor/modelo/tipo?modeloId=' + modeloId,
			success : function(data) {
				//console.log("data => " + data);
				tmp = data;
			}
		});
		return tmp;
	}();

	var iconUrl = '/citytrash/resources/static/img/contenedores/c_negro.png';
	switch (tipo) {
	case 'INORG':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_amarrillo_di.png';
		break;
	case 'ORGAN':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_marron.png';
		break;
	case 'GLASS':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_verde.png';
		break;
	case 'PAPER':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_azul_claro.png';
		console.log("PAPER");
		break;
	case 'PLAST':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_rojo.png';
		break;
	default:
		iconUrl = '/citytrash/resources/static/img/contenedores/c_negro.png';
	}

	var ICONO = L.icon({
		iconUrl : iconUrl,
		iconSize : [ 30, 40 ], // size of the icon
		iconAnchor : [ 20, 30 ], // point of the icon which will correspond to marker's location
		popupAnchor : [ -3, -76 ] // point from which the popup should open relative to the iconAnchor
	});

	//console.log("tipo =>" + tipo);
	return L.marker(latlng, {
		icon : ICONO,
	}).bindPopup(popupHtml(nombre, tipo))
		.addTo(map);
}

/**
 * crear un popup 
 */
function popupHtml(nombre, tipo) {
	var html = "<b>" + nombre + "</b><br>" + tipo;
	return html;
}

/**
 * Centra el mapa
 */


function centerLeafletMapOnMarker(map, marker) {
	//var latLngs = [ marker.getLatLng() ];
	//var markerBounds = L.latLngBounds(latLngs);
	//map.fitBounds(markerBounds);
}



/**
 * Al cargar el mapa se cargara esta funcion o al cambiar latitud, longitud o el modelo del contenedor
 */

$(document).ready(function() {
	//console.log("ready!");
	initialize();


	$("#modelo").change(function() {
		initialize();
	});

	$("#lat").change(function() {
		initialize();
	});

	$("#lng").change(function() {
		initialize();
	});

	$("#mapa").click(function() {
		console.log("pasa por aqui mapa");
		initialize();
	});
});

function initialize() {
	var inputDireccionFormateada = $('#direccion');
	var inputLatitud = $('#lat');
	var inputLongitud = $('#lng');
	//var id = $('#id').val();
	var modeloId = $("#modelo").val();
	var nombre = $('#nombre').val();

	try {

		var point = {
			lat : inputLatitud.val(),
			lng : inputLongitud.val()
		};

		//clear layers
		results.clearLayers();
		var marker = getMarket(point, modeloId, nombre);
		results.addLayer(marker);
		//centra el mapa 
		centerLeafletMapOnMarker(map, marker);
		L.esri.Geocoding.reverseGeocode().latlng(point).run(function(error, result, response) {
			// callback is called with error, result, and response.
			// result.latlng contains the latlng of the located address
			// result.address contains the address information	
			var direccion = result.address.LongLabel;
			//console.log("adrress =>" + JSON.stringify(result.address));
			/*adrress =>{"Match_addr":"Carrer de Castella 18-28, 08018, El Poblenou, Barcelona, Catalunya","LongLabel":"Carrer de Castella 18-28, 08018, El Poblenou, Barcelona, Catalunya, ESP","ShortLabel":"Carrer de Castella 18-28","Addr_type":"StreetAddress","Type":"","PlaceName":"","AddNum":"20","Address":"Carrer de Castella 20","Block":"","Sector":"","Neighborhood":"El Poblenou","District":"Barcelona","City":"Barcelona","MetroArea":"","Subregion":"Barcelona","Region":"Catalunya","Territory":"","Postal":"08018","PostalExt":"","CountryCode":"ESP"}		 
			 */
			//console.log("direccion inside=>" + direccion);
			$('#direccion').val("" + direccion);
		});

	} catch (err) {
		console.log("error al intentar buscar la direccion x latitud y longitud");
		results.clearLayers();
		$('#direccion').val("");
	}
}