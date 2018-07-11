
var startBtn;
var destBtn;
var route = [];
var inicio = L.latLng(41.4046070299688, 2.14336386680975);
var fin = L.latLng(41.42726412919911, 2.1656798458136564);




var shownLayer,
	polygon;

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


//Custom radius and icon create function
var markerClusters = L.markerClusterGroup({
	maxClusterRadius : 120,
	spiderfyOnMaxZoom : false,
	showCoverageOnHover : false,
	zoomToBoundsOnClick : false
});


var searchControl1 = L.esri.Geocoding.geosearch({
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


var searchControl2 = L.esri.Geocoding.geosearch({
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

searchControl1.on('results', function(data) {
	//results.clearLayers();
	for (var i = data.results.length - 1; i >= 0; i--) {
		console.log("result =>" + data.results[i].latlng);
		$('#puntoInicioDir').val(data.results[i].text);
		$('#puntoInicioLat').val(data.results[i].latlng.lat);
		$('#puntoInicioLng').val(data.results[i].latlng.lng);
	//results.addLayer(L.marker(data.results[i].latlng));
	//results.addLayer(getMarket(data.results[i].latlng.lat, data.results[i].latlng.lng, modeloId, id, nombre))
	//results.addLayer(getMarket(data.results[i].latlng, modeloId, nombre));
	}
});

searchControl2.on('results', function(data) {
	//results.clearLayers();
	for (var i = data.results.length - 1; i >= 0; i--) {
		console.log("result =>" + data.results[i].latlng);
		$('#puntoFinalDir').val(data.results[i].text);
		$('#puntoFinalLat').val(data.results[i].latlng.lat);
		$('#puntoFinalLng').val(data.results[i].latlng.lng);
	//results.addLayer(L.marker(data.results[i].latlng));
	//results.addLayer(getMarket(data.results[i].latlng.lat, data.results[i].latlng.lng, modeloId, id, nombre))
	//results.addLayer(getMarket(data.results[i].latlng, modeloId, nombre));
	}
});

/*var router = L.Routing.control(L.extend(window.lrmConfig, {
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
*/

function popupHtml(feature) {
	var a = '<a href="' + feature.properties.id + '/detalles" target="_blank">' + feature.properties.nombre + '</a>'
	var html = a + '<br> TIPO: ' + feature.properties.tipo + '<br> CARGA NOMINAL: '
		+ feature.properties.carga_nominal + ' kg';
	return html;
}



$('#contenedores').on('change', function() {
	cargarContenedores();
});

function cargarContenedores() {
	markerClusters.clearLayers();
	var rutaForm = $('form').serializeArray();
	var contenedoresIds = [];
	$.each($("#contenedores option:selected"), function() {
		contenedoresIds.push(parseInt($(this).val()));
	});

	/*Load contenedores */
	$.ajax({
		type : "GET",
		url : '/citytrash/rutas/contenedores/geojson',
		traditional : true,
		data : {
			contenedoresIds : contenedoresIds
		},
		dataType : "json",
		success : mostrarContenedores,
		error : function(xhr, error) {
			//console.log(" error al cargar los contenedores de la ruta =>" + error);
		}
	});
}

function mostrarContenedores(data) {
	route = [];
	route.push(inicio);

	$.each(data.features, function(i, feature) {
		//console.log("PASO tipo =>" + feature.properties.tipo);
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

		marker.bindPopup(popupHtml(feature));
		markerClusters.addLayer(marker);
		route.push(L.latLng(latlng));
	});

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

	route.push(fin);
	console.log("route fin =>" + route);


}

function cargarDireccionDeInicio() {
	try {
		var puntoInicioDir = $('#puntoInicioDir');
		var puntoInicioLatitud = $('#puntoInicioLat');
		var puntoInicioLongitud = $('#puntoInicioLng');

		var pointInicio = {
			lat : puntoInicioLatitud.val(),
			lng : puntoInicioLongitud.val()
		};

		L.esri.Geocoding.reverseGeocode().latlng(pointInicio).run(function(error, result, response) {
			var direccion = result.address.LongLabel;
			//console.log("dir 1 =>" + direccion);
			puntoInicioDir.val("" + direccion);
		});

	} catch (err) {
		//console.log("error reverse geocode");
		puntoInicioDir.val("");
	}
}

function cargarDireccionFinal() {
	try {
		var puntoFinalDir = $('#puntoFinalDir');
		var puntoFinalLatitud = $('#puntoFinalLat');
		var puntoFinalLongitud = $('#puntoFinalLng');

		var pointFinal = {
			lat : puntoFinalLatitud.val(),
			lng : puntoFinalLongitud.val()
		};

		L.esri.Geocoding.reverseGeocode().latlng(pointFinal).run(function(error, result, response) {
			var direccion = result.address.LongLabel;
			//console.log("dir 2 =>" + direccion);
			puntoFinalDir.val("" + direccion);
		});
	} catch (err) {
		//console.log("error reverse geocode");
		puntoFinalDir.val("");
	}
}


function initialize() {
	try {
		cargarDireccionDeInicio();
		cargarDireccionFinal();
		cargarContenedores();
	} catch (err) {
		console.log("error initialize");
	}
}

function tiposDeBasuraChange() {
	try {
		var rutaForm = $('form').serializeArray();
		var camion = $("#camion");
		var contenedores = $("#contenedores");
		console.log("#tiposDeBasura change rutaForm=>" + JSON.stringify(rutaForm));
		/*Load contenedores */
		$.ajax({
			type : "GET",
			url : '/citytrash/ajax/listaContenedoresDisponibles',
			data : rutaForm,
			success : function(data, status) {
				//console.log("response =>" + JSON.stringify(data));
				//console.log("#tiposDeBasura frag 2 antes=>" + contenedores.html());
				contenedores.html(data).selectpicker("refresh");
			}
		});

		/*Load camiones */
		$.ajax({
			type : "GET",
			url : '/citytrash/ajax/listaCamionesDisponibles',
			data : rutaForm,
			success : function(data, status) {
				//console.log("#tiposDeBasura frag 1 antes =>" + camion.html());
				camion.html(data).selectpicker("refresh");
			}
		});
	} catch (err) {
		console.log("error tiposDeBasuraChange");
	}
}

$(document).ready(function() {
	console.log("ready!");
	initialize();

	$("#puntoInicioLat").change(function() {
		cargarDireccionDeInicio();
	});

	$("#puntoInicioLng").change(function() {
		cargarDireccionDeInicio();
	});

	$("#puntoFinalLat").change(function() {
		cargarDireccionFinal();
	});

	$("#puntoFinalLng").change(function() {
		cargarDireccionFinal();
	});

	$('#tiposDeBasura').on('change', function() {
		tiposDeBasuraChange();
	});
});