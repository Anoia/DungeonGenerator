package com.stuckinadrawer.generator;

import com.stuckinadrawer.Utils;

public class CaveWalk extends Generator {

    public CaveWalk(int levelWidth, int levelHeight){
        super(levelWidth, levelHeight);

    }

    int currentX;
    int currentY;

    int floortiles;

    public CaveWalk(){
        this(70, 50);
    }

    @Override
    public Tile[][] generate() {
        initializeEmptyLevel();
        currentX = levelWidth/2;
        currentY = levelHeight/2;
        level[currentX][currentY] = Tile.ROOM;
        floortiles = levelWidth*levelHeight/3;
        doStuff();
        return level;
    }

    @Override
    public Tile[][] step(){
        doStuff();
        return level;
    }

    private void doStuff() {
      //  for(int i = 0; i < 100; i++){
        while (floortiles > 0){
            int rand = Utils.random(1, 4);
            switch(rand){
                case 1:
                    if(currentX+1 < levelWidth-1){
                        currentX++;
                    }else{
                    //    i--;
                    }
                    break;
                case 2:
                    if(currentX-1 > 0){
                        currentX--;
                    }else{
                      //  i--;
                    }
                    break;
                case 3:
                    if(currentY+1 < levelHeight-1){
                        currentY++;
                    }else{
                     //   i--;
                    }
                    break;
                case 4:
                    if(currentY-1 > 0){
                        currentY--;
                    }else{
                      //  i--;
                    }
                    break;
            }

            if(level[currentX][currentY] == Tile.EMPTY){
                level[currentX][currentY] = Tile.ROOM;
                floortiles--;
            }


        }


    }

}
