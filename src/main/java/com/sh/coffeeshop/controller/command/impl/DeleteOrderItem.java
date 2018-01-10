package com.sh.coffeeshop.controller.command.impl;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.controller.helper.PageLibrary;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.service.OrderService;
import com.sh.coffeeshop.service.exception.ServiceException;
import com.sh.coffeeshop.service.factory.ServiceFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeleteOrderItem implements Command {

    private static final Logger log = LogManager.getRootLogger();

    private static String COFFEE_ID = "coffeeId";
    private static String ORDER = "order";

    private ServiceFactory factory = ServiceFactory.getInstance();
    private OrderService orderService = factory.getOrderService();


    /**
     * delete OrderItem from Order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String coffeeId;
        Order order;

        HttpSession session = request.getSession(true);

        coffeeId = request.getParameter(COFFEE_ID);
        order = (Order) session.getAttribute(ORDER);

        try {
            order = orderService.deleteOrderItem(order, coffeeId);

            if(order.getItems().isEmpty()){
                session.removeAttribute(ORDER);

                response.sendRedirect("controller?command=LIST_COFFEE");
            } else {
                session.setAttribute(ORDER, order);

                response.sendRedirect("controller?command=ORDER_PAGE");
            }
        } catch (ServiceException e) {
            log.error("fail in DeleteOrderItem");
        }
    }
}
