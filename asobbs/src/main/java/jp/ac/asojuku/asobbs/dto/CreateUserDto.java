package jp.ac.asojuku.asobbs.dto;

import lombok.Data;

/**
 * ユーザー作成用のDTO
 * @author nishino
 *
 */
@Data
public class CreateUserDto {
	private Integer roleId;
	private String studentNo;
	private String mailadress;
	private String nickname;
	private Integer courseId;
	private String password;
	private String admissionYear;
}
