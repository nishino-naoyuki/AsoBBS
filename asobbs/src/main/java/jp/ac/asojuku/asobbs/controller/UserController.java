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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.config.AppSettingProperty;
import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.dto.CreateUserDto;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.form.UserInputForm;
import jp.ac.asojuku.asobbs.service.CourseService;
import jp.ac.asojuku.asobbs.service.UserService;
import jp.ac.asojuku.asobbs.util.FileUtils;
import jp.ac.asojuku.asobbs.validator.UserValidator;

@Controller
@RequestMapping(value= {"/user"})
public class UserController {
	
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

        UserInputForm userInputForm = new UserInputForm();
		mv.addObject("createUserDto",dto);
        mv.addObject("courseList",list);
        mv.addObject("userInputForm",userInputForm);
        
        return mv;
    }
	

	/**
	 * 確認画面コントローラー　入力チェックを行う
	 * 
	 * @param userInputForm
	 * @param bindingResult
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/confirm"}, method=RequestMethod.POST)
    public ModelAndView cofirm(
    		@Valid UserInputForm userInputForm,
			BindingResult bindingResult,
    		ModelAndView mv) throws AsoBbsSystemErrException {

        //学科の一覧を取得する
        List<CourseDto> list = courseService.getAllList();
                
		//入力チェックを行う
		validateRequestParams(userInputForm,list,bindingResult);

		//DTOに入れなおす
		CreateUserDto dto = getCreateUserDto(userInputForm);
		
		if( bindingResult.hasErrors() ) {
			//エラーの場合はリクエストパラメータに保存して、入力画面へ
			mv.addObject("createUserDto",dto);
	        mv.addObject("courseList",list);
	        mv.setViewName("input_user");
		}else {
			//エラーが無ければ、セッションに保存して確認画面へ
			session.setAttribute("createUserDto",dto);
	        mv.setViewName("confirm_user");
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
	 * ユーザー登録完了
	 * 
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/complete_user"}, method=RequestMethod.GET)
    public ModelAndView complete_user(ModelAndView mv) throws AsoBbsSystemErrException {
		
		mv.setViewName("complete_user");
        
		return mv;
    }
	
	
	/**
	 * リクエストパラメータのチェック
	 * @param userInputForm
	 * @param list
	 * @param err
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	private void validateRequestParams(
			UserInputForm userInputForm,
			List<CourseDto> list,
			BindingResult err) throws AsoBbsSystemErrException {
		
		
		//学籍番号
		if( userService.isExistStudentNo(userInputForm.getStudentNo()) ) {
			UserValidator.setErrorcode("studentNo",err,ErrorCode.ERR_MEMBER_ENTRY_DUPLICATE_STUDENTNO);
		}
		
		//ニックネーム
		UserValidator.useNickName(userInputForm.getNickname(),err);
		
		//メールアドレス
		UserValidator.mailAddress(userInputForm.getMailadress(),err);
		if( userService.isExistMailadress(userInputForm.getMailadress()) ) {
			UserValidator.setErrorcode("mailadress",err,ErrorCode.ERR_MEMBER_ENTRY_DUPLICATE_MEIL);
		}

		//学科ID
		UserValidator.courseId(userInputForm.getCourse_id(),list,err);
		
		//パスワード
		if( userInputForm.getPassword1().equals(userInputForm.getPassword2()) != true) {
			//パスワード不一致
			UserValidator.setErrorcode("password1",err,ErrorCode.ERR_MEMBER_ENTRY_PASSWORD_NOTMATCH);
		}
		UserValidator.password(userInputForm.getPassword1(),err);
		
		//ロールID
		UserValidator.roleId(userInputForm.getRole(),err);
		
		//入学年度
		UserValidator.admissionYear(userInputForm.getAdmission_year(),err);
		
		return ;
	}
	
	/**
	 * CreateUserDtoを生成する
	 * 
	 * @param userInputForm
	 * @return
	 */
	private CreateUserDto getCreateUserDto(
			UserInputForm userInputForm) {
		
		CreateUserDto dto = new CreateUserDto();
		
		dto.setRoleId(Integer.parseInt(userInputForm.getRole()));
		dto.setStudentNo(userInputForm.getStudentNo());
		dto.setMailadress(userInputForm.getMailadress());
		dto.setNickname(userInputForm.getNickname());
		dto.setCourseId(Integer.parseInt(userInputForm.getCourse_id()));
		dto.setPassword(userInputForm.getPassword1());
		dto.setAdmissionYear(userInputForm.getAdmission_year());
		
		return dto;
		
	}

}
