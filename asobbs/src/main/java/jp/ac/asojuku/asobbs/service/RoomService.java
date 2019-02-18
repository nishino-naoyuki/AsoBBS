package jp.ac.asojuku.asobbs.service;

import static jp.ac.asojuku.asobbs.repository.RoomSpecifications.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.dto.RoomDetailDto;
import jp.ac.asojuku.asobbs.dto.RoomInsertDto;
import jp.ac.asojuku.asobbs.dto.RoomListDto;
import jp.ac.asojuku.asobbs.dto.UserListDto;
import jp.ac.asojuku.asobbs.entity.RoomTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomUserTblEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.form.RoomInputForm;
import jp.ac.asojuku.asobbs.form.RoomSearchForm;
import jp.ac.asojuku.asobbs.param.RoleId;
import jp.ac.asojuku.asobbs.param.RoomRoleId;
import jp.ac.asojuku.asobbs.repository.RoomRepository;
import jp.ac.asojuku.asobbs.repository.RoomUserRepository;
import jp.ac.asojuku.asobbs.repository.UserRepository;

@Service
public class RoomService {

	@Autowired 
	UserRepository userRepository;
	@Autowired
	RoomRepository roomRepository;
	@Autowired
	RoomUserRepository roomUserRepository;
	
	/**
	 * ルーム情報の追加
	 * 
	 * @param roomInsetDto
	 * @param loginInfo
	 */
	@Transactional
	public void insert(RoomInsertDto roomInsetDto,LoginInfoDto loginInfo) {
		//DtoからEntityを作成する
		RoomTblEntity roomEntity = getRoomTblEntityFrom(null,roomInsetDto,loginInfo.getUserId());
		
		roomRepository.save(roomEntity);
		
		saveRoomAdminUsers(roomEntity.getRoomId(),roomInsetDto);
	}

	/**
	 * 更新処理
	 * @param roomInsetDto
	 * @param loginInfo
	 * @param roomId
	 */
	@Transactional
	public void update(RoomInsertDto roomInsetDto,LoginInfoDto loginInfo,Integer roomId) {
		//DtoからEntityを作成する
		RoomTblEntity roomEntity =roomRepository.getOne(roomId);
		roomEntity = getRoomTblEntityFrom(roomEntity,roomInsetDto,loginInfo.getUserId());
		
		roomRepository.save(roomEntity);
		
		saveRoomAdminUsers(roomEntity.getRoomId(),roomInsetDto);
	}
	/**
	 * DTOからRoomTblEntityを作成する
	 * 
	 * @param roomInsetDto
	 * @param userId
	 * @return
	 */
	public RoomTblEntity getRoomTblEntityFrom(RoomTblEntity roomEntity,RoomInsertDto roomInsetDto,Integer userId) {
		if( roomEntity == null ) {
			roomEntity = new RoomTblEntity();
		}
		
		//ルーム情報
		roomEntity.setName(roomInsetDto.getRoomName());
		
		//作成情報
		UserTblEntity createUserTbl = userRepository.getOne(userId);
		roomEntity.setCreateUserId(createUserTbl);
		roomEntity.setUpdateUserId(createUserTbl);
		
		return roomEntity;
	}
	
	/**
	 * ルーム一覧を取得する
	 * @param searchCondition
	 * @return
	 */
	public List<RoomListDto> getListBy(RoomSearchForm searchCondition,LoginInfoDto loginInfo){
		List<RoomListDto> list = new ArrayList<RoomListDto>();
		
		//学生の場合は自分が所属持しているルームのみを取得する
		Integer filterUserId = ( RoleId.STUDENT.equals( loginInfo.getRole() ) ? loginInfo.getUserId() : null );
		//検索条件を指定して実行
		List<RoomTblEntity> entityList = roomRepository.findAll(
				Specification.where(roomNameContains(searchCondition.getName())).
				and(roomUserContains(filterUserId))
				);
		
		//entity->dto
		for(RoomTblEntity entity : entityList) {
			RoomListDto dto = getDtoFrom(entity);
			
			list.add(dto);
		}
		
		return list;
	}
	
