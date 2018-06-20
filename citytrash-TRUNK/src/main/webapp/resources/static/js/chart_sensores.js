
var startDateGlobal;
var endDate;

window.chartColors = {
	red : 'rgb(255, 99, 132)',
	orange : 'rgb(255, 159, 64)',
	yellow : 'rgb(255, 205, 86)',
	green : 'rgb(0,128,0)',
	green2 : 'rgb(75, 192, 192)',
	blue : 'rgb(54, 162, 235)',
	purple : 'rgb(153, 102, 255)',
	grey : 'rgb(231,233,237)'
};

function dibujarGrafica(startDate, endDate, contenedor, modeloId, tipo, progress, etiqueta1, etiqueta2) {
	console.log("buscarValoresConenedor function");

	startDateGlobal = startDate;
	endDateGlobal = endDate;

	Date.prototype.formatMMDDYYYY = function() {
		return (this.getMonth() + 1) +
			"/" + this.getDate() +
			"/" + this.getFullYear() + " " + this.getHours() + ":" + this.getMinutes();
	}

	var formData = {
		fromDate : startDate.format('DD/MM/YYYY'),
		toDate : endDate.format('DD/MM/YYYY')
	}

	var url = window.location.origin + '/citytrash/sensor/' + modeloId + '/json' + "?fromDate=" +
	startDate.format('DD/MM/YYYY') + "&" + "toDate=" + endDate.format('DD/MM/YYYY');

	console.log("data =>" + JSON.stringify(formData));
	console.log("url" + url);
	$("#postResultDiv").html("");

	$.ajax({
		type : "GET",
		url : url,
		//data : JSON.stringify(formData),
		contentType : "application/json",
		dataType : 'json',
		success : function(result) {
			console.log("paso1");
			var labels = [],
				data = [];
			var unidad = "";
			$.each(result, function(i, valor) {
				console.log("paso2 =>" + valor.fechaHora);
				labels.push(valor.fechaHora)
				//labels.push(new Date(valor.fechaHora).formatMMDDYYYY());
				data.push(parseFloat(valor.valor));
				unidad = valor.unidad;
			});

			console.log("paso3");
			var tempData = {
				labels : labels,
				animationEnabled : true,
				animationDuration : 1000,
				datasets : [ {
					backgroundColor : window.chartColors.green,
					borderColor : window.chartColors.green,
					label : etiqueta1,
					fill : false,
					data : data
				} ]
			};
			console.log("paso4");
			var chart = new Chart(document.getElementById("contenedor-canvas").getContext("2d"), {
				type : 'line',
				data : tempData,
				options : {
					responsive : true,
					title : {
						display : true,
						text : contenedor
					},
					animation : {
						duration : 2000,
						onProgress : function(animation) {
							progress.value = animation.currentStep / animation.numSteps;
						},
						onComplete : function() {
							window.setTimeout(function() {
								progress.value = 0;
							//progress.hide();
							}, 2000);
						}
					},
					scales : {
						xAxes : [ {
							display : true,
							scaleLabel : {
								display : true
							}
						} ],
						yAxes : [ {
							display : true,
							scaleLabel : {
								display : true,
								labelString : unidad + " " + tipo
							}
						} ]
					}
				},
			});
		},
		error : function(e) {
			//alert("Error!")
			console.log("ERROR: ", e);
			$("#postResultDiv").html("<strong>Error</strong>");
		}
	});
}

function downloadPDF() {
	try {
		var canvas = document.querySelector('#contenedor-canvas');
		//creates image
		var canvasImg = canvas.toDataURL("image/jpeg", 1.0);
		//creates PDF from img
		var doc = new jsPDF('landscape');
		doc.setFontSize(20);
		doc.text(15, 15, "Chart");
		doc.addImage(canvasImg, 'JPEG', 10, 10, 280, 150);
		var modeloId = Number(document.getElementById('sensorId').innerHTML);
		var tipo = document.getElementById('tipo').innerHTML;
		doc.save('canvas.pdf');
	} catch (err) {
		//console.log("error dowload pdf")
	}
}

function cb(start, end) {
	console.log("cb function inicio");
	$('#reportrange span').html(start.format('DD MMM YYYY') + ' - ' + end.format('DD MMM YYYY'));
	//buscarValoresConenedor(start, end);
	var modeloId = Number(document.getElementById('sensorId').innerHTML);

	var tipo = document.getElementById('tipo').innerHTML;
	var contenedor = document.getElementById('contenedor').innerHTML;
	var lbl_pronostico = document.getElementById('lbl_pronostico').innerHTML;
	var lbl_tiempo_real = document.getElementById('lbl_tiempo_real').innerHTML;

	var progress = document.getElementById('animationProgress');
	console.log("antes");
	dibujarGrafica(start, end, contenedor, modeloId, tipo, progress, lbl_tiempo_real, lbl_pronostico);
	console.log("despues");

	console.log("cb function fin");

}

$(function() {

	//dowload listener
	document.getElementById('download-pdf').addEventListener("click", downloadPDF);

	/*http://tamble.github.io/jquery-ui-daterangepicker/*/
	var start = moment().subtract(1, 'days');
	var end = moment();

	$('#reportrange').daterangepicker({
		startDate : start,
		endDate : end,
		maxDate : moment(),
		//timePicker: true,
		ranges : {
			'Yesterday' : [ moment().subtract(1, 'days'), moment().subtract(1, 'days') ],
			'Today' : [ moment(), moment() ],
			'Last 7 Days' : [ moment().subtract(6, 'days'), moment() ],
			'Last 30 Days' : [ moment().subtract(29, 'days'), moment() ],
			'This Month' : [ moment().startOf('month'), moment().endOf('month') ],
			'Last Month' : [ moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month') ]
		}
	}, cb);
	cb(start, end);

	window.setInterval(function() {
		/// call your function here
		console.log("cada 20 segundos");
		// startDate = startDate;
		// endDate = endDate;
		cb(startDateGlobal, endDateGlobal);
	}, 20000);
});