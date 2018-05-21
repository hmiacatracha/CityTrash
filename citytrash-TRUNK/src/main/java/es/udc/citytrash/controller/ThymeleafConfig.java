package es.udc.citytrash.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
class ThymeleafConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

	final Logger logger = LoggerFactory.getLogger(ThymeleafConfig.class);

	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.addDialect(new LayoutDialect());
		engine.addDialect(new SpringDataDialect());
		engine.addDialect(new SpringSecurityDialect());
		engine.setTemplateResolver(templateResolver());
		logger.info("CONFIGURACION templateEngine");
		return engine;
	}

	@Bean
	public SpringDataDialect springDataDialect() {
		return new SpringDataDialect();
	}

	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCacheable(false);
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}

	@Bean
	public WebRequestInterceptor openEntityManagerInViewInterceptor() {
		return new OpenEntityManagerInViewInterceptor();
	}
}
