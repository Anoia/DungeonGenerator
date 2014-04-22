package com.stuckinadrawer.generator;

import com.stuckinadrawer.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GeneratorDemo extends JFrame{

    private Tile[][] level;
    private int tileSize = 16;
    private Generator g;

    public GeneratorDemo(){
        //g = new GeneratorBSPLayout();
        g = new GeneratorScatterLayout();
        this.level = g.generate();
        initGUI();
    }

    private void initGUI() {
        setTitle("LevelGenerator: ScatterLayout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new MyPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 32){
                    createNewLevel();
                }
                switch(e.getKeyCode()){
                    case 32:
                        createNewLevel();
                        break;
                    case 83:
                        //scatter
                        setTitle("LevelGenerator: ScatterLayout");
                        System.out.println("Now using ScatterLayout to generate Level");
                        g = new GeneratorScatterLayout();
                        createNewLevel();
                        break;
                    case 66:
                        //bsp
                        setTitle("LevelGenerator: BSP-Tree");
                        System.out.println("Now using BSP-Tree to generate Level");
                        g = new GeneratorBSPLayout();
                        createNewLevel();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }


    private class MyPanel extends JPanel {
        private void draw(Graphics g){

            //this is where the drawing happens
            Graphics2D g2d = (Graphics2D) g;

            for (int x = 0; x < level.length; x++) {
                for (int y = 0; y < level[0].length; y++) {
                    g2d.setColor(Color.black);
                    switch (level[x][y]) {
                        case EMPTY:

                            break;
                        case WALL:
                            g2d.setColor(Color.red);
                            break;
                        case ROOM:
                            g2d.setColor(Color.gray);
                            break;
                        case CORRIDOR:
                            g2d.setColor(Color.darkGray);
                            break;
                    }

                    g2d.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
                    g2d.setColor(Color.black);
                    g2d.drawRect(x*tileSize, y*tileSize, tileSize, tileSize);
                }
            }

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponents(g);
            draw(g);
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(level.length*tileSize, level[0].length*tileSize);
        }
    }

    public void createNewLevel(){
        level = g.generate();
        repaint();
    }

    public static void main(String [] arg){
        new GeneratorDemo();
    }
}
