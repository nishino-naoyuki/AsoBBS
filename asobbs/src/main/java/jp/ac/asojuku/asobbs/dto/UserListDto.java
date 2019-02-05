package jp.ac.asojuku.asobbs.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class UserListDto {

	private String studentNo;
	
	private String roleName;

	private String mailadress;
	
	private String nickname;
	
	private String courseName;
	
	private Integer grade;
	
}
