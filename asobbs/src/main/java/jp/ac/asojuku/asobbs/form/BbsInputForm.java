package jp.ac.asojuku.asobbs.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BbsInputForm {
	@NotEmpty(message = "{errmsg0301}")
	private String categoryName;
	
	@NotEmpty(message = "{errmsg0302}")
	private String title;
	private Boolean emergencyFlg;
	
	@NotEmpty(message = "{errmsg0303}")
	private String content;
	
	private Integer roomId;
	
	private MultipartFile multipartFile1;
	private MultipartFile multipartFile2;
	private MultipartFile multipartFile3;
	
	private List<String> uploadFilePathList = new ArrayList<String>();
	
	public void addUploadFilePath(String filePath) {
		uploadFilePathList.add(filePath);
	}
}
