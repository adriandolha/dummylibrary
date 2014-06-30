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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
public class BookSearchController {
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
    public static final String LIKE_EXPRESSION = "%%%s%%";
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @PersistenceContext
    public EntityManager entityManager;
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String search(BookSearch bookSearch, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        SearchType searchType = SearchType.valueOf(bookSearch.getSearchType().toUpperCase());
        int sizeNo = 10;
        Integer page = 1;
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
        uiModel.addAttribute("books", resultBooks);
        float nrOfPages = (float) resultBooks.size() / sizeNo;
        uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        return "books/searchList";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String searchForm(Model uiModel) {
        uiModel.addAttribute(new BookSearch());
        return "books/searchForm";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("books", bookService.findBookEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) bookRepository.count() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            Sort sort = sortOrder != null && sortFieldName!= null? new Sort(Sort.Direction.fromString(sortOrder), sortFieldName):null;
            uiModel.addAttribute("books", bookRepository.findAll(sort));
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
