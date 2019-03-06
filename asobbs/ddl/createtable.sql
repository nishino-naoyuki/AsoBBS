SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Indexes */

DROP INDEX idX_aTTaCED_FILE_BBSid ON attached_file_tbl;
DROP INDEX INDEX_CaTEGORYtbl_roomid ON category_tbl;
DROP INDEX idX_His_user_id ON history_tbl;
DROP INDEX idX_His_action_id ON history_tbl;
DROP INDEX idX_His_action_date ON history_tbl;
DROP INDEX idX_user_course_id ON user_tbl;
DROP INDEX idX_user_role_id ON user_tbl;
DROP INDEX idX_user_tbl_nickname ON user_tbl;
DROP INDEX idX_user_grade ON user_tbl;
DROP INDEX idX_user_mailadress ON user_tbl;
DROP INDEX INDEX_usertbl_studentno ON user_tbl;



/* Drop Tables */

DROP TABLE IF EXISTS history_tbl;
DROP TABLE IF EXISTS action_master;
DROP TABLE IF EXISTS attached_file_tbl;
DROP TABLE IF EXISTS bbscheck_tbl;
DROP TABLE IF EXISTS bbstbl;
DROP TABLE IF EXISTS category_tbl;
DROP TABLE IF EXISTS chat_table;
DROP TABLE IF EXISTS room_user_tbl;
DROP TABLE IF EXISTS room_tbl;
DROP TABLE IF EXISTS user_tbl;
DROP TABLE IF EXISTS course_master;
DROP TABLE IF EXISTS role_master;




/* Create Tables */

-- 操作マスター
CREATE TABLE action_master
(
	action_id int NOT NULL,
	action_name varchar(200),
	PRIMARY KEY (action_id)
) COMMENT = '操作マスター';


CREATE TABLE attached_file_tbl
(
	attached_file_id int NOT NULL AUTO_INCREMENT,
	bbsid int NOT NULL,
	-- ファイルのフルパス
	file_path varchar(600) NOT NULL COMMENT 'ファイルのフルパス',
	-- 不正防止用に、ファイルをDLさせるときは必ずファイルidとファイルサイズとをマッチングさせる
	file_size bigint NOT NULL COMMENT '不正防止用に、ファイルをDLさせるときは必ずファイルidとファイルサイズとをマッチングさせる',
	PRIMARY KEY (attached_file_id)
);


CREATE TABLE bbscheck_tbl
(
	bbsid int NOT NULL,
	-- ユーザーid
	user_id int NOT NULL COMMENT 'ユーザーid',
	check_date datetime NOT NULL,
	PRIMARY KEY (bbsid, user_id)
);


CREATE TABLE bbstbl
(
	bbsid int NOT NULL AUTO_INCREMENT,
	-- ルーム毎に設定されている掲示板のカテゴリ
	category_id int NOT NULL COMMENT 'ルーム毎に設定されている掲示板のカテゴリ',
	title varchar(100) NOT NULL,
	message varchar(16000) NOT NULL,
	-- 緊急の書き込みの場合はONにする
	-- 1:緊急の書き込み
	-- 0:通常の書き込み
	emergency_flg int DEFAULT 0 NOT NULL COMMENT '緊急の書き込みの場合はONにする
1:緊急の書き込み
0:通常の書き込み',
	-- 返信元のBBSid
	-- おおもとの書き込みの場合はNULLとなる
	parent_bbsid int COMMENT '返信元のBBSid
おおもとの書き込みの場合はNULLとなる',
	-- 返信可能な書き込みかどうかを表す
	reply_ok_flg int NOT NULL COMMENT '返信可能な書き込みかどうかを表す',
	create_date datetime NOT NULL,
	create_user_id int NOT NULL,
	update_date datetime NOT NULL,
	update_user_id int NOT NULL,
	PRIMARY KEY (bbsid)
);


CREATE TABLE category_tbl
(
	-- ルーム毎に設定されている掲示板のカテゴリ
	category_id int NOT NULL AUTO_INCREMENT COMMENT 'ルーム毎に設定されている掲示板のカテゴリ',
	-- カテゴリが属しているルームid
	room_id int NOT NULL COMMENT 'カテゴリが属しているルームid',
	name varchar(100) NOT NULL,
	count int NOT NULL,
	PRIMARY KEY (category_id),
	CONSTRAINT UNIQUE_CaTEGORY_BY_room_Cname UNIQUE (room_id, name)
);


CREATE TABLE chat_table
(
	msg_id int NOT NULL AUTO_INCREMENT,
	-- メッセージ
	msg varchar(1000) NOT NULL COMMENT 'メッセージ',
	regster_datetime datetime NOT NULL,
	-- ユーザーid
	from_user_id int NOT NULL COMMENT 'ユーザーid',
	-- ユーザーid
	to_user_id int NOT NULL COMMENT 'ユーザーid',
	PRIMARY KEY (msg_id)
);


