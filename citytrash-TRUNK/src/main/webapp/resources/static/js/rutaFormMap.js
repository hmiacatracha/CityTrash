
//https://leafletjs.com/examples/geojson/
//https://savaslabs.com/2015/05/18/mapping-geojson.html
//https://www.atomicsmash.co.uk/blog/build-interactive-map-leaflet-js/

var mapa = L.map('mapa', {
	center : [ 41.3818, 2.1685 ],
	minZoom : 0,
	zoom : 10
})

L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	attribution : '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
	subdomains : [ 'a', 'b', 'c' ],
	maxZoom : 20,
	minZoom : 5
}).addTo(mapa)

//Load the GeoJSON
$.ajax({
	url : "/citytrash/contenedores/geojson",
	dataType : "json",
	success : function(data) {

		$.each(data.features, function(i, feature) {
			console.log("PASO tipo =>" + feature.properties.tipo);
			var id = feature.properties.id
			
			var latlng = feature.geometry.coordinates;
			var iconUrl = '/citytrash/resources/static/img/contenedores/c_negro.png';
			switch (feature.properties.tipo) {
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
				iconSize : [ 15, 30 ], // size of the icon
				iconAnchor : [ 9, 20 ], // point of the icon which will correspond to marker's location
				popupAnchor : [ -3, -76 ] // point from which the popup should open relative to the iconAnchor
			});
			L.marker(latlng, {
				icon : ICONO
			}).bindPopup(popupHtml(feature))
				.addTo(mapa);
		});
	},
	error : function(xhr) {
		//alert(xhr.statusText)
		console.log("error contenedores map=>" + xhr.statusText)
	}
});

function popupHtml(feature) {
	var a = '<a href="' + feature.properties.id + '/detalles" target="_blank">' + feature.properties.nombre + '</a>'
	var html = a + '<br> TIPO: ' + feature.properties.tipo + '<br> CARGA NOMINAL: '
		+ feature.properties.carga_nominal + ' kg';
	return html;
}