package jp.ac.asojuku.asobbs.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BbsDetailDto {

	private Integer bbsId;
	private Integer roomId;
	private String roomName;
	private Integer categoryId;
	private String categoryName;
	private String title;
	private Boolean emergencyFlg;
	private Boolean anyoneFlg;
	private Boolean replyOkFlg;
	private String content;
	private String updateDate;
	private String updateName;
	private List<ReplyDto> replyDtoList = new ArrayList<ReplyDto>();
	private List<AttachedFileDto> attachedFileList  = new ArrayList<AttachedFileDto>();
	private Boolean emergencyReplyFlg;	//すでに緊急掲示板に返信済みかどうか
	
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
