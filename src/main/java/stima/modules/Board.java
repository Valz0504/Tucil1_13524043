package stima.modules;

public class Board {
    
    /**
     * Atribut
     */
    private char[][] content;
    private int rows, cols;

    /**
     * { Membuat Board kosong berdasarkan masukan jumlah baris dan kolom }
     * 
     * @param row
     * @param col
     */
    public Board(int row, int col)
    {
        this.rows = row;
        this.cols = col;
        this.content = new char[row][col];
    }

    /**
     * { Membuat Board dengan mengcopy Board lain }
     * 
     * @param Papan
     */
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

    /**
     * Selektor baris
     * 
     * @return jumlah baris dari Boardnya
     */
    public int getRow()
    {
        return this.rows;
    }

    /**
     * Selektor kolom
     * 
     * @return jumlah kolom dari Boardnya
     */
    public int getCol()
    {
        return this.cols;
    }

    /**
     * Getter element
     * 
     * @param row
     * @param col
     * @return elemen yang berada di baris row dan kolom col
     */
    public char getElmt(int row, int col)
    {
        return this.content[row][col];
    }

    /**
     * { Set elemen pada Board dengan value baru }
     * 
     * @param row
     * @param col
     * @param value
     */
    public void setElmt(int row, int col, char value)
    {
        this.content[row][col] = value;
    }
}
