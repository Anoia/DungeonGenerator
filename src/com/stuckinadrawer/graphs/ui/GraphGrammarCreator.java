package com.stuckinadrawer.graphs.ui;

import com.stuckinadrawer.graphs.Production;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GraphGrammarCreator extends JFrame {

    private Font defaultFont;
    private Font bigFont;

    private ArrayList<Production> productions;

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

        JList productionList = new JList<Object>(productions.toArray());
        JScrollPane scrollPane = new JScrollPane(productionList);
        insidePanel.add(scrollPane);

        //insidePanel.add(new GraphDisplayPanel());
        insidePanel.add(new JPanel());
        insidePanel.add(new JPanel());



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
        JButton btn_delete = new JButton("Delete");
        buttonsPanel.add(btn_new);
        buttonsPanel.add(btn_edit);
        buttonsPanel.add(btn_delete);


        productionsPanel.add(label);
        productionsPanel.add(insidePanel);
        productionsPanel.add(buttonsPanel);
        return productionsPanel;
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

    public void addProduction(Production p){
        productions.add(p);
    }

    public static void main(String[] arg){
        new GraphGrammarCreator();
    }

}
