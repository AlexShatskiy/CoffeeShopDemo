package com.sh.coffeeshop.service.impl;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.factory.DaoFactory;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.service.CoffeeService;

import java.util.List;

public class CoffeeServiceImpl implements CoffeeService {

    DaoFactory factory = DaoFactory.getInstance();
    CoffeeDao dao = factory.getCoffeeDao();

    @Override
    public Coffee get(Long id) {
        return null;
    }

    @Override
    public Long save(Coffee coffee) {
        return null;
    }

    @Override
    public List<Coffee> list() {
        return dao.list();
    }
}
