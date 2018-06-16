package es.udc.citytrash.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloDto;
import es.udc.citytrash.controller.util.dtos.contenedor.Localizacion;
import es.udc.citytrash.controller.util.dtos.contenedor.MapaContenedoresDto;
import es.udc.citytrash.controller.util.dtos.contenedor.SensorValores;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.sensorValor.Valor;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.usuarioService.UsuarioService;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@Controller
public class PublicRestController {

	/*
	 * http://www.jtech.ua.es/j2ee/publico/spring-2012-13/sesion04-apuntes.html
	 */

	@Autowired
	TrabajadorService tservicio;

	@Autowired
	ContenedorService cServicio;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	private ModelMapper modelMapper;

	final Logger logger = LoggerFactory.getLogger(PublicRestController.class);

	@RequestMapping(value = "/contenedores/geojson", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody FeatureCollection getContenedoresGeoJson() {
		ContenedorFormBusq form = new ContenedorFormBusq();
		FeatureCollection featureCollection = new FeatureCollection();
		List<Contenedor> contenedores = cServicio.buscarContenedores(form);
		TipoDeBasura tipo;
		ContenedorModelo modelo;

		for (Contenedor contenedor : contenedores) {
			Feature feature = new Feature();
			Point geometry = new Point(contenedor.getLatitud().doubleValue(), contenedor.getLongitud().doubleValue());
			feature.setGeometry(geometry);
			feature.setProperty("id", contenedor.getId());
			feature.setProperty("nombre", contenedor.getNombre());

			try {
				modelo = cServicio.buscarModeloById(contenedor.getModelo().getId());
				tipo = cServicio.buscarTipoDeBasuraByModelo(contenedor.getModelo().getId());
				feature.setProperty("tipo", tipo.getTipo());
				feature.setProperty("color", "#" + tipo.getColor());
				feature.setProperty("capacidad_nominal", modelo.getCapacidadNominal());
				feature.setProperty("carga_nominal", modelo.getCargaNominal());
				feature.setProperty("profundidad", modelo.getProfundidad());
				feature.setProperty("peso_Vacio", modelo.getPesoVacio());
			} catch (InstanceNotFoundException e) {
				feature.setProperty("tipo", "UNKOWN");
				feature.setProperty("color", "#000000");
				feature.setProperty("capacidad_nominal", "0");
				feature.setProperty("carga_nominal", "0");
				feature.setProperty("profundidad", "0");
				feature.setProperty("peso_Vacio", "0");
			}
			featureCollection.add(feature);
		}
		return featureCollection;
	}

	@RequestMapping(value = "/contenedores/json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<MapaContenedoresDto> getContenedoresByLocalizacion() {
		ContenedorFormBusq form = new ContenedorFormBusq();
		List<Contenedor> contenedores = cServicio.buscarContenedores(form);
		return contenedores.stream().map(contenedor -> convertToDto(contenedor)).collect(Collectors.toList());
	}

	private MapaContenedoresDto convertToDto(Contenedor contenedor) {
		MapaContenedoresDto dto = new MapaContenedoresDto();

		try {
			TipoDeBasura tipo = cServicio.buscarTipoDeBasuraByModelo(contenedor.getModelo().getId());
			dto.setTipo(tipo.getTipo());
		} catch (InstanceNotFoundException e) {
			dto.setTipo("");
		}
		Localizacion localizacion = new Localizacion();
		localizacion.setLatitude(contenedor.getLatitud());
		localizacion.setLongitude(contenedor.getLongitud());
		dto.setLocalizacion(localizacion);
		dto.setNombre(contenedor.getNombre());
		dto.setId(contenedor.getId());
		return dto;
	}

	@RequestMapping(value = "/cuenta/validarEmail", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Boolean validarEmail(HttpServletResponse response,
			@RequestParam(value = "email", required = true) String email) {
		logger.info("buscando trabajador por email");
		try {
			tservicio.buscarTrabajadorByEmail(email);
			logger.info("buscando trabajador por email => si");
			logger.info("buscando trabajador por email = " + email);
			return false;
		} catch (InstanceNotFoundException e) {
			logger.info("buscando trabajador por email => no");
			return true;
		}
	}

	@RequestMapping(value = "/cuenta/validarDocumento", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Boolean validarDocumento(HttpServletResponse response,
			@RequestParam(value = "doc", required = true) String documento) {
		try {
			tservicio.buscarTrabajadorDocumento(documento);
			return false;
		} catch (InstanceNotFoundException e) {
			return true;
		}
	}

	@RequestMapping(value = "/contenedor/modelo/tipo", method = RequestMethod.GET, produces = "text/plain")
	public @ResponseBody String getTipoBasura(@RequestParam(value = "modeloId", required = true) int id) {
		try {
			return cServicio.buscarTipoDeBasuraByModelo(id).getTipo();
		} catch (InstanceNotFoundException e) {
			return "";
		}
	}

	@RequestMapping(value = "/sensor/{id}/json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<SensorValores> gets(@PathVariable("id") Long sensorId,
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date toDate)
			throws InstanceNotFoundException {
		logger.info("contenedores/sensores/id/json");
		List<Valor> valores = new ArrayList<Valor>();

		cServicio.buscarSensorById(sensorId);
		
		if (fromDate != null)
			logger.info("fromDate =>" + fromDate.toString());
		if (toDate != null)
			logger.info("toDate =>" + toDate.toString());
		
		valores = cServicio.buscarValoresBySensor(sensorId, fromDate, toDate);
		return valores.stream().map(valor -> convertToDto(valor)).collect(Collectors.toList());
	}

	private SensorValores convertToDto(Valor valor) {
		Timestamp timestamp = new Timestamp(valor.getPk().getFechaHora().getTimeInMillis());
		SensorValores sensorValor = new SensorValores(timestamp, valor.getValor(), "%");
		return sensorValor;
	}
}
