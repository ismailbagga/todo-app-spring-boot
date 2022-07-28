create table app_user (
	id  SERIAL PRIMARY KEY ,
	full_name VARCHAR(50) ,
	username VARCHAR(20) unique ,
	bio VARCHAR(255) ,
	email VARCHAR(255) UNIQUE,
	enabled boolean DEFAULT true ,
	password VARCHAR(500)
	) ;

create table todo (
	id Serial PRIMARY KEY ,
	task_name VARCHAR(50) NOT NULL  ,
	task_desc VARCHAR(255) ,
	created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP ,
	update_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP  ,
	user_id integer  references app_user(id) ON DELETE CASCADE ,
	completed boolean DEFAULT true
)
;