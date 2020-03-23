package jp.ac.asojuku.asobbs.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.dto.BookMarkDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.exception.AsoBbsDuplicateException;
import jp.ac.asojuku.asobbs.exception.AsoBbsIllegalException;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.BookMarkService;

@Controller
@RequestMapping("bookmark")
public class BookMarkController {
	
	@Autowired
	BookMarkService bookMarkService;
	
	@Autowired
	HttpSession session;

	

	@RequestMapping(value= {"/list"}, method=RequestMethod.GET)
    public ModelAndView list(ModelAndView mv) throws AsoBbsSystemErrException, AsoBbsIllegalException {
		mv.setViewName("list_bookmark");

        return mv;
	}

	/**
	 * 掲示板詳細表示へリダイレクトする
	 * 
	 * @param bookmarkId
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/dispbbs"}, method=RequestMethod.GET)
	public ModelAndView dispbbs(@ModelAttribute("bookmarkId")Integer bookmarkId,ModelAndView mv) 
			throws AsoBbsSystemErrException{

		//セッションからユーザー情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//掲示板の情報を取得する（もしエラーがある場合は例外が飛ぶ）
		BookMarkDto bookMarkDto = bookMarkService.getBy(
				loginInfo.getUserId(),bookmarkId
				);
		
		//掲示板へリダイレクト
		return new ModelAndView("redirect:/bbs/detail?id="+bookMarkDto.getBbsId());
	}
	
}
