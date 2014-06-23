package com.disertatie.mylibrary.web;
import com.disertatie.mylibrary.domain.Author;
import com.disertatie.mylibrary.domain.Publisher;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/publishers")
@Controller
@RooWebScaffold(path = "publishers", formBackingObject = Publisher.class)
public class PublisherController {
    @PersistenceContext
    public EntityManager entityManager;
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Publisher publisher, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, publisher);
            return "publishers/create";
        }
        uiModel.asMap().clear();
        String jpaQuery = "SELECT o FROM Publisher o where o.name like '%"+publisher.getName() + "%'";
        List<Publisher> authors = entityManager.createQuery(jpaQuery, Publisher.class).getResultList();
        if(authors.size() > 0){
            throw new Exception("Publisher " +  publisher.getName() +" already exists!");
        }
        publisher.persist();
        return "redirect:/publishers/" + encodeUrlPathSegment(publisher.getId().toString(), httpServletRequest);
    }
}
