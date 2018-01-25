
package hibernate;

import static org.junit.Assert.assertNotNull;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import hibernate.annotationMapping.domain.L00024Guide;
import hibernate.annotationMapping.domain.L00024Student;
import hibernate.util.HibernateUtil;

public class L00024_MappingAssociations {

	@Test
	public void mappingEntityAssociations() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			txn.begin();

			L00024Guide guide = new L00024Guide("2000MO10789", "Mike Lawson", 1000);
			L00024Student student = new L00024Student("2014JT50123", "John Smith", guide);

			session.save(guide);
			session.save(student);

			// both the entities should be saved with ids provided by the RDBMS
			assertNotNull("ID should not be null", guide.getId());
			assertNotNull("ID should not be null", student.getId());

			L00024Student studentDb = (L00024Student) session.get(L00024Student.class, student.getId());

			// this should retrieve the guide object, by default @OneToMany entities are EAGERLY fetched
			L00024Guide guideDb = studentDb.getGuide();
			assertNotNull("The guideDb object should not be null", guideDb);

			txn.commit();
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
