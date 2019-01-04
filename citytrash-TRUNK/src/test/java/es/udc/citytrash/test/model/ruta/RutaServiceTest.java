package es.udc.citytrash.test.model.ruta;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.model.contenedor.ContenedorDao;
import es.udc.citytrash.model.contenedorModelo.ContenedorModeloDao;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.test.model.contenedor.ContenedorServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class RutaServiceTest {

	@Autowired
	ContenedorService cService;

	@Autowired
	ContenedorDao contenedorDao;

	@Autowired
	ContenedorModeloDao modeloDao;

	@Autowired
	TipoDeBasuraDao tipoDao;

	final Logger logger = LoggerFactory.getLogger(ContenedorServiceTest.class);

}
