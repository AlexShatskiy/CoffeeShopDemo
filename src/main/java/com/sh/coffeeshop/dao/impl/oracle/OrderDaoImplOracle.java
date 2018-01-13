package com.sh.coffeeshop.dao.impl.oracle;

import com.sh.coffeeshop.dao.OrderDao;
import com.sh.coffeeshop.dao.connection.ConnectionPool;
import com.sh.coffeeshop.dao.exception.ConnectionPoolException;
import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("OrderDaoImplOracle")
public class OrderDaoImplOracle implements OrderDao {

    @Autowired
    ConnectionPool pool;

    private static final Logger log = LogManager.getRootLogger();

    private static final String GET_ORDER_ID = "SELECT seq_Orders.nextval FROM dual";
    private static final String SAVE_ORDER = "INSERT INTO Orders (seqnr, customername, customeraddress, phone, status, price) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SAVE_ORDER_ITEM = "INSERT INTO orderitem (seqnr, coffee, \"order\", quantity) VALUES (seq_OrderItem.nextval, ?, ?, ?)";

    private static final String GET_ORDER_BY_ID = "SELECT customername, customeraddress, phone, status, price FROM orders WHERE seqnr = ?";
    private static final String GET_ORDERITEM_BY_ORDER_ID = "SELECT orderitem.seqnr, coffee, quantity, name, description, price FROM orderitem INNER JOIN coffee ON orderitem.coffee = coffee.seqnr WHERE \"order\" = ?";

    private static final String GET_ORDERS = "SELECT seqnr, customername, customeraddress, phone, status, price FROM orders";

    private static final String UPDATE_ORDER_STATUS = "UPDATE orders SET status = ? WHERE seqnr = ?";



    /**
     * get Order by id
     *
     * @param id
     * @return
     * @throws DaoException
     */
    @Override
    public Order get(Long id) throws DaoException {
       Order order = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(GET_ORDER_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            //put orders in orderList
            while (resultSet.next()) {
                order = new Order();

                order.setId(id);
                order.setCustomername(resultSet.getString(1));
                order.setCustomeraddress(resultSet.getString(2));
                order.setPhone(resultSet.getString(3));
                order.setStatus(resultSet.getInt(4));
                order.setPrice(new BigDecimal(resultSet.getFloat(5)));
            }

            preparedStatement = connection.prepareStatement(GET_ORDERITEM_BY_ORDER_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            List<OrderItem> orderItemList = new ArrayList<>();

            //put orderItem in orderItemList
            addToListOrderItems(resultSet, orderItemList, order);

            order.setItems(orderItemList);

        } catch (ConnectionPoolException | SQLException e) {
            log.error(e);
            throw new DaoException("fail in Order get(Long id)", e);
        } finally {
            try {
                ConnectionPool.closeConnect(connection, preparedStatement, resultSet);
            } catch (ConnectionPoolException e) {
                log.error(e);
                throw new DaoException("fail in Order get(Long id)", e);
            }
        }
        return order;
    }

    /**
     * save Order
     *
     * @param order
     * @return
     * @throws DaoException
     */
    @Override
    public Long save(Order order) throws DaoException {
        Long orderId = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = pool.takeConnection();

            //get id for order
            preparedStatement = connection.prepareStatement(GET_ORDER_ID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                orderId = resultSet.getLong(1);
            }

            //save order
            preparedStatement = connection.prepareStatement(SAVE_ORDER);

            preparedStatement.setLong(1, orderId);
            preparedStatement.setString(2, order.getCustomername());
            preparedStatement.setString(3, order.getCustomeraddress());
            preparedStatement.setString(4, order.getPhone());
            preparedStatement.setInt(5, 0);
            preparedStatement.setFloat(6, order.getPrice().floatValue());

            preparedStatement.executeUpdate();

            //save orderItem
            preparedStatement = connection.prepareStatement(SAVE_ORDER_ITEM);

            for (OrderItem item : order.getItems()) {
                preparedStatement.setLong(1, item.getCoffee().getId());
                preparedStatement.setLong(2, orderId);
                preparedStatement.setInt(3, item.getQuantity());

                preparedStatement.executeUpdate();
            }

        } catch (ConnectionPoolException | SQLException e) {
            log.error(e);
            throw new DaoException("fail in save(Order order)", e);
        } finally {
            try {
                ConnectionPool.closeConnect(connection, preparedStatement, resultSet);
            } catch (ConnectionPoolException e) {
                log.error(e);
                throw new DaoException("fail in save(Order order)", e);
            }
        }
        return orderId;
    }

    /**
     * get list of orders
     *
     * @return
     * @throws DaoException
     */
    @Override
    public List<Order> list() throws DaoException {

        List<Order> orderList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(GET_ORDERS);
            resultSet = preparedStatement.executeQuery();

            //put orders in orderList
            while (resultSet.next()) {

                Order order = new Order();

                order.setId(resultSet.getLong(1));
                order.setCustomername(resultSet.getString(2));
                order.setCustomeraddress(resultSet.getString(3));
                order.setPhone(resultSet.getString(4));
                order.setStatus(resultSet.getInt(5));
                order.setPrice(new BigDecimal(resultSet.getFloat(6)));

                orderList.add(order);
            }

            preparedStatement = connection.prepareStatement(GET_ORDERITEM_BY_ORDER_ID);

            for (Order order : orderList) {

                preparedStatement.setLong(1, order.getId());
                resultSet = preparedStatement.executeQuery();

                List<OrderItem> orderItemList = new ArrayList<>();

                //put orderItem in orderItemList
                addToListOrderItems(resultSet, orderItemList, order);

                order.setItems(orderItemList);
            }

        } catch (ConnectionPoolException | SQLException e) {
            log.error(e);
            throw new DaoException("fail in List<Order> list()", e);
        } finally {
            try {
                ConnectionPool.closeConnect(connection, preparedStatement, resultSet);
            } catch (ConnectionPoolException e) {
                log.error(e);
                throw new DaoException("fail in List<Order> list()", e);
            }
        }
        return orderList;
    }

