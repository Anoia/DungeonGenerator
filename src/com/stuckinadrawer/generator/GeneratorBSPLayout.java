package com.stuckinadrawer.generator;

import com.stuckinadrawer.Tile;
import com.stuckinadrawer.Utils;
import com.sun.deploy.si.SingleInstanceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GeneratorBSPLayout implements Generator{

    private final static int MAX_LEAF_SIZE = 30;

    public static Tile[][] level;

    private int levelWidth;
    private int levelHeight;

    public GeneratorBSPLayout(int levelWidth, int levelHeight){
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;

    }

    public GeneratorBSPLayout(){
        this(70, 50);
    }

    @Override
    public Tile[][] generate(){
        initializeEmptyLevel();
        ArrayList<Leaf> leaves = new ArrayList<Leaf>();

        // Create new leaf that is root of all other leaves
        Leaf root = new Leaf(0, 0, levelWidth, levelHeight);
        leaves.add(root);
        boolean didSplit = true;
        // we loop through every Leaf in our Vector over and over again, until no more Leaves can be split.
        while(didSplit){
            didSplit = false;
            for(int i = 0; i < leaves.size(); i++){
                Leaf leaf = leaves.get(i);
                System.out.println("iterating");
                //check if this leaf is already split
                if(leaf.rightChild == null && leaf.rightChild == null){
                    // if this leaf is too big or  75% chance
                    System.out.println("ready to split");
                    if (leaf.width > MAX_LEAF_SIZE || leaf.height > MAX_LEAF_SIZE || Utils.random() > 0.25){
                        //Split the leaf
                        if(leaf.split()){
                            //if split was successful
                            leaves.add(leaf.leftChild);
                            leaves.add(leaf.rightChild);
                            didSplit = true;
                        }
                    }

                }
            }
        }

        root.createRooms();

        //fill level with tiles
        for(Leaf leaf: leaves){
            if(leaf.room != null){
                Room r = leaf.room;
                for(int x = 0; x < r.width; x++){
                    for(int y = 0; y < r.height; y++){
                        level[r.x + x][r.y + y] = Tile.ROOM;
                    }
                }
            }
        }

        buildWalls();

        return level;
    }

    /**
     * Initializes the Level with empty Tiles
     */
    private void initializeEmptyLevel() {
        level = new Tile[levelWidth][levelHeight];

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                level[x][y] = Tile.EMPTY;
            }
        }
    }

    private void buildWalls() {
        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                if (level[x][y] == Tile.ROOM || level[x][y] == Tile.CORRIDOR) {
                    for (int xx = x - 1; xx <= x + 1; xx++) {
                        for (int yy = y - 1; yy <= y + 1; yy++) {
                            if (level[xx][yy] == Tile.EMPTY) {
                                level[xx][yy] = Tile.WALL;
                            }
                        }
                    }
                }
            }
        }
    }
}
