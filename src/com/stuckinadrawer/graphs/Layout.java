package com.stuckinadrawer.graphs;

public class Layout {

    private Graph graph;

    private static final int REPULSION = 200000;
    private static final int SPRING_LENGTH = 60;
    private static final double STEP_SIZE = 0.0005;


    public Layout(Graph g){
        this.graph = g;
    }

    public void step(){

        //calculate force on all vertices
        for(Vertex vertex1: graph.getVertices()){
            vertex1.forceX = 0;
            vertex1.forceY = 0;
            for(Vertex vertex2: graph.getVertices()){
                if(!vertex2.equals(vertex1)){
                    double deltaX = vertex2.getX() - vertex1.getX();
                    double deltaY = vertex2.getY() - vertex1.getY();
                    double delta2 = deltaX * deltaX + deltaY * deltaY;

                    //Add some jitter if distance² is very small
                    if(delta2 < 0.01){
                        System.out.println("Too Close! Jitter!");
                        deltaX = 0.1 * Math.random() + 0.1;
                        deltaY = 0.1 * Math.random() + 0.1;
                        delta2 = deltaX * deltaX + deltaY * deltaY;
                    }

                    //Coulomb's Law -- repulsion varies inversely with square of distance
                    vertex1.forceX -= (REPULSION / delta2) * deltaX;
                    vertex1.forceY -= (REPULSION / delta2) * deltaY;

                    //Hooke's Law -- spring force along edges
                    if(graph.hasEdge(vertex1, vertex2)){
                        double distance = Math.sqrt(delta2);
                        vertex1.forceX += (distance - SPRING_LENGTH) * deltaX;
                        vertex1.forceY += (distance - SPRING_LENGTH) * deltaY;
                    }


                }
            }

        }

        //apply calculated forces
        for(Vertex vertex: graph.getVertices()){
            vertex.applyForce(STEP_SIZE);

        }

    }
}
