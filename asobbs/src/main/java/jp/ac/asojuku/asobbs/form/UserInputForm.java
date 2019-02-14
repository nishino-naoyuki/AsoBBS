package jp.ac.asojuku.asobbs.form;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.Data;

@Data
public class UserInputForm implements Serializable {
	
	private String role;
	
	@NotEmpty(message = "{errmsg0106}")
	private String studentNo;
	
	@NotEmpty(message = "{errmsg0105}")
	@Size(max = 256, message = "{errmsg0101}")
	private String mailadress;
	
	@NotEmpty(message = "{errmsg0112}")
	@Size(max = 100, message = "{errmsg0113}")
	private String nickname;
	
	private String course_id;
	
	private String password1;
	
	private String password2;
	
	private String admission_year;
}
