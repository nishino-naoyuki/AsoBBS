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

/**
 * マスターメンテのコントローラ
 * @author nishino
 *
 */
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

	/**
	 * 学科一覧の表示画面
	 * 
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/courselist"}, method=RequestMethod.GET)
	public ModelAndView courseList(
    		ModelAndView mv) throws AsoBbsSystemErrException {

        mv.setViewName("list_course_master");
        
        return mv;
    }
	

}
