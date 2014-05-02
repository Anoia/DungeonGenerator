package com.stuckinadrawer.graphs;

import java.util.ArrayList;

public class Grammar {

    private ArrayList<Production> productions;
    private String name;
    private Graph startingGraph;

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
}
