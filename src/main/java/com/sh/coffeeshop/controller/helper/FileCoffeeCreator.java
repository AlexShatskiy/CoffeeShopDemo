package com.sh.coffeeshop.controller.helper;

import com.sh.coffeeshop.dao.manager.DaoPropertiesParameter;
import com.sh.coffeeshop.dao.manager.DaoPropertiesResourceManager;
import com.sh.coffeeshop.model.Coffee;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class FileCoffeeCreator {

    private static final Logger log = LogManager.getRootLogger();

    private FileCoffeeCreator(){}

    public static void createCoffeeList(){
        String rootPath = DaoPropertiesResourceManager.getInstance().getValue(DaoPropertiesParameter.FOLDER_PATH);
        String listCoffeeFileName = "listCoffee";

        List<Coffee> list = new ArrayList<>();

        Coffee coffee1 = new Coffee();
        coffee1.setId(new Long(1));
        coffee1.setName("Espresso (Short Black)");
        coffee1.setDescription("The espresso (aka “short black”) is the foundation and the most important part to every espresso based drink.");
        coffee1.setPrice(new BigDecimal(1));

        Coffee coffee2 = new Coffee();
        coffee2.setId(new Long(2));
        coffee2.setName("Double Espresso (Doppio)");
        coffee2.setDescription("A double espresso (aka “Doppio”) is just that, two espresso shots in one cup.");
        coffee2.setPrice(new BigDecimal(2));

        Coffee coffee3 = new Coffee();
        coffee3.setId(new Long(3));
        coffee3.setName("Short Macchiato");
        coffee3.setDescription("A short macchiato is similar to an espresso but with a dollop of steamed milk and foam to mellow the harsh taste of an espresso.");
        coffee3.setPrice(new BigDecimal(3));

        Coffee coffee4 = new Coffee();
        coffee4.setId(new Long(4));
        coffee4.setName("Long Macchiato");
        coffee4.setDescription("A long macchiato is the same as a short macchiato but with a double shot of espresso.");
        coffee4.setPrice(new BigDecimal(4));

        list.add(coffee1);
        list.add(coffee2);
        list.add(coffee3);
        list.add(coffee4);

        String path = rootPath + File.separator + listCoffeeFileName;

        //write coffee
        try(FileOutputStream fileOutputStream= new FileOutputStream(path);
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(list);

        }catch(IOException e){
            log.error("Fail in init");
        }
    }
}
