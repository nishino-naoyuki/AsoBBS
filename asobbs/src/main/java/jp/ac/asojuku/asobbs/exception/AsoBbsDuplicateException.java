package jp.ac.asojuku.asobbs.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 二重登録例外
 * 
 * @author nishino
 *
 */
public class AsoBbsDuplicateException extends Exception {

	Logger logger = LoggerFactory.getLogger(AsoBbsDuplicateException.class);

	public AsoBbsDuplicateException(String errMsg){
		logger.error("二重登録エラー：",errMsg);
	}
	public AsoBbsDuplicateException(Exception e) {
		logger.error("二重登録エラー：",e);
	}
}
