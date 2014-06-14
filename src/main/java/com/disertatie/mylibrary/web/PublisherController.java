package com.disertatie.mylibrary.web;
import com.disertatie.mylibrary.domain.Publisher;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/publishers")
@Controller
@RooWebScaffold(path = "publishers", formBackingObject = Publisher.class)
public class PublisherController {
}
