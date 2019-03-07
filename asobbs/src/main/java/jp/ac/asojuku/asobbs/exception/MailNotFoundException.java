package jp.ac.asojuku.asobbs.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailNotFoundException extends Exception {

	Logger logger = LoggerFactory.getLogger(MailNotFoundException.class);

	public MailNotFoundException(String errMsg){
		logger.error("不正メールアドレスエラー："+errMsg);
	}
	public MailNotFoundException(Exception e) {
		logger.error("不正メールアドレスエラー：",e);
	}
}
