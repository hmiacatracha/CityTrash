package es.udc.citytrash.test.model.camionModelo;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModelo.CamionModeloDao;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class CamionModeloDaoTest {

	@Autowired
	CamionModeloDao modeloDao;

	CamionModelo modelo;

	private static String NOMBRE_MODELO = "modelo1";
	private static BigDecimal ANCHO = new BigDecimal("24.7");
	private static BigDecimal ALTURA = new BigDecimal("12.2");
	private static BigDecimal LONGITUD = new BigDecimal("25");
	private static final int ID_NO_EXISTENTE = -1;

	@Before
	public void setUp() throws InstanceNotFoundException {
		modelo = new CamionModelo(NOMBRE_MODELO, ANCHO, ALTURA, LONGITUD);
	}

	@Test
	public void testGuardarModelo() throws InstanceNotFoundException {
		modeloDao.guardar(modelo);
		CamionModelo modeloEncontrado = modeloDao.buscarById(modelo.getId());
		assertTrue(modeloEncontrado.getModelo().equals(NOMBRE_MODELO));
		assertTrue(modeloEncontrado.getAltura().equals(ALTURA));
		assertTrue(modeloEncontrado.getAncho().equals(ANCHO));
		assertTrue(modeloEncontrado.getLongitud().equals(LONGITUD));
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testGuardarModeloConNombreRepetido() throws InstanceNotFoundException {
		modeloDao.guardar(modelo);
		CamionModelo modeloEncontrado = modeloDao.buscarById(modelo.getId());
		assertTrue(modeloEncontrado.getModelo().equals(NOMBRE_MODELO));
		assertTrue(modeloEncontrado.getAltura().equals(ALTURA));
		assertTrue(modeloEncontrado.getAncho().equals(ANCHO));
		assertTrue(modeloEncontrado.getLongitud().equals(LONGITUD));

		BigDecimal volumenNuevo = new BigDecimal("10.32");
		BigDecimal alturaNueva = new BigDecimal("1.2");
		CamionModelo nuevoModelo = new CamionModelo(NOMBRE_MODELO, ANCHO, alturaNueva, LONGITUD);
		nuevoModelo.setVolumenTolva(volumenNuevo);
		modeloDao.guardar(nuevoModelo);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarConErrorInstanceNotFoundException() throws InstanceNotFoundException {
		modeloDao.buscarById(ID_NO_EXISTENTE);
	}

	@Test
	public void buscarSinError() throws InstanceNotFoundException {
		modeloDao.guardar(modelo);
		CamionModelo modeloEncontrado = modeloDao.buscarById(modelo.getId());
		assertTrue(modeloEncontrado.equals(modelo));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void eliminar() throws InstanceNotFoundException {
		modeloDao.guardar(modelo);
		int id = modelo.getId();
		CamionModelo modeloEncontrado = modeloDao.buscarById(id);
		assertTrue(modeloEncontrado.equals(modelo));
		modeloDao.eliminar(modelo);
		modeloDao.buscarById(id);
	}

	@Test
	public void editar() throws InstanceNotFoundException {
		BigDecimal volumen = new BigDecimal("10.32");
		BigDecimal alturaNueva = new BigDecimal("1.2");
		modeloDao.guardar(modelo);
		CamionModelo modeloEncontrado = modeloDao.buscarById(modelo.getId());
		assertTrue(modeloEncontrado.getAltura().equals(ALTURA));
		assertTrue(modeloEncontrado.getAncho().equals(ANCHO));
		assertTrue(modeloEncontrado.getLongitud().equals(LONGITUD));
		modelo.setAltura(alturaNueva);
		modelo.setVolumenTolva(volumen);
		modeloDao.guardar(modelo);
		modeloEncontrado = modeloDao.buscarById(modelo.getId());
		assertTrue(modeloEncontrado.getAltura().equals(alturaNueva));
		assertTrue(modeloEncontrado.getAncho().equals(ANCHO));
		assertTrue(modeloEncontrado.getLongitud().equals(LONGITUD));
		assertTrue(modeloEncontrado.getVolumenTolva().equals(volumen));
	}

}
