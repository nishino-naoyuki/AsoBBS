package jp.ac.asojuku.asobbs.service;

import static jp.ac.asojuku.asobbs.repository.RoomSpecifications.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.asobbs.dto.CategoryDto;
import jp.ac.asojuku.asobbs.dto.CategoryListDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.dto.RoomDetailDto;
import jp.ac.asojuku.asobbs.dto.RoomInsertDto;
import jp.ac.asojuku.asobbs.dto.RoomListDto;
import jp.ac.asojuku.asobbs.dto.UserListDto;
import jp.ac.asojuku.asobbs.entity.BbsTblEntity;
import jp.ac.asojuku.asobbs.entity.CategoryTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomUserTblEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.exception.MailNotFoundException;
import jp.ac.asojuku.asobbs.form.RoomInputForm;
import jp.ac.asojuku.asobbs.form.RoomSearchForm;
import jp.ac.asojuku.asobbs.param.RoleId;
import jp.ac.asojuku.asobbs.param.RoomRoleId;
import jp.ac.asojuku.asobbs.param.StringConst;
import jp.ac.asojuku.asobbs.repository.BbsRepository;
import jp.ac.asojuku.asobbs.repository.CategoryRepository;
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
	@Autowired
	BbsRepository bbsRepository;
	
	/**
	 * ルーム情報の追加
	 * 
	 * @param roomInsetDto
	 * @param loginInfo
	 * @throws MailNotFoundException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insert(RoomInsertDto roomInsetDto,LoginInfoDto loginInfo) throws MailNotFoundException {
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
	 * @throws MailNotFoundException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void update(RoomInsertDto roomInsetDto,LoginInfoDto loginInfo,Integer roomId) throws MailNotFoundException {
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
		roomEntity.setAllFlg( (roomInsetDto.getAllUserFlag() ? 1:0) );
		
		//作成情報
		UserTblEntity createUserTbl = userRepository.getOne(userId);
		roomEntity.setCreateUserId(createUserTbl);
		roomEntity.setUpdateUserId(createUserTbl);
		
		return roomEntity;
	}

	public List<RoomListDto> getListBy(LoginInfoDto loginInfo){
		return getListBy(new RoomSearchForm(),loginInfo);
	}
	/**
	 * ルーム一覧を取得する
	 * @param searchCondition
	 * @return
	 */
	public List<RoomListDto> getListBy(RoomSearchForm searchCondition,LoginInfoDto loginInfo){
		return getListBy(searchCondition,loginInfo,false);
	}
	public List<RoomListDto> getListBy(RoomSearchForm searchCondition,LoginInfoDto loginInfo,boolean allFlag){
		List<RoomListDto> list = new ArrayList<RoomListDto>();
		
		//学生の場合は自分が所属持しているルームのみを取得する
		Integer filterUserId = ( allFlag == false ? loginInfo.getUserId() : null );
		Integer allFlg = ( allFlag == false ? 1 : null );

		
		//検索条件を指定して実行
		List<RoomTblEntity> entityList = roomRepository.findAll(
				Specification.where(roomNameContains(searchCondition.getName())).
				and(roomUserContains(filterUserId)).or(roomAllUsers(allFlg))
				);
		
		//entity->dto
		for(RoomTblEntity entity : entityList) {
			RoomListDto dto = getListDtoFrom(entity);
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
	public RoomDetailDto getRoomDetailDtoBy(Integer roomId,Integer userId) {
		RoomTblEntity entity = roomRepository.getOne(roomId);
		
		return getRoomDetailDto(entity,userId);
	}

	/**
	 * RoomDetailDtoを取得する
	 * @param entity
	 * @return
	 */
	public RoomDetailDto getRoomDetailDto(
			RoomTblEntity entity,Integer userId
			) {
		RoomDetailDto roomDetailDto = new RoomDetailDto();

		//ルームID、ルーム名
		roomDetailDto.setRoomId(entity.getRoomId());
		roomDetailDto.setRoomName(entity.getName());
		//管理者リストを取得
		boolean isAdmin = false;
		for(RoomUserTblEntity roomUser : entity.getRoomUserTblSet()) {
			UserTblEntity userEntity = userRepository.getOne(  roomUser.getUserId() );
			if( RoomRoleId.ADMIN.equals(roomUser.getRoomRole()) ) {
				roomDetailDto.addAdminListDto( getUserListDtoFrom(userEntity) );
			}else {
				roomDetailDto.addUserListDto( getUserListDtoFrom(userEntity) );
			}
		}
		
		//このルーム内の掲示情報数
		int allBbsNum = 0;
		
		//カテゴリリストを登録する
		for(CategoryTblEntity categoryEntity : entity.getCategoryTblSet()) {
			CategoryListDto categoryListDto = getCategoryDtoFrom(categoryEntity);
			roomDetailDto.addCategoryList( categoryListDto );
			allBbsNum += categoryListDto.getBbsNum();	//カテゴリごとの掲示情報を集計する
		}
		
		roomDetailDto.setRoomBbsNum(allBbsNum);
		
		//全員フラグ
		roomDetailDto.setAllUser( (entity.getAllFlg() == 1 ? true:false) );
		
		//管理者かどうかをせっと
		roomDetailDto.setIsAdmin(getIsAdminUser(userId,roomDetailDto));
		
		return roomDetailDto;
	}
	
	/**
	 * 指定されたユーザーがその掲示板情報の管理者かどうかを取得する
	 * 
	 * @param userId
	 * @param roomDetailDto
	 * @return
	 */
	private boolean getIsAdminUser(Integer userId,RoomDetailDto roomDetailDto) {
		if( roomDetailDto == null ) {
			return false;
		}
		boolean isAdmin = false;
		for( UserListDto userDto : roomDetailDto.getAdminList() ) {
			if( userDto.getUserId() == userId ) {
				isAdmin = true;
				break;
			}
		}
		return isAdmin;
	}
	
	/**
	 * CategoryListDtoを作成する
	 * 
	 * @param categoryEntity
	 * @return
	 */
	private CategoryListDto getCategoryDtoFrom(CategoryTblEntity categoryEntity) {
		CategoryListDto categoryListDto = new CategoryListDto();
		
		categoryListDto.setId( categoryEntity.getCategoryId() );
		categoryListDto.setName( categoryEntity.getName() );
		categoryListDto.setBbsNum( bbsRepository.getCount(categoryEntity) );
		
		return categoryListDto;
	}
	
	/**
	 * RoomTblEntityからRoomListDtoを作成する
	 * 
	 * @param entity
	 * @return
	 */
	private RoomListDto getListDtoFrom(RoomTblEntity entity) {
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
		if( entity.getAllFlg() == 1 ) {
			//全員
			dto.setUserNum(StringConst.ALLUSERS);
		}else {
			dto.setUserNum(String.valueOf(userCount));
		}
		dto.setCategoryNum(entity.getCategoryTblSet().size());
		
		return dto;
	}
	
	/**
	 * ルームに所属するユーザーを更新する
	 * ※作成しなおし（いったん削除して追加）する
	 * @param roomId
	 * @param roomInsetDto
	 * @throws MailNotFoundException 
	 */
	private void saveRoomAdminUsers(Integer roomId,RoomInsertDto roomInsetDto) throws MailNotFoundException {
		List<RoomUserTblEntity> roomUserList = new ArrayList<RoomUserTblEntity>();
		
		/////////////////////////////////////////////
		//該当ルームの情報をいったん削除
		List<RoomUserTblEntity> list = roomUserRepository.getListBy(roomId);
		if( list.size() > 0 ) {
			roomUserRepository.deleteAll(list);
		}
		
		/////////////////////////////////////////////
		//所属ユーザー（管理者と一般ユーザー）
		if( !roomInsetDto.getAllUserFlag() ) {
			//対象が全員の場合は登録しない
			roomUserList = addRoomUsers(roomUserList,roomId,roomInsetDto);
		}
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
	 * ※対象者が全員の場合は、登録しない
	 * 
	 * @param roomUserList
	 * @param roomId
	 * @param roomInsetDto
	 * @return
	 * @throws MailNotFoundException 
	 */
	private List<RoomUserTblEntity> addRoomUsers(
			List<RoomUserTblEntity> roomUserList,Integer roomId,RoomInsertDto roomInsetDto) throws MailNotFoundException {

		//個別に追加する場合は１けんづつ登録していく
		for(String mail : roomInsetDto.getUserMailList()) {
			RoomUserTblEntity roomUserTbl = new RoomUserTblEntity();
			UserTblEntity userEntity = userRepository.getUserByMail(mail);
			
			//メールアドレスがない
			if( userEntity == null ) {
				throw new MailNotFoundException("メールアドレスが存在しません："+mail);
			}

			roomUserTbl.setRoomId(roomId);
			roomUserTbl.setUserId(userEntity.getUserId());
			roomUserTbl.setRoomRole(RoomRoleId.USER.getId());
			
			roomUserList.add(roomUserTbl);
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
			if( StringUtils.isNotEmpty(admin)) {
				UserTblEntity userEntity = userRepository.getUserByMail(admin);
				roomConfirmDto.addAdminListDto( getUserListDtoFrom(userEntity) );
			}
		}
		//所属者を取得
		String[] users = roomInputForm.getRoomUsers().split(",",0);
		for( String user : users ) {
			if( StringUtils.isNotEmpty(user)) {
				roomConfirmDto.addUserMailDto(user);
			}
		}
		//全員フラグ
		roomConfirmDto.setAllUserFlag(roomInputForm.getAllUserFlg());
		
		return roomConfirmDto;
	}
	

	/**
	 * UserEntityからUserListDtoを生成する
	 * 
	 * @param userEntity
	 * @return
	 */
	private UserListDto getUserListDtoFrom(UserTblEntity userEntity) {
		if( userEntity == null ) {
			return new UserListDto();
		}
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
		
		roomInputForm.setAllUserFlg( (entity.getAllFlg()==1?true:false) );
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
	
	/**
	 * ルームに所属するカテゴリの一覧を取得する
	 * 
	 * @param roomId
	 * @return
	 */
	public List<CategoryListDto> getCategoryListDto(Integer roomId){
		
		RoomTblEntity roomEntity = roomRepository.getOne(roomId);
		List<CategoryListDto> list = new ArrayList<CategoryListDto>();

		//カテゴリリストを登録する
		for(CategoryTblEntity categoryEntity : roomEntity.getCategoryTblSet()) {
			CategoryListDto categoryListDto = getCategoryDtoFrom(categoryEntity);
			list.add(categoryListDto);
		}
		
		return list;
	}
	
	/**
	 * 名前を指定して一致したEntityを返す
	 * 
	 * @param roomName
	 * @return
	 */
	public RoomTblEntity getBy(String roomName ) {
		return roomRepository.getBy(roomName);
	}
	
	/**
	 * 指定されたメールアドレスが存在するか？
	 * 
	 * @param mail
	 * @return true：存在する false：存在しない
	 */
	public boolean isExistsMailaddress(String mail) {
		return (userRepository.getUserByMail(mail) != null);
	}
}
