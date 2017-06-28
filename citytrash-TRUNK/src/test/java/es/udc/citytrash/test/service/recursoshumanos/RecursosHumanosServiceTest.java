package es.udc.citytrash.test.service.recursoshumanos;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.model.entity.trabajador.Administrador;
import es.udc.citytrash.model.entity.trabajador.Conductor;
import es.udc.citytrash.model.entity.trabajador.Recolector;
import es.udc.citytrash.model.entity.trabajador.Trabajador;
import es.udc.citytrash.model.repository.trabajador.TrabajadorDao;
import es.udc.citytrash.util.excepciones.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class RecursosHumanosServiceTest {

	// @Autowired
	// private RecursosHumanosService recurosHumanosService;

	@Autowired
	private TrabajadorDao trabajadorDao;

	@Test
	public void testbuscarTrabajadorPorEmail() {

		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
		String email1 = "conductor@gmail.com";
		String email2 = "recolector@gmail.com";
		String email3 = "administrador@gmail.com";

		Conductor c = new Conductor();
		c.setNombre("nombre");
		c.setApellidos("apellidos");
		c.setFecNac(fecNacimiento);
		c.setEmail(email1);
		c.setEncryptedPassword("contrasena");

		Recolector r = new Recolector();
		r.setNombre("nombre");
		r.setApellidos("apellidos");
		r.setFecNac(fecNacimiento);
		r.setEmail(email2);
		r.setEncryptedPassword("contrasena");

		Administrador a = new Administrador();
		a.setNombre("nombre");
		a.setApellidos("apellidos");
		a.setFecNac(fecNacimiento);
		a.setEmail(email3);
		a.setEncryptedPassword("contrasena");

		trabajadorDao.save(c);
		trabajadorDao.save(r);
		trabajadorDao.save(a);
		
		try {
			Trabajador t1 = trabajadorDao.findByLoginEmail(email1);
			assertEquals(t1, c);
			Trabajador t2 = trabajadorDao.findByLoginEmail(email2);
			assertEquals(t2, r);
			Trabajador t3 = trabajadorDao.findByLoginEmail(email3);
			assertEquals(t3.getEmail(), a.getEmail());

		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	@Test
	public void testbuscarConductorPorEmail() {

		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
		String email = "hmiacatracha@gmail.com";

		Conductor c1 = new Conductor();
		c1.setNombre("nombre");
		c1.setApellidos("apellidos");
		c1.setFecNac(fecNacimiento);
		c1.setEmail(email);
		c1.setEncryptedPassword("contrasena");

		trabajadorDao.save(c1);
		try {
			Conductor t2 = (Conductor) trabajadorDao.findByLoginEmail(email);
			assertEquals(t2, c1);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}
}
