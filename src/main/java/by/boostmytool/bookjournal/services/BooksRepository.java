package by.boostmytool.bookjournal.services;

import by.boostmytool.bookjournal.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<Book,Integer> {
}
