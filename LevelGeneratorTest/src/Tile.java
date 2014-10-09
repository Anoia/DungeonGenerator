public class Tile {

    public TileType tileType;
    public int roomID = -1;

    public Tile(TileType tileType){
        this.tileType = tileType;
    }


    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

}
