DROP TABLE IF EXISTS bookmark_tbl;

CREATE TABLE bookmark_tbl
(
	bookmark_id int NOT NULL AUTO_INCREMENT,
	-- ユーザーid
	user_id int NOT NULL COMMENT 'ユーザーid',
	bbs_id int NOT NULL,
	create_date_time datetime NOT NULL,
	PRIMARY KEY (bookmark_id)
);


ALTER TABLE bookmark_tbl
	ADD FOREIGN KEY (bbs_id)
	REFERENCES bbs_tbl (bbs_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE bookmark_tbl
	ADD FOREIGN KEY (user_id)
	REFERENCES user_tbl (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;