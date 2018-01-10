package com.sh.coffeeshop.dao;

import com.sh.coffeeshop.model.Order;

import java.util.List;

public interface OrderDao {

    Order get(Long id);
    Long save(Order order);
    List<Order> list();
}
