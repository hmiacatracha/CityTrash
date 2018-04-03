package es.udc.citytrash.test.model.trabajador;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.udc.citytrash.business.entity.trabajador.Administrador;
import es.udc.citytrash.business.entity.trabajador.Conductor;
import es.udc.citytrash.business.entity.trabajador.Recolector;
import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.repository.trabajador.TrabajadorDao;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;

public class TrabajadoDaoTest {

	@Autowired
	TrabajadorDao tDao;

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
	public void testGuardarTrabajadorAdministrador() {

		String email3 = "administrador@gmail.com";

		Calendar fechaNacimiento = Calendar.getInstance();

		Administrador a = new Administrador("10284977Y", "ADMIN", "apellidos", email3, fechaNacimiento,
				"xasakdsadasoideqnrasd", fechaExpiracion);
		tDao.guardar(a);

		try {
			Trabajador t3 = tDao.buscarTrabajadorPorEmail(email3);
			assertEquals(t3, a);
			// assertEquals(t3.getEmail(), a.getEmail());
			
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	/*
	@Test
	public void testGuardarTrabajadorConductor() {

		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);

		String email1 = "conductor@gmail.com";

		Conductor c = new Conductor("10284977K", "conductor", "", "USER", email1,);

		tDao.save(c);

		try {
			Trabajador t1 = tDao.buscarTrabajadorPorEmail(email1);
			assertEquals(t1, c);

		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	@Test
	public void testGuardarTrabajadorRecolector() {

		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
		String email2 = "recolector@gmail.com";
		Recolector r = new Recolector("10284977X", "recolector", "apellidos", email2);
		tDao.save(r);
		try {

			Trabajador t2 = tDao.buscarTrabajadorPorEmail(email2);
			assertEquals(t2, r);
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

		Conductor c1 = new Conductor("10284977T", "conductor", "", "USER", email);

		tDao.save(c1);
		try {
			Conductor t2 = (Conductor) tDao.buscarTrabajadorPorEmail(email);
			assertEquals(t2, c1);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testEliminarTrabajador() throws Exception {

		Calendar fecNacimiento = Calendar.getInstance();
		fecNacimiento.set(Calendar.YEAR, 1990);
		fecNacimiento.set(Calendar.MONTH, 8);
		fecNacimiento.set(Calendar.DAY_OF_MONTH, 24);
		String email = "hmiacatracha@gmail.com";
		Conductor c1 = new Conductor("10284977T", "conductor", "", "USER", email);
		Conductor t2;
		tDao.save(c1);
		try {
			t2 = (Conductor) tDao.buscarTrabajadorPorEmail(email);
			assertEquals(t2, c1);
		} catch (InstanceNotFoundException e) {
			assert (false);
		}

		t2 = (Conductor) tDao.buscarTrabajadorPorEmail(email);
	}
*/
}
