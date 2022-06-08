package com.thegostsniper.ArfforniaLauncher;

import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;



public class MainApp extends Application {

		  private Saver saver;
		  private static MainApp instance;


		  public MainApp() throws IOException {
					instance = this;

					String dataLocate = (System.getenv("APPDATA") + "\\Arffornia_V.4");
					File dossier = new File(dataLocate);
					dossier.mkdir();

					String saverLocate = (dataLocate + "\\launcher.properties");
					File configProperties = new File(saverLocate);

					configProperties.createNewFile();



					saver = new Saver(Paths.get(saverLocate));
					saver.load();
		  }


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



		  public Saver getSaver() {
					return saver;
		  }

		  public static MainApp getInstance(){
					return instance;
		  }



		  public static void main(String[] args) {
					launch(args);
		  }
}