package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.asobbs.dto.UserListDto;
import jp.ac.asojuku.asobbs.service.UserService;

@RestController
public class UserListController {

	@Autowired
	UserService userService;
	
	@RequestMapping("/getaccountlist")
    public List<UserListDto> getuserlist() {
        return userService.getList();
    }
}
