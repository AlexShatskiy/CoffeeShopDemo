package com.sh.coffeeshop.controller;

import com.sh.coffeeshop.controller.command.Command;
import com.sh.coffeeshop.controller.command.provider.CommandProviderXML;
import com.sh.coffeeshop.controller.helper.FileCoffeeCreator;
import com.sh.coffeeshop.dao.connection.ConnectionPool;
import com.sh.coffeeshop.dao.exception.ConnectionPoolException;
import com.sh.coffeeshop.dao.manager.DaoPropertiesParameter;
import com.sh.coffeeshop.dao.manager.DaoPropertiesResourceManager;
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
    private static final CommandProviderXML provider = CommandProviderXML.getInstance();
    private static final String COMMAND = "command";
    private static final String MAIN_SETTING = DaoPropertiesResourceManager.getInstance().getValue(DaoPropertiesParameter.DAO_ACTIVE);

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

    @Override
    public void init() throws ServletException {

        if("file".equals(MAIN_SETTING)){
            FileCoffeeCreator.createCoffeeList();
        } else if ("mysql".equals(MAIN_SETTING)){
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            try {
                connectionPool.initPoolData();
            } catch (ConnectionPoolException e) {
                log.error(e);
            }
        } else {
            FileCoffeeCreator.createCoffeeList();
            log.info("dao default setting: file");
        }
    }

    @Override
    public void destroy() {
        if("mysql".equals(MAIN_SETTING)) {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            try {
                connectionPool.destroyConnectionPool();
            } catch (ConnectionPoolException e) {
                log.error(e);
            }
        }
    }
}
