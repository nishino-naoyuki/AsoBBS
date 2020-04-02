package jp.ac.asojuku.asobbs.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BbsListDto {
	private final Integer BBS_NAME_MAX = 25;
	private Integer id;
	private String title;
	private String updateDate;

	public String getTitle() {

		String title = this.title;
		
		if( title != null && title.length() > BBS_NAME_MAX) {
			title = this.title.substring(0, BBS_NAME_MAX) + "...";
		}
		
		return title;
	}
}
