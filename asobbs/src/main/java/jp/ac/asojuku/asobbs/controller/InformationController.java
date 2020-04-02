package jp.ac.asojuku.asobbs.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.asobbs.config.AppSettingProperty;
import jp.ac.asojuku.asobbs.dto.AttachedFileDto;
import jp.ac.asojuku.asobbs.dto.BbsCheckTblDto;
import jp.ac.asojuku.asobbs.dto.BbsDetailDto;
import jp.ac.asojuku.asobbs.dto.CategoryListDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.exception.AsoBbsIllegalException;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.form.BbsInputForm;
import jp.ac.asojuku.asobbs.form.BbsReplyInputForm;
import jp.ac.asojuku.asobbs.param.IntConst;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.BbsService;
import jp.ac.asojuku.asobbs.service.RoomService;
import jp.ac.asojuku.asobbs.util.FileUtils;

@Controller
@RequestMapping(value= {"/info"})
public class InformationController {

	@Autowired
	HttpSession session;
	@Autowired
	BbsService bbsService;
	@Autowired
	RoomService roomService;
	
	/**
	 * お知らせ表示画面
	 * 
	 * @param msg
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException 
	 * @throws AsoBbsIllegalException 
	 */
	@RequestMapping(value= {"/disp"}, method=RequestMethod.GET)
    public ModelAndView disp(@ModelAttribute("id")Integer bbsId,ModelAndView mv) throws AsoBbsIllegalException, AsoBbsSystemErrException {

		mv.setViewName("display_info");
		
		BbsDetailDto bbsDetailDto = bbsService.getInfoBbsBy(bbsId);

		mv.addObject("bbsDetailDto",bbsDetailDto);
		mv.addObject("bbsReplyInputForm",new BbsReplyInputForm());
		
        return mv;
    }


	/**
	 * お知らせ入力画面
	 * 
	 * @param msg
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/input"}, method=RequestMethod.GET)
    public ModelAndView input(ModelAndView mv) {
		
		BbsInputForm form = new BbsInputForm();
		form.setRoomId(IntConst.ALL_ROOM_ID);
		form.setCategoryName(IntConst.INFO_CATE_NAME);
        mv.setViewName("/input_info");
        mv.addObject("bbsInputForm",form);
        
        return mv;
    }
	
	/**
	 * お知らせ確認画面
	 * 
	 * @param mv
	 * @param bbsInputForm
	 * @param bindingResult
	 * @return
	 * @throws IOException 
	 * @throws AsoBbsSystemErrException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value= {"/confirm"}, method=RequestMethod.POST)
    public ModelAndView confirm(ModelAndView mv,
    		@Valid BbsInputForm bbsInputForm,
			BindingResult bindingResult) throws IllegalStateException, AsoBbsSystemErrException, IOException {

		//エラーがある場合は入力画面へ戻る
		if( bindingResult.hasErrors() ) {
			//エラー情報をセットする
	        mv.setViewName("/input_info");
		}else {
	        mv.setViewName("/confirm_info");
	        session.setAttribute(SessionConst.BBS_CONFIG_DTO,bbsInputForm);
		}
        return mv;
    }
	/**
	 * 掲示板挿入
	 * @param mv
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws AsoBbsSystemErrException 
	 */
	@RequestMapping(value= {"/insert"}, method=RequestMethod.POST)
    public String insert(ModelAndView mv) throws IllegalStateException, IOException, AsoBbsSystemErrException {
		
		BbsInputForm bbsInputForm = (BbsInputForm)session.getAttribute(SessionConst.BBS_CONFIG_DTO);
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
        mv.setViewName("/complete_bbs");
        
        //登録処理
        bbsService.insert(bbsInputForm,loginInfo);

        return "redirect:/bbs/complete";
    }

}
