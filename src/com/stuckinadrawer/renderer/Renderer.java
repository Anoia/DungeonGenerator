package com.stuckinadrawer.renderer;

import com.stuckinadrawer.Tile;
import com.stuckinadrawer.generator.Generator;
import com.stuckinadrawer.generator.GeneratorBSPLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Renderer extends JFrame{

    private Tile[][] level;
    private int tileSize = 16;
    private Generator g;

    public Renderer(){
        g = new GeneratorBSPLayout();
        this.level = g.generate();
        initGUI();
    }

    private void initGUI() {
        setTitle("Points");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(level.length*tileSize, level[0].length*tileSize);
        add(new MyPanel());

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
    }

    public void createNewLevel(){
        level = g.generate();
        repaint();
    }
}
