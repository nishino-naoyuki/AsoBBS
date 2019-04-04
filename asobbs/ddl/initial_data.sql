insert into course_master(course_id,course_name)values(0,"情報システム科");
insert into course_master(course_id,course_name)values(1,"情報システム専攻科");
insert into course_master(course_id,course_name)values(2,"情報工学科");
insert into course_master(course_id,course_name)values(3,"組込みシステム科");
insert into course_master(course_id,course_name)values(4,"ネットワーク・セキュリティ科科");
insert into course_master(course_id,course_name)values(5,"電子システム科");
insert into course_master(course_id,course_name)values(6,"情報システム科 Ad");
insert into course_master(course_id,course_name)values(7,"情報システム専攻科 Ad");
insert into course_master(course_id,course_name)values(8,"税理士専攻科");
insert into course_master(course_id,course_name)values(9,"税理士科");
insert into course_master(course_id,course_name)values(10,"経理科");
insert into course_master(course_id,course_name)values(11,"経営ビジネス科");
insert into course_master(course_id,course_name)values(12,"ビジネスエキスパート科");
insert into course_master(course_id,course_name)values(13,"情報エキスパート科");
insert into course_master(course_id,course_name)values(14,"情報ビジネス科");
insert into course_master(course_id,course_name)values(15,"国際ビジネス科");
insert into course_master(course_id,course_name)values(16,'情報システム科PG');
insert into course_master(course_id,course_name)values(17,'情報システム科NW');
insert into course_master(course_id,course_name)values(18,'情報システム科電子');
insert into course_master(course_id,course_name)values(19,'情報システム専攻科SE');
insert into course_master(course_id,course_name)values(20,'情報システム専攻科NW');
insert into course_master(course_id,course_name)values(21,'情報システム専攻科電子');
insert into course_master(course_id,course_name)values(22,'情報工学科IT');
insert into course_master(course_id,course_name)values(23,'情報工学科NW');
insert into course_master(course_id,course_name)values(24,'情報工学科電子');
insert into course_master(course_id,course_name)values(25,'情報システム科PGアド');
insert into course_master(course_id,course_name)values(26,'情報システム専攻科SEアド');
insert into course_master(course_id,course_name)values(27,'国際ＩＴエンジニア科');
insert into course_master(course_id,course_name)values(999,"その他");

insert into role_master(role_id,role_name)values(0,"学生");
insert into role_master(role_id,role_name)values(1,"教員");
insert into role_master(role_id,role_name)values(2,"管理者");

insert into user_tbl(user_id,mailadress,password,student_no,name,nick_name,account_expry_date,password_expirydate,course_id,role_id,is_first_flg,certify_err_cnt,is_lock_flg,entry_date,update_date)
values(null,"nishino@asojuku.ac.jp","a2b06f65cf9c6797c6d3e8ae38d618b0","0000001","123456789","西野先生",null,null,1,2,1,0,0,current_date,current_date);
