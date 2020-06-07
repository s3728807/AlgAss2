package solver;

import java.util.ArrayList;
import java.util.List;

import grid.Cage;
import grid.Tuple;

public class KillerMatrix extends Matrix
{
    private List<Cage> cagesList = new ArrayList<Cage>();

    public KillerMatrix(int s, List<Cage> cl)
    {
        super(s);
        cagesList = cl;
    }

    public void addPossibilitiesList(int[] pvList)
    {
        for (int row = 0; row<getSize(); row++)
        {
            for (int col = 0; col<getSize(); col++)
            {
                for (int pv:pvList)
                {
                    getPossibilitiesList().add(new Tuple(row, col, pv, findBox(row, col), findCage(row, col)));
                }
            }
        }
    }

    public int findBox(int row, int col)
    {
        for (int i = 0; i<getSize(); i++)
        {
            int r = getBoxesList()[i][0];
            int c = getBoxesList()[i][1];
            for (int ro = r; ro<r+Math.sqrt(getSize()); ro++)
            {
                for (int co = c; co<c+Math.sqrt(getSize()); co++)
                {
                    if (ro == row && co == col)
                        return i;
                }
            }
        }

        return -1;
    }

    public int findCage(int row, int col)
    {
        int[] c = new int[]{row, col};
        for (int i = 0; i<cagesList.size(); i++)
        {
            if (cagesList.get(i).has(c))
                return i;
        }

        return -1;
    }

    public void cellConstraints()
    {
        for (int row = 0; row<getSize(); row++)
        {
            for (int col = 0; col<getSize(); col++)
            {
                getConstraintsList().add(new Tuple(row, col, -1));
            }
        }
    }

    public void rowConstraints(int[] pvList)
    {
        for (int row = 0; row<getSize(); row++)
        {
            for (int pv:pvList)
            {
                getConstraintsList().add(new Tuple(row, -1, pv));
            }
        }
    }

    public void colConstraints(int[] pvList)
    {
        for (int col = 0; col<getSize(); col++)
        {
            for (int pv:pvList)
            {
                getConstraintsList().add(new Tuple(-1, col, pv));
            }
        }
    }

    public void boxConstraints( int[] pvList)
    {
        for (int i = 0; i<getBoxesList().length; i++)
        {
            for (int pv:pvList)
            {
                    getConstraintsList().add(new Tuple(pv, i));
            }
        }
    }
    
}