package es.udc.tfg;

import javax.inject.Inject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import es.udc.tfg.model.api.ComponentMap;
import es.udc.tfg.service.ConsumerService;
import es.udc.tfg.util.exceptions.DuplicateInstanceException;

@SpringBootApplication
// @ComponentScan(basePackages = { "es.udc.fic.tfg.sensores.*" })
// @EntityScan("es.udc.fic.tfg.sensores.*")
// @EnableJpaRepositories("es.udc.fic.tfg.sensores.*")
public class TfgAppApplication {

	@Inject
	private ConsumerService service;
	private String url = "http://connecta.bcn.cat/connecta-catalog-web/component/map/json";
	private ComponentMap componentMap;
	
	public static void main(String[] args) {
		SpringApplication.run(TfgAppApplication.class, args);

	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			componentMap = restTemplate.getForObject(url, ComponentMap.class);
			// log.info(componentMap.toString());
			// apiBarcelona(componentMap);
			addType();
		};
	}

	public void addType() {
		for (int i = 0; i < 10; i++) {
			try {
				//log.info("AÑADIENDO TIPOS");
				service.registerType("PRUEBA_TYPE" + i, "Haciendo_pruebas");
				//log.info("AÑADIENDO PRUEBA_TYPE" + i);
			} catch (DuplicateInstanceException e) {
			}
		}
	}
}
