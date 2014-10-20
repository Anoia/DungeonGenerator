import com.stuckinadrawer.FileReader;
import com.stuckinadrawer.Point;
import com.stuckinadrawer.Utils;
import com.stuckinadrawer.graphs.Grammar;
import com.stuckinadrawer.graphs.GrammarManager;
import com.stuckinadrawer.graphs.Graph;
import com.stuckinadrawer.graphs.Vertex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public class LevelGeneratorTest {

    private Graph levelGraph;
    Tile[][] level;
    ArrayList<Room> rooms = new ArrayList<Room>();

    private int levelWidth = 150;
    private int levelHeight = 150;

    public LevelGeneratorTest(){
        levelGraph = createLevelGraph();
        level = initEmptyLevel();

        createGroups();
        new Grid(rooms);
        positionRooms();





        for(Room r: rooms){
            putRoomInMap(r);
        }

        makeCorridors();

        new Renderer(level);
    }

    private void makeCorridors() {
        Pathfinder pathfinder = new Pathfinder(level);
        for(Room r: rooms){
            if(r.id == 0) continue;
            Room connectedRoom = rooms.get(r.incomingRoomID);
            LinkedList<Point> path = pathfinder.findPath(r, connectedRoom);
            for(Point pos: path){
                Tile t = level[pos.getX()][pos.getY()];
                if(t.tileType == TileType.WALL || t.tileType == TileType.EMPTY){
                    level[pos.getX()][pos.getY()].tileType = TileType.CORRIDOR;
                }
            }
            surroundCorridorWithWalls();
        }


    }

    private void surroundCorridorWithWalls() {
        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                if (level[x][y].tileType == TileType.CORRIDOR) {
                    for (int xx = x - 1; xx <= x + 1; xx++) {
                        for (int yy = y - 1; yy <= y + 1; yy++) {
                            if ( level[xx][yy].tileType == TileType.EMPTY) {
                                level[xx][yy].tileType = TileType.WALL;
                            }
                        }
                    }
                }
            }
        }

    }

    private void positionRooms() {

        for(Room r: rooms){
            if(r.id == 0){
                r.x = levelWidth/2;
                r.y = levelHeight/2;
                r.initialPos = true;
                continue;
            }



            Room connectedRoom = rooms.get(r.incomingRoomID);

            int offset = Utils.random(0, 8) - 4;
            int x = connectedRoom.x + offset;
            offset = Utils.random(0, 8) - 4;
            int y = connectedRoom.y + offset;

            if(x == connectedRoom.x && y == connectedRoom.y){
                x = (Utils.random(1)>0)? connectedRoom.x-1:connectedRoom.x+1;
                y = (Utils.random(1)>0)? connectedRoom.y-1:connectedRoom.y+1;
            }

            r.x = x;
            r.y = y;
            r.initialPos = true;

            separateRooms();

        }





    }

    private void separateRooms(){
        boolean overlap = true;
        while(overlap){
            overlap = false;
            for(Room room1: rooms){
                if(!room1.initialPos){
                    continue;
                }
                room1.forceX = 0;
                room1.forceY = 0;

                for(Room room2: rooms){
                    if(!room2.initialPos || room2.equals(room1) || !checkForRoomCollision(room1, room2)){
                        continue;
                    }

                    overlap = true;

                    double deltaX = room2.x - room1.x;
                    double deltaY = room2.y - room1.y;
                    if(deltaX == 0 && deltaY == 0){
                        deltaX = 1;
                        deltaY = 1;
                    }

                    double delta2 = deltaX * deltaX + deltaY * deltaY;

                    room1.forceX -= (deltaX>0)?1:-1;
                    room1.forceY -= (deltaY>0)?1:-1;

                    if(room1.id == room2.incomingRoomID || room2.id == room1.incomingRoomID){
                        double distance = Math.sqrt(delta2);
                        if(distance > 15){
                            room1.forceX += (distance - 15) * deltaX;
                            room1.forceY += (distance - 15) * deltaY;
                        }

                    }


                }
            }

            for(Room r: rooms){
                r.applyForce();

            }
        }
    }



    private boolean checkForRoomCollision(Room room1, Room room2) {
        int border = 2;
        return !(
                (room1.x + room1.width < room2.x - border) ||
                        (room1.x > room2.x + room2.width + border) ||
                        (room1.y + room1.height < room2.y - border) ||
                        (room1.y > room2.y + room2.height + border)
        );

    }


    private void putRoomInMap(Room r) {
        for(int x = r.x; x <= r.x+r.width; x++){
            for(int y = r.y; y <= r.y+r.height; y++){

                if(x < 0 || x >=levelWidth || y < 0 || y >= levelHeight) continue;

                if(x == r.x || x == r.x+r.width || y == r.y || y == r.y+r.height){
                    level[x][y] = new Tile(TileType.WALL);
                }else{
                    level[x][y] = new Tile(TileType.ROOM);
                }

                level[x][y].setRoomID(r.id);
            }
        }
        int i = 0;
        for(Vertex v: r.elements){
            TileType tileType = TileType.EMPTY;
            if(v.getType().equals("start")){
                tileType = TileType.ENTRANCE;
            }
            if(v.getType().equals("exit")){
                tileType = TileType.EXIT;
            }
            if(v.getType().equals("opponent")){
                tileType = TileType.OPPONENT;
            }
            if(v.getType().equals("boss")){
                tileType = TileType.BOSS;
            }
            if(v.getType().equals("key")){
                tileType = TileType.KEY;
            }
            if(v.getType().equals("lock")){
                tileType = TileType.LOCK;
            }
            if(v.getType().equals("trap")){
                tileType = TileType.TRAP;
            }
            if(v.getType().equals("chest")){
                tileType = TileType.CHEST;
            }
            if(v.getType().equals("buff")){
                tileType = TileType.BUFF;
            }
            if(r.x+2+i < 0 || r.x+2+i >=levelWidth || r.y+2 < 0 || r.y+2 >= levelHeight) continue;
            level[r.x+2+i][r.y+2].tileType = tileType;


            i++;
        }

    }

    private void createGroups() {
        //uses morphism field to assign groups;
        //same group = in same room

        int groupID = 0;

        Stack<Vertex> nextVertices = new Stack<Vertex>();

        Vertex currentVertex = findStartVertex();
        currentVertex.setMorphism(groupID);
        Room room = new Room();
        room.id = groupID;
        room.elements.add(currentVertex);
        room.expand();
        rooms.add(room);

        groupID++;

        nextVertices.addAll(levelGraph.getOutgoingNeighbors(currentVertex));
        System.out.println("### GROUPING ###");
        int sameID = 0;
        while(!nextVertices.empty()){
            currentVertex = nextVertices.pop();
            currentVertex.setMorphism(groupID);



            if(groupID >= rooms.size()){
                room = new Room();
                room.id = groupID;
                rooms.add(room);
            }else{
                room = rooms.get(groupID);
            }

            room.expand();
            room.elements.add(currentVertex);

            System.out.println("\n"+currentVertex.getDescription() + " was assigned");
            HashSet<Vertex> outgoingNeighbours = levelGraph.getOutgoingNeighbors(currentVertex);
            System.out.println("has "+ outgoingNeighbours.size() + " outgoing neighbours");
            if(outgoingNeighbours.size() > 1){
                System.out.println("added all, new id");

                sameID = 0;
                for(Vertex v: outgoingNeighbours){
                    if(v.getType().equals("key")){
                        room.expand();
                        room.elements.add(v);
                    }else{
                        nextVertices.push(v);
                    }

                }
                groupID++;
            }else if (outgoingNeighbours.size() == 1){
                System.out.println("added one");
                for(Vertex v: outgoingNeighbours){
                    nextVertices.push(v);
                    if(v.getType().equals("lock") || v.getType().equals("exit") || sameID>=2){
                        groupID++;
                        sameID = 0;
                    }else{
                        sameID++;
                    }
                }
            }else{
                //no outgoing neighbours
                System.out.println("added none, new id");
                groupID++;
                sameID = 0;
            }


        }
        System.out.println("end");

        for(Room r: rooms){
            String elements = "";
            for(Vertex v: r.elements){
                elements += v.getType()+ " ";
                for(Vertex incomingNeighbour: levelGraph.getIncomingNeighbors(v)){
                    if (incomingNeighbour.getMorphism()!= r.id){
                        r.incomingRoomID = incomingNeighbour.getMorphism();
                        break;
                    }
                }
            }
            System.out.println("ROOM "+r.id + " contains: "+elements+"; incoming: room "+r.incomingRoomID);
        }

    }

    private Vertex findStartVertex(){
        // get starting vertex
        for(Vertex v: levelGraph.getVertices()){
            if(v.getType().equals("start")){
                return v;
            }
        }
        return null;
    }

    private Tile[][] initEmptyLevel() {
        Tile[][] level = new Tile[levelWidth][levelHeight];

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                level[x][y] = new Tile(TileType.EMPTY);
            }
        }
        return level;
    }

    private Graph createLevelGraph() {
        GrammarManager grammarManager = new GrammarManager();
        grammarManager.setGrammar(loadGrammar());
        grammarManager.applyAllProductions();
        return grammarManager.getCurrentGraph();
    }

    private Grammar loadGrammar(){
        FileReader fr = new FileReader();
        try {
            return fr.loadGrammar("grammar1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Grammar();
    }

    public static void main(String [] arg){
        new LevelGeneratorTest();
    }


}
