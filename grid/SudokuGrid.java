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
    private int numCages = 0;
    private int[] possibleValues;
    private List<Tuple> tuplesList = new ArrayList<Tuple>();
    private int[][] grid;
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

    public int getSize()
    {
        return size;
    }

    public void setSize(int s)
    {
        size = s;
    }

    public int getNumCages()
    {
        return numCages;
    }

    public void setNumCages(int nc)
    {
        numCages = nc;
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

    public StdSudokuGrid gridStdCopy()
    {
        StdSudokuGrid copyOfGrid = new StdSudokuGrid();
        copyOfGrid.setGrid(getGrid());
        copyOfGrid.setNumCages(getNumCages());
        copyOfGrid.setPossibleValue(getPossibleValues());
        copyOfGrid.setSize(getSize());
        copyOfGrid.setTuples(getTuples());
        

        return copyOfGrid;
    }

} // end of abstract class SudokuGrid
