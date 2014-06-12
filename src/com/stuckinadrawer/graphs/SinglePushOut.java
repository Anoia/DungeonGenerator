package com.stuckinadrawer.graphs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SinglePushOut {

    private final VertexFactory vertexFactory;

    public SinglePushOut(){
        this.vertexFactory = new VertexFactory();
    }

    public void applyProduction(Production p, Graph g, HashMap<Vertex, Vertex> morphism){
        System.out.println("applying prod");

        //entferne alle nodes aus Host die in Plinks aber nicht in Prechts enthalten sind
        for(Vertex v: p.getLeft().getVertices()){
            System.out.println("check if "+v.toString()+" needs to be deleted");
            int morph = v.getId();
            boolean isInPrechts = false;
            for(Map.Entry<Vertex, Vertex> entry : morphism.entrySet()){
                Vertex vInHost = entry.getValue();
                if(morph == vInHost.getId()){
                    isInPrechts = true;
                    break;
                }

            }
            System.out.println("deletion of "+v.toString()+" "+ !isInPrechts);
            if(!isInPrechts){
                g.deleteVertex(morphism.get(v));
            }

        }


        // Füge alle nodes aus Prechts hinzu, die in Host noch nicht enthalten sind

        for(Vertex vRechts : p.getRight().getVertices()){
            int morphRechts = vRechts.getId();
            boolean isInPlinks = false;
            for(Vertex vLinks : p.getLeft().getVertices()){
                if(vLinks.getId() == morphRechts){
                    isInPlinks = true;
                    break;
                }
            }
            System.out.println(vRechts.toString()+" is In links? "+isInPlinks);

            if(!isInPlinks){
                //add to host
                Vertex newVertex = vertexFactory.createNewVertex(vRechts.getType());
                newVertex.setId(g.getHighestVertexId()+1);
                g.addVertex(newVertex);

                //add to morphism to check for missing edges in Host later
                morphism.put(vRechts, newVertex);

            }
        }

        // füge alle Kanten aus Prechts hinzu, die in Host noch nicht enthalten sind
        for(HashSet<Vertex> edge: p.getRight().getEdges()){
            Vertex v1Prod = null;
            Vertex v2Prod = null;
            for(Vertex v: edge){
                if(v1Prod==null){
                    v1Prod = v;
                }else{
                    v2Prod=v;
                }
            }

            Vertex v1Graph = morphism.get(v1Prod);
            Vertex v2Graph = morphism.get(v2Prod);


            if(!g.hasEdge(v1Graph, v2Graph)){
                //wenn es in g noch keine edge zwischen den beiden gibt, füg hinzu
                g.addEdge(v1Graph, v2Graph);
            }

        }


        System.out.println(g.toString());

    }

}
