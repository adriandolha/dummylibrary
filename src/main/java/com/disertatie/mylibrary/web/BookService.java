package com.disertatie.mylibrary.web;

import com.disertatie.mylibrary.domain.Book;
import com.disertatie.mylibrary.domain.Book;
import com.disertatie.mylibrary.domain.QBook;
import com.disertatie.mylibrary.repository.BookRepository;
import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class BookService {
    public static final String LIKE_EXPRESSION = "%%%s%%";
    public enum SearchType{TITLE("title"), AUTHOR("author"), CATEGORY("category");
        String title;
        SearchType(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

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

    public List<Book> getBooks(BookSearch bookSearch) {
        SearchType searchType = SearchType.valueOf(bookSearch.getSearchType().toUpperCase());
        JPAQuery query = new JPAQuery(entityManager);
        QBook books = QBook.book;
        List<Book> resultBooks = null;
        switch (searchType){
            case TITLE:
                resultBooks = query.from(books).where(
                        books.title.like(String.format(LIKE_EXPRESSION, bookSearch.getValue())))
                        .list(books);
                break;
            case AUTHOR:
                resultBooks = bookRepository.findByAuthorLike(bookSearch.getValue());
                break;
            case CATEGORY:
                resultBooks = bookRepository.findByCategoryLike(bookSearch.getValue());
                break;
        }
        return resultBooks;
    }

    public Page findBookEntries(BookSearch bookSearch, int firstResult, int maxResults) {
        List<Book> books = getBooks(bookSearch);
        int to = firstResult + maxResults;
        return new Page(books.subList(firstResult, to < books.size() ? to : books.size()), books.size());
    }

    public static class Page{
        List<Book> books;
        int size;

        public Page(List<Book> books, int size) {
            this.books = books;
            this.size = size;
        }

        public List<Book> getBooks() {
            return books;
        }

        public void setBooks(List<Book> books) {
            this.books = books;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
