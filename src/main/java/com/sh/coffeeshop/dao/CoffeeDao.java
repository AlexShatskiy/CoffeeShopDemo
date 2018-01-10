package com.sh.coffeeshop.dao;

import com.sh.coffeeshop.model.Coffee;

import java.util.List;

public interface CoffeeDao {

    Coffee get(Long id);
    Long save(Coffee coffee);
    List<Coffee> list();
}
