package com.stuckinadrawer;

import com.stuckinadrawer.graphs.*;

import java.util.ArrayList;
import java.util.HashSet;

public class GraphFactory {

    VertexFactory factory = new VertexFactory();

    public Graph createRandomGraph(){
        int amountOfVertices = Utils.random(2, 6);
        HashSet<Vertex> vertices = new HashSet<Vertex>();
        ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
        for(int i = 0; i < amountOfVertices; i++){
            Vertex v = factory.createNewVertex();
            vertices.add(v);
            vertexList.add(v);
        }
        HashSet<HashSet<Vertex>> edges = new HashSet<HashSet<Vertex>>();
        int amountOfEdges = Utils.random(amountOfVertices, amountOfVertices+3);
        for(int i = 0; i < amountOfEdges; i++){
            HashSet<Vertex> edge = new HashSet<Vertex>();
            Vertex v1 = getRandomVertexFromList(vertexList);
            Vertex v2 = getRandomVertexFromList(vertexList);
            while(v1.equals(v2)){
                v2 = getRandomVertexFromList(vertexList);
            }
            edge.add(v1);
            edge.add(v2);
            edges.add(edge);
        }

        return new Graph(edges, vertices);
    }

    public Graph createStartGraph(){
        Vertex entrance = factory.createNewVertex("entrance");
        Vertex exit = factory.createNewVertex("exit");
        Vertex dungeon = factory.createNewVertex("dungeon");
        Graph graph = new Graph();
        graph.addVertex(entrance);
        graph.addVertex(exit);
        graph.addVertex(dungeon);
        graph.addEdge(entrance, dungeon);
        graph.addEdge(dungeon, exit);
        return graph;
    }

    public Vertex getRandomVertexFromList(ArrayList<Vertex> list){
        int index = Utils.random(list.size()-1);
        return list.get(index);
    }


}