CREATE TABLE course_master
(
	course_id int NOT NULL,
	-- 学科名
	course_name varchar(100) NOT NULL COMMENT '学科名',
	PRIMARY KEY (course_id)
);


-- 操作履歴テーブル
-- 操作履歴のテーブルです
CREATE TABLE history_tbl
(
	history_id int NOT NULL AUTO_INCREMENT,
	-- ユーザーid
	user_id int NOT NULL COMMENT 'ユーザーid',
	action_id int NOT NULL,
	action_date datetime NOT NULL,
	-- 付加情報
	message varchar(2000) COMMENT '付加情報',
	PRIMARY KEY (history_id)
) COMMENT = '操作履歴テーブル
操作履歴のテーブルです';


-- 役割マスタ
-- ロールによる画面へのアクセス権限は、Webアプリの設定ファイルにて行う
-- （いちいちDBにアクセスするとパ
CREATE TABLE role_master
(
	role_id int NOT NULL,
	role_name varchar(100) NOT NULL,
	PRIMARY KEY (role_id)
) COMMENT = '役割マスタ
ロールによる画面へのアクセス権限は、Webアプリの設定ファイルにて行う
（いちいちDBにアクセスするとパ';


CREATE TABLE room_tbl
(
	-- ルームid
	room_id int NOT NULL AUTO_INCREMENT COMMENT 'ルームid',
	-- ルーム名
	name varchar(100) NOT NULL COMMENT 'ルーム名',
	-- 作成日時
	create_date datetime NOT NULL COMMENT '作成日時',
	-- 作成ユーザーid
	create_user_id int NOT NULL COMMENT '作成ユーザーid',
	-- 初回作成時、update_dateとcreate_dateは同じになる
	update_date datetime NOT NULL COMMENT '初回作成時、update_dateとcreate_dateは同じになる',
	-- ユーザーid
	update_user_id int NOT NULL COMMENT 'ユーザーid',
	-- 対象者が全員かどうかのフラグ
	-- ０：対象者は全員ではない
	-- １：対象者は全員
	all_flg int DEFAULT 0 NOT NULL COMMENT '対象者が全員かどうかのフラグ
０：対象者は全員ではない
１：対象者は全員',
	PRIMARY KEY (room_id),
	UNIQUE (name)
);


CREATE TABLE room_user_tbl
(
	-- ルームid
	room_id int NOT NULL COMMENT 'ルームid',
	-- ユーザーid
	user_id int NOT NULL COMMENT 'ユーザーid',
	-- 0:管理者（ルームの編集可能）
	-- 1:閲覧者（ルームの編集不可能）
	room_role int NOT NULL COMMENT '0:管理者（ルームの編集可能）
1:閲覧者（ルームの編集不可能）',
	PRIMARY KEY (room_id, user_id)
);


