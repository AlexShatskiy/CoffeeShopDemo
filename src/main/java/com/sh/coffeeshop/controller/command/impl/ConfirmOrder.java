package com.sh.coffeeshop.controller.command.impl;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.controller.helper.PageLibrary;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import com.sh.coffeeshop.service.CoffeeService;
import com.sh.coffeeshop.service.OrderService;
import com.sh.coffeeshop.service.exception.ServiceException;
import com.sh.coffeeshop.service.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfirmOrder implements Command{

    private static String ORDER = "order";

    private ServiceFactory factory = ServiceFactory.getInstance();
    private OrderService orderService = factory.getOrderService();
    private CoffeeService coffeeService = factory.getCoffeeService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("ConfirmOrder");
        Order order;
        List<String> quantityList = new ArrayList<>();

        HttpSession session = request.getSession(true);

        order = (Order) session.getAttribute(ORDER);

        for (OrderItem item : order.getItems()){
            quantityList.add(request.getParameter(item.getCoffee().getName()));
        }

        try {
            order = orderService.checkQuantityOrder(order, quantityList);
            session.setAttribute(ORDER, order);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(PageLibrary.ORDER_PAGE);
        dispatcher.forward(request, response);
    }
}
