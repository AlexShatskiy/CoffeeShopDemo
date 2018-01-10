package com.sh.coffeeshop.service.impl;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.OrderDao;
import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.dao.factory.DaoFactory;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import com.sh.coffeeshop.service.OrderService;
import com.sh.coffeeshop.service.exception.ServiceException;
import com.sh.coffeeshop.service.validator.ServiceValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private static final Logger log = LogManager.getRootLogger();

    private DaoFactory daoFactory = DaoFactory.getInstance();
    private CoffeeDao coffeeDao = daoFactory.getCoffeeDao();
    private OrderDao orderDao = daoFactory.getOrderDao();


    /**
     * get order dy id
     * @param id
     * @return
     * @throws ServiceException
     */
    @Override
    public Order get(String id) throws ServiceException {

        Order order;

        if (ServiceValidator.isNumberValid(id)){
            try {
                order = orderDao.get(Long.parseLong(id));
            } catch (DaoException e) {
                log.error("fail in Order get(String id)");
                throw new ServiceException("DaoException", e);
            }
        } else {
            log.error("fail in Order get(String id) invalid parameter");
            throw new ServiceException("invalid parameter");
        }
        return order;
    }

    /**
     * save order
     * @param order
     * @return
     * @throws ServiceException
     */
    @Override
    public Long save(Order order) throws ServiceException {
        Long idOrder;
        try {
            idOrder = orderDao.save(order);
        } catch (DaoException e) {
            log.error("fail in save(Order order)");
            throw new ServiceException("DaoException", e);
        }
        return idOrder;
    }

    /**
     * get list of orders
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Order> list() throws ServiceException {
        List<Order> list;
        try {
            list = orderDao.list();
        } catch (DaoException e) {
            log.error("fail in List<Order> list()");
            throw new ServiceException("DaoException", e);
        }
        return list;
    }

    /**
     * create new OrderItem
     * @param coffeeId
     * @param quantity
     * @return
     * @throws ServiceException
     */
    @Override
    public OrderItem createOrderItem(String coffeeId, String quantity) throws ServiceException {

        OrderItem orderItem;

        if (ServiceValidator.isNumberValid(quantity)){

            Coffee coffee;

            try {
                coffee = coffeeDao.get(Long.parseLong(coffeeId));

                orderItem = new OrderItem();
                orderItem.setCoffee(coffee);
                orderItem.setQuantity(Integer.parseInt(quantity));
            } catch (DaoException e) {
                log.error("fail in createOrderItem(String coffeeId, String quantity)");
                throw new ServiceException("DaoException", e);
            }
        } else {
            log.error("fail in createOrderItem(String coffeeId, String quantity) invalid parameter");
            throw new ServiceException("invalid parameter");
        }
        return orderItem;
    }

    /**
     * add OrderItem to Order
     * @param order
     * @param orderItem
     * @return
     */
    @Override
    public Order addToOrder(Order order, OrderItem orderItem) {

        boolean isUniqueCoffee = true;

        for (OrderItem item : order.getItems()){
            if (item.getCoffee().getId().longValue() == orderItem.getCoffee().getId().longValue()){
                Integer quantity = item.getQuantity() + orderItem.getQuantity();
                item.setQuantity(quantity);
                isUniqueCoffee = false;
                break;
            }
        }

        if (isUniqueCoffee){
            order.getItems().add(orderItem);
        }

        log.info("addToOrder");
        return order;
    }

    /**
     * delete OrderItem from Order
     * @param order
     * @param coffeeId
     * @return
     * @throws ServiceException
     */
    @Override
    public Order deleteOrderItem(Order order, String coffeeId) throws ServiceException {
        if (ServiceValidator.isNumberValid(coffeeId)){
            Long coffeeIdLong = Long.parseLong(coffeeId);

            Iterator<OrderItem> orderItemIterator = order.getItems().iterator();

            while (orderItemIterator.hasNext()) {
                if (orderItemIterator.next().getCoffee().getId().longValue() == coffeeIdLong.longValue()) {
                    orderItemIterator.remove();
                    break;
                }
            }
        } else {
            log.error("fail in deleteOrderItem(Order order, String coffeeId) invalid parameter");
            throw new ServiceException("invalid parameter");
        }
        return order;
    }

    /**
     * check quantity by order
     * @param order
     * @param quantityList
     * @return
     * @throws ServiceException
     */
    @Override
    public Order checkQuantityOrder(Order order, List<String> quantityList) throws ServiceException {

        for (int i = 0; i < quantityList.size(); i++){

            String quantity = quantityList.get(i);
            if (ServiceValidator.isNumberValid(quantity)){
                order.getItems().get(i).setQuantity(Integer.parseInt(quantity));
            }
        }
        log.info("checkQuantityOrder");
        return order;
    }

    /**
     * set price by order
     * @param order
     * @return
     */
    @Override
    public Order setPrice(Order order) {

        BigDecimal price = new BigDecimal("0");

        for (OrderItem item : order.getItems()){
            BigDecimal priceCoffee = item.getCoffee().getPrice().multiply(new BigDecimal(item.getQuantity()));
            price = price.add(priceCoffee);
        }

        order.setPrice(price);
        log.info("setPrice");
        return order;
    }

    /**
     * set User to order
     * @param order
     * @param name
     * @param address
     * @param phone
     * @return
     * @throws ServiceException
     */
    @Override
    public Order setUserToOrder(Order order, String name, String address, String phone) throws ServiceException {

        if (ServiceValidator.isNumberValid(phone) && ServiceValidator.isTextValid(name) && ServiceValidator.isTextValid(address)){

            order.setCustomername(name);
            order.setCustomeraddress(address);
            order.setPhone(phone);

        } else {
            log.error("fail in setUserToOrder(Order order, String name, String address, String phone) invalid parameter");
            throw new ServiceException("invalid parameter");
        }
        return order;
    }

    /**
     * deliver order
     * @param orderId
     * @throws ServiceException
     */
    @Override
    public void updateStatusOrder(String orderId) throws ServiceException {
        if (ServiceValidator.isNumberValid(orderId)){
            try {
                orderDao.updateStatusOrder(Long.parseLong(orderId));
            } catch (DaoException e) {
                log.error("fail in deliverOrder(String orderId) invalid parameter");
                throw new ServiceException("DaoException", e);
            }
        } else {
            log.error("fail in deliverOrder(String orderId) invalid parameter");
            throw new ServiceException("invalid parameter");
        }
    }
}
