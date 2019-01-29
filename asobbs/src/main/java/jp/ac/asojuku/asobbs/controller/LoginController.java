package jp.ac.asojuku.asobbs.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.service.LoginService;

@Controller
public class LoginController {

	@Autowired
	LoginService loginService;
	@Autowired
	HttpSession session;
	
	@RequestMapping(value= {"/","/login"}, method=RequestMethod.GET)
    public ModelAndView login(@ModelAttribute("msg")String msg,ModelAndView mv) {
        mv.setViewName("login");
        if( msg != null && msg.length() > 0) {
        	mv.addObject("msg", msg);
        }else {
        	mv.addObject("msg", "");
        }
        return mv;
    }

	@RequestMapping(value= {"/auth"}, method=RequestMethod.POST)
	public String auth(
			RedirectAttributes redirectAttributes,
			@RequestParam("mail")String mail, 
    		@RequestParam("password")String password,
    		ModelAndView mv
			) {

		String url;
		//login
		LoginInfoDto loginInfo;
		try {
			loginInfo = loginService.login(mail,password);
			if( loginInfo != null) {
				session.setAttribute("loginInfo",loginInfo);
				System.out.println("login success");
				url = "redirect:top";
			}else {
				System.out.println("login failure");
				redirectAttributes.addFlashAttribute("msg", "ログイン失敗！");
				url = "redirect:login";
			}
		} catch (AsoBbsSystemErrException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			url = "redirect:login";
		}
		
        return url;
	}
}
