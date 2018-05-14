package es.udc.citytrash.controller.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {

	private WebUtils() {

	}

	/**
	 * VISTAS
	 */
	public static final String IMAGES_PREFIX = "/images/";
	public static final String IMAGES_DIR = "classpath";
	public static final String TIME_ZONE = "UTC";

	/* tamano de página por defecto */
	public final static int DEFAULT_PAGE_SIZE = 10;
	public final static int DEFAULT_PAGE_NUMBER = 0;
	public final static String DEFAULT_SORT = "id";

	/* paquete HOME */
	public static final String VISTA_HOME_PREFIX = "home/";
	public static final String VISTA_HOME = VISTA_HOME_PREFIX + "index";

	/* paquete TRABAJADORES */
	private static final String VISTA_TRABAJADORES_PREFIX = "trabajador/";
	public static final String VISTA_TRABAJADORES_REGISTRO = VISTA_TRABAJADORES_PREFIX + "registro";
	public static final String VISTA_TRABAJADORES_UPDATE = VISTA_TRABAJADORES_PREFIX + "update";
	public static final String VISTA_TRABAJADORES = VISTA_TRABAJADORES_PREFIX + "trabajadores";
	public static final String VISTA_TRABAJADOR_DETALLES = VISTA_TRABAJADORES_PREFIX + "detalles";
	public static final String VISTA_TRABAJADOR_INFORMACION_PERSONAL = VISTA_TRABAJADORES_PREFIX
			+ "/detalles/informacionPersonal";
	public static final String VISTA_TRABAJADOR_INFORMACION_CUENTA = VISTA_TRABAJADORES_PREFIX
			+ "/detalles/informacionCuenta";
	public static final String VISTA_TRABAJADOR_RUTAS = VISTA_TRABAJADORES_PREFIX + "/detalles/rutas";

	/* Paquete cuenta */
	private static final String VISTA_CUENTA_PREFIX = "cuenta/";
	public static final String VISTA_REINICIAR_CONTRASENA = VISTA_CUENTA_PREFIX + "reiniciarPassword";
	public static final String VISTA_CAMBIAR_CONTRASENA = VISTA_CUENTA_PREFIX + "cambiarPassword";
	public static final String VISTA_LOGIN = VISTA_CUENTA_PREFIX + "login";
	public static final String VISTA_RECUPERAR_CUENTA = VISTA_CUENTA_PREFIX + "recuperarCuenta";
	public static final String VISTA_CAMBIAR_IDIOMA = VISTA_CUENTA_PREFIX + "cambiarIdioma";
	public static final String VISTA_PERFIL = VISTA_CUENTA_PREFIX + "perfil_tabs";
	public static final String VISTA_PERFIL_DETALLES = VISTA_CUENTA_PREFIX + "perfil_detalles";

	/* Paquete about us */
	private static final String VISTA_ABOUT_PREFIX = "about/";
	public static final String VISTA_ABOUT_US = VISTA_ABOUT_PREFIX + "about_us";

	/* paquete CAMIONES */
	public static final String VISTA_CAMIONES_PREFIX = "camiones/";
	public static final String VISTA_CAMIONES_MODELOS_PREFIX = "camiones/modelos/";
	public static final String VISTA_CAMIONES = VISTA_CAMIONES_PREFIX + "camiones";
	public static final String VISTA_CAMIONES_REGISTRO = VISTA_CAMIONES_PREFIX + "camionesRegistro";
	public static final String VISTA_CAMIONES_EDITAR = VISTA_CAMIONES_PREFIX + "camionesModificar";
	public static final String VISTA_CAMIONES_DETALLES = VISTA_CAMIONES_PREFIX + "detalles";
	public static final String VISTA_CAMIONES_DETALLES_INFO = VISTA_CAMIONES_PREFIX + "detalles/informacionCamion";
	public static final String VISTA_CAMIONES_DETALLES_RUTAS = VISTA_CAMIONES_PREFIX + "detalles/historialRutas";
	public static final String VISTA_CAMIONES_DETALLES_MODELO = VISTA_CAMIONES_PREFIX + "detalles/informacionModelo";

	public static final String VISTA_CAMIONES_MODELOS = VISTA_CAMIONES_MODELOS_PREFIX + "modelos";
	public static final String VISTA_CAMIONES_MODELOS_FORMULARIO = VISTA_CAMIONES_MODELOS_PREFIX
			+ "modelosRegistroYEditar";
	public static final String VISTA_CAMIONES_MODELOS_DETALLES = VISTA_CAMIONES_MODELOS_PREFIX + "detalles";
	public static final String VISTA_CAMIONES_MODELOS_DETALLES_INFO = VISTA_CAMIONES_MODELOS_PREFIX
			+ "detalles/informacionModelo";
	public static final String VISTA_CAMIONES_MODELOS_DETALLES_CAMIONES = VISTA_CAMIONES_MODELOS_PREFIX
			+ "detalles/ListaDeCamiones";
	/**
	 * URLS
	 */

	/* PUBLIC */
	public static final String URL_HOME = "/";
	public static final String URL_HOME1 = "/home";
	public static final String URL_HOME2 = "/index";
	public static final String URL_LOGOUT = "/logout";
	public static final String URL_LOGIN = "/login";
	public static final String URL_ABOUT_US = "/about";

	public static final String URL_FORBIDDEN = "/403";
	public static final String URL_NOT_FOUND = "/404";
	public static final String URL_BAD_REQUEST = "/400";
	public static final String URL_CONFLICT = "/409";
	public static final String URL_UNAUTHORIZED = "/401";

	/* URLS BAJO EL ROOT /AUTH/ADMIN/ */
	public static final String REQUEST_MAPPING_TRABAJADORES = "/";
	public static final String URL_TRABAJADORES = "/trabajadores/registro";
	public static final String URL_TRABAJADORES_REGISTRO = "/trabajadores/registro";
	public static final String REQUEST_MAPPING_TRABAJADORES_REGISTRO = "registro";
	public static final String URL_TRABAJADOR_DETALLES = "/trabajadores/{trabajadorId}/detalles";
	public static final String REQUEST_MAPPING_TRABAJADOR_DETALLES = "/{trabajadorId}/detalles";

	public static final String URL_TRABAJADOR_UPDATE = "/trabajadores/{trabajadorId}/editar";
	public static final String REQUEST_MAPPING_TRABAJADOR_UPDATE = "/{trabajadorId}/editar";

	public static final String URL_TRABAJADOR_CAMBIAR_ESTADO = "/trabajadores/{trabajadorId}/cambiarEstado";
	public static final String REQUEST_MAPPING_CAMBIAR_ESTADO = "/{trabajadorId}/cambiarEstado";

	public static final String URL_TRABAJADOR_INFO_PERSONAL = "/trabajadores/{trabajadorId}/detalles/infopersonal";
	public static final String REQUEST_MAPPING_TRABAJADOR_INFO_PERSONAL = "/{trabajadorId}/detalles/infopersonal";

	public static final String URL_TRABAJADOR_INFO_CUENTA = "/trabajadores/{trabajadorId}/detalles/cuenta";
	public static final String REQUEST_MAPPING_TRABAJADOR_INFO_CUENTA = "/{trabajadorId}/detalles/cuenta";

	public static final String URL_TRABAJADOR_RUTAS = "/trabajadores/{trabajadorId}/rutas";
	public static final String REQUEST_MAPPING_TRABAJADOR_RUTAS = "/{trabajadorId}/rutas";

	/* URLS CUENTA */
	public static final String URL_CUENTA_ACTIVAR_REAL = "/cuenta/activar";
	public static final String REQUEST_MAPPING_CUENTA_ACTIVAR = "activar";

	public static final String URL_CUENTA_RECUPERAR = "/cuenta/recuperar";
	public static final String REQUEST_MAPPING_CUENTA_RECUPERAR = "recuperar";

	public static final String URL_CAMBIAR_PASSWORD = "/cuenta/cambiarPassword";
	public static final String REQUEST_MAPPING_CAMBIAR_PASSWORD = "cambiarPassword";

	public static final String URL_CUENTA_RESET_PASSWORD = "/cuenta/reset-password";
	public static final String REQUEST_MAPPING_CUENTA_RESET_PASSWORD = "reset-password";

	public static final String URL_CUENTA_CAMBIO_IDIOMA = "/cuenta/idiomapreferencia";
	public static final String REQUEST_MAPPING_CUENTA_CAMBIO_IDIOMA = "idiomapreferencia";

	public static final String URL_CUENTA_ACTUALIZAR_CONTRASENA = "/cuenta/actualizarPassword";
	public static final String REQUEST_MAPPING_CUENTA_ACTUALIZAR_CONTRASENA = "actualizarPassword";

	public static final String URL_CUENTA_PERFIL = "cuenta/perfil";
	public static final String REQUEST_MAPPING_PERFIL = "perfil";

	public static final String URL_CUENTA_PERFIL_DETALLES = "cuenta/perfil/detalles";
	public static final String REQUEST_MAPPING_PERFIL_DETALLES = "perfil/detalles";

	// URLS CAMIONES
	public static final String URL_MAPPING_CAMIONES = "/camiones";
	public static final String REQUEST_MAPPING_CAMIONES = "/";

	public static final String URL_MAPPING_CAMIONES_REGISTRO = "/camiones/registro";
	public static final String REQUEST_MAPPING_CAMIONES_REGISTRO = "/registro";

	public static final String URL_MAPPING_CAMIONES_DETALLES = "/camiones/{id}/detalles";
	public static final String REQUEST_MAPPING_CAMIONES_DETALLES = "/{id}/detalles";

	public static final String URL_MAPPING_CAMIONES_DETALLES_INFO_CAMION = "/camiones/{id}/detalles/camion";
	public static final String REQUEST_MAPPING_CAMIONES_DETALLES_INFO_CAMION = "/{id}/detalles/camion";

	public static final String URL_MAPPING_CAMIONES_DETALLES_INFO_MODELO = "/camiones/{id}/detalles/modelo";
	public static final String REQUEST_MAPPING_CAMIONES_DETALLES_INFO_MODELO = "/{id}/detalles/modelo";

	public static final String URL_MAPPING_CAMIONES_DETALLES_INFO_RUTAS = "/camiones/{id}/detalles/modelo";
	public static final String REQUEST_MAPPING_CAMIONES_DETALLES_INFO_RUTAS = "/{id}/detalles/modelo";

	public static final String URL_MAPPING_CAMIONES_EDITAR = "/camiones/{id}/editar";
	public static final String REQUEST_MAPPING_CAMIONES_EDITAR = "/{id}/editar";

	public static final String URL_MAPPING_CAMIONES_ESTADO = "/camiones/{id}/estado";
	public static final String REQUEST_MAPPING_CAMIONES_ESTADO = "/{id}/estado";

	public static final String URL_MAPPING_CAMIONES_MODELOS = "/camiones/modelos";
	public static final String REQUEST_MAPPING_CAMIONES_MODELOS = "/modelos";

	public static final String URL_MAPPING_CAMIONES_REGISTRO_MODELO = "/camiones/modelos/registro";
	public static final String REQUEST_MAPPING_CAMIONES_REGISTRO_MODELO = "/modelos/registro";

	public static final String URL_MAPPING_CAMIONES_DETALLES_MODELO = "/camiones/modelos/{id}/detalles";
	public static final String REQUEST_MAPPING_CAMIONES_DETALLES_MODELO = "/modelos/{id}/detalles";

	public static final String URL_MAPPING_CAMIONES_DETALLES_MODELO_INFO_MODELO = "/camiones/modelos/{id}/detalles/modelo";
	public static final String REQUEST_MAPPING_CAMIONES_DETALLES_MODELO_INFO_MODELO = "/modelos/{id}/detalles/modelo";

	public static final String URL_MAPPING_CAMIONES_DETALLES_MODELO_INFO_CAMIONES = "/camiones/modelos/{id}/detalles/camiones";
	public static final String REQUEST_MAPPING_CAMIONES_DETALLES_MODELO_INFO_CAMIONES = "/modelos/{id}/detalles/camiones";

	public static final String URL_MAPPING_CAMIONES_EDITAR_MODELO = "/camiones/modelos/{id}/editar";
	public static final String REQUEST_MAPPING_CAMIONES_EDITAR_MODELO = "/modelos/{id}/editar";

	public static final String URL_MAPPING_CAMIONES_MODELO_REGISTRO_OR_MODIFICAR = "/camiones/modelos/guardar";
	public static final String REQUEST_MAPPING_CAMIONES_MODELO_REGISTRO_OR_MODIFICAR = "/modelos/guardar";

	public static String getUrlWithContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

}
