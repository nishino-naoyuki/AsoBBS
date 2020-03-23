package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.DashBoadDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.BbsService;

@Controller
public class DashboadController {
	
	@Autowired
	BbsService bbsService;
	@Autowired
	HttpSession session;

	/**
	 * ダッシュボード用
	 * 
	 * @param msg
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/dashboad"}, method=RequestMethod.GET)
    public ModelAndView index(@ModelAttribute("msg")String msg,ModelAndView mv) {

		//ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//1週間以内の更新情報を取得する
		DashBoadDto dashBoadDto = bbsService.getRecentlyBbs(loginInfo);
		
		mv.addObject("dashBoadDto",dashBoadDto);
        mv.setViewName("/dashboad");
        return mv;
    }
}
