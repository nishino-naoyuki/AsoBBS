package jp.ac.asojuku.asobbs.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChatDto {
	
	private Integer myUserId;
	private Integer targetUserId;
	private String myUserName;
	private String targetUserName;
	private List<ChatMessage> messageList = new ArrayList<ChatMessage>();
	
	public void addMessageList(ChatMessage chatMessage) {
		messageList.add(chatMessage);
	}
}
