package es.udc.citytrash.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.udc.citytrash.controller.excepciones.ResourceNotFoundException;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.Localizacion;
import es.udc.citytrash.controller.util.dtos.contenedor.MapaContenedoresDto;
import es.udc.citytrash.controller.util.dtos.contenedor.SensorValores;
import es.udc.citytrash.controller.util.dtos.estadisticas.ComparativaPorTipoReciclado;
import es.udc.citytrash.controller.util.dtos.estadisticas.EstadisticasPorRecicladoForm;
import es.udc.citytrash.model.camionService.CamionService;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.rutaService.RutaService;
import es.udc.citytrash.model.sensorValor.Valor;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.util.enums.TipoComparativa;

@Controller
public class RestController {

	/*
	 * http://www.jtech.ua.es/j2ee/publico/spring-2012-13/sesion04-apuntes.html
	 */

	@Autowired
	TrabajadorService tservicio;

	@Autowired
	ContenedorService contServicio;

	@Autowired
	CamionService camServicio;

	@Autowired
	RutaService rutaServicio;

	@Autowired
	private ModelMapper modelMapper;

	final Logger logger = LoggerFactory.getLogger(RestController.class);

	@RequestMapping(value = "/contenedores/geojson", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody FeatureCollection getContenedoresGeoJson() {
		ContenedorFormBusq form = new ContenedorFormBusq();
		FeatureCollection featureCollection = new FeatureCollection();

		List<Contenedor> contenedores = contServicio.buscarContenedores(form);
		TipoDeBasura tipo;
		ContenedorModelo modelo;

		for (Contenedor contenedor : contenedores) {
			Feature feature = new Feature();

			Point geometry = new Point(contenedor.getLongitud().doubleValue(), contenedor.getLatitud().doubleValue());

			feature.setGeometry(geometry);
			feature.setProperty("id", contenedor.getId());
			feature.setProperty("nombre", contenedor.getNombre());

			try {
				modelo = contServicio.buscarModeloById(contenedor.getModelo().getId());
				tipo = contServicio.buscarModeloById(contenedor.getModelo().getId()).getTipo();
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

	@RequestMapping(value = "/rutas/{id}/contenedores/geojson", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody FeatureCollection getContenedoresDeRutaGeoJson(@PathVariable("id") Integer rutaId)
			throws ResourceNotFoundException {
		FeatureCollection featureCollection = new FeatureCollection();
		List<Contenedor> contenedores;
		try {
			contenedores = rutaServicio.buscarRuta(rutaId).getContenedores();

			logger.info("get contenedores ruta geoson =>" + contenedores.toString());
			TipoDeBasura tipo;
			ContenedorModelo modelo;

			for (Contenedor contenedor : contenedores) {
				Feature feature = new Feature();
				Point geometry = new Point(contenedor.getLatitud().doubleValue(),
						contenedor.getLongitud().doubleValue());
				feature.setGeometry(geometry);
				feature.setProperty("id", contenedor.getId());
				feature.setProperty("nombre", contenedor.getNombre());

				try {
					modelo = contServicio.buscarModeloById(contenedor.getModelo().getId());
					tipo = modelo.getTipo();
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

		} catch (InstanceNotFoundException e1) {
			throw new ResourceNotFoundException(rutaId);
		}
	}

	@RequestMapping(value = "/rutasDiarias/{id}/contenedores/geojson", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody FeatureCollection getContenedoresDeRutaDiariaGeoJson(@PathVariable("id") long rutaDiariaId)
			throws ResourceNotFoundException {
		FeatureCollection featureCollection = new FeatureCollection();
		List<Contenedor> contenedores;

		try {
			rutaServicio.buscarRutaDiaria(rutaDiariaId);

			contenedores = rutaServicio.buscarContenedoresDeRutaDiaria(rutaDiariaId);
			logger.info("get contenedores ruta geoson =>" + contenedores.toString());
			TipoDeBasura tipo;
			ContenedorModelo modelo;

			for (Contenedor contenedor : contenedores) {
				Feature feature = new Feature();
				Point geometry = new Point(contenedor.getLatitud().doubleValue(),
						contenedor.getLongitud().doubleValue());
				feature.setGeometry(geometry);
				feature.setProperty("id", contenedor.getId());
				feature.setProperty("nombre", contenedor.getNombre());

				try {
					modelo = contServicio.buscarModeloById(contenedor.getModelo().getId());
					tipo = modelo.getTipo();
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

		} catch (InstanceNotFoundException e1) {
			throw new ResourceNotFoundException(rutaDiariaId);
		}
	}

	@RequestMapping(value = "/rutas/contenedores/geojson", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody FeatureCollection getContenedoresGeoJson(
			@RequestParam(value = "contenedoresIds", required = false) List<Long> contenedoresIds) {
		FeatureCollection featureCollection = new FeatureCollection();
		List<Contenedor> contenedores = contServicio.buscarContenedores(contenedoresIds);
		logger.info("get contenedores ruta geoson =>" + contenedores.toString());
		TipoDeBasura tipo;
		ContenedorModelo modelo;

		for (Contenedor contenedor : contenedores) {
			Feature feature = new Feature();
			Point geometry = new Point(contenedor.getLatitud().doubleValue(), contenedor.getLongitud().doubleValue());
			feature.setGeometry(geometry);
			feature.setProperty("id", contenedor.getId());
			feature.setProperty("nombre", contenedor.getNombre());

			try {
				modelo = contServicio.buscarModeloById(contenedor.getModelo().getId());
				tipo = modelo.getTipo();
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
		List<Contenedor> contenedores = contServicio.buscarContenedores(form);
		return contenedores.stream().map(contenedor -> convertToDto(contenedor)).collect(Collectors.toList());
	}

	private MapaContenedoresDto convertToDto(Contenedor contenedor) {
		MapaContenedoresDto dto = new MapaContenedoresDto();

		try {
			TipoDeBasura tipo = contServicio.buscarModeloById(contenedor.getModelo().getId()).getTipo();
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
			return contServicio.buscarModeloById(id).getTipo().getTipo();
		} catch (InstanceNotFoundException e) {
			return "";
		}
	}

	@RequestMapping(value = "/rutas/alertas/numItems", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody int getAlertasItems() {
		try {
			logger.info(" get alertas spring mvc 1");
			int items = rutaServicio.getNumeroAlertas();
			logger.info(" get alertas spring mvc 2");
			return items;
		} catch (Exception e) {
			return 0;
		}
	}

	@RequestMapping(value = "/sensor/{id}/json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<SensorValores> gets(@PathVariable("id") Long sensorId,
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date toDate)
			throws InstanceNotFoundException {
		logger.info("contenedores/sensores/id/json");
		List<Valor> valores = new ArrayList<Valor>();

		contServicio.buscarSensorById(sensorId);

		if (fromDate != null)
			logger.info("fromDate =>" + fromDate.toString());
		if (toDate != null)
			logger.info("toDate =>" + toDate.toString());

		valores = contServicio.buscarValoresBySensor(sensorId, fromDate, toDate);
		return valores.stream().map(valor -> convertToDto(valor)).collect(Collectors.toList());
	}

	@RequestMapping(value = "/chart/reciclado/json", method = RequestMethod.GET)
	public @ResponseBody List<ComparativaPorTipoReciclado> getEstadisticaRecicladoXTipo(
			@RequestParam(value = "tipoComparativa", required = true) TipoComparativa tipoComparativa,
			@RequestParam(value = "tiposDeBasura", required = true) List<Integer> tiposDeBasura)
			throws InstanceNotFoundException {
		logger.info("/estadisticas/reciclado/json");
		logger.info(" getEstadisticaRecicladoXTipo INICIO");
		List<ComparativaPorTipoReciclado> comparativa = new ArrayList<ComparativaPorTipoReciclado>();
		comparativa = contServicio.comparativaPorTipoReciclado(tipoComparativa, tiposDeBasura);
		logger.info(" getEstadisticaRecicladoXTipo FINAL");
		return comparativa;
	}

	public BigDecimal average(List<BigDecimal> bigDecimals, RoundingMode roundingMode) {
		BigDecimal sum = bigDecimals.stream().map(Objects::requireNonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
		return sum.divide(new BigDecimal(bigDecimals.size()), roundingMode);
	}

	private SensorValores convertToDto(Valor valor) {
		Timestamp timestamp = new Timestamp(valor.getPk().getFechaHora().getTimeInMillis());
		SensorValores sensorValor = new SensorValores(timestamp, valor.getValor(), "%");
		return sensorValor;
	}
}
