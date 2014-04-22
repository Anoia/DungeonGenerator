package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.Point;
import com.stuckinadrawer.graphs.Graph;
import com.stuckinadrawer.graphs.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GraphDisplayPanel extends JPanel{

    private final int width;
    private final int height;
    private int offsetX;
    private int offsetY;

    private Graph graph;

    private final int vertexRadius = 20;

    public GraphDisplayPanel(Graph graph, int width, int height) {
        super();
        this.graph = graph;
        this.width = width;
        this.height = height;
        offsetX = width/2;
        offsetY = height/2;
    }

    public GraphDisplayPanel(int width, int height){
        this(null, width, height);
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
            Color c = Color.black;
            if(vertex.marked){
                c = Color.red;
            }
            circeWithText(g2d, vertex.getLabel(), vertex.getX(), vertex.getY(), c);
        }
    }

    public void circeWithText(Graphics2D g, String vertex, int x, int y, Color c){
        g.setColor(java.awt.Color.WHITE);
        g.fillOval(x- vertexRadius, y- vertexRadius, 2* vertexRadius, 2* vertexRadius);
        g.setColor(c);
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
