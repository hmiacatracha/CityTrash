//https://leafletjs.com/examples/geojson/
//https://savaslabs.com/2015/05/18/mapping-geojson.html
//https://www.atomicsmash.co.uk/blog/build-interactive-map-leaflet-js/


/***************************************************************************************************
 * **
 * *			inicio
 * *
 * *************************************************************************************************/
var shownLayer,
	polygon;


var map = L.map('mapaContenedores', {
	center : [ 41.3818, 2.1685 ],
	minZoom : 0,
	zoom : 10
})

var basemap = L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	attribution : '&copy; <a hrefL.marker="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
	subdomains : [ 'a', 'b', 'c' ],
	maxZoom : 20,
	minZoom : 5
}).addTo(map)

/*******************search control*****************************/
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

/*******************MARKET CLUSTER*****************************/
var mcgLayerSupportGroup = L.markerClusterGroup(),
	myLayerGroup = L.layerGroup();

/******************************TAG FILTER******************************************/

/*********************************** JAX CALL ***********************************************/
var contenedores = $.ajax({
	url : "geojson",
	dataType : "json",
	success : console.log("Contenedores data successfully loaded."),
	error : function(xhr) {
		alert(xhr.statusText)
	}
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

$.when(contenedores).done(function() {
	console.log("contenedores done");
	//console.log("contenedores =>" + JSON.stringify(contenedores));
	console.log("contenedores =>" + contenedores.responseJSON);

	// Add requested external GeoJSON to map
	console.log("geoJSON antes =>" + contenedores.responseJSON);
	var plasticBins = L.geoJSON(contenedores.responseJSON, myLayerPlaticOptions).addTo(map);
	var glassBins = L.geoJSON(contenedores.responseJSON, myLayerGlassOptions).addTo(map);
	var organicBins = L.geoJSON(contenedores.responseJSON, myLayerOrganicOptions).addTo(map);
	var inorganicBins = L.geoJSON(contenedores.responseJSON, myLayerInorganicOptions).addTo(map);
	var paperBins = L.geoJSON(contenedores.responseJSON, myLayerPaperOptions).addTo(map);

	console.log("contenedores después 2");
	var tipoBasuraFiltro = L.control.tagFilterButton({
		data : [ 'plastic', 'glass', 'organic', 'inorganic', 'paper' ],
		filterOnEveryClick : true,
	}).addTo(map);

	/*mcgLayerSupportGroup.addLayer(myLayerGroup);
	mcgLayerSupportGroup.addTo(map);
	mcgLayerSupportGroup.checkIn(myLayerGroup); // <= this is where the magic happens!
	myLayerGroup.addTo(map);
	console.log("contenedores después 3");
	*/
	mcgLayerSupportGroup.addTo(map);

});


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