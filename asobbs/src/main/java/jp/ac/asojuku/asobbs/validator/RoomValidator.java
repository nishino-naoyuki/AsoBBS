package jp.ac.asojuku.asobbs.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import jp.ac.asojuku.asobbs.config.MessageProperty;
import jp.ac.asojuku.asobbs.config.ValidationConfig;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.service.RoomService;

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
	public static void roomName(String name,RoomService roomService,BindingResult error) throws AsoBbsSystemErrException{
		//必須
		if( StringUtils.isEmpty(name) ){
			setErrorcode("roomUsers",error,ErrorCode.ERR_ROOM_NAME_ISNEED);
		}
		//最大文字数
		if( name != null && name.length() > 100){
			setErrorcode("roomUsers",error,ErrorCode.ERR_ROOM_NAME_ERROR);
		}
		//ダブリチェック
		if( roomService.getBy(name) != null ) {
			setErrorcode("roomUsers",error,ErrorCode.ERR_ROOM_DUPLICATE_ROOMNAME);
		}
	}
	
	/**
	 * ルーム管理者チェック
	 * 
	 * @param adminCSVList
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void roomAdmin(String adminCSVList,RoomService roomService,BindingResult error) throws AsoBbsSystemErrException{
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
				setErrorcode("roomAdmins",error,ErrorCode.ERR_ROOM_ADMIN_FMT_ERROR);
				break;
			}
			//存在チェック
			if( !roomService.isExistsMailaddress(mailAddress) ) {
				setErrorcode("roomAdmins",error,ErrorCode.ERR_ROOM_ADMIN_MAIL_NOT_FOUND);
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
	public static void roomUser(String userCSVList,RoomService roomService,BindingResult error) throws AsoBbsSystemErrException{
				//必須
				if( StringUtils.isEmpty(userCSVList) ){
					setErrorcode("roomUsers",error,ErrorCode.ERR_ROOM_BELONG_ISNEED);
				}
				
				//形式チェック
				String[] users = userCSVList.split(",",0);
				for( String mailAddress : users) {
					//メアド形式チェック
					if (!chkMailFormat(mailAddress)) {
						setErrorcode("roomUsers",error,ErrorCode.ERR_ROOM_BELONG_FMT_ERROR);
						break;
					}
					//存在チェック
					if( !roomService.isExistsMailaddress(mailAddress) ) {
						setErrorcode("roomUsers",error,ErrorCode.ERR_ROOM_USER_MAIL_NOT_FOUND);
						break;				
					}
				}
	}
	
}
