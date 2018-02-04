
package hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Test;

import hibernate.annotationMapping.domain.L00030Customer;
import hibernate.annotationMapping.domain.L00030Passport;
import hibernate.util.HibernateUtil;

public class L00030_BiDirectionalOneToOne {

	@After
	public void resetTableState() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			txn.begin();

			// clear the tables
			Query truncate1 = session.createSQLQuery("TRUNCATE TABLE l00030customer");
			truncate1.executeUpdate();

			Query truncate2 = session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 0");
			truncate2.executeUpdate();
			truncate2 = session.createSQLQuery("TRUNCATE TABLE l00030passport");
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
	public void oneToOneWithCustomerAssignmentInPassport() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			txn.begin();

			/*
			 * As the passport is passed to the customer class, the customer knows the
			 * passport but the internal field "customer" of the passport class has not been
			 * set. When these are saved the entity manager saves these java classes as is
			 * which means that when the passport is retrieved from the session the customer
			 * field is still null!
			 */
			L00030Passport passport = new L00030Passport("925076473");
			L00030Customer customer = new L00030Customer("Nicole Scott", passport);

			// Explicit assignment, this should ideally be done in the customer constructor
			passport.setCustomer(customer);
			
			session.persist(customer);

			// get the customer by its passport
			L00030Passport dbPassport = (L00030Passport) session.get(L00030Passport.class, passport.getId());

			assertEquals("The passport id should match", customer.getPassport().getId(), dbPassport.getId());
			assertEquals("The customer id should match", customer.getId(), dbPassport.getCustomer().getId());

			txn.commit();
		} catch (Exception e) {
			fail("Exception : " + e.getMessage());
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
	public void oneToOneWithoutCustomerAssignmentInPassport() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			txn.begin();

			/*
			 * As the passport is passed to the customer class, the customer knows the
			 * passport but the internal field "customer" of the passport class has not been
			 * set. When these are saved the entity manager saves these java classes as is
			 * which means that when the passport is retrieved from the session the customer
			 * field is still null!
			 */
			L00030Passport passport = new L00030Passport("925076473");
			L00030Customer customer = new L00030Customer("Nicole Scott", passport);

			session.persist(customer);

			// get the customer by its passport
			L00030Passport dbPassport = (L00030Passport) session.get(L00030Passport.class, passport.getId());

			assertNull("Customer id in the passport entity is null", dbPassport.getCustomer());

			txn.commit();
		} catch (Exception e) {
			fail("Exception : " + e.getMessage());
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
	public void oneToOneWithoutCustomerAssignmentInPassportDifferentSession() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			txn.begin();

			L00030Passport passport = new L00030Passport("925076473");
			L00030Customer customer = new L00030Customer("Nicole Scott", passport);

			session.persist(customer);

			txn.commit();

			/*
			 * As the passport is passed to the customer class, the customer knows the
			 * passport but the internal field "customer" of the passport class has not been
			 * set. When these are saved the entity manager saves these java classes as is
			 * which means that when the passport is retrieved from the session the customer
			 * field is still null!
			 * 
			 * closing and opening a new session fixes this as then hibernate fetches the
			 * customer eagerly
			 */
			session.close();
			session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();

			// get the customer by its passport
			L00030Passport dbPassport = (L00030Passport) session.get(L00030Passport.class, passport.getId());

			assertEquals("The passport id should match", customer.getPassport().getId(), dbPassport.getId());
			assertEquals("The customer id should match", customer.getId(), dbPassport.getCustomer().getId());
		} catch (Exception e) {
			fail("Exception : " + e.getMessage());
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
	public void oneToOneWithoutCustomerAssignmentInPassportClearedSession() {

		Session session = HibernateUtil.getSessionFactoryAnnotationMapping().openSession();
		Transaction txn = session.getTransaction();
		try {
			txn.begin();

			L00030Passport passport = new L00030Passport("925076473");
			L00030Customer customer = new L00030Customer("Nicole Scott", passport);

			session.persist(customer);

			/*
			 * As the passport is passed to the customer class, the customer knows the
			 * passport but the internal field "customer" of the passport class has not been
			 * set. When these are saved the entity manager saves these java classes as is
			 * which means that when the passport is retrieved from the session the customer
			 * field is still null!
			 * 
			 * Clearing the session fixes this as then hibernate fetches the customer
			 * eagerly
			 */
			session.clear();

			// get the customer by its passport
			L00030Passport dbPassport = (L00030Passport) session.get(L00030Passport.class, passport.getId());

			assertNotNull("The customer field should not be null", dbPassport.getCustomer());

			assertEquals("The passport id should match", customer.getPassport().getId(), dbPassport.getId());
			assertEquals("The customer id should match", customer.getId(), dbPassport.getCustomer().getId());

			txn.commit();
		} catch (Exception e) {
			fail("Exception : " + e.getMessage());
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
