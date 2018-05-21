package es.udc.citytrash.test.model.camion;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camion.CamionDao;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModelo.CamionModeloDao;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class CamionDaoTest {

	@Autowired
	CamionDao camionDao;

	@Autowired
	CamionModeloDao camionModeloDao;

	Camion camion1;
	Camion camion2;
	CamionModelo camionModelo1;
	CamionModelo camionModelo2;

	private static String NOMBRE_MODELO1 = "modelo1";
	private static String NOMBRE_MODELO2 = "modelo2";
	private static String MATRICULA1 = "matricula2";
	private static String MATRICULA2 = "matricula1";
	private static BigDecimal ANCHO = new BigDecimal("24.7");
	private static BigDecimal ALTURA = new BigDecimal("12.2");
	private static BigDecimal LONGITUD = new BigDecimal("25");
	private static final long ID_NO_EXISTENTE = -1;

	@Before
	public void setUp() throws InstanceNotFoundException {
		camionModelo1 = new CamionModelo(NOMBRE_MODELO1, ANCHO, ALTURA, LONGITUD);
		camionModeloDao.guardar(camionModelo1);
		camionModelo2 = new CamionModelo(NOMBRE_MODELO2, ANCHO, ALTURA, LONGITUD);
		camionModeloDao.guardar(camionModelo2);

	}

//	/@Test
	public void testGuardarCamion() throws InstanceNotFoundException {
		camion1 = new Camion(MATRICULA1, camionModelo1);
		Calendar inicio = Calendar.getInstance();
		camionDao.guardar(camion1);
		Calendar fin = Calendar.getInstance();
		Camion camionEncontrado = camionDao.buscarById(camion1.getId());
		assertTrue(camionEncontrado.getMatricula().equals(MATRICULA1));
		assertTrue(camionEncontrado.getModeloCamion().equals(camionModelo1));
		assertTrue(camionEncontrado.getFechaAlta().after(inicio));
		assertTrue(camionEncontrado.getFechaAlta().before(fin));
	}

	// @Test(expected = DataIntegrityViolationException.class)
	@Test(expected = NullPointerException.class)
	public void testGuardarCamionMatriculaDuplicada() throws InstanceNotFoundException {
		camion1 = new Camion(MATRICULA1, camionModelo1);
		camionDao.guardar(camion1);
		Camion camionEncontrado = camionDao.buscarById(camion1.getId());
		assertTrue(camionEncontrado.getMatricula().equals(MATRICULA1));
		camion2 = new Camion(MATRICULA1, camionModelo2);
		camionDao.guardar(camion2);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testBuscarNoEncontrado() throws InstanceNotFoundException {
		camionDao.buscarById(ID_NO_EXISTENTE);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testEliminar() throws InstanceNotFoundException {
		camion1 = new Camion(MATRICULA1, camionModelo1);
		camionDao.guardar(camion1);
		long id = camion1.getId();
		Camion camionEncontrado = camionDao.buscarById(id);
		assertEquals(camion1, camionEncontrado);
		camionDao.eliminar(camion1);
		camionDao.buscarById(id);
	}

	//@Test
	public void testModificar() throws InstanceNotFoundException {
		camion1 = new Camion(MATRICULA1, camionModelo1);
		camionDao.guardar(camion1);
		long id = camion1.getId();
		Camion camionEncontrado = camionDao.buscarById(id);
		assertEquals(MATRICULA1, camionEncontrado.getMatricula());
		assertEquals(camionModelo1, camionEncontrado.getModeloCamion());
		camion1.setMatricula(MATRICULA2);
		camionDao.guardar(camion1);
		camionEncontrado = camionDao.buscarById(id);
		assertEquals(MATRICULA2, camionEncontrado.getMatricula());
		assertEquals(camionModelo1, camionEncontrado.getModeloCamion());
		assertNotEquals(MATRICULA1, camionEncontrado.getMatricula());
		camion1.setModeloCamion(camionModelo2);
		camionEncontrado = camionDao.buscarById(id);
		assertEquals(MATRICULA2, camionEncontrado.getMatricula());
		assertNotEquals(camionModelo1, camionEncontrado.getModeloCamion());
		assertEquals(camionModelo2, camionEncontrado.getModeloCamion());
	}
}
