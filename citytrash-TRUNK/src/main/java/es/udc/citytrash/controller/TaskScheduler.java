package es.udc.citytrash.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import es.udc.citytrash.model.rutaService.RutaService;

@Configuration
@EnableAsync
@EnableScheduling
@PropertySources(value = { @PropertySource("classpath:config/mail.properties") })
public class TaskScheduler {

	@Autowired
	RutaService rServicio;

	final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

	@Scheduled(cron = "0 0/1 * * * ?")
	@Async
	public void alertasContenedoresLlenos() {
		logger.info("INICIO alertasContenedoresLlenos");
		rServicio.crearAlertaContenedoresLlenos();
		logger.info("FIN alertasContenedoresLlenos");
	}
	
	@Scheduled(cron = "0 0/10 * * * ?")
	@Async
	public void alertascambioBruscoDeVolumen() {
		logger.info("INICIO alertascambioBruscoDeVolumen");
		rServicio.crearAlertaCambioBruscoContenedor();
		logger.info("FIN alertascambioBruscoDeVolumen");
	}

	@Scheduled(cron = "0 0/60 * * * ?")
	@Async
	public void alertasAsignacionRecursosAUnaRutaDiaria() {
		logger.info("INICIO alertasAsignacionRecursosAUnaRutaDiaria");
		rServicio.crearAlertaTrabajadoresAsignadosAMasDeUnaRuta();
		logger.info("FIN alertasAsignacionRecursosAUnaRutaDiaria fin");
	}

	@Scheduled(cron = "0 0/60 * * * ?")
	@Async
	public void alertasRutasSinRecursos() {
		logger.info("INICIO alertasRutasSinRecursos");
		rServicio.crearAlertaRutaSinRecolectoresAsignados();
		rServicio.crearAlertaRutaSinConductorAsignado();
		logger.info("FIN alertasRutasSinRecursos");
	}
}
