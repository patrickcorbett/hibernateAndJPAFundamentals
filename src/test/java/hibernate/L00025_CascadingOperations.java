
package hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import hibernate.annotationMapping.domain.L00025Guide;
import hibernate.annotationMapping.domain.L00025Student;
import hibernate.util.HibernateUtil;

public class L00025_CascadingOperations {

	@Test
	public void usingCascadeAnnotations() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			txn.begin();

			L00025Guide guide = new L00025Guide("2000MO10789", "Mike Lawson", 1000);
			L00025Student student = new L00025Student("2014JT50123", "John Smith", guide);

			// persist must be used not save, otherwise the cascade is not performed
			// session.save(student);
			session.persist(student);

			// both the entities should be saved with ids provided by the RDBMS
			assertNotNull("ID should not be null", student.getId());
			assertNotNull("ID should not be null", student.getId());

			// delete the student with then deletes the guide
			session.delete(student);

			L00025Student studentDb = (L00025Student) session.get(L00025Student.class, student.getId());
			assertNull("The student should be deleted", studentDb);
			
			L00025Guide guideDb = (L00025Guide) session.get(L00025Guide.class, student.getGuide().getId());
			assertNull("The guide should be deleted", guideDb);
			
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
