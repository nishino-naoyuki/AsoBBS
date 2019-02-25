package jp.ac.asojuku.asobbs.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class BbsReplyInputForm {
	
	private Integer bbsId;	//親の掲示板ID
	private Integer categoryId;

	@NotEmpty(message = "{errmsg0304}")
	private String comment;
}
