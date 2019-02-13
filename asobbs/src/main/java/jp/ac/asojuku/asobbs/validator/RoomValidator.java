package jp.ac.asojuku.asobbs.validator;

import org.springframework.util.StringUtils;

import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;

/**
 * ルーム情報のチェック処理
 * @author nishino
 *
 */
public class RoomValidator extends Validator {

	/**
	 * ルーム名のチェック
	 * @param name
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void roomName(String name,ActionErrors errors) throws AsoBbsSystemErrException{
		//必須
		if( StringUtils.isEmpty(name) ){
			errors.add(ErrorCode.ERR_ROOM_NAME_ISNEED);
		}
		//最大文字数
		if( name != null && name.length() > 100){
			errors.add(ErrorCode.ERR_ROOM_NAME_ERROR);
		}
	}
	
	/**
	 * ルーム管理者チェック
	 * 
	 * @param adminCSVList
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void roomAdmin(String adminCSVList,ActionErrors errors) throws AsoBbsSystemErrException{
		//必須
		if( StringUtils.isEmpty(adminCSVList) ){
			errors.add(ErrorCode.ERR_ROOM_ADMIN_ISNEED);
		}
		
		//形式チェック
		String[] admins = adminCSVList.split(",",0);
		for( String mailAddress : admins) {
			//メアド形式チェック
			if (!chkMailFormat(mailAddress)) {
				errors.add(ErrorCode.ERR_ROOM_ADMIN_FMT_ERROR);
				break;
			}
		}
	}
	
	/**
	 * ルーム所属者チェック
	 * 
	 * @param userCSVList
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void roomUser(String userCSVList,ActionErrors errors) throws AsoBbsSystemErrException{
				//必須
				if( StringUtils.isEmpty(userCSVList) ){
					errors.add(ErrorCode.ERR_ROOM_BELONG_ISNEED);
				}
				
				//形式チェック
				String[] users = userCSVList.split(",",0);
				for( String mailAddress : users) {
					//メアド形式チェック
					if (!chkMailFormat(mailAddress)) {
						errors.add(ErrorCode.ERR_ROOM_BELONG_FMT_ERROR);
						break;
					}
				}
	}
}
