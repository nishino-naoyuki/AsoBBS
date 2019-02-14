package jp.ac.asojuku.asobbs.validator;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import jp.ac.asojuku.asobbs.config.MessageProperty;
import jp.ac.asojuku.asobbs.config.ValidationConfig;
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
	public static void roomAdmin(String adminCSVList,BindingResult error) throws AsoBbsSystemErrException{
		//必須
//		if( StringUtils.isEmpty(adminCSVList) ){
//			error.rejectValue("roomAdmins",
//					ValidationConfig.ERR_PROP_PREFIX+ErrorCode.ERR_ROOM_ADMIN_ISNEED.getCode(),
//					ValidationConfig.getInstance().getMsg(ErrorCode.ERR_ROOM_ADMIN_ISNEED));
//		}
		
		//形式チェック
		String[] admins = adminCSVList.split(",",0);
		for( String mailAddress : admins) {
			//メアド形式チェック
			if (!chkMailFormat(mailAddress)) {
				error.rejectValue("roomAdmins",
						ValidationConfig.ERR_PROP_PREFIX+ErrorCode.ERR_ROOM_ADMIN_FMT_ERROR.getCode(),
						ValidationConfig.getInstance().getMsg(ErrorCode.ERR_ROOM_ADMIN_FMT_ERROR));
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
	public static void roomUser(String userCSVList,BindingResult error) throws AsoBbsSystemErrException{
				//必須
//				if( StringUtils.isEmpty(userCSVList) ){
//					error.reject(ErrorCode.ERR_ROOM_BELONG_ISNEED.getCode());
//					error.rejectValue("roomUsers",
//							ValidationConfig.ERR_PROP_PREFIX+ErrorCode.ERR_ROOM_BELONG_ISNEED.getCode(),
//							ValidationConfig.getInstance().getMsg(ErrorCode.ERR_ROOM_BELONG_ISNEED));
//				}
				
				//形式チェック
				String[] users = userCSVList.split(",",0);
				for( String mailAddress : users) {
					//メアド形式チェック
					if (!chkMailFormat(mailAddress)) {
						error.rejectValue("roomUsers",
								ValidationConfig.ERR_PROP_PREFIX+ErrorCode.ERR_ROOM_BELONG_FMT_ERROR.getCode(),
								ValidationConfig.getInstance().getMsg(ErrorCode.ERR_ROOM_BELONG_FMT_ERROR));
						break;
					}
				}
	}
}
