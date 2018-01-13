package com.sh.coffeeshop.controller;

import com.sh.coffeeshop.controller.helper.FileCoffeeCreator;
import com.sh.coffeeshop.dao.manager.DaoPropertiesParameter;
import com.sh.coffeeshop.dao.manager.DaoPropertiesResourceManager;
import com.sh.coffeeshop.model.Coffee;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import com.sh.coffeeshop.service.CoffeeService;
import com.sh.coffeeshop.service.OrderService;
import com.sh.coffeeshop.service.exception.ServiceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * main controller executes all requests
 */
@Controller
public class CoffeeController implements InitializingBean {


    private static final String MAIN_SETTING = DaoPropertiesResourceManager.getInstance().getValue(DaoPropertiesParameter.DAO_ACTIVE);

    private static final Logger log = LogManager.getRootLogger();

    @Autowired
    @Qualifier("CoffeeService")
    private CoffeeService coffeeService;

    @Autowired
    @Qualifier("OrderService")
    private OrderService orderService;

    //constants
    private static String LIST_COFFEE = "listCoffee";
    private static String CONGRATULATION = "congratulation";

    private static String COFFEE_ID = "coffeeId";
    private static String QUANTITY = "quantity";
    private static String ORDER = "order";

    private static String ADDRESS = "address";
    private static String NAME = "name";
    private static String PHONE = "phone";

    private static String LIST_NEW_ORDER = "listNewOrder";
    private static String LIST_OLD_ORDER = "listOldOrder";

    private static String ORDER_ID = "orderId";



    /**
     * get list coffee
     * @param request
     * @return
     */
    @RequestMapping(value = "coffeeList", method = RequestMethod.GET)
    public String coffeeList(HttpServletRequest request) {

        List<Coffee> list;

        HttpSession session = request.getSession(true);
        session.removeAttribute(CONGRATULATION);

        try {
            list = coffeeService.list();
            request.setAttribute(LIST_COFFEE, list);
        } catch (ServiceException e) {
            log.error("fail ListCoffee");
        }

        return "coffeeList";
    }

    /**
     * add OrderItem in session
     * @param request
     * @return
     */
    @RequestMapping(value = "addOrderItem", method = RequestMethod.POST)
    public String addOrderItem(HttpServletRequest request) {

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
            order = orderService.setPrice(order);

            session.setAttribute(ORDER, order);

        } catch (ServiceException e) {
            log.error("fail in AddOrderItem");
        }

        return "redirect:coffeeList";
    }

    /**
     * forward to order page
     * @return
     */
    @RequestMapping(value = "orderPage", method = RequestMethod.GET)
    public String orderPage() {

        return "order";
    }

    /**
     * confirm Order
     * @param request
     * @return
     */
    @RequestMapping(value = "confirmOrder", method = RequestMethod.POST)
    public String confirmOrder(HttpServletRequest request) {

        Order order;
        List<String> quantityList = new ArrayList<>();

        HttpSession session = request.getSession(true);

        order = (Order) session.getAttribute(ORDER);

        //determine page
        String confirm = request.getParameter("confirm");

        for (OrderItem item : order.getItems()){
            quantityList.add(request.getParameter(item.getCoffee().getName()));
        }

        try {
            order = orderService.checkQuantityOrder(order, quantityList);
            order = orderService.setPrice(order);

            session.setAttribute(ORDER, order);

            if(confirm != null) {
                return "redirect:confirmOrderPage";
            } else {
                return "redirect:orderPage";
            }
        } catch (ServiceException e) {
            log.error("fail in ConfirmOrder");
            return "redirect:coffeeList";
        }
    }

    /**
     * delete OrderItem from Order
     * @param request
     * @return
     */
    @RequestMapping(value = "deleteOrderItem", method = RequestMethod.GET)
    public String deleteOrderItem(HttpServletRequest request) {

        String coffeeId;
        Order order;

        HttpSession session = request.getSession(true);

        coffeeId = request.getParameter(COFFEE_ID);
        order = (Order) session.getAttribute(ORDER);

        try {
            order = orderService.deleteOrderItem(order, coffeeId);
            order = orderService.setPrice(order);

            if(order.getItems().isEmpty()){
                session.removeAttribute(ORDER);

                return "redirect:coffeeList";
            } else {
                session.setAttribute(ORDER, order);

                return "redirect:orderPage";
            }
        } catch (ServiceException e) {
            log.error("fail in DeleteOrderItem");
            return "redirect:coffeeList";
        }
    }

    /**
     * forward to confirmOrder page
     * @return
     */
    @RequestMapping(value = "confirmOrderPage", method = RequestMethod.GET)
    public String confirmOrderPage() {

        return "confirmOrder";
    }

    /**
     * execute order
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "executeOrder", method = RequestMethod.POST)
    public String executeOrder(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

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
            return "redirect:coffeeList";
        } else {
            try {
                orderService.setUserToOrder(order, name, address, phone);
                orderService.save(order);
                session.removeAttribute(ORDER);
                session.setAttribute(CONGRATULATION, true);

                return "redirect:congratulation";
            } catch (ServiceException e) {
                log.error("fail in ExecuteOrder");
                return "redirect:confirmOrderPage";
            }
        }
    }

    /**
     * forward to congratulation page
     * @return
     */
    @RequestMapping(value = "congratulation", method = RequestMethod.GET)
    public String congratulation() {

        return "congratulation";
    }

    /**
     * get list of orders
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "admin/listOrder", method = RequestMethod.GET)
    public String listOrder(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        List<Order> list;

        List<Order> listNewOrder = new ArrayList<>();
        List<Order> listOldOrder = new ArrayList<>();

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

            return "admin";

        } catch (ServiceException e) {
            log.error("fail in ListOrder");
            return "redirect:coffeeList";
        }
    }

    /**
     * delivery order
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/DeliveredOrder", method = RequestMethod.POST)
    public String deliveredOrder(HttpServletRequest request) {

        String orderId;

        orderId = request.getParameter(ORDER_ID);

            try {

                orderService.updateStatusOrder(orderId);
            } catch (ServiceException e) {

                log.error("ListOrder not admin");
            }
        return "redirect:listOrder";
    }


    /**
     * create coffee list if main setting = file
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if("file".equals(MAIN_SETTING)){
            FileCoffeeCreator.createCoffeeList();
            log.info("create CoffeeList");
        }
    }
}
