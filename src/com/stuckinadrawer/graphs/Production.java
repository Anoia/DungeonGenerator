package com.stuckinadrawer.graphs;

public class Production {
    private Graph left;
    private Graph right;

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
}
