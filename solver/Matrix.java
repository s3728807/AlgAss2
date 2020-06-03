package solver;

import java.util.List;
import java.util.ArrayList;

import grid.StdSudokuGrid;
import grid.Tuple;

public class Matrix 
{
    private int size;
    private int[][] grid;
    private int columns = 0;

    private int[][] boxesList;

    private List<Tuple> constraintsList = new ArrayList<Tuple>();
    private List<Tuple> possibilitiesList = new ArrayList<Tuple>();
    private List<Integer> solutionsList = new ArrayList<Integer>();
    private List<Integer> deletedRows = new ArrayList<Integer>();
    private List<Integer> deletedCols = new ArrayList<Integer>();

    public Matrix(int s) 
    {
        size = s;
    }

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

    public int getLeastOnes()
    {
        int min = Integer.MAX_VALUE;
        int colNum =0 ;
        int[] oneCount = new int[constraintsList.size()];
        
        for (int c = 0; c < constraintsList.size(); c++)
        {
            for (int r = 0; r < possibilitiesList.size(); r++)
            {
                if (grid[r][c] == 1 && !isDeletedRow(r))
                {
                    oneCount[c]++;
                }
            }
        }

        for (int c = 0; c < constraintsList.size()-1; c++)
        {
            if (oneCount[c] <= min)
            {
                colNum = c;
                min = oneCount[c];
            }
        }

        if (min == 0)
            colNum = -1;

        return colNum;
    }

    public void addPossibilitiesList(List<Tuple> tList, int[] pvList)
    {
        for (int row = 0; row<size; row++)
        {
            for (int col = 0; col<size; col++)
            {
                for (int pv:pvList)
                {
                    if (possibilitiesCheck(tList, row, col, pv))
                    {
                        possibilitiesList.add(new Tuple(row, col, pv, findBox(row, col)));
                    }
                }
            }
        }
    }

    public boolean possibilitiesCheck(List<Tuple> tList, int row, int col, int pv)
    {
        for (Tuple t:tList)
        {
            if (t.getRow() == row && t.getCol() == col)
            {
                return false;
            }
        }

        return true;
    }

    public void cellConstraints(List<Tuple> tList)
    {
        for (int row = 0; row<size; row++)
        {
            for (int col = 0; col<size; col++)
            {
                if (cellMatchCheck(row, col, tList))
                    constraintsList.add(new Tuple(row, col, -1));
            }
        }
    }

    public boolean cellMatchCheck(int row, int col, List<Tuple> tList)
    {
        for (Tuple t:tList)
        {
            if (row == t.getRow() && col == t.getCol())
            {
                return false;
            }

        }

        return true;
    }

    public void rowConstraints(List<Tuple> tList, int[] pvList)
    {
        for (int row = 0; row<size; row++)
        {
            for (int pv:pvList)
            {
                if (rowMatchCheck(row, pv, tList, pvList))
                    constraintsList.add(new Tuple(row, -1, pv));
            }
        }
    }

    public boolean rowMatchCheck(int row, int pv, List<Tuple> tList, int[] pvList)
    {
        for (Tuple t:tList)
        {
            if (row == t.getRow() && pv == t.getVal())
                return false;
        }

        return true;
    }

    public void colConstraints(List<Tuple> tList, int[] pvList)
    {
        for (int col = 0; col<size; col++)
        {
            for (int pv:pvList)
            {
                if (colMatchCheck(col, pv, tList, pvList))
                    constraintsList.add(new Tuple(-1, col, pv));
            }
        }
    }

    public boolean colMatchCheck(int col, int pv, List<Tuple> tList, int[] pvList)
    {
        for (Tuple t:tList)
        {
            if (col == t.getCol() && pv == t.getVal())
                return false;
        }

        return true;
    }

    public void boxConstraints(List<Tuple> tList, int[] pvList, int size)
    {
        for (int i = 0; i<boxesList.length; i++)
        {
            for (int pv:pvList)
            {
                if (boxMatchCheck(pv, tList, boxesList[i], size))
                    constraintsList.add(new Tuple(pv, i));
            }
        }
    }

    public boolean boxMatchCheck(int val, List<Tuple> tList, int[] boxes, int size)
    {
        for (Tuple t:tList)
        {
            if (insideBox(boxes, t.getRow(), t.getCol(), size) && t.getVal() == val)
                return false;
        }

        return true;
    }

    public boolean insideBox(int[] boxes, int row, int col, int size)
    {
        
        int r = boxes[0];
        int c = boxes[1];

        for (int ro = r; ro<r+size; ro++)
        {
            for (int co = c; co<c+size; co++)
            {
                if (ro == row && co == col)
                    return true;
            }
        }

        return false;
    }

    public int findBox(int row, int col)
    {
        for (int i = 0; i<size; i++)
        {
            int r = boxesList[i][0];
            int c = boxesList[i][1];
            for (int ro = r; ro<r+Math.sqrt(size); ro++)
            {
                for (int co = c; co<c+Math.sqrt(size); co++)
                {
                    if (ro == row && co == col)
                        return i;
                }
            }
        }

        return -1;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int[][] getGrid() {
        return this.grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<Tuple> getConstraintsList() {
        return this.constraintsList;
    }

    public void setConstraintsList(List<Tuple> constraintsList) {
        this.constraintsList = constraintsList;
    }

    public List<Tuple> getPossibilitiesList() {
        return this.possibilitiesList;
    }

    public void setPossibilitiesList(List<Tuple> possibilitiesList) {
        this.possibilitiesList = possibilitiesList;
    }

    public List<Integer> getSolutionsList() {
        return this.solutionsList;
    }

    public void setSolutionsList(List<Integer> solutionsList) {
        this.solutionsList = solutionsList;
    }    

    public void addToSolution(int i)
    {
        solutionsList.add(i);
    }

    public int[][] getBoxesList() {
        return this.boxesList;
    }

    public void setBoxesList(int[][] boxes) {
        this.boxesList = boxes;
    }

    public List<Integer> getDeletedRows() {
        return this.deletedRows;
    }

    public void setDeletedRows(List<Integer> deleted) {
        this.deletedRows = deleted;
    }

    public List<Integer> getDeletedCols() {
        return this.deletedCols;
    }

    public void setDeletedCols(List<Integer> deletedCols) {
        this.deletedCols = deletedCols;
    }

    public boolean isDeletedRow(int row)
    {
        for (int i:deletedRows)
            if (i == row)
                return true;
        
        return false;
    }

    public void addDeltedRow(int row)
    {
        if (!deletedRows.contains(row)) 
            deletedRows.add(row);
    }

    public boolean isDeletedCol(int col)
    {
        for (int i:deletedCols)
            if (i == col)
                return true;
        
        return false;
    }

    public void addDeltedCol(int col)
    {
        if (!deletedCols.contains(col)) 
            deletedCols.add(col);
    }


}