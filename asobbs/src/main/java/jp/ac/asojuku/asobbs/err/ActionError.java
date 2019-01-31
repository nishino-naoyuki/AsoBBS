package jp.ac.asojuku.asobbs.err;

import lombok.Data;

@Data
public class ActionError {

	private ErrorCode code;
	private String message;

	public ActionError(ErrorCode code ,String message){
		this.code = code;
		this.message = message;
	}


}
