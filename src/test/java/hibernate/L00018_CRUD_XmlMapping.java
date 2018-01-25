package hibernate;

import static org.junit.Assert.assertNull;

import org.hibernate.Session;
import org.junit.Test;

import hibernate.util.HibernateUtil;
import hibernate.xmlMapping.domain.Message;

public class L00018_CRUD_XmlMapping {

	@Test
	public void helloWorld() {

		Session session = HibernateUtil.getSessionFactoryXmlMapping().openSession();
		// create the message ---------------------------------------------
		session.beginTransaction();

		Message message = new Message("Hello World with Hibernate & XML Mapping");

		session.save(message);

		session.getTransaction().commit();

		System.out.println("Created :" + message.toString());

		// Finding objects ---------------------------------------------
		session.beginTransaction();

		Message msg = (Message) session.get(Message.class, message.getId());

		session.getTransaction().commit();

		System.out.println("Read :" + msg.toString());

		// Updating objects ---------------------------------------------
		session.beginTransaction();

		Message msgUpdate = (Message) session.get(Message.class, message.getId());
		msgUpdate.setText("Hello Automatic Dirty Checking");

		session.getTransaction().commit();

		System.out.println("Updated :" + msgUpdate.toString());

		// Deleting objects ---------------------------------------------
		session.beginTransaction();

		Message msgDelete = (Message) session.get(Message.class, message.getId());
		session.delete(msgDelete);

		session.getTransaction().commit();

		System.out.println("Deleted :" + msgUpdate.toString());

		Message msgDeleted = (Message) session.get(Message.class, message.getId());
		assertNull("Message with ID " + message.getId() + " should be deleted", msgDeleted);

		session.close();
	}
}