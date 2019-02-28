package jp.ac.asojuku.asobbs.validator;

import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

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
	public static void useName(String name,BindingResult err) throws AsoBbsSystemErrException{

		//必須
		if( StringUtils.isEmpty(name) ){
			setErrorcode("nickname",err,ErrorCode.ERR_MEMBER_ENTRY_NAME_ISNEED);
		}
		//最大文字数
		if( name != null && name.length() > 100){
			setErrorcode("nickname",err,ErrorCode.ERR_MEMBER_ENTRY_NAME);
		}
	}

	/**
	 * ニックネーム
	 *
	 * @param name
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void useNickName(String nickname,BindingResult err) throws AsoBbsSystemErrException{

		//必須
		if( StringUtils.isEmpty(nickname) ){
			setErrorcode("nickname",err,ErrorCode.ERR_MEMBER_ENTRY_NICKNAME_ISNEED);
		}
		//最大文字数
		if( nickname != null && nickname.length() > 100){
			setErrorcode("nickname",err,ErrorCode.ERR_MEMBER_ENTRY_NICKNAME);
		}
	}
	/**
	 * メールアドレスのチェック
	 *
	 * @param mailAddress
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void mailAddress(String mailAddress,BindingResult err) throws AsoBbsSystemErrException{

		//必須
		if( StringUtils.isEmpty(mailAddress) ){
			setErrorcode("mailadress",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS_ISNEED);
		}
		if (mailAddress != null && !chkMailFormat(mailAddress)) {
			setErrorcode("mailadress",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
		}
		//最大文字数
		if( mailAddress != null && mailAddress.length() > 256){
			setErrorcode("mailadress",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
		}
	}

	/**
	 * コースＩＤのチェック
	 * @param couseId
	 * @param list
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void courseId(String couseId,List<CourseDto> list,BindingResult err ) throws AsoBbsSystemErrException{

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
				setErrorcode("course_id",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
			}

		}catch(NumberFormatException e){
			setErrorcode("course_id",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
		}
	}

	/**
	 * 入学年度
	 * @param admissionYear
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void admissionYear(String admissionYear,BindingResult err) throws AsoBbsSystemErrException{

		try{
			//数値チェック
			Integer.parseInt(admissionYear);

			//4桁以下の場合は、西暦じゃないと判断しエラーとする
			if( admissionYear != null && admissionYear.length()  < 4){
				setErrorcode("admission_year",err,ErrorCode.ERR_MEMBER_ENTRY_ADMISSIONYEAR_ERR);
			}

		}catch(NumberFormatException e){
			setErrorcode("admission_year",err,ErrorCode.ERR_MEMBER_ENTRY_ADMISSIONYEAR);
		}
	}

	/**
	 * 卒業年度
	 * @param graduateYear
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void graduateYear(String graduateYear,BindingResult err) throws AsoBbsSystemErrException{

		try{
			//数値チェック
			Integer.parseInt(graduateYear);

			//4桁以下の場合は、西暦じゃないと判断しエラーとする
			if( graduateYear != null && graduateYear.length()  < 4){
				setErrorcode("admission_year",err,ErrorCode.ERR_MEMBER_ENTRY_GRADUATE_ERR);
			}

		}catch(NumberFormatException e){
			setErrorcode("admission_year",err,ErrorCode.ERR_MEMBER_ENTRY_GRADUATEYEAR);
		}
	}

	/**
	 * 退学年度
	 * @param giveupYear
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void gibeupYear(String giveupYear,BindingResult err) throws AsoBbsSystemErrException{

		try{
			//数値チェック
			Integer.parseInt(giveupYear);

			//4桁以下の場合は、西暦じゃないと判断しエラーとする
			if(giveupYear != null && giveupYear.length() < 4){
				setErrorcode("admission_year",err,ErrorCode.ERR_MEMBER_ENTRY_GIVEUP_ERR);
			}

		}catch(NumberFormatException e){
			setErrorcode("admission_year",err,ErrorCode.ERR_MEMBER_ENTRY_GIVEUPYEAR);
		}
	}

	/**
	 * ロールＩＤ
	 * @param roleId
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void roleId(String roleId,BindingResult err) throws AsoBbsSystemErrException{

		int introleId;
		try{
			introleId = Integer.parseInt(roleId);

			if( !RoleId.check(introleId) ){
				setErrorcode("role",err,ErrorCode.ERR_MEMBER_ENTRY_ROLEID_ERR);
			}

		}catch(NumberFormatException e){
			setErrorcode("role",err,ErrorCode.ERR_MEMBER_ENTRY_ROLEID_ERR);
		}
	}

	/**
	 * パスワード
	 * @param password
	 * @param errors
	 * @throws AsoBbsSystemErrException
	 */
	public static void password(String password,BindingResult err) throws AsoBbsSystemErrException{
		String policy = AppSettingProperty.getInstance().getPasswordPolicy();

		if( !StringUtils.isEmpty(policy)){
			if(!password.matches(policy)){
				setErrorcode("password1",err,ErrorCode.ERR_MEMBER_ENTRY_PASSWORD_POLICY);
			}

		}
	}
}
