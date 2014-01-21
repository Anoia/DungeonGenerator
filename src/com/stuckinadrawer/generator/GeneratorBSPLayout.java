package com.stuckinadrawer.generator;

import com.stuckinadrawer.Tile;
import com.stuckinadrawer.Utils;

public class GeneratorBSPLayout implements Generator{

    private Tile[][] level;

    private int levelWidth;
    private int levelHeight;

    private int maxRoomSize;

    int count = 0;

    public GeneratorBSPLayout(int levelWidth, int levelHeight, int maxRoomSize){
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.maxRoomSize = maxRoomSize;
        initializeEmptyLevel();


    }

    public GeneratorBSPLayout(){
        this(60, 60, 20);
    }

    @Override
    public Tile[][] generate(){
        Dungeon dungeon = new Dungeon(0, 0, levelWidth, levelHeight, null, 0);
        split(dungeon);
        return level;
    }

    private void split(Dungeon dungeon){
        System.out.println(" --- ");
        System.out.println("Count " + count);
        count++;
        System.out.println("Dungeon x:"+dungeon.x+" y:"+dungeon.y+" w:"+dungeon.width+" h:"+dungeon.height);
        //check if we have to split
        if((dungeon.width <= maxRoomSize && dungeon.height <= maxRoomSize)||dungeon.iteration >5){
            System.out.println("NO Split");
            //add to level
            for(int y = 0; y < dungeon.height; y++){
                for(int x = 0; x < dungeon.width; x++){
                    if(y == 0 || x == 0 || y == dungeon.height-1 || x == dungeon.width-1){
                        level[dungeon.y + y][dungeon.x + x] = Tile.WALL;
                    }else{
                        level[dungeon.y + y][dungeon.x + x] = Tile.ROOM;
                    }
                }
            }
        }else{
            int r = Utils.random(0, 1);
            Boolean splitVertical = (r == 0) ? true : false;
            Dungeon a;
            Dungeon b;

            if(dungeon.width <=8) splitVertical=false;
            if(dungeon.height<=8) splitVertical=true;

            if(splitVertical){
                System.out.println("Split Vertical");
                int pos = Utils.random(4, dungeon.width-4);
                a = new Dungeon(dungeon.x, dungeon.y, pos, dungeon.height, dungeon, dungeon.iteration+1);
                b = new Dungeon(dungeon.x+pos, dungeon.y, dungeon.width-pos, dungeon.height, dungeon, dungeon.iteration+1);
            }else{
                System.out.println("Split Horizontal");
                int pos = Utils.random(4, dungeon.height-4);
                a =  new Dungeon(dungeon.x, dungeon.y, dungeon.width, pos, dungeon, dungeon.iteration+1);
                b = new Dungeon(dungeon.x, dungeon.y+pos, dungeon.width, dungeon.height-pos, dungeon, dungeon.iteration+1);
            }
            split(a);
            split(b);
        }
    }

    private class Dungeon{
        int x;
        int y;
        int width;
        int height;
        Dungeon parent;
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
