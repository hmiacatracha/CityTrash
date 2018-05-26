/*<![CDATA[*/

function renderGoogleMap() {
	console.log("pasa por por renderGoogleMap 1");
	var start_point = new google.maps.LatLng(41.3818, 2.1685);
	// Creating a new map
	var map = new google.maps.Map(document.getElementById("map_canvas"), {
		center : start_point,
		zoom : 5,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	});
	console.log("pasa por por renderGoogleMap 2");

	var infoWindow = new google.maps.InfoWindow();

	function setContenedorLocalizacion(map, contenedor) {
		console.log("pasa por por renderGoogleMap 2");
		var bounds = new google.maps.LatLngBounds();
		$.ajax({
			type : "GET",
			url : 'json',
			dataType : "json",
			success : function(data) {
				if (data.length !== 0) {
					console.log("pasa por por renderGoogleMap 3b " + data);
					$.each(data, function(contenedor, data) {

						console.log("pasa por por renderGoogleMap 4");
						var latLng = new google.maps.LatLng(data.localizacion.latitude,
							data.localizacion.longitude);
						console.log("pasa por por renderGoogleMap 5");
						console.log("latLng =>" + data.localizacion.latitude + "," +
							data.localizacion.longitude);
						bounds.extend(latLng);

						// Creating a contenedor and putting it on the map
						var contenedor = new google.maps.Marker({
							position : latLng,
							map : map,
							title : data.nombre
						});

						var windowContent = '<h3>' + data.id + '</h3>' + '<p>'
							+ data.tipo + '</p>';

						// Attaching a click event to the current contenedor
						infobox = new InfoBox({
							content : infoWindow.setContent(windowContent),
							alignBottom : true,
							pixelOffset : new google.maps.Size(-160, -45)
						});

						google.maps.event.addListener(contenedor, 'click',
							function() {

								// Open this map's infobox
								infobox.open(map, contenedor);
								infobox.setContent(windowContent);
								map.panTo(contenedor.getPosition());
								infobox.show();
							});
						google.maps.event.addListener(map, 'click', function() {
							infobox.setMap(null);
						});
					});

				}
			},
			error : function(data) {
				console.log('Please refresh the page and try again');
			}
		});
		// end loop through json
		map.setCenter(start_point);
		map.fitBounds(bounds);
	}
	setContenedorLocalizacion(map);
}

console.log("pasa por por renderGoogleMap");
google.maps.event.addDomListener(window, 'ready', renderGoogleMap);

/* ]]> */