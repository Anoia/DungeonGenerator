package com.stuckinadrawer.graphtheory;

import java.util.Collection;
import java.util.Set;

public interface Graph<V, E> {

    public E getEdge(V sourceVertex, V targetVertex);

    public EdgeFactory<V, E> getEdgeFactory();

    public E addEdge(V sourceVertex, V targetVertex);

    public boolean addEdge(V sourceVertex, V targetVertex, E e);

    public boolean addVertex(V v);

    public boolean containsEdge(V sourceVertex, V targetVertex);

    public boolean containsEdge(E e);

    public boolean containsVertex(V v);

    public Set<E> edgeSet();

    public Set<E> edgesOf(V vertex);

    public Set<V> vertexSet();


    public boolean removeAllEdges(Collection<? extends E> edges);

    public Set<E> removeAllEdges(V sourceVertex, V targetVertex);

    public boolean removeAllVertices(Collection<? extends V> vertices);

    public E removeEdge(V sourceVertex, V targetVertex);

    public boolean removeEdge(E e);

    public boolean removeVertex(V v);

}
