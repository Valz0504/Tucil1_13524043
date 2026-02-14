package stima;

import stima.modules.Board;
import stima.modules.Solver;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Masukkan nama file test case: ");
        String fileName = input.nextLine(), path = "test/";

        File tc = new File(path.concat(fileName));

        if (!tc.exists())
        {
            System.out.println("File tidak ditemukan.");
            input.close();
            return;
        }

        Board Papan = null;
        
        try (Scanner fileReader = new Scanner(tc)) {
            ArrayList<String> lines = new ArrayList<>();
            
            while (fileReader.hasNextLine())
            {
                String data = fileReader.nextLine().trim();
                if (!data.isEmpty()) {
                    lines.add(data);
                }
            }
            
            int row = lines.size();
            int col = 0;
            for (int i = 0; i < row; i++)
            {
                if (lines.get(i).length() > col)
                {
                    col = lines.get(i).length();
                }
            }
            
            Papan = new Board(row, col);
            for (int i = 0; i < row; i++)
            {
                String line = lines.get(i);
                for (int j = 0; j < col && j < line.length(); j++)
                {
                    Papan.setElmt(i, j, line.charAt(j));
                }
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan.");
            input.close();
            return;
        }

        Solver solver = new Solver();
        
        boolean found = solver.solveQueens(Papan);
        
        System.out.println("\n========== HASIL ==========");
        System.out.println("Waktu pencarian: " + solver.getExecutionTime().toMillis() + " ms");
        System.out.println("Banyak kasus yang ditinjau: " + solver.getCaseCount());
        
        if (found)
        {
            System.out.println("\nSolusi:");
            solver.printBoard(Papan);
        }
        else
        {
            System.out.println("Tidak ada solusi\n");
        }
        
        input.close();
    }
}
