package com.sh.coffeeshop.controller.command.impl;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.controller.helper.PageLibrary;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.service.CoffeeService;
import com.sh.coffeeshop.service.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ListCoffee implements Command{

    private static String LIST_COFFEE = "listCoffee";

    private ServiceFactory factory = ServiceFactory.getInstance();
    private CoffeeService service = factory.getCoffeeService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Coffee> list = service.list();

        request.setAttribute(LIST_COFFEE, list);

        RequestDispatcher dispatcher = request.getRequestDispatcher(PageLibrary.COFFEE_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
