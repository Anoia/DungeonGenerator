import com.stuckinadrawer.graphs.Vertex;

import java.util.ArrayList;

public class Room{
    int x, y, width, height;
    int id;
    ArrayList<Vertex> elements = new ArrayList<Vertex>();
    int incomingRoomID = -1;

    public Room(){
        this.width = 3;
        this.height = 3;
    }

    public void expand() {
        this.width++;
        this.height++;

    }
}