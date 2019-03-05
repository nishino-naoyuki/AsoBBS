set session foreign_key_checks=0;

/* drop indexes */

drop index idx_attaced_file_bbsid on attached_file_tbl;
drop index index_categorytbl_roomid on category_tbl;
drop index idx_his_user_id on history_tbl;
drop index idx_his_action_id on history_tbl;
drop index idx_his_action_date on history_tbl;
drop index idx_user_course_id on user_tbl;
drop index idx_user_role_id on user_tbl;
drop index idx_user_tbl_nickname on user_tbl;
drop index idx_user_grade on user_tbl;
drop index idx_user_mailadress on user_tbl;
drop index index_usertbl_studentno on user_tbl;



/* drop tables */

drop table if exists history_tbl;
drop table if exists action_master;
drop table if exists attached_file_tbl;
drop table if exists bbs_check_tbl;
drop table if exists bbs_tbl;
drop table if exists category_tbl;
drop table if exists chat_table;
drop table if exists room_user_tbl;
drop table if exists room_tbl;
drop table if exists user_tbl;
drop table if exists course_master;
drop table if exists role_master;




/* create tables */

-- 操作マスター
create table action_master
(
	action_id int not null,
	action_name varchar(200),
	primary key (action_id)
) comment = '操作マスター';


create table attached_file_tbl
(
	attached_file_id int not null auto_increment,
	bbs_id int not null,
	-- ファイルのフルパス
	file_path varchar(600) not null comment 'ファイルのフルパス',
	-- 不正防止用に、ファイルをdlさせるときは必ずファイルidとファイルサイズとをマッチングさせる
	file_size bigint not null comment '不正防止用に、ファイルをdlさせるときは必ずファイルidとファイルサイズとをマッチングさせる',
	primary key (attached_file_id)
);


create table bbs_check_tbl
(
	bbs_id int not null,
	-- ユーザーid
	user_id int not null comment 'ユーザーid',
	check_date datetime not null,
	primary key (bbs_id, user_id)
);


create table bbs_tbl
(
	bbs_id int not null auto_increment,
	-- ルーム毎に設定されている掲示板のカテゴリ
	category_id int not null comment 'ルーム毎に設定されている掲示板のカテゴリ',
	title varchar(100) not null,
	message varchar(16000) not null,
	-- 緊急の書き込みの場合はonにする
	-- 1:緊急の書き込み
	-- 0:通常の書き込み
	emergency_flg int default 0 not null comment '緊急の書き込みの場合はonにする
1:緊急の書き込み
0:通常の書き込み',
	-- 返信元のbbsid
	-- おおもとの書き込みの場合はnullとなる
	parent_bbs_id int comment '返信元のbbsid
おおもとの書き込みの場合はnullとなる',
	-- 返信可能な書き込みかどうかを表す
	reply_ok_flg int not null comment '返信可能な書き込みかどうかを表す',
	create_date datetime not null,
	create_user_id int not null,
	update_date datetime not null,
	update_user_id int not null,
	primary key (bbs_id)
);


create table category_tbl
(
	-- ルーム毎に設定されている掲示板のカテゴリ
	category_id int not null auto_increment comment 'ルーム毎に設定されている掲示板のカテゴリ',
	-- カテゴリが属しているルームid
	room_id int not null comment 'カテゴリが属しているルームid',
	name varchar(100) not null,
	count int not null,
	primary key (category_id),
	constraint unique_category_by_room_cname unique (room_id, name)
);


create table chat_table
(
	msg_id int not null auto_increment,
	-- メッセージ
	msg varchar(1000) not null comment 'メッセージ',
	regster_datetime datetime not null,
	-- ユーザーid
	from_user_id int not null comment 'ユーザーid',
	-- ユーザーid
	to_user_id int not null comment 'ユーザーid',
	primary key (msg_id)
);


create table course_master
(
	course_id int not null,
	-- 学科名
	course_name varchar(100) not null comment '学科名',
	primary key (course_id)
);


