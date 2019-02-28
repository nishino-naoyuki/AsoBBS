package jp.ac.asojuku.asobbs.controller;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.filter.LoginCheckFilter;

@ControllerAdvice
public class WebExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(LoginCheckFilter.class);
	
	/**
	 * システムエラー画面
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(AsoBbsSystemErrException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception exception) {
		logger.error("AsoBbsSystemErrException", exception);

		return "/error/systemerror";	// error1.htmlへ遷移
	}

	/**
	 * 404エラー
	 * @param exception
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String notFound(Exception exception) {
		logger.error("NOT_FOUND", exception);

		return "/error/404";	// error1.htmlへ遷移
	}
	
	/**
	 * 例外全般
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(Throwable.class)
    public String ThrowableHandler(Exception exception) {
		logger.error("ThrowableHandler", exception);
		return "/error/systemerror";	// error1.htmlへ遷移
    }
	

	/**
	 * ファイルサイズ
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(FileSizeLimitExceededException.class)
	public String fileSizeException(Exception exception) {
		logger.error("fileSizeException", exception);

		return "/error/systemerror";	// error1.htmlへ遷移
	}
}
