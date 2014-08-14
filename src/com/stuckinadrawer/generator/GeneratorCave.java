package com.stuckinadrawer.generator;

import com.stuckinadrawer.Utils;

public class GeneratorCave extends Generator {

    int step = 0;

    public GeneratorCave(int levelWidth, int levelHeight){
        super(levelWidth, levelHeight);

    }

    public GeneratorCave(){
        this(70, 50);
    }

    @Override
    public Tile[][] generate() {
        initializeCave();
        step = 0;
        return level;
    }

    @Override
    public Tile[][] step(){
        doStuff();
        step++;
        return level;
    }

    private void doStuff() {
        Tile[][] newLevel = new Tile[levelWidth][levelHeight];

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {

                if(x==0 || y == 0 || x == levelWidth-1 || y == levelHeight-1){
                    newLevel[x][y] = Tile.CORRIDOR;
                }else{
                    int walls = getAmountOfWallsAroundTile(x, y, 1);

                    if(level[x][y] == Tile.CORRIDOR){
                        newLevel[x][y] = (walls  >= 4)?Tile.CORRIDOR:Tile.ROOM;

                    }else{
                        newLevel[x][y] = (walls  >= 5)?Tile.CORRIDOR:Tile.ROOM;
                    }

                    if(step < 3 && getAmountOfWallsAroundTile(x, y, 3)<1){
                        newLevel[x][y] = Tile.CORRIDOR;
                    }


                }



            }
        }

        level = newLevel;


    }

    private int getAmountOfWallsAroundTile(int x, int y, int range) {
        int amount = 0;
        for(int i = x-range; i<=x+range; i++){
            for(int j = y-range; j<=y+range; j++){
                if(i >= 0 && i < levelWidth && j >= 0 && j < levelHeight) {
                    if(!(x==i && y==j) && level[i][j] == Tile.CORRIDOR){
                        amount++;
                    }
                }
            }
        }

        return amount;
    }


    private void initializeCave() {
        level = new Tile[levelWidth][levelHeight];

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                int rand = Utils.random(1, 100);
                if(rand>45){
                    level[x][y] = Tile.ROOM;
                }else{
                    level[x][y] = Tile.CORRIDOR;
                }

                if(x==0 || y == 0 || x == levelWidth-1 || y == levelHeight-1){
                    level[x][y] = Tile.CORRIDOR;
                }

            }
        }
    }
}
