package jp.ac.asojuku.asobbs.param;

public enum RoomRoleId {

	ADMIN(0,"管理者"),
	USER(1,"閲覧者");

	//ステータス
	private int id;
	private String msg;

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return msg1
	 */
	public String getMsg() {
		return msg;
	}


	private RoomRoleId(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	public boolean equals(Integer id){
		if(id == null){
			return false;
		}

		return (this.id == id);
	}
}
