package jp.ac.asojuku.asobbs.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserInputCSVForm {
	private MultipartFile uploadFile;
}
