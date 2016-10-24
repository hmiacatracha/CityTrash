package es.udc.fic.tfg.sensores.test.experiments;



import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	private static final SessionFactory currentSessionFactory;

	static {
		try {
			
			Configuration configuration = new Configuration()
					.configure("hibernate-config-experiments.xml");
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			
			currentSessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getCurrentSessionFactory() {
		return currentSessionFactory;
	}

	public static void shutdown() {
		// Close caches and connection pools
		getCurrentSessionFactory().close();
	}

}
