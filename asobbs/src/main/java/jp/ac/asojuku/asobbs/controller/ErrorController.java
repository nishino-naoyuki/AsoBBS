package jp.ac.asojuku.asobbs.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value= {"/error"})
public class ErrorController {

	@RequestMapping(value= {"/accessdeny"}, method=RequestMethod.GET)
    public ModelAndView input(ModelAndView mv) {
		
        mv.setViewName("/error/accessdeny");
        
        return mv;
    }
}
