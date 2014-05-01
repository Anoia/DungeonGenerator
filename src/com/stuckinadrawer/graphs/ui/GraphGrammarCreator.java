package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.graphs.Production;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GraphGrammarCreator extends JFrame {

    private Font defaultFont;
    private Font bigFont;

    private ArrayList<Production> productions;
    JList<String> productionList;

    SimpleGraphDisplayPanel gpLeft;
    SimpleGraphDisplayPanel gpRight;

    public GraphGrammarCreator(){
        setLookAndFeel();
        defaultFont = new Font("default", Font.PLAIN, 12);
        bigFont = new Font("big", Font.BOLD, 16);
        productions = new ArrayList<Production>();
        initUI();
    }

    private void initUI() {
        setTitle("Graph Grammar Creator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel productionsPanel = createProductionsPanel();
        add(productionsPanel);
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
        for(Production p: productions){
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
        buttonsPanel.add(btn_new);
        buttonsPanel.add(btn_edit);
        buttonsPanel.add(btn_delete);


        productionsPanel.add(label);
        productionsPanel.add(insidePanel);
        productionsPanel.add(buttonsPanel);
        return productionsPanel;
    }

    public Production getSelectedProductionFromList(){
        if(!productionList.isSelectionEmpty()){
            String name = productionList.getSelectedValue();
            for(Production p: productions){
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
        if(!productions.contains(production)){
            productions.add(production);
        }
        updateListModel();

    }

    public void removeProduction(Production production){
        if(productions.contains(production)){
            productions.remove(production);
        }
        updateListModel();
    }

    private void updateListModel(){
        gpRight.clear();
        gpLeft.clear();
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for(Production p: productions){
            listModel.addElement(p.getName());
        }
        productionList.setModel(listModel);
    }

    public static void main(String[] arg){
        new GraphGrammarCreator();
    }

}
