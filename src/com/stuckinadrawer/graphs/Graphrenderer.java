package com.stuckinadrawer.graphs;

import com.stuckinadrawer.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GraphRenderer extends JFrame{

    Graph graph = null;
    MyPanel panel;
    private int width = 800;
    private int height = 600;
    private int offsetX = width/2;
    private int offsetY = height/2;
    private int vertexRadius = 20;

    public GraphRenderer(){

        initGUI();
    }

    private void initGUI() {
        setTitle("Graph");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new MyPanel();
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case 32:
                        break;
                    case 83:
                        break;
                    case 66:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });


    }

    public void setGraph(Graph g) {
        graph = g;
        graph.setRandomVertexPosition(width, height);
        panel.repaint();
    }

    public void update(){
        panel.repaint();
    }

    private class MyPanel extends JPanel {

        private Point dragging = null;
        private Vertex draggedVertex = null;

        private Vertex markedVertex = null;


        public MyPanel(){
            super();
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int mouseX = e.getX() - offsetX;
                    int mouseY = e.getY() - offsetY;
                    Vertex v = getVertexOnPosition(mouseX, mouseY);
                    if (v != null) {
                        System.out.println("Clicked on Vertex: " + v.getLabel());
                        if(markedVertex == null){
                            markedVertex = v;
                        }else{
                            graph.addEdge(v, markedVertex);
                            markedVertex = null;
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                        draggedVertex = getVertexOnPosition(e.getX() - offsetX, e.getY() - offsetY);
                        dragging = new Point(e.getX(), e.getY());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    draggedVertex = null;
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (draggedVertex != null) {
                        int deltaX = e.getX() - dragging.getX();
                        int deltaY = e.getY() - dragging.getY();

                        draggedVertex.move(deltaX, deltaY);

                        dragging = new Point(e.getX(), e.getY());
                        repaint();

                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });
        }

        private Vertex getVertexOnPosition(int x, int y){
            for(Vertex v: graph.getVertices()){
                int distanceX = x - v.getX();
                int distanceY = y - v.getY();
                int distance2 = distanceX*distanceX + distanceY*distanceY;
                if(distance2 < vertexRadius*vertexRadius){
                    return v;
                }
            }
            return null;

        }

        private void draw(Graphics g){

            //this is where the drawing happens
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.BLACK);

            g2d.translate(offsetX, offsetY);

            if(graph != null){
                drawEdges(g2d);
                drawVertices(g2d);
            }
            g2d.translate(-offsetX, -offsetY);


        }

        private void drawEdges(Graphics2D g2d) {
            for(HashSet<Vertex> edge: graph.getEdges()){
                ArrayList<Integer> positions = new ArrayList<Integer>();
                for(Vertex vertex: edge){
                    positions.add(vertex.getX());
                    positions.add(vertex.getY());
                }

                g2d.drawLine(positions.get(0), positions.get(1), positions.get(2), positions.get(3));
            }

        }

        public void drawVertices(Graphics2D g2d){
            for(Vertex vertex: graph.getVertices()){
                circeWithText(g2d, vertex.getLabel(), vertex.getX(), vertex.getY());
            }
        }

        public void circeWithText(Graphics2D g, String vertex, int x, int y){
            g.setColor(Color.WHITE);
            g.fillOval(x- vertexRadius, y- vertexRadius, 2* vertexRadius, 2* vertexRadius);
            g.setColor(Color.BLACK);
            g.drawOval(x- vertexRadius, y- vertexRadius, 2* vertexRadius, 2* vertexRadius);
            g.drawString(vertex, x-3, y+3);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponents(g);
            draw(g);
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(width, height);
        }
    }
}
