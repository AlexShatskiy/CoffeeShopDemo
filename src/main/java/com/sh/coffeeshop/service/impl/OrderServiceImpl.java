package com.sh.coffeeshop.service.impl;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.factory.DaoFactory;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import com.sh.coffeeshop.service.OrderService;
import com.sh.coffeeshop.service.exception.ServiceException;
import com.sh.coffeeshop.service.validator.ServiceValidator;

import java.util.Iterator;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private DaoFactory factory = DaoFactory.getInstance();
    private CoffeeDao service = factory.getCoffeeDao();

    @Override
    public Order get(Long id) {
        return null;
    }

    @Override
    public Long save(Order order) {
        return null;
    }

    @Override
    public List<Order> list() {
        return null;
    }

    @Override
    public OrderItem createOrderItem(String coffeeId, String coffeeName, String quantity) throws ServiceException {

        OrderItem orderItem;

        if (ServiceValidator.isQuantityOrIdValid(quantity) && ServiceValidator.isNameValid(coffeeName)){
            Coffee coffee = new Coffee();
            coffee.setId(Long.parseLong(coffeeId));
            coffee.setName(coffeeName);

            orderItem = new OrderItem();
            orderItem.setCoffee(coffee);
            orderItem.setQuantity(Integer.parseInt(quantity));
        } else {
            throw new ServiceException("invalid parameter");
        }

        return orderItem;
    }

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
        return order;
    }

    @Override
    public Order deleteOrderItem(Order order, String coffeeId) throws ServiceException {
        if (ServiceValidator.isQuantityOrIdValid(coffeeId)){
            Long coffeeIdLong = Long.parseLong(coffeeId);

            Iterator<OrderItem> orderItemIterator = order.getItems().iterator();

            while (orderItemIterator.hasNext()) {
                if (orderItemIterator.next().getCoffee().getId().longValue() == coffeeIdLong.longValue()) {
                    orderItemIterator.remove();
                    break;
                }
            }
        } else {
            throw new ServiceException("invalid parameter");
        }
        return order;
    }

    @Override
    public Order checkQuantityOrder(Order order, List<String> quantityList) throws ServiceException {

        for (int i = 0; i < quantityList.size(); i++){

            String quantity = quantityList.get(i);
            if (ServiceValidator.isQuantityOrIdValid(quantity)){
                order.getItems().get(i).setQuantity(Integer.parseInt(quantity));
            }
        }
        return order;
    }
}
