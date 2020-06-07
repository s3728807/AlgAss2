package grid;

public class Cage {
    private int total;
    private int length;
    private int[][] coords;

    public Cage(int t, int s)
    {
        total = t;
        length = s;
        coords = new int[length][2];
    }

    public boolean has(int[] c)
    {
        for (int i = 0; i<length; i++)
        {
            if (coords[i][0] == c[0] && coords[i][1] == c[1])
            {
                return true;
            }
        }

        return false;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int[][] getCoords() {
        return this.coords;
    }

    public void setCoords(int[][] coords) {
        this.coords = coords;
    }

    
}