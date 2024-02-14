package com.study.insert_and_select.config;

public interface DBConfig { // interface = public static final // 바뀌면 안되기 때문에 final, 
	
	String URL = "jdbc:mysql://mysql-db.cfo64mwoijhv.ap-northeast-2.rds.amazonaws.com/db_study"; // DB에 연결
	String USERNAME = "aws";
	String PASSWORD = "1q2w3e!!";
}
