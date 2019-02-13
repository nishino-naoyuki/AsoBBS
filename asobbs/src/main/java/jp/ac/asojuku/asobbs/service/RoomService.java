package jp.ac.asojuku.asobbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.asobbs.dto.CreateRoomDto;
import jp.ac.asojuku.asobbs.dto.RoomConfirmDto;
import jp.ac.asojuku.asobbs.dto.UserListDto;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.repository.UserRepository;

@Service
public class RoomService {

	@Autowired 
	UserRepository userRepository;
	
	public void insert(CreateRoomDto roomDto) {
		
	}
	

	/**
	 * RoomConfirmDtoを生成する
	 * 
	 * @param roomname
	 * @param roomadmin
	 * @param roombelong
	 * @return
	 */
	public RoomConfirmDto getRoomConfirmDto(
			String roomname,String roomadmin,String roombelong) {
		RoomConfirmDto roomConfirmDto = new RoomConfirmDto();

		//ユーザー名
		roomConfirmDto.setRoomName(roomname);
		//管理者リストを取得
		String[] admins = roomadmin.split(",",0);
		for( String admin : admins ) {
			UserTblEntity userEntity = userRepository.getUserByMail(admin);
			roomConfirmDto.addAdminListDto( getUserListDtoFrom(userEntity) );
		}
		//所属者を取得
		String[] users = roombelong.split(",",0);
		for( String user : users ) {
			roomConfirmDto.addUserMailDto(user);
		}
		
		return roomConfirmDto;
	}
	

	/**
	 * UserEntityからUserListDtoを生成する
	 * 
	 * @param userEntity
	 * @return
	 */
	private UserListDto getUserListDtoFrom(UserTblEntity userEntity) {
		UserListDto userListDto = new UserListDto();
		
		userListDto.setCourseName(userEntity.getCourseMaster().getCourseName());
		userListDto.setMailadress(userEntity.getMailadress());
		userListDto.setNickname(userEntity.getNickName());
		userListDto.setRoleName(userEntity.getRoleMaster().getRoleName());
		userListDto.setStudentNo(userEntity.getStudentNo());
		userListDto.setUserId(userEntity.getUserId());
		
		return userListDto;
	}
}
