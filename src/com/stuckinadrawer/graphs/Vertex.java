package com.stuckinadrawer.graphs;

import java.io.Serializable;

public class Vertex implements Serializable{
    private static int currentMaxId = 0;
    private String label;
    private int id;
    protected int x, y;
    public float forceX, forceY = 0;

    public boolean marked = false;

    public Vertex(String label) {
        this.label = label;
        id = currentMaxId++;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
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
        return label.hashCode();
    }

    public void applyForce(double stepSize) {
        this.x += forceX * stepSize;
        this.y += forceY * stepSize;
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;

    }
}
