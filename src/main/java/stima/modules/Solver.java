package stima.modules;

import java.time.Duration;
import java.time.Instant;
import java.util.function.BiConsumer;

public class Solver {

    /**
     * Atribut
     */
    private int[] color;
    private int[] usedColumn;
    private int[][] board;
    private int[][] solution;
    private Duration executionTime;
    private int caseCount;
    private int solutionCount;
    private int delayMs = 30;
    private boolean isOptimized = false;
    private volatile boolean cancelled = false;
    private BiConsumer<int[][], Board> stepBack = null;

    /**
     * Parent function untuk solve queens dengan inisiasi atribut dan pemanggilan prosedur lain
     * 
     * @param Papan
     * @return true jika solusi ada, false jika tidak
     */
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

    /**
     * Algoritma brute force backtracking tanpa heuristik yang mencoba menempatkan satu Queen di setiap baris
     * dengan percobaan untuk menempatkannya di masing-masing kolom, kompleksitasnya O(n^n)
     * 
     * Ini ga ada heuristik atau pruning semacamnya sih kalau menurut saya, dengan mengetahui aturan permainan,
     * better brute force untuk setiap baris, coba tempatkan Queen di masing-masing kolom lalu lanjut ke baris selanjutnya
     * 
     * @param row
     * @param Papan
     */
    private void solveBruteForce(int row, Board Papan)
    {
        if (cancelled) return;
        
        if (this.isOptimized == true) 
        {
            caseCount++;
        }

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
                caseCount++;
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

    /**
     * { Mengecek 8 arah adjacent apakah ada Queen yang ditempatkan atau tidak }
     * 
     * @param row
     * @param col
     * @param Papan
     * 
     * @return true jika di ke 8 arah tidak ada queens yang diletakkan, false jika ada
     */
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

    /**
     * { Mengecek apakah Board merupakan solusi yang valid untuk permainan Queens }
     * 
     * @param row
     * @param Papan
     * 
     * @return true jika Board valid, false jika tidak
     */
    public boolean isBoardValid(int row, Board Papan)
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

    /**
     * Print kondisi papan
     * 
     * @param Papan
     */
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

    /**
     * Live Update Papan
     * 
     * @param Papan
     */
    private void debugCases(Board Papan)
    {
        if (stepBack != null) {
            int[][] snapshot = new int[board.length][board[0].length];
            for (int i = 0; i < board.length; i++) {
                System.arraycopy(board[i], 0, snapshot[i], 0, board[i].length);
            }
            stepBack.accept(snapshot, Papan);
        }

        if (stepBack == null) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Finding solution...");
            System.out.flush();
            this.printBoard(Papan);
            System.out.flush();
        }

        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            cancelled = true;
        }
    }

    /**
     * Getter execution time
     * 
     * @return
     */
    public Duration getExecutionTime() 
    {
        return this.executionTime;
    }

    /**
     * Getter case count
     * 
     * @return
     */
    public int getCaseCount() 
    {
        return this.caseCount;
    }

    /**
     * Getter solution count
     * 
     * @return
     */
    public int getSolutionCount() 
    {
        return this.solutionCount;
    }

    /**
     * Setter delayMs
     * 
     * @param delayMs
     */
    public void setDelay(int delayMs) 
    {
        this.delayMs = delayMs;
    }

    /**
     * Setter isOptimized
     * 
     * @return
     */
    public void setOptimized() 
    {
        this.isOptimized = true;
    }

    /**
     * Getter Board
     * 
     * @return
     */
    public int[][] getBoard()
    {
        return this.board;
    }

    /**
     * Set callback yang dipanggil setiap langkah solver
     * 
     * @param callback menerima (int[][] boardSnapshot, Board papan)
     */
    public void setStepBack(BiConsumer<int[][], Board> callback)
    {
        this.stepBack = callback;
    }

    /**
     * Cancel solver yang sedang berjalan
     */
    public void cancel()
    {
        this.cancelled = true;
    }

    /**
     * Cek apakah solver sudah di-cancel
     */
    public boolean isCancelled()
    {
        return this.cancelled;
    }
}
