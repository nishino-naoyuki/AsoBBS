package jp.ac.asojuku.asobbs.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BbsDetailDto {

	private Integer bbsId;
	private String categoryName;
	private String title;
	private Boolean emergencyFlg;
	private Boolean replyOkFlg;
	private String content;
	private List<ReplyDto> replyDtoList = new ArrayList<ReplyDto>();
	private List<AttachedFileDto> attachedFileList  = new ArrayList<AttachedFileDto>();
	
	public void addAttachedFileDto(AttachedFileDto attachedFileDto) {
		attachedFileList.add(attachedFileDto);
	}
	
	public void addReplyDto(ReplyDto replyDto) {
		replyDtoList.add(replyDto);
	}
	
	public boolean isExistAttachedFile() {
		return (attachedFileList.size() > 0);
	}
	
}