-- 操作履歴テーブル
-- 操作履歴のテーブルです
create table history_tbl
(
	history_id int not null auto_increment,
	-- ユーザーid
	user_id int not null comment 'ユーザーid',
	action_id int not null,
	action_date datetime not null,
	-- 付加情報
	message varchar(2000) comment '付加情報',
	primary key (history_id)
) comment = '操作履歴テーブル
操作履歴のテーブルです';


-- 役割マスタ
-- ロールによる画面へのアクセス権限は、webアプリの設定ファイルにて行う
-- （いちいちdbにアクセスするとパ
create table role_master
(
	role_id int not null,
	role_name varchar(100) not null,
	primary key (role_id)
) comment = '役割マスタ
ロールによる画面へのアクセス権限は、webアプリの設定ファイルにて行う
（いちいちdbにアクセスするとパ';


create table room_tbl
(
	-- ルームid
	room_id int not null auto_increment comment 'ルームid',
	-- ルーム名
	name varchar(100) not null comment 'ルーム名',
	-- 作成日時
	create_date datetime not null comment '作成日時',
	-- 作成ユーザーid
	create_user_id int not null comment '作成ユーザーid',
	-- 初回作成時、update_dateとcreate_dateは同じになる
	update_date datetime not null comment '初回作成時、update_dateとcreate_dateは同じになる',
	-- ユーザーid
	update_user_id int not null comment 'ユーザーid',
	primary key (room_id),
	unique (name)
);


create table room_user_tbl
(
	-- ルームid
	room_id int not null comment 'ルームid',
	-- ユーザーid
	user_id int not null comment 'ユーザーid',
	-- 0:管理者（ルームの編集可能）
	-- 1:閲覧者（ルームの編集不可能）
	room_role int not null comment '0:管理者（ルームの編集可能）
1:閲覧者（ルームの編集不可能）',
	primary key (room_id, user_id)
);


-- 利用者テーブル
-- ログイン可能な利用者は全てこのテーブルに登録される
create table user_tbl
(
	-- ユーザーid
	user_id int not null auto_increment comment 'ユーザーid',
	student_no varchar(10) not null,
	-- メールアドレス（ログインid）
	mailadress varchar(255) not null comment 'メールアドレス（ログインid）',
	-- パスワードのハッシュ値
	-- ソルト値は設定ファイルから取得
	-- ソルト+パスワード+ソルト
	-- でハッシュ値を計算
	password varchar(255) not null comment 'パスワードのハッシュ値
ソルト値は設定ファイルから取得
ソルト+パスワード+ソルト
でハッシュ値を計算',
	-- 学生は学籍番号
	-- 職員は職員id
	name varchar(100) not null comment '学生は学籍番号
職員は職員id',
	-- ニックネーム
	-- ※aesで暗号化する
	-- 鍵は、設定ファイルの設定値+メアド
	nick_name varchar(100) not null comment 'ニックネーム
※aesで暗号化する
鍵は、設定ファイルの設定値+メアド',
	-- アカウントの有効期限（指定日まで有効）
	-- nullの場合は無期限
	account_expry_date date comment 'アカウントの有効期限（指定日まで有効）
nullの場合は無期限',
	-- nullは無期限
	password_expirydate date comment 'nullは無期限',
	-- ユーザーの所属学科
	-- roleが「先生」の場合も必要
	-- どこにも所属していない場合は
	-- 学科「その他」のidが入る
	course_id int not null comment 'ユーザーの所属学科
roleが「先生」の場合も必要
どこにも所属していない場合は
学科「その他」のidが入る',
	-- 役割id
	role_id int not null comment '役割id',
	-- 初めてのログインかどうかを判定するフラグ
	is_first_flg int not null comment '初めてのログインかどうかを判定するフラグ',
	-- 認証失敗時カウントアップされる
	-- 何度失敗してもアカウントをロックしない場合は、カウントアップしない
	certify_err_cnt int default 0 not null comment '認証失敗時カウントアップされる
何度失敗してもアカウントをロックしない場合は、カウントアップしない',
	is_lock_flg int default 0 not null,
	-- 入学年度
	-- ロールが学生の場合のみ
	-- そのほかはnull
	admission_year int comment '入学年度
ロールが学生の場合のみ
そのほかはnull',
	-- 在校生や現役の教務はnull
	graduate_year int comment '在校生や現役の教務はnull',
	-- 留年するたびに１プラスする
	-- 学年は
	-- 現在の年度-入学年度-留年回数+1
	-- で求める
	repeat_year_count int default 0 not null comment '留年するたびに１プラスする
学年は
現在の年度-入学年度-留年回数+1
で求める',
	giVe_up_year int,
	remark varchar(4000),
	entry_date datetime not null,
	update_date datetime not null,
	-- nullの場合は未設定
	aVatar_id_csV varchar(100) comment 'nullの場合は未設定',
	-- 2018/1/19で追加
	grade int default 1 not null comment '2018/1/19で追加',
	-- 卒業や退学などで無効となったデータについて
	-- 検索しやすいようにフラグを立てる
	-- 1=削除済み
	-- 0=未削除
	del_flg int default 0 not null comment '卒業や退学などで無効となったデータについて
検索しやすいようにフラグを立てる
1=削除済み
0=未削除',
	primary key (user_id),
	unique (student_no),
	unique (mailadress)
) comment = '利用者テーブル
ログイン可能な利用者は全てこのテーブルに登録される';



