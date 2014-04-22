package com.stuckinadrawer.graphs;

import com.stuckinadrawer.FileReader;
import com.stuckinadrawer.Utils;

import java.util.ArrayList;

public class VertexFactory {
    String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    ArrayList<String> terminals;

    public VertexFactory(){
        FileReader fr = new FileReader();
        terminals = fr.getTerminals();
    }


    public Vertex createNewVertex(){
        //int index = Utils.random(abc.length()-1);
        //String label = abc.charAt(index)+"";

        int index = Utils.random(terminals.size()-1);
        String label = terminals.get(index);
        return new Vertex(label);
    }
}
