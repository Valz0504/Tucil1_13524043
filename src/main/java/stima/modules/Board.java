package stima.modules;

public class Board {
    
    private char[][] content;
    private int rows, cols;

    public Board(int row, int col)
    {
        this.rows = row;
        this.cols = col;
        this.content = new char[row][col];
    }

    public Board(Board Papan)
    {
        this.rows = Papan.rows;
        this.cols = Papan.cols;
        this.content = new char[Papan.rows][Papan.cols];
        for (int i = 0; i < this.rows; i++)
        {
            for (int j = 0; j < this.cols; j++) 
            {
                this.setElmt(i, j, Papan.content[i][j]);
            }
        }
    }

    public int getRow()
    {
        return this.rows;
    }

    public int getCol()
    {
        return this.cols;
    }

    public char getElmt(int row, int col)
    {
        return this.content[row][col];
    }

    public void setElmt(int row, int col, char value)
    {
        this.content[row][col] = value;
    }
}
