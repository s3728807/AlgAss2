package grid;

public class Tuple {
    private int row;
    private int col;
    private int value;

    public Tuple(int r, int c, int v)
    {
        row = r;
        col = c;
        value = v;
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