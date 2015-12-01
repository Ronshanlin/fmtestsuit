create table system (
    id int not null IDENTITY,
    sys_code varchar(20),
    sys_name varchar(40)
);

create table user (
	id int not null identity,
	user_no varchar(10),
	user_password varchar(20),
	create_time datetime,
	pwd_expire bigint
);

create table system_user_rel(
	id int not null identity,
	sys_code varchar(20),
	user_no varchar(10)
);

create table system_conf (
	sys_code varchar(20),
	sys_conf_code varchar(50),
	sys_conf_path varchar(512)
);

create table system_conf_vals (
	sys_conf_path varchar(120),
	sys_conf_key varchar(80),
	sys_conf_val varchar(100)
);

create table system_svn_tree(
	sys_code varchar(20),
	node_path varchar(512),
	parent_node_path varchar(512)
);