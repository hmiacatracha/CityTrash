package es.udc.citytrash.test.model.camion;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import es.udc.citytrash.controller.util.dtos.camion.CamionDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloTipoDeBasuraDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionRegistroDto;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camion.CamionDao;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModelo.CamionModeloDao;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;
import es.udc.citytrash.model.camionService.CamionService;
import es.udc.citytrash.model.contenedor.ContenedorDao;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.trabajador.Administrador;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.Recolector;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.model.util.excepciones.InactiveResourceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;
import es.udc.citytrash.util.enums.CampoBusqPalabrasClavesCamion;
import es.udc.citytrash.util.enums.Idioma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class CamionServiceTest {

	@Autowired
	CamionService cService1;

	@Autowired
	CamionDao camionDao;

	@Autowired
	CamionModeloDao modeloDao;

	@Autowired
	TipoDeBasuraDao tipoDao;

	@Autowired
	TrabajadorDao tDao;

	final Logger logger = LoggerFactory.getLogger(CamionServiceTest.class);

	private final int TIPO_ID_NO_EXISTENTE = -1;
	private final String TIPO_NOMBRE_1 = "TIPO1";
	private final String TIPO_NOMBRE_2 = "TIPO2";
	private final String TIPO_NOMBRE_3 = "TIPO3";
	private final String TIPO_COLOR_1 = "00000";
	private final String TIPO_COLOR_2 = "FFFFF";
	private final String TIPO_COLOR_3 = "000F0";
	List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();
	private final int MODELO_ID_NO_EXISTENTE = -1;
	private final String CM_NOMBRE_1 = "MODELO1";
	private final String CM_NOMBRE_2 = "MODELO2";
	private final String CM_NOMBRE_3 = "MODELO3";

	private final long CAMION_ID_NO_EXISTENTE = -1;
	private final String C_NOMBRE_1 = "CAMION1";
	private final String C_NOMBRE_2 = "CAMION2";
	TipoDeBasura tipo1;
	TipoDeBasura tipo2;
	TipoDeBasura tipo3;

	Administrador a1;
	Conductor c1;
	Recolector r1;

	Administrador a2;
	Conductor c2;
	Recolector r2;

	@Before
	public void startUp() {
		tipo1 = new TipoDeBasura(TIPO_NOMBRE_1, TIPO_COLOR_1);
		tipo2 = new TipoDeBasura(TIPO_NOMBRE_2, TIPO_COLOR_2);
		tipo3 = new TipoDeBasura(TIPO_NOMBRE_3, TIPO_COLOR_3);

		tipoDao.guardar(tipo1);
		tipoDao.guardar(tipo2);
		tipoDao.guardar(tipo3);

		tipos.add(tipo1);
		tipos.add(tipo2);
		tipos.add(tipo3);

		a1 = new Administrador("03077018D", "ADMIN", "ADMIN", "administrador1@gmail.com", Idioma.es);
		tDao.guardar(a1);

		a2 = new Administrador("10441351H", "ADMIN", "ADMIN", "administrador2@gmail.com", Idioma.es);
		tDao.guardar(a2);

		c1 = new Conductor("08337662R", "conductor", "conduct", "conductor1@gmail.com", Idioma.en);
		tDao.guardar(c1);

		c2 = new Conductor("17600290T", "conductor", "conduct", "conductor2@gmail.com", Idioma.en);
		tDao.guardar(c2);

		r1 = new Recolector("01025555P", "recolector", "apellidos", "recolector1@gmail.com", Idioma.es);
		tDao.guardar(r1);

		r2 = new Recolector("03548986V", "recolector", "apellidos", "recolector2@gmail.com", Idioma.es);
		tDao.guardar(r2);
	}

	/*
	 * private CamionModeloTipoDeBasuraDto convertToDto(CamionModeloTipoDeBasura
	 * tipo) { CamionModeloTipoDeBasuraDto postDto = new
	 * CamionModeloTipoDeBasuraDto(); postDto.setNuevo(false);
	 * postDto.setCapacidad(tipo.getCapacidad());
	 * postDto.setIdTipo(tipo.getTipo().getId()); return postDto; }
	 */

	public CamionModeloTipoDeBasuraDto getTipoDeBasura(int tipo, BigDecimal capacidad) {
		return new CamionModeloTipoDeBasuraDto(tipo, capacidad);
	}

	public CamionModeloDto getFormRegistroModelo(String nombre, BigDecimal volumenTolva, BigDecimal ancho,
			BigDecimal altura, BigDecimal distancia, BigDecimal longitud, Integer pma,
			List<CamionModeloTipoDeBasuraDto> tiposDeBasura) {

		CamionModeloDto modelo = new CamionModeloDto();
		modelo.setNombre(nombre);
		modelo.setVolumenTolva(volumenTolva);
		modelo.setAncho(ancho);
		modelo.setAltura(altura);
		modelo.setDistancia(distancia);
		modelo.setLongitud(longitud);
		modelo.setPma(pma);
		modelo.setListaTiposDeBasura(tiposDeBasura);

		return modelo;
	}

	public CamionRegistroDto getFormRegistroCamion(String vin, String nombre, String matricula, int modeloCamion,
			Long recogedorUno, Long recogedorDos, Long conductorPrincipal, Long conductorSuplente, boolean activo) {

		CamionRegistroDto camion = new CamionRegistroDto();
		camion.setVin(vin);
		camion.setNombre(nombre);
		camion.setMatricula(matricula);
		camion.setModeloCamion(modeloCamion);
		camion.setRecogedorUno(recogedorUno);
		camion.setRecogedorDos(recogedorDos);
		camion.setConductorPrincipal(conductorPrincipal);
		camion.setConductorSuplente(conductorSuplente);
		camion.setActivo(activo);
		return camion;
	}

	@Test
	public void buscarTiposDeBasura()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		assertEquals(tipos.size(), tipoDao.buscarTodos().size());
		assertTrue(tipos.contains(tipo1));
		assertTrue(tipos.contains(tipo2));
		assertTrue(tipos.contains(tipo3));
		TipoDeBasura tipoSinGuardar = new TipoDeBasura(TIPO_NOMBRE_3, TIPO_COLOR_3);
		assertFalse(tipos.contains(tipoSinGuardar));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarModeloNoEncontrado() throws InstanceNotFoundException {
		cService1.buscarModeloById(TIPO_ID_NO_EXISTENTE);
	}

	@Test
	public void buscarModeloOk() throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		List<CamionModeloTipoDeBasura> tiposBasura = new ArrayList<CamionModeloTipoDeBasura>();

		CamionModeloDto dto = getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null);

		CamionModelo modelo1 = cService1.registrarModelo(dto);
		CamionModelo tipoEncontrado1 = cService1.buscarModeloById(modelo1.getId());
		assertEquals(tipoEncontrado1.getModelo(), modelo1.getModelo());
		assertEquals(tipoEncontrado1, modelo1);
	}

	@Test
	public void registrarModeloOk() throws InstanceNotFoundException, DuplicateInstanceException {
		CamionModeloDto registroForm = getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16,
				null);
		CamionModelo modelo = cService1.registrarModelo(registroForm);
		assertEquals(modelo.getModelo(), registroForm.getNombre());
	}

	@Test(expected = DuplicateInstanceException.class)
	public void registrarModeloDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		CamionModeloDto registroForm1 = getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16,
				null);
		cService1.registrarModelo(registroForm1);

		CamionModeloDto registroForm2 = getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("1.2"), new BigDecimal("4.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 8, null);
		cService1.registrarModelo(registroForm2);
	}

	@Test
	public void buscarModelosPageableVacios() throws FormBusquedaException {
		int psize = 1;
		int pnum = 0;
		String campoSort = "id";
		CamionModeloFormBusq formBusqueda = new CamionModeloFormBusq();
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<CamionModelo> page = cService1.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().isEmpty());
	}

	@Test
	public void buscarModelosPageable()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		CamionModeloFormBusq formBusqueda = new CamionModeloFormBusq();

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionModelo modelo2 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<CamionModelo> page;

		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == modelos.size());
		} catch (FormBusquedaException e) {
			assert (false);
		}

		pnum = 0;
		psize = 1;
		pageable = createPageRequest(pnum, psize, "id");
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == psize);
			assertEquals(page.getContent().get(0), modelo1);
		} catch (FormBusquedaException e) {
			assert (false);
		}
		pnum = 1;
		psize = 1;
		pageable = createPageRequest(pnum, psize, "id");
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == psize);
			assertEquals(page.getContent().get(0), modelo2);
		} catch (FormBusquedaException e) {
			assert (false);
		}

	}

	@Test
	public void buscarModelosPageableByPalabrasClave()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionModelo modelo2 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		CamionModeloFormBusq formBusqueda = new CamionModeloFormBusq();
		Page<CamionModelo> page;
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == modelos.size());
		} catch (FormBusquedaException e1) {
			assert (false);
		}

		/* Con form busqueda palabras claves */
		pnum = 0;
		psize = modelos.size();
		formBusqueda = new CamionModeloFormBusq();
		formBusqueda.setPalabrasClaveModelo("MoDElo");
		pageable = createPageRequest(pnum, psize, "id");
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == 2);
			assertTrue(page.getContent().contains(modelo2));
			assertTrue(page.getContent().contains(modelo1));
		} catch (FormBusquedaException e) {
			assert (false);
		}
	}

	@Test
	public void buscarModelosPageableByTipo()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";

		CamionModeloTipoDeBasuraDto tipoBasura1 = getTipoDeBasura(tipo1.getId(), new BigDecimal("2.2"));
		CamionModeloTipoDeBasuraDto tipoBasura2 = getTipoDeBasura(tipo2.getId(), new BigDecimal("1.2"));
		CamionModeloTipoDeBasuraDto tipoBasura3 = getTipoDeBasura(tipo3.getId(), new BigDecimal("0.2"));

		List<CamionModeloTipoDeBasuraDto> tiposBasuraModelo1 = new ArrayList<>();
		tiposBasuraModelo1.add(tipoBasura1);
		tiposBasuraModelo1.add(tipoBasura2);

		List<CamionModeloTipoDeBasuraDto> tiposBasuraModelo2 = new ArrayList<>();
		tiposBasuraModelo2.add(tipoBasura2);
		tiposBasuraModelo2.add(tipoBasura3);

		List<CamionModeloTipoDeBasuraDto> tiposBasuraModelo3 = new ArrayList<>();
		tiposBasuraModelo3.add(tipoBasura3);

		CamionModeloFormBusq formBusqueda = new CamionModeloFormBusq();

		CamionModelo modelo1 = cService1.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16,
				tiposBasuraModelo1));

		CamionModelo modelo2 = cService1.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16,
				tiposBasuraModelo3));

		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<CamionModelo> page;
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == modelos.size());
		} catch (FormBusquedaException e) {
			assert (false);
		}

		/* Con form busqueda tipo */
		List<Integer> tiposBasuraInt = new ArrayList<Integer>();
		tiposBasuraInt.add(tipo3.getId());
		formBusqueda.setTipos(tiposBasuraInt);
		pnum = 0;
		psize = modelos.size();
		pageable = createPageRequest(pnum, psize, "id");
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == 1);
			assertEquals(page.getContent().get(0), modelo2);
		} catch (FormBusquedaException e) {
			assert (false);
		}
	}

	@Test
	public void buscarModelosPageableByPalabrasClaveYTipo()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		CamionModeloTipoDeBasuraDto tipoBasura1 = getTipoDeBasura(tipo1.getId(), new BigDecimal("2.2"));
		CamionModeloTipoDeBasuraDto tipoBasura2 = getTipoDeBasura(tipo2.getId(), new BigDecimal("1.2"));
		CamionModeloTipoDeBasuraDto tipoBasura3 = getTipoDeBasura(tipo3.getId(), new BigDecimal("0.2"));

		List<CamionModeloTipoDeBasuraDto> tiposBasuraModelo1 = new ArrayList<>();
		tiposBasuraModelo1.add(tipoBasura1);
		tiposBasuraModelo1.add(tipoBasura2);

		List<CamionModeloTipoDeBasuraDto> tiposBasuraModelo2 = new ArrayList<>();
		tiposBasuraModelo2.add(tipoBasura2);
		tiposBasuraModelo2.add(tipoBasura3);

		List<CamionModeloTipoDeBasuraDto> tiposBasuraModelo3 = new ArrayList<>();
		tiposBasuraModelo3.add(tipoBasura3);

		CamionModeloFormBusq formBusqueda = new CamionModeloFormBusq();

		CamionModelo modelo1 = cService1.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16,
				tiposBasuraModelo1));

		CamionModelo modelo2 = cService1.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16,
				tiposBasuraModelo2));

		CamionModelo modelo3 = cService1.registrarModelo(getFormRegistroModelo(CM_NOMBRE_3, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16,
				tiposBasuraModelo3));

		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		modelos.add(modelo3);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<CamionModelo> page;
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == modelos.size());
		} catch (FormBusquedaException e1) {
			assert (false);
		}

		/* Con form busqueda palabras claves y tipo */

		List<Integer> tiposBasuraInt = new ArrayList<Integer>();
		tiposBasuraInt.add(tipo2.getId());

		String modeloABuscar = "ModELO";
		formBusqueda.setPalabrasClaveModelo(modeloABuscar);
		formBusqueda.setTipos(tiposBasuraInt);
		pnum = 0;
		psize = modelos.size();
		pageable = createPageRequest(pnum, psize, "id");
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == 2);
			assertTrue(page.getContent().contains(modelo1));
			assertTrue(page.getContent().contains(modelo2));
		} catch (FormBusquedaException e) {
			assert (false);
		}

		/* Con form busqueda palabras claves y tipo */

		tiposBasuraInt = new ArrayList<Integer>();
		tiposBasuraInt.add(tipo2.getId());

		modeloABuscar = "ModELO2";
		formBusqueda.setPalabrasClaveModelo(modeloABuscar);
		formBusqueda.setTipos(tiposBasuraInt);
		pnum = 0;
		psize = modelos.size();
		pageable = createPageRequest(pnum, psize, "id");
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == 1);
			assertTrue(page.getContent().contains(modelo2));
		} catch (FormBusquedaException e) {

		}

		/* Con form busqueda palabras claves y tipo */
		tiposBasuraInt = new ArrayList<Integer>();
		tiposBasuraInt.add(tipo2.getId());
		modeloABuscar = "ModELO1";
		formBusqueda.setPalabrasClaveModelo(modeloABuscar);
		formBusqueda.setTipos(tiposBasuraInt);
		pnum = 0;
		psize = modelos.size();
		pageable = createPageRequest(pnum, psize, "id");
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == 1);
			assertTrue(page.getContent().contains(modelo1));
		} catch (FormBusquedaException e) {

		}
	}

	@Test
	public void buscarModelosPageableByPalabrasClaveYTipoVacio()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		CamionModeloFormBusq formBusqueda = new CamionModeloFormBusq();

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionModelo modelo2 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<CamionModelo> page;
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().size() == modelos.size());
		} catch (FormBusquedaException e1) {
			assert (false);
		}
		/* Con form busqueda palabras claves y tipo */
		List<Integer> tiposBasuraInt = new ArrayList<Integer>();
		tiposBasuraInt.add(tipo2.getId());
		tiposBasuraInt.add(TIPO_ID_NO_EXISTENTE);
		tiposBasuraInt.add(TIPO_ID_NO_EXISTENTE);
		formBusqueda.setTipos(tiposBasuraInt);
		formBusqueda.setPalabrasClaveModelo(modelo1.getModelo());
		pnum = 0;
		psize = modelos.size();
		pageable = createPageRequest(pnum, psize, "id");
		logger.info("PRINT BUSQUEDA ULTIMA");
		try {
			page = cService1.buscarModelos(pageable, formBusqueda);
			assertTrue(page.getContent().isEmpty());
		} catch (FormBusquedaException e) {
			assert (false);
		}

	}

	@Test
	public void modificarModeloOk()
			throws DuplicateInstanceException, InvalidFieldException, InstanceNotFoundException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		assertEquals(modelo1.getModelo(), CM_NOMBRE_1);
		CamionModeloDto modificarForm = new CamionModeloDto(modelo1);
		modificarForm.setNombre(CM_NOMBRE_2);
		BigDecimal altura = new BigDecimal("0.0");
		modificarForm.setAltura(altura);
		CamionModelo modeloModificado = cService1.modificarModelo(modificarForm);
		assertEquals(modelo1.getModelo(), CM_NOMBRE_2);
		assertEquals(modelo1.getAltura(), altura);
		assertEquals(modelo1, modeloModificado);
	}

	@Test(expected = DuplicateInstanceException.class)
	public void modificarModeloDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionModelo modelo2 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionModeloDto modificarForm = new CamionModeloDto(modelo1);
		modificarForm.setNombre(modelo2.getModelo());
		cService1.modificarModelo(modificarForm);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarTipoDeBasuraByModeloNoExistente()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		cService1.buscarTipoDeBasuraByModelo(MODELO_ID_NO_EXISTENTE);
	}

	@Test
	public void buscarTipoDeBasuraByModelo()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));
		/* TODO : FALTA INSERTAR TIPOS DE BASURA Y COMPARARLOS */
		List<CamionModeloTipoDeBasura> tiposEncontrados = cService1.buscarTipoDeBasuraByModelo(modelo1.getId());
		assertEquals(tiposEncontrados, modelo1.getTiposDeBasura());
	}

	@Test
	public void registrarCamionOk()
			throws DuplicateInstanceException, InvalidFieldException, InstanceNotFoundException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm = getFormRegistroCamion("WDDGF8BB3CR296129", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		Camion camion;
		try {
			camion = cService1.buscarCamionById(cService1.registrarCamion(registroCamionForm).getId());
			assertEquals(camion.getNombre(), registroCamionForm.getNombre());
			assertEquals(camion.getVin(), registroCamionForm.getVin());
			assertEquals(camion.getConductor().getId(), c1.getId());
			assertEquals(camion.getConductorSuplente().getId(), c2.getId());
			assertEquals(camion.getRecogedor1().getId(), r1.getId());
			assertEquals(camion.getRecogedor2().getId(), r2.getId());
			assertEquals(camion.getModeloCamion().getId(), modelo1.getId());
		} catch (InactiveResourceException e) {
			assertFalse(true);
		}
	}

	@Test(expected = DuplicateInstanceException.class)
	public void registrarCamionNombreDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm = getFormRegistroCamion("WDDGF8BB3CR296129", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		try {
			cService1.registrarCamion(registroCamionForm);
		} catch (InactiveResourceException e1) {
			assert (false);
		}

		CamionRegistroDto registroCamionForm2 = getFormRegistroCamion("1FUYTWEB7XHA73350", C_NOMBRE_1, "C-0004-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		try {
			cService1.registrarCamion(registroCamionForm2);
		} catch (InactiveResourceException e1) {
			assert (false);
		}
	}

	@Test(expected = DuplicateInstanceException.class)
	public void registrarCamionVinDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm = getFormRegistroCamion("WDDGF8BB3CR296129", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		try {
			cService1.registrarCamion(registroCamionForm);
		} catch (InactiveResourceException e1) {
			assert (false);
		}

		CamionRegistroDto registroCamionForm2 = getFormRegistroCamion("WDDGF8BB3CR296129", C_NOMBRE_2, "C-0004-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		try {
			cService1.registrarCamion(registroCamionForm2);
		} catch (InactiveResourceException e1) {
			assert (false);
		}
	}

	@Test(expected = DuplicateInstanceException.class)
	public void registrarCamionMatriculaDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		try {
			cService1.registrarCamion(registroCamionForm);
		} catch (InactiveResourceException e1) {
			assert (false);
		}

		CamionRegistroDto registroCamionForm2 = getFormRegistroCamion("WDDGF8BB3CR296129", C_NOMBRE_2, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		try {
			cService1.registrarCamion(registroCamionForm2);
		} catch (InactiveResourceException e1) {
			assert (false);
		}
	}

	@Test(expected = InactiveResourceException.class)
	public void registrarCamionConRecogedor1Inactivo() throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidFieldException, InactiveResourceException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		c1.setActiveWorker(false);
		tDao.guardar(r1);
		cService1.registrarCamion(registroCamionForm);
	}

	@Test(expected = InactiveResourceException.class)
	public void registrarCamionConRecogedor2Inactivo() throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidFieldException, InactiveResourceException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		c1.setActiveWorker(false);
		tDao.guardar(r2);
		cService1.registrarCamion(registroCamionForm);
	}

	@Test(expected = InactiveResourceException.class)
	public void registrarCamionConConductor1Inactivo() throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidFieldException, InactiveResourceException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		c1.setActiveWorker(false);
		tDao.guardar(c1);
		cService1.registrarCamion(registroCamionForm);
	}

	@Test(expected = InactiveResourceException.class)
	public void registrarCamionConConductor2Inactivo() throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidFieldException, InactiveResourceException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);

		c1.setActiveWorker(false);
		tDao.guardar(c2);
		cService1.registrarCamion(registroCamionForm);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void registrarCamionModeloNoExistente() throws InstanceNotFoundException, DuplicateInstanceException,
			InactiveResourceException, InvalidFieldException {
		CamionRegistroDto registroCamionForm = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				MODELO_ID_NO_EXISTENTE, r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		cService1.registrarCamion(registroCamionForm);
	}

	@Test
	public void buscarCamionesPageableVacios() throws FormBusquedaException {
		int psize = 1;
		int pnum = 0;
		String campoSort = "id";
		CamionFormBusq formBusqueda = new CamionFormBusq();
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<Camion> page = cService1.buscarCamiones(pageable, formBusqueda);
		assertTrue(page.getContent().isEmpty());
	}

	@Test
	public void buscarCamionesPageable() throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidFieldException, FormBusquedaException, InactiveResourceException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionModelo modelo2 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		List<Camion> camiones = new ArrayList<Camion>();

		CamionRegistroDto registroCamionForm1 = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion1 = cService1.registrarCamion(registroCamionForm1);
		cService1.buscarCamionById(camion1.getId());
		camiones.add(camion1);

		List<Camion> lcamiones = new ArrayList<Camion>();
		lcamiones = camionDao.buscarCamiones(null);
		assertTrue(lcamiones.size() == 1);

		CamionFormBusq formBusqueda = new CamionFormBusq();
		Pageable pageable = createPageRequest(pnum, 10, campoSort);
		logger.info("BUSCAR CAMIONES PAGEABLE");
		Page<Camion> page = cService1.buscarCamiones(pageable, formBusqueda);
		logger.info("BUSCAR CAMIONES PAGEABLE =>" + page.getContent().toString());
		assertTrue(page.getContent().size() == 1);

		CamionRegistroDto registroCamionForm2 = getFormRegistroCamion("1FMJU1FT4FEF19530", C_NOMBRE_2, "C-0004-A",
				modelo2.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion2 = cService1.registrarCamion(registroCamionForm2);
		camiones.add(camion2);

		formBusqueda = new CamionFormBusq();
		pageable = createPageRequest(pnum, camiones.size(), campoSort);
		page = cService1.buscarCamiones(pageable, formBusqueda);
		assertTrue(page.getContent().size() == camiones.size());
		assertTrue(page.getContent().contains(camion1));
		assertTrue(page.getContent().contains(camion2));
	}

	@Test
	public void buscarCamionesPageableByPalabrasClave() throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidFieldException, FormBusquedaException, InactiveResourceException {
		int pnum = 0;
		String campoSort = "id";

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionModelo modelo2 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		List<Camion> camiones = new ArrayList<Camion>();
		CamionRegistroDto registroCamionForm1 = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion1 = cService1.registrarCamion(registroCamionForm1);
		camiones.add(camion1);

		CamionRegistroDto registroCamionForm2 = getFormRegistroCamion("1FMJU1FT4FEF19530", C_NOMBRE_2, "C-0004-A",
				modelo2.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion2 = cService1.registrarCamion(registroCamionForm2);
		camiones.add(camion2);

		CamionFormBusq formBusqueda = new CamionFormBusq();
		formBusqueda.setCampo(CampoBusqPalabrasClavesCamion.nombre.toString());
		formBusqueda.setBuscar(C_NOMBRE_2);
		Pageable pageable = createPageRequest(pnum, camiones.size(), campoSort);
		Page<Camion> page = cService1.buscarCamiones(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertFalse(page.getContent().contains(camion1));
		assertTrue(page.getContent().contains(camion2));

		formBusqueda = new CamionFormBusq();
		formBusqueda.setCampo(CampoBusqPalabrasClavesCamion.vin.toString());
		formBusqueda.setBuscar("1MELM66L0SK671941");
		pageable = createPageRequest(pnum, camiones.size(), campoSort);
		page = cService1.buscarCamiones(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertFalse(page.getContent().contains(camion2));
		assertTrue(page.getContent().contains(camion1));

		formBusqueda = new CamionFormBusq();
		formBusqueda.setCampo(CampoBusqPalabrasClavesCamion.matricula.toString());
		formBusqueda.setBuscar("---------NO EXISTENTE ? ASSAO32R-------------");
		pageable = createPageRequest(pnum, camiones.size(), campoSort);
		page = cService1.buscarCamiones(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 0);
		assertFalse(page.getContent().contains(camion2));
		assertFalse(page.getContent().contains(camion1));
	}

	// @Test
	public void buscarCamionesPageableByTipoDeBasuras()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		/* TODO: FALTA BUSCA POR TIPOS */
	}

	@Test
	public void buscarCamionesPageableByModelo() throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidFieldException, FormBusquedaException, InactiveResourceException {
		int pnum = 0;
		String campoSort = "id";

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionModelo modelo2 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		List<Camion> camiones = new ArrayList<Camion>();

		CamionRegistroDto registroCamionForm1 = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion1 = cService1.registrarCamion(registroCamionForm1);
		camiones.add(camion1);

		CamionRegistroDto registroCamionForm2 = getFormRegistroCamion("1FMJU1FT4FEF19530", C_NOMBRE_2, "C-0004-A",
				modelo2.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion2 = cService1.registrarCamion(registroCamionForm2);
		camiones.add(camion2);

		CamionFormBusq formBusqueda = new CamionFormBusq();
		formBusqueda.setModelo(modelo1.getId());
		Pageable pageable = createPageRequest(pnum, camiones.size(), campoSort);
		Page<Camion> page = cService1.buscarCamiones(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertTrue(page.getContent().contains(camion1));

		formBusqueda = new CamionFormBusq();
		formBusqueda.setModelo(modelo2.getId());
		pageable = createPageRequest(pnum, camiones.size(), campoSort);
		page = cService1.buscarCamiones(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertTrue(page.getContent().contains(camion2));
	}

	// @Test
	public void buscarCamionesPageableByPalabrasClavesTipoYModelo()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		/* TODO: FALTARIA ESTE TAMBIEN => BUSQUEDA POR TIPO */
		// int pnum = 0;
		// String campoSort = "id";
		//
		// ContenedorModelo modelo1 =
		// cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new
		// BigDecimal("19.2"),
		// new BigDecimal("19.2"), new BigDecimal("19.2"), new
		// BigDecimal("19.2"), new BigDecimal("19.2"),
		// new BigDecimal("19.2"), tipo1.getId()));
		//
		// ContenedorModelo modelo2 =
		// cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new
		// BigDecimal("19.2"),
		// new BigDecimal("19.2"), new BigDecimal("19.2"), new
		// BigDecimal("19.2"), new BigDecimal("19.2"),
		// new BigDecimal("19.2"), tipo2.getId()));
		//
		// List<Contenedor> contenedores = new ArrayList<Contenedor>();
		// BigDecimal latitud = new BigDecimal("41.00001");
		// BigDecimal longitud = new BigDecimal("2.0232");
		// ContenedorRegistroDto registroContenedorForm1 =
		// getFormRegistroContenedor(C_NOMBRE_1, modelo1.getId(), null,
		// null, latitud, longitud, "Avenida buenos Aires 1", false);
		// Contenedor contenedor1 =
		// cService.registrarContenedor(registroContenedorForm1);
		// contenedores.add(contenedor1);
		//
		// latitud = new BigDecimal("41.00002");
		// longitud = new BigDecimal("2.0232");
		// ContenedorRegistroDto registroContenedorForm2 =
		// getFormRegistroContenedor(C_NOMBRE_2, modelo2.getId(), null,
		// null, latitud, longitud, "Avenida buenos Aires 2", true);
		// Contenedor contenedor2 =
		// cService.registrarContenedor(registroContenedorForm2);
		// contenedores.add(contenedor2);
		//
		// ContenedorFormBusq formBusqueda = new ContenedorFormBusq();
		// List<Integer> tiposInteger = new ArrayList<Integer>();
		// tiposInteger.add(tipo1.getId());
		// formBusqueda.setBuscar(C_NOMBRE_1);
		// formBusqueda.setModelo(modelo1.getId());
		// formBusqueda.setTiposDeBasura(tiposInteger);
		// Pageable pageable = createPageRequest(pnum, contenedores.size(),
		// campoSort);
		// Page<Contenedor> page = cService.buscarContenedores(pageable,
		// formBusqueda);
		// assertTrue(page.getContent().size() == 1);
		// assertTrue(page.getContent().contains(contenedor1));
		//
		// formBusqueda = new ContenedorFormBusq();
		// tiposInteger = new ArrayList<Integer>();
		// tiposInteger.add(tipo1.getId());
		// formBusqueda.setBuscar(contenedor1.getNombre().substring(0,
		// contenedor1.getNombre().length() - 2));
		// formBusqueda.setModelo(modelo2.getId());
		// formBusqueda.setTiposDeBasura(tiposInteger);
		// pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		// page = cService.buscarContenedores(pageable, formBusqueda);
		// assertTrue(page.getContent().size() == 0);
	}

	@Test
	public void modificarContenedorOk() throws DuplicateInstanceException, InvalidFieldException,
			InstanceNotFoundException, InactiveResourceException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm1 = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion1 = cService1.registrarCamion(registroCamionForm1);

		assertTrue(camion1.getActivo());
		CamionDto editarCamionForm1 = new CamionDto(camion1);
		editarCamionForm1.setActivo(false);
		cService1.modificarCamion(editarCamionForm1);
		Camion camionEncontrado = cService1.buscarCamionById(camion1.getId());
		assertEquals(camionEncontrado.getNombre(), editarCamionForm1.getNombre());
		assertFalse(camion1.getActivo());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void modificarCamionInstanceNotFoundExceptio() throws DuplicateInstanceException, InstanceNotFoundException,
			InactiveResourceException, InvalidFieldException {
		CamionDto editarCamionForm1 = new CamionDto();
		editarCamionForm1.setId(CAMION_ID_NO_EXISTENTE);
		editarCamionForm1.setNombre(C_NOMBRE_1);
		editarCamionForm1.setActivo(false);
		cService1.modificarCamion(editarCamionForm1);
	}

	@Test(expected = DuplicateInstanceException.class)
	public void modificarCamionDuplicado() throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidFieldException, InactiveResourceException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionModelo modelo2 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm1 = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion1 = cService1.registrarCamion(registroCamionForm1);

		CamionRegistroDto registroCamionForm2 = getFormRegistroCamion("1FMJU1FT4FEF19530", C_NOMBRE_2, "C-0004-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion2 = cService1.registrarCamion(registroCamionForm2);

		assertNotEquals(camion1.getNombre(), camion2.getNombre());

		CamionDto editarCamionForm1 = new CamionDto(camion2);
		editarCamionForm1.setNombre(C_NOMBRE_1);
		editarCamionForm1.setModeloCamion(modelo2.getId());
		cService1.modificarCamion(editarCamionForm1);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void modificarCamionByModeloNoExistente() throws InstanceNotFoundException, DuplicateInstanceException,
			InactiveResourceException, InvalidFieldException {

		CamionModelo modelo1 = cService1
				.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null));

		CamionRegistroDto registroCamionForm1 = getFormRegistroCamion("1MELM66L0SK671941", C_NOMBRE_1, "C-0003-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion1 = cService1.registrarCamion(registroCamionForm1);

		CamionRegistroDto registroCamionForm2 = getFormRegistroCamion("1FMJU1FT4FEF19530", C_NOMBRE_2, "C-0004-A",
				modelo1.getId(), r1.getId(), r2.getId(), c1.getId(), c2.getId(), true);
		Camion camion2 = cService1.registrarCamion(registroCamionForm2);

		assertNotEquals(camion1.getNombre(), camion2.getNombre());
		CamionDto editarCamionForm1 = new CamionDto(camion2);
		editarCamionForm1.setModeloCamion(MODELO_ID_NO_EXISTENTE);
		cService1.modificarCamion(editarCamionForm1);
	}

	// @Test(expected = InstanceNotFoundException.class)
	public void buscarTipoDeBasuraByContenedorNoExistente()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		// cService1.buscarTiposDeBasuraByContenedor(CAMION_ID_NO_EXISTENTE);
		/* TODO: FALTA ESTE */
	}

	private Pageable createPageRequest(int page, int size, String campoSort) {
		return new PageRequest(page, size, Sort.Direction.ASC, campoSort);
	}
}
