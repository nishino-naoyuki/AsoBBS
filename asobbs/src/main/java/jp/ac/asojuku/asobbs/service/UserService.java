package jp.ac.asojuku.asobbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.asobbs.dto.CreateUserDto;
import jp.ac.asojuku.asobbs.entity.CourseMasterEntity;
import jp.ac.asojuku.asobbs.entity.RoleMasterEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.repository.UserRepository;
import jp.ac.asojuku.asobbs.util.Digest;

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
}
