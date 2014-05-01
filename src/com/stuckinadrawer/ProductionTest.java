package com.stuckinadrawer;

import com.stuckinadrawer.graphs.Production;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ProductionTest {

    public ProductionTest(){
        RandomGraphFactory graphFactory = new RandomGraphFactory();
        Production p = new Production(graphFactory.createRandomGraph(), graphFactory.createRandomGraph());
        System.out.println(p.toString());
        String fileName = "production.ser";

        serialize(p, fileName);

        Production p2 = deSerialize(fileName);
        System.out.println(p2.toString());


    }

    private Production deSerialize(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        Production p = null;
        try {
            fis = new FileInputStream(fileName);
            in = new ObjectInputStream(fis);
            p = (Production) in.readObject();
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p;
    }

    public void serialize(Production p, String fileName){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(p);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public static void main(String[] arg){
        new ProductionTest();
    }
}
