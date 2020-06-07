package grid;

public class Tuple {
    private int row;
    private int col;
    private int value;
    private int boxes;
    private int cage;

    public Tuple(int r, int c, int v)
    {
        row = r;
        col = c;
        value = v;
        boxes = -1;
    }

    public Tuple(int r, int c, int v, int b)
    {
        row = r;
        col = c;
        value = v;
        boxes = b;
    }

    public Tuple(int r, int c, int v, int b, int ca)
    {
        row = r;
        col = c;
        value = v;
        boxes = b;
        cage = ca;
    }

    public Tuple(int v, int b)
    {
        value = v;
        boxes = b;
        row = -1;
        col = -1;
    }
    
    public int getBoxes() {
        return this.boxes;
    }

    public void setBoxes(int boxes) {
        this.boxes = boxes;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public int getVal()
    {
        return value;
    }

    public void setRC(int r, int c)
    {
        row = r;
        col = c;
    }

    public void setVal(int v)
    {
        value = v;
    }
	public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCage() {
        return this.cage;
    }

    public void setCage(int cage) {
        this.cage = cage;
    }
}