/* create foreign keys */

alter table history_tbl
	add foreign key (action_id)
	references action_master (action_id)
	on update restrict
	on delete restrict
;


alter table attached_file_tbl
	add foreign key (bbs_id)
	references bbs_tbl (bbs_id)
	on update restrict
	on delete restrict
;


alter table bbs_check_tbl
	add foreign key (bbs_id)
	references bbs_tbl (bbs_id)
	on update restrict
	on delete restrict
;


alter table bbs_tbl
	add foreign key (category_id)
	references category_tbl (category_id)
	on update restrict
	on delete restrict
;


alter table user_tbl
	add foreign key (course_id)
	references course_master (course_id)
	on update restrict
	on delete restrict
;


alter table user_tbl
	add foreign key (role_id)
	references role_master (role_id)
	on update restrict
	on delete restrict
;


alter table category_tbl
	add foreign key (room_id)
	references room_tbl (room_id)
	on update restrict
	on delete restrict
;


alter table room_user_tbl
	add foreign key (room_id)
	references room_tbl (room_id)
	on update restrict
	on delete restrict
;


alter table bbs_check_tbl
	add foreign key (user_id)
	references user_tbl (user_id)
	on update restrict
	on delete restrict
;


alter table chat_table
	add foreign key (to_user_id)
	references user_tbl (user_id)
	on update restrict
	on delete restrict
;


alter table chat_table
	add foreign key (from_user_id)
	references user_tbl (user_id)
	on update restrict
	on delete restrict
;


alter table history_tbl
	add foreign key (user_id)
	references user_tbl (user_id)
	on update restrict
	on delete restrict
;


alter table room_tbl
	add foreign key (update_user_id)
	references user_tbl (user_id)
	on update restrict
	on delete restrict
;


alter table room_tbl
	add foreign key (create_user_id)
	references user_tbl (user_id)
	on update restrict
	on delete restrict
;


alter table room_user_tbl
	add foreign key (user_id)
	references user_tbl (user_id)
	on update restrict
	on delete restrict
;



/* create indexes */

create index idx_attaced_file_bbsid on attached_file_tbl (bbs_id asc);
create index index_categorytbl_roomid on category_tbl (room_id asc);
create index idx_his_user_id on history_tbl (user_id asc);
create index idx_his_action_id on history_tbl (action_id asc);
create index idx_his_action_date on history_tbl (action_date asc);
create index idx_user_course_id on user_tbl (course_id asc);
create index idx_user_role_id on user_tbl (role_id asc);
create index idx_user_tbl_nickname on user_tbl (nick_name asc);
create index idx_user_grade on user_tbl (grade asc);
create index idx_user_mailadress on user_tbl (mailadress asc);
create index index_usertbl_studentno on user_tbl (student_no asc);



