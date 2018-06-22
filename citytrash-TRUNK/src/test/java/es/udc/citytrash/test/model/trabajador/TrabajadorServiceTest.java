package es.udc.citytrash.test.model.trabajador;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorBusqFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorRegistroFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorUpdateFormDto;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.CampoBusqTrabajador;
import es.udc.citytrash.util.enums.Idioma;
import es.udc.citytrash.util.enums.TipoTrabajador;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class TrabajadorServiceTest {

	final Logger logger = LoggerFactory.getLogger(TrabajadorServiceTest.class);

	Trabajador trabajadorAdmin;
	Trabajador trabajadorRecolec;
	Trabajador trabajadorConductor;

	TrabajadorRegistroFormDto frmRegistro_ADMIN;
	TrabajadorRegistroFormDto frmRegistro_CONDUCT;
	TrabajadorRegistroFormDto frmRegistro_RECOLEC;

	TrabajadorUpdateFormDto frmEditar_ADMIN;
	TrabajadorUpdateFormDto frmEditar_CONDUCT;
	TrabajadorUpdateFormDto frmEditar_RECOLEC;

	private final Idioma T_IDIOMA_ADMIN = Idioma.en;
	private final Idioma T_IDIOMA_CONDUCT = Idioma.es;
	private final Idioma T_IDIOMA_RECOLECTOR = Idioma.en;

	private final String T_EMAIL_ADMIN = "administradortfg@citytrash.com";
	private final String T_EMAIL_RECOLEC = "recolector_tfg@citytrash.com";
	private final String T_EMAIL_CONDUCT = "conductor.tfg@citytrash.com";
	private final String T_TELEFONO = "981270122";
	private final String T_TELEFONO_ERRONEO = "ASBAS1012";
	private final String T_DOC_ADMIN = "56118277W";
	private final String T_DOC_RECOLECT = "40097094K";
	private final String T_DOC_CONCUCT = "21558310L";
	private final long T_ID_NO_EXISTENTE = -1;
	private final String T_DOC_NO_EXISTENTE = "-1";
	private final String T_EMAIL_NO_EXISTENTE = "-1";
	private final String NUM_CON_FORM_ERRONEO = "ASDA9213";
	private final String CP_CON_FORM_OK = "15001";
	private final String CP_CON_FORM_ERRONEO = "900804";
	private final String APELLIDO = "PRUEBA";

	@Autowired
	private TrabajadorService trabajadorService;

	@Autowired
	TrabajadorDao trabajadorDao;

	@Before
	public void startUp() {

		frmRegistro_ADMIN = rellenarRegistro(T_DOC_ADMIN, "admin", APELLIDO, T_EMAIL_ADMIN, new Date(),
				TipoTrabajador.ADMIN, T_IDIOMA_ADMIN);

		frmRegistro_CONDUCT = rellenarRegistro(T_DOC_CONCUCT, "conductor", APELLIDO, T_EMAIL_CONDUCT, new Date(),
				TipoTrabajador.CONDUCT, T_IDIOMA_CONDUCT);

		frmRegistro_RECOLEC = rellenarRegistro(T_DOC_RECOLECT, "recolector", APELLIDO, T_EMAIL_RECOLEC, new Date(),
				TipoTrabajador.RECOLEC, T_IDIOMA_RECOLECTOR);

	}

	private TrabajadorRegistroFormDto rellenarRegistro(String documento, String nombre, String apellidos, String email,
			Date fechaNacimiento, TipoTrabajador tipo, Idioma idioma) {
		return new TrabajadorRegistroFormDto(documento, nombre, apellidos, email, fechaNacimiento, tipo, idioma);
	}

	private TrabajadorUpdateFormDto rellenarActualizar(Trabajador t) {
		return new TrabajadorUpdateFormDto(t);
	}

	@Test
	public void registrarTrabajadorAdministrador() {
		Trabajador t = null;
		Trabajador tencontrado = null;
		try {
			t = trabajadorService.registrar(frmRegistro_ADMIN);
			tencontrado = trabajadorDao.buscarById(t.getId());
			assertEquals(t, tencontrado);
			assertNotNull(t.getToken());
			assertNotNull(t.getFechaExpiracionToken());
			assertNull(t.getPassword());
			assertTrue((t.getRol().equals(GlobalNames.ROL_ADMINISTRADOR)));
			assertTrue(t.isActiveWorker());
			assertFalse(t.isEnabledCount());

		} catch (DuplicateInstanceException e) {
			assert (false);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	@Test
	public void registrarTrabajadorRecolector() {
		Trabajador t = null;
		Trabajador tencontrado = null;
		try {
			t = trabajadorService.registrar(frmRegistro_RECOLEC);
			tencontrado = trabajadorDao.buscarById(t.getId());
			assertEquals(t, tencontrado);
			assertNotNull(t.getToken());
			assertNotNull(t.getFechaExpiracionToken());
			assertNull(t.getPassword());
			assertTrue((t.getRol().equals(GlobalNames.ROL_RECOLECTOR)));
			assertTrue(t.isActiveWorker());
			assertFalse(t.isEnabledCount());
		} catch (DuplicateInstanceException e) {
			assert (false);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	@Test
	public void registrarTrabajadorConductor() {
		Trabajador t = null;
		Trabajador tencontrado = null;
		try {
			t = trabajadorService.registrar(frmRegistro_CONDUCT);
			tencontrado = trabajadorDao.buscarById(t.getId());
			assertEquals(t, tencontrado);
			assertNotNull(t.getToken());
			assertNotNull(t.getFechaExpiracionToken());
			assertNull(t.getPassword());
			assertTrue((t.getRol().equals(GlobalNames.ROL_CONDUCTOR)));
			assertTrue(t.isActiveWorker());
			assertFalse(t.isEnabledCount());

		} catch (DuplicateInstanceException e) {
			assert (false);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	@Test(expected = DuplicateInstanceException.class)
	public void registrarTrabajadorDuplicadoEmail() throws DuplicateInstanceException {

		trabajadorService.registrar(frmRegistro_CONDUCT);
		/* duplicamos el email */
		frmRegistro_ADMIN.setEmail(frmRegistro_CONDUCT.getEmail());
		trabajadorService.registrar(frmRegistro_ADMIN);
	}

	@Test(expected = DuplicateInstanceException.class)
	public void registrarTrabajadorDuplicadoDocumento() throws DuplicateInstanceException {

		trabajadorService.registrar(frmRegistro_ADMIN);
		/* duplicamos el documento (dni/nie del trabajador) */
		frmRegistro_CONDUCT.setDocumento(frmRegistro_ADMIN.getDocumento());
		trabajadorService.registrar(frmRegistro_CONDUCT);
	}

	//@Test
	public void verificarFormatoFormRegistroError() {
		Trabajador t = null;
		try {

			frmRegistro_CONDUCT.setPiso(NUM_CON_FORM_ERRONEO);
			frmRegistro_CONDUCT.setCp(NUM_CON_FORM_ERRONEO);
			frmRegistro_CONDUCT.setTelefono(NUM_CON_FORM_ERRONEO);
			frmRegistro_CONDUCT.setNumero(NUM_CON_FORM_ERRONEO);
			t = trabajadorService.registrar(frmRegistro_CONDUCT);
			assertTrue(t.getDocId().equals(frmRegistro_CONDUCT.getDocumento()));
			assertNull(t.getPiso());
			assertNull(t.getCp());
			assertNull(t.getTelefonos());
			assertNull(t.getNumero());

		} catch (DuplicateInstanceException e) {
			assert (false);
		}
	}

	@Test(expected = ConstraintViolationException.class)
	public void formatoCPIncorrecto() throws DuplicateInstanceException {
		frmRegistro_CONDUCT.setCp(CP_CON_FORM_ERRONEO);
		trabajadorService.registrar(frmRegistro_CONDUCT);
	}

	@Test(expected = ConstraintViolationException.class)
	public void formatoNombreIncorrecto() throws DuplicateInstanceException {
		frmRegistro_CONDUCT.setNombre("");
		trabajadorService.registrar(frmRegistro_CONDUCT);
	}

	@Test(expected = ConstraintViolationException.class)
	public void formatoApellidosIncorrecto() throws DuplicateInstanceException {
		frmRegistro_CONDUCT.setApellidos("");
		trabajadorService.registrar(frmRegistro_CONDUCT);
	}

	@Test(expected = ConstraintViolationException.class)
	public void formatoEmailIncorrecto() throws DuplicateInstanceException {
		frmRegistro_CONDUCT.setEmail("");
		trabajadorService.registrar(frmRegistro_CONDUCT);
	}

	@Test
	public void actualizarDatosTrabajador() throws DuplicateInstanceException, InstanceNotFoundException {

		frmRegistro_CONDUCT.setEmail(T_EMAIL_CONDUCT);
		Trabajador t = trabajadorService.registrar(frmRegistro_CONDUCT);
		frmEditar_CONDUCT = rellenarActualizar(t);
		frmEditar_CONDUCT.setEmail(T_EMAIL_RECOLEC);
		frmEditar_CONDUCT.setCp(CP_CON_FORM_OK);
		assertEquals(t, trabajadorService.buscarTrabajadorByEmail(T_EMAIL_CONDUCT));
		trabajadorService.modificarTrabajador(frmEditar_CONDUCT);
		assertEquals(t, trabajadorService.buscarTrabajadorByEmail(T_EMAIL_RECOLEC));
		assertEquals(t.getCp().toString(), CP_CON_FORM_OK);

		if (t instanceof Conductor)
			assert (true);
		else
			assert (false);
		assertTrue(t.getRol().equals(GlobalNames.ROL_CONDUCTOR));
	}

	@Test(expected = DuplicateInstanceException.class)
	public void actualizarDatosEmailDuplicado() throws DuplicateInstanceException, InstanceNotFoundException {

		frmRegistro_CONDUCT.setEmail(T_EMAIL_CONDUCT);
		Trabajador t = trabajadorService.registrar(frmRegistro_CONDUCT);
		assertEquals(t, trabajadorService.buscarTrabajadorByEmail(T_EMAIL_CONDUCT));
		Trabajador t1 = trabajadorService.registrar(frmRegistro_RECOLEC);
		assertEquals(t1, trabajadorService.buscarTrabajadorByEmail(T_EMAIL_RECOLEC));

		frmEditar_CONDUCT = rellenarActualizar(t);
		frmEditar_CONDUCT.setEmail(T_EMAIL_RECOLEC);
		frmEditar_CONDUCT.setCp(CP_CON_FORM_OK);
		trabajadorService.modificarTrabajador(frmEditar_CONDUCT);
	}

	@Test(expected = DuplicateInstanceException.class)
	public void actualizarDatosDocumentoDuplicado() throws DuplicateInstanceException, InstanceNotFoundException {

		frmRegistro_CONDUCT.setEmail(T_EMAIL_CONDUCT);
		Trabajador t = trabajadorService.registrar(frmRegistro_CONDUCT);
		assertEquals(t, trabajadorService.buscarTrabajadorByEmail(T_EMAIL_CONDUCT));
		Trabajador t1 = trabajadorService.registrar(frmRegistro_RECOLEC);
		assertEquals(t1, trabajadorService.buscarTrabajadorByEmail(T_EMAIL_RECOLEC));

		frmEditar_CONDUCT = rellenarActualizar(t);
		frmEditar_CONDUCT.setDocumento(T_DOC_RECOLECT);
		trabajadorService.modificarTrabajador(frmEditar_CONDUCT);
	}

	@Test
	public void actualizarDatosTrabajadorTipo() throws DuplicateInstanceException, InstanceNotFoundException {

		frmRegistro_CONDUCT.setEmail(T_EMAIL_CONDUCT);
		Trabajador t = trabajadorService.registrar(frmRegistro_CONDUCT);

		if (t instanceof Conductor)
			assert (true);
		else
			assert (false);
		assertTrue(t.getRol().equals(GlobalNames.ROL_CONDUCTOR));

		frmEditar_CONDUCT = rellenarActualizar(t);
		frmEditar_CONDUCT.setEmail(T_EMAIL_ADMIN);
		frmEditar_CONDUCT.setTipo(TipoTrabajador.ADMIN);
		assertEquals(t, trabajadorService.buscarTrabajadorByEmail(T_EMAIL_CONDUCT));
		trabajadorService.modificarTrabajador(frmEditar_CONDUCT);
		assertEquals(t, trabajadorService.buscarTrabajadorByEmail(T_EMAIL_ADMIN));

		/*
		 * if (t instanceof Administrador) assert (true); else assert (false);
		 * assertTrue(t.getRol().equals(GlobalNames.ROL_ADMINISTRADOR));
		 */
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarTrabajadorIdNoExistente() throws InstanceNotFoundException {
		trabajadorService.buscarTrabajador(T_ID_NO_EXISTENTE);
	}

	public void buscarTrabajadorIdExistente() throws InstanceNotFoundException, DuplicateInstanceException {
		Trabajador t = trabajadorService.registrar(frmRegistro_ADMIN);
		trabajadorService.buscarTrabajador(t.getId());
	}

	public void buscarTrabajadorEmailExistente() throws InstanceNotFoundException, DuplicateInstanceException {
		trabajadorService.registrar(frmRegistro_ADMIN);
		trabajadorService.buscarTrabajadorByEmail(T_EMAIL_ADMIN);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarTrabajadorEmailNOExistente() throws InstanceNotFoundException, DuplicateInstanceException {
		trabajadorService.registrar(frmRegistro_ADMIN);
		trabajadorService.registrar(frmRegistro_CONDUCT);
		trabajadorService.registrar(frmRegistro_RECOLEC);
		trabajadorService.buscarTrabajadorByEmail(T_EMAIL_NO_EXISTENTE);
	}

	@Test
	public void buscarTrabajadorDocumentoExistente() throws InstanceNotFoundException, DuplicateInstanceException {
		trabajadorService.registrar(frmRegistro_ADMIN);
		trabajadorService.buscarTrabajadorDocumento(T_DOC_ADMIN);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarTrabajadorDocumentoNoExistente() throws InstanceNotFoundException {
		trabajadorService.buscarTrabajadorDocumento(T_DOC_NO_EXISTENTE);
	}

	@Test
	public void buscarTrabajadoresVacio() {
		int psize = 1;
		int pnum = 0;
		String campoSort = "id";
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		/*
		 * Comprobamos que no haya trabajadores en la base de datos, porque aun
		 * no hemos registrado ninguno
		 */
		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, true);
		assertTrue(page.getContent().isEmpty());
	}

	@Test
	public void buscarTrabajadoresNoVacio() {
		int psize = 3;
		int pnum = 0;
		String campoSort = "id";
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		/*
		 * Comprobamos que no haya trabajadores en la base de datos, porque aun
		 * no hemos registrado ninguno
		 */
		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, true);
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();

		try {
			// Añadimos tres trabajadores en la base de datos
			trabajadores.add(trabajadorService.registrar(frmRegistro_ADMIN));
			trabajadores.add(trabajadorService.registrar(frmRegistro_CONDUCT));
			trabajadores.add(trabajadorService.registrar(frmRegistro_RECOLEC));

		} catch (DuplicateInstanceException e) {
			assert (false);
		}
		/*
		 * Comprobamos que ahora que hemos registrado trabajadores si buscamos
		 * aparezcan
		 */
		page = trabajadorService.buscarTrabajadores(pageable, true);
		assertFalse(page.getContent().isEmpty());
		assertEquals(psize, page.getContent().size());
	}

	@Test
	public void buscarTrabajadoresTodosConFrmBusqueda() throws FormBusquedaException {
		int psize = 3;
		int pnum = 0;
		int elementosTotales = 0;
		String campoSort = "id";
		TrabajadorBusqFormDto formBusqueda = new TrabajadorBusqFormDto();
		Pageable pageable = createPageRequest(pnum, psize, campoSort);

		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, true);
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		assertTrue(page.getContent().isEmpty());

		try {
			// Añadimos tres trabajadores en la base de datos
			trabajadores.add(trabajadorService.registrar(frmRegistro_ADMIN));
			trabajadores.add(trabajadorService.registrar(frmRegistro_CONDUCT));
			trabajadores.add(trabajadorService.registrar(frmRegistro_RECOLEC));

		} catch (DuplicateInstanceException e) {
			assert (false);
		}
		/* Realizamos busqueda sin ningun filtro de busqueda */
		logger.info("paso1");
		formBusqueda.setBuscar("");
		formBusqueda.setTipo(TipoTrabajador.NONE.name());
		formBusqueda.setCampo(CampoBusqTrabajador.nombre.name());
		formBusqueda.setMostrarTodosLosTrabajadores(true);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		elementosTotales = trabajadorDao.buscarTodos().size();
		assertFalse(page.getContent().isEmpty());
		assertEquals(elementosTotales, page.getSize());
	}

	@Test
	public void buscarTrabajadoresEnUnaSolaPagina() {
		int psize = 1;
		int pnum = 0;
		int elementosTotales = 0;
		int elementos = 0;
		String campoSort = "id";
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		/*
		 * Comprobamos que no haya trabajadores en la base de datos, porque aun
		 * no hemos registrado ninguno
		 */
		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, true);
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();

		try {
			// Añadimos tres trabajadores en la base de datos
			trabajadores.add(trabajadorService.registrar(frmRegistro_ADMIN));
			trabajadores.add(trabajadorService.registrar(frmRegistro_CONDUCT));
			trabajadores.add(trabajadorService.registrar(frmRegistro_RECOLEC));

		} catch (DuplicateInstanceException e) {
			assert (false);
		}
		// recuperamos todos los items en una sola pagina
		elementos = 0;
		elementosTotales = trabajadorDao.buscarTodos().size();
		psize = elementosTotales;

		pnum = 0;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, true);
		logger.error("elementos 1 => " + elementos);
		assertEquals(elementos, page.getNumberOfElements());

		pnum = 1;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, true);
		logger.error("elementos 2=> " + elementos);
		assertEquals(elementos, page.getNumberOfElements());

		pnum = 2;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, true);
		logger.error("elementos 3=> " + elementos);
		assertEquals(elementos, page.getNumberOfElements());

	}

	@Test
	public void buscarTrabajadoresEnUnaSolaPaginaConFrmBusqueda() throws FormBusquedaException {
		int psize = 3;
		int pnum = 0;
		int elementosTotales = 0;
		int elementos = 0;
		String campoSort = "id";
		TrabajadorBusqFormDto formBusqueda = new TrabajadorBusqFormDto();
		Pageable pageable = createPageRequest(pnum, psize, campoSort);

		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, true);
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();

		try {
			// Añadimos tres trabajadores en la base de datos
			trabajadores.add(trabajadorService.registrar(frmRegistro_ADMIN));
			trabajadores.add(trabajadorService.registrar(frmRegistro_CONDUCT));
			trabajadores.add(trabajadorService.registrar(frmRegistro_RECOLEC));

		} catch (DuplicateInstanceException e) {
			assert (false);
		}
		// recuperamos todos los items en una sola pagina
		formBusqueda.setBuscar(APELLIDO);
		formBusqueda.setTipo(TipoTrabajador.NONE.name());
		formBusqueda.setCampo(CampoBusqTrabajador.nombre.name());
		formBusqueda.setMostrarTodosLosTrabajadores(true);
		elementosTotales = 3;
		psize = elementosTotales;
		pnum = 0;

		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);

		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(3, page.getNumberOfElements());

		pnum = 1;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		logger.error("elementos 2=> " + elementos);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(0, page.getNumberOfElements());

		formBusqueda.setMostrarTodosLosTrabajadores(false);
		elementosTotales = 3;
		psize = elementosTotales;
		pnum = 0;

		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);

		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(3, page.getNumberOfElements());

	}

	public void buscarTrabajadoresEnVariasPaginas() {
		int psize = 1;
		int pnum = 0;
		int elementosTotales = 0;
		int elementos = 0;
		String campoSort = "id";
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		/*
		 * Comprobamos que no haya trabajadores en la base de datos, porque aun
		 * no hemos registrado ninguno
		 */
		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, true);
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();

		try {
			// Añadimos tres trabajadores en la base de datos
			trabajadores.add(trabajadorService.registrar(frmRegistro_ADMIN));
			trabajadores.add(trabajadorService.registrar(frmRegistro_CONDUCT));
			trabajadores.add(trabajadorService.registrar(frmRegistro_RECOLEC));

		} catch (DuplicateInstanceException e) {
			assert (false);
		}

		/* Varias paginas */
		elementosTotales = 3;
		psize = elementosTotales - 1;

		pnum = 0;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, true);
		assertEquals(elementos, page.getNumberOfElements());

		pnum = 1;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, true);
		assertEquals(elementos, page.getNumberOfElements());

		pnum = 2;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, true);
		assertEquals(elementos, page.getNumberOfElements());
	}

	@Test
	public void buscarTrabajadoresPorApellidosEnVariasPaginasConFrmBusquedad() throws FormBusquedaException {
		int psize = 0;
		int pnum = 0;
		int elementosTotales = 0;
		int elementos = 0;
		String campoSort = "id";
		TrabajadorBusqFormDto formBusqueda = new TrabajadorBusqFormDto();

		try {
			// Añadimos tres trabajadores en la base de datos
			trabajadorService.registrar(frmRegistro_ADMIN);
			trabajadorService.registrar(frmRegistro_CONDUCT);
			trabajadorService.registrar(frmRegistro_RECOLEC);

		} catch (DuplicateInstanceException e) {
			assert (false);
		}

		formBusqueda.setBuscar(APELLIDO);
		formBusqueda.setTipo(TipoTrabajador.NONE.name());
		formBusqueda.setCampo(CampoBusqTrabajador.nombre.name());
		formBusqueda.setMostrarTodosLosTrabajadores(true);

		/* Varias paginas buscar por apellido */
		elementosTotales = 3;
		psize = 1;

		pnum = 0;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		Pageable pageable = createPageRequest(pnum, psize, campoSort);

		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(1, page.getNumberOfElements());

		pnum = 1;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(1, page.getNumberOfElements());

		pnum = 2;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(1, page.getNumberOfElements());

		pnum = 3;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(0, page.getNumberOfElements());

		/* Varias paginas buscar por apellido */
		formBusqueda.setMostrarTodosLosTrabajadores(false);
		elementosTotales = 3;
		psize = 1;

		pnum = 0;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);

		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(1, page.getNumberOfElements());

	}

	@Test
	public void buscarTrabajadorePorEmailExistenteEnUnaSolaPaginaConFrmBusquedad() throws FormBusquedaException {
		int psize = 0;
		int pnum = 0;
		int elementosTotales = 0;
		int elementos = 0;
		String campoSort = "id";
		TrabajadorBusqFormDto formBusqueda = new TrabajadorBusqFormDto();

		try {
			// Añadimos tres trabajadores en la base de datos
			trabajadorService.registrar(frmRegistro_ADMIN);
			trabajadorService.registrar(frmRegistro_CONDUCT);
			trabajadorService.registrar(frmRegistro_RECOLEC);

		} catch (DuplicateInstanceException e) {
			assert (false);
		}

		/* Buscamos por email existente */
		formBusqueda.setBuscar("tfg");
		formBusqueda.setTipo(TipoTrabajador.NONE.name());
		formBusqueda.setCampo(CampoBusqTrabajador.email.name());
		formBusqueda.setMostrarTodosLosTrabajadores(true);
		elementosTotales = 3;
		psize = elementosTotales;
		pnum = 0;

		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);

		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(3, page.getNumberOfElements());

		pnum = 1;
		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(0, page.getNumberOfElements());

	}

	@Test
	public void buscarTrabajadoresTelefonoConFrmBusquedad() throws FormBusquedaException {
		int psize = 0;
		int pnum = 0;
		String campoSort = "id";
		TrabajadorBusqFormDto formBusqueda = new TrabajadorBusqFormDto();

		try {
			// Añadimos tres trabajadores en la base de datos
			trabajadorService.registrar(frmRegistro_ADMIN);
			trabajadorService.registrar(frmRegistro_CONDUCT);

		} catch (DuplicateInstanceException e) {
			assert (false);
		}

		formBusqueda.setBuscar(T_TELEFONO);
		formBusqueda.setTipo(TipoTrabajador.NONE.name());
		formBusqueda.setCampo(CampoBusqTrabajador.telefono.name());
		formBusqueda.setMostrarTodosLosTrabajadores(true);
		psize = 3;
		pnum = 0;

		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(0, page.getNumberOfElements());

		/* Añadimos un telefono y lo buscamos sin filtro de trabajador */

		try {
			frmRegistro_RECOLEC.setTelefono(T_TELEFONO);
			trabajadorService.registrar(frmRegistro_RECOLEC);
		} catch (DuplicateInstanceException e) {
			assert (false);
		}

		formBusqueda.setBuscar(T_TELEFONO);
		formBusqueda.setTipo(TipoTrabajador.NONE.name());
		formBusqueda.setCampo(CampoBusqTrabajador.telefono.name());
		psize = 3;
		pnum = 0;

		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(1, page.getNumberOfElements());

		/* telefono no existente con palabras claves no numericas */
		formBusqueda.setBuscar(T_TELEFONO_ERRONEO);
		formBusqueda.setTipo(TipoTrabajador.NONE.name());
		formBusqueda.setCampo(CampoBusqTrabajador.telefono.name());
		psize = 3;
		pnum = 0;

		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(0, page.getNumberOfElements());

		/* Buscamos el telefono con filtro del tipo de trabajador */
		formBusqueda.setBuscar(T_TELEFONO);
		formBusqueda.setTipo(TipoTrabajador.ADMIN.name());
		formBusqueda.setCampo(CampoBusqTrabajador.telefono.name());
		psize = 3;
		pnum = 0;

		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(0, page.getNumberOfElements());

		formBusqueda.setTipo(TipoTrabajador.RECOLEC.name());
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(1, page.getNumberOfElements());

	}

	@Test
	public void buscarTrabajadoresPorDocumentoNoExistenteConFrmBusquedad() throws FormBusquedaException {
		int psize = 0;
		int pnum = 0;
		int elementosTotales = 0;
		int elementos = 0;
		String campoSort = "id";
		TrabajadorBusqFormDto formBusqueda = new TrabajadorBusqFormDto();

		try {
			// Añadimos tres trabajadores en la base de datos
			trabajadorService.registrar(frmRegistro_ADMIN);
			trabajadorService.registrar(frmRegistro_CONDUCT);
			trabajadorService.registrar(frmRegistro_RECOLEC);

		} catch (DuplicateInstanceException e) {
			assert (false);
		}

		/* Buscamos por documento no existente */
		logger.info("paso5");
		formBusqueda.setBuscar(T_DOC_NO_EXISTENTE);
		formBusqueda.setTipo(TipoTrabajador.NONE.name());
		formBusqueda.setCampo(CampoBusqTrabajador.documento.name());
		formBusqueda.setMostrarTodosLosTrabajadores(true);
		elementosTotales = 0;
		psize = 3;
		pnum = 0;

		elementos = ((pnum + 1) * psize <= elementosTotales) ? psize
				: ((pnum + 1) * psize - elementosTotales) >= psize ? 0 : ((pnum + 1) * psize - elementosTotales);
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(0, page.getNumberOfElements());
	}

	@Test
	public void buscarTrabajadoresPorDocumentoExistenteYTipoConFrmBusquedad() throws FormBusquedaException {
		int psize = 0;
		int pnum = 0;
		int elementosTotales = 0;
		int elementos = 0;
		String campoSort = "id";
		TrabajadorBusqFormDto formBusqueda = new TrabajadorBusqFormDto();

		try {
			trabajadorService.registrar(frmRegistro_ADMIN);
			trabajadorService.registrar(frmRegistro_CONDUCT);
			trabajadorService.registrar(frmRegistro_RECOLEC);

		} catch (DuplicateInstanceException e) {
			assert (false);
		}

		/* Buscamos por documento existente y sin tipo */
		formBusqueda.setBuscar(T_DOC_ADMIN);
		formBusqueda.setTipo(TipoTrabajador.NONE.getValue());
		formBusqueda.setCampo(CampoBusqTrabajador.documento.toString());
		formBusqueda.setMostrarTodosLosTrabajadores(true);
		elementosTotales = 1;
		psize = 3;
		pnum = 0;

		elementos = ((pnum + 1) * Math.min(psize, elementosTotales) <= elementosTotales)
				? Math.min(psize, elementosTotales)
				: ((pnum + 1) * Math.min(psize, elementosTotales) - elementosTotales) >= Math.min(psize,
						elementosTotales) ? 0 : ((pnum + 1) * Math.min(psize, elementosTotales) - elementosTotales);
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<Trabajador> page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(1, page.getNumberOfElements());

		// Buscamos por documento existente y tipo recolector
		formBusqueda.setTipo(TipoTrabajador.NONE.name());
		formBusqueda.setCampo(CampoBusqTrabajador.documento.name());
		formBusqueda.setBuscar(T_DOC_ADMIN);

		logger.error("tipoTrabajador RECOLECT => " + TipoTrabajador.RECOLEC.name());
		formBusqueda.setTipo(TipoTrabajador.RECOLEC.name());
		logger.error("TIPOCampo RECOLECT => " + CampoBusqTrabajador.documento.toString());
		formBusqueda.setCampo(CampoBusqTrabajador.documento.toString());
		elementosTotales = 0;
		psize = 3;
		pnum = 0;

		elementos = ((pnum + 1) * Math.min(psize, elementosTotales) <= elementosTotales)
				? Math.min(psize, elementosTotales)
				: ((pnum + 1) * Math.min(psize, elementosTotales) - elementosTotales) >= Math.min(psize,
						elementosTotales) ? 0 : ((pnum + 1) * Math.min(psize, elementosTotales) - elementosTotales);
		logger.error("form de busqueda =>" + formBusqueda.toString());
		pageable = createPageRequest(pnum, psize, campoSort);
		page = trabajadorService.buscarTrabajadores(pageable, formBusqueda);
		assertEquals(elementos, page.getNumberOfElements());
		assertEquals(0, page.getNumberOfElements());

	}

	@Test(expected = FormBusquedaException.class)
	public void buscarTrabajadoresErrorFrmBusquedad() throws FormBusquedaException {
		String campoSort = "id";
		TrabajadorBusqFormDto formBusqueda = new TrabajadorBusqFormDto();

		try {
			trabajadorService.registrar(frmRegistro_ADMIN);
			trabajadorService.registrar(frmRegistro_CONDUCT);
			trabajadorService.registrar(frmRegistro_RECOLEC);

		} catch (DuplicateInstanceException e) {
			assert (false);
		}

		/* Buscamos por documento existente y sin tipo */
		formBusqueda.setBuscar(T_DOC_ADMIN);
		formBusqueda.setTipo("---");
		formBusqueda.setCampo("----");
		Pageable pageable = createPageRequest(0, 3, campoSort);
		trabajadorService.buscarTrabajadores(pageable, formBusqueda);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void darDeBajaATrabajadorNoExistente() throws InstanceNotFoundException {
		trabajadorService.desactivarTrabajador(T_ID_NO_EXISTENTE);
	}

	@Test
	public void darDeBajaATrabajadoExistente() throws InstanceNotFoundException, DuplicateInstanceException {
		Trabajador t = trabajadorService.registrar(frmRegistro_ADMIN);
		trabajadorService.desactivarTrabajador(t.getId());
		assertFalse(t.isActiveWorker());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void activarATrabajadorNoExistente() throws InstanceNotFoundException {
		trabajadorService.activarTrabajador(T_ID_NO_EXISTENTE);
	}

	@Test
	public void activarTrabajadoExistente() throws InstanceNotFoundException, DuplicateInstanceException {
		Trabajador t = trabajadorService.registrar(frmRegistro_ADMIN);
		trabajadorService.activarTrabajador(t.getId());
		assertTrue(t.isActiveWorker());
	}

	private Pageable createPageRequest(int page, int size, String campoSort) {
		return new PageRequest(page, size, Sort.Direction.ASC, campoSort);
	}
}
