package com.disertatie.mylibrary.web;

import com.disertatie.mylibrary.domain.*;
import com.disertatie.mylibrary.repository.BookRepository;
import com.mysema.query.jpa.impl.JPAQuery;
import org.hibernate.criterion.LikeExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/books/search")
@SessionAttributes("filter")
public class BookSearchController {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @PersistenceContext
    public EntityManager entityManager;
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String search(BookSearch bookSearch, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        uiModel.addAttribute("filter", bookSearch);
        List<Book> resultBooks = bookService.getBooks(bookSearch);
        int sizeNo = 5;
        float nrOfPages = (float) resultBooks.size() / sizeNo;
        int toIndex = Math.min(5, resultBooks.size());
        uiModel.addAttribute("books", resultBooks.subList(0, toIndex));
        uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        uiModel.addAttribute("size", sizeNo);
        return "books/searchList";
    }

    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String searchForm(Model uiModel) {
        uiModel.addAttribute(new BookSearch());
        return "books/searchForm";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        BookSearch bookSearch = (BookSearch) uiModel.asMap().get("filter");
        if (page != null || size != null) {
            int sizeNo = size == null ? 5 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            List<Book> books = null;
            if (bookSearch == null){
                books = bookService.findBookEntries(firstResult, sizeNo);
                size = books.size();
            } else{
                BookService.Page bookEntries = bookService.findBookEntries(bookSearch, firstResult, sizeNo);
                books = bookEntries.getBooks();
                size = bookEntries.getSize();
            }
            uiModel.addAttribute("books", books);
            float nrOfPages = (float) size / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
            uiModel.addAttribute("size", sizeNo);
        }
        return "books/searchList";
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

    @ModelAttribute(value = "searchTypes")
    public List<String> getSearchTypes(){
        return Arrays.asList(new String[]{"title", "author", "category"});
    }

    @ModelAttribute(value = "publishers")
    public List<Publisher> getPaublishers(){
        return Publisher.findAllPublishers();
    }
}
