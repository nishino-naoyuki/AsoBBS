package jp.ac.asojuku.asobbs.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.asobbs.dto.BbsListDto;
import jp.ac.asojuku.asobbs.service.BbsService;
import jp.ac.asojuku.asobbs.util.FileUtils;

@RestController
@RequestMapping("download")
public class DownloadController {
	
	@Autowired
	BbsService bbsService;

	@RequestMapping("/bbs")
    public void bbsDownload(@ModelAttribute("fid")Integer fid,@ModelAttribute("fsize")Long fsize,HttpServletResponse response) throws IOException {
		
		File file = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			String fPath = bbsService.getAttacedFileName(fid, fsize);
			file = new File(fPath);
			os = response.getOutputStream();

		    response.setContentType("application/octet-stream");
		    response.setContentLength((int) file.length());
		    response.setHeader("Content-Disposition", "attachment; filename="+FileUtils.getFileNameFromPath(fPath));
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
