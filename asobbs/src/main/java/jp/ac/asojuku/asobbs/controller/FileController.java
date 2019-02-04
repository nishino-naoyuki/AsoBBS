package jp.ac.asojuku.asobbs.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.asobbs.config.AppSettingProperty;
import jp.ac.asojuku.asobbs.csv.UserCSV;
import jp.ac.asojuku.asobbs.dto.CSVProgressDto;
import jp.ac.asojuku.asobbs.err.ActionError;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.service.UserCSVService;
import jp.ac.asojuku.asobbs.util.FileUtils;

@RestController
@RequestMapping("file")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	UserCSVService userCSVService;

	@Autowired
	private ActionErrors errors;
	
	@RequestMapping(value= {"/csvinput"}, method=RequestMethod.POST)
	public Object csvinput(@RequestParam("upload_file") MultipartFile file) throws AsoBbsSystemErrException {
		// ファイルが空の場合は HTTP 400 を返す。
	   // if (file.isEmpty()) {
	   //   response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  //    return;
	   // }
	    
	    //ディレクトリを作成する
	    File uploadDir = mkdirs();
	    
	    try {
		    //出力ファイル名を決定する
		    File uploadFile = new File(uploadDir.getPath() + "/" + "test.csv");
		    //アップロードファイルを取得
		    byte[] bytes = file.getBytes();
		    //出力ストリームを取得
		    BufferedOutputStream uploadFileStream =
	                new BufferedOutputStream(new FileOutputStream(uploadFile));
		    //ストリームに書き込んでクローズ
		    uploadFileStream.write(bytes);
	        uploadFileStream.close();
	        
	        //エラーチェックを行う
	        List<UserCSV> userList = userCSVService.checkForCSV(uploadFile.getAbsolutePath(),errors,"");

			if( errors.isHasErr() ){
				return outputErrorResult();
			}
			
			//登録処理
			userCSVService.insertByCSV(userList);

			//処理件数を返す
			return outputResult(userList);
	    } catch (Exception e) {
            // 異常終了時の処理
        } catch (Throwable t) {
            // 異常終了時の処理
        }
        
	    return "error";
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
    	StringBuffer filePath = new StringBuffer(AppSettingProperty.getInstance().getUploadDirectory());
    	
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

	/**
	 * エラー処理時のJSON文字列を作成する
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	private String outputErrorResult() throws JsonProcessingException {
		CSVProgressDto progress = new CSVProgressDto();
		StringBuffer sb = new StringBuffer();

		for( ActionError error : errors.getList() ){
			sb.append( error.getMessage() );
			sb.append("\n");
		}
		progress.setErrorMsg(sb.toString());

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(progress);

        logger.trace("jsonString:{}",jsonString);

        return jsonString;
	}

	/**
	 * 処理結果のJSON文字列を作成する
	 * 
	 * @param userList
	 * @return
	 * @throws JsonProcessingException
	 */
	private String outputResult(List<UserCSV> userList ) throws JsonProcessingException {

		CSVProgressDto progress = new CSVProgressDto();
		
		progress.setTotal(userList.size());
		progress.setNow(userList.size());

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(progress);

        logger.trace("jsonString:{}",jsonString);

        return jsonString;

	}
}
