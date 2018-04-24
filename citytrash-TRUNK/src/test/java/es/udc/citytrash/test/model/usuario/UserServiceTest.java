package es.udc.citytrash.test.model.usuario;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.controller.util.dtos.TrabajadorRegistroFormDto;
import es.udc.citytrash.controller.util.dtos.TrabajadorUpdateFormDto;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.usuarioService.UsuarioService;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.Idioma;
import es.udc.citytrash.util.enums.TipoTrabajador;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserServiceTest {

	final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

	Trabajador trabajadorAdmin;
	Trabajador trabajadorRecolec;
	Trabajador trabajadorConductor;

	TrabajadorRegistroFormDto frmRegistro_ADMIN;
	TrabajadorRegistroFormDto frmRegistro_CONDUCT;
	TrabajadorRegistroFormDto frmRegistro_RECOLEC;

	private final Idioma T_IDIOMA_ADMIN = Idioma.en;
	private final Idioma T_IDIOMA_CONDUCT = Idioma.es;
	private final Idioma T_IDIOMA_RECOLECTOR = Idioma.en;

	private final String T_EMAIL_ADMIN = "administradortfg@citytrash.com";
	private final String T_EMAIL_RECOLEC = "recolector_tfg@citytrash.com";
	private final String T_EMAIL_CONDUCT = "conductor.tfg@citytrash.com";
	private final String T_DOC_ADMIN = "56118277W";
	private final String T_DOC_RECOLECT = "40097094K";
	private final String T_DOC_CONCUCT = "21558310L";
	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final String T_EMAIL_NO_EXISTENTE = "-1";
	private final String APELLIDO = "PRUEBA";

	@Autowired
	UsuarioService cuentaServicio;

	@Autowired
	private TrabajadorService trabajadorService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	TrabajadorDao trabajadorDao;

	@Before
	public void startUp() throws DuplicateInstanceException {

		frmRegistro_ADMIN = new TrabajadorRegistroFormDto(T_DOC_ADMIN, "admin", APELLIDO, T_EMAIL_ADMIN, new Date(),
				TipoTrabajador.ADMIN, T_IDIOMA_ADMIN);

		frmRegistro_CONDUCT = new TrabajadorRegistroFormDto(T_DOC_CONCUCT, "conductor", APELLIDO, T_EMAIL_CONDUCT,
				new Date(), TipoTrabajador.CONDUCT, T_IDIOMA_CONDUCT);

		frmRegistro_RECOLEC = new TrabajadorRegistroFormDto(T_DOC_RECOLECT, "recolector", APELLIDO, T_EMAIL_RECOLEC,
				new Date(), TipoTrabajador.RECOLEC, T_IDIOMA_RECOLECTOR);

		trabajadorAdmin = trabajadorService.registrar(frmRegistro_ADMIN);
		trabajadorConductor = trabajadorService.registrar(frmRegistro_CONDUCT);
		trabajadorRecolec = trabajadorService.registrar(frmRegistro_RECOLEC);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarTrabajadorPorEmailNoExistente() throws InstanceNotFoundException {
		cuentaServicio.buscarTrabajadorPorEmail(T_EMAIL_NO_EXISTENTE);
	}

	@Test
	public void buscarTrabajadorPorEmailExistente() throws InstanceNotFoundException {
		Trabajador tEncontrado = cuentaServicio.buscarTrabajadorPorEmail(trabajadorConductor.getEmail());
		assertEquals(tEncontrado, trabajadorConductor);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void recuperarCuentaPorEmailNoExistente() throws InstanceNotFoundException {
		cuentaServicio.recuperarCuenta(T_EMAIL_NO_EXISTENTE);
	}

	@Test(expected = DisabledException.class)
	public void recuperarCuentaPorEmailExistentePeroDeshabilitada()
			throws InstanceNotFoundException, DisabledException {

		cuentaServicio.recuperarCuenta(T_EMAIL_ADMIN);
		trabajadorAdmin.setActiveWorker(false);
		trabajadorDao.guardar(trabajadorAdmin);
		cuentaServicio.recuperarCuenta(T_EMAIL_ADMIN);
	}

	@Test
	public void recuperarCuentaPorEmailExistente() throws InstanceNotFoundException, DisabledException {
		String token = trabajadorAdmin.getToken();
		Calendar fechaVencimiento = trabajadorAdmin.getFechaExpiracionToken();

		cuentaServicio.recuperarCuenta(trabajadorAdmin.getEmail());
		assertNotEquals(token, trabajadorAdmin.getToken());
		assertNotEquals(fechaVencimiento, trabajadorAdmin.getFechaExpiracionToken());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void cambiarIdiomaEmailNoExistente() throws InstanceNotFoundException {
		cuentaServicio.cambiarIdioma(T_EMAIL_NO_EXISTENTE, Idioma.es);
	}

	@Test
	public void cambiarIdiomaEmailExistente() throws InstanceNotFoundException {
		Idioma idiomaAntes = trabajadorAdmin.getIdioma();
		Idioma idiomaDespues = null;

		switch (idiomaAntes) {
		case en:
			idiomaDespues = Idioma.es;
		case es:
			idiomaDespues = Idioma.en;
		default:
			idiomaDespues = Idioma.es;
		}

		cuentaServicio.cambiarIdioma(trabajadorAdmin.getEmail(), idiomaDespues);
		assertNotEquals(idiomaAntes, trabajadorAdmin.getIdioma());
		assertEquals(idiomaDespues, trabajadorAdmin.getIdioma());

		idiomaAntes = trabajadorAdmin.getIdioma();
		cuentaServicio.cambiarIdioma(trabajadorAdmin.getEmail(), idiomaAntes);
		assertEquals(idiomaAntes, trabajadorAdmin.getIdioma());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void obtenerIdiomaPreferenciaCuentaNoExistente() throws InstanceNotFoundException {
		cuentaServicio.obtenerIdiomaPreferencia(NON_EXISTENT_USER_PROFILE_ID);
	}

	@Test
	public void obtenerIdiomaPreferenciaCuentaExistente() throws InstanceNotFoundException {
		assertEquals(T_IDIOMA_CONDUCT, cuentaServicio.obtenerIdiomaPreferencia(trabajadorConductor.getId()));
		assertEquals(T_IDIOMA_ADMIN, cuentaServicio.obtenerIdiomaPreferencia(trabajadorAdmin.getId()));
		assertEquals(T_IDIOMA_RECOLECTOR, cuentaServicio.obtenerIdiomaPreferencia(trabajadorRecolec.getId()));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void reiniciarPasswordNoExistente() throws InstanceNotFoundException, PasswordInvalidException {
		cuentaServicio.reiniciarPassword(NON_EXISTENT_USER_PROFILE_ID, "hola");
	}

	@Test
	public void reiniciarPassword() throws InstanceNotFoundException, PasswordInvalidException {
		String nuevaContraseña = "hoLa";
		cuentaServicio.reiniciarPassword(trabajadorConductor.getId(), nuevaContraseña);
		assertTrue(passwordEncoder.matches(nuevaContraseña, trabajadorConductor.getPassword()));
		assertFalse(passwordEncoder.matches(nuevaContraseña + "_", trabajadorConductor.getPassword()));
	}
}