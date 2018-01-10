package com.sh.coffeeshop.controller.command.impl;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.controller.helper.PageLibrary;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
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

public class ExecuteOrder implements Command {

    private static final Logger log = LogManager.getRootLogger();

    private static String ORDER = "order";
    private static String ADDRESS = "address";
    private static String NAME = "name";
    private static String PHONE = "phone";

    private static String CONGRATULATION = "congratulation";


    private ServiceFactory factory = ServiceFactory.getInstance();
    private OrderService orderService = factory.getOrderService();


    /**
     * execute order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String address;
        String name;
        String phone;

        Order order;

        address = request.getParameter(ADDRESS).trim();
        name = request.getParameter(NAME).trim();
        phone = request.getParameter(PHONE).trim();

        HttpSession session = request.getSession(true);

        order = (Order) session.getAttribute(ORDER);

        if (order == null){
            response.sendRedirect("controller?command=LIST_COFFEE");
        } else {
            try {
                orderService.setUserToOrder(order, name, address, phone);
                orderService.save(order);
                session.removeAttribute(ORDER);
                session.setAttribute(CONGRATULATION, true);

                response.sendRedirect("controller?command=CONGRATULATION");
            } catch (ServiceException e) {
                log.error("fail in ExecuteOrder");
                response.sendRedirect("controller?command=CONFIRM_ORDER_PAGE");
            }
        }
    }
}
