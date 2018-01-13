package com.sh.coffeeshop.dao.impl.mysql;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.connection.ConnectionPool;
import com.sh.coffeeshop.dao.exception.ConnectionPoolException;
import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.model.Coffee;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("CoffeeDaoImplMySQL")
public class CoffeeDaoImplMySQL implements CoffeeDao {

    @Autowired
    ConnectionPool pool;

    private static final Logger log = LogManager.getRootLogger();

    private static final String GET_ALL_COFFEE = "SELECT id, name, description, price FROM coffee";
    private static final String GET_COFFEE_BY_ID = "SELECT id, name, description, price FROM coffee WHERE id = ?";
    private static final String SAVE_COFFEE = "INSERT INTO coffee (name, description, price) VALUES (?, ?, ?);";


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

        Long coffeeId;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(SAVE_COFFEE, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, coffee.getName());
            preparedStatement.setString(2, coffee.getDescription());
            preparedStatement.setFloat(3, coffee.getPrice().floatValue());

            preparedStatement.executeLargeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            coffeeId = resultSet.getLong(1);
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
