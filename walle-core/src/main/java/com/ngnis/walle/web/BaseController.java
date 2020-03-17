package com.ngnis.walle.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author houyi.wh
 * @since 2018-09-14
 */
@RestController
public class BaseController {

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("redirect:index.html");
    }


}
