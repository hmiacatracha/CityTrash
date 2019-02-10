package es.udc.citytrash.test.model.ruta;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloRegistroDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorRegistroDto;
import es.udc.citytrash.model.alerta.AlertaDao;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camion.CamionDao;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModelo.CamionModeloDao;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedor.ContenedorDao;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorModelo.ContenedorModeloDao;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.ruta.RutaDao;
import es.udc.citytrash.model.rutaDiaria.RutaDiariaDao;
import es.udc.citytrash.model.rutaDiariaContenedores.RutaDiariaContenedoresDao;
import es.udc.citytrash.model.rutaService.RutaService;
import es.udc.citytrash.model.sensorValor.ValorDao;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.trabajador.Administrador;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.Recolector;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;
import es.udc.citytrash.test.model.contenedor.ContenedorServiceTest;
import es.udc.citytrash.util.enums.Idioma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class RutaServiceTest {

	/*@Autowired
	TrabajadorDao tDao;

	@Autowired
	CamionDao camionDao;

	@Autowired
	CamionModeloDao camionModeloDao;

	@Autowired
	TipoDeBasuraDao tipoDao;

	@Autowired
	ContenedorDao contenedorDao;

	@Autowired
	ContenedorModeloDao contenedorModeloDao;

	@Autowired
	ValorDao valorDao;

	@Autowired
	RutaDiariaContenedoresDao rutaDiariaContenedoresDao;

	@Autowired
	AlertaDao alertaDao;

	@Autowired
	RutaService rutaService;

	final Logger logger = LoggerFactory.getLogger(ContenedorServiceTest.class);

	private final int RUTA_DIARIA_ID_NO_EXISTENTE = -1;
	private final int RUTA_ID_NO_EXISTENTE = -1;

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
	private final String CAMION_NOMBRE_1 = "CAMION1";
	private final String CAMION_NOMBRE_2 = "CAMION2";
	private final String CAMION_NOMBRE_3 = "CAMION3";

	private final String CONTENEDOR_NOMBRE_1 = "CONTENEDOR1";
	private final String CONTENEDOR_NOMBRE_2 = "CONTENEDOR2";
	private final String CONTENEDOR_NOMBRE_3 = "CONTENEDOR3";
	private final String CONTENEDOR_NOMBRE_4 = "CONTENEDOR4";
	private final String CONTENEDOR_NOMBRE_5 = "CONTENEDOR5";
	private final String CONTENEDOR_NOMBRE_6 = "CONTENEDOR6";
	private final String CONTENEDOR_NOMBRE_7 = "CONTENEDOR7";
	private final String CONTENEDOR_NOMBRE_8 = "CONTENEDOR8";
	private final String CONTENEDOR_NOMBRE_9 = "CONTENEDOR9";
	private final String CONTENEDOR_NOMBRE_10 = "CONTENEDOR10";

	TipoDeBasura tipo1;
	TipoDeBasura tipo2;
	TipoDeBasura tipo3;

	Administrador a1;
	Conductor c1;
	Recolector r1;

	Administrador a2;
	Conductor c2;
	Recolector r2;

	CamionModelo camionModeloCamion1;
	CamionModelo camionModeloCamion2;

	Camion camion1;
	Camion camion2;
	Camion camion3;

	ContenedorModelo contenedorModeloCamion1;
	ContenedorModelo contenedorModeloCamion2;

	Contenedor contenedor1;
	Contenedor contenedor2;
	Contenedor contenedor3;
	Contenedor contenedor4;
	Contenedor contenedor5;
	Contenedor contenedor6;
	Contenedor contenedor7;
	Contenedor contenedor8;
	Contenedor contenedor9;
	Contenedor contenedor10;

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

		camionModeloCamion1 = getCamionModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null);
		camionModeloDao.guardar(camionModeloCamion1);

		List<CamionModeloTipoDeBasura> tiposDeBasuraModelo1 = new ArrayList<CamionModeloTipoDeBasura>();
		tiposDeBasuraModelo1.add(new CamionModeloTipoDeBasura(camionModeloCamion1, tipo1, new BigDecimal("100")));
		tiposDeBasuraModelo1.add(new CamionModeloTipoDeBasura(camionModeloCamion1, tipo2, new BigDecimal("50")));

		camionModeloCamion1.setTiposDeBasura(tiposDeBasuraModelo1);
		camionModeloDao.guardar(camionModeloCamion1);

		camionModeloCamion2 = getCamionModelo(CM_NOMBRE_2, new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), 16, null);

		List<CamionModeloTipoDeBasura> tiposDeBasuraModelo2 = new ArrayList<CamionModeloTipoDeBasura>();
		tiposDeBasuraModelo2.add(new CamionModeloTipoDeBasura(camionModeloCamion1, tipo2, new BigDecimal("100")));
		tiposDeBasuraModelo2.add(new CamionModeloTipoDeBasura(camionModeloCamion1, tipo3, new BigDecimal("50")));

		camionModeloCamion2.setTiposDeBasura(tiposDeBasuraModelo2);
		camionModeloDao.guardar(camionModeloCamion2);

		camion1 = getCamion("WDDGF8BB3CR296129", CAMION_NOMBRE_1, "C-0001-A", camionModeloCamion1, r1, r2, c1, c2,
				true);

		camion2 = getCamion("WDDGF8BB3CR296130", CAMION_NOMBRE_2, "C-0002-A", camionModeloCamion2, r1, r2, c1, c2,
				true);

		camion3 = getCamion("WDDGF8BB3CR296131", CAMION_NOMBRE_3, "C-0003-A", camionModeloCamion2, r2, r2, c2, c2,
				true);

		camionDao.guardar(camion1);
		camionDao.guardar(camion2);
		camionDao.guardar(camion3);

		contenedorModeloCamion1 = contenedorModeloDao.guardar(
				getContenedorModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), tipo1));

		contenedorModeloCamion2 = contenedorModeloDao.guardar(
				getContenedorModelo(CM_NOMBRE_2, new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
						new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), tipo2));

		contenedor1 = getContenedor(CONTENEDOR_NOMBRE_1, contenedorModeloCamion1, null, null,
				new BigDecimal("41.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedor2 = getContenedor(CONTENEDOR_NOMBRE_2, contenedorModeloCamion1, null, null,
				new BigDecimal("40.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedor3 = getContenedor(CONTENEDOR_NOMBRE_3, contenedorModeloCamion1, null, null,
				new BigDecimal("40.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedor4 = getContenedor(CONTENEDOR_NOMBRE_4, contenedorModeloCamion1, null, null,
				new BigDecimal("40.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedor5 = getContenedor(CONTENEDOR_NOMBRE_5, contenedorModeloCamion1, null, null,
				new BigDecimal("40.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedor6 = getContenedor(CONTENEDOR_NOMBRE_6, contenedorModeloCamion1, null, null,
				new BigDecimal("40.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedor7 = getContenedor(CONTENEDOR_NOMBRE_7, contenedorModeloCamion1, null, null,
				new BigDecimal("40.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedor8 = getContenedor(CONTENEDOR_NOMBRE_8, contenedorModeloCamion1, null, null,
				new BigDecimal("40.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedor9 = getContenedor(CONTENEDOR_NOMBRE_9, contenedorModeloCamion1, null, null,
				new BigDecimal("40.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedor3 = getContenedor(CONTENEDOR_NOMBRE_10, contenedorModeloCamion1, null, null,
				new BigDecimal("40.00002"), new BigDecimal("2.0232"), "Avenida buenos Aires 1", true);

		contenedorDao.guardar(contenedor1);
		contenedorDao.guardar(contenedor2);
		contenedorDao.guardar(contenedor3);
		contenedorDao.guardar(contenedor4);
		contenedorDao.guardar(contenedor5);
		contenedorDao.guardar(contenedor6);
		contenedorDao.guardar(contenedor7);
		contenedorDao.guardar(contenedor8);
		contenedorDao.guardar(contenedor9);
		contenedorDao.guardar(contenedor10);

	}

	/**
	 * 
	 * @param nombre
	 * @param volumenTolva
	 * @param ancho
	 * @param altura
	 * @param distancia
	 * @param longitud
	 * @param pma
	 * @param tiposDeBasura
	 * @return
	 */
	

	@Autowired
	ContenedorService cService;

	@Autowired
	ContenedorDao contenedorDao;

	@Autowired
	ContenedorModeloDao modeloDao;

	@Autowired
	TipoDeBasuraDao tipoDao;

	final Logger logger = LoggerFactory.getLogger(ContenedorServiceTest.class);

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

	private final int CONTENEDOR_ID_NO_EXISTENTE = -1;
	private final String C_NOMBRE_1 = "CONTENEDOR1";
	private final String C_NOMBRE_2 = "CONTENEDOR2";
	TipoDeBasura tipo1;
	TipoDeBasura tipo2;
	TipoDeBasura tipo3;

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
	}

	public ContenedorModeloRegistroDto getFormRegistroModelo(String nombre, BigDecimal altura, BigDecimal anchura,
			BigDecimal capacidadNominal, BigDecimal cargaNominal, BigDecimal pesoVacio, BigDecimal profundidad,
			int tipo) {
		ContenedorModeloRegistroDto modeloRegistro = new ContenedorModeloRegistroDto();
		modeloRegistro.setAltura(altura);
		modeloRegistro.setAncho(anchura);
		modeloRegistro.setCapacidadNominal(capacidadNominal);
		modeloRegistro.setCargaNominal(cargaNominal);
		modeloRegistro.setNombre(nombre);
		modeloRegistro.setPesoVacio(pesoVacio);
		modeloRegistro.setProfundidad(profundidad);
		modeloRegistro.setTipo(tipo);
		return modeloRegistro;
	}

	public ContenedorRegistroDto getFormRegistroContenedor(String nombre, int modeloId, Date fechaAlta, Date fechaBaja,
			BigDecimal latitud, BigDecimal longitud, String localizacion, boolean activo) {
		ContenedorRegistroDto ContenedorRegistroDto = new ContenedorRegistroDto();
		ContenedorRegistroDto.setNombre(nombre);
		ContenedorRegistroDto.setModeloContenedor(modeloId);
		ContenedorRegistroDto.setFechaAlta(fechaAlta);
		ContenedorRegistroDto.setFechaBaja(fechaBaja);
		ContenedorRegistroDto.setLatitud(latitud);
		ContenedorRegistroDto.setLongitud(longitud);
		ContenedorRegistroDto.setLocalizacion(localizacion);
		ContenedorRegistroDto.setActivo(activo);
		return ContenedorRegistroDto;
	}

	@Test
	public void buscarTiposDeBasura()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		assertEquals(tipos.size(), cService.buscarTiposDeBasura().size());
		assertTrue(tipos.contains(tipo1));
		assertTrue(tipos.contains(tipo2));
		assertTrue(tipos.contains(tipo3));
		TipoDeBasura tipoSinGuardar = new TipoDeBasura(TIPO_NOMBRE_3, TIPO_COLOR_3);
		assertFalse(tipos.contains(tipoSinGuardar));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarModeloNoEncontrado() throws InstanceNotFoundException {
		cService.buscarModeloById(TIPO_ID_NO_EXISTENTE);
	}

	@Test
	public void buscarModeloOk() throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));
		ContenedorModelo tipoEncontrado1 = cService.buscarModeloById(modelo1.getId());
		assertEquals(tipoEncontrado1.getModelo(), modelo1.getModelo());
		assertEquals(tipoEncontrado1, modelo1);
	}

	@Test
	public void registrarModeloOk() throws DuplicateInstanceException, InvalidFieldException {
		ContenedorModeloRegistroDto registroForm = getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId());
		ContenedorModelo modelo = cService.registrarModelo(registroForm);
		assertEquals(modelo.getModelo(), registroForm.getNombre());
	}

	@Test(expected = DuplicateInstanceException.class)
	public void registrarModeloDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				tipo1.getId()));

		cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("18.2"),
				new BigDecimal("18.2"), new BigDecimal("19.2"), new BigDecimal("14.2"), new BigDecimal("19.2"),
				tipo2.getId()));
	}

	@Test
	public void registrarModeloInvalidFieldException() throws InstanceNotFoundException, DuplicateInstanceException {
		try {
			cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
					new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
					TIPO_ID_NO_EXISTENTE));
			assert (false);
		} catch (InvalidFieldException e) {
			assert (true);
		}

		try {
			BigDecimal cargaNominal = new BigDecimal(-1);
			cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"),
					new BigDecimal("19.2"), cargaNominal, new BigDecimal("19.2"), new BigDecimal("19.2"),
					tipo1.getId()));
			assert (false);
		} catch (InvalidFieldException e) {
			assert (true);
		}

		try {
			BigDecimal capacidadNominal = new BigDecimal(-1);
			cService.registrarModelo(
					getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"), new BigDecimal("19.2"), capacidadNominal,
							new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), tipo1.getId()));
			assert (false);
		} catch (InvalidFieldException e) {
			assert (true);
		}
	}

	@Test
	public void buscarModelosPageableVacios() {
		int psize = 1;
		int pnum = 0;
		String campoSort = "id";
		ContenedorModeloFormBusq formBusqueda = new ContenedorModeloFormBusq();
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<ContenedorModelo> page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().isEmpty());
	}

	@Test
	public void buscarModelosPageable()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		ContenedorModeloFormBusq formBusqueda = new ContenedorModeloFormBusq();

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));
		ContenedorModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo2.getId()));

		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<ContenedorModelo> page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == modelos.size());

		pnum = 0;
		psize = 1;
		pageable = createPageRequest(pnum, psize, "id");
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == psize);
		assertEquals(page.getContent().get(0), modelo1);

		pnum = 1;
		psize = 1;
		pageable = createPageRequest(pnum, psize, "id");
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == psize);
		assertEquals(page.getContent().get(0), modelo2);

	}

	@Test
	public void buscarModelosPageableByPalabrasClave()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		ContenedorModeloFormBusq formBusqueda = new ContenedorModeloFormBusq();

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));
		ContenedorModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo2.getId()));

		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<ContenedorModelo> page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == modelos.size());

		/* Con form busqueda palabras claves */
		pnum = 0;
		psize = modelos.size();
		formBusqueda.setPalabrasClaveModelo(modelo2.getModelo());
		pageable = createPageRequest(pnum, psize, "id");
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertEquals(page.getContent().get(0), modelo2);

	}

	@Test
	public void buscarModelosPageableByTipo()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		ContenedorModeloFormBusq formBusqueda = new ContenedorModeloFormBusq();

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));
		ContenedorModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo2.getId()));

		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<ContenedorModelo> page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == modelos.size());

		/* Con form busqueda tipo */
		List<Integer> tiposBasuraInt = new ArrayList<Integer>();
		tiposBasuraInt.add(tipo1.getId());
		formBusqueda.setTipos(tiposBasuraInt);
		formBusqueda.setPalabrasClaveModelo(null);
		pnum = 0;
		psize = modelos.size();
		pageable = createPageRequest(pnum, psize, "id");
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertEquals(page.getContent().get(0), modelo1);

	}

	@Test
	public void buscarModelosPageableByPalabrasClaveYTipo()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		ContenedorModeloFormBusq formBusqueda = new ContenedorModeloFormBusq();

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));
		ContenedorModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo2.getId()));

		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<ContenedorModelo> page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == modelos.size());

		/* Con form busqueda palabras claves y tipo */
		List<Integer> tiposBasuraInt = new ArrayList<Integer>();
		tiposBasuraInt.add(tipo2.getId());
		tiposBasuraInt.add(TIPO_ID_NO_EXISTENTE);
		tiposBasuraInt.add(TIPO_ID_NO_EXISTENTE);
		formBusqueda.setTipos(tiposBasuraInt);
		String nombreModelo2 = modelo2.getModelo().substring(0, modelo2.getModelo().length() - 2);
		formBusqueda.setPalabrasClaveModelo(nombreModelo2);
		pnum = 0;
		psize = modelos.size();
		pageable = createPageRequest(pnum, psize, "id");
		logger.info("PRINT BUSQUEDA ULTIMA");
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertEquals(page.getContent().get(0), modelo2);
	}

	@Test
	public void buscarModelosPageableByPalabrasClaveYTipoVacio()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		ContenedorModeloFormBusq formBusqueda = new ContenedorModeloFormBusq();

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));
		ContenedorModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo2.getId()));

		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<ContenedorModelo> page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == modelos.size());

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
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().isEmpty());
	}

	@Test
	public void modificarModeloOk()
			throws DuplicateInstanceException, InvalidFieldException, InstanceNotFoundException {
		ContenedorModeloRegistroDto registroForm = getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId());
		ContenedorModelo modelo = cService.registrarModelo(registroForm);

		assertEquals(modelo.getModelo(), CM_NOMBRE_1);
		ContenedorModeloEditarDto modificarForm = new ContenedorModeloEditarDto(modelo);
		modificarForm.setNombre(CM_NOMBRE_2);
		BigDecimal altura = new BigDecimal("0.0");
		modificarForm.setAltura(altura);
		ContenedorModelo modeloModificado = cService.modificarModelo(modificarForm);
		assertEquals(modelo.getModelo(), CM_NOMBRE_2);
		assertEquals(modelo.getAltura(), altura);
		assertEquals(modelo, modeloModificado);
	}

	@Test(expected = DuplicateInstanceException.class)
	public void modificarModeloDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));

		ContenedorModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("18.2"), new BigDecimal("18.2"), new BigDecimal("19.2"), new BigDecimal("14.2"),
				new BigDecimal("19.2"), tipo2.getId()));

		ContenedorModeloEditarDto modificarForm = new ContenedorModeloEditarDto(modelo1);
		modificarForm.setNombre(modelo2.getModelo());
		cService.modificarModelo(modificarForm);
	}

	@Test
	public void modificarModeloInvalidFieldException()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));

		ContenedorModeloEditarDto modificarForm = new ContenedorModeloEditarDto(modelo1);

		try {
			modificarForm.setTipo(TIPO_ID_NO_EXISTENTE);
			cService.modificarModelo(modificarForm);
			assert (false);
		} catch (InvalidFieldException e) {
			assert (true);
		}

		try {
			BigDecimal cargaNominal = new BigDecimal(-1);
			modificarForm.setCargaNominal(cargaNominal);
			cService.modificarModelo(modificarForm);
			assert (false);
		} catch (InvalidFieldException e) {
			assert (true);
		}

		try {
			BigDecimal capacidadNominal = new BigDecimal(-1);
			modificarForm.setCargaNominal(capacidadNominal);
			cService.modificarModelo(modificarForm);
			assert (false);
		} catch (InvalidFieldException e) {
			assert (true);
		}
	}

	@Test
	public void buscarTipoDeBasuraByModelo()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));
		// cService.buscarModeloById(modelo1.getId());
		assertEquals(tipo1, modelo1.getTipo());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void registrarContenedorOk()
			throws DuplicateInstanceException, InvalidFieldException, InstanceNotFoundException {
		ContenedorModeloRegistroDto registroModeloForm = getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId());
		ContenedorModelo modelo = cService.registrarModelo(registroModeloForm);

		BigDecimal latitud = new BigDecimal("41.00002");
		BigDecimal longitud = new BigDecimal("2.0232");

		ContenedorRegistroDto registroContenedorForm = getFormRegistroContenedor(C_NOMBRE_1, modelo.getId(), null, null,
				latitud, longitud, "Avenida buenos Aires", true);

		Contenedor contenedor = cService
				.buscarContenedorById(cService.registrarContenedor(registroContenedorForm).getId());
		assertEquals(contenedor.getNombre(), registroContenedorForm.getNombre());
		assertEquals(contenedor.getLatitud(), registroContenedorForm.getLatitud());
		assertEquals(contenedor.getLongitud(), registroContenedorForm.getLongitud());
		assertEquals(contenedor.getModelo(), modelo);
	}

	@Test(expected = DuplicateInstanceException.class)
	public void registrarContenedorDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		ContenedorModeloRegistroDto registroModeloForm = getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId());
		ContenedorModelo modelo = cService.registrarModelo(registroModeloForm);

		BigDecimal latitud = new BigDecimal("41.00002");
		BigDecimal longitud = new BigDecimal("2.0232");

		ContenedorRegistroDto registroContenedorForm1 = getFormRegistroContenedor(C_NOMBRE_1, modelo.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires", true);

		cService.registrarContenedor(registroContenedorForm1);

		latitud = new BigDecimal("41.00001");
		longitud = new BigDecimal("2.0235");
		ContenedorRegistroDto registroContenedorForm2 = getFormRegistroContenedor(C_NOMBRE_1, modelo.getId(), null,
				null, latitud, longitud, "", true);

		cService.registrarContenedor(registroContenedorForm2);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void registrarContenedorModeloNoExistente() throws InstanceNotFoundException, DuplicateInstanceException {
		BigDecimal latitud = new BigDecimal("41.00002");
		BigDecimal longitud = new BigDecimal("2.0232");
		ContenedorRegistroDto registroContenedorForm1 = getFormRegistroContenedor(C_NOMBRE_1, MODELO_ID_NO_EXISTENTE,
				null, null, latitud, longitud, "Avenida buenos Aires", true);
		cService.registrarContenedor(registroContenedorForm1);
	}

	@Test
	public void buscarContenedoresPageableVacios() {
		int psize = 1;
		int pnum = 0;
		String campoSort = "id";
		ContenedorFormBusq formBusqueda = new ContenedorFormBusq();
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<Contenedor> page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().isEmpty());
	}

	@Test
	public void buscarContenedoresPageable()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));

		ContenedorModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo2.getId()));

		List<Contenedor> contenedores = new ArrayList<Contenedor>();
		BigDecimal latitud = new BigDecimal("41.00002");
		BigDecimal longitud = new BigDecimal("2.0232");
		ContenedorRegistroDto registroContenedorForm1 = getFormRegistroContenedor(C_NOMBRE_1, modelo1.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires 1", true);
		Contenedor contenedor1 = cService.registrarContenedor(registroContenedorForm1);
		contenedores.add(contenedor1);

		ContenedorFormBusq formBusqueda = new ContenedorFormBusq();
		Pageable pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		Page<Contenedor> page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().size() == contenedores.size());

		latitud = new BigDecimal("41.00002");
		longitud = new BigDecimal("2.0232");
		ContenedorRegistroDto registroContenedorForm2 = getFormRegistroContenedor(C_NOMBRE_2, modelo2.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires 2", true);
		Contenedor contenedor2 = cService.registrarContenedor(registroContenedorForm2);
		contenedores.add(contenedor2);

		formBusqueda = new ContenedorFormBusq();
		pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().size() == contenedores.size());
		assertTrue(page.getContent().contains(contenedor1));
		assertTrue(page.getContent().contains(contenedor2));

	}

	@Test
	public void buscarContenedoresPageableByPalabrasClave()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		int pnum = 0;
		String campoSort = "id";

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));

		ContenedorModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo2.getId()));

		List<Contenedor> contenedores = new ArrayList<Contenedor>();
		BigDecimal latitud = new BigDecimal("41.00001");
		BigDecimal longitud = new BigDecimal("2.0232");
		ContenedorRegistroDto registroContenedorForm1 = getFormRegistroContenedor(C_NOMBRE_1, modelo1.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires 1", false);
		Contenedor contenedor1 = cService.registrarContenedor(registroContenedorForm1);
		contenedores.add(contenedor1);

		latitud = new BigDecimal("41.00002");
		longitud = new BigDecimal("2.0232");
		ContenedorRegistroDto registroContenedorForm2 = getFormRegistroContenedor(C_NOMBRE_2, modelo2.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires 2", true);
		Contenedor contenedor2 = cService.registrarContenedor(registroContenedorForm2);
		contenedores.add(contenedor2);

		ContenedorFormBusq formBusqueda = new ContenedorFormBusq();
		formBusqueda.setBuscar(C_NOMBRE_2);
		Pageable pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		Page<Contenedor> page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertFalse(page.getContent().contains(contenedor1));
		assertTrue(page.getContent().contains(contenedor2));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarTipoDeBasuraByContenedorNoExistente()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		cService.buscarTiposDeBasuraByContenedor(CONTENEDOR_ID_NO_EXISTENTE);
	}

	private Pageable createPageRequest(int page, int size, String campoSort) {
		return new PageRequest(page, size, Sort.Direction.ASC, campoSort);
	}

}
