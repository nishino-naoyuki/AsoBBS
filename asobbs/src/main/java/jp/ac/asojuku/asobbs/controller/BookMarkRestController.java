package jp.ac.asojuku.asobbs.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.asobbs.dto.BookMarkDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.exception.AsoBbsDuplicateException;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.BookMarkService;

@RestController
@RequestMapping("bookmark")
public class BookMarkRestController {
	
	@Autowired
	BookMarkService bookMarkService;
	
	@Autowired
	HttpSession session;

	
	/**
	 * お気に入り情報の登録
	 * 
	 * @param bbsId
	 */
	@RequestMapping("/insert")
	public String insert(@ModelAttribute("bbsId")Integer bbsId) {
		
		//セッションからユーザー情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//追加する
		try {
			bookMarkService.insert(bbsId, loginInfo.getUserId());
		}catch( AsoBbsDuplicateException e ) {
			return "duplicate";
		}
		
		return "ok";
	}
	
	/**
	 * 削除を行う
	 * 
	 * @param bookmarkId
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping("/delete")
	public String delete(@ModelAttribute("bookmarkId")Integer bookmarkId) throws AsoBbsSystemErrException {

		//セッションからユーザー情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//削除処理（不正があった場合は例外が飛ぶ（システムエラー））
		bookMarkService.delete(bookmarkId, loginInfo.getUserId());
		
		return "ok";
	}
	
	/**
	 * お気に入り情報を取得する
	 * 
	 * @return
	 */
	@RequestMapping("/getlist")
	public List<BookMarkDto> getList(){

		//セッションからユーザー情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		return bookMarkService.getList(loginInfo.getUserId());
	}
	
}
