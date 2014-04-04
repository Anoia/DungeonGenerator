package com.stuckinadrawer.graphtheory;



public interface EdgeFactory<V, E> {

    public E createEdge(V sourceVertex, V targetVertex);

}
