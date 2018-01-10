package com.sh.coffeeshop.dao;

import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.model.Order;

import java.util.List;

public interface OrderDao {

    /**
     * get Order by id
     * @param id
     * @return
     * @throws DaoException
     */
    Order get(Long id) throws DaoException;

    /**
     * save Order
     * @param order
     * @return
     * @throws DaoException
     */
    Long save(Order order) throws DaoException;

    /**
     * get list of orders
     * @return
     * @throws DaoException
     */
    List<Order> list() throws DaoException;

    /**
     * set order status "completed"
     * @param orderId
     * @throws DaoException
     */
    void updateStatusOrder(Long orderId) throws DaoException;
}
