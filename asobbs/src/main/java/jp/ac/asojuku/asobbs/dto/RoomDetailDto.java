package jp.ac.asojuku.asobbs.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RoomDetailDto {

	private Integer roomId;
	private String roomName;
	private List<UserListDto> adminList = new ArrayList<UserListDto>();
	private List<UserListDto> userList = new ArrayList<UserListDto>();
	private List<CategoryListDto> categoryList = new ArrayList<CategoryListDto>();
	private Integer roomBbsNum;	//このルーム内のすべての掲示情報数
	private Boolean allUser;
	private Boolean isAdmin;

	public void addAdminListDto( UserListDto userListDto ) {
		adminList.add(userListDto);
	}

	public void addUserListDto( UserListDto userListDto ) {
		userList.add(userListDto);
	}
	
	public void addCategoryList(CategoryListDto list) {
		categoryList.add(list);
	}
}
