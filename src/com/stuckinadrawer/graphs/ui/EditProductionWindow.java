package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.graphs.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.HashSet;

public class EditProductionWindow extends JFrame {
    private Production p;

    private VertexFactory vertexFactory;
    private ClickMode clickMode = ClickMode.NONE;
    GraphDisplayPanel gp;

    private JList<Object> vertexList;

    public EditProductionWindow(){
        this(new Production(new Graph(), new Graph()));
    }

    public EditProductionWindow(Production p){
        this.p = p;
        vertexFactory = new VertexFactory();
        initUI();
        startUpdating();
    }

    private void startUpdating() {
        ForceBasedLayout forceBasedLayout = new ForceBasedLayout();
        while(true){
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            forceBasedLayout.step(p.getLeft());
            update();
        }

    }

    private void initUI() {
        setTitle("Edit Production");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create Buttons with Listeners to change mode
        JButton btn_addEdge = new JButton("Add Edge");
        btn_addEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickMode = ClickMode.ADD_EDGE;
                vertexList.setEnabled(false);
            }
        });
        JButton btn_rmvEdge = new JButton("Remove Edge");
        btn_rmvEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickMode = ClickMode.REMOVE_EDGE;
                vertexList.setEnabled(false);
            }
        });
        JButton btn_addVertex = new JButton("Add Vertex");
        btn_addVertex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickMode = ClickMode.ADD_VERTEX;
                vertexList.setEnabled(true);
            }
        });
        JButton btn_rmvVertex = new JButton("Remove Vertex");
        btn_rmvVertex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickMode = ClickMode.REMOVE_VERTEX;
                //vertexList.setVisible(false);
            }
        });

        // create menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

        Dimension distanceBetweenElements = new Dimension(5, 5);

        //add buttons
        menuPanel.add(Box.createRigidArea(distanceBetweenElements));
        menuPanel.add(btn_addEdge);
        menuPanel.add(Box.createRigidArea(distanceBetweenElements));
        menuPanel.add(btn_rmvEdge);
        menuPanel.add(Box.createRigidArea(distanceBetweenElements));
        menuPanel.add(btn_addVertex);
        menuPanel.add(Box.createRigidArea(distanceBetweenElements));
        menuPanel.add(btn_rmvVertex);
        menuPanel.add(Box.createRigidArea(distanceBetweenElements));
        menuPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        //liste
        vertexList = new JList<Object>(vertexFactory.getAllSymbols().toArray());
        vertexList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vertexList.setLayoutOrientation(JList.VERTICAL);
        vertexList.setVisibleRowCount(-1);

        vertexList.setCellRenderer(new CellRenderer());

        //vertexList.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(vertexList);
        menuPanel.add(scrollPane);
        menuPanel.add(Box.createVerticalGlue());

        JButton cancel = new JButton("Cancel");
        JButton save = new JButton("Save");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancel);
        buttonPanel.add(save);
        menuPanel.add(buttonPanel);

        this.add(menuPanel, BorderLayout.WEST);
        gp = new GraphDisplayPanel(p.getLeft(), 800, 600, this);
        this.add(gp, BorderLayout.CENTER);


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void update(){
        gp.repaint();
    }

    public ClickMode getClickMode() {
        return clickMode;
    }

    public void setClickMode(ClickMode clickMode) {
        this.clickMode = clickMode;
    }

    public VertexFactory getVertexFactory() {
        return vertexFactory;
    }

    public String getCurrentVertexSelection(){
        if(vertexList.isSelectionEmpty()){
            return null;
        }
        return vertexList.getSelectedValue().toString();
    }

    private class CellRenderer extends DefaultListCellRenderer{
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if(value!=null && value.toString().equals("-")){
                if(index == 0){
                    return new JLabel("Non-Terminals");
                }else{
                    return new JLabel("Terminals");
                }
            }else{
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        }

    }


}

enum ClickMode {
    ADD_EDGE, ADD_VERTEX, NONE, REMOVE_EDGE, REMOVE_VERTEX
}
