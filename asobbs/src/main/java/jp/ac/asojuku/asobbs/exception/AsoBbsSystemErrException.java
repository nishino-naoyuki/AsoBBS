/**
 *
 */
package jp.ac.asojuku.asobbs.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nishino
 *
 */
public class AsoBbsSystemErrException extends Exception {
	Logger logger = LoggerFactory.getLogger(AsoBbsSystemErrException.class);

	public AsoBbsSystemErrException(String errMsg){
		logger.error("致命的エラー：",errMsg);
	}
	public AsoBbsSystemErrException(Exception e) {
		logger.error("致命的エラー：",e);
	}

}
