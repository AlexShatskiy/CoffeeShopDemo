package com.sh.coffeeshop.dao.manager;

import java.util.ResourceBundle;

public class DaoPropertiesResourceManager {
	
	private static final String FILE_NAME = "daoSettings";
	
	private static DaoPropertiesResourceManager instance = null;
	private final ResourceBundle bundle = ResourceBundle.getBundle(FILE_NAME);
	
	private DaoPropertiesResourceManager() {}

	public static DaoPropertiesResourceManager getInstance() {
		if(instance == null){
			instance = new DaoPropertiesResourceManager();
		}
		return instance;
	}
	
	public String getValue(String key){
		return bundle.getString(key);
	}
}
