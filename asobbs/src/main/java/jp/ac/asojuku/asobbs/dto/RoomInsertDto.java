package jp.ac.asojuku.asobbs.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;

@Data
public class RoomInsertDto {

	private String roomName;
	private List<UserListDto> adminList = new ArrayList<UserListDto>();
	private List<String> userMailList = new ArrayList<String>();
	private Boolean allUserFlag;
	
	public void addAdminListDto( UserListDto userListDto ) {
		adminList.add(userListDto);
	}

	public void addUserMailDto( String userMail ) {
		userMailList.add(userMail);
	}
}
