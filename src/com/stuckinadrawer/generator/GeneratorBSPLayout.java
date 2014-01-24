package com.stuckinadrawer.generator;

import com.stuckinadrawer.Tile;
import com.stuckinadrawer.Utils;
import com.sun.deploy.si.SingleInstanceManager;

import java.util.ArrayList;
import java.util.Collections;
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
        this(70, 50, 25);
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
        Collections.reverse(dungeons);
        HashSet<Dungeon> connectedDungeons = new HashSet<Dungeon>();
        for(Dungeon d: dungeons){
            if(d.parent!=null && !connectedDungeons.contains(d) && d.childA == null){
                Dungeon dungeonA = d.parent.childA;
                Dungeon dungeonB = d.parent.childB;

                //drawCorridor from A to B
                drawCorridor(dungeonA, dungeonB);

                connectedDungeons.add(dungeonA);
                connectedDungeons.add(dungeonB);
            }
        }


    }

    private void drawCorridor(Dungeon dA, Dungeon dB){

        int pointAX = dA.x + dA.width/2;
        int pointAY = dA.y + dA.height/2;

        int pointBX = dB.x + dB.width/2;
        int pointBY = dB.y + dB.height/2;
        System.out.println(pointAX + " " + pointAY + " | " + pointBX +" "+ pointBY);
                /*
        if(pointAX < pointBX){
            //a links von b
            System.out.println("a links von b");
            int walkingPoint = pointBX;
            boolean drawing = false;
            while(true){
                System.out.println("test");
                if(level[walkingPoint][pointBY] == Tile.WALL){
                    if(drawing){
                        level[walkingPoint][pointBY] = Tile.CORRIDOR;
                        System.out.println("stop drawing");
                        break;
                    }else{
                    drawing = true;
                        System.out.println("Start drawing");
                    }
                }
                if(drawing){
                    level[walkingPoint][pointBY] = Tile.CORRIDOR;
                    level[walkingPoint][pointBY-1] = Tile.WALL;
                    level[walkingPoint][pointBY+1] = Tile.WALL;
                }
                walkingPoint --;
            }
        } else if(pointAX > pointBX){
            //a rechts von b
            System.out.println("a rechts von b");
            int walkingPoint = pointBX;
            boolean drawing = false;
            while(true){
                if(level[walkingPoint][pointBY] == Tile.WALL){
                    if(drawing){
                        level[walkingPoint][pointBY] = Tile.CORRIDOR;
                        break;
                    }else{
                        drawing = true;
                    }
                }
                if(drawing){
                    level[walkingPoint][pointBY] = Tile.CORRIDOR;
                    level[walkingPoint][pointBY-1] = Tile.CORRIDOR;
                    level[walkingPoint][pointBY+1] = Tile.CORRIDOR;
                }
                walkingPoint++;
            }

        } else if(pointAY > pointBY){
            //a über b
            System.out.println("a über b");

        } else{
            //a unter b
            System.out.println("a unter b");

        }  */

        while ((pointBX != pointAX) || (pointBY != pointAY)) {
            if (pointBX != pointAX) {
                if (pointBX > pointAX) pointBX--;
                else pointBX++;
            } else {
                if (pointBY > pointAY) pointBY--;
                else pointBY++;
            }

            level[pointBX][pointBY] = Tile.CORRIDOR;
        }

        //surround with walls

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
            dungeon.childA = a;
            dungeon.childB = b;
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
        Dungeon childA = null;
        Dungeon childB = null;
        int iteration;

        public Dungeon(int x, int y, int width, int height, Dungeon parent, int iteration){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.parent = parent;
            this.iteration = iteration;
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
