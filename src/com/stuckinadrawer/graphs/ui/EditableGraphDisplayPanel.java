package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.graphs.Graph;
import com.stuckinadrawer.graphs.Vertex;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditableGraphDisplayPanel extends SimpleGraphDisplayPanel {

    private EditProductionWindow container;

    public EditableGraphDisplayPanel(Graph graph, int width, int height, EditProductionWindow container) {
        super(graph, width, height);
        this.container = container;
        CustomMouseClickListener customMouseListener = new CustomMouseClickListener();
        addMouseListener(customMouseListener);
    }



    private class CustomMouseClickListener implements MouseListener{

        private Vertex markedVertex;

        @Override
        public void mouseClicked(MouseEvent e) {
            int mouseX = e.getX() - offsetX;
            int mouseY = e.getY() - offsetY;

            Vertex v = getVertexOnPosition(mouseX, mouseY);
            System.out.println("Mouse Clicked");

            switch (container.getClickMode()){
                case NONE:

                    break;
                case ADD_EDGE:
                    if(v!=null) {
                        System.out.println("Clicked on Vertex: " + v.getLabel());
                        if (markedVertex == null) {
                            markedVertex = v;
                            v.marked = true;
                        } else {
                            if (v.equals(markedVertex)) {
                                markedVertex.marked = false;
                                markedVertex = null;
                            } else {
                                markedVertex.marked = false;
                                graph.addEdge(v, markedVertex);
                                markedVertex = null;
                            }

                        }
                    }
                    break;
                case ADD_VERTEX:
                    if(v == null) {
                        String vertexSelection = container.getCurrentVertexSelection();
                        if(vertexSelection!= null && !vertexSelection.equals("-")){
                            Vertex vertex = container.getVertexFactory().createNewVertex(vertexSelection);
                            vertex.setPosition(mouseX, mouseY);
                            graph.addVertex(vertex);
                        }

                    }
                    break;
                case REMOVE_EDGE:
                    if(v!=null) {
                        System.out.println("Clicked on Vertex: " + v.getLabel());
                        if (markedVertex == null) {
                            markedVertex = v;
                            v.marked = true;
                        } else {
                            if (v.equals(markedVertex)) {
                                markedVertex.marked = false;
                                markedVertex = null;
                            } else {
                                markedVertex.marked = false;
                                graph.deleteEdge(v, markedVertex);
                                markedVertex = null;
                            }

                        }
                    }
                    break;
                case REMOVE_VERTEX:
                    if(v!=null) {
                        graph.deleteVertex(v);
                    }
                    break;
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
