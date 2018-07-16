
//https://leafletjs.com/examples/geojson/
//https://savaslabs.com/2015/05/18/mapping-geojson.html
//https://www.atomicsmash.co.uk/blog/build-interactive-map-leaflet-js/


var shownLayer,
	polygon;

var map = L.map('mapa', {
	center : [ 41.3818, 2.1685 ],
	minZoom : 0,
	zoom : 10
})

L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	attribution : '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
	subdomains : [ 'a', 'b', 'c' ],
	maxZoom : 20,
	minZoom : 5
}).addTo(map)


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
	//$('#direccion').val(data.results[i].text);
	//$('#lat').val(data.results[i].latlng.lat);
	//$('#lng').val(data.results[i].latlng.lng);
	//var id = $('#id').val();
	//var nombre = $('#nombre').val();
	//var modeloId = $("#modelo").val();
	//console.log("nombre =>" + nombre)
	//console.log("modeloId =>" + modeloId)
	//console.log("id =>" + id)
	//results.addLayer(L.marker(data.results[i].latlng));
	//results.addLayer(getMarket(data.results[i].latlng.lat, data.results[i].latlng.lng, modeloId, id, nombre))
	//results.addLayer(getMarket(data.results[i].latlng, modeloId, nombre))
	}
});

var markerClusters = L.markerClusterGroup({
	maxClusterRadius : 120,
	spiderfyOnMaxZoom : false,
	showCoverageOnHover : false,
	zoomToBoundsOnClick : false
});


//Load the GeoJSON
$.ajax({
	url : "geojson",
	dataType : "json",
	success : function(data) {

		markerClusters.clearLayers();
		$.each(data.features, function(i, feature) {
			console.log("PASO tipo =>" + feature.properties.tipo);
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

			var marker = L.marker(latlng, {
				icon : ICONO
			});

			marker.bindPopup(popupHtml(feature));
			markerClusters.addLayer(marker);

			/*L.marker(latlng, {
				icon : ICONO
			}).bindPopup(popupHtml(feature))
				.addTo(map);*/


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

		map.addLayer(markerClusters);

	/*L.geoJson(data, {
		onEachFeature : onEachFeature,
		pointToLayer : function(feature, latlng) {
			//Returns L.Marker object
			console.log("pasa por aqui pointToLayer tipo =>" + feature.properties.tipo);
			console.log("pasa por aqui pointToLayer latlng =>" + latlng);
			switch (feature.properties.tipo) {
			case 'GLASS':
				console.log("GLASS =>" + feature.properties.tipo);
				return L.marker(latlng, {
					icon : GLASS
				});
			case 'INORG':
				console.log("INORG =>" + feature.properties.tipo);
				return L.marker(latlng, {
					icon : INORG
				});
			case 'ORGAN':
				console.log("ORGAN =>" + feature.properties.tipo);
				return L.marker(latlng, {
					icon : ORGAN
				});
			case 'PAPER':
				console.log("PAPER =>" + feature.properties.tipo);
				return L.marker(latlng, {
					icon : PAPER
				});
			case 'PLAST':
				console.log("PLAST =>" + feature.properties.tipo);
				return L.marker(latlng, {
					icon : PLAST
				});
			default:
				console.log("NONE =>" + feature.properties.tipo);
				return L.marker(latlng, {
					icon : NONE
				});
			}
		}
	}).addTo(map);*/
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




