package jp.ac.asojuku.asobbs.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.service.CourseService;

@RestController
public class MasterMainteListController {
	
	@Autowired
	CourseService courseService;

	@RequestMapping(value= {"/getcouselist"}, method=RequestMethod.GET)
	public List<CourseDto> getCourseList(){
		//学科の一覧を取得する
        return courseService.getAllList();
	}
}
