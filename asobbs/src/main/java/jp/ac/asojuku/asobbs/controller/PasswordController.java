package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.CategoryListDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.form.BbsInputForm;
import jp.ac.asojuku.asobbs.form.PasswordChangeForm;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.LoginService;
import jp.ac.asojuku.asobbs.service.UserService;
import jp.ac.asojuku.asobbs.validator.PasswordValidator;

/**
 * パスワード関連
 * @author nishino
 *
 */
@Controller
@RequestMapping(value= {"/pwd"})
public class PasswordController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	LoginService loginService;

	@Autowired
	UserService userService;
	

	/**
	 * パスワード変更入力画面
	 * 
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/input"}, method=RequestMethod.GET)
    public ModelAndView input(ModelAndView mv) {
		
		PasswordChangeForm form = new PasswordChangeForm();
        mv.setViewName("/input_passchg");
        mv.addObject("passwordChangeForm",form);
        
        
        return mv;
    }

	/**
	 * パスワード変更処理
	 * 
	 * @param form
	 * @param bindingResult
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/change"}, method=RequestMethod.POST)
    public String change(
    		@Valid PasswordChangeForm form,
    		BindingResult bindingResult,
    		ModelAndView mv) throws AsoBbsSystemErrException {
		
		//ユーザー情報をセッションから取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//エラーチェック
		PasswordValidator.match(form.getNewPassword1(),form.getNewPassword2(),bindingResult);
		
		LoginInfoDto chkLoginInfo = loginService.login(
															loginInfo.getMail(),
															form.getOldPassword()
															);
		if( chkLoginInfo == null ) {
			//nullということはパスワードが違う
			PasswordValidator.setErrorcode("oldPassword",bindingResult,ErrorCode.ERR_PWD_CHG_OLD_PWD_WRONG);
		}
		
		if( bindingResult.hasErrors()) {
			return "/input_passchg";
		}
		
		//パスワード変更
		userService.changePassword(
				loginInfo.getUserId(),
				loginInfo.getMail(),
				form.getNewPassword1()
				);

        return "redirect:/pwd/chg_complete";
	}

	/**
	 * パスワード変更完了処理
	 * 
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/chg_complete"}, method=RequestMethod.GET)
    public ModelAndView complete(ModelAndView mv) throws AsoBbsSystemErrException {
		        
		mv.setViewName("complete_pwd_chg");
        
        return mv;
    }
}
