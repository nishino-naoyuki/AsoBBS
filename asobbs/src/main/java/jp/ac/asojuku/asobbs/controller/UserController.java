package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.service.CourseService;
import jp.ac.asojuku.asobbs.validator.UserValidator;

@Controller
@RequestMapping(value= {"/user"})
public class UserController {
	
	@Autowired
	CourseService courseService;

	/**
	 * ユーザー情報の入力
	 * @param msg
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/input"}, method=RequestMethod.GET)
    public ModelAndView input(@ModelAttribute("msg")String msg,ModelAndView mv) {
		
        mv.setViewName("input_user");
        
        //学科の一覧を取得する
        List<CourseDto> list = courseService.getAllList();
        
        mv.addObject("courseList",list);
        
        //エラーメッセージがあればメッセージを仕込んでおく
        if( msg != null && msg.length() > 0) {
        	mv.addObject("msg", msg);
        }else {
        	mv.addObject("msg", "");
        }
        return mv;
    }
	

	@RequestMapping(value= {"/cofirm"}, method=RequestMethod.GET)
    public ModelAndView cofirm(
    		@RequestParam("role")String role, 
    		@RequestParam("studentNo")String studentNo, 
    		@RequestParam("mailadress")String mailadress, 
    		@RequestParam("nickname")String nickname, 
    		@RequestParam("course_id")String course_id, 
    		@RequestParam("password1")String password1, 
    		@RequestParam("password2")String password2, 
    		@RequestParam("admission_year")String admission_year, 
    		ModelAndView mv) {

		try {
			//入力チェックを行う
			ActionErrors errs = 
					validateRequestParams(role,studentNo,mailadress,nickname,course_id,password1,password2,admission_year);

			//エラー情報をセットする
			mv.addObject("errs",errs);
			
			//画面遷移
	        mv.setViewName("confirm_user");
		} catch (AsoBbsSystemErrException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
        return mv;
	}
	
	/**
	 * リクエストパラメータのチェック
	 * @param role
	 * @param studentNo
	 * @param mailadress
	 * @param nickname
	 * @param course_id
	 * @param password1
	 * @param password2
	 * @param admission_year
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	private ActionErrors validateRequestParams(
			String role,String studentNo,
			String mailadress,String nickname,
			String course_id,String password1,
			String password2,String admission_year) throws AsoBbsSystemErrException {
		
		ActionErrors errs = new ActionErrors();
		
		//ニックネーム
		UserValidator.useNickName(nickname,errs);
		
		//メールアドレス
		UserValidator.mailAddress(mailadress,errs);

		//学科ID
		//UserValidator.courseId(course_id,errs);
		
		//パスワード
		if( password1.equals(password2) != true) {
			//パスワード不一致
			errs.add(ErrorCode.ERR_MEMBER_ENTRY_PASSWORD_NOTMATCH);
		}
		UserValidator.password(password1,errs);
		
		//ロールID
		UserValidator.roleId(role,errs);
		
		//入学年度
		UserValidator.admissionYear(admission_year,errs);
		
		return errs;
	}
}
