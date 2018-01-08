package es.udc.citytrash.business.service.email;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.util.UriComponentsBuilder;

import es.udc.citytrash.business.entity.mail.Mail;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

//http://www.technicalkeeda.com/spring-tutorials/spring-4-sending-email-with-freemarker-template
@Service("EmailNotificationService")
public class EmailNotificationServiceImpl implements EmailNotificacionesService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	Configuration fmConfiguration;

	final Logger logger = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

	public boolean activacionCuentaEmail(String nombre, String apellidos, String email, String token, String appUrl) {
		Map<String, Object> model = new HashMap<String, Object>();
		Locale currentLocale = Locale.getDefault();
		String lang = currentLocale.getLanguage();
		Mail mail = new Mail();
		logger.info("enviar email  registro " + email);
		try {
			model.put("nombre", nombre);
			model.put("email", email);
			model.put("apellidos", apellidos);
			model.put("firma", "CityTrash");
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appUrl + "/cuenta/activar/" + token)
					.queryParam("lang", lang);
			String uri = builder.build().encode().toUriString();
			model.put("activar_url", uri);
			mail.setMailTo(email);
			mail.setMailFrom("no-reply@citytrash.com");
			mail.setMailCc("citytrashtfg@gmail.com");

			logger.info("enviar email activar cuenta  URL->" + uri);
			logger.info("enviar email activar cuenta  LANG->" + lang);
			if (lang != "es") {
				mail.setMailSubject("Activate account cityTrash");
				model.put("titulo", "Activate account cityTrash");
				lang = "en";
			} else {
				mail.setMailSubject("Activar cuenta cityTrash");
				model.put("titulo", "Activar cuenta cityTrash");
			}
			mail.setMailContent(getContenidoPlantilla("registration_" + lang + ".ftl", model));
			enviarEmail(mail);
			return true;
		} catch (TemplateNotFoundException e) {
			try {
				model.put("motivo", "Activar Cuenta de " + email);
				mail.setMailSubject("Error al intentar activar la cuenta");
				mail.setMailTo("citytrashtfg@gmail.com");
				mail.setMailFrom("no-reply@citytrash.com");
				mail.setMailCc("citytrashtfg@gmail.com");
				mail.setMailContent(getContenidoPlantilla("error.ftl", model));
				enviarEmail(mail);
				return false;
			} catch (IOException | TemplateException | MessagingException e1) {
				return false;
			}
		} catch (MessagingException | IOException | TemplateException e) {
			return false;
		}
	}

	@Override
	public boolean recuperarCuentaEmail(String nombre, String apellidos, String email, String token, String appUrl) {
		Map<String, Object> model = new HashMap<String, Object>();
		Locale currentLocale = Locale.getDefault();
		Mail mail = new Mail();
		String lang = currentLocale.getLanguage();
		logger.info("enviar email  registro " + email);
		try {
			model.put("nombre", nombre);
			model.put("email", email);
			model.put("apellidos", apellidos);
			model.put("firma", "CityTrash");
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appUrl + "/cuenta/recuperar/" + token)
					.queryParam("lang", lang);
			String uri = builder.build().encode().toUriString();
			model.put("recuperar_url", uri);
			mail.setMailTo(email);
			mail.setMailFrom("no-reply@citytrash.com");
			mail.setMailCc("citytrashtfg@gmail.com");

			logger.info("enviar email recuperar cuenta  URL->" + uri);
			logger.info("enviar email recuperar cuenta  LANG->" + lang);
			if (lang != "es") {
				mail.setMailSubject("Recover cityTrash account");
				model.put("titulo", "Recover cityTrash account");
				lang = "en";
			} else {
				mail.setMailSubject("Recuperar cuenta cityTrash");
				model.put("titulo", "Recuperar cuenta cityTrash");
			}
			mail.setMailContent(
					getContenidoPlantilla("recuperarCuenta_" + currentLocale.getLanguage() + ".ftl", model));
			enviarEmail(mail);
			return true;
		} catch (TemplateNotFoundException e) {
			try {
				model.put("motivo", "Recuperar Cuenta de " + email);
				mail.setMailSubject("Error al intentar recuperar la cuenta");
				mail.setMailTo("citytrashtfg@gmail.com");
				mail.setMailFrom("no-reply@citytrash.com");
				mail.setMailCc("citytrashtfg@gmail.com");
				mail.setMailContent(getContenidoPlantilla("error.ftl", model));
				enviarEmail(mail);
				return false;
			} catch (IOException | TemplateException | MessagingException e1) {
				return false;
			}
		} catch (MessagingException | IOException | TemplateException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void enviarEmail(Mail mail) throws MessagingException {
		logger.info("Enviar email");
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setSubject(mail.getMailSubject());
		mimeMessageHelper.setFrom(mail.getMailFrom());
		mimeMessageHelper.setTo(mail.getMailTo());
		mimeMessageHelper.setText(mail.getMailContent(), true);
		mailSender.send(mimeMessageHelper.getMimeMessage());
	}

	private String getContenidoPlantilla(String templateName, Map<String, Object> model)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
			TemplateException {

		logger.info("Coger contenio de la plantilla ");
		StringBuffer content = new StringBuffer();
		content.append(
				FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateName), model));
		return content.toString();
	}
}
