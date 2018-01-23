package jdbcNative;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jdbcNative.domain.Book;
import jdbcNative.domain.Chapter;
import jdbcNative.domain.Publisher;
import jdbcNative.service.BookStoreService;

public class L00010_CR_BookStoreClient {

    @Test
    public void persistBook() {
        BookStoreService bookStoreService = new BookStoreService();

        // persisting object graph
        Publisher publisher = new Publisher("MANN", "Manning Publications Co.");

        Book book = new Book("9781617290459", "Java Persistence with Hibernate, Second Edition", publisher);

        List<Chapter> chapters = new ArrayList<Chapter>();
        Chapter chapter1 = new Chapter("Introducing JPA and Hibernate", 1);
        chapters.add(chapter1);
        Chapter chapter2 = new Chapter("Domain Models and Metadata", 2);
        chapters.add(chapter2);

        book.setChapters(chapters);

        bookStoreService.persistObjectGraph(book);

        // retrieving object graph
        Book bookDb = bookStoreService.retrieveObjectGraph("9781617290459");
        System.out.println(bookDb);
    }
}
