package com.stuckinadrawer;

import com.stuckinadrawer.graphs.Graph;
import com.stuckinadrawer.graphs.GraphRenderer;
import com.stuckinadrawer.graphs.Layout;
import com.stuckinadrawer.graphs.Vertex;

import java.util.HashSet;

public class Test {

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

        Graph g = new Graph(edges);
        GraphRenderer renderer = new GraphRenderer();
        renderer.setGraph(g);
        System.out.println(g.toString());

        Layout layout = new Layout(g);
        while(true){
            Thread.sleep(1000/64);
            layout.step();
            renderer.update();
        }


    }



    public static void main(String[] args) throws InterruptedException {
        new Test();
    }

}
