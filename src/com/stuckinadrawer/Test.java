package com.stuckinadrawer;

import com.stuckinadrawer.graphs.*;

import java.util.ArrayList;
import java.util.HashSet;

public class Test {

    VertexFactory factory = new VertexFactory();

    public Test() throws InterruptedException {
        Vertex a = new Vertex("A");
        Vertex b = new Vertex("B");
        Vertex c = new Vertex("C");
        Vertex d = new Vertex("D");
        Vertex e = new Vertex("E");
        Vertex f = new Vertex("F");

        HashSet<HashSet<Vertex>> edges = new HashSet<HashSet<Vertex>>();

        HashSet<Vertex> edge = new HashSet<Vertex>();
        edge.add(e);
        edge.add(d);
        edges.add(edge);

        edge = new HashSet<Vertex>();
        edge.add(c);
        edge.add(b);
       // edges.add(edge);
        edge = new HashSet<Vertex>();
        edge.add(c);
        edge.add(d);
        edges.add(edge);
        edge = new HashSet<Vertex>();
        edge.add(a);
        edge.add(d);
        edges.add(edge);
        edge = new HashSet<Vertex>();
        edge.add(f);
        edge.add(a);
        edges.add(edge);
        edge = new HashSet<Vertex>();
        edge.add(e);
        edge.add(b);
        edges.add(edge);
        edge = new HashSet<Vertex>();
        edge.add(c);
        edge.add(a);
        edges.add(edge);

      //  Graph g = new Graph(edges);
        Graph g = createRandomGraph();
        System.out.println(g.toString());
        GraphRenderer renderer = new GraphRenderer();
        renderer.setGraph(g);


        ForceBasedLayout forceBasedLayout = new ForceBasedLayout(g);
        while(true){
            Thread.sleep(1000/64);
            forceBasedLayout.step();
            renderer.update();
        }


    }

    public Graph createRandomGraph(){
        int amountOfVertices = Utils.random(5, 15);
        HashSet<Vertex> vertices = new HashSet<Vertex>();
        ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
        for(int i = 0; i < amountOfVertices; i++){
            Vertex v = factory.createNewVertex();
            vertices.add(v);
            vertexList.add(v);
        }
        HashSet<HashSet<Vertex>> edges = new HashSet<HashSet<Vertex>>();
        int amountOfEdges = Utils.random(amountOfVertices, amountOfVertices+10);
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

    public Vertex getRandomVertexFromList(ArrayList<Vertex> list){
        int index = Utils.random(list.size()-1);
        return list.get(index);
    }

    public static void main(String[] args) throws InterruptedException {
        new Test();
    }

}
