package com.disertatie.mylibrary.repository;

import com.disertatie.mylibrary.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BookRepository extends JpaRepository<Book, Long> {
   public Book findByTitle(String title);
}
