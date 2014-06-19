package com.disertatie.mylibrary.web;
import com.disertatie.mylibrary.domain.*;
import com.disertatie.mylibrary.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.io.UnsupportedEncodingException;

@RequestMapping("/books")
@Controller
@RooWebScaffold(path = "books", formBackingObject = Book.class)
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @InitBinder
    public void init(final WebDataBinder dataBinder) {

        dataBinder.registerCustomEditor(Book.class, new BookPropertyEditor());
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Book book, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, book);
            return "books/create";
        }
        uiModel.asMap().clear();
        Book existingBook = bookRepository.findByTitle(book.getTitle());
        if(existingBook != null){
            throw new Exception("Book already exists with title" + book.getTitle());
        }
        book = bookRepository.saveAndFlush(book);
        return "redirect:/books/" + encodeUrlPathSegment(book.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Book());
        return "books/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("book", bookService.findBook(id));
        uiModel.addAttribute("itemId", id);
        return "books/show";
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
        return "books/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Book book, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, book);
            return "books/update";
        }
        uiModel.asMap().clear();
        bookRepository.saveAndFlush(book);
        return "redirect:/books/" + encodeUrlPathSegment(book.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, bookService.findBook(id));
        return "books/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Book book = bookService.findBook(id);
        bookRepository.delete(book);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/books";
    }

    void populateEditForm(Model uiModel, Book book) {
        uiModel.addAttribute("book", book);
        uiModel.addAttribute("authors", Author.findAllAuthors());
        uiModel.addAttribute("categorys", Category.findAllCategorys());
        uiModel.addAttribute("publishers", Publisher.findAllPublishers());
        uiModel.addAttribute("languages", Language.findAllLanguages());

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


    private class BookPropertyEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String id) throws IllegalArgumentException {
            Book book = null;
            if(id!=null && !id.equals("")){
                book = bookService.findBook(Long.valueOf(id));
                System.out.println("id " + id + ": " + book.getTitle());
            }
            setValue(book);
        }
    }
}
