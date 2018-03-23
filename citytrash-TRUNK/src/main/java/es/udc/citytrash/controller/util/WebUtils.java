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

	/* paquete HOME */
	public static final String VISTA_HOME_PREFIX = "home/";
	public static final String VISTA_HOME = VISTA_HOME_PREFIX + "index";

	/* paquete TRABAJADORES */
	private static final String VISTA_TRABAJADORES_PREFIX = "trabajador/";
	public static final String VISTA_TRABAJADORES_REGISTRO = VISTA_TRABAJADORES_PREFIX + "registro";
	public static final String VISTA_TRABAJADORES = VISTA_TRABAJADORES_PREFIX + "trabajadores";
	public static final String VISTA_TRABAJADOR_DETALLE = VISTA_TRABAJADORES_PREFIX + "detalle";

	/* Paquete cuenta */
	private static final String VISTA_CUENTA_PREFIX = "cuenta/";
	public static final String VISTA_REINICIAR_CONTRASENA = VISTA_CUENTA_PREFIX + "cambiar-password";
	public static final String VISTA_LOGIN = VISTA_CUENTA_PREFIX + "login";
	public static final String VISTA_RECUPERAR_CUENTA = VISTA_CUENTA_PREFIX + "recuperarCuenta";
	public static final String VISTA_CAMBIAR_IDIOMA = VISTA_CUENTA_PREFIX + "cambiar-idioma";

	/* Paquete about us */
	private static final String VISTA_ABOUT_PREFIX = "about/";
	public static final String VISTA_ABOUT_US = VISTA_ABOUT_PREFIX + "about_us";

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

	/* URLS BAJO EL ROOT /AUTH/ADMIN/ */
	public static final String URL_TRABAJADORES = "/trabajadores/registro";
	public static final String REQUEST_MAPPING_TRABAJADORES = "/";
	public static final String URL_TRABAJADORES_REGISTRO = "/trabajadores/registro";
	public static final String REQUEST_MAPPING_TRABAJADORES_REGISTRO = "registro";
	public static final String URL_TRABAJADORES_DETALLES = "/trabajadores/{trabajadorId}/detalle";
	public static final String REQUEST_MAPPING_TRABAJADORES_DETALLES = "/{trabajadorId}/detalle";

	/* URLS CUENTA */
	public static final String URL_CUENTA_ACTIVAR_REAL = "/cuenta/activar";
	public static final String REQUEST_MAPPING_CUENTA_ACTIVAR = "activar";

	public static final String URL_CUENTA_RECUPERAR = "/cuenta/recuperar";
	public static final String REQUEST_MAPPING_CUENTA_RECUPERAR = "recuperar";

	public static final String URL_CUENTA_RESET_PASSWORD = "/cuenta/reset-password";
	public static final String REQUEST_MAPPING_CUENTA_RESET_PASSWORD = "reset-password";

	public static final String URL_CUENTA_CAMBIO_IDIOMA = "/cuenta/idiomapreferencia";
	public static final String REQUEST_MAPPING_CUENTA_CAMBIO_IDIOMA = "idiomapreferencia";

	public static final String URL_CUENTA_ACTUALIZAR_CONTRASENA = "/cuenta/actualizarPassword";
	public static final String REQUEST_MAPPING_CUENTA_ACTUALIZAR_CONTRASENA = "actualizarPassword";

	public static final String URL_CUENTA_CAMBIAR_CONTRASENA = "/cambiarPassword";
	public static final String REQUEST_MAPPING_CAMBIAR_CONTRASENA = "cuenta/cambiarPassword";

	public static String getURLWithContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

}