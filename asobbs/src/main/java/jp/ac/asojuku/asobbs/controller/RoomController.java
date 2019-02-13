package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.dto.RoomConfirmDto;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
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
	
	/**
	 * 入力画面
	 * @param msg
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/input"}, method=RequestMethod.GET)
    public ModelAndView input(@ModelAttribute("msg")String msg,ModelAndView mv) {

        //学科の一覧を取得する
        List<CourseDto> list = courseService.getAllList();
        
        mv.setViewName("input_room");
        mv.addObject("courseList",list);
        mv.addObject("errs",errs);
        
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
    		@RequestParam("roomname")String roomname, 
    		@RequestParam("roomadmin")String roomadmin, 
    		@RequestParam("roombelong")String roombelong
    		) throws AsoBbsSystemErrException {

		//入力チェックを行う
		validateParams(roomname,roomadmin,roombelong);
		
		//エラーがある場合は入力画面へ戻る
		if( errs.isHasErr() ) {
			//エラー情報をセットする
			mv.addObject("errs",errs);
	        mv.setViewName("input_room");
		}else {
			//セッションに保存する
			RoomConfirmDto roomConfirmDto = 
					roomService.getRoomConfirmDto(roomname,roomadmin,roombelong);
			session.setAttribute("roomConfirmDto",roomConfirmDto);
	        mv.setViewName("confirm_room");
		}

        return mv;
	}
	
	/**
	 * ルーム情報チェック
	 * 
	 * @param roomname
	 * @param roomadmin
	 * @param roombelong
	 * @throws AsoBbsSystemErrException
	 */
	private void validateParams(String roomname,String roomadmin,String roombelong ) throws AsoBbsSystemErrException {
		//いったんクリア
		errs.clear();
		
		//ルーム名
		RoomValidator.roomName(roomname,errs);
		//ルーム管理者
		RoomValidator.roomAdmin(roomadmin,errs);
		//ルーム所属者
		RoomValidator.roomUser(roombelong,errs);
	}
	
}