    /**
     * set order status "completed"
     *
     * @param orderId
     * @throws DaoException
     */
    @Override
    public void updateStatusOrder(Long orderId) throws DaoException {

        int result = 0;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS);

            preparedStatement.setInt(1, 1);
            preparedStatement.setLong(2, orderId);

            result = preparedStatement.executeUpdate();
            if (result != 1) {
                log.error("fail in updateStatusOrder(Long orderId)");
                throw new DaoException("fail in updateStatusOrder(Long orderId)");
            }
        } catch (ConnectionPoolException | SQLException e) {
            log.error(e);
            throw new DaoException("fail in updateStatusOrder(Long orderId)");
        } finally {
            try {
                ConnectionPool.closeConnect(connection, preparedStatement, resultSet);
            } catch (ConnectionPoolException e) {
                log.error(e);
                throw new DaoException("fail in updateStatusOrder(Long orderId)", e);
            }
        }
    }

    //put orderItem in orderItemList
    private void addToListOrderItems(ResultSet resultSet, List<OrderItem> orderItemList, Order order) throws SQLException {

        while (resultSet.next()) {
            OrderItem orderItem = new OrderItem();
            Coffee coffee = new Coffee();

            orderItem.setId(resultSet.getLong(1));
            coffee.setId(resultSet.getLong(2));
            orderItem.setQuantity(resultSet.getInt(3));
            coffee.setName(resultSet.getString(4));
            coffee.setDescription(resultSet.getString(5));
            coffee.setPrice(new BigDecimal(resultSet.getFloat(6)));

            orderItem.setCoffee(coffee);
            orderItem.setOrder(order);

            orderItemList.add(orderItem);
        }
    }
}
