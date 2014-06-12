package com.stuckinadrawer.graphs;

import java.io.Serializable;

public class Vertex implements Serializable{
    private static int currentMaxId = 0;
    private String type;
    private int label = -1;
    private int id;
    protected int x, y;
    public float forceX, forceY = 0;

    public boolean marked = false;

    public Vertex(String type) {
        this.type = type;
        id = currentMaxId++;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public int getLabel() {
        return label;
    }

    public String getDescription(){
        return getId()+":"+getType();
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Vertex)) {
            return false;
        }
        Vertex other = (Vertex) obj;

        return this.id == other.id;
    }


    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public void applyForce(double stepSize) {
        this.x += forceX * stepSize;
        this.y += forceY * stepSize;
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;

    }

    @Override
    public String toString(){
        return getId()+":"+getType();
    }
}
