package com.disertatie.mylibrary.repository;

import com.disertatie.mylibrary.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
   public Book findByTitle(String title);
    @Query( value = "select book.*from book book, author author, bookauthors ba " +
            "WHERE ba.bookId=book.id and ba.authorId=author.id and ba.bookId=book.id "+
            "and author.name like %:author%", nativeQuery = true)
    public List<Book> findByAuthorLike(@Param("author") String author);

    @Query( value = "select book.* from book book " +
            " inner join category category on book.categoryId=category.id" +
            " WHERE category.name like %:category%", nativeQuery = true)
    public List<Book> findByCategoryLike(@Param("category") String category);
}
