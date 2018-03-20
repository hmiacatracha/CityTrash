package es.udc.citytrash.business.service.email;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.util.UriComponentsBuilder;

import es.udc.citytrash.business.entity.idioma.Idioma;
import es.udc.citytrash.business.entity.mail.Mail;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

//http://www.technicalkeeda.com/spring-tutorials/spring-4-sending-email-with-freemarker-template
@Service("EmailNotificationService")
@PropertySources(value = { @PropertySource("classpath:config/mail.properties"),
		@PropertySource("classpath:config/urls.properties") })
public class EmailNotificationServiceImpl implements EmailNotificacionesService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	Configuration fmConfiguration;

	@Value("${mail.from}")
	private String from;
	@Value("${mail.firma}")
	private String firma;
	@Value("${link.cuenta.recuperar}")
	private String urlActivar;
	@Value("${link.cuenta.recuperar}")
	private String urlRecuperar;
	@Value("${mail.plantilla.activar}")
	private String plantillaActivarCuenta;
	@Value("${mail.plantilla.recuperar}")
	private String plantillaRecuperarCuenta;
	@Value("${mail.asunto.activar.en}")
	private String activiarAsuntoEn;
	@Value("${mail.asunto.activar.es}")
	private String activiarAsuntoEs;
	@Value("${mail.asunto.activar.gal}")
	private String activarAsuntoGal;
	@Value("${mail.asunto.recuperar.es}")
	private String recuperarAsuntoEs;
	@Value("${mail.asunto.recuperar.en}")
	private String recuperarAsuntoEn;
	@Value("${mail.asunto.recuperar.gal}")
	private String recuperarAsuntoGal;

	final Logger logger = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

	public void activacionCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl) {
		Map<String, Object> model = new HashMap<String, Object>();
		SimpleDateFormat sdf;
		Mail mail = new Mail();
		logger.info("enviar email  registro " + email);
		try {
			model.put("nombre", nombre);
			model.put("email", email);
			model.put("apellidos", apellidos);
			model.put("firma", firma);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appUrl + urlActivar).queryParam("id", id)
					.queryParam("token", token).queryParam("lang", lang.name());
			String uri = builder.build().encode().toUriString();
			model.put("activar_url", uri);
			mail.setMailTo(email);
			mail.setMailFrom("no-reply@citytrash.com");
			mail.setMailCc(from);

			switch (lang) {
			case es:
				sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", new Locale("es_ES"));
				mail.setMailSubject(activiarAsuntoEs);
				model.put("titulo", activiarAsuntoEs);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				break;
			case gal:
				sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss", new Locale("gal_ES"));
				mail.setMailSubject(activarAsuntoGal);
				model.put("titulo", activarAsuntoGal);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				break;
			default:
				sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
				mail.setMailSubject(activiarAsuntoEn);
				model.put("titulo", activiarAsuntoEn);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				lang = Idioma.en;
			}
			mail.setMailContent(getContenidoPlantilla(this.plantillaActivarCuenta + "_" + lang.name() + ".ftl", model));

			enviarEmail(mail);
		} catch (TemplateNotFoundException e) {
			try {
				model.put("motivo", "Activar Cuenta de " + email);
				mail.setMailSubject("Error al intentar activar la cuenta");
				mail.setMailTo(from);
				mail.setMailFrom("no-reply@citytrash.com");
				mail.setMailCc(from);
				mail.setMailContent(getContenidoPlantilla("error.ftl", model));
				enviarEmail(mail);
			} catch (IOException | TemplateException | MessagingException e1) {
			}
		} catch (MessagingException | IOException | TemplateException e) {

		}
	}

	@Override
	public void recuperarCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl) {
		Map<String, Object> model = new HashMap<String, Object>();
		SimpleDateFormat sdf;// new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		Mail mail = new Mail();

		logger.info("enviar email  registro " + email);
		try {
			model.put("nombre", nombre);
			model.put("email", email);
			model.put("apellidos", apellidos);
			model.put("firma", firma);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appUrl + urlRecuperar)
					.queryParam("id", id).queryParam("token", token).queryParam("lang", lang.name());
			String uri = builder.build().encode().toUriString();
			model.put("recuperar_url", uri);
			mail.setMailTo(email);
			mail.setMailFrom("no-reply@citytrash.com");
			mail.setMailCc(from);

			switch (lang) {
			case es:
				sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", new Locale("es_ES"));
				mail.setMailSubject(recuperarAsuntoEs);
				model.put("titulo", recuperarAsuntoEs);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				break;
			case gal:
				sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", new Locale("gal_ES"));
				mail.setMailSubject(recuperarAsuntoGal);
				model.put("titulo", recuperarAsuntoEs);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				break;
			default:
				sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
				mail.setMailSubject(recuperarAsuntoEn);
				model.put("titulo", recuperarAsuntoEn);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				lang = Idioma.en;
			}

			mail.setMailContent(
					getContenidoPlantilla(this.plantillaRecuperarCuenta + "_" + lang.name() + ".ftl", model));
			enviarEmail(mail);

		} catch (TemplateNotFoundException e) {
			try {
				model.put("motivo", "Recuperar Cuenta de " + email);
				mail.setMailSubject("Error al intentar recuperar la cuenta");
				mail.setMailTo(from);
				mail.setMailFrom("no-reply@citytrash.com");
				mail.setMailContent(getContenidoPlantilla("error.ftl", model));
				enviarEmail(mail);
			} catch (IOException | TemplateException | MessagingException e1) {
			}
		} catch (MessagingException | IOException | TemplateException e) {
			e.printStackTrace();
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
