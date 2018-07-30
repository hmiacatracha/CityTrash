var route = [];
var inicio = L.latLng(41.4046070299688, 2.14336386680975);
var fin = L.latLng(41.42726412919911, 2.1656798458136564);
var shownLayer,
	polygon;
var startBtn;
var destBtn;


var tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom : 20,
		minZoom : 5,
		attribution : '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
	}),
	centerLatlng = L.latLng(41.3818, 2.1685);

var map = L.map('mapa', {
	center : centerLatlng,
	zoom : 10,
	layers : [ tiles ]
});


var markerClusters = L.markerClusterGroup({
	maxClusterRadius : 120,
	spiderfyOnMaxZoom : false,
	showCoverageOnHover : false,
	zoomToBoundsOnClick : false
});


function popupHtml(feature) {
	var a = '<a href="' + feature.properties.id + '/detalles" target="_blank">' + feature.properties.nombre + '</a>'
	var html = a + '<br> TIPO: ' + feature.properties.tipo + '<br> CARGA NOMINAL: '
		+ feature.properties.carga_nominal + ' kg';
	return html;
}

function button(label, container) {
	var btn = L.DomUtil.create('button', '', container);
	btn.setAttribute('type', 'button');
	btn.innerHTML = label;
	return btn;
}
function removePolygon() {
	if (shownLayer) {
		shownLayer.setOpacity(1);
		shownLayer = null;
	}
	if (polygon) {
		map.removeLayer(polygon);
		polygon = null;
	}
}



function cargarContenedoresRutaDiariaForm() {
	console.log("cargar contenedores");
	markerClusters.clearLayers();
	var rutaDiariaId = $('#id').val();
	markerClusters.clearLayers();

	/*Load contenedores */
	$.ajax({
		type : "GET",
		url : '/citytrash/rutasDiarias/' + rutaDiariaId + '/contenedores/geojson',
		traditional : true,
		dataType : "json",
		success : mostrarRuta,
		error : function(xhr, error) {}
	});
}

function mostrarRuta(data) {
	route = [];
	try {
		var puntoFinalLatitud = $('#puntoInicioLat');
		var puntoFinalLongitud = $('#puntoInicioLng');
		var puntoInicioLatitud = $("#puntoFinalLat");
		var puntoInicioLongitud = $("#puntoFinalLng");

		inicio = L.latLng(puntoInicioLatitud, puntoInicioLongitud);
		fin = L.latLng(puntoFinalLatitud, puntoFinalLongitud);

	} catch (err) {
		inicio = L.latLng(41.4046070299688, 2.14336386680975);
		fin = L.latLng(41.4046070299688, 2.14336386680975);
	}

	route.push(inicio);

	$.each(data.features, function(i, feature) {
		var id = feature.properties.id
		var latlng = feature.geometry.coordinates;
		var iconUrl = '/citytrash/resources/static/img/contenedores/c_negro.png';
		switch (feature.properties.tipo) {
		case 'INORG':
			iconUrl = '/citytrash/resources/static/img/contenedores/c_amarrillo_di.png';
			//console.log("INORG");
			break;
		case 'ORGAN':
			iconUrl = '/citytrash/resources/static/img/contenedores/c_marron.png';
			//console.log("ORGAN");
			break;
		case 'GLASS':
			iconUrl = '/citytrash/resources/static/img/contenedores/c_verde.png';
			//console.log("GLASS");
			break;
		case 'PAPER':
			iconUrl = '/citytrash/resources/static/img/contenedores/c_azul_claro.png';
			//console.log("PAPER");
			break;
		case 'PLAST':
			iconUrl = '/citytrash/resources/static/img/contenedores/c_rojo.png';
			//console.log("PLAST");
			break;
		default:
			iconUrl = '/citytrash/resources/static/img/contenedores/c_negro.png';
		}

		var ICONO = L.icon({
			iconUrl : iconUrl,
			iconSize : [ 15, 30 ], // size of the icon
			iconAnchor : [ 9, 20 ], // point of the icon which will correspond to marker's location
			popupAnchor : [ -3, -76 ] // point from which the popup should open relative to the iconAnchor
		});


		var marker = L.marker(latlng, {
			icon : ICONO
		});

		route.push(L.latLng(latlng));
		marker.bindPopup(popupHtml(feature));
		markerClusters.addLayer(marker);
	});

	//add the last point (the end)
	route.push(fin);
	console.log("route =>" + route);

	markerClusters.on('clustermouseover', function(a) {
		removePolygon();
		a.layer.setOpacity(0.2);
		shownLayer = a.layer;
		polygon = L.polygon(a.layer.getConvexHull());
		map.addLayer(polygon);
	});

	markerClusters.on('clustermouseout', removePolygon);
	map.on('zoomend', removePolygon);

	markerClusters.on('clusterclick', function(a) {
		var group = a.layer.getAllChildMarkers();
		group.forEach(function(m) {
			markerClusters.zoomToShowLayer(m, function() {
				m.openPopup();
			});
		});
	});

	map.addLayer(markerClusters);
	var router = L.Routing.control(L.extend(window.lrmConfig, {
		waypoints : route,
		language : 'sp',
		geocoder : L.Control.Geocoder.nominatim(),
		routeWhileDragging : true,
		reverseWaypoints : false,
		showAlternatives : true,
		altLineOptions : {
			styles : [
				{
					color : 'black',
					opacity : 0.15,
					weight : 9
				},
				{
					color : 'white',
					opacity : 0.8,
					weight : 6
				},
				{
					color : 'blue',
					opacity : 0.5,
					weight : 2
				}
			]
		},
		router : L.Routing.mapbox('pk.eyJ1IjoiY2l0eXRyYXNoIiwiYSI6ImNqamJxMWw3aTF1b3ozcHRlNmFwYm44aHoifQ.UPfOSfcTbSPCY-aLUBnfGQ')
	})).addTo(map);

	L.Routing.errorControl(router).addTo(map);

}


