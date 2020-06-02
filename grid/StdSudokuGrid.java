/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;


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
        Scanner scan = new Scanner(new File(filename));
        List<String> in = new ArrayList<String>();
        while(scan.hasNext())
        {
            in.add(scan.nextLine());
        }
        int[][] c = new int[in.size()][10];
        
        for (int i = 0; i<in.size();i++)
        {
            int x = 0;
            for (int j = 0; j<in.get(i).length(); j++)
            {
                if (in.get(i).charAt(j) != ' ' && in.get(i).charAt(j) != ',')
                {
                    c[i][x] = Character.getNumericValue(in.get(i).charAt(j));
                    x++;
                    
                }                
            }
        }

        setSize(c[0][0]);
        int[] pv = new int[getSize()];
        for (int i = 0; i<getSize(); i++)
        { 
            pv[i] = c[1][i];
        }
        setPossibleValue(pv);
        setNumCages((int) Math.sqrt(getSize()));
        
        int[][] grid = new int[getSize()][getSize()];

        for (int i = 0; i<getSize(); i++)
        {
            for (int j = 0; j<getSize(); j++)
            {
                grid[i][j] = EMPTY;
            }
        }

        List<Tuple> list = new ArrayList<Tuple>();
        for (int i = 2; i<in.size(); i++)
        {
            list.add(new Tuple(c[i][0], c[i][1], c[i][2])); 
        }

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
        // TODO
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
        for (int i = 0; i<getNumCages(); i++)
        {
            for (int j = 0; j<getNumCages(); j++)
            {
                if(!boxConstraint(i*getNumCages(), j*getNumCages()))
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
        for (int i = 0; i<getNumCages(); i++)
        {
            int ro = i*getNumCages();
            for (int r = ro; r<ro+getNumCages(); r++)
            {
                for (int j = 0; j<getNumCages(); j++)
                {
                    int co = j*getNumCages();
                    for (int c = j*getNumCages(); c<co+getNumCages(); c++)
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

        for (int r = s[0]; r<s[0]+getNumCages(); r++)
        {
            for (int c = s[1]; c<s[1]+getNumCages(); c++)
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
