package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.asobbs.dto.RoomListDto;
import jp.ac.asojuku.asobbs.form.RoomSearchForm;
import jp.ac.asojuku.asobbs.service.RoomService;

@RestController
public class RoomListController {
	
	@Autowired
	RoomService roomService;

	@RequestMapping("/getroomlist")
    public List<RoomListDto> getList(RoomSearchForm form) {
        return roomService.getListBy(form);
    }
}
