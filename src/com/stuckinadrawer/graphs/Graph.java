package com.stuckinadrawer.graphs;

import com.stuckinadrawer.Utils;

import java.io.Serializable;
import java.util.HashSet;

public class Graph implements Serializable{

    private HashSet<HashSet<Vertex>> edges;
    private HashSet<Vertex> vertices;

    public Graph(HashSet<HashSet<Vertex>> edges, HashSet<Vertex> vertices){
        this.vertices = new HashSet<Vertex>();
        for(Vertex vertex: vertices){
            addVertex(vertex);
        }
        this.edges = new HashSet<HashSet<Vertex>>();
        for(HashSet<Vertex> edge: edges){
            // vertices in edge will be added too
            addEdge(edge);
        }


    }

    public Graph(HashSet<HashSet<Vertex>> edges){
        this(edges, new HashSet<Vertex>());
    }

    public Graph(){
        vertices = new HashSet<Vertex>();
        edges = new HashSet<HashSet<Vertex>>();
    }

   /*
    public Graph(HashSet<Vertex> vertices){
        this(new HashSet<HashSet<Vertex>>(), vertices);
    }*/


    public void addEdge(Vertex v1, Vertex v2){
        HashSet<Vertex> edge = new HashSet<Vertex>();
        edge.add(v1);
        edge.add(v2);
        addEdge(edge);
    }

    public void addEdge(HashSet<Vertex> edge){
        for(Vertex vertex: edge){
            vertices.add(vertex);
        }
        this.edges.add(edge);
    }

    public void addVertex(Vertex vertex){
        this.vertices.add(vertex);
    }

    public void addVertices(HashSet<Vertex> vertices){
        for(Vertex vertex: vertices){
            addVertex(vertex);
        }
    }

    public HashSet<Vertex> getVertices(){
        return this.vertices;
    }

    public HashSet<HashSet<Vertex>> getEdges(){
        return this.edges;
    }

    public HashSet<HashSet<Vertex>> getIncidentEdges(Vertex vertex){
        HashSet<HashSet<Vertex>> result = new HashSet<HashSet<Vertex>>();
        for(HashSet<Vertex> edge: this.edges){
            if(edge.contains(vertex)){
                result.add(edge);
            }
        }
        return result;
    }

    public void deleteEdge(HashSet<Vertex> edge){
        this.edges.remove(edge);
    }

    public void deleteEdge(Vertex v1, Vertex v2){
        HashSet<Vertex> edge = new HashSet<Vertex>();
        edge.add(v1);
        edge.add(v2);
        deleteEdge(edge);
    }


    public void deleteVertex(Vertex vertex){
        for(HashSet<Vertex> edge: getIncidentEdges(vertex)){
            this.edges.remove(edge);
        }
        this.vertices.remove(vertex);
    }

    public HashSet<Vertex> getNeighbors(Vertex vertex){
        HashSet<Vertex> result = new HashSet<Vertex>();
        for(Vertex v: this.vertices){
            HashSet<Vertex> edge = new HashSet<Vertex>();
            edge.add(vertex);
            edge.add(v);
            if(hasEdge(edge)){
                result.add(v);
            }
        }
        return result;
    }

    public int getDegree(Vertex vertex){
        return getNeighbors(vertex).size();
    }

    public boolean hasVertex(Vertex vertex){
        return this.vertices.contains(vertex);
    }

    public boolean hasEdge(HashSet<Vertex> edge){
        return this.edges.contains(edge);
    }

    public boolean hasEdge(Vertex v1, Vertex v2){
        HashSet<Vertex> edge = new HashSet<Vertex>();
        edge.add(v1);
        edge.add(v2);
        return hasEdge(edge);
    }

    public void setRandomVertexPosition(int width, int height){
        for(Vertex vertex: vertices){
            vertex.setPosition(Utils.random(width)-width/2, Utils.random(height)-height/2);
        }
    }

    @Override
    public String toString(){
        String result = "";
        result += "Vertices: ";
        for(Vertex v: vertices){
            result += v.getId()+":"+v.getType()+" ";
        }
        result += "\n Edges: ";
        for(HashSet<Vertex> edge: edges){
            result+=" [";
            for(Vertex v: edge){
                result+=v.getId()+":"+v.getType();
            }
            result+="] ";
        }
        return result;
    }

    public HashSet<Vertex> getVerticesByType(String type){
        HashSet<Vertex> result = new HashSet<Vertex>();
        for(Vertex v: vertices){
            if(v.getType().equals(type)) result.add(v);
        }
        return result;
    }



}
