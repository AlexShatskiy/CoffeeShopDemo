package com.sh.coffeeshop.service;

import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.service.exception.ServiceException;

import java.util.List;

public interface CoffeeService {

    /**
     * save new Coffee
     * @param coffeeName
     * @param description
     * @param price
     * @return
     * @throws ServiceException
     */
    Long saveCoffee(String coffeeName, String description, String price) throws ServiceException;

    /**
     * get list of coffee
     * @return
     * @throws ServiceException
     */
    List<Coffee> list() throws ServiceException;
}
