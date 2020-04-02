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
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.BbsService;
import jp.ac.asojuku.asobbs.service.RoomService;
import jp.ac.asojuku.asobbs.util.FileUtils;

@Controller
@RequestMapping(value= {"/bbs"})
public class BbsController {

	@Autowired
	HttpSession session;
	@Autowired
	BbsService bbsService;
	@Autowired
	RoomService roomService;
	
	/**
	 * 入力画面
	 * 
	 * @param msg
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/input"}, method=RequestMethod.GET)
    public ModelAndView input(@ModelAttribute("id")Integer roomId,ModelAndView mv) {
		
		BbsInputForm form = new BbsInputForm();
		form.setRoomId(roomId);
        mv.setViewName("/input_bbs");
        mv.addObject("bbsInputForm",form);
        
        //カテゴリの一覧を取得する
        List<CategoryListDto> categoryListDtoList = roomService.getCategoryListDto(roomId);
        mv.addObject("categoryListDtoList",categoryListDtoList);
        
        return mv;
    }


	/**
	 * 掲示板の編集画面
	 * 
	 * @param bbsId
	 * @param mv
	 * @return
	 * @throws AsoBbsIllegalException
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/edit_input"}, method=RequestMethod.GET)
    public ModelAndView edit_input(@ModelAttribute("bbsId")Integer bbsId,ModelAndView mv) throws AsoBbsIllegalException, AsoBbsSystemErrException {
		
		//ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//掲示板情報を取得
		BbsDetailDto bbsDetailDto = bbsService.getBy(bbsId,loginInfo);
		
		//DTO->form
		BbsInputForm form = new BbsInputForm();
		form.setBbsId( bbsDetailDto.getBbsId() );
		form.setCategoryName( bbsDetailDto.getCategoryName());
		form.setTitle( bbsDetailDto.getTitle() );
		form.setEmergencyFlg( bbsDetailDto.getEmergencyFlg() );
		form.setAnyoneFlg(bbsDetailDto.getAnyoneFlg());
		form.setContent( bbsDetailDto.getContent() );
		form.setRoomId( bbsDetailDto.getRoomId()  );
		for( int i = 0 ; i < bbsDetailDto.getAttachedFileList().size(); i++) {
			form.addNowFilePath( i,
											bbsDetailDto.getAttachedFileList().get(i));
		}
		
        mv.setViewName("/edit_bbs");
        mv.addObject("bbsInputForm",form);
        
        //カテゴリの一覧を取得する
        List<CategoryListDto> categoryListDtoList = roomService.getCategoryListDto(form.getRoomId());
        mv.addObject("categoryListDtoList",categoryListDtoList);
        
        return mv;
    }
	
	/**
	 * 確認画面
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
		
		//添付ファイルがある場合はフォルダを作成して
		//ファイルをコピーする
		uploadFiles(bbsInputForm);

		//エラーがある場合は入力画面へ戻る
		if( bindingResult.hasErrors() ) {
			//エラー情報をセットする
	        mv.setViewName("/input_bbs");
		}else {
	        mv.setViewName("/confirm_bbs");
	        session.setAttribute(SessionConst.BBS_CONFIG_DTO,bbsInputForm);
		}
        return mv;
    }

	@RequestMapping(value= {"/edit_confirm"}, method=RequestMethod.POST)
    public ModelAndView editConfirm(ModelAndView mv,
    		@Valid BbsInputForm bbsInputForm,
			BindingResult bindingResult) throws IllegalStateException, AsoBbsSystemErrException, IOException, AsoBbsIllegalException {
		
		//添付ファイルがある場合はフォルダを作成して
		//ファイルをコピーする
		uploadFiles(bbsInputForm);

		//ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		//掲示板情報を取得
		BbsDetailDto bbsDetailDto = bbsService.getBy(bbsInputForm.getBbsId(),loginInfo);
		//現状のファイル状況を保存する
		for( int i = 0 ; i < bbsDetailDto.getAttachedFileList().size(); i++) {
			bbsInputForm.addNowFilePath( i,
											bbsDetailDto.getAttachedFileList().get(i));
		}

		//エラーがある場合は入力画面へ戻る
		if( bindingResult.hasErrors() ) {
			//エラー情報をセットする
	        mv.setViewName("/edit_bbs");
		}else {
	        mv.setViewName("/confirm_edit_bbs");
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

	/**
	 * 掲示板挿入処理
	 * 
	 * @param mv
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/edit_insert"}, method=RequestMethod.POST)
    public String editInsert(ModelAndView mv) throws IllegalStateException, IOException, AsoBbsSystemErrException {
		
		BbsInputForm bbsInputForm = (BbsInputForm)session.getAttribute(SessionConst.BBS_CONFIG_DTO);
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
        mv.setViewName("/complete_edit_bbs");
        
        //登録処理
        bbsService.update(bbsInputForm,loginInfo);

        return "redirect:/bbs/complete";
    }
	/**
	 * 登録完了処理
	 * @param editFlg
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	@RequestMapping(value= {"/complete"}, method=RequestMethod.GET)
    public ModelAndView complete(@ModelAttribute("edit")String editFlg,ModelAndView mv) throws AsoBbsSystemErrException {
		        
        //セッションから登録データを取得する
		session.removeAttribute(SessionConst.BBS_CONFIG_DTO);
        
		if( "true".equals(editFlg) ) {
			mv.setViewName("complete_edit_bbs");
		}else {
			mv.setViewName("complete_input_bbs");
		}
        
        return mv;
    }


	/**
	 * 掲示板情報を表示す
	 * 
	 * @param id
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 * @throws AsoBbsIllegalException 
	 */
	@RequestMapping(value= {"/detail"}, method=RequestMethod.GET)
    public ModelAndView detail(@ModelAttribute("id")Integer id,ModelAndView mv) throws AsoBbsSystemErrException, AsoBbsIllegalException {
		        
		//セッションよりログイン情報取得（不正防止チェック用)
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		mv.setViewName("detail_bbs");
		
		BbsDetailDto bbsDetailDto = bbsService.getBy(id,loginInfo);

		mv.addObject("bbsDetailDto",bbsDetailDto);
		mv.addObject("bbsReplyInputForm",new BbsReplyInputForm());
		
        return mv;
    }

