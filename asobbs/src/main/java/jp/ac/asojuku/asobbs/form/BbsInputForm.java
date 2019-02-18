package jp.ac.asojuku.asobbs.form;

import lombok.Data;

@Data
public class BbsInputForm {
	private String categoryName;
	private String title;
	private Boolean emergencyFlg;
	private String content;
}
