package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.dto.RoomListDto;
import jp.ac.asojuku.asobbs.form.RoomSearchForm;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.RoomService;

@RestController
public class RoomListController {
	
	@Autowired
	RoomService roomService;
	@Autowired
	HttpSession session;

	/**
	 * ルーム一覧取得（ツリー用）
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping("/getroomlist")
    public List<RoomListDto> getList(RoomSearchForm form) {
		//セッションからログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
        return roomService.getListBy(form,loginInfo);
    }
	/**
	 * ルーム一覧取得（先生・管理者用）
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping("/getroomalllist")
	@ResponseBody
    public List<RoomListDto> getAllList(RoomSearchForm form) {
		//セッションからログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
        return roomService.getListBy(form,loginInfo,true);
    }
}
