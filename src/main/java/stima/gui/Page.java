package stima.gui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.stage.Stage;

import stima.modules.Board;
import stima.modules.Solver;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Page extends Application {

    /**
     * Atribut
     */
    private static final Color[] REGION_COLORS = 
    {
        Color.web("#ff6d6d"),
        Color.web("#cdd1ff"), 
        Color.web("#45B7D1"),
        Color.web("#8785ff"),
        Color.web("#cfb253"),
        Color.web("#ff99ff"),
        Color.web("#8ce4ce"), 
        Color.web("#F7DC6F"), 
        Color.web("#ddd2ff"), 
        Color.web("#ffe2cb"), 
        Color.web("#ffc089"), 
        Color.web("#82E0AA"), 
        Color.web("#F1948A"), 
        Color.web("#AED6F1"), 
        Color.web("#D7BDE2"), 
        Color.web("#ff8bda"), 
        Color.web("#FAD7A0"), 
        Color.web("#afdfff"), 
        Color.web("#f3ff72"), 
        Color.web("#a2ffd5"), 
        Color.web("#cff18f"), 
        Color.web("#c6dffc"), 
        Color.web("#cfffd1"), 
        Color.web("#46d36b"), 
        Color.web("#ffbdbd"), 
        Color.web("#62ffda"), 
    };
    private Board papan;
    private Solver solver;
    private GridPane boardGrid;
    private Label statusLabel;
    private Label timeLabel;
    private Label caseLabel;
    private CheckBox optimizedCheck;
    private Slider delaySlider;
    private Label delayValueLabel;
    private Button solveBtn;
    private Button stopBtn;
    private Button loadBtn;
    private Button saveBtn;
    private Button saveImageBtn;
    private Thread solverThread;
    private boolean solving;
    private StackPane boardContainer;

    @Override
    public void start(Stage primaryStage) 
    {
        primaryStage.setTitle("Queens Linkedin Solver");

        try 
        {
            if (java.awt.Taskbar.isTaskbarSupported()) 
            {
                java.awt.Taskbar taskbar = java.awt.Taskbar.getTaskbar();
                java.awt.Image dockIcon = ImageIO.read(getClass().getResourceAsStream("/assets/Queens.png"));
                taskbar.setIconImage(dockIcon);
            }
        } catch (Exception ignored) {}

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #0a1824;");

        Text title = new Text("Solver");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        title.setFill(Color.WHITE);
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 15, 0));
        root.setTop(titleBox);

        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(2);
        boardGrid.setVgap(2);
        boardGrid.setPadding(new Insets(10));
        boardGrid.setStyle("-fx-background-color: #162839; -fx-background-radius: 15;");

        boardContainer = new StackPane(boardGrid);
        boardContainer.setPadding(new Insets(10));
        root.setCenter(boardContainer);

        VBox controlPanel = new VBox(12);
        controlPanel.setPadding(new Insets(10, 10, 10, 20));
        controlPanel.setAlignment(Pos.TOP_CENTER);
        controlPanel.setPrefWidth(220);

        loadBtn = new Button("Load File");
        loadBtn.setPrefWidth(160);
        loadBtn.setStyle(buttonStyle("#69b5e8"));
        loadBtn.setOnAction(e -> loadFile(primaryStage));

        optimizedCheck = new CheckBox("Optimized");
        optimizedCheck.setPadding(new Insets(10, 0, 10, 0));
        optimizedCheck.setTextFill(Color.WHITE);
        optimizedCheck.setFont(Font.font("Georgia", 14));

        Label delayLabel = new Label("Delay (ms):");
        delayLabel.setPadding(new Insets(0, 0, -10, 0));
        delayLabel.setTextFill(Color.WHITE);
        delayLabel.setFont(Font.font("Georgia", 12));

        delaySlider = new Slider(0, 100, 5);
        delaySlider.setPrefWidth(160);
        delaySlider.setShowTickLabels(true);
        delaySlider.setShowTickMarks(true);
        delaySlider.setMajorTickUnit(20);
        delaySlider.setBlockIncrement(10);

        delayValueLabel = new Label("5 ms");
        delayValueLabel.setPadding(new Insets(-10, 0, 20, 0));
        delayValueLabel.setTextFill(Color.LIGHTGRAY);
        delayValueLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 11));
        delaySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            delayValueLabel.setText(newVal.intValue() + " ms");
        });

        solveBtn = new Button("Solve");
        solveBtn.setPrefWidth(160);
        solveBtn.setStyle(buttonStyle("#44c87b"));
        solveBtn.setOnAction(e -> solve());

        stopBtn = new Button("Stop");
        stopBtn.setPrefWidth(160);
        stopBtn.setStyle(buttonStyle("#E74C3C"));
        stopBtn.setDisable(true);
        stopBtn.setOnAction(e -> stopSolving());

        saveBtn = new Button("Save as Text");
        saveBtn.setPrefWidth(160);
        saveBtn.setStyle(buttonStyle("#E67E22"));
        saveBtn.setOnAction(e -> saveSolution(primaryStage));

        saveImageBtn = new Button("Save as Image");
        saveImageBtn.setPrefWidth(160);
        saveImageBtn.setStyle(buttonStyle("#9B59B6"));
        saveImageBtn.setOnAction(e -> saveAsImage(primaryStage));

        Separator sep = new Separator();

        statusLabel = new Label("No files uploaded");
        statusLabel.setTextFill(Color.LIGHTGRAY);
        statusLabel.setWrapText(true);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setTextAlignment(TextAlignment.CENTER);
        statusLabel.setFont(Font.font("Georgia", FontWeight.BLACK, 13));

        timeLabel = new Label("Time: -");
        timeLabel.setTextFill(Color.LIGHTGRAY);
        timeLabel.setAlignment(Pos.CENTER);
        timeLabel.setFont(Font.font("Georgia", 12));

        caseLabel = new Label("Case reviewed: -");
        caseLabel.setTextFill(Color.LIGHTGRAY);
        caseLabel.setAlignment(Pos.CENTER);
        caseLabel.setFont(Font.font("Georgia", 12));

        controlPanel.getChildren().addAll(
            loadBtn, optimizedCheck,
            delayLabel, delaySlider, delayValueLabel,
            solveBtn, stopBtn, saveBtn, saveImageBtn,
            sep, statusLabel, timeLabel, caseLabel
        );
        root.setRight(controlPanel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }

    /**
     * Load file test case
     */
    private void loadFile(Stage stage) 
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a test case file");
        fileChooser.setInitialDirectory(new File("test/input"));
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file == null) return;

        try (Scanner fileReader = new Scanner(file)) 
        {
            ArrayList<String> lines = new ArrayList<>();
            while (fileReader.hasNextLine()) 
            {
                String data = fileReader.nextLine().trim();
                if (!data.isEmpty()) 
                {
                    lines.add(data);
                }
            }

            int row = lines.size();
            int col = 0;
            for (String line : lines) 
            {
                if (line.length() > col) col = line.length();
            }

            for (String line : lines) 
            {
                if (line.length() != col) 
                {
                    showAlert("Error", "Board's size not consistent!");
                    return;
                }
            }

            papan = new Board(row, col);
            for (int i = 0; i < row; i++) 
            {
                String line = lines.get(i);
                for (int j = 0; j < col && j < line.length(); j++) 
                {
                    papan.setElmt(i, j, line.charAt(j));
                }
            }

            solver = null;
            renderBoard(null);
            statusLabel.setText("File loaded: " + file.getName());
            timeLabel.setText("Time: -");
            caseLabel.setText("Case reviewed: -");

        } catch (FileNotFoundException e) 
        {
            showAlert("Error", "File not found.");
        }
    }

    /**
     * Solve Queens Board
     */
    private void solve() 
    {
        if (papan == null) 
        {
            showAlert("Error", "No files uploaded!");
            return;
        }

        solving = true;
        solveBtn.setDisable(true);
        loadBtn.setDisable(true);
        saveBtn.setDisable(true);
        saveImageBtn.setDisable(true);
        optimizedCheck.setDisable(true);
        stopBtn.setDisable(false);

        solver = new Solver();
        solver.setDelay((int) delaySlider.getValue());

        if (optimizedCheck.isSelected()) 
        {
            solver.setOptimized();
        }

        solver.setStepBack((boardSnapshot, board) -> 
        {
            Platform.runLater(() -> 
            {
                renderBoard(boardSnapshot);
                caseLabel.setText("Case: " + solver.getCaseCount());
            });
        });

        statusLabel.setText("Finding solution...");
        timeLabel.setText("Time: ...");
        caseLabel.setText("Case: 0");

        solverThread = new Thread(() -> 
        {
            boolean found = solver.solveQueens(papan);

            Platform.runLater(() -> {
                solving = false;
                solveBtn.setDisable(false);
                loadBtn.setDisable(false);
                saveBtn.setDisable(false);
                saveImageBtn.setDisable(false);
                optimizedCheck.setDisable(false);
                stopBtn.setDisable(true);

                if (solver.isCancelled()) 
                {
                    statusLabel.setText("Search terminated.");
                    timeLabel.setText("Time: " + solver.getExecutionTime().toMillis() + " ms");
                    caseLabel.setText("Case reviewed: " + solver.getCaseCount());
                } 
                else 
                {
                    timeLabel.setText("Time: " + solver.getExecutionTime().toMillis() + " ms");
                    caseLabel.setText("Case reviewed: " + solver.getCaseCount());

                    if (found) 
                    {
                        statusLabel.setText("Solved!");
                        renderBoard(solver.getBoard());
                        showSolvePopUp("Solved!", "#44c87b");
                    } 
                    else 
                    {
                        statusLabel.setText("No solution.");
                        renderBoard(null);
                        showAlert("No Solution", "No solution found for this board configuration.");
                    }
                }
            });
        });
        solverThread.setDaemon(true);
        solverThread.start();
    }

    /**
     * Menghentikan solver yang sedang berjalan
     */
    private void stopSolving() 
    {
        if (solver != null) 
        {
            solver.cancel();
        }
        if (solverThread != null) 
        {
            solverThread.interrupt();
        }
        statusLabel.setText("Stopping...");
    }

    /**
     * Render board ke GridPane
     */
    private void renderBoard(int[][] solution) 
    {
        boardGrid.getChildren().clear();
        if (papan == null) return;

        int rows = papan.getRow();
        int cols = papan.getCol();

        int cellSize = Math.max(30, Math.min(60, 500 / Math.max(rows, cols)));

        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                char c = papan.getElmt(i, j);
                int colorIndex = c - 'A';
                if (colorIndex < 0 || colorIndex >= REGION_COLORS.length) colorIndex = 0;

                Color bgColor = REGION_COLORS[colorIndex];
                boolean isQueen = (solution != null && solution[i][j] == 1);

                StackPane cell = new StackPane();
                cell.setPrefSize(cellSize, cellSize);
                cell.setMinSize(cellSize, cellSize);

                String bgHex = toHex(bgColor);
                cell.setStyle(
                    "-fx-background-color: " + bgHex + ";" +
                    "-fx-background-radius: 4;" +
                    "-fx-border-color: #2C3E50;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 4;"
                );

                if (isQueen) 
                {
                    Text queenText = new Text("\u265B"); 
                    queenText.setFont(Font.font("Georgia", FontWeight.BOLD, cellSize * 0.6));
                    queenText.setFill(Color.web("#2C3E50"));
                    cell.getChildren().add(queenText);
                }

                boardGrid.add(cell, j, i);
            }
        }
    }

    /**
     * Save solusi ke file
     */
    private void saveSolution(Stage stage) 
    {
        if (papan == null) 
        {
            showAlert("Error", "No solution to be saved!");
            return;
        }

        if (solver.getSolutionCount() == 0)
        {
            showAlert("Error", "Board is not fully solved!");
            return;
        }

        if (!solver.isBoardValid(papan.getRow() - 1, papan))
        {
            showAlert("Error", "Solution is not valid!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save solution");
        fileChooser.setInitialDirectory(new File("test/output"));
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialFileName("text.txt");

        File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) 
        {
            int[][] board = solver.getBoard();
            for (int i = 0; i < papan.getRow(); i++) 
            {
                for (int j = 0; j < papan.getCol(); j++) 
                {
                    writer.print(board[i][j] == 1 ? '#' : papan.getElmt(i, j));
                }
                writer.println();
            }
            statusLabel.setText("Solution saved as: \n" + file.getName());
        } catch (IOException e) 
        {
            showAlert("Error", "Failed to save file: " + e.getMessage());
        }
    }

    /**
     * Save board sebagai image (PNG)
     */
    private void saveAsImage(Stage stage) 
    {
        if (papan == null) 
        {
            showAlert("Error", "No solution to be saved!");
            return;
        }

        if (solver.getSolutionCount() == 0)
        {
            showAlert("Error", "Board is not fully solved!");
            return;
        }

        if (!solver.isBoardValid(papan.getRow() - 1, papan))
        {
            showAlert("Error", "Solution is not valid!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as image");
        fileChooser.setInitialDirectory(new File("test/output"));
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG Image", "*.png")
        );
        fileChooser.setInitialFileName("image.png");

        File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;

        try 
        {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.web("#34495E"));
            WritableImage image = boardGrid.snapshot(params, null);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            statusLabel.setText("Image saved as: " + file.getName());
        } catch (IOException e) 
        {
            showAlert("Error", "Failed to save image: " + e.getMessage());
        }
    }


    /**
     * { Styling button }
     * 
     * @param color
     * 
     * @return
     */
    private String buttonStyle(String color) 
    {
        return "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Georgia';" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 15;" +
            "-fx-cursor: hand;";
    }

    private String toHex(Color color) 
    {
        return String.format("#%02X%02X%02X",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255));
    }

    /**
     * { Menampilkan pop up alert }
     * 
     * @param title
     * @param message
     * 
     * @return
     */
    private void showAlert(String title, String message) 
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Notifikasi pop up muncul ketika solved
     */
    private void showSolvePopUp(String message, String bgColor) 
    {
        Label popup = new Label(message);
        popup.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Georgia';" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 24;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 3);"
        );
        popup.setOpacity(0);

        StackPane.setAlignment(popup, Pos.TOP_CENTER);
        StackPane.setMargin(popup, new Insets(15, 0, 0, 0));
        boardContainer.getChildren().add(popup);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), popup);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition stay = new PauseTransition(Duration.seconds(3));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), popup);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition seq = new SequentialTransition(fadeIn, stay, fadeOut);
        seq.setOnFinished(e -> boardContainer.getChildren().remove(popup));
        seq.play();
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}