	/**
	 * 掲示板挿入
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/reply"}, method=RequestMethod.POST)
    public String reply(ModelAndView mv,
    		@Valid BbsReplyInputForm bbsReplyInputForm,
    		BindingResult bindingResult) {

		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		bbsService.insertChild(bbsReplyInputForm,loginInfo);

        return "redirect:/bbs/detail?id="+bbsReplyInputForm.getBbsId();
    }
	

	/**
	 * 緊急掲示板の確認しましたを登録する
	 * 
	 * @param id
	 * @param mv
	 * @return
	 * @throws AsoBbsSystemErrException
	 * @throws AsoBbsIllegalException 
	 */
	@RequestMapping(value= {"/emergency_reply"}, method=RequestMethod.POST)
    public String emergencyReply(@ModelAttribute("bbsId")Integer bbsId,ModelAndView mv) throws AsoBbsSystemErrException, AsoBbsIllegalException {
		        
		//セッションよりログイン情報取得（不正防止チェック用)
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
				
		bbsService.insertBbsCheck(bbsId,loginInfo);

        return "redirect:/bbs/detail?id="+bbsId;
    }
	
	/**
	 * 緊急記事の確認済みリストを表示する
	 * 
	 * @param bbsId
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/emergency_reply_list"}, method=RequestMethod.GET)
    public ModelAndView emergencyReplyList(@ModelAttribute("bbsId")Integer bbsId,ModelAndView mv)  {

		mv.setViewName("list_emergency_reply");
		
		List<BbsCheckTblDto> list = bbsService.getBbsCheckTblDtoList(bbsId);
		
		mv.addObject("bbsCheckTblDtoList",list);
		
		return mv;
	}
	/**
	 * ファイルのコピー
	 * @param bbsInputForm
	 * @throws AsoBbsSystemErrException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void uploadFiles(BbsInputForm bbsInputForm) throws AsoBbsSystemErrException, IllegalStateException, IOException {
		
		MultipartFile[] uploadFiles = {
				bbsInputForm.getMultipartFile1(),
				bbsInputForm.getMultipartFile2(),
				bbsInputForm.getMultipartFile3()
		};
		//Boolean[] uploadfileDeltes = {
		//		bbsInputForm.getMultipartFile1DelFlg(),
		//		bbsInputForm.getMultipartFile2DelFlg(),
		//		bbsInputForm.getMultipartFile3DelFlg(),
		//};
		
		File uploadDir = null;
		//ファイルがあれば保存して、パスを覚えておく
		//for( MultipartFile uploadFile : uploadFiles) {
		for(int i = 0; i < uploadFiles.length; i++) {
			MultipartFile uploadFile = uploadFiles[i];
			if( uploadFile != null && !uploadFile.isEmpty() ) {
				//アップロードディレクトリを取得する
				uploadDir = (uploadDir == null ? mkdirs() : uploadDir);
			    //出力ファイル名を決定する
			    File uploadFilePath = new File(uploadDir.getPath() + "/" + uploadFile.getOriginalFilename());
			    //ファイルコピー
			    uploadFile.transferTo(uploadFilePath);
			    //アップロードしたファイル名を覚えておく
			    bbsInputForm.addUploadFilePath(i,uploadFilePath.toString(),uploadFile.getSize());
			}
		}
		//削除チェック
		//for( int index = 0; index < uploadfileDeltes.length; index++) {
		//	if( uploadfileDeltes[index] != null && uploadfileDeltes[index] ) {
		//		bbsInputForm.deleteUploadFilePath(index);
		//	}
		//}
	}

    /**
     * アップロードファイルを格納するディレクトリを作成する
     *
     * @param filePath
     * @return
     * @throws AsoBbsSystemErrException 
     */
    private File mkdirs() throws AsoBbsSystemErrException{
    	
    	//アップロードディレクトリを取得する
    	StringBuffer filePath = new StringBuffer(AppSettingProperty.getInstance().getBbsUploadWorkDirectory());
    	
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        File uploadDir = new File(filePath.toString(), sdf.format(now));
        // 既に存在する場合はプレフィックスをつける
        int prefix = 0;
        while(uploadDir.exists()){
            prefix++;
            uploadDir =
                    new File(filePath.toString() + sdf.format(now) + "-" + String.valueOf(prefix));
        }

        // フォルダ作成
        FileUtils.makeDir( uploadDir.toString());

        return uploadDir;
    }
}
