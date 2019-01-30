package jp.ac.asojuku.asobbs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.service.BbsService;

@Controller
public class DashboadController {
	
	@Autowired
	BbsService bbsService;

	@RequestMapping(value= {"/dashboad"}, method=RequestMethod.GET)
    public ModelAndView index(@ModelAttribute("msg")String msg,ModelAndView mv) {
		
		//1週間以内の更新情報を取得する
		bbsService.getRecentlyBbs();
		
        mv.setViewName("/dashboad");
        return mv;
    }
}
