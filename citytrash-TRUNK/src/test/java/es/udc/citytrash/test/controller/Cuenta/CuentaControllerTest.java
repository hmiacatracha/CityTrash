package es.udc.citytrash.test.controller.Cuenta;

import static es.udc.citytrash.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.citytrash.util.GlobalNames.SPRING_SECURITY_FILE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_SECURITY_FILE, SPRING_CONFIG_TEST_FILE })
@WebAppConfiguration
public class CuentaControllerTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	TrabajadorService tservicioMock;

	protected MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	/* Testing with CSRF Protection */
	@Test
	public void testSigninIsAvailableToAnonymous() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk());
	}

	@Test
	public void TestingWithCSRFProtection() throws Exception {
		mockMvc.perform(post("/login").with(csrf()));
	}

	@Test
	public void TestingWithCSRFProtectionTokenInTheHeader() throws Exception {
		mockMvc.perform(post("/login").with(csrf().asHeader()));
	}

	@Test
	public void TestingWithCSRFProtectionTokenUserInvalidToken() throws Exception {
		mockMvc.perform(post("/login").with(csrf().useInvalidToken()));
	}

	@Test
	public void testingFormBasedAuthentication() throws Exception {
		mockMvc.perform(formLogin("/login").user("u", "admin").password("p", "pass"));
	}

	@Test
	public void testinglogout() throws Exception {
		mockMvc.perform(logout());
	}

	@Test
	public void unauthenticatedAssertion() throws Exception {
		mockMvc.perform(formLogin().password("invalid")).andExpect(unauthenticated());
	}

}
