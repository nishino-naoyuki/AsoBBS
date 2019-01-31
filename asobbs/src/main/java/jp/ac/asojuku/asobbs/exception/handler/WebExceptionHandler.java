package jp.ac.asojuku.asobbs.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.filter.LoginCheckFilter;

@ControllerAdvice
public class WebExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(LoginCheckFilter.class);
	
	@ExceptionHandler(AsoBbsSystemErrException.class)
	public String handleException(Exception exception) {
		logger.error("WebExceptionHandler", exception);

		return "redirect:/error/systemerror";	// error1.htmlへ遷移
	}
}
