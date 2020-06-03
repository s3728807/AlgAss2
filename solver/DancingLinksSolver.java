/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.List;

import grid.LinkedList;
import grid.Node;
import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
{
    public DancingLinksSolver() {
    } // end of DancingLinksSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
        StdSudokuGrid stdGrid = grid.gridStdCopy();
        LinkedList linked = new LinkedList(new Matrix(grid.getSize()), stdGrid);

        List<Integer> solved = exactCover(linked.getHead());
        for (int s:solved)
        {
            System.out.println(s);
        }
        // placeholder
        return false;
    } // end of solve()

    public List<Integer> exactCover(Node n)
    {
        return null;
    }

} // end of class DancingLinksSolver
