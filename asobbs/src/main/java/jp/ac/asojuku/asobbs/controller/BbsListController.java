package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.asobbs.dto.BbsListDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.dto.RoomListDto;
import jp.ac.asojuku.asobbs.form.RoomSearchForm;
import jp.ac.asojuku.asobbs.param.SessionConst;
import jp.ac.asojuku.asobbs.service.BbsService;

@RestController
@RequestMapping("bbslist")
public class BbsListController {
	
	@Autowired
	BbsService bbsService;

	@RequestMapping("/list")
    public List<BbsListDto> getList(@ModelAttribute("id")Integer id,@ModelAttribute("roomId")Integer roomId) {
		
        return bbsService.getBbsListDto(id,roomId);
    }
}
