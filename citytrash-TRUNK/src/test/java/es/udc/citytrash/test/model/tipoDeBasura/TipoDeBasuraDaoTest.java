package es.udc.citytrash.test.model.tipoDeBasura;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.trabajador.Administrador;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.util.enums.Idioma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class TipoDeBasuraDaoTest {

	@Autowired
	TipoDeBasuraDao tipoDao;

	TipoDeBasura tipo;

	private static String COLOR1 = "000000";
	private static String COLOR2 = "F00F00";
	private static String TIPO1 = "TIPO1";
	private static String TIPO2 = "TIPO2";
	private static final int ID_NO_EXISTENTE = -1;

	@Before
	public void setUp() throws InstanceNotFoundException {
		tipo = new TipoDeBasura(TIPO1, COLOR1);
	}

	@Test
	public void testGuardar() throws InstanceNotFoundException {
		tipoDao.guardar(tipo);
		TipoDeBasura tipoEncontrado = tipoDao.buscarById(tipo.getId());
		assertTrue(tipoEncontrado.getColor().equals(COLOR1));
		assertTrue(tipoEncontrado.getTipo().equals(TIPO1));
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testGuardarConTipoRepetido() throws InstanceNotFoundException {
		tipo.setTipo(TIPO1);
		tipoDao.guardar(tipo);
		TipoDeBasura tipoEncontrado = tipoDao.buscarById(tipo.getId());
		assertTrue(tipoEncontrado.getColor().equals(COLOR1));
		assertTrue(tipoEncontrado.getTipo().equals(TIPO1));

		TipoDeBasura tipoNuevo = new TipoDeBasura(TIPO1, COLOR2);
		tipoDao.guardar(tipoNuevo);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testGuardarConColorRepetido() throws InstanceNotFoundException {
		tipo.setTipo(TIPO1);
		tipoDao.guardar(tipo);
		TipoDeBasura tipoEncontrado = tipoDao.buscarById(tipo.getId());
		assertTrue(tipoEncontrado.getColor().equals(COLOR1));
		assertTrue(tipoEncontrado.getTipo().equals(TIPO1));
		TipoDeBasura tipoNuevo = new TipoDeBasura(TIPO2, COLOR1);
		tipoDao.guardar(tipoNuevo);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void buscarConErrorInstanceNotFoundException() throws InstanceNotFoundException {
		tipoDao.buscarById(ID_NO_EXISTENTE);
	}

	@Test
	public void buscarSinError() throws InstanceNotFoundException {
		tipoDao.guardar(tipo);
		TipoDeBasura tipoEncontrado = tipoDao.buscarById(tipo.getId());
		assertTrue(tipoEncontrado.equals(tipo));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void eliminar() throws InstanceNotFoundException {
		tipoDao.guardar(tipo);
		int id = tipo.getId();
		TipoDeBasura tipoEncontrado = tipoDao.buscarById(id);
		assertTrue(tipoEncontrado.equals(tipo));
		tipoDao.eliminar(tipo);
		tipoDao.buscarById(id);
	}

	@Test
	public void editar() throws InstanceNotFoundException {
		String colorNuevo = "FFFFFF";
		tipoDao.guardar(tipo);
		TipoDeBasura tipoEncontrado = tipoDao.buscarById(tipo.getId());
		assertTrue(tipoEncontrado.getColor().equals(COLOR1));
		assertTrue(tipoEncontrado.getTipo().equals(TIPO1));
		tipo.setColor(colorNuevo);
		tipoDao.guardar(tipo);
		tipoEncontrado = tipoDao.buscarById(tipo.getId());
		assertTrue(tipoEncontrado.getColor().equals(colorNuevo));
		assertTrue(tipoEncontrado.getTipo().equals(TIPO1));
	}

}
