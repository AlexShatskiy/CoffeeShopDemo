package com.sh.coffeeshop.controller.command.impl;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.controller.helper.PageLibrary;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.service.CoffeeService;
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
import java.util.List;

public class ListCoffee implements Command{

    private static final Logger log = LogManager.getRootLogger();

    private static String LIST_COFFEE = "listCoffee";
    private static String CONGRATULATION = "congratulation";

    private ServiceFactory factory = ServiceFactory.getInstance();
    private CoffeeService service = factory.getCoffeeService();

    /**
     * get list of coffee
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Coffee> list;

        HttpSession session = request.getSession(true);
        session.removeAttribute(CONGRATULATION);

        try {
            list = service.list();
            request.setAttribute(LIST_COFFEE, list);
        } catch (ServiceException e) {
            log.error("fail ListCoffee");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(PageLibrary.COFFEE_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
