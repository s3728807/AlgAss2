/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.List;
import java.util.ArrayList;
import grid.StdSudokuGrid;
import grid.SudokuGrid;
import java.lang.Math;
import grid.Tuple;


/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver
{
    public AlgorXSolver() {
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
        Matrix matrix = new Matrix(grid.getSize());
        StdSudokuGrid solver = grid.gridStdCopy();

        matrix.cellConstraints(solver.getTuples());
        matrix.rowConstraints(solver.getTuples(), solver.getPossibleValues());
        matrix.colConstraints(solver.getTuples(), solver.getPossibleValues());

        int[][] boxes = findBoxes(solver);
        matrix.setBoxesList(boxes);
        matrix.boxConstraints(solver.getTuples(), solver.getPossibleValues(), (int) Math.sqrt(solver.getSize()));

        matrix.addPossibilitiesList(solver.getTuples(), solver.getPossibleValues());

        // List<List<Integer>> mat =  new ArrayList<List<Integer>>();
        // List<Integer> colCon = new ArrayList<Integer>();
        // for (int i = 0; i<matrix.getConstraintsList().size(); i++)
        // {
        //     colCon.add(0);
        // }

        // for (int i = 0; i<matrix.getPossibilitiesList().size(); i++)
        // {
        //     mat.add(colCon);
        // }

        // matrix.setGrid(mat);

        int constraintSize = (matrix.getSize()*matrix.getSize()) - solver.getTuples().size();
        int colCount = 0;
        int[][] temp = new int[matrix.getPossibilitiesList().size()][matrix.getConstraintsList().size()];

        //determining cell constraint satisfaction
        for (;colCount<constraintSize; colCount++)
        {
            Tuple currCon = matrix.getConstraintsList().get(colCount);
            //loops through every possibility
            for (int i = 0; i<matrix.getPossibilitiesList().size(); i++)
            {
                Tuple currTup = matrix.getPossibilitiesList().get(i);
                //checks if the possibile tuple matches the constraing
                if (currCon.getRow() == currTup.getRow() && currCon.getCol() == currTup.getCol())
                {
                    temp[i][colCount] = 1;
                }
            }
        }

        //determining row constraint satisfaction
        for (;colCount<constraintSize*2; colCount++)
        {
            Tuple currCon = matrix.getConstraintsList().get(colCount);
            //loops through every possibility
            for (int i = 0; i<matrix.getPossibilitiesList().size(); i++)
            {
                Tuple currTup = matrix.getPossibilitiesList().get(i);
                //checks if the possibile tuple matches the constraing
                if (currCon.getRow() == currTup.getRow() && currCon.getVal() == currTup.getVal())
                {
                    temp[i][colCount] = 1;
                }
            }
        }

        //determining col constraint satisfaction
        for (;colCount<constraintSize*3; colCount++)
        {
            Tuple currCon = matrix.getConstraintsList().get(colCount);
            //loops through every possibility
            for (int i = 0; i<matrix.getPossibilitiesList().size(); i++)
            {
                Tuple currTup = matrix.getPossibilitiesList().get(i);
                //checks if the possibile tuple matches the constraing
                if (currCon.getCol() == currTup.getCol() && currCon.getVal() == currTup.getVal())
                {
                    temp[i][colCount] = 1;
                }
            }
        }

        //determining box constraint satisfaction
        for (;colCount<constraintSize*4; colCount++)
        {
            Tuple currCon = matrix.getConstraintsList().get(colCount);
            //loops through every possibility
            for (int i = 0; i<matrix.getPossibilitiesList().size(); i++)
            {
                Tuple currTup = matrix.getPossibilitiesList().get(i);
                //checks if the possibile tuple matches the constraing
                if (currCon.getBoxes() == currTup.getBoxes() && currCon.getVal() == currTup.getVal())
                {
                    temp[i][colCount] = 1;
                }
            }
        }



        // String out = "";
        // for (int r = 0; r<matrix.getPossibilitiesList().size(); r++)
        // {
        //     for (int c = 0; c<matrix.getConstraintsList().size(); c++)
        //     {
        //         out += temp[r][c] + " ";
        //     }

        //     out += "\n";
        // }

        // System.out.println(out);

        matrix.setGrid(temp);

        matrix.setColumns(matrix.getConstraintsList().size());

        List<Integer> solved = exactCover(matrix);
        //System.out.println("size: ");
        for (int i:solved)
        {
            Tuple t = matrix.getPossibilitiesList().get(i);
            int[][] g = grid.getGrid();
            g[t.getRow()][t.getCol()] = t.getVal();
            grid.setGrid(g);
        }
        //System.out.println(out);

        return grid.validate();
    } // end of solve()

    //finds all the starting positions of every boxes
    public int[][] findBoxes(StdSudokuGrid solver)
    {
        int[][] boxes = new int[solver.getSize()][2];
        int x = 0;
        for (int row = 0; row<solver.getSize(); row+=Math.sqrt(solver.getSize()))
        {
            for (int col = 0; col<solver.getSize(); col+=Math.sqrt(solver.getSize()))
            {
                for (int j = 0; j<2; j++)
                {
                    if (j == 0)
                        boxes[x][j] = row;
                    else
                    {
                        boxes[x][j] = col;
                        x++;
                    }
                }
            }
        }

        return boxes;
    }

    public List<Integer> exactCover(Matrix mtr)
    {
        if (mtr.getColumns() == 0 )
            return mtr.getSolutionsList();
        
        int colLeastOnes = mtr.getLeastOnes();
        if (colLeastOnes == -1)
            return null;
        
        int r = 0;       
        //for each row in the column with the least ones 
        for (; r<mtr.getPossibilitiesList().size(); r++)
        {
            //if that row has a one in it
            if (mtr.getGrid()[r][colLeastOnes] == 1 && !mtr.isDeletedRow(r))
            {
                mtr.addToSolution(r);
                //System.out.println(r);

                //deep copy of the matrix
                Matrix testMatrix = new Matrix(mtr.getSize());
                List<Integer> delrows = new ArrayList<Integer>();
                for (int ro:mtr.getDeletedRows())    
                    delrows.add(ro);
                testMatrix.setDeletedRows(delrows);

                int[][] g = new int[mtr.getGrid().length][mtr.getGrid()[0].length];
                for (int ro = 0; ro<mtr.getGrid().length; ro++)
                {
                    for (int co = 0; co<mtr.getGrid()[ro].length; co++)
                    {
                        g[ro][co] = mtr.getGrid()[ro][co];
                    }   
                }
                testMatrix.setGrid(g);

                List<Tuple> l = new ArrayList<Tuple>();
                for (Tuple t:mtr.getConstraintsList())
                {
                    l.add(new Tuple(t.getRow(), t.getCol(), t.getVal(), t.getBoxes()));
                }
                testMatrix.setConstraintsList(l);

                List<Tuple> l1 = new ArrayList<Tuple>();
                for (Tuple t:mtr.getPossibilitiesList())
                {
                    l1.add(new Tuple(t.getRow(), t.getCol(), t.getVal(), t.getBoxes()));
                }
                testMatrix.setPossibilitiesList(l1);

                List<Integer> l2 = new ArrayList<Integer>();
                for (int t:mtr.getSolutionsList())
                {
                    l2.add(t);
                }
                testMatrix.setSolutionsList(l2);
                testMatrix.setColumns(mtr.getColumns());
                //end of deep copy

                //for every column in the row
                for (int j = mtr.getColumns()-1; j>=0; j--)
                {   
                    //if theres a one in it
                    if (mtr.getGrid()[r][j] == 1)
                    {
                        //for every row in each column
                        for (int i = 0; i<mtr.getPossibilitiesList().size(); i++)
                        {
                            //if theres a one, delete the row
                            if (mtr.getGrid()[i][j] == 1) 
                            {
                                testMatrix.addDeltedRow(i);
                            }
                        } 
                        //delete col
                        for (int x=j+1;x<testMatrix.getColumns(); x++) {
                            for (int y=0;y<testMatrix.getPossibilitiesList().size(); y++) {
                                testMatrix.getGrid()[y][x-1] = testMatrix.getGrid()[y][x];
                            }                    
                        }
                        testMatrix.getConstraintsList().remove(j);
                        testMatrix.setColumns(testMatrix.getColumns()-1);
                    }
                }
                List<Integer> solved = exactCover(testMatrix);
                if (solved!=null)
                {
                    testMatrix.getSolutionsList().addAll(solved);
                    return testMatrix.getSolutionsList();
                }
            }
        }
        return null;

    }
} // end of class AlgorXSolver
