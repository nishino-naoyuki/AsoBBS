package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.service.CourseService;

@Controller
@RequestMapping(value= {"/room"})
public class RoomController {

	@Autowired
	CourseService courseService;
	
	@RequestMapping(value= {"/input"}, method=RequestMethod.GET)
    public ModelAndView input(@ModelAttribute("msg")String msg,ModelAndView mv) {

        //学科の一覧を取得する
        List<CourseDto> list = courseService.getAllList();
        
        mv.setViewName("input_room");
        mv.addObject("courseList",list);
        
        return mv;
    }
}
