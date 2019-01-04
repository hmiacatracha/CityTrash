/*https://esri.github.io/esri-leaflet/api-reference/controls/geosearch.html
 * https://github.com/Esri/esri-leaflet-geocoder/
 * 
 */

var map = L.map('mapa').setView([ 40.0000000, -4.0000000 ], 5);
//var geocodeService = new L.esri.Services.Geocoding();

L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	attribution : '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

/*Layer*/
var results = new L.LayerGroup().addTo(map);

/**
 * Al cargar el mapa se cargara esta funcion o al cambiar latitud, longitud o el modelo del contenedor
 */

console.log("ready 1!");

$(document).ready(function() {
	console.log("ready2!");
	initialize();
});


$('a[data-toggle=tab]').click(function() {
	alert(this.id);
	console.log("cargando mapa! 2");
});


$(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function(e) {
	console.log("cargando mapa! 1");
	initialize();
});

function initialize() {
	var labelDireccion = $('#direccion');
	var labelLatitud = $('#lat');
	var labelLongitud = $('#lng');
	var tipo = $("#tipo").text();
	var nombre = $('#nombre').text();

	try {

		var point = {
			lat : labelLatitud.text(),
			lng : labelLongitud.text()
		};


		//clear layers		
		results.clearLayers();
		var marker = getMarket(point, tipo, nombre);
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
			labelDireccion.text("" + direccion.toUpperCase());
		});

	} catch (err) {
		console.log("error al intentar buscar la direccion x latitud y longitud");
		results.clearLayers();
		labelDireccion.text("");
	}
}
/**
 * Devuelve un L.Market a partir de la latitud, longitud, id y nombre 
 */
function getMarket(latlng, tipo, nombre) {
	var iconUrl = '/citytrash/resources/static/img/contenedores/c_negro.png';
	switch (tipo) {
	case 'INORG':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_amarrillo_di.png';
		console.log("INORG");
		break;
	case 'ORGAN':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_marron.png';
		console.log("ORGAN");
		break;
	case 'GLASS':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_verde.png';
		console.log("GLASS");
		break;
	case 'PAPER':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_azul_claro.png';
		console.log("PAPER");
		break;
	case 'PLAST':
		iconUrl = '/citytrash/resources/static/img/contenedores/c_rojo.png';
		console.log("PLAST");
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

	console.log("tipo =>" + tipo);
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
	var latLngs = [ marker.getLatLng() ];
	var markerBounds = L.latLngBounds(latLngs);
	map.fitBounds(markerBounds);
}