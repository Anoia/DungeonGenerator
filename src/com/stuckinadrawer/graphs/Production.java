package com.stuckinadrawer.graphs;

public class Production {
    private Graph left;
    private Graph right;
    private String name;

    public Production(Graph left, Graph right) {
        this.left = left;
        this.right = right;
    }

    public Graph getLeft() {
        return left;
    }

    public void setLeft(Graph left) {
        this.left = left;
    }

    public Graph getRight() {
        return right;
    }

    public void setRight(Graph right) {
        this.right = right;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
