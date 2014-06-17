package com.stuckinadrawer.graphs;

import com.stuckinadrawer.GraphFactory;

import java.io.Serializable;
import java.util.ArrayList;

public class Grammar implements Serializable{

    private ArrayList<Production> productions;
    private String name;
    private Graph startingGraph = null;

    public int currentMaxVertexId = 0;

    public Grammar(){
        productions = new ArrayList<Production>();
        name = null;

        GraphFactory graphFactory = new GraphFactory();
        startingGraph = graphFactory.createStartGraph();
    }

    public ArrayList<Production> getProductions() {
        return productions;
    }

    public void setProductions(ArrayList<Production> productions) {
        this.productions = productions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Graph getStartingGraph() {
        return startingGraph;
    }

    public void setStartingGraph(Graph startingGraph) {
        this.startingGraph = startingGraph;
    }

    public void addProduction(Production production){
        if(!productions.contains(production)){
            productions.add(production);
        }
    }

    public void removeProduction(Production production){
        if(productions.contains(production)){
            productions.remove(production);
        }
    }
}
