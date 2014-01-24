package com.stuckinadrawer.generator;

import com.stuckinadrawer.Tile;
import com.stuckinadrawer.Utils;

import java.util.ArrayList;
import java.util.HashSet;

public class GeneratorBSPLayout implements Generator{

    private Tile[][] level;

    private int levelWidth;
    private int levelHeight;

    private int maxRoomSize;

    private int count = 0;

    private int distanceFromWallsWhenSplitting = 10;

    private ArrayList<Dungeon> dungeons;

    public GeneratorBSPLayout(int levelWidth, int levelHeight, int maxRoomSize){
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.maxRoomSize = maxRoomSize;
    }

    public GeneratorBSPLayout(){
        this(90, 70, 25);
    }

    @Override
    public Tile[][] generate(){
        initializeEmptyLevel();
        dungeons = new ArrayList<Dungeon>();
        Dungeon dungeon = new Dungeon(0, 0, levelWidth, levelHeight, null, 0);
        dungeons.add(dungeon);
        split(dungeon);
        createCorridors();
        return level;
    }

    private void createCorridors() {
        //array list from back to front
        //take dungeon, find partner via parent, connect, remove both
        //if parent  == null : ende


    }

    private void split(Dungeon dungeon){
        System.out.println(" --- ");
        System.out.println("Count " + count);
        System.out.println("it " + dungeon.iteration);
        count++;
        System.out.println("Dungeon x:"+dungeon.x+" y:"+dungeon.y+" w:"+dungeon.width+" h:"+dungeon.height);

        //check if we have to split
        if((dungeon.width <= maxRoomSize && dungeon.height <= maxRoomSize)||dungeon.iteration >5){
            System.out.println("NO Split");
            //add to level
            createRoom(dungeon);
        }else{
            int r = Utils.random(0, 1);
            Boolean splitVertical = (r == 0) ? true : false;
            Dungeon a;
            Dungeon b;

            if(dungeon.width <=2*distanceFromWallsWhenSplitting) splitVertical=false;
            if(dungeon.height<=2*distanceFromWallsWhenSplitting) splitVertical=true;

            if(splitVertical){
                System.out.println("Split Vertical");
                int pos = Utils.random(distanceFromWallsWhenSplitting, dungeon.width-distanceFromWallsWhenSplitting);
                a = new Dungeon(dungeon.x, dungeon.y, pos, dungeon.height, dungeon, dungeon.iteration+1);
                b = new Dungeon(dungeon.x+pos, dungeon.y, dungeon.width-pos, dungeon.height, dungeon, dungeon.iteration+1);
            }else{
                System.out.println("Split Horizontal");
                int pos = Utils.random(distanceFromWallsWhenSplitting, dungeon.height-distanceFromWallsWhenSplitting);
                a =  new Dungeon(dungeon.x, dungeon.y, dungeon.width, pos, dungeon, dungeon.iteration+1);
                b = new Dungeon(dungeon.x, dungeon.y+pos, dungeon.width, dungeon.height-pos, dungeon, dungeon.iteration+1);
            }
            dungeon.addChild(a);
            dungeon.addChild(b);
            dungeons.add(a);
            dungeons.add(b);

            split(a);
            split(b);
        }
    }

    private void createRoom(Dungeon dungeon){

        int variable = 3;

        //add to level
        int offsetX = Utils.random(variable);            /// + dungeon.x
        int offsetY = Utils.random(variable);

        int roomWidth = Utils.random(dungeon.width-offsetX-variable, dungeon.width-offsetX);
        int roomHeight = Utils.random(dungeon.height-offsetY-variable, dungeon.height-offsetY);

        Room r = new Room(dungeon.x + offsetX, dungeon.y + offsetY, roomWidth, roomHeight);

        for(int x = 0; x < r.width; x++){
            for(int y = 0; y < r.height; y++){
                if(y == 0 || x == 0 || y == r.height-1 || x == r.width-1){
                    level[r.x + x][r.y + y] = Tile.WALL;
                }else{
                    level[r.x + x][r.y + y] = Tile.ROOM;
                }
            }
        }

    }

    private class Dungeon{
        int x;
        int y;
        int width;
        int height;
        Dungeon parent;
        HashSet<Dungeon> children;
        int iteration;

        public Dungeon(int x, int y, int width, int height, Dungeon parent, int iteration){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.parent = parent;
            this.iteration = iteration;
            children = new HashSet<Dungeon>();
        }

        public void addChild(Dungeon d){
            children.add(d);
        }
    }



    private void initializeEmptyLevel() {
        level = new Tile[levelWidth][levelHeight];

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                level[x][y] = Tile.EMPTY;
            }
        }
    }

}
