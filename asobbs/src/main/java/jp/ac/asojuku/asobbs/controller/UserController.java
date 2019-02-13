package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.config.AppSettingProperty;
import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.dto.CreateUserDto;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.service.CourseService;
import jp.ac.asojuku.asobbs.service.UserService;
import jp.ac.asojuku.asobbs.util.FileUtils;
import jp.ac.asojuku.asobbs.validator.UserValidator;

@Controller
@RequestMapping(value= {"/user"})
public class UserController {
	@Autowired
	ActionErrors errs;
	
	@Autowired
	CourseService courseService;

	@Autowired
	UserService userService;
	
	@Autowired
	HttpSession session;

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
        
        //空のデータを作る
        CreateUserDto dto = new CreateUserDto();

		mv.addObject("createUserDto",dto);
        mv.addObject("courseList",list);
        mv.addObject("errs",errs);
        
        return mv;
    }
	

	/**
	 * 確認画面コントローラー　入力チェックを行う
	 * @param role
	 * @param studentNo
	 * @param mailadress
	 * @param nickname
	 * @param course_id
	 * @param password1
	 * @param password2
	 * @param admission_year
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/confirm"}, method=RequestMethod.POST)
    public ModelAndView cofirm(
    		@RequestParam("role")String role, 
    		@RequestParam("studentNo")String studentNo, 
    		@RequestParam("mailadress")String mailadress, 
    		@RequestParam("nickname")String nickname, 
    		@RequestParam("course_id")String course_id, 
    		@RequestParam("password1")String password1, 
    		@RequestParam("password2")String password2, 
    		@RequestParam("admission_year")String admission_year, 
    		ModelAndView mv) throws AsoBbsSystemErrException {

        //学科の一覧を取得する
        List<CourseDto> list = courseService.getAllList();
        
        //いったんエラーをクリアする
        errs.clear();
        
		//入力チェックを行う
		errs = 
				validateRequestParams(role,studentNo,mailadress,nickname,course_id,list,password1,password2,admission_year);

		//DTOに入れなおす
		CreateUserDto dto = getCreateUserDto(role,studentNo,mailadress,nickname,course_id,password1,admission_year);
		
		if( !errs.isHasErr() ) {
			//エラーが無ければ、セッションに保存して確認画面へ
			session.setAttribute("createUserDto",dto);
	        mv.setViewName("confirm_user");
		}else {
			//エラー情報をセットする
			mv.addObject("errs",errs);
			//エラーの場合はリクエストパラメータに保存して、入力画面へ
			mv.addObject("createUserDto",dto);
	        mv.addObject("courseList",list);
	        mv.setViewName("input_user");
		}
		
        return mv;
	}
	

	/**
	 * ユーザー情報の挿入
	 * 
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/insert"}, method=RequestMethod.POST)
    public String insert(ModelAndView mv) throws AsoBbsSystemErrException {
		        
        //セッションから登録データを取得する
		CreateUserDto dto = (CreateUserDto)session.getAttribute("createUserDto");
        
        //DBに保存
		userService.insert(dto);
        
        return "redirect:/user/complete_user";
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
			String course_id,List<CourseDto> list,String password1,
			String password2,String admission_year) throws AsoBbsSystemErrException {
		
		ActionErrors errs = new ActionErrors();
		
		//学籍番号
		if( userService.isExistStudentNo(studentNo) ) {
			errs.add(ErrorCode.ERR_MEMBER_ENTRY_DUPLICATE_STUDENTNO);
		}
		
		//ニックネーム
		UserValidator.useNickName(nickname,errs);
		
		//メールアドレス
		UserValidator.mailAddress(mailadress,errs);
		if( userService.isExistMailadress(mailadress) ) {
			errs.add(ErrorCode.ERR_MEMBER_ENTRY_DUPLICATE_MEIL);
		}

		//学科ID
		UserValidator.courseId(course_id,list,errs);
		
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
	
	/**
	 * CreateUserDtoを生成する
	 * 
	 * @param role
	 * @param studentNo
	 * @param mailadress
	 * @param nickname
	 * @param course_id
	 * @param password
	 * @param admission_year
	 * @return
	 */
	private CreateUserDto getCreateUserDto(
			String role,String studentNo,
			String mailadress,String nickname,
			String course_id,String password,
			String admission_year) {
		
		CreateUserDto dto = new CreateUserDto();
		
		dto.setRoleId(Integer.parseInt(role));
		dto.setStudentNo(studentNo);
		dto.setMailadress(mailadress);
		dto.setNickname(nickname);
		dto.setCourseId(Integer.parseInt(course_id));
		dto.setPassword(password);
		dto.setAdmissionYear(admission_year);
		
		return dto;
		
	}

}
