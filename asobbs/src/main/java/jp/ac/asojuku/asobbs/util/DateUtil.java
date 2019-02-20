package jp.ac.asojuku.asobbs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;


public class DateUtil {

	public static String formattedDate(Date date, String timeFormat) {
		if( date == null ){
			return "";
		}
        return new SimpleDateFormat(timeFormat).format(date);
    }

	public static Date plusDay(Date srcDate,int day){
		Calendar cal = Calendar.getInstance();

		cal.setTime(srcDate);

		cal.add(Calendar.DATE, day);

		return cal.getTime();
	}

	/**
	 * 日付の文字列からDateを取得
	 *
	 * @param str
	 * @param format
	 * @return
	 * @throws AsoLearningSystemErrException
	 */
	public static Date getDateFrom(String str,String format) throws AsoBbsSystemErrException{

		if( StringUtils.isEmpty(str) ){
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date fdate = null;

		try {
			fdate = sdf.parse(str);
		} catch (ParseException e) {
			fdate = null;
		}

		return fdate;
	}

}
