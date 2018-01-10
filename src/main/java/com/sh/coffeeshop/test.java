package com.sh.coffeeshop;

import com.sh.coffeeshop.dao.connection.ConnectionPool;
import com.sh.coffeeshop.dao.exception.ConnectionPoolException;
import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.dao.impl.mysql.CoffeeDaoImplMySQL;
import com.sh.coffeeshop.dao.impl.mysql.OrderDaoImplMySQL;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;

public class test {





    public static void main(String[] args) throws ConnectionPoolException, DaoException {
        testGetOrder();
    }

    public static void testSeveCoffee() throws ConnectionPoolException, DaoException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.initPoolData();


        CoffeeDaoImplMySQL coffeeDao = new CoffeeDaoImplMySQL();

        Coffee coffee2 = new Coffee();
        coffee2.setName("Double Espresso (Test)");
        coffee2.setDescription("A double espresso (aka “Doppio”) is just that, two espresso shots in one cup.");
        coffee2.setPrice(new BigDecimal(5));

        System.out.println("Test: " + coffeeDao.save(coffee2));

        connectionPool.destroyConnectionPool();
    }

    public static void testGetOrder() throws ConnectionPoolException, DaoException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.initPoolData();


        OrderDaoImplMySQL orderDao = new OrderDaoImplMySQL();

        Order order = orderDao.get(new Long(1));
        for (OrderItem item : order.getItems()){
            System.out.println(item);
        }

        connectionPool.destroyConnectionPool();
    }


}
