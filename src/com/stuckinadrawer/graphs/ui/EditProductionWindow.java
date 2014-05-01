package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.graphs.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditProductionWindow extends JFrame {
    private Production p;
    GraphGrammarCreator creator;
    private VertexFactory vertexFactory;
    private ClickMode clickMode = ClickMode.NONE;
    SimpleGraphDisplayPanel gp;
    private boolean keepUpdating = true;

    private JList<Object> vertexList;

    public EditProductionWindow(GraphGrammarCreator creator){
        this(new Production(new Graph(), new Graph()), creator);
    }

    public EditProductionWindow(Production p, GraphGrammarCreator creator){
        //super(creator, "Edit Production", ModalityType.APPLICATION_MODAL);
        System.out.println("hello");
        this.p = p;
        this.creator = creator;
        vertexFactory = new VertexFactory();
        initUI();
        System.out.println("UI initiated");


    }

    void startUpdating() {
        ForceBasedLayout forceBasedLayout = new ForceBasedLayout();
        System.out.println("test");
        while(keepUpdating){
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
        setTitle("production");
        System.out.println("start UI");
       // setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Create Buttons with Listeners to change mode
        JButton btn_addEdge = new JButton("Add Edge");
        btn_addEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
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
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keepUpdating = false;
                System.out.println("Close");
            }
        });
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keepUpdating = false;
                creator.addProduction(p);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancel);
        buttonPanel.add(save);
        menuPanel.add(buttonPanel);

        this.add(menuPanel, BorderLayout.WEST);
        System.out.println("added menu panel");
        gp = new EditableGraphDisplayPanel(p.getLeft(), 800, 600, this);
        this.add(gp, BorderLayout.CENTER);
        System.out.println("added graphpanel");


        pack();
        System.out.println("packed");
        setLocationRelativeTo(creator);
        System.out.println("location");


        System.out.println("visible");

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
