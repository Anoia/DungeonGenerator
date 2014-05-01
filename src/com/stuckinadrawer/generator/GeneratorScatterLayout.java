package com.stuckinadrawer.generator;

import com.stuckinadrawer.Utils;

import java.util.ArrayList;

public class GeneratorScatterLayout implements Generator{

    private int roomCount;

    private int minRoomSize;
    private int maxRoomSize;

    private Tile[][] level;

    private ArrayList<Room> rooms;

    private int levelWidth;
    private int levelHeight;


    public GeneratorScatterLayout(int minRoomCount, int maxRoomCount, int minRoomSize, int maxRoomSize, int levelWidth, int levelHeight){
        this.minRoomSize = minRoomSize;
        this.maxRoomSize = maxRoomSize;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        roomCount = Utils.random(minRoomCount, maxRoomCount);
        initializeEmptyLevel();

    }

    public GeneratorScatterLayout(){
        this(15, 25, 5, 15, 70, 50);
    }

    private void initializeEmptyLevel() {
        level = new Tile[levelWidth][levelHeight];

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                level[x][y] = Tile.EMPTY;
            }
        }
    }

    @Override
    public Tile[][] generate(){
        initializeEmptyLevel();
        generateRooms();
       // moveRoomsCloser();
        //buildCorridors();

        //createEnemies();
        buildBetterCorridors();
        putRoomsInMap();



        buildWalls();

        return level;
    }

    private void generateRooms() {
        rooms = new ArrayList<Room>();
        for (int i = 0; i < roomCount; i++) {

            int width = Utils.random(minRoomSize, maxRoomSize);
            int height = Utils.random(minRoomSize, maxRoomSize);
            int x = Utils.random(1, levelWidth - width - 1);
            int y = Utils.random(1, levelHeight - height - 1);

            Room r = new Room(x, y, width, height);
            if (doesRoomCollide(r)) {
                i--;
            } else {
                //to make sure not 2 rooms are directly next to each other
                r.width--;
                r.height--;

                rooms.add(r);

            }

        }

    }

    private boolean doesRoomCollide(Room newRoom) {
        for (Room roomToCheck : rooms) {
            if (!(
                    (newRoom.x + newRoom.width < roomToCheck.x) ||
                            (newRoom.x > roomToCheck.x + roomToCheck.width) ||
                            (newRoom.y + newRoom.height < roomToCheck.y) ||
                            (newRoom.y > roomToCheck.y + roomToCheck.height)
            )) {
                return true;
            }

        }
        return false;
    }

    private void moveRoomsCloser() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < rooms.size(); j++) {
                Room room = rooms.get(j);
                rooms.remove(room);
                while (true) {
                    int oldX = room.x;
                    int oldY = room.y;

                    if (room.x > 1) room.x--;
                    if (room.y > 1) room.y--;
                    if ((room.x == 1) && (room.y == 1)) {
                        break;
                    }

                    if (doesRoomCollide(room)) {
                        room.x = oldX;
                        room.y = oldY;
                        break;
                    }
                }
                rooms.add(room);

            }
        }
    }

    private void buildCorridors() {

        Boolean first = true;
        for (int i = 0; i < rooms.size(); i++) {
            Room roomA = rooms.get(i);
            Room roomB;
            if (first) {
                roomB = findClosestRoom(roomA);
                first = false;
            } else {
                int index = Utils.random(0, rooms.size() - 1);
                roomB = rooms.get(index);
                first = true;
            }


            if (roomB == null) {
                continue;
            }
            int pointAX = Utils.random(roomA.x + 1, roomA.x + roomA.width - 1);
            int pointAY = Utils.random(roomA.y + 1, roomA.y + roomA.height - 1);

            int pointBX = Utils.random(roomB.x + 1, roomB.x + roomB.width - 1);
            int pointBY = Utils.random(roomB.y + 1, roomB.y + roomB.height - 1);

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
        }
    }

    private void buildBetterCorridors(){
        for (Room roomA : rooms) {
            Room roomB = getNextRoom(roomA);

            int pointAX = Utils.random(roomA.x + 1, roomA.x + roomA.width - 1);
            int pointAY = Utils.random(roomA.y + 1, roomA.y + roomA.height - 1);

            int pointBX = Utils.random(roomB.x + 1, roomB.x + roomB.width - 1);
            int pointBY = Utils.random(roomB.y + 1, roomB.y + roomB.height - 1);

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

        }

    }

    private Room getNextRoom(Room room){

        int index = rooms.indexOf(room);
        if(index == rooms.size()-1){
            return rooms.get(0);
        }
        return rooms.get(index+1);
    }

    private Room findClosestRoom(Room room) {

        int midX = room.x + (room.width / 2);
        int midY = room.y * (room.height / 2);

        Room closest = null;
        int closestDistance = Integer.MAX_VALUE;
        for (Room roomToCheck : rooms) {
            if (room.equals(roomToCheck)) continue;
            int roomToCheckMidX = roomToCheck.x + (roomToCheck.width / 2);
            int roomToCheckMidY = roomToCheck.y + (roomToCheck.height / 2);
            int distance = Math.abs(midX - roomToCheckMidX) + Math.abs(midY - roomToCheckMidY);
            if (distance < closestDistance) {
                closest = roomToCheck;
                closestDistance = distance;
            }
        }
        if (closest == null) {
            System.out.println("ROOM WAS NULL");
        }
        return closest;
    }

    private void createEnemies() {

    }

    private void putRoomsInMap() {

        for (int i = 0; i < roomCount; i++) {
            Room room = rooms.get(i);
            for (int x = room.x; x < room.width + room.x; x++) {
                for (int y = room.y; y < room.height + room.y; y++) {

                    level[x][y] = Tile.ROOM;

                }
            }
        }
    }

    private void buildWalls() {
        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                //level[x][y] = Tiles.EMPTY;
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
