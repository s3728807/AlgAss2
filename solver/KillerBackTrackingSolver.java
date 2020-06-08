/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.KillerSudokuGrid;
import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver
{
    public KillerBackTrackingSolver() {
    } // end of KillerBackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
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
            KillerSudokuGrid testGrid = grid.gridKillerCopy();

            testGrid.getGrid()[row][col] = testValue;

            //checks all the constraints to see if it is legal
            if (testGrid.rowConstraint(row) && testGrid.colConstraint(col) && testGrid.boxConstraint(row, col) && testGrid.cageConstraints(row, col))
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

        // placeholder
        return false;
    } // end of solve()

} // end of class KillerBackTrackingSolver()
