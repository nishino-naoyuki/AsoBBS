package jp.ac.asojuku.asobbs.form;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RoomInputForm implements Serializable {
	
	@NotEmpty(message = "{errmsg0201}")
	@Size(max = 30, message="{errmsg0202}")
	private String roomName;
	
	@NotEmpty(message = "{errmsg0203}")
	private String roomAdmins;
	
	private String roomUsers;
	
	private Boolean allUserFlg;
	
	
}
