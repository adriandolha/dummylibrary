package com.disertatie.mylibrary.web;

import com.disertatie.mylibrary.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm(Model model){
        model.addAttribute("user", new User());
        return "users/create";
    }
}
