package jp.ac.asojuku.asobbs.config;

import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;

/**
 * バリデーション用のメッセージクラス
 * @author nishino
 *
 */
public class ValidationConfig extends ConfigBase {

	public static final String ERR_PROP_PREFIX = "errmsg";

	public ValidationConfig() throws AsoBbsSystemErrException {
		super();
	}

	//シングルトン
	private static ValidationConfig prop = null;
	private static final String CONFIG_NAME = "ValidationMessages.properties";

	/**
	 * インスタンスの取得
	 * @return
	 * @throws BookStoreSystemErrorException
	 */
	public static ValidationConfig getInstance() throws AsoBbsSystemErrException{

		if( prop == null ){
			prop = new ValidationConfig();
		}

		return prop;
	}

	@Override
	protected String getConfigName() {
		return CONFIG_NAME;
	}

	/**
	 * エラーコードよりエラーメッセージを取得する
	 * @param code
	 * @return
	 */
	public String getMsg(ErrorCode code){

		return getProperty(ERR_PROP_PREFIX+code.getCode());
	}
}
