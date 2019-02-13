package jp.ac.asojuku.asobbs.validator;

import java.util.List;

import org.springframework.util.StringUtils;

import jp.ac.asojuku.asobbs.config.AppSettingProperty;
import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.param.RoleId;

/**
 * ユーザー情報に関するバリデーションクラス
 * @author nishino
 *
 */
public class UserValidator extends Validator{

	/**
	 * ユーザー名のチェック
	 * 今のところ文字種は何でもOKとする
	 *
	 * @param name
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void useName(String name,ActionErrors errors) throws AsoBbsSystemErrException{

		//必須
		if( StringUtils.isEmpty(name) ){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_NAME_ISNEED);
		}
		//最大文字数
		if( name != null && name.length() > 100){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_NAME);
		}
	}

	/**
	 * ニックネーム
	 *
	 * @param name
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void useNickName(String nickname,ActionErrors errors) throws AsoBbsSystemErrException{

		//必須
		if( StringUtils.isEmpty(nickname) ){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_NICKNAME_ISNEED);
		}
		//最大文字数
		if( nickname != null && nickname.length() > 100){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_NICKNAME);
		}
	}
	/**
	 * メールアドレスのチェック
	 *
	 * @param mailAddress
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void mailAddress(String mailAddress,ActionErrors errors) throws AsoBbsSystemErrException{

		//必須
		if( StringUtils.isEmpty(mailAddress) ){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS_ISNEED);
		}
		if (!chkMailFormat(mailAddress)) {
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
		}
		//最大文字数
		if( mailAddress != null && mailAddress.length() > 256){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
		}
	}

	/**
	 * コースＩＤのチェック
	 * @param couseId
	 * @param list
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void courseId(String couseId,List<CourseDto> list,ActionErrors errors ) throws AsoBbsSystemErrException{

		int intCourseId;
		try{
			intCourseId = Integer.parseInt(couseId);

			boolean find = false;

			for(CourseDto dto : list ){
				if( dto.getCourseId()== intCourseId){
					find = true;
					break;
				}
			}

			if( !find ){
				errors.add(ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
			}

		}catch(NumberFormatException e){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
		}
	}

	/**
	 * 入学年度
	 * @param admissionYear
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void admissionYear(String admissionYear,ActionErrors errors) throws AsoBbsSystemErrException{

		try{
			//数値チェック
			Integer.parseInt(admissionYear);

			//4桁以下の場合は、西暦じゃないと判断しエラーとする
			if( admissionYear != null && admissionYear.length()  < 4){
				errors.add(ErrorCode.ERR_MEMBER_ENTRY_ADMISSIONYEAR_ERR);
			}

		}catch(NumberFormatException e){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_ADMISSIONYEAR);
		}
	}

	/**
	 * 卒業年度
	 * @param graduateYear
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void graduateYear(String graduateYear,ActionErrors errors) throws AsoBbsSystemErrException{

		try{
			//数値チェック
			Integer.parseInt(graduateYear);

			//4桁以下の場合は、西暦じゃないと判断しエラーとする
			if( graduateYear != null && graduateYear.length()  < 4){
				errors.add(ErrorCode.ERR_MEMBER_ENTRY_GRADUATE_ERR);
			}

		}catch(NumberFormatException e){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_GRADUATEYEAR);
		}
	}

	/**
	 * 退学年度
	 * @param giveupYear
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void gibeupYear(String giveupYear,ActionErrors errors) throws AsoBbsSystemErrException{

		try{
			//数値チェック
			Integer.parseInt(giveupYear);

			//4桁以下の場合は、西暦じゃないと判断しエラーとする
			if(giveupYear != null && giveupYear.length() < 4){
				errors.add(ErrorCode.ERR_MEMBER_ENTRY_GIVEUP_ERR);
			}

		}catch(NumberFormatException e){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_GIVEUPYEAR);
		}
	}

	/**
	 * ロールＩＤ
	 * @param roleId
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void roleId(String roleId,ActionErrors errors) throws AsoBbsSystemErrException{

		int introleId;
		try{
			introleId = Integer.parseInt(roleId);

			if( !RoleId.check(introleId) ){
				errors.add(ErrorCode.ERR_MEMBER_ENTRY_ROLEID_ERR);
			}

		}catch(NumberFormatException e){
			errors.add(ErrorCode.ERR_MEMBER_ENTRY_ROLEID_ERR);
		}
	}

	/**
	 * パスワード
	 * @param password
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void password(String password,ActionErrors errors) throws AsoBbsSystemErrException{
		String policy = AppSettingProperty.getInstance().getPasswordPolicy();

		if( !StringUtils.isEmpty(policy)){
			if(!password.matches(policy)){
				errors.add(ErrorCode.ERR_MEMBER_ENTRY_PASSWORD_POLICY);
			}

		}
	}
}
