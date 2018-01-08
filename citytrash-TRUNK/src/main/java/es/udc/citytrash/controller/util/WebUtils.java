package es.udc.citytrash.controller.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {

	private WebUtils() {

	}

	public static final String IMAGES_PREFIX = "/images/";
	public static final String IMAGES_DIR = "classpath";

	public static String getURLWithContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}
	
}
