package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.graphs.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditProductionWindow extends JDialog {
    private Production p;
    GraphGrammarCreator creator;
    private VertexFactory vertexFactory;
    private ClickMode clickMode = ClickMode.NONE;
    EditableGraphDisplayPanel gpLeft;
    EditableGraphDisplayPanel gpRight;
    private boolean keepUpdating = true;

    private JList<Object> vertexList;
    private JTextField nameField;
    private JTextField txt_morphism;

    public EditProductionWindow(GraphGrammarCreator creator){
        this(new Production(new Graph(), new Graph()), creator);
    }

    public EditProductionWindow(Production p, GraphGrammarCreator creator){
        super(creator, "Edit Production", ModalityType.APPLICATION_MODAL);
        System.out.println("hello");
        this.p = p;
        this.creator = creator;
        vertexFactory = VertexFactory.getInstance();
        initUI();
        System.out.println("UI initiated");


    }

    void startUpdating() {
        UpdateRunnable updateRunnable = new UpdateRunnable();
        new Thread(updateRunnable).start();

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
        JButton btn_morphism = new JButton("Morphism");
        btn_morphism.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickMode = ClickMode.MORPHISM;
            }
        });

        txt_morphism = new JTextField("0");
        txt_morphism.setMaximumSize(txt_morphism.getPreferredSize());

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
        menuPanel.add(btn_morphism);
        menuPanel.add(txt_morphism);
        menuPanel.add(Box.createRigidArea(distanceBetweenElements));
       // menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
    //    menuPanel.add(Box.createVerticalGlue());
        //liste
        vertexList = new JList<Object>(vertexFactory.getAllSymbols().toArray());
        vertexList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vertexList.setLayoutOrientation(JList.VERTICAL);
        vertexList.setVisibleRowCount(-1);

        vertexList.setCellRenderer(new CellRenderer());
        vertexList.setPreferredSize(new Dimension(100, 400));


        //vertexList.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(vertexList);
        menuPanel.add(scrollPane);
        menuPanel.add(Box.createVerticalGlue());

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keepUpdating = false;
                dispose();
            }
        });
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keepUpdating = false;
                p.setName(nameField.getText());
                p.setLeft(gpLeft.graph);
                p.setRight(gpRight.graph);
                creator.addProduction(p);
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancel);
        buttonPanel.add(save);
        menuPanel.add(buttonPanel);

        this.add(menuPanel, BorderLayout.WEST);
        gpLeft = new EditableGraphDisplayPanel(p.getLeft(), 400, 400, this);
        gpRight = new EditableGraphDisplayPanel(p.getRight(), 400, 400, this);
        JPanel productionPanelContainer = new JPanel();
        productionPanelContainer.setLayout(new BoxLayout(productionPanelContainer, BoxLayout.LINE_AXIS));
        productionPanelContainer.add(Box.createRigidArea(new Dimension(5, 5)));
        productionPanelContainer.add(gpLeft);
        productionPanelContainer.add(Box.createRigidArea(new Dimension(5, 5)));
        productionPanelContainer.add(gpRight);
        productionPanelContainer.add(Box.createRigidArea(new Dimension(5, 5)));


        JPanel productionNameContainer = new JPanel();
        productionNameContainer.setLayout(new BoxLayout(productionNameContainer, BoxLayout.LINE_AXIS));
        productionNameContainer.add(new Label("Name of the Production: "));
        nameField = new JTextField(p.getName());
        productionNameContainer.add(nameField);

        JPanel centerPanelContailer = new JPanel();
        centerPanelContailer.setLayout(new BoxLayout(centerPanelContailer, BoxLayout.PAGE_AXIS));
        centerPanelContailer.add(productionNameContainer);
        centerPanelContailer.add(productionPanelContainer);
        this.add(centerPanelContailer, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(creator);
    }

    public void update(){
        gpLeft.repaint();
        gpRight.repaint();
    }

    public ClickMode getClickMode() {
        return clickMode;
    }

    public int getMorphism(){
        return Integer.parseInt(txt_morphism.getText());
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

    private class UpdateRunnable implements Runnable{
        ForceBasedLayout forceBasedLayout;
        public UpdateRunnable(){
            forceBasedLayout = new ForceBasedLayout();
        }

        @Override
        public void run() {
            while (keepUpdating){
                try {
                    Thread.sleep(1000/60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                forceBasedLayout.step(p.getLeft());
                forceBasedLayout.step(p.getRight());
                update();
            }
        }
    }


}

enum ClickMode {
    ADD_EDGE, ADD_VERTEX, NONE, REMOVE_EDGE, MORPHISM, REMOVE_VERTEX
}
