package jp.ac.asojuku.asobbs.dto;

import jp.ac.asojuku.asobbs.util.HtmlUtil;
import lombok.Data;

@Data
public class ChatMessage {
	private String message;
	private String date;
	private Integer sendUserId;
	private Integer toUserId;

	public String getMessage() {
		return HtmlUtil.nl2be(message);
	}
}
