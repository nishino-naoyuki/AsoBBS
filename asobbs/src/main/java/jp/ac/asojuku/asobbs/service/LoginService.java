package jp.ac.asojuku.asobbs.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.param.RoleId;
import jp.ac.asojuku.asobbs.repository.UserRepository;
import jp.ac.asojuku.asobbs.util.Digest;

import static jp.ac.asojuku.asobbs.repository.UserSpecifications.*;

@Service
public class LoginService {

	@Autowired 
    private UserRepository userRepository;
	
	
	/**
	 * ログイン処理を行う
	 * 
	 * @param mail
	 * @param password
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	public LoginInfoDto login(String mail,String password) throws AsoBbsSystemErrException {
		LoginInfoDto dto = null;
		
		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(mail, password);
		
		//ログインユーザー検索
		UserTblEntity entity = userRepository.getUser(mail, hashedPwd);
		
		if( entity!= null ) {
			//entity -> dto
			dto = new LoginInfoDto();
			
			dto.setUserId(entity.getUserId());
			dto.setMail(entity.getMailadress());
			dto.setGrade(entity.getGrade());
			dto.setNickName(entity.getNickName());
			dto.setRole( RoleId.search(entity.getRoleMaster().getRoleId()) );
			dto.setRoleName(entity.getRoleMaster().getRoleName());
			dto.setCourseName( entity.getCourseMaster().getCourseName() );
			dto.setGrade(entity.getGrade());
		}
		
		//TODO:パスワードの有効期限チェック
		//TODO:ログイン失敗回数チェック
		//TODO:履歴の出力
		
		return dto;
	}
	

}
