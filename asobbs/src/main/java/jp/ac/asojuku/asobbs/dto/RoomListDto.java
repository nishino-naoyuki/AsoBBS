package jp.ac.asojuku.asobbs.dto;

import lombok.Data;

@Data
public class RoomListDto {
	//ルームID
	private Integer roomId;
	//ルーム名
	private String name;
	//管理者名
	private String adminUserName;
	//カテゴリ数
	private Integer categoryNum;
	//ユーザー数
	private String userNum;
}
