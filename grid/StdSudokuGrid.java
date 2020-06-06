/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors


/**
 * Class implementing the grid for standard Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task A and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class StdSudokuGrid extends SudokuGrid
{

    public StdSudokuGrid() {
        super();
    } // end of StdSudokuGrid()

    @Override
    public void initGrid(String filename)
        throws FileNotFoundException, IOException
    {
        //scans file input
        Scanner scan = new Scanner(new File(filename));
        List<String> in = new ArrayList<String>();
        //parses file input into string, separated by whitespaces or ','
        scan.useDelimiter("\\s+|,");
        while(scan.hasNext())
        {
            in.add(scan.next());
        }
        int[][] c = new int[10000][100];

        //converts the size of the sudoku puzzle into an int
        c[0][0] = Integer.parseInt(in.get(0));

        //converts the possible values into ints
        for (int x = 0; x<c[0][0]; x++)
                    c[1][x] = Integer.parseInt(in.get(1+x));
        for (int i = 0; i<=c[0][0]; i++)
            in.remove(0);

        //converts the the tuples into ints
        int size = in.size();
        for (int i = 2; i<(size/3)+2;i++)
        {
            for (int x = 0; x<3; x++)
            {
                c[i][x] = Integer.parseInt(in.get(0));
                in.remove(0);
            }
        }

        //sets the puzzle size
        setSize(c[0][0]);
        //sets the possible values of the sudoku
        int[] pv = new int[getSize()];
        for (int i = 0; i<getSize(); i++)
        { 
            pv[i] = c[1][i];
        }
        setPossibleValue(pv);

        setNumBoxes((int) Math.sqrt(getSize()));
        
        //initially fills the grid with -1, to represent empty spaces
        int[][] grid = new int[getSize()][getSize()];

        for (int i = 0; i<getSize(); i++)
        {
            for (int j = 0; j<getSize(); j++)
            {
                grid[i][j] = EMPTY;
            }
        }

        //creates an array of the tuples from the input
        List<Tuple> list = new ArrayList<Tuple>();
        for (int i = 2; i<(size/3)+2; i++)
        {
            list.add(new Tuple(c[i][0], c[i][1], c[i][2])); 
        }

        //using the array of tuples, fills the sudoku with initial values
        setTuples(list);
        for (Tuple t:list)
        {
            grid[t.getRow()][t.getCol()] = t.getVal();
        }
        setGrid(grid);

      //  System.out.println("x");
    } // end of initBoard()


    @Override
    public void outputGrid(String filename)
        throws FileNotFoundException, IOException
    {
        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write(toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } 
        catch (IOException e) {
            System.out.println("An error occurred while trying to save the file.");
            e.printStackTrace();
          }
    } // end of outputBoard()


    @Override
    public String toString() {
        int[][] g = getGrid();
        String str = "";
        for (int r = 0; r<getSize(); r++)
        {
            for (int c = 0; c<getSize(); c++)
            {
                if( g[r][c] > 0)
                {
                    str += g[r][c];
                }
                else
                {
                    str += " ";
                }
                str += ",";
            }
            str += "\n";
        }

        return str;
    } // end of toString()


    @Override
    public boolean validate() {
        boolean rowBool = true;
        for (int row = 0; row<getSize(); row++)
        {
            if (!rowConstraint(row))
            {
                rowBool = false;
            }
        }

        boolean colBool = true;
        for (int col = 0; col<getSize(); col++)
        {
            if (!rowConstraint(col))
            {
                colBool = false;
            }
        }

        boolean boxBool = true;
        for (int i = 0; i<getNumBoxes(); i++)
        {
            for (int j = 0; j<getNumBoxes(); j++)
            {
                if(!boxConstraint(i*getNumBoxes(), j*getNumBoxes()))
                {
                    boxBool = false;
                }
            }
        }
        
        return boxBool && colBool && rowBool && validNumbers();
    } // end of validate()

    public int totalPV()
    {
        int totalPV = 0;
        for (int i:getPossibleValues())
        {
            totalPV += i;
        }

        return totalPV;
    }

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

        for (int r = s[0]; r<s[0]+getNumBoxes(); r++)
        {
            for (int c = s[1]; c<s[1]+getNumBoxes(); c++)
            {
                boxNum[index] = getGrid()[r][c];
                index++;
            }
        }

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

    public boolean validNumbers()
    {
        boolean valid = true;
        for (int r = 0; r<getSize(); r++)
        {
            for (int c = 0; c<getSize(); c++)
            {
                if (getGrid()[r][c] > getPossibleValues().length)
                {
                    valid = false;
                }
            }
        }

        return valid;
    }

    public boolean isFull()
    {
        boolean full = true;

        for (int row = 0; row<getSize(); row++)
        {
            for (int col = 0; col<getSize(); col++)
            {
                if (getGrid()[row][col]!=EMPTY)
                {
                    full = false;
                }
            }
        }

        return full;
    }

} // end of class StdSudokuGrid
