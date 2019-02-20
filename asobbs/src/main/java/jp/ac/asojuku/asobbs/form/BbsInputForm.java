package jp.ac.asojuku.asobbs.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import jp.ac.asojuku.asobbs.dto.AttachedFileDto;
import jp.ac.asojuku.asobbs.util.FileUtils;
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
	
	private List<AttachedFileDto> uploadFilePathList = new ArrayList<AttachedFileDto>();
	
	public void addUploadFilePath(AttachedFileDto filePath) {
		uploadFilePathList.add(filePath);
	}
	public void addUploadFilePath(String filePath,Long size) {
		AttachedFileDto dto = new AttachedFileDto();
		
		dto.setFileName(FileUtils.getFileNameFromPath(filePath));
		dto.setFilePath(filePath);
		dto.setSize(size);
		
		uploadFilePathList.add(dto);
	}

	public void addUploadFilePath(Integer id,String filePath,Long size) {
		AttachedFileDto dto = new AttachedFileDto();

		dto.setFileName(FileUtils.getFileNameFromPath(filePath));
		dto.setId(id);
		dto.setFilePath(filePath);
		dto.setSize(size);
		
		uploadFilePathList.add(dto);
	}
}
