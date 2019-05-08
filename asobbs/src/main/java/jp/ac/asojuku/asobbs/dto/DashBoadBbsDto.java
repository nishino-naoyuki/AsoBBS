package jp.ac.asojuku.asobbs.dto;

import lombok.Data;

/**
 * ダッシュボードに表示する掲示板情報
 * @author nishino
 *
 */
@Data
public class DashBoadBbsDto {
	private final Integer BBS_NAME_MAX = 20;
	//掲示板情報
	private Integer roomId;
	private String roomName;
	private Integer categoryId;
	private Integer bbsId;
	private String bbsName;
	private boolean update;
	
	public String getBbsName() {
		String bbsName = this.bbsName;
		
		if( bbsName != null && bbsName.length() > BBS_NAME_MAX) {
			bbsName = this.bbsName.substring(0, BBS_NAME_MAX) + "...";
		}
		
		return bbsName;
	}
}
