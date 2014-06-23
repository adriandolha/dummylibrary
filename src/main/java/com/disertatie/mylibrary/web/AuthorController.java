package com.disertatie.mylibrary.web;
import com.disertatie.mylibrary.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequestMapping("/authors")
@Controller
@RooWebScaffold(path = "authors", formBackingObject = Author.class)
public class AuthorController {
    @PersistenceContext
    public EntityManager entityManager;
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Author author, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, author);
            return "authors/create";
        }
        uiModel.asMap().clear();
        String jpaQuery = "SELECT o FROM Author o where o.name like '%"+author.getName() + "%'";
        List<Author> authors = entityManager.createQuery(jpaQuery, Author.class).getResultList();
        if(authors.size() > 0){
            throw new Exception("Author " +  author.getName() +" already exists!");
        }
        author.persist();
        return "redirect:/authors/" + encodeUrlPathSegment(author.getId().toString(), httpServletRequest);
    }
}
