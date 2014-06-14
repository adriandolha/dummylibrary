// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.disertatie.mylibrary.web;

import com.disertatie.mylibrary.domain.Book;
import com.disertatie.mylibrary.domain.Publisher;
import com.disertatie.mylibrary.web.PublisherController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect PublisherController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String PublisherController.create(@Valid Publisher publisher, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, publisher);
            return "publishers/create";
        }
        uiModel.asMap().clear();
        publisher.persist();
        return "redirect:/publishers/" + encodeUrlPathSegment(publisher.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String PublisherController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Publisher());
        return "publishers/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String PublisherController.show(@PathVariable("id") Integer id, Model uiModel) {
        uiModel.addAttribute("publisher", Publisher.findPublisher(id));
        uiModel.addAttribute("itemId", id);
        return "publishers/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String PublisherController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("publishers", Publisher.findPublisherEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Publisher.countPublishers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("publishers", Publisher.findAllPublishers(sortFieldName, sortOrder));
        }
        return "publishers/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String PublisherController.update(@Valid Publisher publisher, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, publisher);
            return "publishers/update";
        }
        uiModel.asMap().clear();
        publisher.merge();
        return "redirect:/publishers/" + encodeUrlPathSegment(publisher.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String PublisherController.updateForm(@PathVariable("id") Integer id, Model uiModel) {
        populateEditForm(uiModel, Publisher.findPublisher(id));
        return "publishers/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String PublisherController.delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Publisher publisher = Publisher.findPublisher(id);
        publisher.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/publishers";
    }
    
    void PublisherController.populateEditForm(Model uiModel, Publisher publisher) {
        uiModel.addAttribute("publisher", publisher);
        uiModel.addAttribute("books", Book.findAllBooks());
    }
    
    String PublisherController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
