package es.udc.fic.tfg.sensores.service.consumer;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import es.udc.fic.tfg.sensores.model.api.barcelona.Component;
import es.udc.fic.tfg.sensores.model.api.barcelona.ComponentMap;
import es.udc.fic.tfg.sensores.model.dumpster.Dumpster;
import es.udc.fic.tfg.sensores.model.townhall.TownHall;
import es.udc.fic.tfg.sensores.model.util.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

@SpringBootApplication
public class SensoresApplication {

	@Inject
	private ConsumerService servicio;

	private String url = "http://connecta.bcn.cat/connecta-catalog-web/component/map/json";
	private ComponentMap componentMap;
	private static final Logger log = LoggerFactory.getLogger(SensoresApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SensoresApplication.class);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			componentMap = restTemplate.getForObject(url, ComponentMap.class);
			log.info(componentMap.toString());
		};
	}

	public void apiBarcelona() {
		TownHall town;
		Dumpster dumspter;
		// Sensor sensor;

		try {
			town = servicio.findTown("BARCELONA");

			for (int i = 0; i < componentMap.getComponents().size(); i++) {
				try {
					Component component = componentMap.getComponents().get(i);

					if (component.getType().contentEquals("container")) {
						String t = getType(component.getType());
						if (!t.equals("")) {
							dumspter = servicio.registerContenedor(component.getId(), town.getId(), t,
									component.getCentroid().getLatitude(), component.getCentroid().getLongitude(), 0);
							System.out.println("Componente añadido:" + dumspter.getKey());
						}
					}
				} catch (DuplicateInstanceException e) {
					continue;
				}
			}
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (DuplicateInstanceException e) {
		}
	}

	private String getType(String type) {

		switch (type.toLowerCase()) {
		case "container_refuse":
			return "CONT_INORG";
		case "container_organic":
			return "CONT_ORGAN";
		case "container_paper":
			return "CONT_PAPER";
		case "container_plastic":
			return "";
		case "container_glass":
			return "CONT_GLASS";
		default:
			return "";
		}
	}
}
