package jp.ac.asojuku.asobbs.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import jp.ac.asojuku.asobbs.dto.RoomDetailDto;
import jp.ac.asojuku.asobbs.dto.RoomInsertDto;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.form.RoomInputForm;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.CourseService;
import jp.ac.asojuku.asobbs.service.RoomService;
import jp.ac.asojuku.asobbs.service.UserCSVService;
import jp.ac.asojuku.asobbs.validator.RoomValidator;

/**
 * @author nishino
 *
 */
@Controller
@RequestMapping(value= {"/room"})
public class RoomController {
	private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
	
	@Autowired
	CourseService courseService;

	@Autowired
	RoomService roomService;

	@Autowired
	HttpSession session;
	
	
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
					roomService.getRoomConfirmDto( roomInputForm );
			session.setAttribute(SessionConst.ROOM_CONFIG_DTO,roomConfirmDto);
	        mv.setViewName("confirm_room");
		}

        return mv;
	}

	/**
	 * 登録処理
	 * 
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/insert"}, method=RequestMethod.POST)
    public String insert(ModelAndView mv) throws AsoBbsSystemErrException {
		        
        //セッションから登録データを取得する
		RoomInsertDto dto = (RoomInsertDto)session.getAttribute(SessionConst.ROOM_CONFIG_DTO);
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
        
        //DBに保存
		roomService.insert(dto,loginInfo);
        
        return "redirect:/room/complete";
    }

	/**
	 * 完了処理
	 * 
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/complete"}, method=RequestMethod.GET)
    public ModelAndView complete(@ModelAttribute("edit")String editFlg,ModelAndView mv) throws AsoBbsSystemErrException {
		        
        //セッションから登録データを取得する
		session.removeAttribute(SessionConst.ROOM_CONFIG_DTO);
        
		if( "true".equals(editFlg) ) {
			mv.setViewName("complete_edit_room");
		}else {
			mv.setViewName("complete_input_room");
		}
        
        return mv;
    }

	/**
	 * ルーム一覧画面表示
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/list"}, method=RequestMethod.GET)
    public ModelAndView list(ModelAndView mv) throws AsoBbsSystemErrException {
		                
		mv.setViewName("list_room");
        
        return mv;
    }
	

	/**
	 * 詳細画面
	 * @param roomId
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/detail"}, method=RequestMethod.GET)
    public ModelAndView detail(@ModelAttribute("roomId")String roomId,ModelAndView mv) {

		try {
	        mv.setViewName("detail_room");
	        //詳細データを取得
	        RoomDetailDto dtailDto = 
	        		roomService.getRoomDetailDtoBy(Integer.parseInt(roomId));
	        mv.addObject("roomDetailDto",dtailDto);
	        //セッションに更新するルームID
	        session.setAttribute(SessionConst.ROOM_ID,Integer.parseInt(roomId));
	        
		}catch(Exception e) {
			logger.warn(e.getMessage());
			mv.setViewName("list_room");
		}
        
        return mv;
	}
	/**
	 * 編集画面
	 * @param msg
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/edit"}, method=RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute("roomId")String roomId,ModelAndView mv,BindingResult bindingResult) {

        //学科の一覧を取得する
        List<CourseDto> list = courseService.getAllList();
        //指定さえたルームIDの情報を取得する
        RoomInputForm roomInputForm = roomService.getRoomInputFormBy(Integer.parseInt(roomId));
        
        mv.setViewName("edit_room");
        mv.addObject("courseList",list);
        mv.addObject("roomInputForm",roomInputForm);
        
        return mv;
    }
	

	/**
	 * 更新用確認画面
	 * 
	 * @param mv
	 * @param roomInputForm
	 * @param bindingResult
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/confirm_edit"}, method=RequestMethod.POST)
	public ModelAndView cofirmEdit(
			ModelAndView mv,
			@Valid RoomInputForm roomInputForm,
			BindingResult bindingResult
    		) throws AsoBbsSystemErrException {

		//入力チェックを行う
		validateParams(roomInputForm,bindingResult);
	    
		//エラーがある場合は入力画面へ戻る
		if( bindingResult.hasErrors() ) {
			//エラー情報をセットする
	        mv.setViewName("edit_room");
		}else {
			//セッションに保存する
			RoomInsertDto roomConfirmDto = 
					roomService.getRoomConfirmDto( roomInputForm );
			session.setAttribute(SessionConst.ROOM_CONFIG_DTO,roomConfirmDto);
	        mv.setViewName("confirm_edit_room");
		}

        return mv;
	}

	/**
	 * 更新処理
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/update"}, method=RequestMethod.POST)
    public String update(ModelAndView mv) throws AsoBbsSystemErrException {
		        
        //セッションから登録データを取得する
		RoomInsertDto dto = (RoomInsertDto)session.getAttribute(SessionConst.ROOM_CONFIG_DTO);
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		Integer roomId = (Integer)session.getAttribute(SessionConst.ROOM_ID);
        
        //DBに保存
		roomService.update(dto,loginInfo,roomId);
        
        return "redirect:/room/complete?edit=true";
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
		
		//ルーム名 は、Fromのバリデーションでチェック
		//RoomValidator.roomName(roomInputForm.getRoomName(),errs);
		//ルーム管理者
		RoomValidator.roomAdmin(roomInputForm.getRoomAdmins(),bindingResult);
		//ルーム所属者
		if( !roomInputForm.getAllUserFlg() ) {
			RoomValidator.roomUser(roomInputForm.getRoomUsers(),bindingResult);
		}
	}
	
}
