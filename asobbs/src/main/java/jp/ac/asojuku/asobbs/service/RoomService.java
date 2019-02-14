package jp.ac.asojuku.asobbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.asobbs.dto.CreateRoomDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.dto.RoomInsertDto;
import jp.ac.asojuku.asobbs.dto.UserListDto;
import jp.ac.asojuku.asobbs.entity.RoomTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomUserTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomUserTblId;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.repository.RoomRepository;
import jp.ac.asojuku.asobbs.repository.UserRepository;

@Service
public class RoomService {

	@Autowired 
	UserRepository userRepository;
	@Autowired
	RoomRepository roomRepository;
	
	@Transactional
	public void insert(RoomInsertDto roomInsetDto,LoginInfoDto loginInfo) {
		//DtoからEntityを作成する
		RoomTblEntity roomEntity = getRoomTblEntityFrom(roomInsetDto,loginInfo.getUserId());
		
		roomRepository.save(roomEntity);
	}
	
	public RoomTblEntity getRoomTblEntityFrom(RoomInsertDto roomInsetDto,Integer userId) {
		RoomTblEntity roomEntity = new RoomTblEntity();
		
		//ルーム情報
		roomEntity.setName(roomInsetDto.getRoomName());
		//所属ユーザー（管理者と一般ユーザー）
		
		//管理者をセット
//		for( UserListDto admin : roomInsetDto.getAdminList()) {
//			RoomUserTblEntity roomUserTbl = new RoomUserTblEntity();
//			RoomUserTblId roomUserId = new RoomUserTblId();
//			
//			roomUserId.setUserId(admin.getUserId());
//			roomUserTbl.setRoomUserTblId(roomUserId);
//		}
		
		//作成情報
		UserTblEntity createUserTbl = userRepository.getOne(userId);
		roomEntity.setCreateUserTbl(createUserTbl);
		
		return roomEntity;
	}

	/**
	 * RoomConfirmDtoを生成する
	 * 
	 * @param roomname
	 * @param roomadmin
	 * @param roombelong
	 * @return
	 */
	public RoomInsertDto getRoomConfirmDto(
			String roomname,String roomadmin,String roombelong) {
		RoomInsertDto roomConfirmDto = new RoomInsertDto();

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
