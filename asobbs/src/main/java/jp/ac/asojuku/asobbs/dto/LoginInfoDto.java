package jp.ac.asojuku.asobbs.dto;

import jp.ac.asojuku.asobbs.param.RoleId;
import lombok.Data;

/**
 * ログイン情報
 * 
 * @author nishino
 *
 */
@Data
public class LoginInfoDto {

	private int userId;
	private String mail;
	private String nickName;
	private RoleId role;
	private String roleName;
	private String courseName;
	private int grade;
	

	public boolean isStudent() {
		return role == RoleId.STUDENT;
	}

	public boolean isTeacher() {
		return role == RoleId.TEACHER;
	}

	public boolean isManager() {
		return role == RoleId.MANAGER;
	}
}
