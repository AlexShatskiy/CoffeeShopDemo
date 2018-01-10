package com.sh.coffeeshop.service;

import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import com.sh.coffeeshop.service.exception.ServiceException;

import java.util.List;

public interface OrderService {

    /**
     * get order dy id
     * @param id
     * @return
     * @throws ServiceException
     */
    Order get(String id) throws ServiceException;

    /**
     * save order
     * @param order
     * @return
     * @throws ServiceException
     */
    Long save(Order order) throws ServiceException;

    /**
     * get list of orders
     * @return
     * @throws ServiceException
     */
    List<Order> list() throws ServiceException;

    /**
     * create new OrderItem
     * @param coffeeId
     * @param quantity
     * @return
     * @throws ServiceException
     */
    OrderItem createOrderItem(String coffeeId, String quantity) throws ServiceException;

    /**
     * add OrderItem to Order
     * @param order
     * @param orderItem
     * @return
     */
    Order addToOrder(Order order, OrderItem orderItem);

    /**
     * delete OrderItem from Order
     * @param order
     * @param coffeeId
     * @return
     * @throws ServiceException
     */
    Order deleteOrderItem(Order order, String coffeeId) throws ServiceException;

    /**
     * check quantity by order
     * @param order
     * @param quantityList
     * @return
     * @throws ServiceException
     */
    Order checkQuantityOrder(Order order, List<String> quantityList) throws ServiceException;

    /**
     * set price by order
     * @param order
     * @return
     */
    Order setPrice(Order order);

    /**
     * set User to order
     * @param order
     * @param name
     * @param address
     * @param phone
     * @return
     * @throws ServiceException
     */
    Order setUserToOrder(Order order, String name, String address, String phone) throws ServiceException;

    /**
     * deliver order
     * @param orderId
     * @throws ServiceException
     */
    void updateStatusOrder(String orderId) throws ServiceException;
}
