package es.udc.citytrash.test.model.trabajador;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.model.trabajador.Administrador;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.Recolector;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.util.enums.Idioma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class TrabajadoDaoTest {

	@Autowired
	TrabajadorDao tDao;

	Trabajador t;

	Calendar fechaExpiracion;
	Calendar fecNacimiento;

	@Before
	public void starUp() {
		fechaExpiracion = Calendar.getInstance();
		fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
	}

	@Test
	public void testGuardarTrabajadorAdministrador() throws InstanceNotFoundException {

		String email3 = "administrador@gmail.com";
		Administrador a = new Administrador("10284977Y", "ADMIN", "ADMIN", email3, Idioma.es);
		tDao.guardar(a);
		Trabajador t3 = tDao.buscarTrabajadorPorEmail(email3);
		assertEquals(t3, a);
		assertEquals(t3.getEmail(), a.getEmail());
	}

	@Test
	public void testGuardarTrabajadorConductor() throws InstanceNotFoundException {

		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);

		String email1 = "conductor@gmail.com";
		Conductor c = new Conductor("10284977K", "conductor", "conduct", email1, Idioma.en);
		tDao.guardar(c);
		Trabajador t1 = tDao.buscarTrabajadorPorEmail(email1);
		assertEquals(t1, c);

	}

	@Test
	public void testGuardarTrabajadorRecolector() throws InstanceNotFoundException {

		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
		String email2 = "recolector@gmail.com";
		Recolector r = new Recolector("10284977X", "recolector", "apellidos", email2, Idioma.es);
		tDao.guardar(r);

		Trabajador t2 = tDao.buscarTrabajadorPorEmail(email2);
		assertEquals(t2, r);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testGuardarEmailDuplicado() throws InstanceNotFoundException {
		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
		String email2 = "recolector@gmail.com";
		Recolector r = new Recolector("10284977X", "recolector", "apellidos", email2, Idioma.es);
		Conductor c = new Conductor("10284977K", "conductor", "conduct", email2, Idioma.en);
		tDao.guardar(r);
		tDao.guardar(c); // duplicamos el email
	}

	@Test
	public void testbuscarConductorPorEmail() throws InstanceNotFoundException {

		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
		String email = "hmiacatracha@gmail.com";

		Conductor c1 = new Conductor("10284977T", "conductor", "conduc", email, Idioma.en);

		tDao.guardar(c1);

		Conductor t2 = (Conductor) tDao.buscarTrabajadorPorEmail(email);
		assertEquals(t2.getEmail(), c1.getEmail());
		assertEquals(t2, c1);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testEliminarTrabajador() throws Exception {
		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
		String email = "hmiacatracha@gmail.com";
		Conductor c1 = new Conductor("10284977T", "conductor", "coduct", email, Idioma.en);
		Conductor t2;
		tDao.guardar(c1);
		try {
			t2 = (Conductor) tDao.buscarTrabajadorPorEmail(email);
			assertEquals(t2, c1);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
		tDao.eliminar(c1);
		tDao.buscarTrabajadorPorEmail(email);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void guardarCambiosTrabajador() throws InstanceNotFoundException {
		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
		String email = "hmiacatracha@gmail.com";
		String email2 = "conductor@citytrash.com";
		Conductor c1 = new Conductor("10284977T", "conductor", "coduct", email, Idioma.en);
		Conductor t2;
		tDao.guardar(c1);

		try {
			t2 = (Conductor) tDao.buscarTrabajadorPorEmail(email);
			assertEquals(t2, c1);
			c1.setEmail(email2);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}

		tDao.guardar(c1);
		try {
			t2 = (Conductor) tDao.buscarTrabajadorPorEmail(email2);
			assertEquals(t2, c1);
			assertEquals(email2, t2.getEmail());
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
		t2 = (Conductor) tDao.buscarTrabajadorPorEmail(email);
	}

}