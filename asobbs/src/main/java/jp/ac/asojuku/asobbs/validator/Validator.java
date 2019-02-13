package jp.ac.asojuku.asobbs.validator;

import jp.ac.asojuku.asobbs.err.ErrorCode;

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
}
