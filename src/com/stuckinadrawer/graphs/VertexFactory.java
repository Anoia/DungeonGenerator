package com.stuckinadrawer.graphs;

import com.stuckinadrawer.FileReader;
import com.stuckinadrawer.Utils;

import java.util.ArrayList;

public class VertexFactory {
    String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    ArrayList<String> terminals;
    ArrayList<String> nonTerminals;

    public VertexFactory(){
        FileReader fr = new FileReader();
        terminals = fr.getTerminals();
        nonTerminals = fr.getNonTerminals();
    }


    public Vertex createNewVertex(){
        //int index = Utils.random(abc.length()-1);
        //String label = abc.charAt(index)+"";

        int index = Utils.random(terminals.size()-1);
        String label = terminals.get(index);
        return new Vertex(label);
    }

    public ArrayList<String> getTerminals() {
        return terminals;
    }

    public ArrayList<String> getNonTerminals() {
        return nonTerminals;
    }

    public ArrayList<String> getAllSymbols(){
        ArrayList<String> all = new ArrayList<String>();
        all.add("-");
        all.addAll(nonTerminals);
        all.add("-");
        all.addAll(terminals);
        return all;

    }

    public Vertex createNewVertex(String label){
        return new Vertex(label);
    }
}
