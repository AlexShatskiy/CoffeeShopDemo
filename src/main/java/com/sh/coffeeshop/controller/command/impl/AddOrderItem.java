package com.sh.coffeeshop.controller.command.impl;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import com.sh.coffeeshop.service.OrderService;
import com.sh.coffeeshop.service.exception.ServiceException;
import com.sh.coffeeshop.service.factory.ServiceFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class AddOrderItem implements Command{

    private static final Logger log = LogManager.getRootLogger();

    private static String COFFEE_ID = "coffeeId";
    private static String QUANTITY = "quantity";
    private static String ORDER = "order";

    private ServiceFactory factory = ServiceFactory.getInstance();
    private OrderService orderService = factory.getOrderService();


    /**
     * add orderItem in Order, add Order in session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String coffeeId;
        String quantity;

        Order order;
        OrderItem orderItem;

        HttpSession session = request.getSession(true);

        coffeeId = request.getParameter(COFFEE_ID);
        quantity = request.getParameter(QUANTITY);

        try {
            orderItem = orderService.createOrderItem(coffeeId, quantity);

            if (session.getAttribute(ORDER) == null) {
                order = new Order();
                order.setItems(new ArrayList<>());
            } else {
                order = (Order) session.getAttribute(ORDER);
            }

            order = orderService.addToOrder(order, orderItem);

            session.setAttribute(ORDER, order);

        } catch (ServiceException e) {
            log.error("fail in AddOrderItem");
        }

        response.sendRedirect("controller?command=LIST_COFFEE");
    }
}
