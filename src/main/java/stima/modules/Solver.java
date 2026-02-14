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
    private int updateInterval = 20;
    private int delayMs = 100;

    public boolean solveQueens(Board Papan)
    {
        // asumsi warnanya sampe 26 kan kak hehe
        this.color = new int[26];
        this.usedColumn = new int[Papan.getCol()];
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
            solutionCount++;
            for (int i = 0; i < Papan.getRow(); i++) {
                for (int j = 0; j < Papan.getCol(); j++) {
                    solution[i][j] = board[i][j];
                }
            }
            return;
        }

        for (int col = 0; col < Papan.getCol(); col++)
        {
            int warna = Papan.getElmt(row, col) - 'A';

            if (usedColumn[col] == 0 && color[warna] == 0 && check8Direction(row, col, Papan))
            {
                board[row][col] = 1;
                usedColumn[col] = 1;
                color[warna] = 1;

                if (caseCount % updateInterval == 0) {
                    System.out.println("\n=== Kasus #" + caseCount + " | Solusi: " + solutionCount + " ===");
                    this.printBoard(Papan);
                    
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                solveBruteForce(row+1, Papan);

                board[row][col] = 0;
                usedColumn[col] = 0;
                color[warna] = 0;
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

    public Duration getExecutionTime() {
        return this.executionTime;
    }

    public int getCaseCount() {
        return this.caseCount;
    }

    public int getSolutionCount() {
        return this.solutionCount;
    }

    public void setUpdateInterval(int interval) {
        this.updateInterval = interval;
    }

    public void setDelay(int delayMs) {
        this.delayMs = delayMs;
    }
}
