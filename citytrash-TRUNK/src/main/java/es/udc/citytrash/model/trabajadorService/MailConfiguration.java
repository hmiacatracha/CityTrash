package es.udc.citytrash.model.trabajadorService;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/*http://www.technicalkeeda.com/spring-tutorials/spring-4-sending-email-with-freemarker-template*/
/*https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/ui/freemarker/FreeMarkerConfigurationFactory.html*/
/*http://viralpatel.net/blogs/introduction-to-freemarker-template-ftl/*/
@Configuration
@PropertySources(value = { @PropertySource("classpath:config/mail.properties") })
public class MailConfiguration {

	@Value("${mail.protocol}")
	private String protocol;
	@Value("${mail.host}")
	private String host;
	@Value("${mail.port}")
	private int port;
	@Value("${mail.smtp.socketFactory.port}")
	private int socketPort;
	@Value("${mail.smtp.auth}")
	private boolean auth;
	@Value("${mail.smtp.starttls.enable}")
	private boolean starttls;
	@Value("${mail.smtp.starttls.required}")
	private boolean startlls_required;
	@Value("${mail.smtp.debug}")
	private boolean debug;
	@Value("${mail.smtp.socketFactory.fallback}")
	private boolean fallback;
	@Value("${mail.from}")
	private String from;
	@Value("${mail.username}")
	private String username;
	@Value("${mail.password}")
	private String password;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Properties mailProperties = new Properties();

		mailProperties.put("mail.smtp.auth", auth);
		mailProperties.put("mail.smtp.starttls.enable", starttls);
		mailProperties.put("mail.smtp.starttls.required", startlls_required);
		mailProperties.put("mail.smtp.socketFactory.port", socketPort);
		mailProperties.put("mail.smtp.debug", debug);
		mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		mailProperties.put("mail.smtp.socketFactory.fallback", fallback);

		mailSender.setJavaMailProperties(mailProperties);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setProtocol(protocol);
		mailSender.setUsername(username);
		mailSender.setPassword(password);	
		return mailSender;
	}

	@Bean
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
		FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
		fmConfigFactoryBean.setTemplateLoaderPath("classpath:templates/email/");
		return fmConfigFactoryBean;
	}
}
