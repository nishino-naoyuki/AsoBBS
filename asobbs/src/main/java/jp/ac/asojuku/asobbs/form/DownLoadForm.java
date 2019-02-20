package jp.ac.asojuku.asobbs.form;

import lombok.Data;

@Data
public class DownLoadForm {
	private Integer id;
	private String filePath;
	private Long size;
}
