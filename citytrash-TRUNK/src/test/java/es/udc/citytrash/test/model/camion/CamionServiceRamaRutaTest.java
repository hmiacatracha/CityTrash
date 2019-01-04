package es.udc.citytrash.test.model.camion;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
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

import es.udc.citytrash.controller.util.dtos.camion.CamionModeloDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloTipoDeBasuraDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloRegistroDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorRegistroDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorBusqFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorRegistroFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorUpdateFormDto;
import es.udc.citytrash.model.camion.CamionDao;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModelo.CamionModeloDao;
import es.udc.citytrash.model.camionService.CamionService;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedor.ContenedorDao;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorModelo.ContenedorModeloDao;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.model.util.excepciones.InactiveResourceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;
import es.udc.citytrash.test.model.trabajador.TrabajadorServiceTest;
import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.CampoBusqTrabajador;
import es.udc.citytrash.util.enums.Idioma;
import es.udc.citytrash.util.enums.TipoTrabajador;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class CamionServiceRamaRutaTest {

	@Autowired
	CamionService cService;

	@Autowired
	CamionDao camionDao;

	@Autowired
	CamionModeloDao modeloDao;

	@Autowired
	TipoDeBasuraDao tipoDao;

	final Logger logger = LoggerFactory.getLogger(CamionServiceRamaRutaTest.class);

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

	public CamionModeloTipoDeBasuraDto getTipoDeBasura(int tipo, BigDecimal capacidad) {
		return new CamionModeloTipoDeBasuraDto(tipo, capacidad);
	}

	public CamionModeloDto getFormRegistroModelo(String nombre, BigDecimal altura, BigDecimal ancho,
			BigDecimal distancia, BigDecimal longitud, BigDecimal volumenTolva, Integer pma,
			List<CamionModeloTipoDeBasuraDto> listaTiposDeBasura) {
		CamionModeloDto modeloRegistro = new CamionModeloDto();
		modeloRegistro.setAltura(altura);
		modeloRegistro.setAncho(ancho);
		modeloRegistro.setDistancia(distancia);
		modeloRegistro.setListaTiposDeBasura(listaTiposDeBasura);
		modeloRegistro.setLongitud(longitud);
		modeloRegistro.setModificado(false);
		modeloRegistro.setNombre(nombre);
		modeloRegistro.setPma(pma);
		modeloRegistro.setVolumenTolva(volumenTolva);
		return modeloRegistro;
	}

	public ContenedorRegistroDto getFormRegistroCamion(String nombre, int modeloId, Date fechaAlta, Date fechaBaja,
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

	
	@Test(expected = InstanceNotFoundException.class)
	public void buscarModeloNoEncontrado() throws InstanceNotFoundException {
		cService.buscarModeloById(TIPO_ID_NO_EXISTENTE);
	}

	
	@Test
	public void buscarModeloOk() throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {		
		List<CamionModeloTipoDeBasuraDto> tiposDeBasuraCamion = new ArrayList<CamionModeloTipoDeBasuraDto>();
		CamionModeloTipoDeBasuraDto tipoBasura1 = getTipoDeBasura(tipo1.getId(), new BigDecimal(100));
		CamionModeloTipoDeBasuraDto tipoBasura2 = getTipoDeBasura(tipo2.getId(), new BigDecimal(100));
		tiposDeBasuraCamion.add(tipoBasura1);
		tiposDeBasuraCamion.add(tipoBasura2);
				
		CamionModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				2, tiposDeBasuraCamion));
		
		CamionModelo tipoEncontrado1 = cService.buscarModeloById(modelo1.getId());
		assertEquals(tipoEncontrado1.getModelo(), modelo1.getModelo());
		assertEquals(tipoEncontrado1, modelo1);
	}

	
	
	@Test(expected = DuplicateInstanceException.class)
	public void registrarModeloDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		List<CamionModeloTipoDeBasuraDto> tiposDeBasuraCamion = new ArrayList<CamionModeloTipoDeBasuraDto>();
		CamionModeloTipoDeBasuraDto tipoBasura1 = getTipoDeBasura(tipo1.getId(), new BigDecimal(100));
		CamionModeloTipoDeBasuraDto tipoBasura2 = getTipoDeBasura(tipo2.getId(), new BigDecimal(100));
		tiposDeBasuraCamion.add(tipoBasura1);
		tiposDeBasuraCamion.add(tipoBasura2);
		
		cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("1.2"), new BigDecimal("19.2"), new BigDecimal("9.2"), new BigDecimal("19.2"),
				2, tiposDeBasuraCamion));
		
		cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.0"),
				new BigDecimal("13.4"), new BigDecimal("1.5"), new BigDecimal("12.2"), new BigDecimal("19.2"),
				2, tiposDeBasuraCamion));
	}	
	
	
	@Test
	public void buscarModelosPageableVacios() throws FormBusquedaException {
		int psize = 1;
		int pnum = 0;
		String campoSort = "id";
		CamionModeloFormBusq formBusqueda = new CamionModeloFormBusq(null, null);
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<CamionModelo> page;		
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().isEmpty());		
	}

	
	@Test
	public void buscarModelosPageable()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException, FormBusquedaException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		CamionModeloFormBusq formBusqueda = new CamionModeloFormBusq();
		
		List<CamionModeloTipoDeBasuraDto> tiposDeBasuraCamion1 = new ArrayList<CamionModeloTipoDeBasuraDto>();		
		CamionModeloTipoDeBasuraDto tipoBasura1 = getTipoDeBasura(tipo1.getId(), new BigDecimal(100));
		CamionModeloTipoDeBasuraDto tipoBasura2 = getTipoDeBasura(tipo2.getId(), new BigDecimal(100));
		tiposDeBasuraCamion1.add(tipoBasura1);
		tiposDeBasuraCamion1.add(tipoBasura2);
		
		List<CamionModeloTipoDeBasuraDto> tiposDeBasuraCamion2 = new ArrayList<CamionModeloTipoDeBasuraDto>();		
		CamionModeloTipoDeBasuraDto tipoBasura3 = getTipoDeBasura(tipo2.getId(), new BigDecimal(60));		
		tiposDeBasuraCamion1.add(tipoBasura3);
				
		CamionModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				2, tiposDeBasuraCamion1));
		
		CamionModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				2, tiposDeBasuraCamion2));
		
		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<CamionModelo> page = cService.buscarModelos(pageable, formBusqueda);
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
	public void buscarModelosPageableByPalabrasClaves()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException, FormBusquedaException {
		int pnum = 0;
		int psize = 2;
		String campoSort = "id";
		CamionModeloFormBusq formBusqueda = new CamionModeloFormBusq();
		
		List<CamionModeloTipoDeBasuraDto> tiposDeBasuraCamion1 = new ArrayList<CamionModeloTipoDeBasuraDto>();		
		CamionModeloTipoDeBasuraDto tipoBasura1 = getTipoDeBasura(tipo1.getId(), new BigDecimal(100));
		CamionModeloTipoDeBasuraDto tipoBasura2 = getTipoDeBasura(tipo2.getId(), new BigDecimal(100));
		tiposDeBasuraCamion1.add(tipoBasura1);
		tiposDeBasuraCamion1.add(tipoBasura2);
		
		List<CamionModeloTipoDeBasuraDto> tiposDeBasuraCamion2 = new ArrayList<CamionModeloTipoDeBasuraDto>();		
		CamionModeloTipoDeBasuraDto tipoBasura3 = getTipoDeBasura(tipo2.getId(), new BigDecimal(60));		
		tiposDeBasuraCamion1.add(tipoBasura3);
				
		CamionModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				2, tiposDeBasuraCamion1));
		
		CamionModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				2, tiposDeBasuraCamion2));
		
		formBusqueda.setPalabrasClaveModelo("modelo");
		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		Pageable pageable = createPageRequest(pnum, modelos.size(), campoSort);
		Page<CamionModelo> page = cService.buscarModelos(pageable, formBusqueda);
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
		
		
		////////////////////////////////
		
		formBusqueda.setPalabrasClaveModelo("modelo1");
		modelos = new ArrayList<CamionModelo>();
		modelos.add(modelo1);
		modelos.add(modelo2);
		pageable = createPageRequest(pnum, modelos.size(), campoSort);
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == modelos.size());

		
		pnum = 0;
		psize = 1;
		pageable = createPageRequest(pnum, psize, "id");
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == psize);
		assertEquals(page.getContent().get(1), modelo1);

		pnum = 1;
		psize = 1;
		pageable = createPageRequest(pnum, psize, "id");
		page = cService.buscarModelos(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 0);

	}


	/*
	//@Test
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

		 Con form busqueda tipo 
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

	//@Test
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

		 Con form busqueda palabras claves y tipo 
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

	//@Test
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

		 Con form busqueda palabras claves y tipo 
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

	//@Test
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

	//@Test(expected = DuplicateInstanceException.class)
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

	//@Test
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

	//@Test
	public void buscarTipoDeBasuraByModelo()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));
		// cService.buscarModeloById(modelo1.getId());
		assertEquals(tipo1, modelo1.getTipo());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////

	//@Test
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

	//@Test(expected = DuplicateInstanceException.class)
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

	//@Test(expected = InstanceNotFoundException.class)
	public void registrarContenedorModeloNoExistente() throws InstanceNotFoundException, DuplicateInstanceException {
		BigDecimal latitud = new BigDecimal("41.00002");
		BigDecimal longitud = new BigDecimal("2.0232");
		ContenedorRegistroDto registroContenedorForm1 = getFormRegistroContenedor(C_NOMBRE_1, MODELO_ID_NO_EXISTENTE,
				null, null, latitud, longitud, "Avenida buenos Aires", true);
		cService.registrarContenedor(registroContenedorForm1);
	}

	//@Test
	public void buscarContenedoresPageableVacios() {
		int psize = 1;
		int pnum = 0;
		String campoSort = "id";
		ContenedorFormBusq formBusqueda = new ContenedorFormBusq();
		Pageable pageable = createPageRequest(pnum, psize, campoSort);
		Page<Contenedor> page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().isEmpty());
	}

	//@Test
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

	//@Test
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

	//@Test
	public void buscarContenedorPageableByTipo()
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
		List<Integer> tiposInteger = new ArrayList<Integer>();
		tiposInteger.add(tipo1.getId());
		formBusqueda.setBuscar(C_NOMBRE_2);
		formBusqueda.setTiposDeBasura(tiposInteger);
		Pageable pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		Page<Contenedor> page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 0);

		formBusqueda = new ContenedorFormBusq();
		tiposInteger = new ArrayList<Integer>();
		tiposInteger.add(tipo1.getId());
		formBusqueda.setBuscar(contenedor1.getNombre().substring(0, contenedor1.getNombre().length() - 2));
		formBusqueda.setTiposDeBasura(tiposInteger);
		pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertTrue(page.getContent().contains(contenedor1));

	}

	//@Test
	public void buscarContenedoresPageableByModelo()
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
		formBusqueda.setModelo(modelo1.getId());
		Pageable pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		Page<Contenedor> page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertTrue(page.getContent().contains(contenedor1));

		formBusqueda = new ContenedorFormBusq();
		formBusqueda.setModelo(modelo2.getId());
		pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertTrue(page.getContent().contains(contenedor2));
	}

	//@Test
	public void buscarContenedoresPageableByPalabrasClavesTipoYModelo()
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
		List<Integer> tiposInteger = new ArrayList<Integer>();
		tiposInteger.add(tipo1.getId());
		formBusqueda.setBuscar(C_NOMBRE_1);
		formBusqueda.setModelo(modelo1.getId());
		formBusqueda.setTiposDeBasura(tiposInteger);
		Pageable pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		Page<Contenedor> page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 1);
		assertTrue(page.getContent().contains(contenedor1));

		formBusqueda = new ContenedorFormBusq();
		tiposInteger = new ArrayList<Integer>();
		tiposInteger.add(tipo1.getId());
		formBusqueda.setBuscar(contenedor1.getNombre().substring(0, contenedor1.getNombre().length() - 2));
		formBusqueda.setModelo(modelo2.getId());
		formBusqueda.setTiposDeBasura(tiposInteger);
		pageable = createPageRequest(pnum, contenedores.size(), campoSort);
		page = cService.buscarContenedores(pageable, formBusqueda);
		assertTrue(page.getContent().size() == 0);
	}

	//@Test
	public void modificarContenedorOk()
			throws DuplicateInstanceException, InvalidFieldException, InstanceNotFoundException {

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));

		BigDecimal latitud = new BigDecimal("41.00001");
		BigDecimal longitud = new BigDecimal("2.0232");
		ContenedorRegistroDto registroContenedorForm1 = getFormRegistroContenedor(C_NOMBRE_1, modelo1.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires 1", false);
		Contenedor contenedor1 = cService.registrarContenedor(registroContenedorForm1);

		assertTrue(contenedor1.getActivo());
		ContenedorEditarDto editarContenedorForm1 = new ContenedorEditarDto(contenedor1);
		editarContenedorForm1.setActivo(false);
		cService.modificarContenedor(editarContenedorForm1);
		Contenedor contenedorEncontrado = cService.buscarContenedorById(contenedor1.getId());
		assertEquals(contenedorEncontrado.getNombre(), editarContenedorForm1.getNombre());
		assertFalse(contenedor1.getActivo());
	}

	//@Test(expected = InstanceNotFoundException.class)
	public void modificarContenedorInstanceNotFoundExceptio()
			throws DuplicateInstanceException, InstanceNotFoundException {
		ContenedorEditarDto editarContenedorForm1 = new ContenedorEditarDto();
		editarContenedorForm1.setId(CONTENEDOR_ID_NO_EXISTENTE);
		editarContenedorForm1.setNombre(C_NOMBRE_1);
		editarContenedorForm1.setActivo(false);
		cService.modificarContenedor(editarContenedorForm1);
	}

	//@Test(expected = DuplicateInstanceException.class)
	public void modificarContenedorDuplicado()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {

		ContenedorModelo modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo1.getId()));

		ContenedorModelo modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
				new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
				new BigDecimal("19.2"), tipo2.getId()));

		BigDecimal latitud = new BigDecimal("41.00001");
		BigDecimal longitud = new BigDecimal("2.0232");
		ContenedorRegistroDto registroContenedorForm1 = getFormRegistroContenedor(C_NOMBRE_1, modelo1.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires 1", false);
		Contenedor contenedor1 = cService.registrarContenedor(registroContenedorForm1);

		ContenedorRegistroDto registroContenedorForm2 = getFormRegistroContenedor(C_NOMBRE_2, modelo2.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires 2", true);
		Contenedor contenedor2 = cService.registrarContenedor(registroContenedorForm2);

		assertNotEquals(contenedor1.getNombre(), contenedor2.getNombre());

		ContenedorEditarDto editarContenedorForm1 = new ContenedorEditarDto(contenedor2);
		editarContenedorForm1.setNombre(C_NOMBRE_1);
		cService.modificarContenedor(editarContenedorForm1);
	}

	//@Test(expected = InstanceNotFoundException.class)
	public void modificarContenedorByModeloNoExistente() throws InstanceNotFoundException, DuplicateInstanceException {

		ContenedorModelo modelo1 = null;
		ContenedorModelo modelo2 = null;
		try {
			modelo1 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_1, new BigDecimal("19.2"),
					new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
					new BigDecimal("19.2"), tipo1.getId()));

			modelo2 = cService.registrarModelo(getFormRegistroModelo(CM_NOMBRE_2, new BigDecimal("19.2"),
					new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"), new BigDecimal("19.2"),
					new BigDecimal("19.2"), tipo2.getId()));
		} catch (InvalidFieldException e) {
			assert (false);
		}

		BigDecimal latitud = new BigDecimal("41.00001");
		BigDecimal longitud = new BigDecimal("2.0232");
		ContenedorRegistroDto registroContenedorForm1 = getFormRegistroContenedor(C_NOMBRE_1, modelo1.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires 1", false);
		Contenedor contenedor1 = cService.registrarContenedor(registroContenedorForm1);

		ContenedorRegistroDto registroContenedorForm2 = getFormRegistroContenedor(C_NOMBRE_2, modelo2.getId(), null,
				null, latitud, longitud, "Avenida buenos Aires 2", true);
		Contenedor contenedor2 = cService.registrarContenedor(registroContenedorForm2);
		assertNotEquals(contenedor1.getNombre(), contenedor2.getNombre());
		ContenedorEditarDto editarContenedorForm1 = new ContenedorEditarDto(contenedor2);
		editarContenedorForm1.setModeloContenedor(MODELO_ID_NO_EXISTENTE);
		cService.modificarContenedor(editarContenedorForm1);
	}

	//@Test(expected = InstanceNotFoundException.class)
	public void buscarTipoDeBasuraByContenedorNoExistente()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		cService.buscarTiposDeBasuraByContenedor(CONTENEDOR_ID_NO_EXISTENTE);
	}
	*/
	private Pageable createPageRequest(int page, int size, String campoSort) {
		return new PageRequest(page, size, Sort.Direction.ASC, campoSort);
	}
}
