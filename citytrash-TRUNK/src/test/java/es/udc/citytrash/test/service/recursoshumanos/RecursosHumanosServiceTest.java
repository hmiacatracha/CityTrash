package es.udc.citytrash.test.service.recursoshumanos;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.model.entity.prioridad.Prioridad;
import es.udc.citytrash.model.repository.prioridad.PrioridadDao;
import es.udc.citytrash.service.recursoshumanos.RecursosHumanosService;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = { SPRING_CONFIG_FILE,
// SPRING_CONFIG_TEST_FILE })
@ContextConfiguration(locations = { "classpath:/spring-config.xml", "classpath:/spring-config-test.xml" })
@Transactional
public class RecursosHumanosServiceTest {

	@Autowired
	private RecursosHumanosService recurosHumanosService;

	@Autowired
	private PrioridadDao prioridadDao;

	@Test
	public void testFindAllPrioridades() {
		prioridadDao.save(new Prioridad("H", "ALTA"));
		List<Prioridad> prioridades1 = recurosHumanosService.getAllPrioridades();
		assertEquals(true, true);
		assertEquals(prioridades1.get(0).tipo, "H");
		assertEquals(prioridades1.get(0).es, "ALTA");
		// assertEquals(prioridades1.get(1).tipo,"L");
		// assertEquals(prioridades1.get(2).tipo,"M");

	}
}
