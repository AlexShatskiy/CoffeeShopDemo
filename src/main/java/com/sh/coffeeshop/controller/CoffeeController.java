package com.sh.coffeeshop.controller;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.controller.command.provider.CommandProviderXML;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CoffeeController extends HttpServlet {

    /**
     * Command provider transfers control to the command
     */
    private static final CommandProviderXML provider = new CommandProviderXML();
    private static final String COMMAND = "command";

    private static final Logger log = LogManager.getRootLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String commandName = request.getParameter(COMMAND);

        Command command = provider.getCommand(commandName);
        log.info("GET, command=" + commandName);
        command.execute(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String commandName = request.getParameter(COMMAND);

        Command command = provider.getCommand(commandName);
        log.info("POST, command=" + commandName);
        command.execute(request, response);
    }
}
