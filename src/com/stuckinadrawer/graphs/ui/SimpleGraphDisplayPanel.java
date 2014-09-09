package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.graphs.Graph;
import com.stuckinadrawer.graphs.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class SimpleGraphDisplayPanel extends JPanel{

    private int width;
    private int height;

    protected int offsetX;
    protected int offsetY;

    protected Graph graphToDisplay;

    private final int vertexRadius = 20;

    public SimpleGraphDisplayPanel(Graph graph, int width, int height) {
        super();
        this.graphToDisplay = graph;
        this.width = width;
        this.height = height;
        offsetX = width/2;
        offsetY = height/2;



    }

    public SimpleGraphDisplayPanel(Graph g){
        super();
    }

    public SimpleGraphDisplayPanel(){
        this(null);
    }

    public SimpleGraphDisplayPanel(int width, int height){
        this(null, width, height);
    }

    public void updateSize(){
        this.width = getSize().width;
        this.height = getSize().height;
        offsetX = width/2;
        offsetY = height/2;
        System.out.println("UPDATE SIZE: "+this.width+" "+this.height);
    }


    private void draw(Graphics g){
       // g.setFont(new Font("default", Font.BOLD, 16));
        //this is where the drawing happens
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);

        g2d.translate(offsetX, offsetY);

        if(graphToDisplay != null){
            drawEdges(g2d);
            drawVertices(g2d);
        }
        g2d.translate(-offsetX, -offsetY);


    }

    private void drawEdges(Graphics2D g2d) {
        if(!graphToDisplay.getEdges().isEmpty()){
            for(HashSet<Vertex> edge: graphToDisplay.getEdges()){
                ArrayList<Integer> positions = new ArrayList<Integer>();
                for(Vertex vertex: edge){
                    positions.add(vertex.getX());
                    positions.add(vertex.getY());
                }

                g2d.drawLine(positions.get(0), positions.get(1), positions.get(2), positions.get(3));
            }
        }

    }

    public void drawVertices(Graphics2D g2d){
        for(Vertex vertex: graphToDisplay.getVertices()){
            Color c = Color.black;
            if(vertex.marked){
                c = Color.red;
            }
            circeWithText(g2d, vertex.toString(), vertex.getX(), vertex.getY(), c);
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


    public Vertex getVertexOnPosition(int x, int y){
        for(Vertex v: graphToDisplay.getVertices()){
            int distanceX = x - v.getX();
            int distanceY = y - v.getY();
            int distance2 = distanceX*distanceX + distanceY*distanceY;
            if(distance2 < vertexRadius*vertexRadius){
                return v;
            }
        }
        return null;

    }

    public void setGraphToDisplay(Graph graphToDisplay) {
        this.graphToDisplay = graphToDisplay;
        repaint();
    }

    public void clear() {
        this.graphToDisplay = null;
        repaint();
    }





}
