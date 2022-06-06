package com.thegostsniper.ArfforniaLauncher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {



		  @Override
		  public void start(Stage primaryStage) throws IOException {
					Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

					primaryStage.setTitle("Arffornia V.4");
					primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/Crafting_Table.png")));

					Scene scene = new Scene(root,1280,720);
					scene.getStylesheets().add(String.valueOf(getClass().getResource("css/style.css")));


					primaryStage.setScene(scene);
					primaryStage.show();


		  }



		  public static void main(String[] args) {
					launch(args);
		  }
}