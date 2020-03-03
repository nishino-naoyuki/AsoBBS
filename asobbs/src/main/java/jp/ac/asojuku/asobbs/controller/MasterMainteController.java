package jp.ac.asojuku.asobbs.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.service.CourseService;

@Controller
@RequestMapping(value= {"/master"})
public class MasterMainteController {
	
	@Autowired
	CourseService courseService;

	@RequestMapping(value= {"/","/list"}, method=RequestMethod.GET)
    public ModelAndView mainte(
    		ModelAndView mv) throws AsoBbsSystemErrException {

        mv.setViewName("master_mainte");
        
        return mv;
    }

}
