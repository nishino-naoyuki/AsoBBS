package jp.ac.asojuku.asobbs.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.dto.CreateUserDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.dto.RoomInsertDto;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.form.RoomInputForm;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.CourseService;
import jp.ac.asojuku.asobbs.service.RoomService;
import jp.ac.asojuku.asobbs.validator.RoomValidator;

@Controller
@RequestMapping(value= {"/room"})
public class RoomController {

	@Autowired
	ActionErrors errs;
	
	@Autowired
	CourseService courseService;

	@Autowired
	RoomService roomService;

	@Autowired
	HttpSession session;
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * 入力画面
	 * @param msg
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/input"}, method=RequestMethod.GET)
    public ModelAndView input(@ModelAttribute("msg")String msg,ModelAndView mv,BindingResult bindingResult) {

        //学科の一覧を取得する
        List<CourseDto> list = courseService.getAllList();
        
        RoomInputForm roomInputForm = new RoomInputForm();
        mv.setViewName("input_room");
        mv.addObject("courseList",list);
        mv.addObject("errs",errs);
        mv.addObject("roomInputForm",roomInputForm);
        
        return mv;
    }

	/**
	 * 確認画面
	 * 
	 * @param mv
	 * @param roomname
	 * @param roomadmin
	 * @param roombelong
	 * @return
	 * @throws AsoBbsSystemErrException 
	 */
	@RequestMapping(value= {"/confirm"}, method=RequestMethod.POST)
	public ModelAndView cofirm(
			ModelAndView mv,
			@Valid RoomInputForm roomInputForm,
			BindingResult bindingResult
    		) throws AsoBbsSystemErrException {

		//入力チェックを行う
		validateParams(roomInputForm,bindingResult);
	    
		//エラーがある場合は入力画面へ戻る
		if( bindingResult.hasErrors() ) {
			//エラー情報をセットする
	        mv.setViewName("input_room");
		}else {
			//セッションに保存する
			RoomInsertDto roomConfirmDto = 
					roomService.getRoomConfirmDto(
							roomInputForm.getRoomName(),
							roomInputForm.getRoomAdmins(),
							roomInputForm.getRoomUsers()
							);
			session.setAttribute("roomConfirmDto",roomConfirmDto);
	        mv.setViewName("confirm_room");
		}

        return mv;
	}

	@RequestMapping(value= {"/insert"}, method=RequestMethod.POST)
    public String insert(ModelAndView mv) throws AsoBbsSystemErrException {
		        
        //セッションから登録データを取得する
		RoomInsertDto dto = (RoomInsertDto)session.getAttribute("roomConfirmDto");
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
        
        //DBに保存
		roomService.insert(dto,loginInfo);
        
        return "redirect:/user/complete_user";
    }
	
	/**
	 * ルーム情報チェック
	 * 
	 * @param roomname
	 * @param roomadmin
	 * @param roombelong
	 * @throws AsoBbsSystemErrException
	 */
	private void validateParams(RoomInputForm roomInputForm,BindingResult bindingResult ) throws AsoBbsSystemErrException {
		//いったんクリア
		errs.clear();
		
		//ルーム名 は、Fromのバリデーションでチェック
		//RoomValidator.roomName(roomInputForm.getRoomName(),errs);
		//ルーム管理者
		RoomValidator.roomAdmin(roomInputForm.getRoomAdmins(),bindingResult);
		//ルーム所属者
		RoomValidator.roomUser(roomInputForm.getRoomUsers(),bindingResult);
	}
	
}
