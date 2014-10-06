import javax.swing.*;
import java.awt.*;

public class Renderer extends JFrame {

    private Tile[][] level;
    private final int tileSize = 16;

    public Renderer(Tile[][] level){
        this.level = level;
        setTitle("LevelGenerator: ScatterLayout");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(new MyPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
                        case ENTRANCE:
                            g2d.setColor(Color.green);
                            break;
                        case EXIT:
                            g2d.setColor(Color.blue);
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


}
