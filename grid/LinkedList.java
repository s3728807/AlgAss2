package grid;

import solver.Matrix;

public class LinkedList {
    private Node head;
    private Matrix matrixHelp;

    public LinkedList(Matrix m, StdSudokuGrid std)
    {
        matrixHelp = m;
        matrixHelp.cellConstraints(std.getTuples());
        matrixHelp.rowConstraints(std.getTuples(), std.getPossibleValues());
        matrixHelp.colConstraints(std.getTuples(), std.getPossibleValues());

        matrixHelp.setBoxesList(matrixHelp.findBoxes(std));
        matrixHelp.boxConstraints(std.getTuples(), std.getPossibleValues(), (int) Math.sqrt(std.getSize()));

        matrixHelp.addPossibilitiesList(std.getTuples(), std.getPossibleValues());

        head = new Node(new Tuple(-100, -100, -100, -100), 0, 0);

        for (Tuple t:matrixHelp.getConstraintsList())
        {
            head.setEast(new Node(t, 0, head.getCol()+1));
            head.getEast().setWest(head);
            right();
        }

        while (head.getWest()!=null)
        {
            System.out.println("b: "+head.getValue().getBoxes()+" r: "+head.getValue().getRow()+" c: "+head.getValue().getCol()+" v: "+head.getValue().getVal());
            left();
        }

        for (Tuple t:matrixHelp.getPossibilitiesList())
        {
            head.setSouth(new Node(t, head.getRow(), 0));
            head.getSouth().setNorth(head);
            down();
        }

        while (head.getNorth()!=null)
        {
            up();
        }

        System.out.println("");
    }





    public Node getHead() {
        return this.head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public void up()
    {
        head = head.getNorth();
    }

    public void right()
    {
        head = head.getEast();
    }

    public void left()
    {
        head = head.getEast();
    }

    public void down()
    {
        head = head.getSouth();
    }
    
}