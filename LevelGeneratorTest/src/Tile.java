public enum Tile {
    EMPTY, WALL, ROOM, CORRIDOR, ENTRANCE, EXIT, OPPONENT, BOSS, KEY, LOCK, TRAP, CHEST, BUFF;

    private int roomID = -1;


    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
}
