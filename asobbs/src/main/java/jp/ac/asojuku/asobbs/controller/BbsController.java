package jp.ac.asojuku.asobbs.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.form.BbsInputForm;
import jp.ac.asojuku.asobbs.form.RoomInputForm;

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

	@RequestMapping(value= {"/confirm"}, method=RequestMethod.POST)
    public ModelAndView confirm(ModelAndView mv,
    		@Valid BbsInputForm bbsInputForm,
			BindingResult bindingResult) {
		
        mv.setViewName("/input_bbs");
        mv.addObject("bbsInputForm",bbsInputForm);
        return mv;
    }
}
