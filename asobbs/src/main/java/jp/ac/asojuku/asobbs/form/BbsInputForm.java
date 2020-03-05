package jp.ac.asojuku.asobbs.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import jp.ac.asojuku.asobbs.dto.AttachedFileDto;
import jp.ac.asojuku.asobbs.util.FileUtils;
import lombok.Data;

@Data
public class BbsInputForm {
	@NotEmpty(message = "{errmsg0302}")
	@Size(max = 60, message="{errmsg0306}")
	private String categoryName;
	
	@NotEmpty(message = "{errmsg0303}")
	@Size(max = 60, message="{errmsg0305}")
	private String title;
	private Boolean emergencyFlg;
	
	@NotEmpty(message = "{errmsg0303}")
	@Size(max = 15000, message="{errmsg0305}")
	private String content;
	
	private final int UPLOAD_FILE_NUM = 3;
	private Integer roomId;
	
	private MultipartFile multipartFile1;
	private MultipartFile multipartFile2;
	private MultipartFile multipartFile3;
	
	//private List<AttachedFileDto> uploadFilePathList = new ArrayList<AttachedFileDto>();
	private AttachedFileDto[] uploadFilePaths = new AttachedFileDto[UPLOAD_FILE_NUM];
	
	//更新用情報
	//private List<AttachedFileDto> nowFilePathList = new ArrayList<AttachedFileDto>();	//更新前ファイルリスト
	private AttachedFileDto[] nowFilePaths = new AttachedFileDto[UPLOAD_FILE_NUM];
	
	private Boolean multipartFile1DelFlg;
	private Boolean multipartFile2DelFlg;
	private Boolean multipartFile3DelFlg;
	//更新用BBSID
	private Integer bbsId;
	
	public BbsInputForm() {
		//nullを返さないように空オブジェクトを入れておく
		for(int i = 0; i < uploadFilePaths.length; i++) {
			uploadFilePaths[i] = new AttachedFileDto();
		}
		for(int i = 0; i < nowFilePaths.length; i++) {
			nowFilePaths[i] = new AttachedFileDto();
		}
	}

	public void addNowFilePath(int idx,AttachedFileDto filePath) {
		//nowFilePathList.add(filePath);
		if( idx < 0 || idx >= UPLOAD_FILE_NUM ) {
			return;
		}
		nowFilePaths[idx] = filePath;
	}
	
	public void addUploadFilePath(int idx,AttachedFileDto filePath) {
		if( idx < 0 || idx >= UPLOAD_FILE_NUM ) {
			return;
		}
		uploadFilePaths[idx] = filePath;
		//uploadFilePathList.add(filePath);
	}
	public AttachedFileDto getUploadFilePath(Integer index) {
		AttachedFileDto attachedFileDto = null;
//		try{
//			attachedFileDto = uploadFilePathList.get(index);
//		}catch(IndexOutOfBoundsException e) {
//			attachedFileDto = new AttachedFileDto();
//		}
		if( index < 0 || index >= UPLOAD_FILE_NUM ) {
			return attachedFileDto;
		}
		
		return uploadFilePaths[index];
	}

	public AttachedFileDto getNowFilePath(Integer index) {
		AttachedFileDto attachedFileDto = null;
//		try{
//			attachedFileDto = nowFilePathList.get(index);
//		}catch(IndexOutOfBoundsException e) {
//			attachedFileDto = new AttachedFileDto();
//		}

		if( index < 0 || index >= UPLOAD_FILE_NUM ) {
			return attachedFileDto;
		}
		
		return nowFilePaths[index];
	}
	public void deleteUploadFilePath(Integer index) {
//		try{
//			uploadFilePathList.remove((int)index);
//		}catch(IndexOutOfBoundsException e) {
//			;//無処理
//		}
		if( index < 0 || index >= UPLOAD_FILE_NUM ) {
			return;
		}
		
		uploadFilePaths[index] = null;
	}

	public void addUploadFilePath(int index,Integer id,String filePath,Long size) {
		AttachedFileDto dto = new AttachedFileDto();

		dto.setFileName(FileUtils.getFileNameFromPath(filePath));
		dto.setId(id);
		dto.setFilePath(filePath);
		dto.setSize(size);
		

		addUploadFilePath(index,dto);
		//uploadFilePathList.add(dto);
	}
	
	public void addUploadFilePath(int index,String filePath,Long size) {
		AttachedFileDto dto = new AttachedFileDto();
		
		dto.setFileName(FileUtils.getFileNameFromPath(filePath));
		dto.setFilePath(filePath);
		dto.setSize(size);
		
		addUploadFilePath(index,dto);
		//uploadFilePathList.add(dto);
	}

}
