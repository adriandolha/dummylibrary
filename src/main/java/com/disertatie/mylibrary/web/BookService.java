package com.disertatie.mylibrary.web;

import com.disertatie.mylibrary.domain.Book;
import com.disertatie.mylibrary.domain.Book;
import com.disertatie.mylibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class BookService {
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");
    @Autowired
    BookRepository bookRepository;
    @PersistenceContext
    public EntityManager entityManager;
    public Book findBook(Long id){
        return bookRepository.findOne(id);
    }

    public List<Book> findAllBooks(){
        return bookRepository.findAll();
    }

    public List<Book> findBookEntries(int firstResult, int maxResults) {
        return entityManager.createQuery("SELECT o FROM Book o", Book.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public List<Book> findBookEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Book o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager.createQuery(jpaQuery, Book.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

}
