import com.stuckinadrawer.graphs.Vertex;

import java.util.ArrayList;

public class Room{
    int x, y, width, height;
    int groupID;
    ArrayList<Vertex> elements = new ArrayList<Vertex>();
    int incomingRoomID = -1;
    boolean initalPos = false;

    int forceX, forceY = 0;

    public Room(){
        this.width = 3;
        this.height = 3;
    }

    public void expand() {
        this.width++;
        this.height++;

    }

    public void applyForce() {
        this.x += this.forceX / 2 + 1;
        this.y += this.forceY / 2 + 1;

    }
}