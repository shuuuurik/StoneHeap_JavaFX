package StoneHeapApplication;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.util.Random;

public class Main extends Application {
    private VBox vbox;
    private Heap heap;
    private ViewHeap viewHeap;
    private Label number_of_stones;
    private Label stepBy;
    private Label wayToWin;
    private Label showDeletedStones;
    private Button pass;
    private Circle [] circles;
    Font font = Font.font("Tahoma", FontWeight.NORMAL, 30);
    private String whoPlay;
    private int howToWin;
    private int deletedStones = 0;
    private String errorMessage="";

    private  void showMessage(String message) {
        Alert messageAlert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        messageAlert.showAndWait();
    }

    private void createSceneElements() {
        vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0, 10, 0, 10));
        createStartButtons();
    }

    void createStartButtons(){
        Button start = new Button("Start new game");
        start.setFont(font);
        start.setOnAction((ActionEvent e) -> {
            vbox.getChildren().clear();
            chooseSettings();
            initNewGame();
        });
        Button load = new Button("Load saved game");
        load.setFont(font);
        load.setOnAction((ActionEvent e) -> {
            readDataFromFile(new File("data.txt"));
                if (errorMessage == "") {
                    vbox.getChildren().clear();
                    initLoadedGame();
                }
                else
                    showMessage("No saved games");
        });
        vbox.getChildren().addAll(start, load);
    }

    void chooseSettings(){
        GameSettingsDialog gameSettingsDialog = new GameSettingsDialog();
        whoPlay = gameSettingsDialog.getSecondPlayer();
        howToWin = gameSettingsDialog.getWayToWin();
    }

    void initNewGame() {
        Random initArray = new Random();
        int n = initArray.nextInt(90) + 10;
        heap = new Heap(n);
        viewHeap = new ViewHeap(heap);
        number_of_stones = new Label("Stones: " + n);
        number_of_stones.setFont(font);
        stepBy = new Label();
        stepBy.setFont(font);
        if (howToWin == 1)
            wayToWin = new Label("Way to win: Pick up the last stone");
        else
            wayToWin = new Label("Way to win: Leave the last stone to the opponent");
        wayToWin.setFont(font);
        showDeletedStones = new Label("Deleted stones per step: "+ 0);
        showDeletedStones.setFont(font);
        vbox.getChildren().addAll(number_of_stones, stepBy, wayToWin, showDeletedStones, viewHeap.getPane());
        pass = new Button("Pass the move to the opponent");
        pass.setFont(font);
        vbox.getChildren().add(pass);
        circles = viewHeap.getCircles();
        for (Circle circle: circles){
            circle.setOnMouseClicked((event) -> {
                if (deletedStones < 4)
                    deleteSelectedStone(circle);
                else
                    showMessage("You can't take more than 4 stones per step");
            });
        }
        startNewGame();
    }

    void initLoadedGame(){
        viewHeap = new ViewHeap(heap);
        number_of_stones = new Label("Stones: " + heap.getCurrentNumber());
        number_of_stones.setFont(font);
        stepBy.setFont(font);
        if (howToWin == 1)
            wayToWin = new Label("Way to win: Pick up the last stone");
        else
            wayToWin = new Label("Way to win: Leave the last stone to the opponent");
        wayToWin.setFont(font);
        showDeletedStones = new Label("Deleted stones per step: "+ deletedStones);
        showDeletedStones.setFont(font);
        vbox.getChildren().addAll(number_of_stones, stepBy, wayToWin, showDeletedStones, viewHeap.getPane());
        pass = new Button("Pass the move to the opponent");
        pass.setFont(font);
        vbox.getChildren().add(pass);
        circles = viewHeap.getCircles();
        for (Circle circle: circles){
            circle.setOnMouseClicked((event) -> {
                if (deletedStones < 4)
                    deleteSelectedStone(circle);
                else
                    showMessage("You can't take more than 4 stones per step");
            });
        }
        startLoadedGame();
    }

    void startLoadedGame(){
        if (whoPlay.equals("Computer")) {
            pass.setOnAction((ActionEvent e) -> {
                if (deletedStones == 0)
                    showMessage("You should take at least 1 stone per step");
                else {
                    if (howToWin == 1 && heap.getCurrentNumber() % 5 != 0)
                        computerStep(heap.getCurrentNumber() % 5);
                    else if (howToWin == 2 && heap.getCurrentNumber() % 5 != 1)
                        if (heap.getCurrentNumber() % 5 == 0)
                            computerStep(4);
                        else
                            computerStep(heap.getCurrentNumber() % 5 - 1);
                }
            });
        }
        else if (/*whoPlay.equals("Player") &&*/ stepBy.getText().equals("Step by: Player1"))
            pass.setOnAction((ActionEvent e) -> {
                if (deletedStones == 0)
                    showMessage("You should take at least 1 stone per step");
                else
                    player2Step();
            });
        else if (/*whoPlay.equals("Player") &&*/ stepBy.getText().equals("Step by: Player2"))
            pass.setOnAction((ActionEvent e) -> {
                if (deletedStones == 0)
                    showMessage("You should take at least 1 stone per step");
                else
                    player1Step();
            });
    }

    void deleteSelectedStone(Circle cir){
        cir.setVisible(false);
        for (int i = 0; i < heap.getNumber(); ++i){
            if (heap.getStone(i) == 1 && !circles[i].isVisible()){
                deletedStones++;
                heap.deleteStone(i);
                break;
            }
        }
        number_of_stones.setText("Stones: " + heap.getCurrentNumber());
        showDeletedStones.setText("Deleted stones per step: "+ deletedStones);
    }

    void startNewGame(){
        if (whoPlay == "Computer" && howToWin == 1 && heap.getCurrentNumber() % 5 == 0)
            player1Step();
        else if (whoPlay == "Computer" && howToWin == 1 && heap.getCurrentNumber() % 5 != 0)
            computerStep(heap.getCurrentNumber() % 5);
        else if (whoPlay == "Computer" && howToWin == 2 && heap.getCurrentNumber() % 5 == 1)
            player1Step();
        else if (whoPlay == "Computer" && howToWin == 2 && heap.getCurrentNumber() % 5 != 1)
            if (heap.getCurrentNumber() % 5 ==0)
                computerStep(4);
            else
                computerStep(heap.getCurrentNumber() % 5 - 1);
        else
            player1Step();
    };

    void computerStep(int n){
        int count = 0;
        for (int i = 0; i < heap.getNumber(); ++i){
            if (count < n)
                if (heap.getStone(i) == 1){
                    heap.deleteStone(i);
                    circles[i].setVisible(false);
                    count++;
                }
        }
        player1Step();
    }
    void player1Step(){
        deletedStones = 0;
        stepBy.setText("Step by: Player1");
        number_of_stones.setText("Stones: " + heap.getCurrentNumber());
        showDeletedStones.setText("Deleted stones per step: "+ 0);
        if (whoPlay.equals("Computer") && (howToWin == 1 && heap.getCurrentNumber() == 0 ||
                howToWin == 2 && heap.getCurrentNumber() == 1)) {
            vbox.getChildren().clear();
            showMessage("Computer won");
        }
        else if (howToWin == 1 && heap.getCurrentNumber() == 0 ||
                howToWin == 2 && heap.getCurrentNumber() <= 1) {
            vbox.getChildren().clear();
            showMessage("Player 2 won");
        }
        pass.setOnAction((ActionEvent e) -> {
            if (deletedStones == 0)
                showMessage("You should take at least 1 stone per step");
            else {
                if (whoPlay.equals("Computer") && howToWin == 1 && heap.getCurrentNumber() % 5 != 0)
                    computerStep(heap.getCurrentNumber() % 5);
                else if (whoPlay.equals("Computer") && howToWin == 2 && heap.getCurrentNumber() % 5 != 1)
                    if (heap.getCurrentNumber() % 5 ==0)
                        computerStep(4);
                    else
                        computerStep(heap.getCurrentNumber() % 5 - 1);
            else player2Step();
            }
        });
    }

    void player2Step() {
        deletedStones = 0;
        stepBy.setText("Step by: Player2");
        number_of_stones.setText("Stones: " + heap.getCurrentNumber());
        showDeletedStones.setText("Deleted stones per step: "+ 0);
        if (howToWin == 1 && heap.getCurrentNumber() == 0 ||
                howToWin == 2 && heap.getCurrentNumber() <= 1) {
            vbox.getChildren().clear();
            showMessage("Player 1 won");
        }
        pass.setOnAction((ActionEvent e) -> {
            if (deletedStones == 0)
                showMessage("You should take at least 1 stone per step");
            else
                player1Step();
        });
    }

    private void saveDataToFile(File dataFile) {
        try {
            FileWriter out = new FileWriter(dataFile);
            out.write(whoPlay + " " + stepBy.getText() + " " + howToWin + " " + deletedStones +
                    " " + heap.getNumber() + " " + heap.getCurrentNumber() + "\n" + "\n");
            for (int i = 0; i < heap.getNumber(); ++i)
                out.write(heap.getStone(i) + " ");
            out.close();
        } catch (IOException e){
            showMessage(e.getMessage());
        }
    }
    private void readDataFromFile(File dataFile) {
        try {
            errorMessage = "";
            BufferedReader in = new BufferedReader(new FileReader(dataFile));
            String str;
            String[] dataArray;
            while ((str = in.readLine()) != null) {
                try {
                    if (str.isEmpty()) break;
                    dataArray = str.split(" +");
                    if (dataArray.length != 8)
                        throw new Exception("wrong data");
                    whoPlay = dataArray[0];
                    stepBy = new Label(dataArray[1] + " " + dataArray[2] + " " + dataArray[3]);
                    howToWin = Integer.parseInt(dataArray[4]);
                    deletedStones = Integer.parseInt(dataArray[5]);
                    heap = new Heap(Integer.parseInt(dataArray[6]));
                    heap.setCurrentNumber(Integer.parseInt(dataArray[7]));
                } catch (Exception e) {
                    errorMessage += e.getMessage() + "\n";
                    in.close();
                }
            }
            String[] s;
            int[] a = new int[101];
            int j = 0;
            while ((str = in.readLine()) != null) {
                s = str.split(" +");
                for (int i = 0; i < s.length; ++i)
                    a[j + i] = Integer.parseInt(s[i]);
                j += s.length;
            }
            heap.setStones(a);
            in.close();
        } catch (IOException ex) {
            errorMessage += ex.getMessage() + "\n";
        }
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("_File");

        MenuItem save = new MenuItem("Save");
        save.setOnAction((ActionEvent event) -> saveDataToFile(new File("data.txt")));

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction((ActionEvent event) -> Platform.exit());

        fileMenu.getItems().addAll(save, new SeparatorMenuItem(), exit);
        return fileMenu;
    }

    @Override
    public void start(Stage primaryStage)  {
        primaryStage.setTitle("Heap of stones");
        BorderPane root = new BorderPane();
        root.setStyle("-fx-font-size: 18 pt");
        root.setPadding(new Insets(5));

        createSceneElements();

        root.setTop(new MenuBar(createFileMenu()));
        root.setCenter(vbox);

        Scene scene = new Scene(root, 800, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
