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
import jp.ac.asojuku.asobbs.dto.BbsDetailDto;
import jp.ac.asojuku.asobbs.dto.CategoryListDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.exception.AsoBbsIllegalException;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.form.BbsInputForm;
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

	/**
	 * 掲示板挿入
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/insert"}, method=RequestMethod.POST)
    public String insert(ModelAndView mv) {
		
		BbsInputForm bbsInputForm = (BbsInputForm)session.getAttribute(SessionConst.BBS_CONFIG_DTO);
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
        mv.setViewName("/complete_bbs");
        
        //登録処理
        bbsService.insert(bbsInputForm,loginInfo);

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
		
		File uploadDir = null;
		//ファイルがあれば保存して、パスを覚えておく
		for( MultipartFile uploadFile : uploadFiles) {
			if( !uploadFile.isEmpty() ) {
				//アップロードディレクトリを取得する
				uploadDir = (uploadDir == null ? mkdirs() : uploadDir);
			    //出力ファイル名を決定する
			    File uploadFilePath = new File(uploadDir.getPath() + "/" + uploadFile.getOriginalFilename());
			    //ファイルコピー
			    uploadFile.transferTo(uploadFilePath);
			    //アップロードしたファイル名を覚えておく
			    bbsInputForm.addUploadFilePath(uploadFilePath.toString(),uploadFile.getSize());
			}
		}
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
    	StringBuffer filePath = new StringBuffer(AppSettingProperty.getInstance().getBbsUploadDirectory());
    	
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
