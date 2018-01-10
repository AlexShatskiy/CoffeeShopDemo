package com.sh.coffeeshop.dao.factory;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.OrderDao;
import com.sh.coffeeshop.dao.impl.file.CoffeeDaoImpl;
import com.sh.coffeeshop.dao.impl.file.OrderDaoImpl;
import com.sh.coffeeshop.dao.impl.mysql.CoffeeDaoImplMySQL;
import com.sh.coffeeshop.dao.impl.mysql.OrderDaoImplMySQL;
import com.sh.coffeeshop.dao.manager.DaoPropertiesParameter;
import com.sh.coffeeshop.dao.manager.DaoPropertiesResourceManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DaoFactory {

	private static final Logger log = LogManager.getRootLogger();

	private static final DaoFactory instance = new DaoFactory();
	
	private final CoffeeDao coffeeDao;
	private final OrderDao orderDao;

	private DaoFactory(){

		String mainSetting = DaoPropertiesResourceManager.getInstance().getValue(DaoPropertiesParameter.DAO_ACTIVE);

		if("file".equals(mainSetting)){
			coffeeDao = new CoffeeDaoImpl();
			orderDao = new OrderDaoImpl();
		} else if ("mysql".equals(mainSetting)){
			coffeeDao = new CoffeeDaoImplMySQL();
			orderDao = new OrderDaoImplMySQL();
		} else {
			coffeeDao = new CoffeeDaoImpl();
			orderDao = new OrderDaoImpl();
			log.info("Factory dao default setting: file");
		}
	}
	
	public static DaoFactory getInstance(){
		return instance;
	}

	public CoffeeDao getCoffeeDao() {

		return coffeeDao;
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}
}
