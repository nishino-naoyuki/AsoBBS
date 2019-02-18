package jp.ac.asojuku.asobbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.form.BbsInputForm;

@Controller
@RequestMapping(value= {"/bbs"})
public class BbsController {

	@RequestMapping(value= {"/input"}, method=RequestMethod.GET)
    public ModelAndView input(@ModelAttribute("msg")String msg,ModelAndView mv) {
		
		BbsInputForm form = new BbsInputForm();
        mv.setViewName("/input_bbs");
        mv.addObject("bbsInputForm",form);
        return mv;
    }
}
