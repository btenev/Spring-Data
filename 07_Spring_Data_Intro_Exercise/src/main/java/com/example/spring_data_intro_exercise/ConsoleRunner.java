package com.example.spring_data_intro_exercise;

import com.example.spring_data_intro_exercise.entities.Author;
import com.example.spring_data_intro_exercise.entities.Book;
import com.example.spring_data_intro_exercise.repositories.AuthorRepository;
import com.example.spring_data_intro_exercise.repositories.BookRepository;
import com.example.spring_data_intro_exercise.services.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final SeedService seedService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public ConsoleRunner(SeedService seedService, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.seedService = seedService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        this.seedService.seedAuthors();
//        this.seedService.seedCategories();
//        this.seedService.seedAll();

//        this.A_booksAfter2000();
//        this.B_authorsWithBooksBefore1990();
//        this.C_authorsByNumberOfBooks();
        this.D_findAllBooksByAuthorAndOrder();

    }

    private void D_findAllBooksByAuthorAndOrder() {
        List<Book> books = this.bookRepository
                .findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitleAsc("George", "Powell");

        books.forEach(b -> System.out.println(b.getTitle() + " " + b.getReleaseDate() + " " + b.getCopies()));

    }

    private void C_authorsByNumberOfBooks() {
        List<Author> authors
                = this.authorRepository.findAll();
        authors.stream()
                .sorted(Comparator.comparingInt((Author a) -> a.getBooks().size()).reversed())
                .forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName() + " " + a.getBooks().size()));
    }

    private void B_authorsWithBooksBefore1990() {
        LocalDate year1990 = LocalDate.of(1990, 1, 1);
        List<Author> releaseDateBefore1990 = this.authorRepository.findDistinctByBooksReleaseDateBefore(year1990);
        releaseDateBefore1990.forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));

    }

    private void A_booksAfter2000() {

        LocalDate year2000 = LocalDate.of(2000, 12, 31);
        List<Book> books = this.bookRepository.findByReleaseDateAfter(year2000);

        books.forEach(b -> System.out.println(b.getTitle()));
        System.out.println("Total count " + bookRepository.countByReleaseDateAfter(year2000));
    }
}
