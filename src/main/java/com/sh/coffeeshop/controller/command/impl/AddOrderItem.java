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

public class AddOrderItem implements Command{

    private static String LIST_COFFEE = "listCoffee";
    private static String COFFEE_NAME = "coffeeName";
    private static String COFFEE_ID = "coffeeId";
    private static String QUANTITY = "quantity";
    private static String ORDER = "order";

    private ServiceFactory factory = ServiceFactory.getInstance();
    private OrderService orderService = factory.getOrderService();
    private CoffeeService coffeeService = factory.getCoffeeService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String coffeeId;
        String coffeeName;
        String quantity;

        Order order;
        OrderItem orderItem;

        HttpSession session = request.getSession(true);

        coffeeId = request.getParameter(COFFEE_ID);
        coffeeName = request.getParameter(COFFEE_NAME);
        quantity = request.getParameter(QUANTITY);

        try {
            orderItem = orderService.createOrderItem(coffeeId, coffeeName, quantity);

            if (session.getAttribute(ORDER) == null) {
                order = new Order();
                order.setItems(new ArrayList<>());
            } else {
                order = (Order) session.getAttribute(ORDER);
            }

            order = orderService.addToOrder(order, orderItem);

            session.setAttribute(ORDER, order);

            for (OrderItem item : order.getItems()){
                System.out.println(item);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        List<Coffee> list = coffeeService.list();

        request.setAttribute(LIST_COFFEE, list);

        RequestDispatcher dispatcher = request.getRequestDispatcher(PageLibrary.COFFEE_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
