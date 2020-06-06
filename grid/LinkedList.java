package grid;

import java.util.ArrayList;
import java.util.List;

import solver.Matrix;

public class LinkedList {
    private Node head;
    private Matrix matrixHelp;
    private StdSudokuGrid stdG;

    private List<Integer> solution = new ArrayList<Integer>();

    public LinkedList(Matrix m, StdSudokuGrid std) {
        stdG = std;
        matrixHelp = m;
        matrixHelp.cellConstraints(std.getTuples());
        matrixHelp.rowConstraints(std.getTuples(), std.getPossibleValues());
        matrixHelp.colConstraints(std.getTuples(), std.getPossibleValues());

        matrixHelp.setBoxesList(matrixHelp.findBoxes(std));
        matrixHelp.boxConstraints(std.getTuples(), std.getPossibleValues(), (int) Math.sqrt(std.getSize()));

        matrixHelp.addPossibilitiesList(std.getTuples(), std.getPossibleValues());

        head = new Node(new Tuple(-100, -100, -100, -100), -100, -100);
        int c = 0;
        for (Tuple t : matrixHelp.getConstraintsList()) {
            head.setEast(new Node(t, -1, c++));
            head.getEast().setWest(head);
            head = head.getEast();
        }

        while (head.getWest() != null) {
            // System.out.println("b: "+head.getTuple().getBoxes()+" r:
            // "+head.getTuple().getRow()+" c: "+head.getTuple().getCol()+" v:
            // "+head.getTuple().getVal());
            head = head.getWest();
        }
        int r = 0;
        for (Tuple t : matrixHelp.getPossibilitiesList()) {
            head.setSouth(new Node(t, r++, -1));
            head.getSouth().setNorth(head);
            head = head.getSouth();
        }
        // System.out.println("1");
        while (head.getNorth() != null) {
            head = head.getNorth();
        }

        int perConstraint = (std.getSize() * std.getSize()) - std.getTuples().size();
        int colCount = 0;
        Node possible = head.getSouth();
        Node curr = head;
        // cell constraints
        // System.out.println("constraint 1");
        while (colCount < perConstraint) {
            // goes through every constraint
            curr = curr.getEast();
            while (possible != null) {
                if (curr.getTuple().getRow() == possible.getTuple().getRow()
                        && curr.getTuple().getCol() == possible.getTuple().getCol()) {
                    addNode(curr, possible);
                }
                // go down every posibility
                possible = possible.getSouth();
            }
            // toString1();
            possible = head.getSouth();
            colCount++;
        }

        // System.out.println("constraint 2");
        colCount = 0;
        possible = head.getSouth();
        // row constraints
        while (colCount < perConstraint) {
            // goes through every constraint
            curr = curr.getEast();
            while (possible != null) {
                if (curr.getTuple().getRow() == possible.getTuple().getRow()
                        && curr.getTuple().getVal() == possible.getTuple().getVal()) {
                    addNode(curr, possible);
                }
                // go down every posibility
                possible = possible.getSouth();
            }

            possible = head.getSouth();
            colCount++;
        }

        colCount = 0;
        possible = head.getSouth();
        // col constraints
        while (colCount < perConstraint) {
            // goes through every constraint
            curr = curr.getEast();
            while (possible != null) {
                if (curr.getTuple().getCol() == possible.getTuple().getCol()
                        && curr.getTuple().getVal() == possible.getTuple().getVal()) {
                    addNode(curr, possible);
                }
                // go down every posibility
                possible = possible.getSouth();
            }

            possible = head.getSouth();
            colCount++;
        }

        colCount = 0;
        possible = head.getSouth();
        // box constraints
        while (colCount < perConstraint) {
            // goes through every constraint
            curr = curr.getEast();
            while (possible != null) {
                if (curr.getTuple().getBoxes() == possible.getTuple().getBoxes()
                        && curr.getTuple().getVal() == possible.getTuple().getVal()) {
                    addNode(curr, possible);
                }
                // go down every posibility
                possible = possible.getSouth();
            }

            possible = head.getSouth();
            colCount++;
        }

        // System.out.println("end");
    }

