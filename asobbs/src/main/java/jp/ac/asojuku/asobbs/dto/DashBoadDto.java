package jp.ac.asojuku.asobbs.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * ダッシュボード用DTO
 * @author nishino
 *
 */
@Data
public class DashBoadDto {
	
	List<DashBoadBbsDto> bbsList = new ArrayList<DashBoadBbsDto>();
	List<DashBoadChatDto> chatList = new ArrayList<DashBoadChatDto>();
	
	public void addDashBoadBbsDto(DashBoadBbsDto bbsDto) {
		bbsList.add(bbsDto);
	}
	public void addDashBoadChatDto(DashBoadChatDto chatDto) {
		chatList.add(chatDto);
	}
}
