package es.udc.citytrash.controller.util;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class MessageUtils {

	public static String getMessage(String key) {

		try {
			ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
			bean.setBasename("messages");
			return bean.getMessage(key, null, Locale.getDefault());
		} catch (Exception e) {
			return "Unresolved key: " + key;
		}
	}
}