package com.sh.coffeeshop.dao.impl;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.model.Coffee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CoffeeDaoImpl implements CoffeeDao {

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
        //hard code
        List<Coffee> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            Coffee coffee = new Coffee();
            coffee.setId(Long.valueOf(i+1));
            coffee.setName("name" + coffee.getId());
            coffee.setDescription("description description description description description description");
            coffee.setPrice(new BigDecimal(10 + i));
            list.add(coffee);
        }
        return list;
    }
}
