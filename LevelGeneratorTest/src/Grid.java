import com.stuckinadrawer.Point;

import java.util.ArrayList;
import java.util.Random;

public class Grid {

    ArrayList<Room> rooms;

    int[][] grid;

    public Grid(ArrayList<Room> rooms){
        this.rooms = rooms;

        initEmpty();
        layout();
    }

    private void initEmpty() {
        int size = rooms.size();
        grid = new int[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = -1;
            }
        }

    }

    private void layout() {
        for(Room r: rooms){
            //place first room in middle
            if(r.id == 0){
                r.x = rooms.size()/2;
                r.y = rooms.size()/2;
                grid[r.x][r.y] = r.id;
                continue;
            }

            //get connected room
            Room incomingConnection = rooms.get(r.incomingRoomID);

            Point newSpot = findEmptySpotNextToRoom(incomingConnection);
            if(newSpot == null){
                //move something

                //choose dir to move:
                newSpot = findNotConnectedSpotNextToRoom(incomingConnection);
                int deltaX = newSpot.getX() - incomingConnection.x;
                int deltaY = newSpot.getY() - incomingConnection.y;

                boolean horizontal = (deltaX == 0);

                int x = incomingConnection.x;
                int y = incomingConnection.y;
                if(deltaX == -1 || deltaY == -1){
                    x--;
                    y--;
                }


                makeSpace(horizontal, new Point(x,y));

                newSpot = findEmptySpotNextToRoom(incomingConnection);

            }

            r.x = newSpot.getX();
            r.y = newSpot.getY();
            grid[r.x][r.y] = r.id;


        }
    }

    private void makeSpace(boolean horizontal, Point point) {
        if(horizontal){
            //deltaX = 0, deltaY + 1
            for(int x = 0; x < grid.length; x++ ){
                for(int y = grid[x].length-1; y > point.getY(); y--){
                    if(y == point.getY()+1){
                        //the new row
                        grid[x][y] = -1;
                        continue;
                    }

                    grid[x][y] = grid[x][y-1];
                }
            }

        }else {

            for(int y = 0; y < grid.length; y++ ){
                for(int x = grid[y].length-1; x > point.getX(); x--){
                    if(x == point.getX()+1){
                        //the new row
                        grid[x][y] = -1;
                        continue;
                    }

                    grid[x][y] = grid[x-1][y];
                }
            }

        }


        //update room positions
        for(int x = 0; x < grid.length; x++ ){
            for(int y = 0; y < grid[x].length; y++ ){
                int id = grid[x][y];
                if(id != -1){
                    rooms.get(id).id = id;
                }
            }
        }

    }

    private Point findEmptySpotNextToRoom(Room r) {

        int x = r.x;
        int y = r.y;

        ArrayList<Point> points = new ArrayList<Point>();

        if(grid[x-1][y] == -1) points.add(new Point(x-1,y));
        if(grid[x+1][y] == -1) points.add(new Point(x+1,y));
        if(grid[x][y-1] == -1) points.add(new Point(x,y-1));
        if(grid[x][y+1] == -1) points.add(new Point(x,y+1));

        if (points.isEmpty()) return null;

        Random random = new Random();
        return points.get(random.nextInt(points.size()));
    }

    private Point findNotConnectedSpotNextToRoom(Room r) {

        int x = r.x;
        int y = r.y;

        ArrayList<Point> points = new ArrayList<Point>();

        if(rooms.get(grid[x-1][y]).incomingRoomID != r.id
                && r.incomingRoomID != rooms.get(grid[x-1][y]).id) {
            points.add(new Point(x - 1, y));
        }
        if(rooms.get(grid[x+1][y]).incomingRoomID != r.id
                && r.incomingRoomID != rooms.get(grid[x+1][y]).id) {
            points.add(new Point(x + 1, y));
        }
        if(rooms.get(grid[x][y-1]).incomingRoomID != r.id
                && r.incomingRoomID != rooms.get(grid[x][y-1]).id) {
            points.add(new Point(x, y - 1));
        }
        if(rooms.get(grid[x][y+1]).incomingRoomID != r.id
                && r.incomingRoomID != rooms.get(grid[x][y+1]).id) {
            points.add(new Point(x, y + 1));
        }

        if (points.isEmpty()) return null;

        Random random = new Random();
        return points.get(random.nextInt(points.size()));
    }




}
