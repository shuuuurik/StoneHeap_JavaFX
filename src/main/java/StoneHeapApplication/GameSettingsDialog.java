package StoneHeapApplication;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameSettingsDialog {
    private Stage dialog;
    private ComboBox<String> whoPlay;
    private ComboBox<String> howToWin;
    private Font font;
    private GridPane root;
    private ButtonType result = ButtonType.CANCEL;

    private void createComboBox1() {
        Label playWith = new Label("Play with:");
        playWith.setFont(font);
        root.add(playWith, 0, 0);
        whoPlay = new ComboBox<>();
        whoPlay.setStyle("-fx-font-size: 24 pt");
        whoPlay.getItems().addAll(
                "Player",
                "Computer"
        );
        whoPlay.setValue("Player");
        root.add(whoPlay, 1, 0);
    }

    private void createComboBox2() {
        Label wayToWin = new Label("Way to win:");
        wayToWin.setFont(font);
        root.add(wayToWin, 0, 1);
        howToWin = new ComboBox<>();
        howToWin.setStyle("-fx-font-size: 24 pt");
        howToWin.getItems().addAll(
                "Pick up the last stone",
                "Leave the last stone to the opponent"
        );
        howToWin.setValue("Pick up the last stone");
        root.add(howToWin, 1, 1);
    }

    private void createButton() {
        Button btnOk = new Button("Ok");
        btnOk.setFont(Font.font(24));
        root.add(btnOk, 1, 2);
        btnOk.setOnAction((ActionEvent e) -> {
                handleOk();
        });
    }

    public GameSettingsDialog() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Select game settings");
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        font = Font.font("Tahoma", FontWeight.NORMAL, 25);
        createComboBox1();
        createComboBox2();
        createButton();

        Scene scene = new Scene(root, 700, 500);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private boolean isInputValid() {
        return whoPlay.getValue().length() > 0  && howToWin.getValue().length() > 0;
    }

    private void handleOk() {
        result = ButtonType.OK;
        dialog.close();
    }

    public ButtonType getResult() {
        return result;
    }
    public String getSecondPlayer(){
        return whoPlay.getValue();
    }
    public int getWayToWin(){
        if ( howToWin.getValue() == "Pick up the last stone")
            return 1;
        else return 2;
    }
}
