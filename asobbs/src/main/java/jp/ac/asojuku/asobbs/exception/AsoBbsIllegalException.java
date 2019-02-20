package jp.ac.asojuku.asobbs.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsoBbsIllegalException extends Exception {
	Logger logger = LoggerFactory.getLogger(AsoBbsIllegalException.class);

	public AsoBbsIllegalException(String errMsg){
		logger.error("不正操作エラー：",errMsg);
	}
	public AsoBbsIllegalException(Exception e) {
		logger.error("不正操作エラー：",e);
	}
}
