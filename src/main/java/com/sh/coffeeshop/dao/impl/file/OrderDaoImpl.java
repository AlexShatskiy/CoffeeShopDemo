package com.sh.coffeeshop.dao.impl.file;

import com.sh.coffeeshop.dao.OrderDao;
import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.dao.manager.DaoPropertiesParameter;
import com.sh.coffeeshop.dao.manager.DaoPropertiesResourceManager;
import com.sh.coffeeshop.model.Order;
import com.sh.coffeeshop.model.OrderItem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository("OrderDaoImpl")
public class OrderDaoImpl implements OrderDao {

    private static final Logger log = LogManager.getRootLogger();

    /**
     * path for root repository
     */
    private static String ROOT_PATH = DaoPropertiesResourceManager.getInstance().getValue(DaoPropertiesParameter.FOLDER_PATH);

    /**
     * name file orders
     */
    private static String LIST_ORDER = "listOrder";

    /**
     * get Order by id
     * @param id
     * @return
     * @throws DaoException
     */
    @Override
    public synchronized Order get(Long id) throws DaoException {

        Order order = null;
        List<Order> list = list();

        for (Order orderFromList : list){
            if(orderFromList.getId().longValue() == id.longValue()){
                order = orderFromList;
                break;
            }
        }
        return order;
    }

    /**
     * save Order
     * @param order
     * @return
     * @throws DaoException
     */
    @Override
    public synchronized Long save(Order order) throws DaoException {

        Long idOrder;

        List<Order> list = list();
        String path = ROOT_PATH + File.separator + LIST_ORDER;

        //determine id order
        if (list.isEmpty()){
            idOrder = new Long(1);
        } else {
            Order orderForGetId = list.get(list.size() - 1);
            OrderItem orderItemForGetId = orderForGetId.getItems().get(orderForGetId.getItems().size() - 1);
            idOrder = orderItemForGetId.getId() + 1;
        }
        order.setId(idOrder);

        //determine id orderItems
        for (int i = 0; i < order.getItems().size(); i++){
            OrderItem item = order.getItems().get(i);
            item.setId(new Long(idOrder + i + 1));
            item.setOrder(order);
        }
        //set Order status "order not executed"
        order.setStatus(0);

        list.add(order);

        //write Orders
        try(FileOutputStream fileOutputStream= new FileOutputStream(path);
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(list);

        }catch(IOException e){
            log.error("Fail in save(Order order)");
            throw new DaoException("IOException", e);
        }
        return idOrder;
    }

    /**
     * get list of orders
     * @return
     * @throws DaoException
     */
    @Override
    public synchronized List<Order> list() throws DaoException {

        List<Order> list;
        String path = ROOT_PATH + File.separator + LIST_ORDER;

        try(FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);)
        {
            list = (ArrayList) objectInputStream.readObject();
        }catch(FileNotFoundException ioe){
            list = new ArrayList<>();
            log.info("list.size == 0");
        }catch(IOException ioe){
            log.error("Fail in list()");
            throw new DaoException("IOException", ioe);
        }catch(ClassNotFoundException c){
            log.error("Fail in list()");
            throw new DaoException("ClassNotFoundException", c);
        }
        return list;
    }

    /**
     * set order status "completed"
     * @param orderId
     * @throws DaoException
     */
    @Override
    public synchronized void updateStatusOrder(Long orderId) throws DaoException {

        List<Order> list = list();

        //set Order status "order executed"
        for (Order orderFromList : list){
            if(orderFromList.getId().longValue() == orderId.longValue()){
                orderFromList.setStatus(1);
                break;
            }
        }

        String path = ROOT_PATH + File.separator + LIST_ORDER;

        //write Orders
        try(FileOutputStream fileOutputStream= new FileOutputStream(path);
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(list);

        }catch(IOException e){
            log.error("Fail in deliverOrder(Long orderId)");
            throw new DaoException("IOException", e);
        }
    }
}
