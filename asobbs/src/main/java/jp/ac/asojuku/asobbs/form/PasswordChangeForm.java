package jp.ac.asojuku.asobbs.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class PasswordChangeForm {

	@NotEmpty(message = "{errmsg0502}")
	private String oldPassword;

	@NotEmpty(message = "{errmsg0503}")
	private String newPassword1;

	private String newPassword2;
}
