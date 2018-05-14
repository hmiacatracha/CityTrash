package es.udc.citytrash.test.controller.Trabajador;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorDto;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;

/**
 * 
 * @author hmia
 * 
 *         https://www.petrikainulainen.net/programming/spring-framework/unit-testing-of-spring-mvc-controllers-normal-controllers/
 *         https://docs.spring.io/spring-security/site/docs/current/reference/html/test-mockmvc.html
 *         https://www.luckyryan.com/2013/08/24/unit-test-controllers-spring-mvc-test/
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@WebAppConfiguration
public class TrabajadoresControllerTest {

	private final int PAGE_NUMBER = 0;
	private final String PAGE_NUMBER_STRING = "1";
	private final int PAGE_SIZE = 5;
	private final String PAGE_SIZE_STRING = "5";
	private final String SEARCH_TERM = "itl";
	private static String SORT_FIELD = "id";
	private Pageable pageRequest;
	private List<TrabajadorDto> trabajadores = null;
	private Page<TrabajadorDto> pageTrabajadorDto = null;
	private Page<Trabajador> pageTrabajador = null;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	TrabajadorService tservicio;

	protected MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		Sort sort = new Sort(Sort.Direction.ASC, SORT_FIELD);
		List<TrabajadorDto> trabajadoresDto = new ArrayList<TrabajadorDto>();
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, sort);
		pageTrabajador = new PageImpl<Trabajador>(trabajadores, pageRequest, trabajadores.size());
		pageTrabajadorDto = new PageImpl<TrabajadorDto>(trabajadoresDto, pageRequest, trabajadoresDto.size());
	}

	@Test
	public void getTrabajadoresVacioSuccess() throws Exception {
		//when(tservicio.buscarTrabajadores(pageRequest)).thenReturn(pageTrabajador);
		mockMvc.perform(get(WebUtils.URL_TRABAJADORES)).andExpect(status().isOk());
		//verifyNoMoreInteractions(tservicio);
	}

	//
	// @Test
	// // @WithMockUser(username = "user", roles = { "ROLE_USER" })
	// public void trabajadoresAccessProtectedError() throws Exception {
	// mockMvc.perform(post(WebUtils.URL_TRABAJADORES)).andExpect(status().isUnauthorized());
	// }
	//
	// @Test
	// public void trabajadoresregistroAccessProtectedError() throws Exception {
	// mockMvc.perform(post(WebUtils.URL_TRABAJADORES_REGISTRO)).andExpect(status().isUnauthorized());
	// }
	//
	// @Test
	// public void trabajadoresdetallesAccessProtectedError() throws Exception {
	// mockMvc.perform(get(WebUtils.URL_TRABAJADOR_DETALLES)).andExpect(status().isUnauthorized());
	// }
	//
	// @Test
	// public void trabajadoreseliminarAccessProtectedError() throws Exception {
	// mockMvc.perform(post(WebUtils.URL_TRABAJADOR_DELETE)).andExpect(status().isUnauthorized());
	// }

}
