
package hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hibernate.annotationMapping.domain.L00027Guide;
import hibernate.annotationMapping.domain.L00027Student;
import hibernate.util.HibernateUtil;

public class L00027_BiDirectionalManyToOne {

	@Before
	public void initializeTableState() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			/*
			 * Using AUTOMATIC DIRTY CHECKING - any read / modified items within the
			 * transaction will be automatically updated - here an explicit persist() is
			 * called
			 */
			txn.begin();

			// Create two guides and assign students to guide 1
			L00027Guide guide1 = new L00027Guide("2000MO10789", "Mike Lawson", 1000);
			L00027Guide guide2 = new L00027Guide("2000IM10901", "Ian Lamb", 2000);

			L00027Student student1 = new L00027Student("2014JT50123", "John Smith", guide1);
			L00027Student student2 = new L00027Student("2014AL50456", "Amy Gill", guide1);

			guide1.getStudents().add(student1);
			guide1.getStudents().add(student2);

			session.persist(guide1);
			session.persist(guide2);

			assertEquals("Guide 1 should be the guide for student 1", guide1.getId(), student1.getGuide().getId());
			assertEquals("Guide 1 should be the guide for student 2", guide1.getId(), student2.getGuide().getId());
			assertEquals("Guide 2 should not be assigned any students", 0, guide2.getStudents().size());

			txn.commit();

		} catch (HibernateException e) {
			fail("Hibernate Exception : " + e.getMessage());
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

	@After
	public void resetTableState() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			txn.begin();

			// clear the tables
			Query truncate1 = session.createSQLQuery("TRUNCATE TABLE l00027student");
			truncate1.executeUpdate();

			Query truncate2 = session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 0");
			truncate2.executeUpdate();
			truncate2 = session.createSQLQuery("TRUNCATE TABLE l00027guide");
			truncate2.executeUpdate();
			truncate2 = session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 1");
			truncate2.executeUpdate();

			txn.commit();

		} catch (HibernateException e) {
			fail("Hibernate Exception : " + e.getMessage());
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

	@Test
	public void updateInverseSide_DoesntUpdate() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			/*
			 * Using AUTOMATIC DIRTY CHECKING - any read / modified items within the
			 * transaction will be automatically updated - in this case however it wont as
			 * explained below
			 */
			txn.begin();

			// Updating inverse end - student 2 should be associated with guide 2 but this
			// fails as the Set.add() method doesnt associate the student with the guide, it
			// merely appends it to the set and this results in no update
			L00027Guide guide1 = (L00027Guide) session.get(L00027Guide.class, 1L);
			L00027Guide guide2 = (L00027Guide) session.get(L00027Guide.class, 2L);
			L00027Student student1 = (L00027Student) session.get(L00027Student.class, 1L);
			L00027Student student2 = (L00027Student) session.get(L00027Student.class, 2L);

			guide2.getStudents().add(student2);

			txn.commit();

			assertEquals("Guide 1 should be the guide for student 1", new Long(1), student1.getGuide().getId());
			assertEquals("Guide 2 should be the guide for student 2", new Long(1), student2.getGuide().getId());

		} catch (HibernateException e) {
			fail("Hibernate Exception : " + e.getMessage());
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

	@Test
	public void updateInverseSideViaCustomMethod() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			/*
			 * Using AUTOMATIC DIRTY CHECKING - any read / modified items within the
			 * transaction will be automatically updated
			 */
			txn.begin();

			// Updating inverse end (after adding addStudent(Student) in Guide entity)
			// student 1 should now be associated with guide 2 thanks to the custom method
			// which modifies the student entity to reference the guide 2
			L00027Guide guide1 = (L00027Guide) session.get(L00027Guide.class, 1L);
			L00027Guide guide2 = (L00027Guide) session.get(L00027Guide.class, 2L);
			L00027Student student1 = (L00027Student) session.get(L00027Student.class, 1L);
			L00027Student student2 = (L00027Student) session.get(L00027Student.class, 2L);

			guide2.addStudent(student1);

			txn.commit();

			assertEquals("Guide 1 should be the guide for student 2", guide1.getId(), student2.getGuide().getId());
			assertEquals("Guide 2 should be the guide for student 1", guide2.getId(), student1.getGuide().getId());

		} catch (HibernateException e) {
			fail("Hibernate Exception : " + e.getMessage());
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

	@Test
	public void updateOwner() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			/*
			 * Using AUTOMATIC DIRTY CHECKING - any read / modified items within the
			 * transaction will be automatically updated
			 */
			txn.begin();

			// Updating owner - student changes its guide
			L00027Guide guide1 = (L00027Guide) session.get(L00027Guide.class, 1L);
			L00027Guide guide2 = (L00027Guide) session.get(L00027Guide.class, 2L);
			L00027Student student1 = (L00027Student) session.get(L00027Student.class, 1L);
			L00027Student student2 = (L00027Student) session.get(L00027Student.class, 2L);

			student2.setGuide(guide2);

			txn.commit();

			assertEquals("Guide 1 should be the guide for student 1", guide1.getId(), student1.getGuide().getId());
			assertEquals("Guide 2 should be the guide for student 2", guide2.getId(), student2.getGuide().getId());

		} catch (HibernateException e) {
			fail("Hibernate Exception : " + e.getMessage());
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
