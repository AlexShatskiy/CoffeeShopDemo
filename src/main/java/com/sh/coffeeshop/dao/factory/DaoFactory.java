package com.sh.coffeeshop.dao.factory;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.OrderDao;
import com.sh.coffeeshop.dao.impl.CoffeeDaoImpl;
import com.sh.coffeeshop.dao.impl.OrderDaoImpl;


/**
 * @author Shatskiy Alex
 * @version 1.0
 */
public class DaoFactory {
private static final DaoFactory instance = new DaoFactory();
	
	private final CoffeeDao coffeeDao = new CoffeeDaoImpl();
	private final OrderDao orderDao = new OrderDaoImpl();

	private DaoFactory(){}
	
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
