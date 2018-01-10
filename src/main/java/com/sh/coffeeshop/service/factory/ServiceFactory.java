package com.sh.coffeeshop.service.factory;

import com.sh.coffeeshop.service.CoffeeService;
import com.sh.coffeeshop.service.OrderService;
import com.sh.coffeeshop.service.impl.CoffeeServiceImpl;
import com.sh.coffeeshop.service.impl.OrderServiceImpl;

public class ServiceFactory {
private static final ServiceFactory instance = new ServiceFactory();
	
	private final CoffeeService coffeeService = new CoffeeServiceImpl();
	private final OrderService orderService = new OrderServiceImpl();

	private ServiceFactory(){}
	
	public static ServiceFactory getInstance(){
		return instance;
	}

	public CoffeeService getCoffeeService() {
		return coffeeService;
	}

	public OrderService getOrderService() {
		return orderService;
	}
}
