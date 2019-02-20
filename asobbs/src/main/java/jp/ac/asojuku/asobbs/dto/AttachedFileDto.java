package jp.ac.asojuku.asobbs.dto;

import lombok.Data;

@Data
public class AttachedFileDto {
	private Integer id;
	private String fileName;
	private String filePath;
	private Long size;
}
