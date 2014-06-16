package com.disertatie.mylibrary.web;

import com.disertatie.mylibrary.domain.*;
import com.disertatie.mylibrary.repository.BookRepository;
import com.mysema.query.jpa.impl.JPAQuery;
import org.hibernate.criterion.LikeExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/books/search")
public class BookSearchController {
    public static final String LIKE_EXPRESSION = "%%%s%%";
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @PersistenceContext
    public EntityManager entityManager;
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String search(@Valid Book book, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        int sizeNo = 10;
        Integer page = 1;
        JPAQuery query = new JPAQuery(entityManager);
        QBook books = QBook.book;
        List<Book> resultBooks = query.from(books).where(
                books.title.like(String.format(LIKE_EXPRESSION, book.getTitle())))
        .list(books);
        uiModel.addAttribute("books", resultBooks);
        float nrOfPages = (float) resultBooks.size() / sizeNo;
        uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        return "books/list";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String searchForm(Model uiModel) {
        uiModel.addAttribute(new Book());
        return "books/search";
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

    @ModelAttribute(value = "authors")
    public List<Author> getAuthors(){
        return Author.findAllAuthors();
    }

    @ModelAttribute(value = "categorys")
    public List<Category> getCategorys(){
        return Category.findAllCategorys();
    }

    @ModelAttribute(value = "languages")
    public List<Language> getLanguages(){
        return Language.findAllLanguages();
    }

    @ModelAttribute(value = "publishers")
    public List<Publisher> getPaublishers(){
        return Publisher.findAllPublishers();
    }
}
