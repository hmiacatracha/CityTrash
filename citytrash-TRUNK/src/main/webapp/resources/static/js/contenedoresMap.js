
var osmUrl = 'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
	osmAttrib = '&copy; <a href="http://openstreetmap.org/copyright">OpenStreetMap</a> contributors',
	osm = L.tileLayer(osmUrl, {
		maxZoom : 18,
		attribution : osmAttrib,
	});

var tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom : 18,
		attribution : '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Points &copy 2012 LINZ'
	}),
	latlng = L.latLng(-42.82, 175.24);


var map = L.map('mapaContenedores')
	.setView([ 41.3818, 2.1685 ], 12)
	.addLayer(osm);

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
	//results.clearLayers();
	for (var i = data.results.length - 1; i >= 0; i--) {
		console.log("result =>" + data.results[i].latlng);
	}
});


var contenedores = $.ajax({
	url : "geojson",
	dataType : "json",
	success : console.log("Contenedores data successfully loaded."),
	error : function(xhr) {
		alert(xhr.statusText)
	}
});

var mcgLayerSupportGroup = L.markerClusterGroup.layerSupport(),
	group1 = L.layerGroup(),
	group2 = L.layerGroup(),
	group3 = L.layerGroup(),
	group4 = L.layerGroup(),
	group5 = L.layerGroup(),
	control = L.control.layers(null, null, {
		collapsed : true
	});


$.when(contenedores).done(function() {
	console.log("contenedores done");

	var plasticBins = L.geoJSON(contenedores.responseJSON, myLayerPlaticOptions).addTo(group1);
	var glassBins = L.geoJSON(contenedores.responseJSON, myLayerGlassOptions).addTo(group2);
	var organicBins = L.geoJSON(contenedores.responseJSON, myLayerOrganicOptions).addTo(group3);
	var inorganicBins = L.geoJSON(contenedores.responseJSON, myLayerInorganicOptions).addTo(group4);
	var paperBins = L.geoJSON(contenedores.responseJSON, myLayerPaperOptions).addTo(group5);

	mcgLayerSupportGroup.addTo(map);
	mcgLayerSupportGroup.checkIn([ group1, group2, group3, group4, group5 ]);

	control.addOverlay(group1, 'Plástico');
	control.addOverlay(group2, 'Vidrio');
	control.addOverlay(group3, 'Orgánica');
	control.addOverlay(group4, 'Inorganica');
	control.addOverlay(group5, 'Papel');
	control.addTo(map);
	
	group1.addTo(map); // Adding to map or to AutoMCG are now equivalent.
	group2.addTo(map);
	group3.addTo(map);
	group4.addTo(map);
	group5.addTo(map);
	map.fitBounds(mcgLayerSupportGroup.getBounds());

});


/*********************************** ICONS MARKET ***********************************************/
function paperIcon(feature, latlng) {
	let myIcon = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_azul_claro.png',
		iconSize : [ 15, 30 ], // size of the icon
		iconAnchor : [ 9, 20 ], // point of the icon which will correspond to marker's location	    	   	 
		popupAnchor : [ 0, 0 ] // point from which the popup should open relative to the iconAnchor
	})
	return L.marker(latlng, {
		icon : myIcon
	}).bindPopup(popupHtml(feature));
}

function plasticIcon(feature, latlng) {
	let myIcon = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_rojo.png',
		iconSize : [ 15, 30 ], // size of the icon
		iconAnchor : [ 9, 20 ], // point of the icon which will correspond to marker's location	    	   	 
		popupAnchor : [ 0, 0 ] // point from which the popup should open relative to the iconAnchor
	})
	return L.marker(latlng, {
		icon : myIcon
	}).bindPopup(popupHtml(feature));
}
;

function inorganicIcon(feature, latlng) {
	let myIcon = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_amarrillo_di.png',
		iconSize : [ 15, 30 ], // size of the icon
		iconAnchor : [ 9, 20 ], // point of the icon which will correspond to marker's location	    	   	 
		popupAnchor : [ 0, 0 ] // point from which the popup should open relative to the iconAnchor
	})
	return L.marker(latlng, {
		icon : myIcon
	}).bindPopup(popupHtml(feature));
}
;


function glassIcon(feature, latlng) {
	let myIcon = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_verde.png',
		iconSize : [ 15, 30 ], // size of the icon
		iconAnchor : [ 9, 20 ], // point of the icon which will correspond to marker's location	    	   	 
		popupAnchor : [ 0, 0 ] // point from which the popup should open relative to the iconAnchor
	})
	return L.marker(latlng, {
		icon : myIcon
	}).bindPopup(popupHtml(feature));
}
;



function organicIcon(feature, latlng) {
	let myIcon = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_marron.png',
		iconSize : [ 15, 30 ], // size of the icon
		iconAnchor : [ 9, 20 ], // point of the icon which will correspond to marker's location	    	   	 
		popupAnchor : [ 0, 0 ] // point from which the popup should open relative to the iconAnchor
	})
	return L.marker(latlng, {
		icon : myIcon
	}).bindPopup(popupHtml(feature));
}
;


function noneIcon(feature, latlng) {
	let myIcon = L.icon({
		iconUrl : '/citytrash/resources/static/img/contenedores/c_negro.png',
		iconSize : [ 15, 30 ], // size of the icon
		iconAnchor : [ 9, 20 ], // point of the icon which will correspond to marker's location	    	   	 
		popupAnchor : [ 0, 0 ] // point from which the popup should open relative to the iconAnchor
	})
	return L.marker(latlng, {
		icon : myIcon
	}).bindPopup(popupHtml(feature));
}
;

/*********************************** LAYER OPTIONS ***********************************************/
let myLayerPlaticOptions = {
	pointToLayer : plasticIcon,
	filter : plasticFilter,
	tags : [ 'plastic' ]
};


//create an options object that specifies which function will called on each feature
let myLayerGlassOptions = {
	pointToLayer : glassIcon,
	filter : glassFilter,
	tags : [ 'glass' ]
};


let myLayerOrganicOptions = {
	pointToLayer : organicIcon,
	filter : organicFilter,
	tags : [ 'organic' ]
};

let myLayerInorganicOptions = {
	pointToLayer : inorganicIcon,
	filter : inorganicFilter,
	tags : [ 'inorganic' ]
};

let myLayerPaperOptions = {
	pointToLayer : paperIcon,
	filter : paperFilter,
	tags : [ 'paper' ]
};


function plasticFilter(feature) {
	if (feature.properties.tipo === "PLAST") return true
}

function glassFilter(feature) {
	if (feature.properties.tipo === "GLASS") return true
}

function organicFilter(feature) {
	if (feature.properties.tipo === "ORGAN") return true
}

function inorganicFilter(feature) {
	if (feature.properties.tipo === "INORG") return true
}

function paperFilter(feature) {
	if (feature.properties.tipo === "PAPER") return true
}

function popupHtml(feature) {
	var a = '<a href="' + feature.properties.id + '/detalles" target="_blank">' + feature.properties.nombre + '</a>'
	var html = a + '<br> TIPO: ' + feature.properties.tipo + '<br> CARGA NOMINAL: '
		+ feature.properties.carga_nominal + ' kg';
	return html;
}