    public LinkedList(StdSudokuGrid m)
    {
        stdG = m;
    }

    public void deleteCol(int colNum)
    {
        Node curr = head;
        Node prev = curr;
        Node dt = head;
        while (dt!=null)
        {
            curr = dt;
            prev = curr;
            while (curr!=null && curr.getCol()!=colNum)
            {
                prev = curr;
                curr = curr.getEast();
            }
            
        
            if (prev!=null && curr!=null)
                prev.setEast(curr.getEast());
            if (curr!=null && curr.getEast()!=null)
                curr.getEast().setWest(prev);
            
            if (curr!=null)
            {
                curr.setNorth(null);
                curr.setEast(null);
                curr.setWest(null);
            }

            dt = dt.getEast();
        } 
    }

    public void deleteRow(int rowNum)
    {
        Node curr = head;
        Node prev = curr;
        Node rt = head;
        while (rt!=null && rt.getEast()!=null)
        {
            curr = rt;
            prev = curr;
            while (curr!=null && curr.getRow()!=rowNum)
            {
                prev = curr;
                curr = curr.getSouth();
            }
            
        
            if (prev!=null && curr!=null)
                prev.setSouth(curr.getSouth());
            if (curr!=null && curr.getSouth()!=null)
                curr.getSouth().setNorth(prev);
            
            if (curr!=null)
            {
                curr.setNorth(null);
                curr.setSouth(null);
                curr.setWest(null);
            }

            rt = rt.getEast();
        } 
    }

    public int colCount()
    {
        int count = 0;
        Node curr = head.getEast();
        while (curr!=null)
        {
            count++;
            curr = curr.getEast();
        }
        return count;
    }

    public List<Node> printCol()
    {
        List<Node> n = new ArrayList<Node>();
        Node curr = head.getEast();
        while (curr!=null)
        {
            n.add(curr);
            curr = curr.getEast();
        }

        return n;
    }

    public List<Node> printRow()
    {
        List<Node> n = new ArrayList<Node>();
        Node curr = head.getSouth();
        while (curr!=null)
        {
            n.add(curr);
            curr = curr.getSouth();
        }

        return n;
    }

    public void copyNode(Node mimic)
    {
        List<Integer> colNum = new ArrayList<Integer>();
        List<Tuple> colConstraints = new ArrayList<Tuple>();
        List<Integer> rowNum = new ArrayList<Integer>();
        List<Tuple> possibilities = new ArrayList<Tuple>();

        Node col = mimic.getEast();
        while (col!=null)
        {
            colNum.add(col.getCol());
            colConstraints.add(new Tuple(col.getTuple().getRow(), col.getTuple().getCol(), col.getTuple().getVal(), col.getTuple().getBoxes()));
            col = col.getEast();
        }

        Node row = mimic.getSouth();
        while (row!=null)
        {
            rowNum.add(row.getRow());
            possibilities.add(new Tuple(row.getTuple().getRow(), row.getTuple().getCol(), row.getTuple().getVal(), row.getTuple().getBoxes()));
            row = row.getSouth();
        }
        head = new Node(new Tuple(-100, -100, -100, -100), -100, -100);
        int i = 0;
        for (Tuple t : colConstraints) {
            head.setEast(new Node(t, -1, colNum.get(i)));
            i++;
            head.getEast().setWest(head);
            head = head.getEast();
        }

        while (head.getWest() != null) {
            head = head.getWest();
        }

        i = 0;
        for (Tuple t : possibilities) {
            head.setSouth(new Node(t, rowNum.get(i), -1));
            i++;
            head.getSouth().setNorth(head);
            head = head.getSouth();
        }
        // System.out.println("1");
        while (head.getNorth() != null) {
            head = head.getNorth();
        }
        
        row = head.getSouth();
        col = head.getEast();
        // cell constraints
        int cons = (stdG.getSize()*stdG.getSize())-stdG.getTuples().size();
        while (col.getCol()<cons) {
            while (row != null) {
                if (col.getTuple().getRow() == row.getTuple().getRow()
                        && col.getTuple().getCol() == row.getTuple().getCol()) {
                    addNode(col, row);
                }
                // go down every posibility
                row = row.getSouth();
            }
            
            // goes through every constraint
            col = col.getEast();
            row = head.getSouth();
        }

        // row constraints
        while (col.getCol()<cons*2) {
            while (row != null) {
                if (col.getTuple().getRow() == row.getTuple().getRow()
                        && col.getTuple().getVal() == row.getTuple().getVal()) {
                    addNode(col, row);
                }
                // go down every posibility
                row = row.getSouth();
            }
            // goes through every constraint
            col = col.getEast();

            row = head.getSouth();
        }

        // col constraints
        while (col.getCol()<cons*3) {
            while (row != null) {
                if (col.getTuple().getCol() == row.getTuple().getCol()
                        && col.getTuple().getVal() == row.getTuple().getVal()) {
                    addNode(col, row);
                }
                // go down every posibility
                row = row.getSouth();
            }
            // goes through every constraint
            col = col.getEast();

            row = head.getSouth();
        }

        // box constraints
        while (col!=null && col.getCol()<cons*4) {
            while (row != null) {
                if (col.getTuple().getBoxes() == row.getTuple().getBoxes()
                        && col.getTuple().getVal() == row.getTuple().getVal()) {
                    addNode(col, row);
                }
                // go down every posibility
                row = row.getSouth();
            }
            // goes through every constraint
            col = col.getEast();

            row = head.getSouth();
        }
    }
    public int leastOnes()
    {
        int test = Integer.MAX_VALUE;
        Node curr = head.getEast();
        int col = 1;
        int count = 0;
        while (curr != null)
        {
            Node d = curr;
            while (d.getSouth() != null)
            {
                count++;
                d = d.getSouth();
            }
            if (count < test)
            {
                col = curr.getCol();
                test = count;
            }
            count = 0;
            curr = curr.getEast();
        }

        return col;
    }

