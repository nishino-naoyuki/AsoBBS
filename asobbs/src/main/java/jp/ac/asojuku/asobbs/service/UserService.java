package jp.ac.asojuku.asobbs.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.asobbs.dto.CreateUserDto;
import jp.ac.asojuku.asobbs.dto.UserListDto;
import jp.ac.asojuku.asobbs.entity.CourseMasterEntity;
import jp.ac.asojuku.asobbs.entity.RoleMasterEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.repository.UserRepository;
import jp.ac.asojuku.asobbs.util.Digest;
import static jp.ac.asojuku.asobbs.repository.UserSpecifications.*;

@Service
public class UserService {
	@Autowired 
	UserRepository userRepository;
	
	/**
	 * 登録処理
	 * 
	 * @param userdto
	 * @throws AsoBbsSystemErrException
	 */
	public void insert(CreateUserDto userdto) throws AsoBbsSystemErrException {
		
		//dto -> entity
		UserTblEntity entity = createUserTblEntityFromDto(userdto);
		
		userRepository.saveAndFlush(entity);
		
	}
	
	/**
	 * ユーザー情報リストの取得
	 * @return
	 */
	public List<UserListDto> getList(){
		List<UserListDto> list = new ArrayList<UserListDto>();
		
		List<UserTblEntity> entityList = userRepository.findAll();
		
		for( UserTblEntity entity : entityList ) {
			UserListDto userDto = new UserListDto();
			
			userDto.setCourseName(entity.getCourseMaster().getCourseName());
			userDto.setGrade(entity.getGrade());
			userDto.setMailadress(entity.getMailadress());
			userDto.setNickname(entity.getNickName());
			userDto.setRoleName(entity.getRoleMaster().getRoleName());
			userDto.setStudentNo(entity.getStudentNo());
			userDto.setUserId(entity.getUserId());
			
			list.add(userDto);
		}
		
		return list;
	}
	
	/**
	 * 指定したメールアドレスが既に存在するかどうかを検査する
	 * 
	 * @param mail
	 * @return
	 */
	public boolean isExistMailadress(String mail) {
		return ( userRepository.getUserByMail(mail) != null ? true:false);
	}
	
	
	/**
	 * 学籍番号が存在するかどうかを返す
	 * @param studentNo
	 * @return
	 */
	public boolean isExistStudentNo(String studentNo) {
		return ( userRepository.getUserByStudentNo(studentNo) != null ? true:false);
	}
	
	/**
	 * 条件を指定してのユーザー検索
	 * 
	 * @param mail
	 * @param courseId
	 * @param nickName
	 * @param grade
	 * @return
	 */
	public List<UserListDto> getList(String mail,Integer courseId,String nickName,Integer grade){
		List<UserListDto> list = new ArrayList<UserListDto>();

		List<UserTblEntity> entityList = userRepository.findAll(
				Specification.
						where(mailContains(mail)).
						and(courseEquals(courseId)).
						and(nicknameContains(nickName)).
						and(gradeEquals(grade))
				);

		for( UserTblEntity entity : entityList ) {
			UserListDto userDto = new UserListDto();
			
			userDto.setCourseName(entity.getCourseMaster().getCourseName());
			userDto.setGrade(entity.getGrade());
			userDto.setMailadress(entity.getMailadress());
			userDto.setNickname(entity.getNickName());
			userDto.setRoleName(entity.getRoleMaster().getRoleName());
			userDto.setStudentNo(entity.getStudentNo());
			userDto.setUserId(entity.getUserId());
			
			list.add(userDto);
		}
		
		return list;
	}
	
	
	
	/**
	 * UserTblEntityを作成する
	 * 
	 * @param userdto
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	private UserTblEntity createUserTblEntityFromDto(CreateUserDto userdto) throws AsoBbsSystemErrException {
		UserTblEntity entity = new UserTblEntity();

		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(userdto.getMailadress(), userdto.getPassword());
		
		entity.setAccountExpryDate(null);
		entity.setAdmissionYear(Integer.parseInt(userdto.getAdmissionYear()));
		entity.setCertifyErrCnt(0);
		entity.setGiveUpYear(null);
		entity.setGrade(1);//後でバッチで更新
		entity.setGraduateYear(null);
		entity.setIsFirstFlg(1);
		entity.setIsLockFlg(0);
		entity.setMailadress(userdto.getMailadress());
		entity.setName(userdto.getNickname());
		entity.setNickName(userdto.getNickname());
		entity.setPassword(hashedPwd);
		entity.setPasswordExpirydate(null);
		entity.setRemark(null);
		entity.setRepeatYearCount(0);
		entity.setStudentNo(userdto.getStudentNo());
		
		CourseMasterEntity cm = new CourseMasterEntity();
		cm.setCourseId(userdto.getCourseId());
		
		RoleMasterEntity rm = new RoleMasterEntity();
		rm.setRoleId(userdto.getRoleId());
		
		entity.setCourseMaster(cm);
		entity.setRoleMaster(rm);
		
		return entity;
	}
	
	/**
	 * パスワードの変更
	 * 
	 * @param userId
	 * @param mail
	 * @param newPassword
	 * @param updateUserId
	 * @throws AsoBbsSystemErrException
	 */
	public void changePassword(Integer userId,String mail,String newPassword) throws AsoBbsSystemErrException {

		//ユーザー情報を取得
		UserTblEntity userEntity = userRepository.getOne(userId);
		
		changePassword(userEntity,mail,newPassword);
	}
	
	/**
	 * パスワードを変更する
	 * @param userEntity
	 * @param mail
	 * @param newPassword
	 * @param updateUserId
	 * @throws AsoBbsSystemErrException
	 */
	private void changePassword(UserTblEntity userEntity,String mail,String newPassword) throws AsoBbsSystemErrException {

		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(mail, newPassword);
		
		//パスワード部分を変更
		userEntity.setPassword(hashedPwd);
		
		userRepository.save(userEntity);
	}
	
	/**
	 * パスワードを変更する
	 * 
	 * @param mail
	 * @param newPassword
	 * @throws AsoBbsSystemErrException
	 */
	public void changePassword(String mail,String newPassword) throws AsoBbsSystemErrException {
		UserTblEntity userEntity = userRepository.getUserByMail(mail);

		changePassword(userEntity,mail,newPassword);
	}
}
