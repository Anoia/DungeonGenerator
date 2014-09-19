package com.stuckinadrawer.graphs;

import com.stuckinadrawer.GraphFactory;
import com.stuckinadrawer.ObjectCloner;
import com.stuckinadrawer.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Grammar implements Serializable{

    private ArrayList<Production> productions;
    private String name;
    private Graph startingGraph = null;
    private Graph graph = null;

    public int currentMaxVertexId = 0;

    public Grammar(){
        productions = new ArrayList<Production>();
        name = null;

        GraphFactory graphFactory = new GraphFactory();
        startingGraph = graphFactory.createStartGraph();
        resetGraph();
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
        resetGraph();

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

    public boolean applyRandomProduction(){

        Production selectedProduction = getRandomProduction();
        if(selectedProduction == null){
            return false;
        }


        HashMap <Vertex, Vertex> morphism = findProductionInGraph(selectedProduction);
        int x, y, num, xVal, yVal;
        xVal = 0;
        yVal = 0;
        num = 0;
        Iterator it = morphism.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Vertex v = (Vertex) pairs.getValue();
            xVal += v.getX();
            yVal += v.getY();
            num++;
           // it.remove(); // avoids a ConcurrentModificationException
        }
        x = xVal/num;
        y = yVal/num;

        Production productionWithoutWildcard = createNewProductionWithoutWildcard(selectedProduction, morphism);
        new SinglePushOut().applyProduction(productionWithoutWildcard, graph, morphism);
        graph.setVertexPosition(x, y);

        return true;
    }

    public void resetGraph(){
        try {
            graph = (Graph) ObjectCloner.deepCopy(startingGraph);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void applyAllProductions(){
        boolean doingStuff = true;
        while(doingStuff){
            doingStuff = applyRandomProduction();
        }
    }

    public Graph getGraph(){
        return graph;
    }


    private Production getRandomProduction() {
        System.out.println("looking for random production");
        ArrayList<Production> possibleProductions = new ArrayList<Production>();
        for(Production p: getProductions()){
            if(findProductionInGraph(p)!=null){
                possibleProductions.add(p);
            }
        }
        if(!possibleProductions.isEmpty()){
            int rand = Utils.random(possibleProductions.size() - 1);
            System.out.println("found "+possibleProductions.size()+"possible prods; chose nr"+rand);
            return possibleProductions.get(rand);
        }
        return null;

    }
    private HashMap<Vertex, Vertex> findProductionInGraph(Production selectedProduction) {
        Graph subGraph = selectedProduction.getLeft();
        SubGraphIsomorphism finder = new SubGraphIsomorphism();
        return finder.findIsomorphism(graph, subGraph);
    }


    private Production createNewProductionWithoutWildcard(Production selectedProduction, HashMap<Vertex, Vertex> morphism) {
        System.out.println("\n ###WILDCARDS### ");
        Production newProduction;
        try {
            newProduction = (Production) ObjectCloner.deepCopy(selectedProduction);
        } catch (Exception e) {
            e.printStackTrace();
            return selectedProduction;
        }

        //remove wildcards from left side
        for(Vertex vertexInLeftSide: newProduction.getLeft().getVertices()){
            if(vertexInLeftSide.getType().equals("*")){
                Vertex assigenedVertexInHost = morphism.get(vertexInLeftSide);
                morphism.remove(vertexInLeftSide);
                vertexInLeftSide.setType(assigenedVertexInHost.getType());
                morphism.put(vertexInLeftSide, assigenedVertexInHost);
                System.out.println("WILDCARD REMOVED FROM: "+vertexInLeftSide.getDescription());
            }
        }

        //remove wildcards from right side
        for(Vertex vertexInRightSide: newProduction.getRight().getVertices()){
            if(vertexInRightSide.getType().equals("*")){
                int morphRight = vertexInRightSide.getMorphism();
                for(Vertex vertexInLeftSide: newProduction.getLeft().getVertices()){
                    if(vertexInLeftSide.getMorphism() == morphRight){
                        System.out.println("WIlDCARD REMOVED FROM "+vertexInRightSide.getDescription());
                        System.out.println("set to: "+vertexInLeftSide.getType());
                        vertexInRightSide.setType(vertexInLeftSide.getType());
                    }
                }


            }
        }

        return newProduction;
    }
}
