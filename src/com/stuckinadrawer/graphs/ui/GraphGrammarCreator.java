package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.ObjectCloner;
import com.stuckinadrawer.Utils;
import com.stuckinadrawer.graphs.*;
import com.sun.org.apache.xpath.internal.SourceTree;

import javax.rmi.CORBA.Util;
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
import java.util.ArrayList;
import java.util.HashMap;

public class GraphGrammarCreator extends JFrame {

    private Font defaultFont;
    private Font bigFont;

    private Grammar grammar;
    JList<String> productionList;

    SimpleGraphDisplayPanel gpLeft;
    SimpleGraphDisplayPanel gpRight;

    JPanel startGraphPanel;

    private static final String FILE_NAME = "./grammar1.ser";

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

        VertexFactory.setCurrentMaxId(grammar.currentMaxVertexId);
    }

    private void saveGrammar(String fileName){

        grammar.currentMaxVertexId = VertexFactory.getCurrentMaxId();

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

        productionList = new JList<String>();
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for(Production p: grammar.getProductions()){
            listModel.addElement(p.getName());
        }
        productionList.setModel(listModel);
        JScrollPane scrollPane = new JScrollPane(productionList);
        insidePanel.add(scrollPane);


        gpLeft = new SimpleGraphDisplayPanel(400, 400);
        gpRight = new SimpleGraphDisplayPanel(400, 400);
        insidePanel.add(gpLeft);
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
                        Production productionWithoutWildcard = createNewProductionWithoutWildcard(selectedProduction, morphism);
                        new SinglePushOut().applyProduction(productionWithoutWildcard, grammar.getStartingGraph(), morphism);


                        Graph startGraph = grammar.getStartingGraph();
                        startGraph.setRandomVertexPosition(800, 400);
                        new ForceBasedLayout().layout(startGraph);
                        startGraphPanel.repaint();


                    }

                }

                System.out.println("\nDONE\n\n");
            }
        });

        JButton btn_apply_random = new JButton("Apply Random");
        btn_apply_random.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Production selectedProduction = getRandomProduction();
                if(selectedProduction!=null){
                    HashMap <Vertex, Vertex> morphism = findProductionInGraph(selectedProduction);
                    if(morphism!=null){
                        Production productionWithoutWildcard = createNewProductionWithoutWildcard(selectedProduction, morphism);
                        new SinglePushOut().applyProduction(productionWithoutWildcard, grammar.getStartingGraph(), morphism);
                        startGraphPanel.repaint();

                        Graph startGraph = grammar.getStartingGraph();

                        startGraph.setRandomVertexPosition(800, 400);
                        new ForceBasedLayout().layout(startGraph);


                    }

                }
            }
        });

        JButton btn_redraw = new JButton("Redraw");
        btn_redraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graph startGraph = grammar.getStartingGraph();
                startGraph.setRandomVertexPosition(800, 400);
                new ForceBasedLayout().layout(startGraph);
                for(Vertex v:startGraph.getVertices())    {
                    v.marked = false;
                }
                startGraphPanel.repaint();
            }
        });

        buttonsPanel.add(btn_new);
        buttonsPanel.add(btn_edit);
        buttonsPanel.add(btn_delete);
        buttonsPanel.add(btn_check);
        buttonsPanel.add(btn_apply);
        buttonsPanel.add(btn_apply_random);
        buttonsPanel.add(btn_redraw);


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

    private Production createNewProductionWithoutWildcard(Production selectedProduction, HashMap<Vertex, Vertex> morphism) {
        System.out.println("\n ###WILDCARDS### ");
        Production newProduction;
        try {
            newProduction = (Production) ObjectCloner.deepCopy(selectedProduction);
        } catch (Exception e) {
            e.printStackTrace();
            return selectedProduction;
        }

        //remove wildcards from left side
        for(Vertex vertexInLeftSide: newProduction.getLeft().getVertices()){
            if(vertexInLeftSide.getType().equals("*")){
                Vertex assigenedVertexInHost = morphism.get(vertexInLeftSide);
                morphism.remove(vertexInLeftSide);
                vertexInLeftSide.setType(assigenedVertexInHost.getType());
                morphism.put(vertexInLeftSide, assigenedVertexInHost);
                System.out.println("WILDCARD REMOVED FROM: "+vertexInLeftSide.getDescription());
            }
        }

        //remove wildcards from right side
        for(Vertex vertexInRightSide: newProduction.getRight().getVertices()){
            if(vertexInRightSide.getType().equals("*")){
                int morphRight = vertexInRightSide.getMorphism();
                for(Vertex vertexInLeftSide: newProduction.getLeft().getVertices()){
                    if(vertexInLeftSide.getMorphism() == morphRight){
                        System.out.println("WIlDCARD REMOVED FROM "+vertexInRightSide.getDescription());
                        System.out.println("set to: "+vertexInLeftSide.getType());
                        vertexInRightSide.setType(vertexInLeftSide.getType());
                    }
                }


            }
        }

        return newProduction;
    }

    private Production getRandomProduction() {
        System.out.println("looking for rand prod");
        ArrayList<Production> possibleProductions = new ArrayList<Production>();
        for(Production p: grammar.getProductions()){
            if(findProductionInGraph(p)!=null){
                possibleProductions.add(p);
            }
        }
        if(!possibleProductions.isEmpty()){
            int rand = Utils.random(possibleProductions.size()-1);
            System.out.println("found "+possibleProductions.size()+"possible prods; chose nr"+rand);
            return possibleProductions.get(rand);
        }
        return null;

    }

    private HashMap<Vertex, Vertex> findProductionInGraph(Production selectedProduction) {
        Graph subGraph = selectedProduction.getLeft();
        UllmanSubgraphIsomorphism finder = new UllmanSubgraphIsomorphism();
        HashMap<Vertex, Vertex> result = finder.findIsomorphism(grammar.getStartingGraph(), subGraph);
        startGraphPanel.repaint();
        return result;
    }

    private JPanel createStartGraphPanel(){
        JPanel startGraphPanel = new JPanel();

        // Title
        JLabel label = new JLabel("StartGraph");
        label.setFont(bigFont);

        startGraphPanel.add(label);
        Graph startGraph = grammar.getStartingGraph();
        new ForceBasedLayout().layout(startGraph);
        SimpleGraphDisplayPanel graphDisplayPanel = new SimpleGraphDisplayPanel(startGraph, 800, 400);
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
