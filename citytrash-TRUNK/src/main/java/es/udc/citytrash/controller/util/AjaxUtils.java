package es.udc.citytrash.controller.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AjaxUtils {

	static final Logger logger = LoggerFactory.getLogger(AjaxUtils.class);

	private AjaxUtils() {
	}

	public static boolean isAjaxRequest(String requestedWith) {
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}
}