function cargarDireccionDeInicioRutaDiariaForm() {
	try {
		var puntoInicioDir = $('#puntoInicioDir');
		var puntoInicioLatitud = $("#puntoInicio").data("lat")
		var puntoInicioLongitud = $("#puntoInicio").data("lng")

		var pointInicio = {
			lat : puntoInicioLatitud,
			lng : puntoInicioLongitud
		};

		L.esri.Geocoding.reverseGeocode().latlng(pointInicio).run(function(error, result, response) {
			var direccion = result.address.LongLabel;
			puntoInicioDir.text("" + direccion);
		});

	} catch (err) {
		//console.log("error reverse geocode");
		puntoInicioDir.text("");
	}
}

function cargarDireccionFinalRutaDiariaForm() {
	try {
		var puntoFinalDir = $('#puntoFinalDir');
		var puntoFinalLatitud = $('#puntoFinal').data("lat")
		var puntoFinalLongitud = $('#puntoFinal').data("lng")
		
		//console.log("puntoFinalLatitud =>" + puntoFinalLatitud);
		//console.log("puntoFinalLongitud =>" + puntoFinalLongitud);

		var pointFinal = {
			lat : puntoFinalLatitud,
			lng : puntoFinalLongitud
		};

		L.esri.Geocoding.reverseGeocode().latlng(pointFinal).run(function(error, result, response) {
			var direccion = result.address.LongLabel;
			//console.log("dir 2 =>" + direccion);
			puntoFinalDir.text("" + direccion);
		});
	} catch (err) {
		//console.log("error reverse geocode");
		puntoFinalDir.text("");
	}
}

function cargarRoutingForm() {
	try {

		cargarDireccionDeInicioRutaDiariaForm();
		cargarDireccionFinalRutaDiariaForm();
		cargarContenedoresRutaDiariaForm();

	} catch (err) {
		console.log("error initialize");
	}
}

$(document).ready(function() {
	try {
		
		if (typeof document.forms[0] !== 'undefined')
			cargarRoutingForm();
		
	} catch (err) {
		console.log("error initialize");
	}
});