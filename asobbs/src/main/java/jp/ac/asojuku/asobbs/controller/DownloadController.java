package jp.ac.asojuku.asobbs.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.asobbs.dto.BbsListDto;
import jp.ac.asojuku.asobbs.param.StringConst;
import jp.ac.asojuku.asobbs.service.BbsService;
import jp.ac.asojuku.asobbs.util.FileUtils;

@RestController
@RequestMapping("download")
public class DownloadController {
	
	@Autowired
	BbsService bbsService;

	/**
	 * 添付ファイルのダウンロードを実行する
	 * ファイルのIDとサイズで検索しパスを取得してファイルをDLする
	 * ファイルIDは不正に変更される恐れがあるので、ファイルサイズも一緒に検索する
	 * 
	 * @param fid
	 * @param fsize
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/bbs")
    public void bbsDownload(
    		@RequestHeader(value="User-Agent",required=false) String userAgent,
    		@ModelAttribute("fid")Integer fid,
    		@ModelAttribute("fsize")Long fsize,
    		HttpServletResponse response) throws IOException {
		
		File file = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			String fPath = bbsService.getAttacedFileName(fid, fsize);
			file = new File(fPath);
			os = response.getOutputStream();

			String fileName = FileUtils.getFileNameFromPath(fPath);
			//fileName = new String(fileName.getBytes(StringConst.DLFILE_NAME_ENCODE), StringConst.DLFILE_NAME_ENCODE);
		   
			//PDFファイルの場合はブラウザ内に展開できるようにする
			if( isPDFFile(fileName)) {
				response.setContentType("application/pdf");
			}else {
				response.setContentType("application/octet-stream");
			}
		    
		    response.setContentLength((int) file.length());
		    String headerDisposition = "";
		    if( isPDFFile(fileName)) {
		    	//pdfの場合はinlineにして、ブラウザ内で表示するようにする
		    	headerDisposition = "inline";
		    }else {
			    //ケータイの場合はinline、PCの場合はattachmentにする
		    	headerDisposition = ( isSmartPhone(userAgent) ? "inline":"attachment");
		    }
		    response.setHeader(
		    		"Content-Disposition", 
		    		headerDisposition+"; filename=\""+fileName +"\"; "
		    				+ "filename*=UTF-8''"+URLEncoder.encode(fileName, "UTF-8")
		    		//"attachment; filename*=utf-8''"+URLEncoder.encode(fileName, "UTF-8")
		    		);
		  //ファイルの読み込み
		    int bytes = 0;
		    os = response.getOutputStream();
		    byte[] bbuf = new byte[1024];
		    in = new DataInputStream(new FileInputStream(file));
		    while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
		    	os.write(bbuf, 0, bytes);
		    }
		    os.flush();
		}finally {
			if( in != null ) in.close();
			if( os != null ) os.close();
		}
		
    }
	
	/**
	 * スマホかどうかを判定する
	 * 
	 * UserAgentをみて、キーワードが含まれているかどうかをチェックする
	 * 参考：https://weback.net/mobile/2114/
	 * 
	 * @param userAgent
	 * @return
	 */
	private boolean isSmartPhone(String userAgent) {
		return( 
				userAgent.contains("Android") ||
				userAgent.contains("iPhone") ||
				userAgent.contains("Windows Phone") );
	}
	
	/**
	 * ダウンロード対象がPDFかどうかを判定する
	 * 
	 * @param filename
	 * @return
	 */
	private boolean isPDFFile(String filename) {
		return ( StringUtils.equalsIgnoreCase(FileUtils.getExt(filename), "pdf")) ;
	}
}