	/**
	 * 指定されたルームの編集情報を取得する
	 * @param roomId
	 * @return
	 */
	public RoomInputForm getRoomInputFormBy(Integer roomId) {
		RoomTblEntity entity = roomRepository.getOne(roomId);
		
		return getRoomInputFormFrom(entity);
	}

	/**
	 * ルーム詳細画面の表示
	 * @param roomId
	 * @return
	 */
	public RoomDetailDto getRoomDetailDtoBy(Integer roomId) {
		RoomTblEntity entity = roomRepository.getOne(roomId);
		
		return getRoomDetailDto(entity);
	}

	/**
	 * RoomDetailDtoを取得する
	 * @param entity
	 * @return
	 */
	public RoomDetailDto getRoomDetailDto(
			RoomTblEntity entity
			) {
		RoomDetailDto roomDetailDto = new RoomDetailDto();

		//ルームID、ルーム名
		roomDetailDto.setRoomId(entity.getRoomId());
		roomDetailDto.setRoomName(entity.getName());
		//管理者リストを取得
		for(RoomUserTblEntity roomUser : entity.getRoomUserTblSet()) {
			UserTblEntity userEntity = userRepository.getOne(  roomUser.getUserId() );
			if( RoomRoleId.ADMIN.equals(roomUser.getRoomRole()) ) {
				roomDetailDto.addAdminListDto( getUserListDtoFrom(userEntity) );
			}else {
				roomDetailDto.addUserListDto( getUserListDtoFrom(userEntity) );
			}
		}
		
		return roomDetailDto;
	}
	/**
	 * RoomTblEntityからRoomListDtoを作成する
	 * 
	 * @param entity
	 * @return
	 */
	private RoomListDto getDtoFrom(RoomTblEntity entity) {
		RoomListDto dto = new RoomListDto();
		
		dto.setRoomId(entity.getRoomId());
		dto.setName(entity.getName());
		StringBuffer adminNames = new StringBuffer();
		int userCount = 0;
		for( RoomUserTblEntity roomUser : entity.getRoomUserTblSet() ) {
			if( RoomRoleId.ADMIN.equals( roomUser.getRoomRole() ) ) {
				//管理者の場合は名前をカンマ区切りで追記
				UserTblEntity userEntity = userRepository.getOne(  roomUser.getUserId() );
				adminNames.append( userEntity.getNickName() + " ");
			}
			userCount++;
		}
		dto.setAdminUserName(adminNames.toString());
		dto.setUserNum(userCount);
		dto.setCategoryNum(entity.getCategoryTblSet().size());
		
		return dto;
	}
	
	/**
	 * ルームに所属するユーザーを更新する
	 * ※作成しなおし（いったん削除して追加）する
	 * @param roomId
	 * @param roomInsetDto
	 */
	private void saveRoomAdminUsers(Integer roomId,RoomInsertDto roomInsetDto) {
		List<RoomUserTblEntity> roomUserList = new ArrayList<RoomUserTblEntity>();
		
		/////////////////////////////////////////////
		//該当ルームの情報をいったん削除
		List<RoomUserTblEntity> list = roomUserRepository.getListBy(roomId);
		if( list.size() > 0 ) {
			roomUserRepository.deleteAll(list);
		}
		
		/////////////////////////////////////////////
		//所属ユーザー（管理者と一般ユーザー）
		roomUserList = addRoomUsers(roomUserList,roomId,roomInsetDto);
		roomUserList = addRoomAdmins(roomUserList,roomId,roomInsetDto);
		
		roomUserRepository.saveAll(roomUserList);
	}
	
