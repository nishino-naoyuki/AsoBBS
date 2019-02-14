package jp.ac.asojuku.asobbs.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;

@Data
public class RoomInsertDto {

	String roomName;
	List<UserListDto> adminList = new ArrayList<UserListDto>();
	List<String> userMailList = new ArrayList<String>();
	
	public void addAdminListDto( UserListDto userListDto ) {
		adminList.add(userListDto);
	}

	public void addUserMailDto( String userMail ) {
		userMailList.add(userMail);
	}
}
