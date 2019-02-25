package jp.ac.asojuku.asobbs.dto;

import lombok.Data;

@Data
public class BbsCheckTblDto {
	private String studentNo;
	
	private String mailadress;
	
	private String nickname;
	
	private String courseName;
	
	private Integer grade;
	
	private String checkDate;
}
