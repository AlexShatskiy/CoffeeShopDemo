package com.sh.coffeeshop.service;

import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import com.sh.coffeeshop.service.exception.ServiceException;

import java.util.List;

public interface OrderService {

    Order get(Long id);
    Long save(Order order);
    List<Order> list();
    OrderItem createOrderItem(String coffeeId, String coffeeName, String quantity) throws ServiceException;
    Order addToOrder(Order order, OrderItem orderItem);
    Order deleteOrderItem(Order order, String coffeeId) throws ServiceException;
    Order checkQuantityOrder(Order order, List<String> quantityList) throws ServiceException;
}
