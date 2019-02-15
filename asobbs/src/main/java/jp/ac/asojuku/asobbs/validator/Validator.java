package jp.ac.asojuku.asobbs.validator;

import org.springframework.validation.BindingResult;

import jp.ac.asojuku.asobbs.config.ValidationConfig;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;

public class Validator {
	protected static String mailFormat = "^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$";

	/**
	 * メールアドレスの形式チェック
	 * 
	 * @param mail
	 * @return
	 */
	protected static boolean chkMailFormat(String mail) {

		if (!mail.matches(mailFormat)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * エラーコードをセットする
	 * 
	 * @param columnName
	 * @param error
	 * @param errCode
	 * @throws AsoBbsSystemErrException
	 */
	protected static void setErrorcode(String columnName,BindingResult error,ErrorCode errCode) throws AsoBbsSystemErrException {
		error.rejectValue(columnName,
				ValidationConfig.ERR_PROP_PREFIX+errCode.getCode(),
				ValidationConfig.getInstance().getMsg(errCode));
	}
}
