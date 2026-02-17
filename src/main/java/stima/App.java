package stima;

import stima.gui.Page;

// import stima.modules.Board;
// import stima.modules.Solver;
// import java.util.Scanner;
// import java.util.ArrayList;
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.io.FileWriter;
// import java.io.PrintWriter;

public class App {
    public static void main(String[] args) {

        // gui
        Page.main(args);

        // cli
        /*
        Scanner input = new Scanner(System.in);

        System.out.println("Masukkan nama file test case: ");
        String fileName = input.nextLine(), path = "test/input/";

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

            boolean validBoard = true;
            for (int i = 0; i < row; i++)
            {
                if (lines.get(i).length() != col) validBoard = false;
            }

            if (!validBoard)
            {
                System.out.println("Ukuran papan tidak benar!");
                return;
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

        System.out.println("Optimized atau tidak? (yes/no)");
        String optimized = input.nextLine();
        if (optimized.equalsIgnoreCase("yes"))
        {
            solver.setOptimized();
        }
        
        boolean found = solver.solveQueens(Papan);
        
        System.out.println("\n========== HASIL ==========");
        System.out.println("Waktu pencarian: " + solver.getExecutionTime().toMillis() + " ms");
        System.out.println("Banyak kasus yang ditinjau: " + solver.getCaseCount());
        
        if (found)
        {
            System.out.println("\nSolusi:");
            solver.printBoard(Papan);

            System.out.println();
            System.out.println("Apakah Anda ingin menyimpan solusi? (yes/no)");
            String keep = input.nextLine();
            if (keep.equalsIgnoreCase("yes"))
            {
                try {
                    String outputFileName = "test/output/output_" + fileName;
                    File output = new File(outputFileName);
                    output.getParentFile().mkdirs();
                    
                    PrintWriter writer = new PrintWriter(new FileWriter(output, false));
    
                    for (int i = 0; i < Papan.getRow(); i++)
                    {
                        for (int j = 0; j < Papan.getCol(); j++)
                        {
                            writer.print(solver.getBoard()[i][j] == 1 ? '#' : Papan.getElmt(i, j));
                        }
                        writer.println();
                    }
                    
                    writer.close();
                    System.out.println("\nOutput solusi disimpan di: " + outputFileName);
                    
                } catch (IOException e) {
                    System.out.println("Ada error saat menyimpan file :(");
                    e.printStackTrace();
                }
            }
        }
        else
        {
            System.out.println("Tidak ada solusi\n");
        }
        
        input.close();
        */
    }
}
