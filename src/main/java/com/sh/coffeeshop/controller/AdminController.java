package com.sh.coffeeshop.controller;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.controller.command.provider.CommandProviderXML;
import com.sh.coffeeshop.controller.helper.PageLibrary;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * admin controller
 */
public class AdminController extends HttpServlet {

    /**
     * Command provider transfers control to the command
     */
    private static final CommandProviderXML provider = CommandProviderXML.getInstance();
    private static final String COMMAND = "command";

    private static final Logger log = LogManager.getRootLogger();

    private static final String LOGIN = "admin";
    private static final String PASSWORD = "12345";
    private static final String ROLE = "role";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String commandName = request.getParameter(COMMAND);

        Command command = provider.getCommand(commandName);
        log.info("GET admin, command=" + commandName);
        command.execute(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(true);
        String commandName = request.getParameter(COMMAND);

        //hard code for login, logout
        if ("LOGIN".equals(commandName)) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");

            if (LOGIN.equals(login) && PASSWORD.equals(password)) {

                session.setAttribute(ROLE, "admin");
                log.info("login");
                response.sendRedirect("admin?command=LIST_ORDER");

            } else {
                log.info("invalid login or password");
                response.sendRedirect("controller?command=LIST_COFFEE");
            }
        } else if ("LOGOUT".equals(commandName)) {
            session.invalidate();
            log.info("logout");
            response.sendRedirect("controller?command=LIST_COFFEE");
        } else {
            Command command = provider.getCommand(commandName);
            log.info("POST, command=" + commandName);
            command.execute(request, response);
        }
    }
}
