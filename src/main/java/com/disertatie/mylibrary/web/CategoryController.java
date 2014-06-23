package com.disertatie.mylibrary.web;
import com.disertatie.mylibrary.domain.Category;
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

@RequestMapping("/categorys")
@Controller
@RooWebScaffold(path = "categorys", formBackingObject = Category.class)
public class CategoryController {
    @PersistenceContext
    public EntityManager entityManager;
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String reate(@Valid Category category, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, category);
            return "categorys/create";
        }
        uiModel.asMap().clear();
        String jpaQuery = "SELECT o FROM Category o where o.name like '%"+category.getName() + "%'";
        List<Category> categorys = entityManager.createQuery(jpaQuery, Category.class).getResultList();
        if(categorys.size() > 0){
            throw new Exception("Category " +  category.getName() +" already exists!");
        }
        category.persist();
        return "redirect:/categorys/" + encodeUrlPathSegment(category.getId().toString(), httpServletRequest);
    }
}
