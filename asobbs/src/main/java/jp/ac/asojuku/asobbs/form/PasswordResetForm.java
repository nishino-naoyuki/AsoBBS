package jp.ac.asojuku.asobbs.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * パスワードをリセットするためのフォーム
 * @author nishino
 *
 */
@Data
public class PasswordResetForm {

	@NotEmpty(message = "{errmsg0505}")
	private String mail;

	@NotEmpty(message = "{errmsg0503}")
	private String newPassword1;
	private String newPassword2;
	
}