-- 利用者テーブル
-- ログイン可能な利用者は全てこのテーブルに登録される
CREATE TABLE user_tbl
(
	-- ユーザーid
	user_id int NOT NULL AUTO_INCREMENT COMMENT 'ユーザーid',
	student_no varchar(10) NOT NULL,
	-- メールアドレス（ログインid）
	mailadress varchar(255) NOT NULL COMMENT 'メールアドレス（ログインid）',
	-- パスワードのハッシュ値
	-- ソルト値は設定ファイルから取得
	-- ソルト+パスワード+ソルト
	-- でハッシュ値を計算
	password varchar(255) NOT NULL COMMENT 'パスワードのハッシュ値
ソルト値は設定ファイルから取得
ソルト+パスワード+ソルト
でハッシュ値を計算',
	-- 学生は学籍番号
	-- 職員は職員id
	name varchar(100) NOT NULL COMMENT '学生は学籍番号
職員は職員id',
	-- ニックネーム
	-- ※aESで暗号化する
	-- 鍵は、設定ファイルの設定値+メアド
	nick_name varchar(100) NOT NULL COMMENT 'ニックネーム
※aESで暗号化する
鍵は、設定ファイルの設定値+メアド',
	-- アカウントの有効期限（指定日まで有効）
	-- NULLの場合は無期限
	account_expry_date date COMMENT 'アカウントの有効期限（指定日まで有効）
NULLの場合は無期限',
	-- NULLは無期限
	password_expirydate date COMMENT 'NULLは無期限',
	-- ユーザーの所属学科
	-- roleが「先生」の場合も必要
	-- どこにも所属していない場合は
	-- 学科「その他」のidが入る
	course_id int NOT NULL COMMENT 'ユーザーの所属学科
roleが「先生」の場合も必要
どこにも所属していない場合は
学科「その他」のidが入る',
	-- 役割id
	role_id int NOT NULL COMMENT '役割id',
	-- 初めてのログインかどうかを判定するフラグ
	is_first_flg int NOT NULL COMMENT '初めてのログインかどうかを判定するフラグ',
	-- 認証失敗時カウントアップされる
	-- 何度失敗してもアカウントをロックしない場合は、カウントアップしない
	certify_err_cnt int DEFAULT 0 NOT NULL COMMENT '認証失敗時カウントアップされる
何度失敗してもアカウントをロックしない場合は、カウントアップしない',
	is_lock_flg int DEFAULT 0 NOT NULL,
	-- 入学年度
	-- ロールが学生の場合のみ
	-- そのほかはNULL
	admission_year int COMMENT '入学年度
ロールが学生の場合のみ
そのほかはNULL',
	-- 在校生や現役の教務はNULL
	graduate_year int COMMENT '在校生や現役の教務はNULL',
	-- 留年するたびに１プラスする
	-- 学年は
	-- 現在の年度-入学年度-留年回数+1
	-- で求める
	repeat_year_count int DEFAULT 0 NOT NULL COMMENT '留年するたびに１プラスする
学年は
現在の年度-入学年度-留年回数+1
で求める',
	give_up_year int,
	remark varchar(4000),
	entry_date datetime NOT NULL,
	update_date datetime NOT NULL,
	-- NULLの場合は未設定
	avatar_id_csv varchar(100) COMMENT 'NULLの場合は未設定',
	-- 2018/1/19で追加
	grade int DEFAULT 1 NOT NULL COMMENT '2018/1/19で追加',
	-- 卒業や退学などで無効となったデータについて
	-- 検索しやすいようにフラグを立てる
	-- 1=削除済み
	-- 0=未削除
	del_flg int DEFAULT 0 NOT NULL COMMENT '卒業や退学などで無効となったデータについて
検索しやすいようにフラグを立てる
1=削除済み
0=未削除',
	PRIMARY KEY (user_id),
	UNIQUE (student_no),
	UNIQUE (mailadress)
) COMMENT = '利用者テーブル
ログイン可能な利用者は全てこのテーブルに登録される';



/* Create Foreign Keys */

ALTER TABLE history_tbl
	ADD FOREIGN KEY (action_id)
	REFERENCES action_master (action_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE attached_file_tbl
	ADD FOREIGN KEY (bbsid)
	REFERENCES bbstbl (bbsid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE bbscheck_tbl
	ADD FOREIGN KEY (bbsid)
	REFERENCES bbstbl (bbsid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE bbstbl
	ADD FOREIGN KEY (category_id)
	REFERENCES category_tbl (category_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE user_tbl
	ADD FOREIGN KEY (course_id)
	REFERENCES course_master (course_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE user_tbl
	ADD FOREIGN KEY (role_id)
	REFERENCES role_master (role_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE category_tbl
	ADD FOREIGN KEY (room_id)
	REFERENCES room_tbl (room_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE room_user_tbl
	ADD FOREIGN KEY (room_id)
	REFERENCES room_tbl (room_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE bbscheck_tbl
	ADD FOREIGN KEY (user_id)
	REFERENCES user_tbl (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE chat_table
	ADD FOREIGN KEY (to_user_id)
	REFERENCES user_tbl (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE chat_table
	ADD FOREIGN KEY (from_user_id)
	REFERENCES user_tbl (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE history_tbl
	ADD FOREIGN KEY (user_id)
	REFERENCES user_tbl (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE room_tbl
	ADD FOREIGN KEY (update_user_id)
	REFERENCES user_tbl (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE room_tbl
	ADD FOREIGN KEY (create_user_id)
	REFERENCES user_tbl (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE room_user_tbl
	ADD FOREIGN KEY (user_id)
	REFERENCES user_tbl (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



/* Create Indexes */

CREATE INDEX idX_aTTaCED_FILE_BBSid ON attached_file_tbl (bbsid ASC);
CREATE INDEX INDEX_CaTEGORYtbl_roomid ON category_tbl (room_id ASC);
CREATE INDEX idX_His_user_id ON history_tbl (user_id ASC);
CREATE INDEX idX_His_action_id ON history_tbl (action_id ASC);
CREATE INDEX idX_His_action_date ON history_tbl (action_date ASC);
CREATE INDEX idX_user_course_id ON user_tbl (course_id ASC);
CREATE INDEX idX_user_role_id ON user_tbl (role_id ASC);
CREATE INDEX idX_user_tbl_nickname ON user_tbl (nick_name ASC);
CREATE INDEX idX_user_grade ON user_tbl (grade ASC);
CREATE INDEX idX_user_mailadress ON user_tbl (mailadress ASC);
CREATE INDEX INDEX_usertbl_studentno ON user_tbl (student_no ASC);



