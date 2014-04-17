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

    VertexFactory factory = new VertexFactory();

    private ClickMode mode = ClickMode.NONE;
    private Font defaultFont;
    private Font bigFont;

    public GraphRenderer(){
        defaultFont = new Font("default", Font.PLAIN, 12);
        bigFont = new Font("big", Font.BOLD, 16);
        initGUI();
    }

    private void initGUI() {
        setTitle("Graph");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new MyPanel();
        panel.setFocusable(true);
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        System.out.println("Modes:" +
                "\n N for MoveVertices" +
                "\n V for AddVertex" +
                "\n B for RemoveVertex" +
                "\n E for AddEdge" +
                "\n R for RemoveEdge");



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

        String displayText = "Mode: MoveVertices";

        private Point dragging = null;
        private Vertex draggedVertex = null;

        private Vertex markedVertex = null;


        public MyPanel(){
            super();

            this.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    switch(e.getKeyCode()){
                        case 78:
                            //none
                            displayText = "Mode: MoveVertices";
                            mode = ClickMode.NONE;
                            markedVertex = null;
                            dragging = null;
                            draggedVertex = null;
                            break;
                        case 86:
                            //vertex
                            displayText = "Mode: AddVertices";
                            mode = ClickMode.ADD_VERTEX;
                            markedVertex = null;
                            dragging = null;
                            draggedVertex = null;
                            break;
                        case 69:
                            //edges
                            displayText = "Mode: AddEdges";
                            mode = ClickMode.ADD_EDGE;
                            markedVertex = null;
                            dragging = null;
                            draggedVertex = null;
                            break;
                        case 82:
                            //remove edge
                            displayText = "Mode: RemoveEdges";
                            mode = ClickMode.REMOVE_EDGE;
                            markedVertex = null;
                            dragging = null;
                            draggedVertex = null;
                            break;
                        case 66:
                            //remove vertex
                            displayText = "Mode: RemoveVertices";
                            mode = ClickMode.REMOVE_VERTEX;
                            markedVertex = null;
                            dragging = null;
                            draggedVertex = null;
                            break;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Transform screen coors to actual position
                    int mouseX = e.getX() - offsetX;
                    int mouseY = e.getY() - offsetY;

                    Vertex v = getVertexOnPosition(mouseX, mouseY);

                    switch (mode){
                        case NONE:

                            break;
                        case ADD_EDGE:
                            if (v != null) {
                                System.out.println("Clicked on Vertex: " + v.getLabel());
                                if(markedVertex == null){
                                    markedVertex = v;
                                    v.marked = true;
                                }else{
                                    if(v.equals(markedVertex)){
                                        markedVertex.marked = false;
                                        markedVertex = null;
                                    }else{
                                        markedVertex.marked = false;
                                        graph.addEdge(v, markedVertex);
                                        markedVertex = null;
                                    }

                                }
                            }
                            break;
                        case ADD_VERTEX:
                            if(v == null){
                                Vertex vertex = factory.createNewVertex();
                                vertex.setPosition(mouseX, mouseY);
                                graph.addVertex(vertex);
                            }
                            break;
                        case REMOVE_EDGE:
                            System.out.println("Clicked on Vertex: " + v.getLabel());
                            if(markedVertex == null){
                                markedVertex = v;
                                v.marked = true;
                            }else{
                                if(v.equals(markedVertex)){
                                    markedVertex.marked = false;
                                    markedVertex = null;
                                }else{
                                    markedVertex.marked = false;
                                    graph.deleteEdge(v, markedVertex);
                                    markedVertex = null;
                                }

                            }
                            break;
                        case REMOVE_VERTEX:
                            if (v != null){
                                graph.deleteVertex(v);
                            }
                            break;
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                        if(mode == ClickMode.NONE){
                            draggedVertex = getVertexOnPosition(e.getX() - offsetX, e.getY() - offsetY);
                            dragging = new Point(e.getX(), e.getY());
                        }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if(mode == ClickMode.NONE){
                        draggedVertex = null;
                    }
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
                    if (mode == ClickMode.NONE && draggedVertex != null) {
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
            g2d.setFont(bigFont);
            g2d.drawString(displayText, 10, 20);
            g2d.setFont(defaultFont);
            String text = "Modes:" +
                    "\n N for MoveVertices" +
                    "\n V for AddVertex" +
                    "\n B for RemoveVertex" +
                    "\n E for AddEdge" +
                    "\n R for RemoveEdge";
            g2d.drawString("Modes", 10, 40);
            g2d.drawString("N for MoveVertices", 10, 50);
            g2d.drawString("V for AddVertex", 10, 60);
            g2d.drawString("B for RemoveVertex", 10, 70);
            g2d.drawString("E for AddEdge", 10, 80);
            g2d.drawString("R for RemoveEdge", 10, 90);

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
            g.setColor(Color.WHITE);
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
}

enum ClickMode {
    ADD_EDGE, ADD_VERTEX, NONE, REMOVE_EDGE, REMOVE_VERTEX
}
