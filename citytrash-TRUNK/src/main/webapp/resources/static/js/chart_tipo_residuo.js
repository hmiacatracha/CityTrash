var canvas = document.getElementById("contenedor-canvas");
var progress = document.getElementById('animationProgress');
var lbl_titulo = document.getElementById('titulo').innerHTML;
var progress = document.getElementById('animationProgress');

Chart.defaults.global.defaultFontFamily = "Lato";
Chart.defaults.global.defaultFontSize = 18;

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

function estadisticas_pronostico() {
	var url = window.location.origin + '/citytrash/chart/reciclado/json';
	var form = $('#estadisticasForm').serializeArray();
	console.log("form =>" + form);
	/*Load modelos de contenedores */
	$.ajax({
		type : "GET",
		url : url,
		data : form,
		success : function(data, status) {
			console.log(" data => " + JSON.stringify(data));
			console.log("bien");
			pintarGrafica(data);
		}
	});
}

function pintarGrafica(result) {
	console.log("pintar grafica ");
	var datasets = [],
		datasetsAnterior = [],
		datasetsActual = [],
		labels = [],
		colors = [],
		borderColor = [];
	$.each(result, function(i, valor) {
		console.log("paso2 => for echa value");
		datasetsAnterior.push(1);
		datasetsActual.push(valor.actual);
		labels.push(valor.tipoDeBasura.tipo);
		colors.push('#' + valor.tipoDeBasura.color.toString());
		borderColor.push('#' + valor.tipoDeBasura.color.toString());
	});

	//backgroundColor : colors,
	//borderColor : borderColor, 

	var dataValoresAnteriores = {
		label : 'Anteriores',
		data : datasetsAnterior,
		backgroundColor : "rgba(220,220,220,0.5)",
		borderColor : "rgba(220,220,220,1)",
		borderWidth : 1
	};
	
	var dataValoresActuales = {
		label : 'Actual',
		data : datasetsActual,
		backgroundColor : "rgba(151,187,205,0.5)",
		borderColor : "rgba(151,187,205,1)",
		borderWidth : 1
	};
	
	datasets.push(dataValoresAnteriores);
	datasets.push(dataValoresActuales);
	var recicladoDatos = {
		labels : labels,
		datasets : datasets
	};



	var options = {
		title : {
			display : true,
			position : "top",
			text : lbl_titulo,
			fontSize : 18,
			fontColor : "#111"
		},
		legend : {
			display : true,
		},
		scales : {
			yAxes : [ {
				ticks : {
					min : 0
				}
			} ]
		},
		animation : {
			duration : 2000,
			onProgress : function(animation) {
				progress.value = animation.currentStep / animation.numSteps;
			},
			onComplete : function() {
				window.setTimeout(function() {
					progress.value = 0;
				}, 2000);
			}
		}
	};

	console.log("paso5 => for echa value");
	var chart = new Chart(canvas, {
		type : "bar",
		data : recicladoDatos,
		options : options
	});

	console.log("paso5 => for echa value");




}

function downloadPDF() {
	try {
		var canvasImg = canvas.toDataURL("image/jpeg", 1.0);
		var doc = new jsPDF('landscape');
		doc.setFontSize(20);
		doc.text(15, 15, "Chart");
		doc.addImage(canvasImg, 'JPEG', 10, 10, 280, 150);
		var date = new Date();
		var current_time = date.toLocaleTimeString();
		doc.save(lbl_titulo.trim() + " " + current_time);
	} catch (err) {
		//console.log("error dowload pdf")
	}
}

$(function() {
	console.log("estadisticas tipo de residuo");
	estadisticas_pronostico();

	window.setInterval(function() {
		console.log("cada 20 segundos");
		estadisticas_pronostico();
	}, 20000);
});

$(document).on('change', '#estRecicladotiposDeBasura', function(e) {
	console.log("cambia de tipo de basura");
	estadisticas_pronostico();
});

$(document).on('change', '#estRecicladotipoComparativa', function(e) {
	console.log("cambia tipo de de comparativa");
	estadisticas_pronostico();
});


$(document).on('click', '#download-pdf', function(e) {
	console.log("descargar pdf");
	downloadPDF();
});