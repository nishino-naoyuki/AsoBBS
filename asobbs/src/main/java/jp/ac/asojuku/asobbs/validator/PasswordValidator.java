package jp.ac.asojuku.asobbs.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;

/**
 * パスワードに関するバリデーションクラス
 * @author nishino
 *
 */
public class PasswordValidator extends Validator{

	/**
	 * 入力された新パスワードが一致するか
	 * 
	 * @param pwd1
	 * @param pwd2
	 * @param error
	 * @throws AsoBbsSystemErrException
	 */
	public static void match(String pwd1,String pwd2,BindingResult error) throws AsoBbsSystemErrException {
		if( !StringUtils.equals(pwd1, pwd2)){
			setErrorcode("newPassword1",error,ErrorCode.ERR_PWD_CHG_NOT_MATCH);
		}		
	}
	
}
