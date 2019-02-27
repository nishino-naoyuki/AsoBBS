package jp.ac.asojuku.asobbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.asobbs.dto.ChatDto;
import jp.ac.asojuku.asobbs.dto.ChatMessage;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.entity.ChatTableEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.form.ChatInputForm;
import jp.ac.asojuku.asobbs.param.StringConst;
import jp.ac.asojuku.asobbs.repository.ChatRepository;
import jp.ac.asojuku.asobbs.repository.UserRepository;
import jp.ac.asojuku.asobbs.util.DateUtil;

@Service
public class ChatService {
	@Autowired
	ChatRepository chatRepository;
	@Autowired
	UserRepository userRepository;
	
	public ChatDto getList(Integer myUserId,Integer targetUserId) {
		
		List<ChatTableEntity> list = chatRepository.getList(myUserId, targetUserId);
		
		ChatDto chatDto = getChatDtoFrom(list,myUserId,targetUserId);
		
		return chatDto;
	}
	
	public void inset(ChatInputForm form,LoginInfoDto loginInfo) {
		//form -> Entity
		ChatTableEntity entity = new ChatTableEntity();
		
		entity.setFromUserId(loginInfo.getUserId());
		entity.setToUserId(form.getUserId());
		entity.setMsg(form.getComment());
		
		chatRepository.save(entity);
	}
	
	private ChatDto getChatDtoFrom(List<ChatTableEntity> list,Integer myUserId,Integer targetUserId ) {
		ChatDto chatDto = new ChatDto();
		
		UserTblEntity myUserTbl = null;
		UserTblEntity targetUserTbl = null;
		//ユーザーデータを取得
		myUserTbl = userRepository.getOne(myUserId);
		targetUserTbl = userRepository.getOne(targetUserId);

		if( myUserTbl != null ) {
			chatDto.setMyUserId(myUserTbl.getUserId());
			chatDto.setMyUserName(myUserTbl.getNickName());
		}
		if( targetUserTbl != null ) {
			chatDto.setTargetUserId(targetUserTbl.getUserId());
			chatDto.setTargetUserName(targetUserTbl.getNickName());
		}
		
		//チャットデータを取得
		for(ChatTableEntity chatTableEntity : list) {			
			ChatMessage message = new ChatMessage();
			
			message.setMessage( chatTableEntity.getMsg() );
			message.setSendUserId( chatTableEntity.getFromUserId() );
			message.setToUserId( chatTableEntity.getToUserId() );
			message.setDate( DateUtil.formattedDate(chatTableEntity.getRegsterDatetime(), StringConst.DATE_FMT) );
			
			chatDto.addMessageList(message);
		}
		
		
		return chatDto;
	}
	
}
