/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;
import grid.StdSudokuGrid;


/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver
{

    public BackTrackingSolver() {
    } // end of BackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
        //System.out.println("solve");
        int row = 0;
        int col = 0;
        //finds the position that is empty 
        while (grid.getGrid()[row][col]!=SudokuGrid.EMPTY)
        {
            col++;
            if (col>=grid.getSize())
            {
                col = 0;
                row++;
            }
            if (row>=grid.getSize())
            {
                return true;
            }
        }

        //loops through every possible
        for (int testValue:grid.getPossibleValues())
        {
            //System.out.println("testbalue");
            StdSudokuGrid testGrid = grid.gridStdCopy();

            testGrid.getGrid()[row][col] = testValue;
            //checks all the constraints to see if it is legal
            if (testGrid.rowConstraint(row) && testGrid.colConstraint(col) && testGrid.boxConstraint(row, col))
            {
                //recursion
                if (solve(testGrid))
                {
                    grid = testGrid.gridStdCopy();
                    return true;
                }
            }
        }

        grid.getGrid()[row][col] = -1;

        //System.out.println("x");

        return false;
    } // end of solve()

} // end of class BackTrackingSolver()
