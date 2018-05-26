package es.udc.citytrash.controller.mapa;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.udc.citytrash.controller.util.AjaxUtils;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.util.excepciones.GeoLocationException;

@Controller
public class MapaController {
	@Autowired
	ContenedorService cServicio;

	@Autowired
	private ModelMapper modelMapper;

	final Logger logger = LoggerFactory.getLogger(MapaController.class);

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_MAPA_CONTENEDORES, method = RequestMethod.GET)
	public String getContenedoresMaps(HttpServletRequest request,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws GeoLocationException {

		logger.info("GET REQUEST_MAPPING_MAPA_CONTENEDORES");
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_MAPA_CONTENEDORES.concat(" ::content");
		return WebUtils.VISTA_MAPA_CONTENEDORES;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_MAPA_RUTAS, method = RequestMethod.GET)
	public String getRutas(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("GET REQUEST_MAPPING_MAPA_RUTAS");
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_MAPA_RUTAS.concat(" ::content");
		return WebUtils.VISTA_MAPA_RUTAS;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_MAPA_RUTA_NAVEGAR, method = RequestMethod.GET)
	public String getNavegar(@PathVariable("id") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("GET REQUEST_MAPPING_MAPA_RUTAS");
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_MAPA_RUTA_NAVEGAR.concat(" ::content");
		return WebUtils.VISTA_MAPA_RUTA_NAVEGAR;
	}
}
