/*-
 * ========================LICENSE_START=================================
 * TFG BYTEPIT-APP
 * %%
 * Copyright (C) 2016 Heidy Mabel Izaguirre Alvarez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * =========================LICENSE_END==================================
 */
package es.udc.tfg;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import es.udc.tfg.model.api.ComponentMapBarcelona;
import es.udc.tfg.model.province.Province;
import es.udc.tfg.model.town.Townhall;
import es.udc.tfg.service.ConsumerService;

/**
 * Main tfg aplication
 * 
 * @author hmia
 *
 */
@SpringBootApplication
@PropertySource(value = { "classpath:application.properties" })
@EntityScan(basePackages = { "es.udc.tfg.model" })
@EnableAutoConfiguration
public class SensoresApplication {

	@Inject
	private ConsumerService service;

	@Value("${api.barcelona.url.maps}")
	private String urlMapsBarcelona;

	@Value("${api.barcelona.url.component}")
	private String urlComponentBarcelona;

	@SuppressWarnings("unused")
	private ComponentMapBarcelona componentBarcelonaMap = new ComponentMapBarcelona();
	
	@SuppressWarnings("unused")
	private RestTemplate restTemplate = null;
	@SuppressWarnings("unused")
	private Townhall townBarcelona = null;

	private static final Logger log = LoggerFactory.getLogger(TaskScheduler.class);

	public static void main(String[] args) {
		SpringApplication.run(SensoresApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			service.addTypes();
			apiBarcelona(restTemplate);
		};
	}

	/*
	 * Initializer barcelona api
	 */
	public void apiBarcelona(RestTemplate restTemplate) {
		log.debug("SensoresApplication:apiBarcelona");
		String name = "Barcelona";
		float latitude = (float) 41.3850639;
		float longitude = (float) 2.1734034999999494;
		Province p = service.registerProvince(8, name);
		this.townBarcelona = service.registerCity(p, "Barcelona", latitude, longitude);
		this.restTemplate = restTemplate;
		this.componentBarcelonaMap = restTemplate.getForObject(urlMapsBarcelona, ComponentMapBarcelona.class);
	}
	
	
}
