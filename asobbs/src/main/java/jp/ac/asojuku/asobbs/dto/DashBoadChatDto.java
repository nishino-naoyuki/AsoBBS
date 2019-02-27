package jp.ac.asojuku.asobbs.dto;

import lombok.Data;

/**
 * ダッシュボード用チャット情報
 * @author nishino
 *
 */
@Data
public class DashBoadChatDto {

	//チャット情報
	private Integer fromUserId;
	private String fromUserName;
}
