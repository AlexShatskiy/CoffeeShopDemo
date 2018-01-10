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

public class DeliveredOrder implements Command {

    private static final Logger log = LogManager.getRootLogger();

    private static String ORDER_ID = "orderId";
    private static final String ROLE = "role";

    private ServiceFactory factory = ServiceFactory.getInstance();
    private OrderService orderService = factory.getOrderService();


    /**
     * delivery order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String orderId;

        orderId = request.getParameter(ORDER_ID);

        HttpSession session = request.getSession(true);
        String role = (String) session.getAttribute(ROLE);

        if("admin".equals(role)) {
            try {
                orderService.updateStatusOrder(orderId);

                response.sendRedirect("admin?command=LIST_ORDER");

            } catch (ServiceException e) {
                log.error("ListOrder not admin");
                response.sendRedirect("controller?command=LIST_COFFEE");
            }
        }
    }
}
