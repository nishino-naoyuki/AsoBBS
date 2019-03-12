package jp.ac.asojuku.asobbs.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.ac.asojuku.asobbs.config.MessageProperty;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.form.LoginForm;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.param.StringConst;
import jp.ac.asojuku.asobbs.service.LoginService;

/**
 * ログイン関連のコントローラー
 * @author nishino
 *
 */
@Controller
public class LoginController {
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	LoginService loginService;
	@Autowired
	HttpSession session;
	
	@RequestMapping(value= {"/","/login"}, method=RequestMethod.GET)
    public ModelAndView login(
    		@ModelAttribute("msg")String msg,
    		@ModelAttribute("mail")String mail,
    		ModelAndView mv,
    		HttpServletRequest request,
    		HttpServletResponse response) throws AsoBbsSystemErrException {
		
		//トークンによる認証
        if(  authToken(request,response) ) {
        	return new ModelAndView("redirect:dashboad");
        }
        
        //エラーメッセージがあればメッセージを仕込んでおく
        if( msg != null && msg.length() > 0) {
        	mv.addObject("msg", msg);
        }else {
        	mv.addObject("msg", "");
        }
        LoginForm form = new LoginForm();
        form.setMail(mail);
    	mv.addObject("loginForm", form);
        mv.setViewName("login");
        
        return mv;
    }
	
	/**
	 * クッキーを使った認証を行う
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	private boolean authToken(
			HttpServletRequest request,HttpServletResponse response) throws AsoBbsSystemErrException {
		boolean isAuthOk = false;

        Cookie[] cookies = request.getCookies();
        if( cookies == null ) {
        	logger.info("authToken: cookie is null");
        	return false;
        }
        
        for(Cookie cookie : cookies) {
    		logger.info("cookie("+cookie.getName()+")");
        	if( StringConst.COOKIE_TOKEN.equals( cookie.getName()) ){
        		String token = cookie.getValue();
        		LoginInfoDto loginInfo = loginService.authToken(token);
        		if( loginInfo != null) {
        			//トークンを発行して、クッキーに保存
        			String newToken = loginService.createLoginToken(loginInfo.getUserId());
        			response.addCookie(new Cookie(StringConst.COOKIE_TOKEN, newToken));
        			//セッションにログイン情報を保存
        			session.setAttribute(SessionConst.LOGININFO,loginInfo);
        			isAuthOk =  true;
        		}
        	}
        }
        return isAuthOk;
	}

	/**
	 * @param redirectAttributes
	 * @param mail
	 * @param password
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException 
	 */
	@RequestMapping(value= {"/auth"}, method=RequestMethod.POST)
	public String auth(
			RedirectAttributes redirectAttributes,
			LoginForm form,
    		ModelAndView mv,
    		HttpServletResponse response
			) throws AsoBbsSystemErrException {

		String url;
		//login
		LoginInfoDto loginInfo = null;
		//ログイン処理を行う
		loginInfo = loginService.login(form.getMail(),form.getPassword());
		if( loginInfo != null) {
			//トークンを発行して、クッキーに保存
			String token = loginService.createLoginToken(loginInfo.getUserId());
			response.addCookie(new Cookie(StringConst.COOKIE_TOKEN, token));
			//セッションにログイン情報を保存
			session.setAttribute(SessionConst.LOGININFO,loginInfo);
			url = "redirect:dashboad";
		}else {
			url = fowardLoginError(redirectAttributes,form);
		}
		
        return url;
	}

	/**
	 * ログアウト処理
	 * @param redirectAttributes
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/logout"}, method=RequestMethod.GET)
	public String logout(HttpServletRequest request,RedirectAttributes redirectAttributes) throws AsoBbsSystemErrException {
		//ログアウトメッセージを取得
		String errMsg = MessageProperty.getInstance().getProperty(MessageProperty.LOGOUT_MSG);
		//エラーメッセージをセット
		redirectAttributes.addFlashAttribute("msg", errMsg);
				
		//セッション破棄
		session.invalidate();
		//DBのトークンを削除する
		Cookie[] cookies = request.getCookies();
		if( cookies != null ) {
	        for(Cookie cookie : cookies) {
	        	if( StringConst.COOKIE_TOKEN.equals( cookie.getName()) ){
	        		String token = cookie.getValue();
	        		loginService.logout(token);
	        		break;
	        	}
	        }
		}
		
		//ログイン画面へリダイレクト
		//ログアウトの時はauto=falseをつけて自動ログインを防ぐ
		return "redirect:login";
	}
	
	/**
	 * ログインエラー時の処理
	 * 
	 * @param redirectAttributes
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	private String fowardLoginError(RedirectAttributes redirectAttributes,LoginForm form) throws AsoBbsSystemErrException {
		//エラーメッセージを取得
		String errMsg = MessageProperty.getInstance().getProperty(MessageProperty.LOGIN_ERR_LOGINERR);
		//エラーメッセージをセット
		redirectAttributes.addFlashAttribute("msg", errMsg);
		redirectAttributes.addFlashAttribute("mail", form.getMail());
		
		return "redirect:login";
	}
}
