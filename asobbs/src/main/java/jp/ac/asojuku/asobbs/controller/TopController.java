package jp.ac.asojuku.asobbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TopController {

	@RequestMapping(value= {"/top"}, method=RequestMethod.GET)
    public ModelAndView index(@ModelAttribute("msg")String msg,ModelAndView mv) {
        mv.setViewName("/top");
        return mv;
    }
}
