package com.sh.coffeeshop.service;

import com.sh.coffeeshop.model.Coffee;

import java.util.List;

public interface CoffeeService {

    Coffee get(Long id);
    Long save(Coffee coffee);
    List<Coffee> list();
}
