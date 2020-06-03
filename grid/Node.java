package grid;

public class Node 
{
    private Node north;
    private Node east;
    private Node west;
    private Node south;
    private Tuple value;
    private int row;
    private int col;

    public Node(Tuple v, int r, int c)
    {
        north = null;
        east = null;
        west = null;
        south = null;
        value = v;
        row = r;
        col = c;
    }

    public Node(Tuple v, Node n, Node e, Node w, Node s)
    {
        north = n;
        east = e;
        west = w;
        south = s;
        value = v;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return this.col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Node getNorth() {
        return this.north;
    }

    public void setNorth(Node north) {
        this.north = north;
    }

    public Node getEast() {
        return this.east;
    }

    public void setEast(Node east) {
        this.east = east;
    }

    public Node getWest() {
        return this.west;
    }

    public void setWest(Node west) {
        this.west = west;
    }

    public Node getSouth() {
        return this.south;
    }

    public void setSouth(Node south) {
        this.south = south;
    }

    public Tuple getValue() {
        return this.value;
    }

    public void setValue(Tuple value) {
        this.value = value;
    }
        
}