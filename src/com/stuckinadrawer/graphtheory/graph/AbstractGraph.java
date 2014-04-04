package com.stuckinadrawer.graphtheory.graph;

import com.stuckinadrawer.graphtheory.Graph;

public abstract class AbstractGraph<V, E>  implements Graph<V, E>{

    @Override
    public E getEdge(V sourceVertex, V targetVertex){
        return null;
    }

    @Override
    public boolean containsEdge(V sourceVertex, V targetVertex)
    {
        return getEdge(sourceVertex, targetVertex) != null;
    }

}
