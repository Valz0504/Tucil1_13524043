package stima.modules;

import java.time.Duration;
import java.time.Instant;

public class Solver {

    private int[] color;
    private int[] usedColumn;
    private int[][] board;
    private int[][] solution;
    private Duration executionTime;
    private int caseCount;
    private int solutionCount;
    private int delayMs = 30;
    private boolean isOptimized = false;

    public boolean solveQueens(Board Papan)
    {
        color = new int[26];
        usedColumn = new int[Papan.getCol()];
        board = new int[Papan.getRow()][Papan.getCol()];
        solution = new int[Papan.getRow()][Papan.getCol()];
        this.caseCount = 0;
        this.solutionCount = 0;
        
        Instant start = Instant.now();
        solveBruteForce(0, Papan);
        Instant end = Instant.now();
        
        this.executionTime = Duration.between(start, end);
        
        if (solutionCount > 0) {
            for (int i = 0; i < Papan.getRow(); i++) {
                for (int j = 0; j < Papan.getCol(); j++) {
                    board[i][j] = solution[i][j];
                }
            }
            return true;
        }
        return false;
    }

    private void solveBruteForce(int row, Board Papan)
    {
        caseCount++;
        
        if (row == Papan.getRow()) 
        {
            if (this.isOptimized == true)
            {
                solutionCount++;
                for (int i = 0; i < Papan.getRow(); i++)
                {
                    for (int j = 0; j < Papan.getCol(); j++)
                    {
                        solution[i][j] = board[i][j];
                    }
                }
                return;
            }
            else
            {
                if (isBoardValid(Papan.getRow() - 1, Papan))
                {
                    solutionCount++;
                    for (int i = 0; i < Papan.getRow(); i++) 
                    {
                        for (int j = 0; j < Papan.getCol(); j++) 
                        {
                            solution[i][j] = board[i][j];
                        }
                    }
                }
                return;
            }

        }

        for (int col = 0; col < Papan.getCol(); col++)
        {
            if (this.isOptimized == true)
            {
                int warna = Papan.getElmt(row, col) - 'A';
                
                if (usedColumn[col] == 0 && color[warna] == 0 && check8Direction(row, col, Papan))
                {
                    color[warna] = 1;
                    board[row][col] = 1;
                    usedColumn[col] = 1;

                    debugCases(Papan);

                    solveBruteForce(row+1, Papan);

                    color[warna] = 0;
                    board[row][col] = 0;
                    usedColumn[col] = 0;
                }
            }
            else
            {
                board[row][col] = 1;
    
                debugCases(Papan);
    
                solveBruteForce(row+1, Papan);
    
                board[row][col] = 0;
            }
        }
    }

    private boolean check8Direction(int row, int col, Board Papan)
    {
        for (int i = -1; i <= 1; i++) 
        {
            for (int j = -1; j <= 1; j++)
            {
                if (i == 0 && j == 0) continue;

                int newrow = row + i, newcol = col + j;
                if (newrow >= 0 && newrow < Papan.getRow() && newcol >= 0 && newcol < Papan.getCol() && board[newrow][newcol] == 1) return false;
            }
        }
        return true;
    }

    private boolean isBoardValid(int row, Board Papan)
    {
        for (int i = 0; i <= row; i++)
        {
            for (int j = 0; j < Papan.getCol(); j++)
            {
                if (board[i][j] == 1)
                {
                    for (int k = 0; k < i; k++)
                    {
                        if (board[k][j] == 1) return false;
                    }
                    
                    int warna = Papan.getElmt(i, j) - 'A';
                    for (int r = 0; r < i; r++)
                    {
                        for (int c = 0; c < Papan.getCol(); c++)
                        {
                            if (board[r][c] == 1 && (Papan.getElmt(r, c) - 'A') == warna)
                            {
                                return false;
                            }
                        }
                    }
                    
                    if (!check8Direction(i, j, Papan)) return false;
                }
            }
        }
        return true;
    }

    public void printBoard(Board Papan)
    {
        for (int i = 0; i < Papan.getRow(); i++)
        {
            for (int j = 0; j < Papan.getCol(); j++)
            {
                System.out.print(board[i][j] == 1 ? '#' : Papan.getElmt(i, j));
            }
            System.out.println();
        }
    }

    private void debugCases(Board Papan)
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("=== Kasus #" + caseCount + " | Solusi: " + solutionCount + " ===");
        this.printBoard(Papan);
        System.out.flush();
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Duration getExecutionTime() 
    {
        return this.executionTime;
    }

    public int getCaseCount() 
    {
        return this.caseCount;
    }

    public int getSolutionCount() 
    {
        return this.solutionCount;
    }

    public void setDelay(int delayMs) 
    {
        this.delayMs = delayMs;
    }

    public void setOptimized() 
    {
        this.isOptimized = true;
    }
}
