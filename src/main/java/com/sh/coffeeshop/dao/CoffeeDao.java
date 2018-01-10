package com.sh.coffeeshop.dao;

import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.model.Coffee;

import java.util.List;

public interface CoffeeDao {

    /**
     * get coffee by id
     * @param id
     * @return
     * @throws DaoException
     */
    Coffee get(Long id) throws DaoException;

    /**
     * save new Coffee in list of coffee
     * @param coffee
     * @return
     * @throws DaoException
     */
    Long save(Coffee coffee) throws DaoException;

    /**
     * get list of coffee
     * @return
     * @throws DaoException
     */
    List<Coffee> list() throws DaoException;
}
