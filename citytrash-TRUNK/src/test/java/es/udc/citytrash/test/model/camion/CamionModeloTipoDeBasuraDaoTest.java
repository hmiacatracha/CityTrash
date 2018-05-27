package es.udc.citytrash.test.model.camion;

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
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasuraDao;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasuraPK;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class CamionModeloTipoDeBasuraDaoTest {

	@Autowired
	TipoDeBasuraDao tipoDao;

	@Autowired
	CamionModeloDao modeloDao;

	@Autowired
	CamionModeloTipoDeBasuraDao mcANDtbDao;

	CamionModelo modelo1;
	CamionModelo modelo2;

	TipoDeBasura tipo1;
	TipoDeBasura tipo2;

	private static String NOMBRE_MODELO1 = "modelo1";
	private static String NOMBRE_MODELO2 = "modelo2";
	private static BigDecimal ANCHO = new BigDecimal("24.7");
	private static BigDecimal ALTURA = new BigDecimal("12.2");
	private static BigDecimal LONGITUD = new BigDecimal("25");
	private static BigDecimal CAPACIDAD_CAMION = new BigDecimal("100.4");
	private static String COLOR1 = "000000";
	private static String COLOR2 = "F00F00";
	private static String TIPO1 = "TIPO1";
	private static String TIPO2 = "TIPO2";

	@Before
	public void setUp() throws InstanceNotFoundException {
		tipo1 = new TipoDeBasura(TIPO1, COLOR1);
		tipo2 = new TipoDeBasura(TIPO2, COLOR2);
		modelo1 = new CamionModelo(NOMBRE_MODELO1, ANCHO, ALTURA, LONGITUD);
		modelo2 = new CamionModelo(NOMBRE_MODELO2, ANCHO, ALTURA, LONGITUD);
	}

	@Test
	public void testGuardar() throws InstanceNotFoundException {
		modeloDao.guardar(modelo1);
		tipoDao.guardar(tipo1);
		CamionModeloTipoDeBasura mcANDtb = new CamionModeloTipoDeBasura(modelo1, tipo1, CAPACIDAD_CAMION);
		mcANDtbDao.guardar(mcANDtb);
		CamionModeloTipoDeBasuraPK pk = new CamionModeloTipoDeBasuraPK(modelo1, tipo1);
		CamionModeloTipoDeBasura mcANDtbEncontrado = mcANDtbDao.buscarById(pk);
		assertTrue(mcANDtbEncontrado.getModelo().equals(pk.getModelo()));
		assertTrue(mcANDtbEncontrado.getTipo().equals(pk.getTipo()));
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testGuardarTipoRepetido() throws InstanceNotFoundException {
		modeloDao.guardar(modelo1);
		tipoDao.guardar(tipo1);
		CamionModeloTipoDeBasura mcANDtb = new CamionModeloTipoDeBasura(modelo1, tipo1, CAPACIDAD_CAMION);
		mcANDtbDao.guardar(mcANDtb);
		CamionModeloTipoDeBasuraPK pk = new CamionModeloTipoDeBasuraPK(modelo1, tipo1);
		CamionModeloTipoDeBasura mcANDtbEncontrado = mcANDtbDao.buscarById(pk);
		assertTrue(mcANDtbEncontrado.getModelo().equals(pk.getModelo()));
		assertTrue(mcANDtbEncontrado.getTipo().equals(pk.getTipo()));
		CamionModeloTipoDeBasura mcANDtb2 = new CamionModeloTipoDeBasura(modelo1, tipo1, CAPACIDAD_CAMION);
		mcANDtbDao.guardar(mcANDtb2);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testEliminar() throws InstanceNotFoundException {
		modeloDao.guardar(modelo1);
		tipoDao.guardar(tipo1);
		CamionModeloTipoDeBasura mcANDtb = new CamionModeloTipoDeBasura(modelo1, tipo1, CAPACIDAD_CAMION);
		mcANDtbDao.guardar(mcANDtb);
		CamionModeloTipoDeBasuraPK pk = new CamionModeloTipoDeBasuraPK(modelo1, tipo1);
		CamionModeloTipoDeBasura mcANDtbEncontrado = mcANDtbDao.buscarById(pk);
		assertTrue(mcANDtbEncontrado.getModelo().equals(pk.getModelo()));
		assertTrue(mcANDtbEncontrado.getTipo().equals(pk.getTipo()));
		mcANDtbDao.eliminar(mcANDtb);
		mcANDtbDao.buscarById(pk);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testModificar() throws InstanceNotFoundException {
		modeloDao.guardar(modelo1);
		tipoDao.guardar(tipo1);
		CamionModeloTipoDeBasura mcANDtb = new CamionModeloTipoDeBasura(modelo1, tipo1, CAPACIDAD_CAMION);

		mcANDtbDao.guardar(mcANDtb);
		tipoDao.guardar(tipo2);
		mcANDtb.setTipo(tipo2);
		mcANDtbDao.guardar(mcANDtb);

		CamionModeloTipoDeBasuraPK pk = new CamionModeloTipoDeBasuraPK(modelo1, tipo2);
		CamionModeloTipoDeBasura mcANDtbEncontrado = mcANDtbDao.buscarById(pk);
		assertTrue(mcANDtbEncontrado.getModelo().equals(pk.getModelo()));
		assertTrue(mcANDtbEncontrado.getTipo().equals(pk.getTipo()));
		mcANDtbDao.buscarById(new CamionModeloTipoDeBasuraPK(modelo1, tipo1));
	}

}
