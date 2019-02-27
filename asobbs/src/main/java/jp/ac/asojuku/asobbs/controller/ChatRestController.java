package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.asobbs.dto.ChatDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.dto.RoomListDto;
import jp.ac.asojuku.asobbs.form.ChatInputForm;
import jp.ac.asojuku.asobbs.form.RoomSearchForm;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.ChatService;

@RestController
@RequestMapping(value= {"/chat"})
public class ChatRestController {

	@Autowired
	HttpSession session;
	
	@Autowired
	ChatService chatService;
	
	@RequestMapping("/getmsglist")
    public ChatDto getList(@ModelAttribute("userId")Integer userId) {
		//セッションからログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
        return chatService.getList(loginInfo.getUserId(),userId);
    }

	@RequestMapping("/send_comment")
    public ChatDto comment(ChatInputForm form) {
		//セッションからログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//コメントを追加
		chatService.inset(form, loginInfo);
		
        return chatService.getList(loginInfo.getUserId(),form.getUserId());
    }
}
