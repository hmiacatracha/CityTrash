// See post: http://asmaloney.com/2014/01/code/creating-an-interactive-map-with-leaflet-and-openstreetmap/


var map = L.map('map', {
	center : [ 41.3818, 2.1685 ],
	minZoom : -5,
	zoom : 12
})

L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	attribution : '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
	subdomains : [ 'a', 'b', 'c' ],
	maxZoom : 20,
	minZoom : 5
}).addTo(map)


var contenedores = $.ajax({
	type : "GET",
	url : 'json',
	dataType : "json",
	success : console.log("contenedores data successfully loaded."),
	error : function(xhr) {
		//alert(xhr.statusText)
		console.log("error contenedores map=>" + xhr.statusText)
	}
})


var filterGroup = document.getElementById('filter-group');

$.when(contenedores).done(function(data) {
	console.log("contenedores done");

	var firefoxIcon = L.icon({
		iconUrl : 'http://joshuafrazier.info/images/firefox.svg',
		iconSize : [ 38, 95 ], // size of the icon
	});

	var PLAST = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_rojo.png',
		iconSize : [ 25, 35 ], // size of the icon
		iconAnchor : [ 12, 35 ], // point of the icon which will correspond to marker's location
		popupAnchor : [ -3, -76 ] // point from which the popup should open relative to the iconAnchor
	});

	var INORG = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_amarrillo_di.png',
		iconSize : [ 25, 35 ], // size of the icon
		iconAnchor : [ 12, 35 ], // point of the icon which will correspond to marker's location
		popupAnchor : [ -3, -76 ] // point from which the popup should open relative to the iconAnchor
	});

	var PAPER = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_azul_claro.png',
		iconSize : [ 25, 35 ], // size of the icon
		iconAnchor : [ 12, 35 ], // point of the icon which will correspond to marker's location
		popupAnchor : [ -3, -76 ] // point from which the popup should open relative to the iconAnchor
	});

	var GLASS = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_verde.png',
		iconSize : [ 25, 35 ], // size of the icon
		iconAnchor : [ 12, 35 ], // point of the icon which will correspond to marker's location
		popupAnchor : [ -3, -76 ] // point from which the popup should open relative to the iconAnchor
	});

	var ORGAN = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_marron.png',
		iconSize : [ 25, 35 ], // size of the icon
		iconAnchor : [ 12, 35 ], // point of the icon which will correspond to marker's location
		popupAnchor : [ -3, -76 ] // point from which the popup should open relative to the iconAnchor
	});

	var NONE = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_negro.png',
		iconSize : [ 25, 35 ], // size of the icon
		iconAnchor : [ 12, 35 ], // point of the icon which will correspond to marker's location
		popupAnchor : [ -3, -76 ] // point from which the popup should open relative to the iconAnchor
	});

	$.each(data, function(i, contenedor) {
		var tipo = contenedor.tipo;
		var symbol = "";

		switch (tipo) {
		case 'GLASS':
			symbol = "GLASS";
			L.marker([ contenedor.localizacion.latitude, contenedor.localizacion.longitude ], {
				icon : GLASS
			}).bindPopup('<a href="' + contenedor.id + '" target="_blank">' + contenedor.nombre + '</a>')
				.addTo(map);
			break;
		case 'INORG':
			symbol = "INORG";
			L.marker([ contenedor.localizacion.latitude, contenedor.localizacion.longitude ], {
				icon : INORG
			}).bindPopup('<a href="' + contenedor.id + '/detalles" target="_blank">' + contenedor.nombre + '</a>')
				.addTo(map);
			break;
		case 'ORGAN':
			symbol = "ORGAN";
			L.marker([ contenedor.localizacion.latitude, contenedor.localizacion.longitude ], {
				icon : ORGAN
			}).bindPopup('<a href="' + contenedor.id + '/detalles" target="_blank">' + contenedor.nombre + '</a>')
				.addTo(map);
			break;
		case 'PAPER':
			symbol = "PAPER";
			L.marker([ contenedor.localizacion.latitude, contenedor.localizacion.longitude ], {
				icon : PAPER
			}).bindPopup('<a href="' + contenedor.id + '/detalles" target="_blank">' + contenedor.nombre + '</a>')
				.addTo(map);
			break;
		case 'PLAST':
			symbol = "PLAST";
			L.marker([ contenedor.localizacion.latitude, contenedor.localizacion.longitude ], {
				icon : PLAST
			}).bindPopup('<a href="' + contenedor.id + '/detalles" target="_blank">' + contenedor.nombre + '</a>')
				.addTo(map);
			break;
		default:
			symbol = "NONE";
			L.marker([ contenedor.localizacion.latitude, contenedor.localizacion.longitude ], {
				icon : NONE
			}).bindPopup('<a href="' + contenedor.id + '/detalles" target="_blank">' + contenedor.nombre + '</a>')
				.addTo(map);
		}
		console.log("contenedor" + contenedor.localizacion.latitude);
	});
});