package hibernate.util;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sessionFactoryXmlMapping = buildSessionFactoryXmlMapping();
	private static final SessionFactory sessionFactoryAnnotationMapping = buildSessionFactoryAnnotationMapping();

	private static SessionFactory buildSessionFactoryXmlMapping() {
		try {
			// for Hibernate 4.3.x users
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration().configure("hibernate/hibernateXmlMapping.cfg.xml");
			return configuration.buildSessionFactory(
					new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());

			// for Hibernate 5.x users
			// Create the SessionFactory from hibernate.cfg.xml
			// StandardServiceRegistry serviceRegistry = new
			// StandardServiceRegistryBuilder().configure("hibernateXmlMapping.cfg.xml").build();
			// Metadata metadata = new
			// MetadataSources(serviceRegistry).getMetadataBuilder().build();
			// return metadata.getSessionFactoryBuilder().build();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static SessionFactory buildSessionFactoryAnnotationMapping() {
		try {
			// for Hibernate 4.3.x users
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration().configure("hibernate/hibernateAnnotationMapping.cfg.xml");
			return configuration.buildSessionFactory(
					new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());

			// for Hibernate 5.x users
			// Create the SessionFactory from hibernate.cfg.xml
			// StandardServiceRegistry serviceRegistry = new
			// StandardServiceRegistryBuilder().configure("hibernateAnnotationMapping.cfg.xml").build();
			// Metadata metadata = new
			// MetadataSources(serviceRegistry).getMetadataBuilder().build();
			// return metadata.getSessionFactoryBuilder().build();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactoryXmlMapping() {
		return sessionFactoryXmlMapping;
	}

	public static SessionFactory getSessionFactoryAnnotationMapping() {
		return sessionFactoryAnnotationMapping;
	}

}
