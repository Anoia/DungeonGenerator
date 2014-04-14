package com.stuckinadrawer.graphs;

import java.io.Serializable;

public class Vertex implements Serializable{
    private String label;
    protected int x, y;
    public float forceX, forceY = 0;

    public Vertex(String label) {
        this.label = label;
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

        return label.equals(other.getLabel());
    }


    @Override
    public int hashCode() {
        return label.hashCode();
    }

    public void applyForce(double stepSize) {
        this.x += forceX * stepSize;
        this.y += forceY * stepSize;
    }
}
