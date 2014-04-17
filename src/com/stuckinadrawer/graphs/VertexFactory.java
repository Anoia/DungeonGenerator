package com.stuckinadrawer.graphs;

import com.stuckinadrawer.Utils;

public class VertexFactory {
    String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public Vertex createNewVertex(){
        int index = Utils.random(abc.length()-1);
        String label = abc.charAt(index)+"";
        return new Vertex(label);
    }
}
