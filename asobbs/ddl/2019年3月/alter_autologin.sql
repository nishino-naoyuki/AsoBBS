CREATE TABLE autologin_tbl
(
	autologin_id int NOT NULL AUTO_INCREMENT,
	-- ユーザーid
	user_id int NOT NULL COMMENT 'ユーザーid',
	-- 16文字のトークン
	token varchar(40) NOT NULL COMMENT '32文字のトークン',
	-- トークンの有効期限
	-- 発行日時ではないことに注意
	lmit_date datetime NOT NULL COMMENT 'トークンの有効期限
発行日時ではないことに注意',
	PRIMARY KEY (autologin_id)
);

ALTER TABLE autologin_tbl
	ADD FOREIGN KEY (user_id)
	REFERENCES user_tbl (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

CREATE INDEX IDX_AUTOLOGIN_TOKEN ON autologin_tbl (token ASC);