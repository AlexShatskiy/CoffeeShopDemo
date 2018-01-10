package com.sh.coffeeshop.dao.impl.oracle;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.connection.ConnectionPool;
import com.sh.coffeeshop.dao.exception.ConnectionPoolException;
import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.model.Coffee;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoffeeDaoImplOracle implements CoffeeDao {

    private static final Logger log = LogManager.getRootLogger();

    private static final String GET_COFFEE_ID = "SELECT SEQ_Coffee.nextval FROM dual";
    private static final String SAVE_COFFEE = "INSERT INTO coffee (seqnr, name, description, price) VALUES (?, ?, ?, ?)";

    private static final String GET_COFFEE_BY_ID = "SELECT seqnr, name, description, price FROM coffee WHERE seqnr = ?";

    private static final String GET_ALL_COFFEE = "SELECT seqnr, name, description, price FROM coffee";


    /**
     * get coffee by id
     * @param id
     * @return
     * @throws DaoException
     */
    @Override
    public Coffee get(Long id) throws DaoException {

        Long coffeeId;
        String name;
        String description;
        BigDecimal price;

        Coffee coffee = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ConnectionPool pool = ConnectionPool.getInstance();

        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(GET_COFFEE_BY_ID);
            preparedStatement.setInt(1, Math.toIntExact(id));
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                coffeeId = resultSet.getLong(1);
                name = resultSet.getString(2);
                description = resultSet.getString(3);
                price = new BigDecimal(resultSet.getFloat(4));

                coffee = new Coffee(coffeeId, name, description, price);
            }
        } catch (ConnectionPoolException | SQLException e) {
            log.error(e);
            throw new DaoException("fail in get(Long id)", e);
        } finally {
            try {
                ConnectionPool.closeConnect(connection, preparedStatement, resultSet);
            } catch (ConnectionPoolException e) {
                log.error(e);
                throw new DaoException("fail in get(Long id)", e);
            }
        }
        return coffee;
    }

    /**
     * save new Coffee in list of coffee
     * @param coffee
     * @return
     * @throws DaoException
     */
    @Override
    public Long save(Coffee coffee) throws DaoException {

        Long coffeeId = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ConnectionPool pool = ConnectionPool.getInstance();

        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(GET_COFFEE_ID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                coffeeId = resultSet.getLong(1);
            }

            preparedStatement = connection.prepareStatement(SAVE_COFFEE);

            preparedStatement.setLong(1, coffeeId);
            preparedStatement.setString(2, coffee.getName());
            preparedStatement.setString(3, coffee.getDescription());
            preparedStatement.setFloat(4, coffee.getPrice().floatValue());

            preparedStatement.executeUpdate();

        }  catch (ConnectionPoolException | SQLException e) {
            log.error(e);
            throw new DaoException("fail in save(Coffee coffee)", e);
        } finally {
            try {
                ConnectionPool.closeConnect(connection, preparedStatement, resultSet);
            } catch (ConnectionPoolException e) {
                log.error(e);
                throw new DaoException("fail in save(Coffee coffee)", e);
            }
        }
        return coffeeId;
    }

    /**
     * get list of coffee
     * @return
     * @throws DaoException
     */
    @Override
    public List<Coffee> list() throws DaoException{
        List<Coffee> list = new ArrayList<>();

        Long coffeeId;
        String name;
        String description;
        BigDecimal price;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ConnectionPool pool = ConnectionPool.getInstance();

        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(GET_ALL_COFFEE);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                coffeeId = resultSet.getLong(1);
                name = resultSet.getString(2);
                description = resultSet.getString(3);
                price = new BigDecimal(resultSet.getFloat(4));

                list.add(new Coffee(coffeeId, name, description, price));
            }
        } catch (ConnectionPoolException | SQLException e) {
            log.error(e);
            throw new DaoException("fail in List<Coffee> list()", e);
        } finally {
            try {
                ConnectionPool.closeConnect(connection, preparedStatement, resultSet);
            } catch (ConnectionPoolException e) {
                log.error(e);
                throw new DaoException("fail in List<Coffee> list()", e);
            }
        }
        return list;
    }
}
