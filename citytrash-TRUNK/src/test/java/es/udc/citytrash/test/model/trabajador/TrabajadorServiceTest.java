package es.udc.citytrash.test.model.trabajador;

import static es.udc.citytrash.business.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.business.util.GlobalNames.SPRING_SECURITY_FILE;
import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.business.entity.idioma.Idioma;
import es.udc.citytrash.business.entity.trabajador.Administrador;
import es.udc.citytrash.business.entity.trabajador.Conductor;
import es.udc.citytrash.business.entity.trabajador.Recolector;
import es.udc.citytrash.business.entity.trabajador.TipoTrabajador;
import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.repository.trabajador.TrabajadorDao;
import es.udc.citytrash.business.service.trabajador.TrabajadorService;
import es.udc.citytrash.business.service.trabajador.TrabajadorServiceImpl;
import es.udc.citytrash.business.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.controller.util.dtos.TrabajadorFormDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class TrabajadorServiceTest {

	TrabajadorFormDto trabajadorDto;

	TrabajadorFormDto trabajadorDuplicado;

	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final String APP_URL = "www.cityTrash.prueba";

	@Autowired
	private TrabajadorService trabajadorService;

	@Autowired
	TrabajadorDao trabajadorDao;

	@Before
	public void startUp() {

		trabajadorDto = new TrabajadorFormDto("Y0454768J", "prueba", "prueba", "citytrasharecol@gmail.com", new Date(),
				TipoTrabajador.ADMIN, Idioma.en);

		trabajadorDuplicado = new TrabajadorFormDto("Y0454768J", "prueba2", "prueba2", "citytrashadmi@gmail.com",
				new Date(), TipoTrabajador.RECOLEC, Idioma.en);
	}

	@Test
	public void registrarAdministrador() {
		trabajadorDto.setTipo(TipoTrabajador.ADMIN);
		try {
			Trabajador t = trabajadorService.registrar(trabajadorDto, APP_URL);
			Trabajador tAux = trabajadorService.buscarTrabajadorEmail(t.getEmail());
			assertEquals(t, tAux);

		} catch (DuplicateInstanceException e) {
			assert (false);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	@Test
	public void registrarConductor() {
		trabajadorDto.setTipo(TipoTrabajador.CONDUCT);
		try {
			Trabajador t = trabajadorService.registrar(trabajadorDto, APP_URL);
			Trabajador tAux = trabajadorService.buscarTrabajadorEmail(t.getEmail());
			assertEquals(t, tAux);

		} catch (DuplicateInstanceException e) {
			assert (false);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	@Test
	public void registrarRecolector() {
		trabajadorDto.setTipo(TipoTrabajador.RECOLEC);

		try {
			Trabajador t = trabajadorService.registrar(trabajadorDto, APP_URL);
			Trabajador tAux = trabajadorService.buscarTrabajadorEmail(t.getEmail());
			assertEquals(t, tAux);

		} catch (DuplicateInstanceException e) {
			assert (false);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	@Test(expected = NullPointerException.class)
	public void registroCamposIncompletos() throws DuplicateInstanceException, InstanceNotFoundException {
		trabajadorDto = new TrabajadorFormDto();
		trabajadorService.registrar(trabajadorDto, APP_URL);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarTrabajadorNoExistente() throws InstanceNotFoundException {
		trabajadorService.buscarTrabajador(NON_EXISTENT_USER_PROFILE_ID);
	}

	@Test(expected = DuplicateInstanceException.class)
	public void trabajadorDuplicado() throws DuplicateInstanceException {
		try {
			trabajadorService.registrar(trabajadorDto, APP_URL);
		} catch (DuplicateInstanceException e) {
			assert (false);
		}
		trabajadorService.registrar(trabajadorDuplicado, APP_URL);
	}

}
