package es.udc.fic.tfg.sensores.test.experiments;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.udc.fic.tfg.sensores.model.townhall.TownHall;
import es.udc.fic.tfg.sensores.model.util.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

public class TownHallServiceExperimentsTest {

	private static final String SPRING_CONFIG_FILE = null;
	private static final String SPRING_CONFIG_TEST_FILE = null;

	public static void main(String[] args) {

		/* Get service object. */
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE });
		TownHall townService = ctx.getBean(TownHall.class);
		//
		// try {
		// // Register user.
		// TownHall townHall = townService.

		// Find user.
		// townHall = townService.login("serviceUser", "userPassword",
		// false);
		// System.out.println("User with userId '" +
		// townHall.getUserProfileId() + "' has been retrieved");
		// System.out.println(townHall);

		// ... proceed in the same way for other entities / methods / use
		// cases

		// } catch (InstanceNotFoundException | DuplicateInstanceException e) {
		// e.printStackTrace();
		// }

	}

}
