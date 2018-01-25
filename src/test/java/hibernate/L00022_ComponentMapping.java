package hibernate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import hibernate.annotationMapping.domain.Address;
import hibernate.annotationMapping.domain.Person;
import hibernate.util.HibernateUtil;

public class L00022_ComponentMapping {

	@Test
	public void componentMappingWithEmbeddable() {
		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();

		Transaction txn = session.getTransaction();
		try {
			txn.begin();

			Address address = new Address("200 E Main St", "Seattle", "85123");
			Person person = new Person("Ruby", address);

			session.save(person);

			assertTrue("id for person should have been set by the RDBMS", person.getId() != null);

			txn.commit();
		} catch (HibernateException e) {
			// if something is thrown from hibernate then fail
			fail();
		} catch (Exception e) {
			if (txn != null) {
				txn.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
