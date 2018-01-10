package com.sh.coffeeshop.dao.manager;

public class DaoPropertiesParameter {
	
	private DaoPropertiesParameter() {}

	//main satting
	public static final String DAO_ACTIVE = "dao.active";

	//for file dao
	public static final String FOLDER_PATH = "folder.path";

	//for mysql dao
	public static final String MYSQL_DRIVER = "mysql.driver";
	public static final String MYSQL_USER = "mysql.user";
	public static final String MYSQL_PASSWORD = "mysql.password";
	public static final String MYSQL_URL = "mysql.url";
	public static final String MYSQL_POOLSIZE = "mysql.poolsize";

	//for mysql oracle
	public static final String ORACLE_DRIVER = "oracle.driver";
	public static final String ORACLE_USER = "oracle.user";
	public static final String ORACLE_PASSWORD = "oracle.password";
	public static final String ORACLE_URL = "oracle.url";
	public static final String ORACLE_POOLSIZE = "oracle.poolsize";
}
