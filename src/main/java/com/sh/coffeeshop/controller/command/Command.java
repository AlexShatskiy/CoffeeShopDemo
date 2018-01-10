package com.sh.coffeeshop.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {

    /**
     * executes the command
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
