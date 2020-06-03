package grid;

public class Tuple {
    private int row;
    private int col;
    private int value;
    private int boxes;

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
}