    public void addNode(Node cS, Node pE)
    {
        //go down the column
        while (cS.getSouth()!=null)
        {
            cS = cS.getSouth();
        }

        //go right of the possibility
        while (pE.getEast()!=null)
        {
            pE = pE.getEast();
        }
        Node newNode = new Node(cS, null, pE, null, pE.getRow(), cS.getCol());
        newNode.getNorth().setSouth(newNode);
        newNode.getWest().setEast(newNode);
    }

    public void toString1(Node n)
    {
        Node r = n.getSouth();
        Node c = r.getEast();
        int s = stdG.getSize()*stdG.getSize() - stdG.getTuples().size();
        int[][] coord = new int[s*s*s][2];
        int l = 0;
        while (r != null)
        {
            while (c != null)
            {
                if (c.getRow()>=0 && c.getCol()>=0)
                {
                    coord[l][0] = c.getRow();
                    coord[l][1] = c.getCol();
                    l++;
                }
                c = c.getEast();
            }
            r = r.getSouth();
            if (r != null)
                c = r.getEast();
        }
        String[][] out = new String[s*4][s*4];
        for (int ro = 0; ro<s*4; ro++)
        {
            for (int co = 0; co<s*4; co++)
            {
                out[ro][co] = "  ";
            }
        }

        for (int i = 0; i<s*s*s; i++)
        {
            out[coord[i][0]][coord[i][1]] = "1";
        }

        String out1 = "";
        for (int ro = 0; ro<s*4; ro++)
        {
            for (int co = 0; co<s*4; co++)
            {
                String a = out[ro][co];
                out1 += a;
            }

            out1 += "\n";
        }

        System.out.println(out1);
        
    }

    public List<Integer> getSolution() {
        return solution;
    }

    public void setSolution(List<Integer> solution) {
        this.solution = solution;
    }

    public Node getHead() {
        return this.head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Matrix getMatrixHelp() {
        return this.matrixHelp;
    }

    public void setMatrixHelp(Matrix matrixHelp) {
        this.matrixHelp = matrixHelp;
    }

    public StdSudokuGrid getStdG() {
        return this.stdG;
    }

    public void setStdG(StdSudokuGrid stdG) {
        this.stdG = stdG;
    }
    
}