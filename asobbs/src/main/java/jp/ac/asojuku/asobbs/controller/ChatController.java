package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.dto.RoomListDto;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.CourseService;
import jp.ac.asojuku.asobbs.service.RoomService;

@Controller
@RequestMapping(value= {"/chat"})
public class ChatController {
	@Autowired
	HttpSession session;
	
	@Autowired
	RoomService roomService;

	@Autowired
	CourseService courseService;
	
	@RequestMapping(value= {"/view"}, method=RequestMethod.GET)
    public ModelAndView view(ModelAndView mv) {

        //学科の一覧を取得する
        List<CourseDto> list = courseService.getAllList();
		mv.setViewName("/view_chat");
        mv.addObject("courseList",list);
		
		return mv;
    }
}