	/**
	 * 管理者を登録する
	 * 
	 * @param roomUserList
	 * @param roomId
	 * @param roomInsetDto
	 * @return
	 */
	private List<RoomUserTblEntity> addRoomAdmins(
			List<RoomUserTblEntity> roomUserList,Integer roomId,RoomInsertDto roomInsetDto) {

		//管理者をセット
		for( UserListDto admin : roomInsetDto.getAdminList()) {
			RoomUserTblEntity roomUserTbl = new RoomUserTblEntity();

			roomUserTbl.setRoomId(roomId);
			roomUserTbl.setUserId(admin.getUserId());
			roomUserTbl.setRoomRole(RoomRoleId.ADMIN.getId());
			
			roomUserList.add(roomUserTbl);
		}
		
		return roomUserList;
	}

	/**
	 * 一般ユーザーを登録する
	 * @param roomUserList
	 * @param roomId
	 * @param roomInsetDto
	 * @return
	 */
	private List<RoomUserTblEntity> addRoomUsers(
			List<RoomUserTblEntity> roomUserList,Integer roomId,RoomInsertDto roomInsetDto) {

		if( roomInsetDto.getAllUserFlag() != null && roomInsetDto.getAllUserFlag() ) {
			//全ての場合は、全権取得した後に登録する
			List<UserTblEntity> userList = userRepository.findAll();
			for(UserTblEntity userEntity : userList) {
				RoomUserTblEntity roomUserTbl = new RoomUserTblEntity();
				roomUserTbl.setRoomId(roomId);
				roomUserTbl.setUserId(userEntity.getUserId());
				roomUserTbl.setRoomRole(RoomRoleId.USER.getId());
				
				roomUserList.add(roomUserTbl);
			}
			
		}else {
			//個別に追加する場合は１けんづつ登録していく
			for(String mail : roomInsetDto.getUserMailList()) {
				RoomUserTblEntity roomUserTbl = new RoomUserTblEntity();
				UserTblEntity userEntity = userRepository.getUserByMail(mail);
	
				roomUserTbl.setRoomId(roomId);
				roomUserTbl.setUserId(userEntity.getUserId());
				roomUserTbl.setRoomRole(RoomRoleId.USER.getId());
				
				roomUserList.add(roomUserTbl);
			}
		}

		return roomUserList;
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
			RoomInputForm roomInputForm
			) {
		RoomInsertDto roomConfirmDto = new RoomInsertDto();

		//ユーザー名
		roomConfirmDto.setRoomName(roomInputForm.getRoomName());
		//管理者リストを取得
		String[] admins = roomInputForm.getRoomAdmins().split(",",0);
		for( String admin : admins ) {
			UserTblEntity userEntity = userRepository.getUserByMail(admin);
			roomConfirmDto.addAdminListDto( getUserListDtoFrom(userEntity) );
		}
		//所属者を取得
		String[] users = roomInputForm.getRoomUsers().split(",",0);
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
	
	/**
	 * Entity→Form変換
	 * @param entity
	 * @return
	 */
	private RoomInputForm getRoomInputFormFrom(RoomTblEntity entity) {
		RoomInputForm roomInputForm = new RoomInputForm();
		
		roomInputForm.setAllUserFlg(false);
		roomInputForm.setRoomName(entity.getName());
		//所属ユーザー名
		StringBuffer sbUsers = new StringBuffer();
		StringBuffer sbAdmins = new StringBuffer();
		for(RoomUserTblEntity roomUser : entity.getRoomUserTblSet()) {
			UserTblEntity userEntity = userRepository.getOne(  roomUser.getUserId() );
			if( RoomRoleId.ADMIN.equals(roomUser.getRoomRole()) ) {
				sbAdmins.append(userEntity.getMailadress()+",");
			}else {
				sbUsers.append(userEntity.getMailadress()+",");
			}
		}
		roomInputForm.setRoomAdmins(sbAdmins.toString());
		roomInputForm.setRoomUsers(sbUsers.toString());
		
		return roomInputForm;
	}
}
