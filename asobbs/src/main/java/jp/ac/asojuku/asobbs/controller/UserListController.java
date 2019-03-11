package jp.ac.asojuku.asobbs.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.asobbs.dto.UserListDto;
import jp.ac.asojuku.asobbs.service.UserService;

@RestController
public class UserListController {

	@Autowired
	UserService userService;
	
	/**
	 * ユーザーのリストを取得する
	 * 
	 * @return
	 */
	@RequestMapping("/getaccountlist")
    public List<UserListDto> getuserlist() {
        return userService.getList();
    }

	/**
	 * ユーザーを検索する
	 * 
	 * @param course_id
	 * @param grade
	 * @param mail
	 * @param nickname
	 * @return
	 */
	@RequestMapping("room/searchUserSelect")
    public List<UserListDto> searchUser(
    		@RequestParam("course_id")String course_id, 
    		@RequestParam("grade")String grade, 
    		@RequestParam("mail")String mail, 
    		@RequestParam("nickname")String nickname
    		) {
		
		
        return userService.getList(
        			mail,
        			(StringUtils.isEmpty(course_id) ? null : Integer.parseInt(course_id)),
        			nickname,
        			(StringUtils.isEmpty(grade) ? null : Integer.parseInt(grade))
        		);
    }
}
