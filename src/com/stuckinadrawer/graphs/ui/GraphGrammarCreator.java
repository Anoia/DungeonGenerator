package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.graphs.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class GraphGrammarCreator extends JFrame {

    private Font defaultFont;
    private Font bigFont;

    private Grammar grammar;
    JList<String> productionList;

    SimpleGraphDisplayPanel gpLeft;
    SimpleGraphDisplayPanel gpRight;

    JPanel startGraphPanel;

    private static final String FILE_NAME = "grammar1.ser";

    public GraphGrammarCreator(){
        setLookAndFeel();
        defaultFont = new Font("default", Font.PLAIN, 12);
        bigFont = new Font("big", Font.BOLD, 16);
        loadGrammar(FILE_NAME);

        initUI();
    }

    private void loadGrammar(String fileName){
        FileInputStream fis = null;
        ObjectInputStream in = null;
        grammar = null;
        try {
            fis = new FileInputStream(fileName);
            in = new ObjectInputStream(fis);
            grammar = (Grammar) in.readObject();
            in.close();
        } catch (Exception ex) {
           // ex.printStackTrace();
            grammar = new Grammar();
        }
    }

    private void saveGrammar(String fileName){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(grammar);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initUI() {
        setTitle("Graph Grammar Creator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        JPanel productionsPanel = createProductionsPanel();


        startGraphPanel = createStartGraphPanel();
        center.add(startGraphPanel);
        center.add(Box.createRigidArea(new Dimension(5, 5)));
        center.add(productionsPanel);

        add(center);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


    }

    private JPanel createProductionsPanel() {
        JPanel productionsPanel = new JPanel();
        productionsPanel.setLayout(new BoxLayout(productionsPanel, BoxLayout.PAGE_AXIS));

        // Title
        JLabel label = new JLabel("Productions");
        label.setFont(bigFont);

        // Actual Productions List+Display
        JPanel insidePanel = new JPanel();
        insidePanel.setLayout(new BoxLayout(insidePanel, BoxLayout.LINE_AXIS));

        productionList = new JList<String>();
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for(Production p: grammar.getProductions()){
            listModel.addElement(p.getName());
        }
        productionList.setModel(listModel);
        JScrollPane scrollPane = new JScrollPane(productionList);
        insidePanel.add(scrollPane);

        //insidePanel.add(new GraphDisplayPanel());
        gpLeft = new SimpleGraphDisplayPanel(400, 400);
        gpRight = new SimpleGraphDisplayPanel(400, 400);
        insidePanel.add(gpLeft);
        insidePanel.add(Box.createRigidArea(new Dimension(5, 5)));
        insidePanel.add(gpRight);

        productionList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Production selectedProduction = getSelectedProductionFromList();
                if(selectedProduction!=null){
                    gpLeft.setGraph(selectedProduction.getLeft());
                    gpRight.setGraph(selectedProduction.getRight());
                }
            }
        });



        // Buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));

        JButton btn_new = new JButton("New");
        btn_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditProductionWindow window = new EditProductionWindow(GraphGrammarCreator.this);
                window.startUpdating();
                window.setVisible(true);

            }
        });
        JButton btn_edit = new JButton("Edit");
        btn_edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Production selectedProduction = getSelectedProductionFromList();
                if(selectedProduction!= null){
                    EditProductionWindow window = new EditProductionWindow(selectedProduction, GraphGrammarCreator.this);
                    window.startUpdating();
                    window.setVisible(true);
                }
            }
        });
        JButton btn_delete = new JButton("Delete");
        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Production selectedProduction = getSelectedProductionFromList();
                if(selectedProduction!=null){
                    removeProduction(selectedProduction);
                }
            }
        });

        JButton btn_check = new JButton("FindInGraph");
        btn_check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Production selectedProduction = getSelectedProductionFromList();
                if(selectedProduction!=null){
                    findProductionInGraph(selectedProduction);
                }
            }
        });

        JButton btn_apply = new JButton("Apply");
        btn_apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Production selectedProduction = getSelectedProductionFromList();
                if(selectedProduction!=null){
                    HashMap <Vertex, Vertex> morphism = findProductionInGraph(selectedProduction);
                    if(morphism!=null){
                        new SinglePushOut().applyProduction(selectedProduction, grammar.getStartingGraph(), morphism);
                        startGraphPanel.repaint();
                    }

                }
            }
        });

        buttonsPanel.add(btn_new);
        buttonsPanel.add(btn_edit);
        buttonsPanel.add(btn_delete);
        buttonsPanel.add(btn_check);
        buttonsPanel.add(btn_apply);


        productionsPanel.add(label);
        productionsPanel.add(insidePanel);
        productionsPanel.add(buttonsPanel);

        JButton btn_saveGrammar = new JButton("Save Grammar");
        btn_saveGrammar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGrammar(FILE_NAME);
            }
        });
        productionsPanel.add(btn_saveGrammar);
        return productionsPanel;
    }

    private HashMap<Vertex, Vertex> findProductionInGraph(Production selectedProduction) {
        Graph subGraph = selectedProduction.getLeft();
        UllmanSubGraphIsomorphism finder = new UllmanSubGraphIsomorphism();
        System.out.println("FINDER:");
        HashMap<Vertex, Vertex> result = finder.findIsomorphism(grammar.getStartingGraph(), subGraph);
        startGraphPanel.repaint();

        return result;

    }

    private JPanel createStartGraphPanel(){
        JPanel startGraphPanel = new JPanel();
        startGraphPanel.setLayout(new BoxLayout(startGraphPanel, BoxLayout.PAGE_AXIS));

        // Title
        JLabel label = new JLabel("StartGraph");
        label.setFont(bigFont);

        startGraphPanel.add(label);
        Graph startGraph = grammar.getStartingGraph();
        new ForceBasedLayout().layout(startGraph);
        SimpleGraphDisplayPanel graphDisplayPanel = new SimpleGraphDisplayPanel(startGraph, 600, 400);
        startGraphPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        startGraphPanel.add(graphDisplayPanel);

        grammar.setStartingGraph(startGraph);

        return startGraphPanel;
    }

    public Production getSelectedProductionFromList(){
        if(!productionList.isSelectionEmpty()){
            String name = productionList.getSelectedValue();
            for(Production p: grammar.getProductions()){
                if(name.equals(p.getName())){
                    return p;
                }
            }

        }
        return null;
    }

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void addProduction(Production production){
        grammar.addProduction(production);
        updateListModel();

    }

    public void removeProduction(Production production){
        grammar.removeProduction(production);
        updateListModel();
    }

    private void updateListModel(){
        gpRight.clear();
        gpLeft.clear();
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for(Production p: grammar.getProductions()){
            listModel.addElement(p.getName());
        }
        productionList.setModel(listModel);
    }

    public static void main(String[] arg){
        new GraphGrammarCreator();
    }

}
