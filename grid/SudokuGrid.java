/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

 package grid;

 import java.io.*;
 import java.util.ArrayList;
 import java.util.List;

 
 
/**
 * Abstract class representing the general interface for a Sudoku grid.
 * Both standard and Killer Sudoku extend from this abstract class.
 */
public abstract class SudokuGrid
{
    public static final int EMPTY = -1;
    private int size = 0;
    private int numBoxes = 0;
    private int[] possibleValues;
    private List<Tuple> tuplesList = new ArrayList<Tuple>();
    private int[][] grid;
    private int numCages;
    List<Cage> cagesList = new ArrayList<Cage>();

    /**
     * Load the specified file and construct an initial grid from the contents
     * of the file.  See assignment specifications and sampleGames to see
     * more details about the format of the input files.
     *
     * @param filename Filename of the file containing the intial configuration
     *                  of the grid we will solve.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void initGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Write out the current values in the grid to file.  This must be implemented
     * in order for your assignment to be evaluated by our testing.
     *
     * @param filename Name of file to write output to.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void outputGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Converts grid to a String representation.  Useful for displaying to
     * output streams.
     *
     * @return String representation of the grid.
     */
    public abstract String toString();


    /**
     * Checks and validates whether the current grid satisfies the constraints
     * of the game in question (either standard or Killer Sudoku).  Override to
     * implement game specific checking.
     *
     * @return True if grid satisfies all constraints of the game in question.
     */
    public abstract boolean validate();

    //checks to see if there are any repeating numbers in the row
    public boolean rowConstraint(int row)
    {
        for (int c = 0; c<getSize(); c++)
        {
            for (int c1 = c+1; c1<getSize(); c1++)
            {
                if (getGrid()[row][c] == getGrid()[row][c1] && getGrid()[row][c]>0)
                {
                    return false;
                }
            }
        }

        return true;
    }

    //checks to see if there are any repeating values in the col
    public boolean colConstraint(int col)
    {
        for (int r = 0; r<getSize(); r++)
        {
            for (int r1 = r+1; r1<getSize(); r1++)
            {
                if (getGrid()[r][col] == getGrid()[r1][col] && getGrid()[r][col]>0)
                {
                    return false;
                }
            }
        }

        return true;
    }

    //finds the starting position of the box containing row and col
    public int[] getBoxStart(int row, int col)
    {
        int[] s = new int[2];
        for (int i = 0; i<getNumBoxes(); i++)
        {
            int ro = i*getNumBoxes();
            for (int r = ro; r<ro+getNumBoxes(); r++)
            {
                for (int j = 0; j<getNumBoxes(); j++)
                {
                    int co = j*getNumBoxes();
                    for (int c = j*getNumBoxes(); c<co+getNumBoxes(); c++)
                    {
                        if (r==row && c==col)
                        {
                            s[0] = ro;
                            s[1] = co;
                            return s;
                        }
                    }
                }
            }
        }

        return null;
    }

    public boolean boxConstraint(int row, int col)
    {
        int index = 0;
        int[] boxNum = new int[getSize()];

        int[] s = new int[2];
        s = getBoxStart(row, col);

        if (s == null)
        {
            return false;
        }

        //fills boxnum with all the values inside the box
        for (int r = s[0]; r<s[0]+getNumBoxes(); r++)
        {
            for (int c = s[1]; c<s[1]+getNumBoxes(); c++)
            {
                boxNum[index] = getGrid()[r][c];
                index++;
            }
        }

        //checks to see if there are any repeating numbers in the box
        for (int r = 0; r<getSize(); r++)
        {
            for (int r1 = r+1; r1<getSize(); r1++)
            {
                if (boxNum[r] == boxNum[r1] && boxNum[r]>0)
                {
                    return false;
                }
            }
        }        

        return true;
    }

    //checks to see if every number is within the range of possible values
    public boolean validNumbers()
    {
        for (int r = 0; r<getSize(); r++)
        {
            for (int c = 0; c<getSize(); c++)
            {
                if (getGrid()[r][c] > getPossibleValues().length)
                {
                    return false;
                }
            }
        }

        return true;
    }

    //checks if there are any empty spots in the grid
    public boolean isFull()
    {
        for (int row = 0; row<getSize(); row++)
        {
            for (int col = 0; col<getSize(); col++)
            {
                if (getGrid()[row][col]!=EMPTY)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int s)
    {
        size = s;
    }

    public int getNumBoxes()
    {
        return numBoxes;
    }

    public void setNumBoxes(int nc)
    {
        numBoxes = nc;
    }

    public List<Tuple> getTuples()
    {
        return tuplesList;
    }

    public void setTuples(List<Tuple> t)
    {
        tuplesList = t;
    }

    public int[] getPossibleValues()
    {
        return possibleValues;
    }

    public void setPossibleValue(int[] pv)
    {
        possibleValues = pv;
    }

    public int[][] getGrid()
    {
        return grid;
    }

    public void setGrid(int[][] g)
    {
        grid = g;
    }

    public List<Cage> getCagesList() {
        return this.cagesList;
    }

    public void setCagesList(List<Cage> cagesList) {
        this.cagesList = cagesList;
    }

    public int getNumCages() {
        return this.numCages;
    }

    public void setNumCages(int numCages) {
        this.numCages = numCages;
    }

    public StdSudokuGrid gridStdCopy()
    {
        StdSudokuGrid copyOfGrid = new StdSudokuGrid();
        copyOfGrid.setGrid(getGrid());
        copyOfGrid.setNumBoxes(getNumBoxes());
        copyOfGrid.setPossibleValue(getPossibleValues());
        copyOfGrid.setSize(getSize());
        copyOfGrid.setTuples(getTuples());
        

        return copyOfGrid;
    }

    public KillerSudokuGrid gridKillerCopy()
    {
        KillerSudokuGrid copyOfGrid = new KillerSudokuGrid();
        copyOfGrid.setGrid(getGrid());
        copyOfGrid.setNumBoxes(getNumBoxes());
        copyOfGrid.setPossibleValue(getPossibleValues());
        copyOfGrid.setSize(getSize());
        copyOfGrid.setCagesList(getCagesList());
        copyOfGrid.setNumCages(numCages);

        return copyOfGrid;
    }

} // end of abstract class SudokuGrid
