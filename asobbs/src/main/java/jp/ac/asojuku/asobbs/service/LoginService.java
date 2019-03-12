package jp.ac.asojuku.asobbs.service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.asobbs.config.AppSettingProperty;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.entity.AutologinTblEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.param.RoleId;
import jp.ac.asojuku.asobbs.repository.AutoLoginRepository;
import jp.ac.asojuku.asobbs.repository.UserRepository;
import jp.ac.asojuku.asobbs.util.Digest;
import jp.ac.asojuku.asobbs.util.Token;

import static jp.ac.asojuku.asobbs.repository.UserSpecifications.*;

@Service
public class LoginService {

	@Autowired 
    private UserRepository userRepository;
	
	@Autowired
	private AutoLoginRepository autoLoginRepository;
	
	
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
	
	/**
	 * トークンを発行してDBに保存する
	 * 
	 * @param userId
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	public String createLoginToken(Integer userId) throws AsoBbsSystemErrException {
		//トークンの発行
		String token = Token.getCsrfToken();
		//有効期限の設定
		int addDays = AppSettingProperty.getInstance().getTokenLimitDayString();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, addDays);
		
		//DBに保存
		AutologinTblEntity autologinTblEntity = new AutologinTblEntity();
		
		autologinTblEntity.setToken(token);
		autologinTblEntity.setLmitDate(cal.getTime());
		autologinTblEntity.setUserId(userId);
		
		autoLoginRepository.save(autologinTblEntity);
		
		return token;
		
	}
	
	/**
	 * トークンを認証する
	 * @param token
	 * @return
	 */
	public LoginInfoDto authToken(String token) {
		LoginInfoDto dto = null;
		
		AutologinTblEntity autologinTblEntity = autoLoginRepository.getTokenInfo(token);
		
		if(autologinTblEntity != null && autologinTblEntity.getUserTbl() != null) {
			UserTblEntity entity = autologinTblEntity.getUserTbl();

			dto = new LoginInfoDto();
			
			dto.setUserId(entity.getUserId());
			dto.setMail(entity.getMailadress());
			dto.setGrade(entity.getGrade());
			dto.setNickName(entity.getNickName());
			dto.setRole( RoleId.search(entity.getRoleMaster().getRoleId()) );
			dto.setRoleName(entity.getRoleMaster().getRoleName());
			dto.setCourseName( entity.getCourseMaster().getCourseName() );
			dto.setGrade(entity.getGrade());
			
			//いったん個のトークンは削除（新しいトークンを発行する）
			autoLoginRepository.delete(autologinTblEntity);
		}
		
		//有効期限切れのトークンを削除する
		List<AutologinTblEntity> lmitList = autoLoginRepository.getLimitTokenInfo();
		if( lmitList.size() > 0 ) {
			autoLoginRepository.deleteAll(lmitList);
		}
		
		return dto;
	}
	
	/**
	 * ログアウト処理
	 * 
	 * @param token
	 */
	public void logout(String token) {

		AutologinTblEntity autologinTblEntity = autoLoginRepository.getTokenInfo(token);
		if( autologinTblEntity != null ) {
			autoLoginRepository.delete(autologinTblEntity);
		}
	}

}
