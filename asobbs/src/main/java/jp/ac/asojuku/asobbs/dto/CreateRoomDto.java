package jp.ac.asojuku.asobbs.dto;

import java.util.List;

import lombok.Data;

/**
 * ルーム情報の登録用DTO
 * @author nishino
 *
 */
@Data
public class CreateRoomDto {
	private String name;
	private String roomAdminId;
	private String roomSubAdminId;
	private List<String> belongUserId;
}
