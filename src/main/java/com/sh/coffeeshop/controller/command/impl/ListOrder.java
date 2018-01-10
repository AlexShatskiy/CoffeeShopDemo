package com.sh.coffeeshop.controller.command.impl;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.controller.helper.PageLibrary;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.service.CoffeeService;
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
import java.util.ArrayList;
import java.util.List;

public class ListOrder implements Command{

    private static final Logger log = LogManager.getRootLogger();

    private static String LIST_NEW_ORDER = "listNewOrder";
    private static String LIST_OLD_ORDER = "listOldOrder";
    private static final String ROLE = "role";

    private ServiceFactory factory = ServiceFactory.getInstance();
    private OrderService orderService = factory.getOrderService();


    /**
     * get list of orders
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Order> list;

        List<Order> listNewOrder = new ArrayList<>();
        List<Order> listOldOrder = new ArrayList<>();

        HttpSession session = request.getSession(true);
        String role = (String) session.getAttribute(ROLE);

        if("admin".equals(role)) {
            try {
                list = orderService.list();

                for (Order order : list) {
                    if (order.getStatus() == 0) {
                        listNewOrder.add(order);
                    } else {
                        listOldOrder.add(order);
                    }
                }

                request.setAttribute(LIST_NEW_ORDER, listNewOrder);
                request.setAttribute(LIST_OLD_ORDER, listOldOrder);

                RequestDispatcher dispatcher = request.getRequestDispatcher(PageLibrary.ADMIN_PAGE);
                dispatcher.forward(request, response);

            } catch (ServiceException e) {
                log.error("fail in ListOrder");
                response.sendRedirect("controller?command=LIST_COFFEE");
            }
        } else {
            log.error("ListOrder not admin");
            response.sendRedirect("controller?command=LIST_COFFEE");
        }
    }
}

