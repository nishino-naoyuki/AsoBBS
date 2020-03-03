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

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public void bbsDownload(@ModelAttribute("fid")Integer fid,@ModelAttribute("fsize")Long fsize,HttpServletResponse response) throws IOException {
		
		File file = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			String fPath = bbsService.getAttacedFileName(fid, fsize);
			file = new File(fPath);
			os = response.getOutputStream();

			String fileName = FileUtils.getFileNameFromPath(fPath);
			//fileName = new String(fileName.getBytes(StringConst.DLFILE_NAME_ENCODE), StringConst.DLFILE_NAME_ENCODE);
		    response.setContentType("application/octet-stream");
		    response.setContentLength((int) file.length());
		    response.setHeader(
		    		"Content-Disposition", 
		    		"inline; filename=\""+fileName +"\"; "
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
}
