package com.lx.musicdump;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("音乐格式转换工具");
//        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setScene(new Scene(root, 1024, 768));
//        primaryStage.getIcons().add(new Image("/icon/baseline_library_music_black_18dp.png"));
//        primaryStage.getIcons().add(new Image("/icon/baseline_library_music_black_24dp.png"));
//        primaryStage.getIcons().add(new Image("/icon/baseline_library_music_black_36dp.png"));
        primaryStage.getIcons().add(new Image("/icon/baseline_library_music_black_48dp.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
