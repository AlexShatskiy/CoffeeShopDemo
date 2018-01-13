package com.sh.coffeeshop.dao.impl.file;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.exception.DaoException;
import com.sh.coffeeshop.dao.manager.DaoPropertiesParameter;
import com.sh.coffeeshop.dao.manager.DaoPropertiesResourceManager;
import com.sh.coffeeshop.model.Coffee;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository("CoffeeDaoImpl")
public class CoffeeDaoImpl implements CoffeeDao {

    private static final Logger log = LogManager.getRootLogger();

    /**
     * path for root repository
     */
    private static String ROOT_PATH = DaoPropertiesResourceManager.getInstance().getValue(DaoPropertiesParameter.FOLDER_PATH);

    /**
     * name file list of coffee
     */
    private static String LIST_COFFEE = "listCoffee";

    /**
     * get coffee by id
     * @param id
     * @return
     * @throws DaoException
     */
    @Override
    public Coffee get(Long id) throws DaoException {

        List<Coffee> list = list();
        Coffee coffee = null;

        for (Coffee coffeeFromList : list){
            if(coffeeFromList.getId().longValue() == id.longValue()){
                coffee = coffeeFromList;
            }
        }
        return coffee;
    }

    /**
     * save new Coffee in list of coffee
     * @param coffee
     * @return
     * @throws DaoException
     */
    @Override
    public Long save(Coffee coffee) throws DaoException {

        List<Coffee> list = list();
        String path = ROOT_PATH + File.separator + LIST_COFFEE;

        Long id = new Long(list.size() + 1);
        coffee.setId(id);

        list.add(coffee);

        //write coffee
        try(FileOutputStream fileOutputStream= new FileOutputStream(path);
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(list);

        }catch(IOException e){
            log.error("Fail in save(Coffee coffee)");
            throw new DaoException("IOException", e);
        }
        return id;
    }

    /**
     * get list of coffee
     * @return
     * @throws DaoException
     */
    @Override
    public List<Coffee> list() throws DaoException{

        List<Coffee> list;
        String path = ROOT_PATH + File.separator + LIST_COFFEE;

        try(FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);)
        {
            list = (ArrayList) objectInputStream.readObject();
        }catch(FileNotFoundException ioe){
            list = new ArrayList<>();
            log.info("list.size == 0");
        }catch(IOException ioe){
            log.error("Fail in List<Coffee> list()");
            throw new DaoException("IOException", ioe);
        }catch(ClassNotFoundException c){
            log.error("Fail in List<Coffee> list()");
            throw new DaoException("ClassNotFoundException", c);
        }
        return list;
    }
}
