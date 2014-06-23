package com.disertatie.mylibrary.web;
import com.disertatie.mylibrary.domain.Author;
import com.disertatie.mylibrary.domain.Language;
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

@RequestMapping("/languages")
@Controller
@RooWebScaffold(path = "languages", formBackingObject = Language.class)
public class LanguageController {
    @PersistenceContext
    public EntityManager entityManager;
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Language language, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, language);
            return "languages/create";
        }
        uiModel.asMap().clear();
        String jpaQuery = "SELECT o FROM Language o where o.name like '%"+language.getName() + "%'";
        List<Language> languages = entityManager.createQuery(jpaQuery, Language.class).getResultList();
        if(languages.size() > 0){
            throw new Exception("Language " +  language.getName() +" already exists!");
        }
        language.persist();
        return "redirect:/languages/" + encodeUrlPathSegment(language.getId().toString(), httpServletRequest);
    }

}
