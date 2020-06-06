/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.List;

import grid.LinkedList;
import grid.Node;
import grid.StdSudokuGrid;
import grid.SudokuGrid;
import grid.Tuple;


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
        Matrix matrix = new Matrix(grid.getSize());
        LinkedList linked = new LinkedList(matrix, stdGrid);

        //linked.toString1(linked.getHead());

        //linked.toString1(linked.getHead());

                
        List<Integer> solved = exactCover(linked);
        for (int i:solved)
        {
            Tuple t1 = matrix.getPossibilitiesList().get(i);
            int[][] g = grid.getGrid();
            g[t1.getRow()][t1.getCol()] = t1.getVal();
            grid.setGrid(g);
        }
        // placeholder
        return false;
    } // end of solve()

    public List<Integer> exactCover(LinkedList linkedList)
    {
        if (linkedList.colCount() == 0)
            return linkedList.getSolution();
        
        if (linkedList.getHead().getEast() == null)
            return null;
        
        int col = linkedList.leastOnes();

        Node outRow = linkedList.getHead();
        //finds the column to go down
        while(outRow.getCol()!=col)
        {
            outRow = outRow.getEast();
        }
        //System.out.println("col with least ones: "+outRow.getCol());
        
        while (outRow.getSouth() != null)
        {
            outRow = outRow.getSouth();

            LinkedList list = new LinkedList(linkedList.getStdG());
            list.copyNode(linkedList.getHead());
            
            linkedList.getSolution().add(outRow.getRow());
            //System.out.println("solution: "+outRow.getRow());
            List<Integer> sol = new ArrayList<Integer>();
            List<Integer> deleteRows = new ArrayList<Integer>();
            List<Integer> deleteCols = new ArrayList<Integer>();
            for (int i:linkedList.getSolution())
            {
                sol.add(i);
            }
            list.setSolution(sol);
            Node currentCol = list.getHead();
            //finds the row to go right
            while(currentCol.getRow() != outRow.getRow())
            {
                currentCol = currentCol.getSouth();
            }

            while (currentCol.getEast()!=null)
            {
                currentCol = currentCol.getEast();
                Node inRow = list.getHead();    
                while (inRow.getCol()!=currentCol.getCol())
                {
                    inRow = inRow.getEast();
                }
                while (inRow.getSouth()!=null)
                {
                    inRow = inRow.getSouth();
                    //System.out.println("delete row: "+inRow.getRow());
                    if (!deleteRows.contains(inRow.getRow())) deleteRows.add(inRow.getRow());
                    //list.deleteRow(delRow.getRow());
                }
                //System.out.println("    delete col: "+currentCol.getCol());
                if (!deleteCols.contains(currentCol.getCol())) deleteCols.add(currentCol.getCol());
                //System.out.println("a");
                //list.deleteCol(delCol.getCol());
            }
            // System.out.println("start row length: "+list.printRow().size());
            for (int r:deleteRows)
            {
                //System.out.println("inside row length: "+list.printRow().size());
                list.deleteRow(r);
            }
            // System.out.println("end row length: "+list.printRow().size());
            // System.out.println("start col length: "+list.printCol().size());
            for (int c:deleteCols)
            {
                // if (list.printCol().size()==4)
                //     System.out.println("inside col length: "+list.printCol().size());
                list.deleteCol(c);
            }
            // System.out.println("end col length: "+list.printCol().size());
            // System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            List<Integer> s = exactCover(list);
            if (s!=null)
            {
                list.getSolution().addAll(s);
                return list.getSolution();
            }
        }
        return null;
    }

} // end of class DancingLinksSolver
