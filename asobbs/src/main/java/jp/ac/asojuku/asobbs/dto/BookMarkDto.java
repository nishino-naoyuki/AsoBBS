package jp.ac.asojuku.asobbs.dto;

import lombok.Data;

/**
 * お気に入りのDTO
 * 
 * @author nishino
 *
 */
@Data
public class BookMarkDto {
	private final Integer BBS_NAME_MAX = 15;
	
	private Integer bookmarkId;
	private Integer bbsId;
	private String roomName;
	private String categoryName;
	private String title;
	private String date;
	
	public String getTitle() {

		String title = this.title;
		
		if( title != null && title.length() > BBS_NAME_MAX) {
			title = this.title.substring(0, BBS_NAME_MAX) + "...";
		}
		
		return title;
	}
}
