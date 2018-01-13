package com.sh.coffeeshop.service.impl;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.service.CoffeeService;
import com.sh.coffeeshop.service.exception.ServiceException;
import com.sh.coffeeshop.service.validator.ServiceValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("CoffeeService")
public class CoffeeServiceImpl implements CoffeeService {

    private static final Logger log = LogManager.getRootLogger();

    @Autowired
    @Qualifier("CoffeeDao")
    private CoffeeDao dao;


    /**
     * save new Coffee
     * @param coffeeName
     * @param description
     * @param price
     * @return
     * @throws ServiceException
     */
    @Override
    public Long saveCoffee(String coffeeName, String description, String price) throws ServiceException {

        Long id;
        
        if (ServiceValidator.isNumberValid(price) && ServiceValidator.isTextValid(coffeeName)){
            Coffee coffee = new Coffee();
            
            coffee.setName(coffeeName);
            coffee.setDescription(description);
            coffee.setPrice(new BigDecimal(price));

            try {
                id = dao.save(coffee);
            } catch (DaoException e) {
                log.error("fail in saveCoffee(String coffeeName, String description, String price)");
                throw new ServiceException("DaoException", e);
            }
        } else {
            log.error("fail in saveCoffee(String coffeeName, String description, String price)");
            throw new ServiceException("invalid parameter");
        }
        return id;
    }

    /**
     * get list of coffee
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Coffee> list() throws ServiceException {
        List<Coffee> list;
        try {
            list = dao.list();
        } catch (DaoException e) {
            log.error("fail in list()");
            throw new ServiceException("DaoException", e);
        }
        return list;
    }
}
