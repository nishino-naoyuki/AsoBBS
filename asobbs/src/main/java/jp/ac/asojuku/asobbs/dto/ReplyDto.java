package jp.ac.asojuku.asobbs.dto;

import lombok.Data;

@Data
public class ReplyDto {
	private String content;
	private String writerName;
	private String writeDate;
}
