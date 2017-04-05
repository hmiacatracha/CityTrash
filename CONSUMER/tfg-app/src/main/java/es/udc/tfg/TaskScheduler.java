package es.udc.tfg;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.tfg.model.api.Component;
import es.udc.tfg.model.api.ComponentLastObserv;
import es.udc.tfg.model.api.ComponentMapBarcelona;
import es.udc.tfg.model.api.SensorLastObservations;
import es.udc.tfg.model.town.Townhall;
import es.udc.tfg.service.ConsumerService;
import es.udc.tfg.util.ContainerType;
import es.udc.tfg.util.exceptions.InstanceNotFoundException;

@Configuration
@EnableAsync
@EnableScheduling
@PropertySource(value = { "classpath:application.properties" })
public class TaskScheduler {

	@Inject
	private ConsumerService service;

	@Value("${api.barcelona.url.maps}")
	private String urlMapsBarcelona;

	@Value("${api.barcelona.url.component}")
	private String urlComponentBarcelona;

	private ComponentMapBarcelona componentBarcelonaMap = new ComponentMapBarcelona();
	private ComponentLastObserv recordBarcelonaMap = new ComponentLastObserv();
	private ObjectMapper mapper = new ObjectMapper();
	private Townhall townBarcelona;
	private static final Logger log = LoggerFactory.getLogger(TaskScheduler.class);

	@Scheduled(cron = "${cron.expression.components}")
	@Async
	public void registrarComponentesBarcelona() {
		log.info("SheduledTask:registrarComponentesBarcelona");
		float tons = (float) 0.0;
		try {
			townBarcelona = service.finTownHall("Barcelona");
			try {
				this.componentBarcelonaMap = mapper.readValue(new URL(urlMapsBarcelona), ComponentMapBarcelona.class);
				for (Component component : componentBarcelonaMap.getComponents()) {
					if (service.converToDumpsterType(component.getType()) != ContainerType.NONE) {
						log.info("prueba heidy sensor:" + component.getId());
						service.registerDumpster(component.getId(), component.getType(), townBarcelona.getProvince(),
								townBarcelona.getId(), component.getCoordinates().get(0).getLatitude(),
								component.getCoordinates().get(0).getLongitude(), tons);
					}
				}
			} catch (IOException e) {
				log.error("Error:mapper in registrarComponentesBarcelona");
			}
		} catch (InstanceNotFoundException e1) {
			log.error("Error: find town in registrarComponentesBarcelona");
		}
	}

	@Scheduled(cron = "${cron.expression.records}")
	@Async
	public void registrarSensoresBarcelona() {
		log.info("SheduledTask:registrarSensoresBarcelona");
		try {
			townBarcelona = service.finTownHall("Barcelona");
			try {
				this.componentBarcelonaMap = mapper.readValue(new URL(urlMapsBarcelona), ComponentMapBarcelona.class);
				for (Component component : componentBarcelonaMap.getComponents()) {
					if (service.converToDumpsterType(component.getType()) != ContainerType.NONE) {
						log.info("prueba heidy componente:" + component.getId());
						String url = urlComponentBarcelona.replace("COMPONENT_ID_URL", component.getId());
						log.info("URL:" + url);
						this.recordBarcelonaMap = mapper.readValue(new URL(url), ComponentLastObserv.class);
						for (SensorLastObservations lastObserv : recordBarcelonaMap.getSensorLastObservations()) {
							service.registerSensor(lastObserv.getSensor(), component.getId(), townBarcelona.getId(),
									lastObserv.getSensorType(), lastObserv.getUnit());
							log.info("prueba heidy register recor before:" + component.getId());
							service.registerRecord(lastObserv.getSensor(), component.getId(), townBarcelona.getId(),
									lastObserv.getTimestamp(), lastObserv.getValue());
							log.info("prueba heidy register record after:" + component.getId());
						}
					}
				}
			} catch (IOException e) {
				log.error("Error:mapper in registrarSensoresBarcelona");
			}
		} catch (InstanceNotFoundException e1) {
			log.error("Error: find town in registrarSensoresBarcelona");
		}
	}
}
